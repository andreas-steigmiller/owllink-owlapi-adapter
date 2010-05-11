/*
 * Copyright (C) 2009, Ulm University
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.semanticweb.owlapi.owllink;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.owllink.builtin.requests.*;
import org.semanticweb.owlapi.owllink.builtin.response.*;
import org.semanticweb.owlapi.owllink.retraction.RetractRequest;
import org.semanticweb.owlapi.owllink.server.response.ErrorResponse;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.reasoner.impl.*;
import org.semanticweb.owlapi.util.Version;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * <code>OWLlinkHTTPXMLReasoner</code> is an implementation of <code>OWLlinkReasoner</code> that uses XML over
 * HTTP as binding protocol. <p/>
 * It behaves as a normal OWLReasoner, i.e., changes on the root ontology will be transfered to the OWLlinkServer
 * via OWLlink. For that a knowledge base (see {@link #getDefaultKB}) will be created that contains all
 * axioms of the root ontology and its import closure. Calling methods of OWLReasoner will always be executed
 * with respect to that OWLlink knowledge base. In addition, additional knowledge bases can be created directly
 * over OWLlink (CreateKB requests) and all OWLlink requests (either on the knowledge base related to the
 * root ontolgoy or on any other knowledge base managed by the OWLlink server) are supported.
 * <p/>
 *
 * @author Olaf Noppens
 */
public class OWLlinkHTTPXMLReasoner extends OWLReasonerBase implements OWLlinkReasoner {

    protected IRI defaultKnowledgeBase;
    protected PrefixManagerProvider prov = new DefaultPrefixManagerProvider();
    private URL reasonerURL;
    Description description;
    OWLOntologyManager manager;
    HTTPSessionImpl session;

    public OWLlinkHTTPXMLReasoner(OWLOntology rootOntology, OWLlinkReasonerConfiguration configuration, BufferingMode bufferingMode) {
        super(rootOntology, configuration, bufferingMode);
        session = new HTTPSessionImpl(manager, configuration.getReasonerURL(), prov);
        this.reasonerURL = configuration.getReasonerURL();
        createDefaultKB();
        this.description = getReasonerInfo();
        this.manager = rootOntology.getOWLOntologyManager();
        flush();
    }

    public String getReasonerName() {
        return this.description.getName();
    }

    public Version getReasonerVersion() {
        return new org.semanticweb.owlapi.util.Version(description.getReasonerVersion().getMajor(),
                description.getReasonerVersion().getMinor(), 0, 0);
    }

    public boolean isEntailmentCheckingSupported(AxiomType<?> axiomType) {
        return true;
    }

    public void interrupt() {
    }

    public IRI getDefaultKB() {
        return this.defaultKnowledgeBase;
    }

    public <R extends Response> R answer(Request<R> request) {
        return performRequest(request);
    }

    public ResponseMessage answer(Request... request) {
        return performRequests(request);
    }

    @Override
    protected void handleChanges(Set<OWLAxiom> addAxioms, Set<OWLAxiom> removeAxioms) {
        if (removeAxioms.isEmpty()) {
            Tell tell = new Tell(defaultKnowledgeBase, addAxioms);
            performRequest(tell);
        } else {
            if (supportsRetraction()) {
                RetractRequest retraction = new RetractRequest(defaultKnowledgeBase, removeAxioms);
                performRequest(retraction);
                if (!addAxioms.isEmpty()) {
                    Tell tell = new Tell(defaultKnowledgeBase, addAxioms);
                    ResponseMessage message = performRequests(tell);
                }

            } else {
                ReleaseKB release = new ReleaseKB(defaultKnowledgeBase);
                performRequest(release);
                CreateKB createKB = new CreateKB(defaultKnowledgeBase);
                performRequest(createKB);
                Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
                axioms.addAll(getReasonerAxioms());
                if (axioms.size() > 0) {
                    Tell tell = new Tell(defaultKnowledgeBase, axioms);
                    performRequest(tell);
                }
            }
        }
    }

    private boolean supportsRetraction() {
        return this.description.getSupportedExtensions().contains(RetractRequest.EXTENSION_IRI);
    }

    public boolean isConsistent() throws ReasonerInterruptedException, TimeOutException {
        IsKBSatisfiable satisfiable = new IsKBSatisfiable(getDefaultKB());
        BooleanResponse response = performRequest(satisfiable);
        return response.getResult();
    }

    public boolean isSatisfiable(OWLClassExpression classExpression) throws ReasonerInterruptedException, TimeOutException, ClassExpressionNotInProfileException, FreshEntitiesException, InconsistentOntologyException {
        IsClassSatisfiable query = new IsClassSatisfiable(defaultKnowledgeBase, classExpression);
        return performRequest(query).getResult();
    }

    public boolean isEntailed(OWLAxiom axiom) throws ReasonerInterruptedException, UnsupportedEntailmentTypeException, TimeOutException, AxiomNotInProfileException, FreshEntitiesException {
        IsEntailed query = new IsEntailed(defaultKnowledgeBase, axiom);
        return performRequest(query).getResult();
    }

    public boolean isEntailed(Set<? extends OWLAxiom> axioms) throws ReasonerInterruptedException, UnsupportedEntailmentTypeException, TimeOutException, AxiomNotInProfileException, FreshEntitiesException {
        IsEntailed[] queries = new IsEntailed[axioms.size()];
        int i = 0;
        for (OWLAxiom axiom : axioms) {
            queries[i++] = new IsEntailed(defaultKnowledgeBase, axiom);
        }
        ResponseMessage message = performRequests(queries);
        for (int j = 0; j < queries.length; j++) {
            if (message.get(j) instanceof BooleanResponse) {
                BooleanResponse answer = (BooleanResponse) message.get(j);
                if (!answer.getResult()) return false;
            } else if (message.get(j) instanceof ErrorResponse) {
                throw new OWLlinkReasonerRuntimeException(((ErrorResponse) message.get(j)).getErrorString());
            }
        }
        return true;
    }

    public Node<OWLClass> getTopClassNode() {
        final GetEquivalentClasses query = new GetEquivalentClasses(defaultKnowledgeBase, getOWLDataFactory().getOWLThing());
        final SetOfClasses classes = performRequest(query);
        final OWLClassNode node = new OWLClassNode();
        node.add(getOWLDataFactory().getOWLThing());
        for (OWLClass clazz : classes)
            node.add(clazz);
        return node;
    }

    public Node<OWLClass> getBottomClassNode() {
        return getUnsatisfiableClasses();
    }

    public NodeSet<OWLClass> getSubClasses(OWLClassExpression ce, boolean direct) {
        GetSubClasses query = new GetSubClasses(defaultKnowledgeBase, ce, direct);
        SetOfClassSynsets result = performRequest(query);
        return result;
    }

    public NodeSet<OWLClass> getSuperClasses(OWLClassExpression ce, boolean direct) throws InconsistentOntologyException, ClassExpressionNotInProfileException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        GetSuperClasses query = new GetSuperClasses(defaultKnowledgeBase, ce, direct);
        SetOfClassSynsets result = performRequest(query);
        return result;
    }

    public NodeSet<OWLClass> getDisjointClasses(OWLClassExpression ce, boolean direct) {
        if (!direct) {
            GetDisjointClasses query = new GetDisjointClasses(defaultKnowledgeBase, ce);
            return performRequest(query);
        } else {
            throw new OWLlinkSemanticErrorResponseException("getDisjointClasses (direct) is not supported");
        }
    }

    public Node<OWLClass> getEquivalentClasses(OWLClassExpression ce) throws InconsistentOntologyException, ClassExpressionNotInProfileException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        GetEquivalentClasses query = new GetEquivalentClasses(defaultKnowledgeBase, ce);
        OWLClassNode node = new OWLClassNode();
        for (OWLClass clazz : performRequest(query))
            node.add(clazz);
        return node;

    }

    public Node<OWLClass> getUnsatisfiableClasses() throws ReasonerInterruptedException, TimeOutException {
        return getEquivalentClasses(getOWLDataFactory().getOWLNothing());
    }

    public NodeSet<OWLObjectProperty> getSubObjectProperties(OWLObjectPropertyExpression pe, boolean direct) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        GetSubObjectProperties query = new GetSubObjectProperties(defaultKnowledgeBase, pe, direct);
        return performRequest(query);
    }

    public NodeSet<OWLObjectProperty> getSuperObjectProperties(OWLObjectPropertyExpression pe, boolean direct) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        GetSuperObjectProperties query = new GetSuperObjectProperties(defaultKnowledgeBase, pe, direct);
        return performRequest(query);
    }

    public Node<OWLObjectProperty> getEquivalentObjectProperties(OWLObjectPropertyExpression pe) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        GetEquivalentObjectProperties query = new GetEquivalentObjectProperties(defaultKnowledgeBase, pe);
        OWLObjectPropertyNode node = new OWLObjectPropertyNode();
        for (OWLObjectProperty prop : performRequest(query)) {
            node.add(prop);
        }
        return node;
    }

    public Node<OWLObjectProperty> getTopObjectPropertyNode() {
        final GetEquivalentObjectProperties query = new GetEquivalentObjectProperties(defaultKnowledgeBase, getOWLDataFactory().getOWLTopObjectProperty());
        final SetOfObjectProperties classes = performRequest(query);
        final OWLObjectPropertyNode node = new OWLObjectPropertyNode();
        node.add(getOWLDataFactory().getOWLTopObjectProperty());
        for (OWLObjectProperty clazz : classes)
            node.add(clazz);
        return node;
    }

    public Node<OWLObjectProperty> getBottomObjectPropertyNode() {
        final GetEquivalentObjectProperties query = new GetEquivalentObjectProperties(defaultKnowledgeBase, getOWLDataFactory().getOWLBottomObjectProperty());
        final SetOfObjectProperties classes = performRequest(query);
        final OWLObjectPropertyNode node = new OWLObjectPropertyNode();
        node.add(getOWLDataFactory().getOWLBottomObjectProperty());
        for (OWLObjectProperty clazz : classes)
            node.add(clazz);
        return node;
    }

    public NodeSet<OWLObjectProperty> getDisjointObjectProperties(OWLObjectPropertyExpression pe, boolean direct) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        if (!direct) {
            GetDisjointObjectProperties properties = new GetDisjointObjectProperties(defaultKnowledgeBase, pe);
            return performRequest(properties);
        }
        throw new OWLlinkSemanticErrorResponseException("getDisjointObjectProperties (direct) is not supported");
    }

    public Node<OWLObjectProperty> getInverseObjectProperties(OWLObjectPropertyExpression pe) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        final OWLObjectPropertyExpression inverse = pe.getInverseProperty();
        return getEquivalentObjectProperties(inverse);
    }

    public NodeSet<OWLClass> getObjectPropertyDomains(OWLObjectPropertyExpression pe, boolean direct) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public NodeSet<OWLClass> getObjectPropertyRanges(OWLObjectPropertyExpression pe, boolean direct) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Node<OWLDataProperty> getTopDataPropertyNode() {
        final GetEquivalentDataProperties query = new GetEquivalentDataProperties(defaultKnowledgeBase, getOWLDataFactory().getOWLTopDataProperty());
        final DataPropertySynonyms classes = performRequest(query);
        final OWLDataPropertyNode node = new OWLDataPropertyNode();
        node.add(getOWLDataFactory().getOWLTopDataProperty());
        for (OWLDataProperty clazz : classes)
            node.add(clazz);
        return node;
    }

    public Node<OWLDataProperty> getBottomDataPropertyNode() {
        final GetEquivalentDataProperties query = new GetEquivalentDataProperties(defaultKnowledgeBase, getOWLDataFactory().getOWLBottomDataProperty());
        final DataPropertySynonyms classes = performRequest(query);
        final OWLDataPropertyNode node = new OWLDataPropertyNode();
        node.add(getOWLDataFactory().getOWLBottomDataProperty());
        for (OWLDataProperty clazz : classes)
            node.add(clazz);
        return node;
    }

    public NodeSet<OWLDataProperty> getSubDataProperties(OWLDataProperty pe, boolean direct) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        GetSubDataProperties query = new GetSubDataProperties(defaultKnowledgeBase, pe, direct);
        return performRequest(query);
    }

    public NodeSet<OWLDataProperty> getSuperDataProperties(OWLDataProperty pe, boolean direct) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        GetSuperDataProperties query = new GetSuperDataProperties(defaultKnowledgeBase, pe, direct);
        return performRequest(query);
    }

    public Node<OWLDataProperty> getEquivalentDataProperties(OWLDataProperty pe) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        GetEquivalentDataProperties query = new GetEquivalentDataProperties(defaultKnowledgeBase, pe);
        return performRequest(query);
    }

    public NodeSet<OWLDataProperty> getDisjointDataProperties(OWLDataPropertyExpression pe, boolean direct) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        GetDisjointDataProperties query = new GetDisjointDataProperties(defaultKnowledgeBase, pe);
        if (!direct)
            return performRequest(query);
        else
            throw new OWLlinkSemanticErrorResponseException("getDisjointDataProperties (direct) is not supported");
    }

    public NodeSet<OWLClass> getDataPropertyDomains(OWLDataProperty pe, boolean direct) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public NodeSet<OWLNamedIndividual> getInstances(OWLClassExpression ce, boolean direct) throws InconsistentOntologyException, ClassExpressionNotInProfileException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        if (getIndividualNodeSetPolicy() == IndividualNodeSetPolicy.BY_NAME) {
            GetFlattenedInstances query = new GetFlattenedInstances(defaultKnowledgeBase, ce, direct);
            SetOfIndividuals individuals = performRequest(query);
            return convertByNamePolicy(individuals);
        }
        GetInstances query = new GetInstances(defaultKnowledgeBase, ce, direct);
        SetOfIndividualSynsets answer = performRequest(query);
        if (answer.isNode())
            return answer.asNode();
        else {
            return convertToNamedIndividuals(answer);
        }
    }

    public NodeSet<OWLClass> getTypes(OWLNamedIndividual ind, boolean direct) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        GetTypes query = new GetTypes(defaultKnowledgeBase, ind, direct);
        return performRequest(query);
    }

    public NodeSet<OWLNamedIndividual> getObjectPropertyValues(OWLNamedIndividual ind, OWLObjectPropertyExpression pe) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        if (getIndividualNodeSetPolicy() == IndividualNodeSetPolicy.BY_NAME) {
            GetFlattenedObjectPropertyTargets query = new GetFlattenedObjectPropertyTargets(defaultKnowledgeBase, ind, pe);
            return convertByNamePolicy(performRequest(query));
        }
        GetObjectPropertyTargets query = new GetObjectPropertyTargets(defaultKnowledgeBase, ind, pe);
        SetOfIndividualSynsets answer = performRequest(query);
        if (answer.isNode())
            return answer.asNode();
        else {

        }
        return convertToNamedIndividuals(answer);
    }

    public Set<OWLLiteral> getDataPropertyValues(OWLNamedIndividual ind, OWLDataProperty pe) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        GetDataPropertyTargets query = new GetDataPropertyTargets(defaultKnowledgeBase, ind, pe);
        return performRequest(query);
    }

    public Node<OWLNamedIndividual> getSameIndividuals(OWLNamedIndividual ind) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        GetSameIndividuals query = new GetSameIndividuals(defaultKnowledgeBase, ind);
        IndividualSynonyms answer = performRequest(query);
        if (answer.isNode()) {
            return answer.asNode();
        } else
            return convertToNamedIndividual(answer);
    }

    public NodeSet<OWLNamedIndividual> getDifferentIndividuals(OWLNamedIndividual ind) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        GetDifferentIndividuals query = new GetDifferentIndividuals(defaultKnowledgeBase, ind);
        SetOfIndividualSynsets answer = performRequest(query);
        if (answer.isNode())
            return answer.asNode();
        else
            return convertToNamedIndividuals(answer);
    }

    protected NodeSet<OWLNamedIndividual> convertToNamedIndividuals(SetOfIndividualSynsets setOfsynsets) {
        OWLNamedIndividualNodeSet set = new OWLNamedIndividualNodeSet();
        for (IndividualSynset synset : setOfsynsets.getSynsets()) {
            OWLNamedIndividualNode node = new OWLNamedIndividualNode();
            for (OWLIndividual indi : synset) {
                if (indi.isNamed())
                    node.add(indi.asOWLNamedIndividual());
            }
            if (node.getSize() > 0)
                set.addNode(node);
        }
        return set;
    }

    protected Node<OWLNamedIndividual> convertToNamedIndividual(IndividualSynonyms synonyms) {
        OWLNamedIndividualNode node = new OWLNamedIndividualNode();
        for (OWLIndividual indi : synonyms) {
            if (indi.isNamed())
                node.add(indi.asOWLNamedIndividual());
        }
        return node;
    }

    protected NodeSet<OWLNamedIndividual> convertByNamePolicy(SetOfIndividuals individuals) {
        OWLNamedIndividualNodeSet set = new OWLNamedIndividualNodeSet();
        for (OWLIndividual indi : individuals) {
            if (indi.isNamed())
                set.addNode(new OWLNamedIndividualNode(indi.asOWLNamedIndividual()));
        }
        return set;

    }

    public void prepareReasoner() throws ReasonerInterruptedException, TimeOutException {
        try {
            classify();
        } catch (OWLReasonerException e) {
            e.printStackTrace();
        }
        try {
            realise();
        } catch (OWLReasonerException e) {
            e.printStackTrace();
        }
    }


    public void classify() throws OWLReasonerException {
        Classify classify = new Classify(defaultKnowledgeBase);
        performRequests(classify);
    }

    public void realise() throws OWLReasonerException {
        Realize realize = new Realize(defaultKnowledgeBase);
        performRequests(realize);
    }


    protected void createDefaultKB() {
        CreateKB kb = new CreateKB();
        KB kbResponse = performRequest(kb);
        this.defaultKnowledgeBase = kbResponse.getKB();
    }

    protected Description getReasonerInfo() {
        GetDescription getDesc = new GetDescription();
        Description description = performRequest(getDesc);
        return description;
    }

    protected ResponseMessage performRequests(Request... request) {
        try {
            //session.setReasonerURL(this.reasonerURL);
            return session.performRequests(request);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected <R extends Response> R performRequest(Request<R> request) {
        ResponseMessage message = performRequests(request);
        return message.getResponse(request);
    }
}

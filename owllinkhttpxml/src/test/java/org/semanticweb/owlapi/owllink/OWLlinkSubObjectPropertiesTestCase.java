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

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSubObjectProperties;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSubObjectPropertyHierarchy;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSuperObjectProperties;
import org.semanticweb.owlapi.owllink.builtin.requests.IsEntailed;
import org.semanticweb.owlapi.owllink.builtin.response.*;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.impl.OWLObjectPropertyNode;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 02.11.2009
 */
public class OWLlinkSubObjectPropertiesTestCase extends AbstractOWLlinkAxiomsTestCase {

    protected Set<? extends OWLAxiom> createAxioms() {
        Set<OWLAxiom> axioms = CollectionFactory.createSet();

        axioms.add(getDataFactory().getOWLSubObjectPropertyOfAxiom(getOWLObjectProperty("A"), getOWLObjectProperty("B")));
        axioms.add(getDataFactory().getOWLSubObjectPropertyOfAxiom(getOWLObjectProperty("B"), getOWLObjectProperty("C")));
        axioms.add(getDataFactory().getOWLSubObjectPropertyOfAxiom(getOWLObjectProperty("D"), getOWLObjectProperty("C")));

        return axioms;
    }

    public void testSubsumedBy() throws Exception {
        IsEntailed query = new IsEntailed(getKBIRI(), getDataFactory().getOWLSubObjectPropertyOfAxiom(
                getOWLObjectProperty("A"), getOWLObjectProperty("B")));
        BooleanResponse response = super.reasoner.answer(query);
        assertTrue(response.getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLSubObjectPropertyOfAxiom(
                getOWLObjectProperty("A"), getOWLObjectProperty("C")));
        response = super.reasoner.answer(query);
        assertTrue(response.getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLSubObjectPropertyOfAxiom(
                getOWLObjectProperty("D"), getOWLObjectProperty("B")));
        response = super.reasoner.answer(query);
        assertFalse(response.getResult());
    }

    public void testSubsumedByViaOWLReasoner() throws Exception {
        OWLAxiom axiom = getDataFactory().getOWLSubObjectPropertyOfAxiom(
                getOWLObjectProperty("A"), getOWLObjectProperty("B"));
        assertTrue(super.reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLSubObjectPropertyOfAxiom(
                getOWLObjectProperty("A"), getOWLObjectProperty("C"));
        assertTrue(super.reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLSubObjectPropertyOfAxiom(
                getOWLObjectProperty("D"), getOWLObjectProperty("B"));
        assertFalse(super.reasoner.isEntailed(axiom));
    }


    public void testGetSubObjectProperties() throws Exception {
        //indirect case
        GetSubObjectProperties query = new GetSubObjectProperties(getKBIRI(), getOWLObjectProperty("B"));
        SetOfObjectPropertySynsets response = super.reasoner.answer(query);

        assertTrue(response.getNodes().size() == 2);
        Node<OWLObjectProperty> synset = response.iterator().next();
        assertTrue(synset.getSize() == 1);
        assertTrue(synset.contains(getOWLObjectProperty("A")));

        Set<OWLObjectProperty> flattenedClasses = response.getFlattened();
        assertTrue(flattenedClasses.size() == 2);
        assertTrue(flattenedClasses.contains(getOWLObjectProperty("A")));
        assertTrue(flattenedClasses.contains(manager.getOWLDataFactory().getOWLBottomObjectProperty()));
    }

    public void testGetSubObjectPropertiesViaOWLReasoner() throws Exception {
        //indirect case
        NodeSet<OWLObjectProperty> response = super.reasoner.getSubObjectProperties(getOWLObjectProperty("B"), false);

        assertTrue(response.getNodes().size() == 2);
        Node<OWLObjectProperty> synset = response.iterator().next();
        assertTrue(synset.getSize() == 1);
        assertTrue(synset.contains(getOWLObjectProperty("A")));

        Set<OWLObjectProperty> flattenedClasses = response.getFlattened();
        assertTrue(flattenedClasses.size() == 2);
        assertTrue(flattenedClasses.contains(getOWLObjectProperty("A")));
        assertTrue(flattenedClasses.contains(manager.getOWLDataFactory().getOWLBottomObjectProperty()));
    }

    public void testGetDirectSubObjectProperties() throws Exception {
        //direct case
        GetSubObjectProperties query = new GetSubObjectProperties(getKBIRI(), getOWLObjectProperty("B"), true);
        SetOfObjectPropertySynsets response = super.reasoner.answer(query);
        assertTrue(response.getNodes().size() == 1);
        assertTrue(response.getFlattened().size() == 1);
        assertTrue(response.getFlattened().contains(getOWLObjectProperty("A")));
    }

    public void testGetDirectSubObjectPropertiesViaOWLReasoner() throws Exception {
        //direct case
        NodeSet<OWLObjectProperty> response = super.reasoner.getSubObjectProperties(getOWLObjectProperty("B"), true);
        assertTrue(response.getNodes().size() == 1);
        assertTrue(response.getFlattened().size() == 1);
        assertTrue(response.getFlattened().contains(getOWLObjectProperty("A")));
    }


    public void testGetSuperProperties() throws Exception {
        GetSuperObjectProperties query = new GetSuperObjectProperties(getKBIRI(), getOWLObjectProperty("A"));
        SetOfObjectPropertySynsets response = super.reasoner.answer(query);
        assertTrue(response.getNodes().size() == 3);
        Node<OWLObjectProperty> synset = new OWLObjectPropertyNode(getOWLObjectProperty("B"));
        assertTrue(response.getNodes().contains(synset));
        synset = new OWLObjectPropertyNode(getOWLObjectProperty("C"));
        assertTrue(response.getNodes().contains(synset));
        synset = new OWLObjectPropertyNode(manager.getOWLDataFactory().getOWLTopObjectProperty());
        assertTrue(response.getNodes().contains(synset));
    }

    public void testGetSuperPropertiesViaOWLReasoner() throws Exception {
        NodeSet<OWLObjectProperty> response = super.reasoner.getSuperObjectProperties(getOWLObjectProperty("A"), false);
        assertTrue(response.getNodes().size() == 3);
        Node<OWLObjectProperty> synset = new OWLObjectPropertyNode(getOWLObjectProperty("B"));
        assertTrue(response.getNodes().contains(synset));
        synset = new OWLObjectPropertyNode(getOWLObjectProperty("C"));
        assertTrue(response.getNodes().contains(synset));
        synset = new OWLObjectPropertyNode(manager.getOWLDataFactory().getOWLTopObjectProperty());
        assertTrue(response.getNodes().contains(synset));
    }

    public void testGetSuperPropertiesDirect() throws Exception {
        GetSuperObjectProperties query = new GetSuperObjectProperties(getKBIRI(), getOWLObjectProperty("A"), true);
        SetOfObjectPropertySynsets response = super.reasoner.answer(query);
        assertTrue(response.getNodes().size() == 1);
    }

    public void testGetSuperPropertiesDirectViaOWLReasoner() throws Exception {
        NodeSet<OWLObjectProperty> response = super.reasoner.getSuperObjectProperties(getOWLObjectProperty("A"), true);
        assertTrue(response.getNodes().size() == 1);
    }

    public void testSubPropertyHierarchy() throws Exception {
        GetSubObjectPropertyHierarchy query = new GetSubObjectPropertyHierarchy(getKBIRI());
        Hierarchy<OWLObjectProperty> response = super.reasoner.answer(query);
        Set<HierarchyPair<OWLObjectProperty>> pairs = response.getPairs();
        assertFalse(pairs.isEmpty());
        assertTrue(pairs.size() == 3);

        Set<HierarchyPair<OWLObjectProperty>> expectedSet = CollectionFactory.createSet();
        Node<OWLObjectProperty> synset = new OWLObjectPropertyNode(getDataFactory().getOWLTopObjectProperty());
        Set<Node<OWLObjectProperty>> set = CollectionFactory.createSet();
        set.add(new OWLObjectPropertyNode(getOWLObjectProperty("C")));
        SubEntitySynsets<OWLObjectProperty> setOfSynsets = new SubObjectPropertySynsets(set);
        expectedSet.add(new HierarchyPairImpl<OWLObjectProperty>(synset, setOfSynsets));

        synset = new OWLObjectPropertyNode(getOWLObjectProperty("C"));
        set = CollectionFactory.createSet();
        set.add(new OWLObjectPropertyNode(getOWLObjectProperty("B")));
        set.add(new OWLObjectPropertyNode(getOWLObjectProperty("D")));

        setOfSynsets = new SubObjectPropertySynsets(set);
        expectedSet.add(new HierarchyPairImpl<OWLObjectProperty>(synset, setOfSynsets));

        synset = new OWLObjectPropertyNode(getOWLObjectProperty("B"));
        set = CollectionFactory.createSet();
        set.add(new OWLObjectPropertyNode(getOWLObjectProperty("A")));
        setOfSynsets = new SubObjectPropertySynsets(set);
        expectedSet.add(new HierarchyPairImpl<OWLObjectProperty>(synset, setOfSynsets));

        for (HierarchyPair pair : pairs) {
            expectedSet.remove(pair);
        }
        assertTrue(expectedSet.isEmpty());
    }

}

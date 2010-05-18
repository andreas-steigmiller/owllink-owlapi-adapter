/*
 * Copyright (C) 2010, Ulm University
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
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSubClassHierarchy;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSubClasses;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSuperClasses;
import org.semanticweb.owlapi.owllink.builtin.requests.IsEntailed;
import org.semanticweb.owlapi.owllink.builtin.response.*;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.impl.OWLClassNode;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 02.11.2009
 */
public class OWLlinkIsSubClassesTestCase extends AbstractOWLlinkAxiomsTestCase {

    protected Set<? extends OWLAxiom> createAxioms() {
        Set<OWLAxiom> axioms = CollectionFactory.createSet();

        axioms.add(getDataFactory().getOWLSubClassOfAxiom(getOWLClass("A"), getOWLClass("B")));
        axioms.add(getDataFactory().getOWLSubClassOfAxiom(getOWLClass("B"), getOWLClass("C")));
        axioms.add(getDataFactory().getOWLSubClassOfAxiom(getOWLClass("D"), getOWLClass("C")));


        return axioms;
    }

    public void testSubsumedBy() throws Exception {
        IsEntailed query = new IsEntailed(getKBIRI(), getDataFactory().getOWLSubClassOfAxiom(
                getOWLClass("A"), getOWLClass("B")));
        BooleanResponse response = super.reasoner.answer(query);
        assertTrue(response.getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLSubClassOfAxiom(getOWLClass("A"), getOWLClass("C")));
        response = super.reasoner.answer(query);
        assertTrue(response.getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLSubClassOfAxiom(
                getOWLClass("D"), getOWLClass("B")));
        response = super.reasoner.answer(query);
        assertFalse(response.getResult());
    }

    public void testSubsumedByViaOWLReasoner() throws Exception {
        OWLSubClassOfAxiom axiom = getDataFactory().getOWLSubClassOfAxiom(
                getOWLClass("A"), getOWLClass("B"));
        assertTrue(super.reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLSubClassOfAxiom(getOWLClass("A"), getOWLClass("C"));
        assertTrue(super.reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLSubClassOfAxiom(
                getOWLClass("D"), getOWLClass("B"));
        assertFalse(super.reasoner.isEntailed(axiom));

    }

    public void testGetSubClasses() throws Exception {
        GetSubClasses query = new GetSubClasses(getKBIRI(), getOWLClass("B"));
        NodeSet<OWLClass> response = super.reasoner.answer(query);
        assertTrue(response.getNodes().size() == 2);
        Set<OWLClass> flattenedClasses = response.getFlattened();
        assertTrue(flattenedClasses.size() == 2);
        assertTrue(flattenedClasses.contains(getOWLClass("A")));
        assertTrue(flattenedClasses.contains(manager.getOWLDataFactory().getOWLNothing()));
    }

    public void testGetSubClassesViaOWLReasoner() throws Exception {
        NodeSet<OWLClass> nodeSet = super.reasoner.getSubClasses(getOWLClass("B"), false);
        assertTrue(nodeSet.getNodes().size() == 2);

        Set<OWLClass> flattenedClasses = nodeSet.getFlattened();
        assertTrue(flattenedClasses.size() == 2);
        assertTrue(flattenedClasses.contains(getOWLClass("A")));
        assertTrue(flattenedClasses.contains(manager.getOWLDataFactory().getOWLNothing()));
    }


    public void testGetSuperClasses() throws Exception {
        GetSuperClasses query = new GetSuperClasses(getKBIRI(), getOWLClass("A"));
        SetOfClassSynsets response = super.reasoner.answer(query);
        assertTrue(response.getNodes().size() == 3);

        query = new GetSuperClasses(getKBIRI(), getOWLClass("A"), true);
        response = super.reasoner.answer(query);
        assertTrue(response.getNodes().size() == 1);
    }

    public void testGetSuperClassesViaOWLReasoner() throws Exception {
        NodeSet<OWLClass> response = super.reasoner.getSuperClasses(getOWLClass("A"), false);
        assertTrue(response.getNodes().size() == 3);

        response = super.reasoner.getSuperClasses(getOWLClass("A"), true);
        assertTrue(response.getNodes().size() == 1);
    }

    public void testClassHierarchy() throws Exception {
        GetSubClassHierarchy query = new GetSubClassHierarchy(getKBIRI());
        Hierarchy<OWLClass> response = super.reasoner.answer(query);
        Set<HierarchyPair<OWLClass>> pairs = response.getPairs();
        assertFalse(pairs.isEmpty());
        assertTrue(pairs.size() == 3);

        Set<HierarchyPair<OWLClass>> expectedSet = CollectionFactory.createSet();
        Node<OWLClass> synset = new OWLClassNode(getDataFactory().getOWLThing());
        Set<Node<OWLClass>> set = CollectionFactory.createSet();
        set.add(new OWLClassNode(getOWLClass("C")));
        SubEntitySynsets<OWLClass> setOfSynsets = new SubClassSynsets(set);
        expectedSet.add(new HierarchyPairImpl<OWLClass>(synset, setOfSynsets));

        synset = new OWLClassNode(getOWLClass("C"));
        set = CollectionFactory.createSet();
        set.add(new OWLClassNode(getOWLClass("B")));
        set.add(new OWLClassNode(getOWLClass("D")));
        setOfSynsets = new SubClassSynsets(set);
        expectedSet.add(new HierarchyPairImpl<OWLClass>(synset, setOfSynsets));

        synset = new OWLClassNode(getOWLClass("B"));
        set = CollectionFactory.createSet();
        set.add(new OWLClassNode(getOWLClass("A")));
        setOfSynsets = new SubClassSynsets(set);
        expectedSet.add(new HierarchyPairImpl<OWLClass>(synset, setOfSynsets));


        for (HierarchyPair pair : pairs) {
            expectedSet.remove(pair);
        }
        assertTrue(expectedSet.isEmpty());


    }

    public void testSubClassHierarchy() throws Exception {
        GetSubClassHierarchy query = new GetSubClassHierarchy(getKBIRI(), getOWLClass("C"));
        Hierarchy<OWLClass> response = super.reasoner.answer(query);
        Set<HierarchyPair<OWLClass>> pairs = response.getPairs();
        assertFalse(pairs.isEmpty());
        assertTrue(pairs.size() == 2);

        Set<HierarchyPair<OWLClass>> expectedSet = CollectionFactory.createSet();

        Node<OWLClass> synset = new OWLClassNode(getOWLClass("C"));
        Set<Node<OWLClass>> set = CollectionFactory.createSet();
        set.add(new OWLClassNode(getOWLClass("D")));
        set.add(new OWLClassNode(getOWLClass("B")));
        SubEntitySynsets<OWLClass> setOfSynsets = new SubClassSynsets(set);
        expectedSet.add(new HierarchyPairImpl<OWLClass>(synset, setOfSynsets));

        synset = new OWLClassNode(getOWLClass("B"));
        set = CollectionFactory.createSet();
        set.add(new OWLClassNode(getOWLClass("A")));
        setOfSynsets = new SubClassSynsets(set);
        expectedSet.add(new HierarchyPairImpl<OWLClass>(synset, setOfSynsets));

        for (HierarchyPair pair : pairs) {
            expectedSet.remove(pair);
        }
        assertTrue(expectedSet.isEmpty());
    }
}

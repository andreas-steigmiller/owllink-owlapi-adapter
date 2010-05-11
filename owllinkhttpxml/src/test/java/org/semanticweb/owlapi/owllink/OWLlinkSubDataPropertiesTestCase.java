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
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSubDataProperties;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSubDataPropertyHierarchy;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSuperDataProperties;
import org.semanticweb.owlapi.owllink.builtin.requests.IsEntailed;
import org.semanticweb.owlapi.owllink.builtin.response.*;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.impl.OWLDataPropertyNode;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 02.11.2009
 */
public class OWLlinkSubDataPropertiesTestCase extends AbstractOWLlinkAxiomsTestCase {

    protected Set<? extends OWLAxiom> createAxioms() {
        Set<OWLAxiom> axioms = CollectionFactory.createSet();

        axioms.add(getDataFactory().getOWLSubDataPropertyOfAxiom(getOWLDataProperty("A"), getOWLDataProperty("B")));
        axioms.add(getDataFactory().getOWLSubDataPropertyOfAxiom(getOWLDataProperty("B"), getOWLDataProperty("C")));
        axioms.add(getDataFactory().getOWLSubDataPropertyOfAxiom(getOWLDataProperty("D"), getOWLDataProperty("C")));

        return axioms;
    }

    public void testSubsumedBy() throws Exception {
        IsEntailed query = new IsEntailed(getKBIRI(), getDataFactory().getOWLSubDataPropertyOfAxiom(getOWLDataProperty("A"), getOWLDataProperty("B")));
        BooleanResponse response = super.reasoner.answer(query);
        assertTrue(response.getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLSubDataPropertyOfAxiom(getOWLDataProperty("A"), getOWLDataProperty("C")));
        response = super.reasoner.answer(query);
        assertTrue(response.getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLSubDataPropertyOfAxiom(getOWLDataProperty("D"), getOWLDataProperty("B")));
        response = super.reasoner.answer(query);
        assertFalse(response.getResult());
    }

    public void testSubsumedByViaOWLReasoner() throws Exception {
        OWLAxiom axiom = getDataFactory().getOWLSubDataPropertyOfAxiom(getOWLDataProperty("A"), getOWLDataProperty("B"));
        assertTrue(super.reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLSubDataPropertyOfAxiom(getOWLDataProperty("A"), getOWLDataProperty("C"));
        assertTrue(super.reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLSubDataPropertyOfAxiom(getOWLDataProperty("D"), getOWLDataProperty("B"));
        assertFalse(super.reasoner.isEntailed(axiom));
    }


    public void testGetSubProperties() throws Exception {
        GetSubDataProperties query = new GetSubDataProperties(getKBIRI(), getOWLDataProperty("B"));
        SetOfDataPropertySynsets response = super.reasoner.answer(query);

        assertTrue(response.getNodes().size() == 2);

        Set<OWLDataProperty> flattenedClasses = response.getFlattened();
        assertTrue(flattenedClasses.size() == 2);
        assertTrue(flattenedClasses.contains(getOWLDataProperty("A")));
        assertTrue(flattenedClasses.contains(manager.getOWLDataFactory().getOWLBottomDataProperty()));
    }

    public void testGetSubPropertiesViaOWLReasoner() throws Exception {
        NodeSet<OWLDataProperty> response = super.reasoner.getSubDataProperties(getOWLDataProperty("B"), false);

        assertTrue(response.getNodes().size() == 2);

        Set<OWLDataProperty> flattenedClasses = response.getFlattened();
        assertTrue(flattenedClasses.size() == 2);
        assertTrue(flattenedClasses.contains(getOWLDataProperty("A")));
        assertTrue(flattenedClasses.contains(manager.getOWLDataFactory().getOWLBottomDataProperty()));
    }

    public void testGetDirectSubProperties() throws Exception {
        GetSubDataProperties query = new GetSubDataProperties(getKBIRI(), getOWLDataProperty("B"), true);
        SetOfDataPropertySynsets response = super.reasoner.answer(query);
        assertTrue(response.getNodes().size() == 1);
        Set<OWLDataProperty> flattenedClasses = response.getFlattened();
        assertTrue(flattenedClasses.size() == 1);
        assertTrue(flattenedClasses.contains(getOWLDataProperty("A")));
    }

    public void testGetDirectSubPropertiesViaOWLReasoner() throws Exception {
        NodeSet<OWLDataProperty> response = super.reasoner.getSubDataProperties(getOWLDataProperty("B"), true);
        assertTrue(response.getNodes().size() == 1);
        Set<OWLDataProperty> flattenedClasses = response.getFlattened();
        assertTrue(flattenedClasses.size() == 1);
        assertTrue(flattenedClasses.contains(getOWLDataProperty("A")));
    }

    public void testGetSuperProperties() throws Exception {
        GetSuperDataProperties query = new GetSuperDataProperties(getKBIRI(), getOWLDataProperty("A"));
        SetOfDataPropertySynsets response = super.reasoner.answer(query);
        assertTrue(response.getNodes().size() == 3);

        query = new GetSuperDataProperties(getKBIRI(), getOWLDataProperty("A"), true);
        response = super.reasoner.answer(query);
        assertTrue(response.getNodes().size() == 1);
    }

    public void testGetSuperPropertiesViaOWLReasoner() throws Exception {
        NodeSet<OWLDataProperty> response = super.reasoner.getSuperDataProperties(getOWLDataProperty("A"), false);
        assertTrue(response.getNodes().size() == 3);

        response = super.reasoner.getSuperDataProperties(getOWLDataProperty("A"), true);
        assertTrue(response.getNodes().size() == 1);
    }

    public void testSubPropertyHierarchy() throws Exception {
        GetSubDataPropertyHierarchy query = new GetSubDataPropertyHierarchy(getKBIRI());
        DataPropertyHierarchy response = super.reasoner.answer(query);
        Set<HierarchyPair<OWLDataProperty>> pairs = response.getPairs();
        assertFalse(pairs.isEmpty());
        assertTrue(pairs.size() == 3);

        Set<HierarchyPair<OWLDataProperty>> expectedSet = CollectionFactory.createSet();
        Node<OWLDataProperty> synset = new OWLDataPropertyNode(getDataFactory().getOWLTopDataProperty());
        Set<Node<OWLDataProperty>> set = CollectionFactory.createSet();
        set.add(new OWLDataPropertyNode(getOWLDataProperty("C")));
        SubEntitySynsets<OWLDataProperty> setOfSynsets = new SubDataPropertySynsets(set);
        expectedSet.add(new HierarchyPairImpl<OWLDataProperty>(synset, setOfSynsets));

        synset = new OWLDataPropertyNode(getOWLDataProperty("C"));
        set = CollectionFactory.createSet();
        set.add(new OWLDataPropertyNode(getOWLDataProperty("D")));
        set.add(new OWLDataPropertyNode(getOWLDataProperty("B")));

        setOfSynsets = new SubDataPropertySynsets(set);
        expectedSet.add(new HierarchyPairImpl<OWLDataProperty>(synset, setOfSynsets));

        synset = new OWLDataPropertyNode(getOWLDataProperty("B"));
        set = CollectionFactory.createSet();
        set.add(new OWLDataPropertyNode(getOWLDataProperty("A")));
        setOfSynsets = new SubDataPropertySynsets(set);
        expectedSet.add(new HierarchyPairImpl<OWLDataProperty>(synset, setOfSynsets));

        for (HierarchyPair pair : pairs) {
            expectedSet.remove(pair);
        }
        assertTrue(expectedSet.isEmpty());
    }
}

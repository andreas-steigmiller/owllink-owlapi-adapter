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
import org.semanticweb.owlapi.owllink.builtin.requests.GetEquivalentObjectProperties;
import org.semanticweb.owlapi.owllink.builtin.requests.IsEntailed;
import org.semanticweb.owlapi.owllink.builtin.response.BooleanResponse;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfObjectProperties;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 02.11.2009
 */
public class OWLlinkEquivalentObjectPropertiesTestCase extends AbstractOWLlinkAxiomsTestCase {

    protected Set<? extends OWLAxiom> createAxioms() {
        Set<OWLAxiom> axioms = CollectionFactory.createSet();
        axioms.add(getDataFactory().getOWLSubObjectPropertyOfAxiom(getOWLObjectProperty("A"), getOWLObjectProperty("B")));
        axioms.add(getDataFactory().getOWLSubObjectPropertyOfAxiom(getOWLObjectProperty("B"), getOWLObjectProperty("C")));
        axioms.add(getDataFactory().getOWLSubObjectPropertyOfAxiom(getOWLObjectProperty("B"), getOWLObjectProperty("A")));
        axioms.add(getDataFactory().getOWLEquivalentObjectPropertiesAxiom(getOWLObjectProperty("D"), getOWLObjectProperty("E")));
        return axioms;
    }

    public void testAreObjectPropertiesEquivalent() throws Exception {
        IsEntailed query = new IsEntailed(getKBIRI(), getDataFactory().getOWLEquivalentObjectPropertiesAxiom(getOWLObjectProperty("A"), getOWLObjectProperty("B")));
        BooleanResponse result = super.reasoner.answer(query);
        assertTrue(result.getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLEquivalentObjectPropertiesAxiom(getOWLObjectProperty("A"), getOWLObjectProperty("B"), getOWLObjectProperty("C")));
        result = super.reasoner.answer(query);
        assertFalse(result.getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLEquivalentObjectPropertiesAxiom(getOWLObjectProperty("D"), getOWLObjectProperty("E"), getOWLObjectProperty("A")));
        result = super.reasoner.answer(query);
        assertFalse(result.getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLEquivalentObjectPropertiesAxiom(getOWLObjectProperty("D"), getOWLObjectProperty("E")));
        result = super.reasoner.answer(query);
        assertTrue(result.getResult());
    }

    public void testAreObjectPropertiesEquivalentViaOWLReasoner() throws Exception {
        OWLAxiom axiom = getDataFactory().getOWLEquivalentObjectPropertiesAxiom(getOWLObjectProperty("A"), getOWLObjectProperty("B"));
        assertTrue(super.reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLEquivalentObjectPropertiesAxiom(getOWLObjectProperty("A"), getOWLObjectProperty("B"), getOWLObjectProperty("C"));
        assertFalse(super.reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLEquivalentObjectPropertiesAxiom(getOWLObjectProperty("D"), getOWLObjectProperty("E"), getOWLObjectProperty("A"));
        assertFalse(super.reasoner.isEntailed(axiom));


        axiom = getDataFactory().getOWLEquivalentObjectPropertiesAxiom(getOWLObjectProperty("D"), getOWLObjectProperty("E"));
        assertTrue(super.reasoner.isEntailed(axiom));
    }

    public void testGetEquivalentObjectProperties() throws Exception {
        GetEquivalentObjectProperties query = new GetEquivalentObjectProperties(getKBIRI(), getOWLObjectProperty("A"));
        SetOfObjectProperties result = super.reasoner.answer(query);
        assertTrue(result.size() == 2);
        assertTrue(result.contains(getOWLObjectProperty("A")));
        assertTrue(result.contains(getOWLObjectProperty("B")));
    }

    public void testGetEquivalentObjectPropertiesViaOWLReasoner() throws Exception {
        Node<OWLObjectProperty> result = super.reasoner.getEquivalentObjectProperties(getOWLObjectProperty("A"));
        assertTrue(result.getSize() == 2);
        assertTrue(result.contains(getOWLObjectProperty("A")));
        assertTrue(result.contains(getOWLObjectProperty("B")));
    }
}

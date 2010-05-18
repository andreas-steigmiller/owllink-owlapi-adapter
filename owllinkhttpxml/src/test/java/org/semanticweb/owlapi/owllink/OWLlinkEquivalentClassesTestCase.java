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
import org.semanticweb.owlapi.owllink.builtin.requests.GetEquivalentClasses;
import org.semanticweb.owlapi.owllink.builtin.requests.IsEntailed;
import org.semanticweb.owlapi.owllink.builtin.response.BooleanResponse;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfClasses;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 02.11.2009
 */
public class OWLlinkEquivalentClassesTestCase extends AbstractOWLlinkAxiomsTestCase {

    protected Set<? extends OWLAxiom> createAxioms() {
        Set<OWLAxiom> axioms = CollectionFactory.createSet();
        axioms.add(getDataFactory().getOWLSubClassOfAxiom(getOWLClass("A"), getOWLClass("B")));
        axioms.add(getDataFactory().getOWLSubClassOfAxiom(getOWLClass("B"), getOWLClass("C")));
        axioms.add(getDataFactory().getOWLSubClassOfAxiom(getOWLClass("C"), getOWLClass("A")));
        return axioms;
    }

    public void testAreClassesEquivalent() throws Exception {
        IsEntailed query = new IsEntailed(getKBIRI(), getDataFactory().getOWLEquivalentClassesAxiom(getOWLClass("A"), getOWLClass("B")));
        BooleanResponse result = super.reasoner.answer(query);
        assertTrue(result.getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLEquivalentClassesAxiom(getOWLClass("A"), getOWLClass("B"), getOWLClass("C")));
        result = super.reasoner.answer(query);
        assertTrue(result.getResult());
    }

    public void testAreClassesEquivalentViaOWLReasoner() throws Exception {
        OWLAxiom axiom = getDataFactory().getOWLEquivalentClassesAxiom(getOWLClass("A"), getOWLClass("B"));
        assertTrue(super.reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLEquivalentClassesAxiom(getOWLClass("A"), getOWLClass("B"), getOWLClass("C"));
        assertTrue(super.reasoner.isEntailed(axiom));
    }

    public void testGetEquivalentClasses() throws Exception {
        GetEquivalentClasses query = new GetEquivalentClasses(getKBIRI(), getOWLClass("A"));
        SetOfClasses result = super.reasoner.answer(query);
        assertTrue(result.size() == 3);
        assertTrue(result.contains(getOWLClass("A")));
        assertTrue(result.contains(getOWLClass("B")));
        assertTrue(result.contains(getOWLClass("C")));
    }

    public void testGetEquivalentClassesViaOWLReasoner() throws Exception {
        Node<OWLClass> result = super.reasoner.getEquivalentClasses(getOWLClass("A"));
        assertTrue(result.getSize() == 3);
        assertTrue(result.contains(getOWLClass("A")));
        assertTrue(result.contains(getOWLClass("B")));
        assertTrue(result.contains(getOWLClass("C")));
    }
}

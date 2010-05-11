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
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.owllink.builtin.requests.GetFlattenedTypes;
import org.semanticweb.owlapi.owllink.builtin.requests.GetInstances;
import org.semanticweb.owlapi.owllink.builtin.requests.GetTypes;
import org.semanticweb.owlapi.owllink.builtin.requests.IsEntailed;
import org.semanticweb.owlapi.owllink.builtin.response.ClassSynsets;
import org.semanticweb.owlapi.owllink.builtin.response.Classes;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfIndividualSynsets;
import org.semanticweb.owlapi.reasoner.NodeSet;
import static org.semanticweb.owlapi.util.CollectionFactory.createSet;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 03.11.2009
 */
public class OWLlinkIsInstanceOfTestCase extends AbstractOWLlinkAxiomsTestCase {

    protected Set<? extends OWLAxiom> createAxioms() {
        Set<OWLAxiom> axioms = createSet();

        axioms.add(getDataFactory().getOWLClassAssertionAxiom(getOWLClass("A"), getOWLIndividual("i")));
        axioms.add(getDataFactory().getOWLClassAssertionAxiom(getOWLClass("B"), getOWLIndividual("i")));
        axioms.add(getDataFactory().getOWLClassAssertionAxiom(getOWLClass("B"), getOWLIndividual("j")));

        return axioms;
    }

    public void testIsInstanceOf() throws Exception {
        IsEntailed query = new IsEntailed(getKBIRI(), getDataFactory().getOWLClassAssertionAxiom(
                getOWLClass("A"), getOWLIndividual("i")));
        assertTrue(super.reasoner.answer(query).getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLClassAssertionAxiom(getDataFactory().getOWLThing(),
                getOWLIndividual("i")));
        assertTrue(super.reasoner.answer(query).getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLClassAssertionAxiom(getOWLClass("B"),
                getOWLIndividual("j")));
        assertTrue(super.reasoner.answer(query).getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLClassAssertionAxiom(getOWLClass("A"),
                getOWLIndividual("j")));
        assertFalse(super.reasoner.answer(query).getResult());
    }

    public void testIsInstanceOfViaOWLReasoner() throws Exception {
        OWLAxiom axiom = getDataFactory().getOWLClassAssertionAxiom(
                getOWLClass("A"), getOWLIndividual("i"));
        assertTrue(reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLClassAssertionAxiom(getDataFactory().getOWLThing(),
                getOWLIndividual("i"));
        assertTrue(reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLClassAssertionAxiom(getOWLClass("B"),
                getOWLIndividual("j"));
        assertTrue(reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLClassAssertionAxiom(getOWLClass("A"),
                getOWLIndividual("j"));
        assertFalse(reasoner.isEntailed(axiom));
    }

    public void testFlattenedTypes() throws Exception {
        GetFlattenedTypes query = new GetFlattenedTypes(getKBIRI(), getOWLIndividual("i"));
        Classes answer = super.reasoner.answer(query);
        assertTrue(answer.getSize() == 3);
        assertTrue(answer.contains(getOWLClass("A")));
        assertTrue(answer.contains(getOWLClass("B")));
        assertTrue(answer.contains(getDataFactory().getOWLThing()));

        query = new GetFlattenedTypes(getKBIRI(), getOWLIndividual("i"), true);
        answer = super.reasoner.answer(query);
        assertTrue(answer.getSize() == 2);
        assertTrue(answer.contains(getOWLClass("A")));
        assertTrue(answer.contains(getOWLClass("B")));
    }

    public void testTypes() throws Exception {
        GetTypes types = new GetTypes(getKBIRI(), getOWLIndividual("i"), true);
        ClassSynsets answerTypes = super.reasoner.answer(types);
        assertTrue(answerTypes.getFlattened().size() == 3);
        assertTrue(answerTypes.getFlattened().contains(getOWLClass("A")));
        assertTrue(answerTypes.getFlattened().contains(getOWLClass("B")));
        assertTrue(answerTypes.getFlattened().contains(getDataFactory().getOWLThing()));
    }

    public void testTypesViaOWLReasoner() throws Exception {
        NodeSet<OWLClass> answerTypes = super.reasoner.getTypes(getOWLIndividual("i"), false);
        assertTrue(answerTypes.getFlattened().size() == 3);
        assertTrue(answerTypes.getFlattened().contains(getOWLClass("A")));
        assertTrue(answerTypes.getFlattened().contains(getOWLClass("B")));
        assertTrue(answerTypes.getFlattened().contains(getDataFactory().getOWLThing()));
    }

    public void testGetInstances() throws Exception {
        GetInstances query = new GetInstances(getKBIRI(), getOWLClass("A"));
        SetOfIndividualSynsets response = super.reasoner.answer(query);
        assertTrue(response.getFlattened().size() == 1);
        assertTrue(response.getFlattened().contains(getOWLIndividual("i")));
    }

    public void testGetInstancesViaOWLReasoner() throws Exception {
        NodeSet<OWLNamedIndividual> response = super.reasoner.getInstances(getOWLClass("A"), false);
        assertTrue(response.getFlattened().size() == 1);
        assertTrue(response.getFlattened().contains(getOWLIndividual("i")));
    }
}

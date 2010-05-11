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
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.owllink.builtin.requests.GetDifferentIndividuals;
import org.semanticweb.owlapi.owllink.builtin.requests.GetFlattenedDifferentIndividuals;
import org.semanticweb.owlapi.owllink.builtin.requests.IsEntailed;
import org.semanticweb.owlapi.owllink.builtin.response.BooleanResponse;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfIndividualSynsets;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfIndividuals;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 02.11.2009
 */
public class OWLlinkDifferentIndividualsTestCase extends AbstractOWLlinkAxiomsTestCase {

    protected Set<? extends OWLAxiom> createAxioms() {
        Set<OWLAxiom> axioms = CollectionFactory.createSet();
        axioms.add(getDataFactory().getOWLDifferentIndividualsAxiom(getOWLIndividual("A"), getOWLIndividual("B"), getOWLIndividual("C")));
        axioms.add(getDataFactory().getOWLSameIndividualAxiom(getOWLIndividual("A"), getOWLIndividual("D")));
        return axioms;
    }

    public void testAreIndividualsDisjoint() throws Exception {
        Set<OWLIndividual> indis = CollectionFactory.createSet();
        indis.add(getOWLIndividual("A"));
        indis.add(getOWLIndividual("B"));
        IsEntailed query = new IsEntailed(getKBIRI(), getDataFactory().getOWLDifferentIndividualsAxiom(indis));
        BooleanResponse answer = super.reasoner.answer(query);
        assertTrue(answer.getResult());
        indis.add(getOWLIndividual("C"));

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLDifferentIndividualsAxiom(indis));
        answer = super.reasoner.answer(query);
        assertTrue(answer.getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLSameIndividualAxiom(indis));
        answer = super.reasoner.answer(query);
        assertFalse(answer.getResult());
    }

    public void testAreIndividualsDisjointViaOWLReasoner() throws Exception {
        Set<OWLIndividual> indis = CollectionFactory.createSet();
        indis.add(getOWLIndividual("A"));
        indis.add(getOWLIndividual("B"));
        OWLAxiom axiom = getDataFactory().getOWLDifferentIndividualsAxiom(indis);
        assertTrue(super.reasoner.isEntailed(axiom));

        indis.add(getOWLIndividual("C"));

        axiom = getDataFactory().getOWLDifferentIndividualsAxiom(indis);
        assertTrue(super.reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLSameIndividualAxiom(indis);
        assertFalse(super.reasoner.isEntailed(axiom));
    }

    public void testGetDisjointIndividuals() throws Exception {
        GetDifferentIndividuals query = new GetDifferentIndividuals(getKBIRI(), getOWLIndividual("B"));
        SetOfIndividualSynsets response = super.reasoner.answer(query);
        assertTrue(response.getSynsets().size() == 2);
    }


    public void testGetDisjointIndividualsWithOWLReasoner() throws Exception {
        NodeSet<OWLNamedIndividual> response = super.reasoner.getDifferentIndividuals(getOWLIndividual("B"));
        assertTrue(response.getFlattened().size() == 2);
    }

    public void testGetFlattenedDisjointIndividuals() throws Exception {
        GetFlattenedDifferentIndividuals query = new GetFlattenedDifferentIndividuals(getKBIRI(), getOWLIndividual("B"));
        SetOfIndividuals response = super.reasoner.answer(query);

        assertTrue(response.size() == 3);
        assertTrue(response.contains(getOWLIndividual("A")));
        assertTrue(response.contains(getOWLIndividual("C")));
        assertTrue(response.contains(getOWLIndividual("D")));

    }
}

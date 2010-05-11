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
import org.semanticweb.owlapi.owllink.builtin.requests.IsEntailed;
import org.semanticweb.owlapi.owllink.builtin.response.BooleanResponse;
import org.semanticweb.owlapi.owllink.retraction.RetractRequest;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Olaf Noppens
 * Date: 28.04.2010
 */
public class OWLlinkRetractionTestCase extends AbstractOWLlinkAxiomsTestCase {

    protected Set<? extends OWLAxiom> createAxioms() {
        Set<OWLAxiom> axioms = CollectionFactory.createSet();
        axioms.add(getDataFactory().getOWLSubClassOfAxiom(getOWLClass("A"), getOWLClass("B")));
        axioms.add(getDataFactory().getOWLSubClassOfAxiom(getOWLClass("A"), getOWLClass("C")));

        axioms.add(getDataFactory().getOWLSubClassOfAxiom(getOWLClass("C"), getOWLClass("D")));

        return axioms;
    }

    public void testSingleRetraction() throws Exception {
        OWLClass A = getOWLClass("A");
        OWLClass B = getOWLClass("B");
        OWLClass C = getOWLClass("C");

        IsEntailed entailed = new IsEntailed(getKBIRI(), getDataFactory().getOWLSubClassOfAxiom(A, B));
        BooleanResponse response = reasoner.answer(entailed);
        assertTrue(response.getResult());
        entailed = new IsEntailed(getKBIRI(), getDataFactory().getOWLSubClassOfAxiom(A, C));
        reasoner.answer(entailed);
        assertTrue(response.getResult());

        Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
        axioms.add(getDataFactory().getOWLSubClassOfAxiom(A, C));
        RetractRequest request = new RetractRequest(getKBIRI(), axioms);
        reasoner.answer(request);

        entailed = new IsEntailed(getKBIRI(), getDataFactory().getOWLSubClassOfAxiom(A, C));
        response = reasoner.answer(entailed);
        assertFalse(response.getResult());
        entailed = new IsEntailed(getKBIRI(), getDataFactory().getOWLSubClassOfAxiom(A, B));
        response = reasoner.answer(entailed);
        assertTrue(response.getResult());

        super.reasoner.answer(request);


    }

}

/*
 * This file is part of the OWLlink API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, derivo GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2011, derivo GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

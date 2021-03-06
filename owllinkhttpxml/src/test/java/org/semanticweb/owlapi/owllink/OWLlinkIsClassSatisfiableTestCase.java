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
import org.semanticweb.owlapi.owllink.builtin.requests.IsClassSatisfiable;
import org.semanticweb.owlapi.owllink.builtin.response.BooleanResponse;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.Set;

/**
 * @author Olaf Noppens
 */
public class OWLlinkIsClassSatisfiableTestCase extends AbstractOWLlinkAxiomsTestCase {

    protected Set<? extends OWLAxiom> createAxioms() {
        Set<OWLAxiom> axioms = CollectionFactory.createSet();
        axioms.add(getDataFactory().getOWLSubClassOfAxiom(getOWLClass("A"), getOWLClass("B")));
        axioms.add(getDataFactory().getOWLSubClassOfAxiom(getOWLClass("C"), getDataFactory().getOWLNothing()));
        axioms.add(getDataFactory().getOWLEquivalentClassesAxiom(getOWLClass("D"), getDataFactory().getOWLNothing()));
        axioms.add(getDataFactory().getOWLEquivalentObjectPropertiesAxiom(getOWLObjectProperty("P"), getOWLObjectProperty("S")));
        return axioms;
    }

    public void testIsClassSatisfiable() throws Exception {
        IsClassSatisfiable query = new IsClassSatisfiable(getKBIRI(), getOWLClass("A"));
        BooleanResponse response = super.reasoner.answer(query);
        assertTrue(response.getResult());
        query = new IsClassSatisfiable(getKBIRI(), getOWLClass("B"));
        response = super.reasoner.answer(query);
        assertTrue(response.getResult());
        query = new IsClassSatisfiable(getKBIRI(), getOWLClass("C"));
        response = super.reasoner.answer(query);
        assertFalse(response.getResult());
        query = new IsClassSatisfiable(getKBIRI(), getOWLClass("D"));
        response = super.reasoner.answer(query);
        assertFalse(response.getResult());
        query = new IsClassSatisfiable(getKBIRI(), getDataFactory().getOWLNothing());
        response = super.reasoner.answer(query);
        assertFalse(response.getResult());
        query = new IsClassSatisfiable(getKBIRI(),
                getDataFactory().getOWLObjectIntersectionOf(getDataFactory().getOWLObjectExactCardinality(0, getOWLObjectProperty("P")),
                        getDataFactory().getOWLObjectExactCardinality(1, getOWLObjectProperty("P"))));
        response = super.reasoner.answer(query);
        assertFalse(response.getResult());
    }
}

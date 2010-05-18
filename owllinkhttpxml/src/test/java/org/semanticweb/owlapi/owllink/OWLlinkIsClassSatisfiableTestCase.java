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

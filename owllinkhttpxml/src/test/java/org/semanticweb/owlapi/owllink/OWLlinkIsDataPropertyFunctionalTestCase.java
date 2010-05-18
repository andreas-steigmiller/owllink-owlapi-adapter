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
import org.semanticweb.owlapi.owllink.builtin.requests.IsEntailed;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 02.11.2009
 */
public class OWLlinkIsDataPropertyFunctionalTestCase extends AbstractOWLlinkAxiomsTestCase {

    protected Set<? extends OWLAxiom> createAxioms() {
        Set<OWLAxiom> axiom = CollectionFactory.createSet();
        axiom.add(getDataFactory().getOWLFunctionalDataPropertyAxiom(getOWLDataProperty("A")));
        axiom.add(getDataFactory().getOWLSubDataPropertyOfAxiom(getOWLDataProperty("B"), getOWLDataProperty("A")));
        axiom.add(getDataFactory().getOWLSubDataPropertyOfAxiom(getOWLDataProperty("C"), getOWLDataProperty("D")));

        return axiom;
    }

    public void testIsFunctional() throws Exception {
        IsEntailed query = new IsEntailed(getKBIRI(), getDataFactory().getOWLFunctionalDataPropertyAxiom(
                getOWLDataProperty("A")));
        assertTrue(reasoner.answer(query).getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLFunctionalDataPropertyAxiom(
                getOWLDataProperty("B")));
        assertTrue(reasoner.answer(query).getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLFunctionalDataPropertyAxiom(
                getOWLDataProperty("C")));
        assertFalse(reasoner.answer(query).getResult());
    }

    public void testIsFunctionalViaOWLReasoner() throws Exception {
        OWLAxiom axiom = getDataFactory().getOWLFunctionalDataPropertyAxiom(
                getOWLDataProperty("A"));
        assertTrue(reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLFunctionalDataPropertyAxiom(
                getOWLDataProperty("B"));
        assertTrue(reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLFunctionalDataPropertyAxiom(
                getOWLDataProperty("C"));
        assertFalse(reasoner.isEntailed(axiom));
    }

}

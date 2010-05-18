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
public class OWLlinkIsObjectPropertyIsXTestCase extends AbstractOWLlinkAxiomsTestCase {
    protected Set<? extends OWLAxiom> createAxioms() {
        Set<OWLAxiom> axiom = CollectionFactory.createSet();
        axiom.add(getDataFactory().getOWLFunctionalObjectPropertyAxiom(getOWLObjectProperty("A")));
        axiom.add(getDataFactory().getOWLInverseFunctionalObjectPropertyAxiom(getOWLObjectProperty("B")));
        axiom.add(getDataFactory().getOWLReflexiveObjectPropertyAxiom(getOWLObjectProperty("C")));
        axiom.add(getDataFactory().getOWLIrreflexiveObjectPropertyAxiom(getOWLObjectProperty("D")));
        axiom.add(getDataFactory().getOWLAsymmetricObjectPropertyAxiom(getOWLObjectProperty("E")));
        axiom.add(getDataFactory().getOWLTransitiveObjectPropertyAxiom(getOWLObjectProperty("F")));

        return axiom;
    }

    public void testIsFunctional() throws Exception {
        IsEntailed query = new IsEntailed(getKBIRI(), getDataFactory().getOWLFunctionalObjectPropertyAxiom
                (getOWLObjectProperty("A")));
        assertTrue(reasoner.answer(query).getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLFunctionalObjectPropertyAxiom
                (getOWLObjectProperty("B")));
        assertFalse(reasoner.answer(query).getResult());
    }

    public void testIsFunctionalViaOWLReasoner() throws Exception {
        OWLAxiom axiom = getDataFactory().getOWLFunctionalObjectPropertyAxiom
                (getOWLObjectProperty("A"));
        assertTrue(reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLFunctionalObjectPropertyAxiom
                (getOWLObjectProperty("B"));
        assertFalse(reasoner.isEntailed(axiom));
    }

    public void testIsInverseFunctional() throws Exception {
        IsEntailed query = new IsEntailed(getKBIRI(), getDataFactory().
                getOWLInverseFunctionalObjectPropertyAxiom(getOWLObjectProperty("B")));
        assertTrue(reasoner.answer(query).getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().
                getOWLInverseFunctionalObjectPropertyAxiom(getOWLObjectProperty("A")));
        assertFalse(reasoner.answer(query).getResult());
    }

    public void testIsInverseFunctionalViaOWLReasoner() throws Exception {
        OWLAxiom axiom = getDataFactory().
                getOWLInverseFunctionalObjectPropertyAxiom(getOWLObjectProperty("B"));
        assertTrue(reasoner.isEntailed(axiom));

        axiom = getDataFactory().
                getOWLInverseFunctionalObjectPropertyAxiom(getOWLObjectProperty("A"));
        assertFalse(reasoner.isEntailed(axiom));
    }

    public void testIsReflexive() throws Exception {
        IsEntailed query = new IsEntailed(getKBIRI(), getDataFactory().getOWLReflexiveObjectPropertyAxiom(
                getOWLObjectProperty("C")));
        assertTrue(reasoner.answer(query).getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLReflexiveObjectPropertyAxiom(
                getOWLObjectProperty("D")));
        assertFalse(reasoner.answer(query).getResult());
    }

    public void testIsReflexiveViaOWLReasoner() throws Exception {
        OWLAxiom axiom = getDataFactory().getOWLReflexiveObjectPropertyAxiom(
                getOWLObjectProperty("C"));
        assertTrue(reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLReflexiveObjectPropertyAxiom(
                getOWLObjectProperty("D"));
        assertFalse(reasoner.isEntailed(axiom));
    }

    public void testIsIrreflexive() throws Exception {
        IsEntailed query = new IsEntailed(getKBIRI(), getDataFactory().getOWLIrreflexiveObjectPropertyAxiom(
                getOWLObjectProperty("D")));
        assertTrue(reasoner.answer(query).getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLIrreflexiveObjectPropertyAxiom(
                getOWLObjectProperty("A")));
        assertFalse(reasoner.answer(query).getResult());
    }

    public void testIsIrreflexiveViaOWLReasoner() throws Exception {
        OWLAxiom axiom = getDataFactory().getOWLIrreflexiveObjectPropertyAxiom(
                getOWLObjectProperty("D"));
        assertTrue(reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLIrreflexiveObjectPropertyAxiom(
                getOWLObjectProperty("A"));
        assertFalse(reasoner.isEntailed(axiom));
    }

    public void testIsAsymmetric() throws Exception {
        IsEntailed query = new IsEntailed(getKBIRI(), getDataFactory().getOWLAsymmetricObjectPropertyAxiom(
                getOWLObjectProperty("E")));
        assertTrue(reasoner.answer(query).getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLAsymmetricObjectPropertyAxiom(
                getOWLObjectProperty("F")));
        assertFalse(reasoner.answer(query).getResult());
    }

    public void testIsAsymmetricViaOWLReasoner() throws Exception {
        OWLAxiom axiom = getDataFactory().getOWLAsymmetricObjectPropertyAxiom(
                getOWLObjectProperty("E"));
        assertTrue(reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLAsymmetricObjectPropertyAxiom(
                getOWLObjectProperty("F"));
        assertFalse(reasoner.isEntailed(axiom));
    }

    public void testIsTranstive() throws Exception {
        IsEntailed query = new IsEntailed(getKBIRI(), getDataFactory().getOWLTransitiveObjectPropertyAxiom(
                getOWLObjectProperty("F")));
        assertTrue(reasoner.answer(query).getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLTransitiveObjectPropertyAxiom(
                getOWLObjectProperty("E")));
        assertFalse(reasoner.answer(query).getResult());
    }

    public void testIsTranstiveViaOWLReasoner() throws Exception {
        OWLAxiom axiom = getDataFactory().getOWLTransitiveObjectPropertyAxiom(
                getOWLObjectProperty("F"));
        assertTrue(reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLTransitiveObjectPropertyAxiom(
                getOWLObjectProperty("E"));
        assertFalse(reasoner.isEntailed(axiom));
    }
}

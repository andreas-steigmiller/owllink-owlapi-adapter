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
import org.semanticweb.owlapi.owllink.builtin.requests.GetDisjointObjectProperties;
import org.semanticweb.owlapi.owllink.builtin.requests.IsEntailed;
import org.semanticweb.owlapi.owllink.builtin.response.BooleanResponse;
import org.semanticweb.owlapi.owllink.builtin.response.ObjectPropertySynsets;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 02.11.2009
 */
public class OWLlinkDisjointObjectPropertiesTestCase extends AbstractOWLlinkAxiomsTestCase {
    protected Set<? extends OWLAxiom> createAxioms() {
        Set<OWLAxiom> axioms = CollectionFactory.createSet();
        axioms.add(getDataFactory().getOWLDisjointObjectPropertiesAxiom(getOWLObjectProperty("A"), getOWLObjectProperty("B"), getOWLObjectProperty("C")));
        axioms.add(getDataFactory().getOWLSubObjectPropertyOfAxiom(getOWLObjectProperty("A"), getOWLObjectProperty("D")));
        return axioms;
    }

    public void testAreObjectPropertiesDisjoint() throws Exception {
        IsEntailed query = new IsEntailed(getKBIRI(), getDataFactory().getOWLDisjointObjectPropertiesAxiom(getOWLObjectProperty("A"), getOWLObjectProperty("B")));
        BooleanResponse answer = super.reasoner.answer(query);
        assertTrue(answer.getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLDisjointObjectPropertiesAxiom(getOWLObjectProperty("A"), getOWLObjectProperty("B"), getOWLObjectProperty("C")));
        answer = super.reasoner.answer(query);
        assertTrue(answer.getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLDisjointObjectPropertiesAxiom(getOWLObjectProperty("A"), getOWLObjectProperty("B"), getOWLObjectProperty("E")));
        answer = super.reasoner.answer(query);
        assertFalse(answer.getResult());


        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLDisjointObjectPropertiesAxiom(getOWLObjectProperty("A"), getOWLObjectProperty("B"), getOWLObjectProperty("C")));
        answer = super.reasoner.answer(query);
        assertFalse(answer.getResult());
    }

    public void testAreObjectPropertiesDisjointViaOWLReasoner() throws Exception {
        OWLAxiom axiom = getDataFactory().getOWLDisjointObjectPropertiesAxiom(getOWLObjectProperty("A"), getOWLObjectProperty("B"));
        assertTrue(reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLDisjointObjectPropertiesAxiom(getOWLObjectProperty("A"), getOWLObjectProperty("B"), getOWLObjectProperty("C"));
        assertTrue(reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLDisjointObjectPropertiesAxiom(getOWLObjectProperty("A"), getOWLObjectProperty("B"), getOWLObjectProperty("E"));
        assertFalse(reasoner.isEntailed(axiom));


        axiom = getDataFactory().getOWLDisjointObjectPropertiesAxiom(getOWLObjectProperty("A"), getOWLObjectProperty("B"), getOWLObjectProperty("C"));
        assertFalse(reasoner.isEntailed(axiom));
    }

    public void testGetDisjointObjectProperties() throws Exception {
        GetDisjointObjectProperties query = new GetDisjointObjectProperties(getKBIRI(), getOWLObjectProperty("B"));
        ObjectPropertySynsets response = super.reasoner.answer(query);
        assertTrue(response.getNodes().size() == 2);
    }

    public void testGetDisjointObjectPropertiesViaOWLReasoner() throws Exception {
        NodeSet<OWLObjectProperty> response = super.reasoner.getDisjointObjectProperties(getOWLObjectProperty("B"), false);
        assertTrue(response.getNodes().size() == 2);
    }
}

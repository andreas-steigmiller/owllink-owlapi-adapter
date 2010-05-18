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
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.owllink.builtin.requests.GetDisjointDataProperties;
import org.semanticweb.owlapi.owllink.builtin.requests.IsEntailed;
import org.semanticweb.owlapi.owllink.builtin.response.BooleanResponse;
import org.semanticweb.owlapi.owllink.builtin.response.DataPropertySynsets;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 02.11.2009
 */
public class OWLlinkDisjointDataPropertiesTestCase extends AbstractOWLlinkAxiomsTestCase {

    protected Set<? extends OWLAxiom> createAxioms() {
        Set<OWLAxiom> axioms = CollectionFactory.createSet();
        axioms.add(getDataFactory().getOWLDisjointDataPropertiesAxiom(getOWLDataProperty("A"), getOWLDataProperty("B"), getOWLDataProperty("C")));
        axioms.add(getDataFactory().getOWLSubDataPropertyOfAxiom(getOWLDataProperty("A"), getOWLDataProperty("D")));
        return axioms;
    }

    public void testAreDataPropertiesDisjoint() throws Exception {
        IsEntailed query = new IsEntailed(getKBIRI(), getDataFactory().getOWLDisjointDataPropertiesAxiom(getOWLDataProperty("A"), getOWLDataProperty("B")));
        BooleanResponse answer = super.reasoner.answer(query);
        assertTrue(answer.getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLDisjointDataPropertiesAxiom(getOWLDataProperty("A"), getOWLDataProperty("B"), getOWLDataProperty("C")));
        answer = super.reasoner.answer(query);
        assertTrue(answer.getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLDisjointDataPropertiesAxiom(getOWLDataProperty("A"), getOWLDataProperty("B"), getOWLDataProperty("E")));
        answer = super.reasoner.answer(query);
        assertFalse(answer.getResult());
    }

    public void testAreDataPropertiesDisjointViaOWLReasoner() throws Exception {
        OWLAxiom axiom = getDataFactory().getOWLDisjointDataPropertiesAxiom(getOWLDataProperty("A"), getOWLDataProperty("B"));
        assertTrue(super.reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLDisjointDataPropertiesAxiom(getOWLDataProperty("A"), getOWLDataProperty("B"), getOWLDataProperty("C"));
        assertTrue(super.reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLDisjointDataPropertiesAxiom(getOWLDataProperty("A"), getOWLDataProperty("B"), getOWLDataProperty("E"));
        assertFalse(super.reasoner.isEntailed(axiom));
    }

    public void testGetDisjointDataProperties() throws Exception {
        GetDisjointDataProperties query = new GetDisjointDataProperties(getKBIRI(), getOWLDataProperty("B"));
        DataPropertySynsets response = super.reasoner.answer(query);
        assertTrue(response.getNodes().size() == 2);
    }

    public void testGetDisjointDataPropertiesViaOWLReasoner() throws Exception {
        NodeSet<OWLDataProperty> response = super.reasoner.getDisjointDataProperties(getOWLDataProperty("B"), false);
        assertTrue(response.getNodes().size() == 2);
    }
}

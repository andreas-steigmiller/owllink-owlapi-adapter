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
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.owllink.builtin.requests.GetDisjointClasses;
import org.semanticweb.owlapi.owllink.builtin.requests.IsEntailed;
import org.semanticweb.owlapi.owllink.builtin.response.BooleanResponse;
import org.semanticweb.owlapi.owllink.builtin.response.ClassSynsets;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 02.11.2009
 */
public class OWLlinkDisjointClassesTestCase extends AbstractOWLlinkAxiomsTestCase {

    protected Set<? extends OWLAxiom> createAxioms() {
        Set<OWLAxiom> axioms = CollectionFactory.createSet();
        axioms.add(getDataFactory().getOWLDisjointClassesAxiom(getOWLClass("A"), getOWLClass("B"), getOWLClass("C")));
        axioms.add(getDataFactory().getOWLSubClassOfAxiom(getOWLClass("A"), getOWLClass("D")));
        return axioms;
    }

    public void testAreClassesDisjoint() throws Exception {
        Set<OWLClassExpression> classes = CollectionFactory.createSet();
        classes.add(getOWLClass("A"));
        classes.add(getOWLClass("B"));
        IsEntailed query = new IsEntailed(getKBIRI(), getDataFactory().getOWLDisjointClassesAxiom(classes));
        BooleanResponse answer = super.reasoner.answer(query);
        assertTrue(answer.getResult());
        classes.add(getOWLClass("C"));
        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLDisjointClassesAxiom(classes));
        answer = super.reasoner.answer(query);
        assertTrue(answer.getResult());
        classes.add(getOWLClass("D"));
        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLDisjointClassesAxiom(classes));
        answer = super.reasoner.answer(query);
        assertFalse(answer.getResult());
    }

    public void testAreClassesDisjointViaOWLReasoner() throws Exception {
        Set<OWLClassExpression> classes = CollectionFactory.createSet();
        classes.add(getOWLClass("A"));
        classes.add(getOWLClass("B"));
        OWLAxiom axiom = getDataFactory().getOWLDisjointClassesAxiom(classes);
        assertTrue(super.reasoner.isEntailed(axiom));

        classes.add(getOWLClass("C"));
        axiom = getDataFactory().getOWLDisjointClassesAxiom(classes);
        assertTrue(super.reasoner.isEntailed(axiom));
        classes.add(getOWLClass("D"));
        axiom = getDataFactory().getOWLDisjointClassesAxiom(classes);
        assertFalse(super.reasoner.isEntailed(axiom));
    }

    public void testGetDisjointClasses() throws Exception {
        GetDisjointClasses query = new GetDisjointClasses(getKBIRI(), getOWLClass("B"));
        ClassSynsets response = super.reasoner.answer(query);
        assertTrue(response.getNodes().size() == 3);
    }

    public void testGetDisjointClassesViaOWLReasoner() throws Exception {
        NodeSet<OWLClass> response = super.reasoner.getDisjointClasses(getOWLClass("B"), false);
        assertTrue(response.getNodes().size() == 3);
    }
}

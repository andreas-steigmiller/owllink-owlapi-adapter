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
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChangeException;
import org.semanticweb.owlapi.owllink.builtin.requests.CreateKB;
import org.semanticweb.owlapi.owllink.builtin.requests.ReleaseKB;
import org.semanticweb.owlapi.owllink.builtin.requests.Tell;
import org.semanticweb.owlapi.owllink.builtin.response.KB;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 02.11.2009
 */
public abstract class AbstractOWLlinkAxiomsTestCase extends AbstractOWLlinkTestCase {

    private Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
    OWLOntology ontology;

    protected void setUp() throws Exception {
        super.setUp();
        this.ontology = createOntology();
        addAxiomsToRootOntology();
    }

    protected void tearDown() throws Exception {
        removeAxiomsFromRootOntology();
        ReleaseKB releaseKB = new ReleaseKB(getKBIRI());
        this.reasoner.answer(releaseKB);
        super.tearDown();
    }

    public final OWLOntology getOntology() {
        return this.ontology;
    }

    final protected OWLOntology createOntology() {
        try {
            OWLOntology ont = getOWLOntology("Ont");
            axioms.clear();
            axioms.addAll(createAxioms());
            getManager().addAxioms(ont, axioms);

            CreateKB createKB = new CreateKB();
            KB kb = reasoner.answer(createKB);
            this.reasonerIRI = kb.getKB();

            Tell tell = new Tell(getKBIRI(), axioms);
            super.reasoner.answer(tell);
            return ont;
        } catch (OWLOntologyChangeException e) {
            throw new RuntimeException(e);
        }
    }

    protected void addAxiomsToRootOntology() {
        for (OWLAxiom axiom : createAxioms())
            addAxiom(getRootOntology(), axiom);
    }

    protected void removeAxiomsFromRootOntology() {
        for (OWLAxiom axiom : createAxioms())
            removeAxiom(getRootOntology(), axiom);
    }

    protected abstract Set<? extends OWLAxiom> createAxioms();

}

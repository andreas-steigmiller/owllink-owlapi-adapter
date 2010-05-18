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

import junit.framework.TestCase;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.BufferingMode;

import java.util.Set;

/**
 * @author Olaf Noppens
 */
public abstract class AbstractOWLlinkTestCase extends TestCase {

    protected OWLlinkReasoner reasoner;
    protected OWLDataFactory dataFactory;
    protected OWLOntologyManager manager;
    private IRI uriBase;
    protected IRI reasonerIRI;
    OWLOntology rootOntology;


    protected void setUp() throws Exception {
        this.manager = OWLManager.createOWLOntologyManager();
        rootOntology = this.manager.createOntology();
        reasoner = new OWLlinkHTTPXMLReasoner(rootOntology, new OWLlinkReasonerConfiguration(), BufferingMode.NON_BUFFERING);
        uriBase = IRI.create("http://www.semanticweb.org/owlapi/owllink/test");
        reasonerIRI = uriBase;
    }

    public IRI getKBIRI() {
        return this.reasonerIRI;
    }


    public OWLOntology getRootOntology() {
        return this.rootOntology;
    }

    public OWLOntology getOWLOntology(String name) {
        try {
            IRI iri = IRI.create(uriBase + "/" + name);
            if (manager.contains(iri)) {
                return manager.getOntology(iri);
            } else {
                return manager.createOntology(iri);
            }
        }
        catch (OWLOntologyCreationException e) {
            throw new RuntimeException(e);
        }
    }


    protected void tearDown() throws Exception {
        this.manager.removeOntology(rootOntology);
        super.tearDown();
    }

    protected OWLOntologyManager getManager() {
        return this.manager;
    }

    protected OWLDataFactory getDataFactory() {
        return getManager().getOWLDataFactory();
    }


    public OWLClass getOWLClass(String name) {
        return getDataFactory().getOWLClass(IRI.create(uriBase + "#" + name));
    }


    public OWLObjectProperty getOWLObjectProperty(String name) {
        return getDataFactory().getOWLObjectProperty(IRI.create(uriBase + "#" + name));
    }


    public OWLDataProperty getOWLDataProperty(String name) {
        return getDataFactory().getOWLDataProperty(IRI.create(uriBase + "#" + name));
    }


    public OWLNamedIndividual getOWLIndividual(String name) {
        return getDataFactory().getOWLNamedIndividual(IRI.create(uriBase + "#" + name));
    }

    public OWLDatatype getOWLDatatype(String name) {
        return getDataFactory().getOWLDatatype(IRI.create(uriBase + "#" + name));
    }

    public OWLAnnotationProperty getOWLAnnotationProperty(String name) {
        return getDataFactory().getOWLAnnotationProperty(IRI.create(uriBase + "#" + name));
    }

    public OWLLiteral getLiteral(int value) {
        return getDataFactory().getOWLTypedLiteral(value);
    }


    public void addAxiom(OWLOntology ont, OWLAxiom ax) {
        try {
            manager.addAxiom(ont, ax);
        }
        catch (OWLOntologyChangeException e) {
            fail(e.getMessage() + " " + e.getStackTrace().toString());
        }
    }

    public void removeAxiom(OWLOntology ont, OWLAxiom ax) {
        try {
            manager.removeAxiom(ont, ax);
        }
        catch (OWLOntologyChangeException e) {
            fail(e.getMessage() + " " + e.getStackTrace().toString());
        }
    }

    protected abstract Set<? extends OWLAxiom> createAxioms();


}

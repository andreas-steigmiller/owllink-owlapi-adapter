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

package org.semanticweb.owlapi.owllink.retraction;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.owllink.builtin.requests.AbstractKBRequest;
import org.semanticweb.owlapi.owllink.builtin.requests.RequestVisitor;
import org.semanticweb.owlapi.owllink.builtin.response.OK;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/**
 * Author: Olaf Noppens
 * Date: 28.04.2010
 */
public class RetractRequest extends AbstractKBRequest<OK> implements Iterable<OWLAxiom> {
    public static final IRI EXTENSION_IRI = IRI.create("http://www.owllink.org/ext/retraction");
    private Set<OWLAxiom> axioms;

    public RetractRequest(IRI kb, Set<OWLAxiom> axioms) {
        super(kb);
        this.axioms = axioms;
    }

    public Set<OWLAxiom> getAxioms() {
        return Collections.unmodifiableSet(this.axioms);
    }

    public Iterator<OWLAxiom> iterator() {
        return this.axioms.iterator();
    }

    public void accept(RequestVisitor visitor) {
        visitor.answer(this);
    }
}

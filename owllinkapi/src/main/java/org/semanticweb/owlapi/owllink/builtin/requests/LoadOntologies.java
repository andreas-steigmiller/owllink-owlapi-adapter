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

package org.semanticweb.owlapi.owllink.builtin.requests;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.owllink.builtin.response.OK;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Represents a <a href="http://www.owllink.org/owllink-20091116/#Tell">LoadOntologies</a>
 * request in the OWLlink specification.
 *
 * Author: Olaf Noppens
 * Date: 23.10.2009
 */
public class LoadOntologies extends AbstractKBRequest<OK> {
    protected boolean considerImports;
    protected java.util.Set<IRI> ontologyIRIs;
    protected List<IRIMapping> irimapping;


    public LoadOntologies(IRI kb, java.util.Set<IRI> ontologyIRIs, List<IRIMapping> iriMapping, boolean considerImports) {
        super(kb);
        this.ontologyIRIs = Collections.unmodifiableSet(new HashSet<IRI>(ontologyIRIs));
        this.irimapping = (iriMapping == null ? Collections.<IRIMapping>emptyList() : Collections.unmodifiableList(iriMapping));
        this.considerImports = considerImports;
    }

    public LoadOntologies(IRI kb, java.util.Set<IRI> ontologyIRIs) {
        this(kb, ontologyIRIs, null, false);
    }

    public LoadOntologies(IRI kb, IRI... ontologyIRIs) {
        this(kb, CollectionFactory.createSet(ontologyIRIs), null, false);
    }

    public boolean isConsideringImports() {
        return this.considerImports;
    }

    public java.util.Set<IRI> getOntologyIRIs() {
        return this.ontologyIRIs;
    }

    public void accept(RequestVisitor visitor) {
        visitor.answer(this);
    }

    public List<IRIMapping> getIRIMapping() {
        final List<IRIMapping> mapping = new ArrayList<IRIMapping>();
        mapping.addAll(this.irimapping);
        return mapping;
    }
}

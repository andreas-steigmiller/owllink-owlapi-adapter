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
import org.semanticweb.owlapi.owllink.Request;
import org.semanticweb.owlapi.owllink.builtin.response.KB;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a <a href="http://www.owllink.org/owllink-20091116/#CreateKB">CreateKB</a>
 * request in the OWLlink specification.
 * Author: Olaf Noppens
 * Date: 23.10.2009
 */
public class CreateKB implements Request<KB> {
    final String name;
    final IRI kb;
    Map<String, String> prefixes;

    public CreateKB() {
        this(null, null, null);
    }

    public CreateKB(IRI kb, String name) {
        this.kb = kb;
        this.name = name;
    }

    public CreateKB(IRI kb, String name, Map<String, String> prefixes) {
        this.kb = kb;
        this.name = name;
        this.prefixes = prefixes == null ? Collections.<String, String>emptyMap() : new HashMap<String, String>(prefixes);
    }

    public CreateKB(IRI kb) {
        this(kb, null, null);
    }

    public CreateKB(IRI kb, Map<String, String> prefixes) {
        this(kb, null, prefixes);
    }

    public CreateKB(Map<String, String> prefixes) {
        this(null, null, prefixes);
    }

    public IRI getKB() {
        return this.kb;
    }

    public String getName() {
        return this.name;
    }

    public boolean hasName() {
        return getName() != null;
    }

    public Map<String, String> getPrefixes() {
        return Collections.unmodifiableMap(this.prefixes);
    }

    public void accept(RequestVisitor visitor) {
        visitor.answer(this);
    }
}

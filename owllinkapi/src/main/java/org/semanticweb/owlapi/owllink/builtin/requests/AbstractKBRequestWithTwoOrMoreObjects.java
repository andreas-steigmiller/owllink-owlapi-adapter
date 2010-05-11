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
import org.semanticweb.owlapi.owllink.builtin.response.KBResponse;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Author: Olaf Noppens
 * Date: 23.10.2009
 */
public abstract class AbstractKBRequestWithTwoOrMoreObjects<R extends KBResponse, O> extends AbstractKBRequest<R> implements Iterable<O> {
    protected java.util.Set<O> elements;

    public AbstractKBRequestWithTwoOrMoreObjects(IRI kb, Collection<O> elements) {
        super(kb);
        if (elements.size() < 2)
            throw new IllegalArgumentException("At least two elements must be in the argument but there are only " + elements.size());
        this.elements = Collections.unmodifiableSet(new HashSet<O>(elements));
    }

    public AbstractKBRequestWithTwoOrMoreObjects(IRI kb, O... elem) {
        super(kb);
        this.elements = Collections.unmodifiableSet(CollectionFactory.createSet(elem));
        if (elements.size() < 2)
            throw new IllegalArgumentException("At least two elements must be in the argument but there are only " + elements.size());
    }

    public java.util.Set<O> getElements() {
        return this.elements;
    }

    public Iterator<O> iterator() {
        return this.elements.iterator();
    }
}

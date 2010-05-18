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

package org.semanticweb.owlapi.owllink.builtin.response;

import org.semanticweb.owlapi.model.OWLLogicalEntity;
import org.semanticweb.owlapi.reasoner.Node;

import java.util.Collections;
import java.util.Set;

/**
 * Author: Olaf Noppens
 * Date: 02.11.2009
 */
public abstract class HierarchyImpl<O extends OWLLogicalEntity> extends KBResponseImpl implements Hierarchy<O> {
    final Set<HierarchyPair<O>> pairs;
    final Node<O> unsatisfiables;

    public HierarchyImpl(Set<HierarchyPair<O>> pairs, Node<O> unsatisfiables, String warning) {
        super(warning);
        this.pairs = Collections.unmodifiableSet(pairs);
        this.unsatisfiables = unsatisfiables;
    }

    public HierarchyImpl(Set<HierarchyPair<O>> pairs, Node<O> unsatisfiables) {
        this(pairs, unsatisfiables, null);
    }

    public Node<O> getUnsatisfiables() {
        return this.unsatisfiables;
    }

    public Set<HierarchyPair<O>> getPairs() {
        return this.pairs;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HierarchyImpl)) return false;

        HierarchyImpl hierarchy = (HierarchyImpl) o;

        if (!pairs.equals(hierarchy.pairs)) return false;

        return true;
    }

    public int hashCode() {
        return pairs.hashCode();
    }
}

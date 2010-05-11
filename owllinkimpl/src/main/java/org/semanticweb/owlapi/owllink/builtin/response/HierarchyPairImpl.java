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

package org.semanticweb.owlapi.owllink.builtin.response;

import org.semanticweb.owlapi.model.OWLLogicalEntity;
import org.semanticweb.owlapi.reasoner.Node;

/**
 * Author: Olaf Noppens
 * Date: 02.11.2009
 */
public class HierarchyPairImpl<O extends OWLLogicalEntity> implements HierarchyPair<O> {
    private final Node<O> superSynset;
    private final SubEntitySynsets<O> subs;

    public HierarchyPairImpl(Node<O> superSynset, SubEntitySynsets<O> subs) {
        this.superSynset = superSynset;
        this.subs = subs;
    }

    public Node<O> getSuper() {
        return this.superSynset;
    }

    public SubEntitySynsets<O> getSubs() {
        return this.subs;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HierarchyPairImpl)) return false;

        HierarchyPairImpl that = (HierarchyPairImpl) o;

        if (!subs.equals(that.subs)) return false;
        if (!superSynset.equals(that.superSynset)) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = superSynset.hashCode();
        result = 31 * result + subs.hashCode();
        return result;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HierarchyPair[ ");
        sb.append(superSynset.toString());
        sb.append(", ");
        sb.append(subs.toString());
        sb.append("]");
        return sb.toString();
    }

}

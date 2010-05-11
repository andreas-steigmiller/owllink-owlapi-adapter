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

import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.impl.OWLObjectPropertyNodeSet;

import java.util.Set;

/**
 * Author: Olaf Noppens
 * Date: 18.02.2010
 */
public class SubObjectPropertySynsets extends OWLObjectPropertyNodeSet implements SubEntitySynsets<OWLObjectProperty> {

    public SubObjectPropertySynsets(OWLObjectProperty entity) {
        super(entity);
    }

    public SubObjectPropertySynsets(Node<OWLObjectProperty> owlObjectPropertyNode) {
        super(owlObjectPropertyNode);
    }

    public SubObjectPropertySynsets(Set<Node<OWLObjectProperty>> nodes) {
        super(nodes);
    }

    @Override
    public int hashCode() {
        return getNodes().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SubObjectPropertySynsets)) {
            return false;
        }
        SubObjectPropertySynsets other = (SubObjectPropertySynsets) obj;
        return other.getNodes().equals(getNodes());
    }
}

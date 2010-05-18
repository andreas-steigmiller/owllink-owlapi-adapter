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

import java.util.Set;

/**
 * Abstract Hierarchy object for OWLLogicalEntities (e.g. classes, properties).
 * <p/>
 * Author: Olaf Noppens
 */
public interface Hierarchy<O extends OWLLogicalEntity> extends KBResponse {

    /**
     * Returns the set of {@link org.semanticweb.owlapi.owllink.builtin.response.HierarchyPair HierarchyPairs}.
     *
     * @return Set of HierarhcyPair object, always not-null.
     */
    Set<HierarchyPair<O>> getPairs();

    /**
     * returns the synset of unsatisfiable elements. Note that the returned Node must not be null
     * nor as it represents a Synset object in OWLlink must not be empty.
     *
     * @return non-empty Node object, always not-null.
     */
    Node<O> getUnsatisfiables();
}

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

import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.reasoner.NodeSet;

import java.util.Set;

/**
 * Represents a <a href="http://www.owllink.org/owllink-20091116/#IndividualDisjoint">SetOfIndividualSynsets</a>
 * in the OWLlink specification.
 *
 * Author: Olaf Noppens
 * Date: 24.11.2009
 */
public interface SetOfIndividualSynsets extends KBResponse {

    /**
     * A convenience method that gets all of the entities contained in the <code>Nodes</code> in this <code>NodeSet</code>.
     *
     * @return The union of the entities contained in the <code>Nodes</code> in this <code>NodeSet</code>.
     */
    Set<OWLIndividual> getFlattened();

    boolean isEmpty();


    /**
     * Determines if this <code>NodeSet</code> is a singleton.  A <code>NodeSet</code> is a singleton if it contains
     * only one <code>Node</code>.
     *
     * @return <code>true</code> if this <code>NodeSet</code> is a singleton, otherwise <code>false</code>.
     */
    boolean isSingleton();

    Set<IndividualSynset> getSynsets();

    boolean isNode();

    NodeSet<OWLNamedIndividual> asNode();

    int getSize();
}

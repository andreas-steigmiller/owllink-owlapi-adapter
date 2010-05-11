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

package org.semanticweb.owlapi.owllink;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.PrefixManager;

/**
 * @author Olaf Noppens
 */
public interface PrefixManagerProvider {

    /**
     * Returns a {@link org.semanticweb.owlapi.model.PrefixManager PrefixManager}
     * for the given knowledge base. Returns <code>null</code> if the knowledge base
     * is unknown.
     *
     * @param knowledgeBase knowledge base IRI
     * @return PrefixManager
     */
    PrefixManager getPrefixes(IRI knowledgeBase);

    /**
     * Add the given PrefixManager for the knowledgeBase (represented by its IRI).
     *
     * @param knowledgeBase IRI
     * @param manager   PrefixManager
     */
    void putPrefixes(IRI knowledgeBase, PrefixManager manager);

    /**
     * Removes all prefixed for the given knowledgeBase.
     *
     * @param knowledgeBase whose prefixes should be removed.
     */
    void removePrefixes(IRI knowledgeBase);

    /**
     * Returns <code>true</code< if a prefix for the IRI is given, otherwise <code>false</code>
     *
     * @param knowledgeBase IRI of the knowledgebase
     * @return true if the IRI is given
     */
    boolean contains(IRI knowledgeBase);

}

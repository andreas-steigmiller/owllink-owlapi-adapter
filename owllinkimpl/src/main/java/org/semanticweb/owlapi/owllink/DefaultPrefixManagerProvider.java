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
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.Map;

/**
 * Author: Olaf Noppens
 * Date: 30.11.2009
 */
public class DefaultPrefixManagerProvider implements PrefixManagerProvider {
    private Map<IRI, PrefixManager> prefixManagerByKB;

    public DefaultPrefixManagerProvider() {
        this.prefixManagerByKB = CollectionFactory.createMap();
    }

    public PrefixManager getPrefixes(IRI knowledgeBase) {
        return this.prefixManagerByKB.get(knowledgeBase);
    }

    public void putPrefixes(IRI knowledgeBase, PrefixManager manager) {
        this.prefixManagerByKB.put(knowledgeBase, manager);
    }

    public void removePrefixes(IRI knowledgeBase) {
        this.prefixManagerByKB.put(knowledgeBase, null);
    }

    public boolean contains(IRI knowledgeBase) {
        return this.prefixManagerByKB.containsKey(knowledgeBase);
    }
}

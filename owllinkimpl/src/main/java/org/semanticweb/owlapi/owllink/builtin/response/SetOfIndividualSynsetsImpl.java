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

import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.impl.OWLNamedIndividualNodeSet;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Olaf Noppens
 * Date: 24.11.2009
 */
public class SetOfIndividualSynsetsImpl extends KBResponseImpl implements SetOfIndividualSynsets {
    Set<IndividualSynset> synsets;

    public SetOfIndividualSynsetsImpl(Set<IndividualSynset> synonymsets) {
        this(synonymsets, null);
    }

    public SetOfIndividualSynsetsImpl(Set<IndividualSynset> synonymsets, String warning) {
        super(warning);
        this.synsets = new HashSet<IndividualSynset>(synonymsets.size());
        this.synsets.addAll(synonymsets);
    }

    public <O> O accept(ResponseVisitor<O> visitor) {
        return visitor.visit(this);
    }

    public Set<OWLIndividual> getFlattened() {
        Set<OWLIndividual> indis = new HashSet<OWLIndividual>();
        for (IndividualSynset synset : this.synsets) {
            indis.addAll(synset.getIndividuals());
        }
        return Collections.unmodifiableSet(indis);
    }

    public boolean isEmpty() {
        return this.synsets.isEmpty();
    }


    public boolean isSingleton() {
        return synsets.size() == 1;
    }

    public Set<IndividualSynset> getSynsets() {
        return Collections.unmodifiableSet(this.synsets);
    }

    public boolean isNode() {
        for (IndividualSynset synset : this.synsets) {
            if (!synset.isNode()) return false;
        }
        return true;
    }

    public int getSize() {
        return this.synsets.size();
    }

    public NodeSet<OWLNamedIndividual> asNode() {
        OWLNamedIndividualNodeSet set = new OWLNamedIndividualNodeSet();
        for (IndividualSynset synset : this.synsets) {
            set.addNode(synset.asNode());
        }
        return set;
    }
}

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
import org.semanticweb.owlapi.model.OWLRuntimeException;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.impl.DefaultNode;
import org.semanticweb.owlapi.reasoner.impl.NodeFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Author: Olaf Noppens
 * Date: 18.02.2010
 */
public class IndividualSynsetImpl implements IndividualSynset {
    Set<OWLIndividual> individuals;

    public IndividualSynsetImpl(Set<OWLIndividual> individuals) {
        if (individuals.isEmpty())
            throw new IllegalArgumentException("IndividualSynsets must not be empty!");
        this.individuals = new HashSet<OWLIndividual>();
        this.individuals.addAll(individuals);
    }

    public IndividualSynsetImpl(Node<OWLNamedIndividual> node) {
        this(convert(node));
    }

    static Set<OWLIndividual> convert(Node<OWLNamedIndividual> node) {
        Set<OWLIndividual> indis = new HashSet<OWLIndividual>();
        indis.addAll(node.getEntities());
        return indis;
    }

    public Iterator<OWLIndividual> iterator() {
        return this.individuals.iterator();
    }

    public boolean isSingleton() {
        return this.individuals.size() == 1;
    }

    public OWLIndividual getSingletonElement() {
        return this.individuals.iterator().next();
    }

    public Set<OWLIndividual> getIndividuals() {
        return Collections.unmodifiableSet(this.individuals);
    }

    public boolean contains(OWLIndividual individual) {
        return this.individuals.contains(individual);
    }

    public boolean isNode() {
        for (OWLIndividual indi : this.individuals)
            if (indi.isAnonymous()) return false;
        return true;
    }

    public Node<OWLNamedIndividual> asNode() {
        if (!isNode())
            throw new OWLRuntimeException("Contains anonymous individuals. Conversion not possible. See isNode()");
        DefaultNode<OWLNamedIndividual> node = NodeFactory.getOWLNamedIndividualNode();
        for (OWLIndividual indi : this.individuals)
            node.add(indi.asOWLNamedIndividual());
        return node;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndividualSynsetImpl that = (IndividualSynsetImpl) o;

        if (individuals != null ? !individuals.equals(that.individuals) : that.individuals != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return individuals != null ? individuals.hashCode() : 0;
    }
}

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

import java.util.*;

/**
 * Author: Olaf Noppens
 * Date: 24.11.2009
 */
public class IndividualSynonymsImpl extends KBResponseImpl implements IndividualSynonyms {
    private Set<OWLIndividual> individuals = new HashSet<OWLIndividual>();

    public IndividualSynonymsImpl(OWLIndividual owlIndividual) {
        this(owlIndividual, null);
    }

    public IndividualSynonymsImpl(OWLIndividual owlIndividual, String warning) {
        super(warning);
        this.individuals.add(owlIndividual);
    }

    public IndividualSynonymsImpl(Collection<OWLIndividual> elements) {
        this(elements, null);
    }

    public IndividualSynonymsImpl(Collection<OWLIndividual> elements, String warning) {
        super(warning);
        if (elements.size() < 1) throw new IllegalArgumentException("size of elements must be greater than zero");
        this.individuals.addAll(elements);
    }

    public <O> O accept(ResponseVisitor<O> visitor) {
        return visitor.visit(this);
    }

    public Iterator<OWLIndividual> iterator() {
        return this.individuals.iterator();
    }

    public boolean isSingleton() {
        return individuals.size() == 1;
    }

    public OWLIndividual getSingletonElement() {
        return individuals.iterator().next();
    }

    public boolean contains(OWLIndividual individual) {
        return this.individuals.contains(individual);
    }

    public Set<OWLIndividual> getIndividuals() {
        return Collections.unmodifiableSet(this.individuals);
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
}

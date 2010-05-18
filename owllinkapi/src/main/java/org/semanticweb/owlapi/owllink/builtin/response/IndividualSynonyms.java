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
import org.semanticweb.owlapi.reasoner.Node;

import java.util.Set;

/**
 * Represents a <a href="http://www.owllink.org/owllink-20091116/#IndividualQuery"> IndivididualSynonyms</a>
 * request of the <a href="http://www.owllink.org/owllink-20091116/#IndividualQuery">OWLlink Specification</a>.
 * <p/>
 * The special treatment of individuals is necessary because OWLAPI only considered OWLNamedIndividuals in
 * responses. OWLlink, in contrast, also considers OWLAnonymousIndividuals.
 * Author: Olaf Noppens
 * Date: 24.11.2009
 */
public interface IndividualSynonyms extends KBResponse, Iterable<OWLIndividual> {

    /**
     * Determines if this set of synonyms is a singleton set.
     *
     * @return <code>true</code> if this synonym set is a singleton set, otherwise <code>false</code>
     */
    boolean isSingleton();

    /**
     * Gets the one and only element if this set of synonyms is a singleton set
     *
     * @return the one and only element if this set is a singleton set.  If this set is not a singleton set
     *         then a runtime exception will be thrown
     * @see #isSingleton()
     */
    OWLIndividual getSingletonElement();

    boolean contains(OWLIndividual individual);

    Set<OWLIndividual> getIndividuals();

    /**
     * Determines <code>true</code> if this IndividualSynonyms object can be converted to
     * a {@link org.semanticweb.owlapi.reasoner.Node Node&lt; OWLNamedIndividual &gt; } without any loss, i.e.,
     * iff the IndividualSynonyms object only contains OWLNamedIndividuals.
     *
     * @return <code>true</code> if this object can be converted to a Node, otherwise <code>false</code>
     */
    boolean isNode();

    /**
     * Converts this object to a {@link org.semanticweb.owlapi.reasoner.Node Node&lt; OWLNamedIndividual &gt; }.
     * This is only possible if {@link #isNode()} returns <code>true</code>.
     *
     * @return Node&lt;OWLNamedIndividual&gt;
     * @throws org.semanticweb.owlapi.model.OWLRuntimeException
     *          if {@link #isNode()} returns <code>false</code>
     */
    Node<OWLNamedIndividual> asNode();
}

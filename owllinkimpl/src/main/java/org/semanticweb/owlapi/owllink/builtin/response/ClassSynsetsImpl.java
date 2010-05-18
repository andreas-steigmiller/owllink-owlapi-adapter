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

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.impl.OWLClassNodeSet;

import java.util.Set;

/**
 * Author: Olaf Noppens
 * Date: 09.12.2009
 */
public class ClassSynsetsImpl extends OWLClassNodeSet implements ClassSynsets {
    private String warning;

    public ClassSynsetsImpl(Set<Node<OWLClass>> synonymsets, String warning) {
        super(synonymsets);
        this.warning = warning;
    }

    public ClassSynsetsImpl(Set<Node<OWLClass>> synonymsets) {
        this(synonymsets, null);
    }

    public boolean hasWarning() {
        return this.warning != null;
    }

    public String getWarning() {
        return warning;
    }


    public <O> O accept(ResponseVisitor<O> visitor) {
        return visitor.visit(this);
    }


    /* public static <E extends OWLLogicalEntity>Set<Node<E>> convertToNode(final Set<EntitySynset<E>> set) {
        Set<Node<E>> set1 = new Set<Node<E>>() {
            public int size() {
                return set.size();
            }

            public boolean isEmpty() {
                return size() == 0;
            }

            public boolean contains(Object o) {
                return set.contains(o);
            }

            public Iterator<Node<E>> iterator() {
                final Iterator<EntitySynset<E>> iter = set.iterator();
                return new Iterator<Node<E>>() {
                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    public Node<E> next() {
                        return iter.next();
                    }

                    public void remove() {
                    }
                };
            };

            public Object[] toArray() {
                return new Object[0];  //To change body of implemented methods use File | Settings | File Templates.
            }

            public <T> T[] toArray(T[] a) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public boolean add(Node<E> o) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public boolean remove(Object o) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public boolean containsAll(Collection<?> c) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public boolean addAll(Collection<? extends Node<E>> c) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public boolean retainAll(Collection<?> c) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public boolean removeAll(Collection<?> c) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public void clear() {
            }
        };


        Set<Node<E>> classes = new HashSet<Node<E>>();
        for (EntitySynset<E> clazz : set) {
            classes.add(clazz);
        }
        return classes;
    }*/
}

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

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.reasoner.impl.OWLClassNode;

import java.util.Set;

/**
 * Author: Olaf Noppens
 * Date: 14.12.2009
 */
public class ClassesImpl extends OWLClassNode implements Classes {
    String warning;

    public ClassesImpl(OWLClass clazz) {
        this(clazz, null);
    }

    public ClassesImpl(OWLClass clazz, String warning) {
        super(clazz);
        this.warning = warning;
    }

    public ClassesImpl(Set<OWLClass> classes) {
        this(classes, null);
    }

    public ClassesImpl(Set<OWLClass> classes, String warning) {
        super(classes);
        this.warning = warning;
        if (classes.isEmpty())
            throw new IllegalArgumentException("Classes should not be empty");
    }

    public void add(OWLClass owlClass) {
        throw new UnsupportedOperationException();
    }

    public <O> O accept(ResponseVisitor<O> visitor) {
        return visitor.visit(this);
    }

    public boolean hasWarning() {
        return warning != null;
    }

    public String getWarning() {
        return warning;
    }
}

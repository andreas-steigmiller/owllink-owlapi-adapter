/*
 * This file is part of the OWLlink API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, derivo GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2011, derivo GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.semanticweb.owlapi.owllink.builtin.requests;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.owllink.builtin.response.OK;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Represents a <a href="http://www.owllink.org/owllink-20091116/#Tell">Tell</a>
 * request in the OWLlink specification.
 *
 * Author: Olaf Noppens
 * Date: 23.10.2009
 */
public class Tell extends AbstractKBRequest<OK> implements Iterable<OWLAxiom> {
    final java.util.Set<OWLAxiom> axioms;

    public Tell(IRI kb, java.util.Set<OWLAxiom> axioms) {
        super(kb);
        if (axioms.size() < 1) throw new IllegalArgumentException("axioms must not be empty");
        this.axioms = Collections.unmodifiableSet(new HashSet<OWLAxiom>(axioms));
    }

    public java.util.Set<OWLAxiom> getAxioms() {
        return this.axioms;
    }

    public Iterator<OWLAxiom> iterator() {
        return this.axioms.iterator();
    }

    public void accept(RequestVisitor visitor) {
        visitor.answer(this);
    }
}

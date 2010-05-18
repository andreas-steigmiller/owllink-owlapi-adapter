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

package org.semanticweb.owlapi.owllink;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.owllink.builtin.requests.CreateKB;
import org.semanticweb.owlapi.owllink.builtin.requests.Tell;
import org.semanticweb.owlapi.owllink.builtin.response.KB;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 04.12.2009
 */
public class OWLlinkPrefixTestCase extends AbstractOWLlinkAxiomsTestCase {
    @Override
    protected Set<? extends OWLAxiom> createAxioms() {
        final Set<OWLAxiom> axioms = CollectionFactory.createSet();
        axioms.add(getDataFactory().getOWLSubClassOfAxiom(getOWLClass("A"), getOWLClass("B")));
        return axioms;
    }

    public void createKBWithPrefixes() throws Exception {
        Map<String, String> prefixes = CollectionFactory.createMap();
        prefixes.put("test:", "http://test/");
        prefixes.put("test2:", "http://test2/");
        CreateKB createKB = new CreateKB(prefixes);
        KB kb = reasoner.answer(createKB);
        Set<OWLAxiom> axioms = CollectionFactory.createSet();
        axioms.addAll(createAxioms());
        Tell tell = new Tell(kb.getKB(), axioms);
        reasoner.answer(tell);
    }
}

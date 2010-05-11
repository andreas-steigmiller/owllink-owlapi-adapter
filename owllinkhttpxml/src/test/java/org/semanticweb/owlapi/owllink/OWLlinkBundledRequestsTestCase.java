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
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.owllink.builtin.requests.CreateKB;
import org.semanticweb.owlapi.owllink.builtin.requests.IsClassSatisfiable;
import org.semanticweb.owlapi.owllink.builtin.requests.ReleaseKB;
import org.semanticweb.owlapi.owllink.builtin.requests.Tell;
import org.semanticweb.owlapi.owllink.builtin.response.BooleanResponse;
import org.semanticweb.owlapi.owllink.builtin.response.KB;
import org.semanticweb.owlapi.owllink.builtin.response.OK;
import org.semanticweb.owlapi.owllink.builtin.response.ResponseMessage;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 07.12.2009
 */
public class OWLlinkBundledRequestsTestCase extends AbstractOWLlinkTestCase {
    @Override
    protected Set<? extends OWLAxiom> createAxioms() {
        Set<OWLAxiom> axioms = CollectionFactory.createSet();
        axioms.add(getDataFactory().getOWLSubClassOfAxiom(getOWLClass("A"), getOWLClass("B")));
        axioms.add(getDataFactory().getOWLSubClassOfAxiom(getOWLClass("B"), getOWLClass("C")));
        return axioms;
    }

    public void testBundle() throws Exception {
        IRI kbIRI = IRI.create("http://A");
        CreateKB kb = new CreateKB(kbIRI);
        Set<OWLAxiom> axioms = CollectionFactory.createSet();
        axioms.addAll(createAxioms());
        Tell tell = new Tell(kbIRI, axioms);
        IsClassSatisfiable cs = new IsClassSatisfiable(kbIRI, getOWLClass("A"));
        ReleaseKB releaseKB = new ReleaseKB(kbIRI);

        ResponseMessage message = reasoner.answer(kb, tell, cs, releaseKB);
        KB kbAnswer = message.getResponse(kb);

        OK tellOKAnswer = message.getResponse(tell);

        BooleanResponse classSatisfiable = message.getResponse(cs);
        assertTrue(classSatisfiable.getResult());

        OK releaseOKAnswer = message.getResponse(releaseKB);


    }
}

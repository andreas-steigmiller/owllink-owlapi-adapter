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

package org.semanticweb.owlapi.owllink.server.parser;

import org.coode.owlapi.owlxmlparser.AbstractOWLAxiomElementHandler;
import org.coode.owlapi.owlxmlparser.OWLXMLParserException;
import org.coode.owlapi.owlxmlparser.OWLXMLParserHandler;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.owllink.builtin.requests.IsEntailedDirect;

/**
 * Author: Olaf Noppens
 * Date: 24.11.2009
 */
public class OWLlinkIsEntailedDirectElementHandler extends AbstractOWLlinkKBRequestElementHandler<IsEntailedDirect> {
    protected OWLAxiom axiom;

    public OWLlinkIsEntailedDirectElementHandler(OWLXMLParserHandler handler) {
        super(handler);
    }

    public void startElement(String name) throws OWLXMLParserException {
        super.startElement(name);
        this.axiom = null;
    }

    public IsEntailedDirect getOWLObject() throws OWLXMLParserException {
        if (isOWLSubClassAxiom(axiom)) {
            return new IsEntailedDirect(getKB(), (OWLSubClassOfAxiom) axiom);
        } else if (isOWLSubObjectPropertyOfAxiom(axiom)) {
            return new IsEntailedDirect(getKB(), (OWLSubObjectPropertyOfAxiom) axiom);
        } else if (isOWLSubDataPropertyOfAxiom(axiom)) {
            return new IsEntailedDirect(getKB(), (OWLSubDataPropertyOfAxiom) axiom);
        } else if (isOWLClassAssertionAxiom(axiom)) {
            return new IsEntailedDirect(getKB(), (OWLClassAssertionAxiom) axiom);
        }
        throw new IllegalArgumentException();
    }

    protected boolean isOWLSubClassAxiom(OWLAxiom axiom) {
        return axiom.isOfType(org.semanticweb.owlapi.model.AxiomType.SUBCLASS_OF);
    }

    protected boolean isOWLSubObjectPropertyOfAxiom(OWLAxiom axiom) {
        return axiom.isOfType(org.semanticweb.owlapi.model.AxiomType.SUB_OBJECT_PROPERTY);
    }

    protected boolean isOWLSubDataPropertyOfAxiom(OWLAxiom axiom) {
        return axiom.isOfType(org.semanticweb.owlapi.model.AxiomType.SUB_DATA_PROPERTY);
    }

    protected boolean isOWLClassAssertionAxiom(OWLAxiom axiom) {
        return axiom.isOfType(org.semanticweb.owlapi.model.AxiomType.CLASS_ASSERTION);
    }

    protected boolean isLegalAxiom(OWLAxiom axiom) {
        return isOWLSubClassAxiom(axiom) || isOWLSubObjectPropertyOfAxiom(axiom) ||
                isOWLSubDataPropertyOfAxiom(axiom) || isOWLClassAssertionAxiom(axiom);
    }

    @Override
    public void handleChild(AbstractOWLAxiomElementHandler handler) throws OWLXMLParserException {
        this.axiom = handler.getOWLObject();
        if (!isLegalAxiom(axiom)) {
            throw new OWLXMLParserException("Illegal axiom. Only SubClassOf, SubObjectPropertyOf, SubDataPropertyOf, ClassAssertion axioms are allowed", getLineNumber(), getColumnNumber());
        }
    }
}

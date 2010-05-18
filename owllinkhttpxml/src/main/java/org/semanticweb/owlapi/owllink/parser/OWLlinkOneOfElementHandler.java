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

package org.semanticweb.owlapi.owllink.parser;

import org.coode.owlapi.owlxmlparser.OWLXMLParserException;
import org.coode.owlapi.owlxmlparser.OWLXMLParserHandler;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.owllink.builtin.response.OWLlinkLiteral;
import org.semanticweb.owlapi.owllink.builtin.response.OWLlinkOneOf;
import org.semanticweb.owlapi.owllink.builtin.response.OWLlinkOneOfImpl;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 22.10.2009
 */
public class OWLlinkOneOfElementHandler extends OWLlinkDataRangeElementHandler<OWLlinkOneOf> {
    private IRI iri;
    Set<OWLlinkLiteral> literals;

    public OWLlinkOneOfElementHandler(OWLXMLParserHandler handler) {
        super(handler);
    }

    public void startElement(String name) throws OWLXMLParserException {
        //super.startElement(name);
        this.iri = null;
        this.literals = new HashSet<OWLlinkLiteral>();
    }

    public void attribute(String localName, String value) throws OWLParserException {
        this.iri = getIRIFromAttribute(localName, value);
    }

    public void handleChild(OWLlinkLiteralElementHandler handler) throws OWLXMLParserException {
        literals.add(handler.getOWLLinkObject());
    }

    public OWLlinkOneOf getOWLLinkObject() {
        return new OWLlinkOneOfImpl(iri, literals);
    }

    public void endElement() throws OWLXMLParserException {
        ((OWLlinkElementHandler) getParentHandler()).handleChild(this);
    }
}

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

import org.coode.owlapi.owlxmlparser.OWLXMLParserException;
import org.coode.owlapi.owlxmlparser.OWLXMLParserHandler;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary;

/**
 * Author: Olaf Noppens
 * Date: 08.12.2009
 */
public class OWLlinkPrefixElementHandler extends AbstractOWLlinkElementHandler<OWLlinkPrefixElementHandler.Prefix> {
    protected String name;
    protected IRI iri;

    public Prefix getOWLObject() throws OWLXMLParserException {
        return new Prefix(name, iri);
    }

    public OWLlinkPrefixElementHandler(OWLXMLParserHandler handler) {
        super(handler);
    }

    public void startElement(String name) throws OWLXMLParserException {
        super.startElement(name);
        this.name = null;

    }

    public void attribute(String localName, String value) throws OWLXMLParserException {
        if (OWLlinkXMLVocabulary.NAME_Attribute.getShortName().equalsIgnoreCase(localName)) {
            this.name = value;
        }
        if (OWLlinkXMLVocabulary.FULL_IRI_ATTRIBUTE.getShortName().equalsIgnoreCase(localName)) {
            this.iri = IRI.create(value);
        }
    }


    public void endElement() throws OWLXMLParserException {
        getParentHandler().handleChild(this);
    }

    public Prefix getOWLLinkObject() {
        return new Prefix(name, iri);
    }

    public static class Prefix {
        public final String name;
        public final IRI iri;

        public Prefix(String name, IRI iri) {
            this.name = name;
            this.iri = iri;
        }
    }
}
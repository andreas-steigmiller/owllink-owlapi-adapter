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

package org.semanticweb.owlapi.owllink.server.parser;

import org.coode.owlapi.owlxmlparser.OWLXMLParserException;
import org.coode.owlapi.owlxmlparser.OWLXMLParserHandler;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary;
import org.semanticweb.owlapi.owllink.builtin.requests.IRIMapping;
import org.semanticweb.owlapi.owllink.builtin.requests.LoadOntologies;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * Author: Olaf Noppens
 * Date: 28.11.2009
 */
public class OWLlinkLoadOntologiesElementHandler extends AbstractOWLlinkKBRequestElementHandler<LoadOntologies> {
    boolean considerImports = false;
    Set<IRI> ontologyIRIs;
    List<IRIMapping> mappings;

    public OWLlinkLoadOntologiesElementHandler(OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    public void startElement(String name) throws OWLXMLParserException {
        super.startElement(name);
        this.ontologyIRIs = CollectionFactory.createSet();
        this.mappings = new Vector<IRIMapping>();
    }

    @Override
    public void attribute(String localName, String value) throws OWLXMLParserException {
        super.attribute(localName, value);
        if (OWLlinkXMLVocabulary.CONSIDER_IMPORTS_ATTRIBUTE.getShortName().equals(localName)) {
            this.considerImports = Boolean.valueOf(value);
        }
    }

    @Override
    public void handleChild(OWLlinkOntologyIRIElementHandler handler) {
        ontologyIRIs.add(handler.getOWLlinkObject());
    }

    @Override
    public void handleChild(OWLlinkIRIMappingElementHandler handler) {
        mappings.add(handler.getOWLlinkObject());
    }

    public LoadOntologies getOWLObject() throws OWLXMLParserException {
        return new LoadOntologies(kb, ontologyIRIs, mappings, considerImports);
    }
}

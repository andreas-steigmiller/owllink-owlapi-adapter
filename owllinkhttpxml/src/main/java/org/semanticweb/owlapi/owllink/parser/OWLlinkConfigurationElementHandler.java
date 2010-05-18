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
import org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary;
import org.semanticweb.owlapi.owllink.builtin.response.Configuration;
import org.semanticweb.owlapi.owllink.builtin.response.OWLlinkDataRange;
import org.semanticweb.owlapi.owllink.builtin.response.OWLlinkLiteral;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: noppens
 * Date: 21.10.2009
 * Time: 17:48:24
 * To change this template use File | Settings | File Templates.
 */
public abstract class OWLlinkConfigurationElementHandler<C extends Configuration> extends AbstractOWLlinkElementHandler<C> {
    protected String key;
    protected Set<OWLlinkLiteral> values;
    protected OWLlinkDataRange type;

    public OWLlinkConfigurationElementHandler(OWLXMLParserHandler handler) {
        super(handler);
    }

    public void startElement(String name) throws OWLXMLParserException {
        super.startElement(name);
        this.key = null;
        this.type = null;
        this.values = new HashSet<OWLlinkLiteral>();
    }

    public void attribute(String localName, String value) throws OWLXMLParserException {
        if (OWLlinkXMLVocabulary.KEY_ATTRIBUTE.getShortName().equalsIgnoreCase(localName)) {
            this.key = value;
        }
    }

    public void handleChild(OWLlinkDataRangeElementHandler handler) throws OWLXMLParserException {
        this.type = handler.getOWLLinkObject();
    }

    public void handleChild(OWLlinkLiteralElementHandler handler) throws OWLXMLParserException {
        this.values.add(handler.getOWLLinkObject());
    }

    public void endElement() throws OWLXMLParserException {
        //((OWLlinkXMLParserHandler) getParentHandler()).handleChild(this);
    }
}

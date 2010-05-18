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
import org.semanticweb.owlapi.owllink.builtin.response.ProtocolVersion;
import org.semanticweb.owlapi.owllink.builtin.response.ProtocolVersionImpl;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 22.10.2009
 */
public class OWLlinkProtocolVersionElementHandler extends AbstractOWLlinkElementHandler<ProtocolVersion> {
    protected int major;
    protected int minor;

    public OWLlinkProtocolVersionElementHandler(OWLXMLParserHandler handler) {
        super(handler);
    }

    public void attribute(String localName, String value) throws OWLXMLParserException {
        if (OWLlinkXMLVocabulary.MAJOR_ATTRIBUTE.getShortName().equalsIgnoreCase(localName)) {
            this.major = Integer.parseInt(value);
        } else if (OWLlinkXMLVocabulary.MINOR_ATTRIBUTE.getShortName().equalsIgnoreCase(localName)) {
            this.minor = Integer.parseInt(value);
        }
    }

    public void startElement(String name) throws OWLXMLParserException {
        super.startElement(name);
        this.major = 0;
        this.minor = 0;
    }

    public ProtocolVersion getOWLLinkObject() {
        return new ProtocolVersionImpl(this.major, this.minor);
    }

    public void endElement() throws OWLXMLParserException {
        ((OWLlinkElementHandler) getParentHandler()).handleChild(this);
    }
}

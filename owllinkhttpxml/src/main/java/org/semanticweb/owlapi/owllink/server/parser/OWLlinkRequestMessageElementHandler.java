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
import org.semanticweb.owlapi.owllink.Request;

import java.util.List;
import java.util.Vector;

/**
 * Author: Olaf Noppens
 * Date: 25.10.2009
 */
public class OWLlinkRequestMessageElementHandler extends AbstractOWLlinkElementHandler<List<Request>>
        implements OWLlinkElementHandler<List<Request>> {
    private List<Request> requests;
    private OWLLinkRequestListener listener;

    public OWLlinkRequestMessageElementHandler(OWLXMLParserHandler handler) {
        super(handler);
    }

    public void attribute(String localName, String value) throws OWLXMLParserException {
    }

    public void startElement(String name) throws OWLXMLParserException {
        super.startElement(name);
        this.requests = new Vector<Request>();
    }

    public void handleChild(OWLlinkRequestElementHandler handler) {
        try {
            if (handler.getOWLObject() instanceof Request) {
                requests.add((Request) handler.getOWLObject());
                if (this.listener != null)
                    this.listener.requestAdded((Request) handler.getOWLlinkObject());
            }
        } catch (OWLXMLParserException e) {
        }
    }

    public void endElement() throws OWLXMLParserException {
    }

    public List<Request> getOWLObject() throws OWLXMLParserException {
        return this.requests;
    }

    public void setRequestListener(OWLLinkRequestListener listener) {
        this.listener = listener;
    }
}

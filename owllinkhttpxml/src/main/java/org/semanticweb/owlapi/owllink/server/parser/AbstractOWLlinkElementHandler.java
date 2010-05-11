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

import org.coode.owlapi.owlxmlparser.AbstractOWLElementHandler;
import org.coode.owlapi.owlxmlparser.OWLXMLParserException;
import org.coode.owlapi.owlxmlparser.OWLXMLParserHandler;

/**
 * Author: Olaf Noppens
 * Date: 25.10.2009
 */
public abstract class AbstractOWLlinkElementHandler<O> extends AbstractOWLElementHandler<O> implements OWLlinkElementHandler<O> {
    public AbstractOWLlinkElementHandler(OWLXMLParserHandler handler) {
        super(handler);
    }

    protected OWLlinkElementHandler getParentHandler() {
        return (OWLlinkElementHandler) super.getParentHandler();
    }

    public void handleChild(OWLlinkRequestElementHandler handler) {
    }

    public void handleChild(OWLlinkLiteralElementHandler handler) {
    }

    public void handleChild(OWLlinkElementHandler handler) {
    }

    public void handleChild(OWLlinkOntologyIRIElementHandler handler) {
    }

    public void handleChild(OWLlinkIRIMappingElementHandler handler) {
    }

    public void handleChild(OWLlinkPrefixElementHandler handler) {
    }

    public O getOWLlinkObject() {
        try {
            return getOWLObject();
        } catch (OWLXMLParserException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public void endElement() throws OWLXMLParserException {
        getParentHandler().handleChild(this);
    }
}

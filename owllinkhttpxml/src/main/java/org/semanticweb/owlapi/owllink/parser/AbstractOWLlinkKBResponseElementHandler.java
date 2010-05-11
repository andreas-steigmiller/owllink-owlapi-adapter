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

package org.semanticweb.owlapi.owllink.parser;

import org.coode.owlapi.owlxmlparser.OWLXMLParserException;
import org.coode.owlapi.owlxmlparser.OWLXMLParserHandler;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.owllink.KBRequest;
import org.semanticweb.owlapi.owllink.PrefixManagerProvider;
import org.semanticweb.owlapi.owllink.builtin.response.KBResponse;

/**
 * Abstract handler for all OWLlink KBResponse.
 * <p/>
 * This implementation handles the correct prefixname2prefix mapping
 * for handling abbreviated IRIs.
 * <p/>
 * Note that KBResponses that need the prefixname2prefix mapping for
 * abbreviated IRIs should derive from this abstract element handler class.
 * Otherwise the correct IRI resolution is not guaranteed.
 * <p/>
 * <p/>
 * Author: Olaf Noppens
 * Date: 30.11.2009
 */
public abstract class AbstractOWLlinkKBResponseElementHandler<R extends KBResponse> extends AbstractConfirmationElementHandler<R> {

    public AbstractOWLlinkKBResponseElementHandler(OWLXMLParserHandler handler) {
        super(handler);
    }

    public void startElement(String s) throws OWLXMLParserException {
        super.startElement(s);
        PrefixManagerProvider prefixProvider = handler.prov;
        IRI kb = getRequest().getKB();
        PrefixManager prefixes = prefixProvider.getPrefixes(kb);
        handler.setPrefixName2PrefixMap(prefixes.getPrefixName2PrefixMap());
    }

    protected KBRequest getRequest() {
        int index = handler.responseMessageHandler.getOWLLinkObject().size();
        return (KBRequest) handler.getRequest(index);
    }

}

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
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.owllink.builtin.response.Prefixes;
import org.semanticweb.owlapi.owllink.builtin.response.PrefixesImpl;
import org.semanticweb.owlapi.util.CollectionFactory;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 20.11.2009
 */
public class OWLlinkPrefixesElementHandler extends AbstractOWLlinkKBResponseElementHandler<Prefixes> {
    protected Map<String, String> mapping;

    public OWLlinkPrefixesElementHandler(OWLXMLParserHandler handler) {
        super(handler);
    }

    public void startElement(String name) throws OWLXMLParserException {
        super.startElement(name);
        this.mapping = CollectionFactory.createMap();
    }

    @Override
    public void handleChild(OWLlinkPrefixElementHandler handler) throws OWLXMLParserException {
        OWLlinkPrefixElementHandler.Prefix prefix = handler.getOWLLinkObject();
        this.mapping.put(prefix.name, prefix.iri.toString());
    }

    @Override
    public Prefixes getOWLLinkObject() {
        return new PrefixesImpl(this.mapping);
    }

    public void endElement() throws OWLXMLParserException {
        super.endElement();
        final IRI kb = getRequest().getKB();
        final DefaultPrefixManager prefixes = new DefaultPrefixManager();
        for (Map.Entry<String, String> entry : mapping.entrySet()) {
            if (!entry.getKey().endsWith(":"))
                prefixes.setPrefix(entry.getKey(), entry.getValue());
            else
                prefixes.setPrefix(entry.getKey() + ":", entry.getValue());
        }
        handler.prov.putPrefixes(kb, prefixes);
    }
}

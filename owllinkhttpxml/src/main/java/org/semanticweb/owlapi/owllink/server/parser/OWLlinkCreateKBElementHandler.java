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
import org.semanticweb.owlapi.owllink.builtin.requests.CreateKB;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.Map;
import java.util.Set;

/**
 * Author: Olaf Noppens
 * Date: 25.10.2009
 */
public class OWLlinkCreateKBElementHandler extends AbstractOWLlinkRequestElementHandler<CreateKB> {
    protected IRI kb;
    protected String name;
    protected Set<OWLlinkPrefixElementHandler.Prefix> prefixes;
    OWLlinkXMLRequestParserHandler handler;

    public OWLlinkCreateKBElementHandler(OWLXMLParserHandler handler) {
        super(handler);
        this.handler = (OWLlinkXMLRequestParserHandler) handler;
    }

    public void attribute(String localName, String value) throws OWLXMLParserException {
        if (OWLlinkXMLVocabulary.KB_ATTRIBUTE.getShortName().equalsIgnoreCase(localName)) {
            this.kb = IRI.create(value);
        } else if (OWLlinkXMLVocabulary.NAME_Attribute.getShortName().equalsIgnoreCase(localName)) {
            this.name = value;
        }
    }

    public void startElement(String name) throws OWLXMLParserException {
        super.startElement(name);
        this.kb = null;
        this.name = null;
        this.prefixes = CollectionFactory.createSet();
    }

    public IRI getKB() {
        return this.kb;
    }

    public void handleChild(OWLlinkPrefixElementHandler handler) {
        prefixes.add(handler.getOWLlinkObject());
    }

    @Override
    public void endElement() throws OWLXMLParserException {
        /*   DefaultPrefixManager manager = new DefaultPrefixManager();
       manager.clear();
       manager.setPrefix("owl:", Namespaces.OWL.toString());
       manager.setPrefix("xsd:", Namespaces.XSD.toString());
       manager.setPrefix("rdf:", Namespaces.RDF.toString());
       manager.setPrefix("rdfs:", Namespaces.RDFS.toString());

       Map<String, String> map = CollectionFactory.createMap();
       for (OWLlinkPrefixElementHandler.Prefix prefix : prefixes) {
           if (!prefix.name.endsWith(":"))
               map.put(prefix.name + ":", prefix.iri.toString());
           else
               map.put(prefix.name, prefix.iri.toString());
       }
       for (Map.Entry<String, String> entry : map.entrySet())
           manager.setPrefix(entry.getKey(), entry.getValue());

       handler.prov.putPrefixes(kb, manager);*/
        super.endElement();
    }

    public CreateKB getOWLObject() throws OWLXMLParserException {
        Map<String, String> map = CollectionFactory.createMap();
        for (OWLlinkPrefixElementHandler.Prefix prefix : prefixes) {
            if (!prefix.name.endsWith(":"))
                map.put(prefix.name + ":", prefix.iri.toString());
            else
                map.put(prefix.name, prefix.iri.toString());
        }
        if (this.name == null) {
            return new CreateKB(kb, map);
        }
        return new CreateKB(kb, name, map);
    }
}

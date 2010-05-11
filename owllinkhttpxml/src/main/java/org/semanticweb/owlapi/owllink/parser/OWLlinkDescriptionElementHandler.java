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
import org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary;
import org.semanticweb.owlapi.owllink.builtin.response.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: noppens
 * Date: 21.10.2009
 * Time: 17:47:55
 * To change this template use File | Settings | File Templates.
 */
public class OWLlinkDescriptionElementHandler extends AbstractOWLlinkResponseElementHandler<Description> {
    private String name;
    private String message;
    private Set<PublicKB> publicKBs;
    private Set<Configuration> configurations;
    private ProtocolVersion pVersion;
    private ReasonerVersion rVersion;
    private Set<IRI> supportedExtensions;

    public OWLlinkDescriptionElementHandler(OWLXMLParserHandler handler) {
        super(handler);
    }

    public void startElement(String name) throws OWLXMLParserException {
        super.startElement(name);
        this.name = null;
        this.message = null;
        this.publicKBs = new HashSet<PublicKB>();
        this.configurations = new HashSet<Configuration>();
        this.supportedExtensions = new HashSet<IRI>();
        this.pVersion = null;
        this.rVersion = null;
    }


    public void attribute(String localName, String value) throws OWLXMLParserException {
        if (OWLlinkXMLVocabulary.NAME_Attribute.getShortName().equals(localName)) {
            this.name = value;
        } else if (OWLlinkXMLVocabulary.MESSAGE_ATTRIBUTE.getShortName().equals(localName)) {
            this.message = value;
        }
    }

    public void endElement() throws OWLXMLParserException {
        ((OWLlinkElementHandler) getParentHandler()).handleChild(this);
    }

    public void handleChild(OWLlinkSettingElementHandler handler) throws OWLXMLParserException {
        configurations.add(handler.getOWLLinkObject());
    }

    public void handleChild(OWLlinkPropertyElementHandler handler) throws OWLXMLParserException {
        configurations.add(handler.getOWLLinkObject());
    }

    public void handleChild(OWLlinkPublicKBElementHandler handler) throws OWLXMLParserException {
        publicKBs.add(handler.getOWLLinkObject());
    }

    public void handleChild(OWLlinkSupportedExtensionElemenetHandler handler) throws OWLXMLParserException {
        supportedExtensions.add(handler.getOWLLinkObject());
    }

    public void handleChild(OWLlinkReasonerVersionElementHandler handler) throws OWLXMLParserException {
        this.rVersion = handler.getOWLLinkObject();
    }

    public void handleChild(OWLlinkProtocolVersionElementHandler handler) throws OWLXMLParserException {
        this.pVersion = handler.getOWLLinkObject();
    }

    public Description getOWLLinkObject() {
        return new DescriptionImpl(name, configurations, rVersion, pVersion, supportedExtensions, publicKBs);
    }
}

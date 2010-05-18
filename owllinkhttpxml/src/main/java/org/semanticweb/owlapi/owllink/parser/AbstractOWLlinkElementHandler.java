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

import org.coode.owlapi.owlxmlparser.AbstractOWLElementHandler;
import org.coode.owlapi.owlxmlparser.OWLXMLParserException;
import org.coode.owlapi.owlxmlparser.OWLXMLParserHandler;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.owllink.Request;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 21.10.2009
 */
public abstract class AbstractOWLlinkElementHandler<O> extends AbstractOWLElementHandler<O> implements OWLlinkElementHandler<O> {
    OWLlinkXMLParserHandler handler;

    public AbstractOWLlinkElementHandler(OWLXMLParserHandler handler) {
        super(handler);
        this.handler = (OWLlinkXMLParserHandler) handler;
    }

    public void handleChild(OWLlinkClassSubClassesPairElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkDataPropertySubDataPropertiesPairElementHandler handler) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void handleChild(OWLlinkObjectPropertySubPropertiesPairElementHandler handler) throws OWLXMLParserException {
    }


    public void handleChild(OWLlinkSubDataPropertySynsetsElementHandler handler) throws OWLXMLParserException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void handleChild(OWLlinkSubObjectPropertySynsetsElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkSubClassSynsetsElementHandler handler) throws OWLXMLParserException {
    }


    public void handleChild(OWLlinkElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkResponseElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkErrorElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkBooleanResponseElementHandler handler) {
    }

    public void handleChild(OWLlinkConfigurationElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkDataRangeElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkLiteralElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkPrefixElementHandler handler) throws OWLXMLParserException {

    }

    public void handleChild(OWLlinkProtocolVersionElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkReasonerVersionElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkPublicKBElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkSupportedExtensionElemenetHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkClassSynsetElementHandler handler) throws OWLXMLParserException {

    }

    public void handleChild(OWLlinkSettingElementHandler handler) throws OWLXMLParserException {

    }

    public void handleChild(OWLlinkPropertyElementHandler handler) throws OWLXMLParserException {

    }


    public void handleChild(OWLlinkObjectPropertySynsetElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkDataPropertySynsetElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkIndividualSynsetElementHandler handler) throws OWLXMLParserException {
    }


    public void handleChild(OWLlinkResponseMessageElementHandler handler) throws OWLXMLParserException {
    }

    public abstract O getOWLLinkObject() throws OWLXMLParserException;

    public void handleChild(OWLlinkDescriptionElementHandler handler) {
    }

    public O getOWLObject() throws OWLXMLParserException {
        return this.getOWLLinkObject();
    }


    public IRI getFullIRI(String value) throws OWLXMLParserException, OWLParserException {
        return super.getIRI(value);
    }

    protected OWLlinkElementHandler getParentHandler() {
        return (OWLlinkElementHandler) super.getParentHandler();
    }

    protected Request getRequest() {
        int index = handler.responseMessageHandler.getOWLLinkObject().size();
        return handler.getRequest(index);
    }
}

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

import org.coode.owlapi.owlxmlparser.OWLElementHandler;
import org.coode.owlapi.owlxmlparser.OWLXMLParserException;

/**
 *
 */
public interface OWLlinkElementHandler<O> extends OWLElementHandler<O> {

    void handleChild(OWLlinkElementHandler handler) throws OWLXMLParserException;

    void handleChild(OWLlinkResponseElementHandler handler) throws OWLXMLParserException;

    void handleChild(OWLlinkErrorElementHandler handler) throws OWLXMLParserException;

    //here are then the built-in element handler, for all other use the first 3 methods.
    void handleChild(OWLlinkConfigurationElementHandler handler) throws OWLXMLParserException;

    void handleChild(OWLlinkPropertyElementHandler handler) throws OWLXMLParserException;

    void handleChild(OWLlinkSettingElementHandler handler) throws OWLXMLParserException;

    void handleChild(OWLlinkDataRangeElementHandler handler) throws OWLXMLParserException;

    void handleChild(OWLlinkLiteralElementHandler handler) throws OWLXMLParserException;

    void handleChild(OWLlinkPrefixElementHandler handler) throws OWLXMLParserException;

    void handleChild(OWLlinkProtocolVersionElementHandler handler) throws OWLXMLParserException;

    void handleChild(OWLlinkReasonerVersionElementHandler handler) throws OWLXMLParserException;

    void handleChild(OWLlinkPublicKBElementHandler handler) throws OWLXMLParserException;

    void handleChild(OWLlinkSupportedExtensionElemenetHandler handler) throws OWLXMLParserException;

    void handleChild(OWLlinkClassSynsetElementHandler handler) throws OWLXMLParserException;

    void handleChild(OWLlinkObjectPropertySynsetElementHandler handler) throws OWLXMLParserException;

    void handleChild(OWLlinkDataPropertySynsetElementHandler handler) throws OWLXMLParserException;

    void handleChild(OWLlinkIndividualSynsetElementHandler handler) throws OWLXMLParserException;


    void handleChild(OWLlinkClassSubClassesPairElementHandler handler) throws OWLXMLParserException;

    void handleChild(OWLlinkObjectPropertySubPropertiesPairElementHandler handler) throws OWLXMLParserException;

    void handleChild(OWLlinkDataPropertySubDataPropertiesPairElementHandler handler) throws OWLXMLParserException;

    void handleChild(OWLlinkSubClassSynsetsElementHandler handler) throws OWLXMLParserException;

    void handleChild(OWLlinkSubObjectPropertySynsetsElementHandler handler) throws OWLXMLParserException;

    void handleChild(OWLlinkSubDataPropertySynsetsElementHandler handler) throws OWLXMLParserException;

    void handleChild(OWLlinkResponseMessageElementHandler handler) throws OWLXMLParserException;

    O getOWLLinkObject() throws OWLXMLParserException;

}

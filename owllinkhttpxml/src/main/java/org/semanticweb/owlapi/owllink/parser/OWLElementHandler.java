/* This file is part of the OWL API.
 * The contents of this file are subject to the LGPL License, Version 3.0.
 * Copyright 2014, The University of Manchester
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0 in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. */
package org.semanticweb.owlapi.owllink.parser;

import org.semanticweb.owlapi.model.*;

import static org.semanticweb.owlapi.util.OWLAPIPreconditions.verifyNotNull;
import static org.semanticweb.owlapi.vocab.OWLXMLVocabulary.*;


public abstract class OWLElementHandler<O> {

    
    final OWLXMLParserHandler handler;
    OWLElementHandler<?> parentHandler;
    
    final StringBuilder sb = new StringBuilder();
    String elementName;
    
    final OWLDataFactory df;

    /**
     * @return object
     */
    
    abstract public O getOWLObject();

    /**
     * @throws UnloadableImportException if an import cannot be resolved
     */
    public abstract void endElement();

    public OWLElementHandler(OWLXMLParserHandler handler) {
        this.handler = handler;
        df = handler.getDataFactory();
    }

    
    public IRI getIRIFromAttribute(String localName, String value) {
        if (localName.equals(IRI_ATTRIBUTE.getShortForm())) {
            return handler.getIRI(value);
        } else if (localName.equals(ABBREVIATED_IRI_ATTRIBUTE.getShortForm())) {
            return handler.getAbbreviatedIRI(value);
        } else if (localName.equals("URI")) {
            // Legacy
            return handler.getIRI(value);
        }
        ensureAttributeNotNull(null, IRI_ATTRIBUTE.getShortForm());
        return IRI.create("");
    }

    public IRI getIRIFromElement(String elementLocalName, String textContent) {
        if (elementLocalName.equals(IRI_ELEMENT.getShortForm())) {
            return handler.getIRI(textContent.trim());
        } else if (elementLocalName.equals(ABBREVIATED_IRI_ELEMENT.getShortForm())) {
            return handler.getAbbreviatedIRI(textContent.trim());
        }
        throw new OWLXMLParserException(handler, elementLocalName + " is not an IRI element");
    }

    /**
     * @param handler element handler
     */
    public void setParentHandler(OWLElementHandler<?> handler) {
        parentHandler = handler;
    }

    
    public OWLElementHandler<?> getParentHandler() {
        return verifyNotNull(parentHandler, "parentHandler cannot be null at this point");
    }

    /**
     * @param localName local attribute name
     * @param value attribute value
     */
    @SuppressWarnings("unused")
    public void attribute(String localName, String value) {}

    /**
     * @param name element name
     */
    public void startElement(String name) {
        elementName = name;
    }

    public String getElementName() {
        return elementName;
    }

    /**
     * @param h element handler
     */
    @SuppressWarnings("unused")
    public void handleChild(AbstractOWLAxiomElementHandler h) {}

    /**
     * @param h element handler
     */
    @SuppressWarnings("unused")
    public void handleChild(AbstractClassExpressionElementHandler h) {}

    /**
     * @param h element handler
     */
    @SuppressWarnings("unused")
    public void handleChild(AbstractOWLDataRangeHandler h) {}

    /**
     * @param h element handler
     */
    @SuppressWarnings("unused")
    public void handleChild(AbstractOWLObjectPropertyElementHandler h) {}

    /**
     * @param h element handler
     */
    @SuppressWarnings("unused")
    public void handleChild(OWLDataPropertyElementHandler h) {}

    /**
     * @param h element handler
     */
    @SuppressWarnings("unused")
    public void handleChild(OWLIndividualElementHandler h) {}

    /**
     * @param h element handler
     */
    @SuppressWarnings("unused")
    public void handleChild(OWLLiteralElementHandler h) {}

    /**
     * @param h element handler
     */
    @SuppressWarnings("unused")
    public void handleChild(OWLAnnotationElementHandler h) {}

    /**
     * @param h element handler
     */
    @SuppressWarnings("unused")
    public void handleChild(OWLSubObjectPropertyChainElementHandler h) {}

    /**
     * @param h element handler
     */
    @SuppressWarnings("unused")
    public void handleChild(OWLDatatypeFacetRestrictionElementHandler h) {}

    /**
     * @param h element handler
     */
    @SuppressWarnings("unused")
    public void handleChild(OWLAnnotationPropertyElementHandler h) {}

    /**
     * @param h element handler
     */
    @SuppressWarnings("unused")
    public void handleChild(OWLAnonymousIndividualElementHandler h) {}

    /**
     * @param h element handler
     */
    @SuppressWarnings("unused")
    public void handleChild(AbstractIRIElementHandler h) {}

    /**
     * @param h element handler
     */
    @SuppressWarnings("unused")
    public void handleChild(SWRLVariableElementHandler h) {}

    /**
     * @param h element handler
     */
    @SuppressWarnings("unused")
    public void handleChild(SWRLAtomElementHandler h) {}

    /**
     * @param h element handler
     */
    @SuppressWarnings("unused")
    public void handleChild(SWRLAtomListElementHandler h) {}

    void ensureNotNull(Object element, String message) {
        if (element == null) {
            throw new OWLXMLParserElementNotFoundException(handler, message);
        }
    }

    void ensureAttributeNotNull(Object element, String message) {
        if (element == null) {
            throw new OWLXMLParserAttributeNotFoundException(handler, message);
        }
    }

    /**
     * @param chars chars to handle
     * @param start start index
     * @param length end index
     */
    void handleChars(char[] chars, int start, int length) {
        if (isTextContentPossible()) {
            sb.append(chars, start, length);
        }
    }

    /**
     * @return text handled
     */

    public String getText() {
        return sb.toString();
    }

    /**
     * @return true if text can be contained
     */
    public boolean isTextContentPossible() {
        return false;
    }
}

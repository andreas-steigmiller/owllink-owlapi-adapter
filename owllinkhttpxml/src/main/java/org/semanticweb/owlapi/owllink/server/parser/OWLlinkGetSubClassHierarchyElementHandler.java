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

import org.coode.owlapi.owlxmlparser.AbstractClassExpressionElementHandler;
import org.coode.owlapi.owlxmlparser.OWLXMLParserException;
import org.coode.owlapi.owlxmlparser.OWLXMLParserHandler;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSubClassHierarchy;

/**
 * Author: Olaf Noppens
 * Date: 28.11.2009
 */
public class OWLlinkGetSubClassHierarchyElementHandler extends AbstractOWLlinkKBRequestElementHandler<GetSubClassHierarchy> {
    protected OWLClass clazz;

    public OWLlinkGetSubClassHierarchyElementHandler(OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    public void startElement(String name) throws OWLXMLParserException {
        super.startElement(name);
        this.clazz = null;
    }

    @Override
    public void handleChild(AbstractClassExpressionElementHandler handler) throws OWLXMLParserException {
        OWLClassExpression clazz = handler.getOWLObject();
        if (!clazz.isAnonymous())
            this.clazz = clazz.asOWLClass();
    }

    public GetSubClassHierarchy getOWLObject() throws OWLXMLParserException {
        if (clazz == null)
            return new GetSubClassHierarchy(getKB());
        else
            return new GetSubClassHierarchy(getKB(), this.clazz);
    }
}

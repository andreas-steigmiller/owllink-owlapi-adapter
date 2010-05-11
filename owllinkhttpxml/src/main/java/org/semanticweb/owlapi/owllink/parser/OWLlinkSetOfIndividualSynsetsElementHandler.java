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
import org.semanticweb.owlapi.owllink.builtin.response.IndividualSynset;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfIndividualSynsets;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfIndividualSynsetsImpl;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 24.11.2009
 */
public class OWLlinkSetOfIndividualSynsetsElementHandler extends AbstractOWLlinkKBResponseElementHandler<SetOfIndividualSynsets> {
    private Set<IndividualSynset> synsets;

    public OWLlinkSetOfIndividualSynsetsElementHandler(OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    public void startElement(String s) throws OWLXMLParserException {
        super.startElement(s);
        this.synsets = new HashSet<IndividualSynset>();
    }

    public void handleChild(OWLlinkIndividualSynsetElementHandler handler) throws OWLXMLParserException {
        this.synsets.add(handler.getOWLLinkObject());
    }


    @Override
    public SetOfIndividualSynsets getOWLLinkObject() throws OWLXMLParserException {
        return new SetOfIndividualSynsetsImpl(this.synsets);
    }
}

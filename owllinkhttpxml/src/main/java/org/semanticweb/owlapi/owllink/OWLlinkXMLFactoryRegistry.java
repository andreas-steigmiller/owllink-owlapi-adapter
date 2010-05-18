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

package org.semanticweb.owlapi.owllink;

import org.semanticweb.owlapi.owllink.parser.OWLlinkElementHandlerFactory;
import org.semanticweb.owlapi.owllink.renderer.OWLlinkRequestRendererFactory;
import org.semanticweb.owlapi.owllink.retraction.OWLlinkXMLRetractionRequestRendererFactory;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Author: Olaf Noppens
 * Date: 28.04.2010
 */
public class OWLlinkXMLFactoryRegistry {
    private static OWLlinkXMLFactoryRegistry instance;

    List<OWLlinkRequestRendererFactory> rendererFactories;
    List<OWLlinkElementHandlerFactory> parserFactories;

    private OWLlinkXMLFactoryRegistry() {
        this.rendererFactories = new Vector<OWLlinkRequestRendererFactory>();
        this.parserFactories = new Vector<OWLlinkElementHandlerFactory>();

        register(new OWLlinkXMLRetractionRequestRendererFactory());

    }


    public synchronized static OWLlinkXMLFactoryRegistry getInstance() {
        if (instance == null)
            instance = new OWLlinkXMLFactoryRegistry();
        return instance;
    }


    public synchronized List<OWLlinkRequestRendererFactory> getRequestRendererFactories() {
        return Collections.unmodifiableList(rendererFactories);
    }

    public synchronized List<OWLlinkElementHandlerFactory> getParserFactories() {
        return Collections.unmodifiableList(parserFactories);
    }

    public synchronized void register(OWLlinkRequestRendererFactory factory) {
        if (!rendererFactories.contains(factory))
            rendererFactories.add(factory);
    }

    public synchronized void register(OWLlinkElementHandlerFactory factory) {
        if (!parserFactories.contains(factory))
            parserFactories.add(factory);
    }

    public synchronized void unregister(OWLlinkRequestRendererFactory factory) {
        rendererFactories.remove(factory);
    }

    public synchronized void unregister(OWLlinkElementHandlerFactory factory) {
        parserFactories.remove(factory);
    }

}

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

package org.semanticweb.owlapi.owllink;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.IllegalConfigurationException;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

/**
 * Author: Olaf Noppens
 * Date: 19.02.2010
 */
public class OWLlinkHTTPXMLReasonerFactory implements OWLReasonerFactory {

    public String getReasonerName() {
        return "OWLlink reasoner (via HTPP)";
    }

    public OWLlinkReasoner createNonBufferingReasoner(OWLOntology ontology) {
        return createNonBufferingReasoner(ontology, new OWLlinkReasonerConfiguration());
    }

    public OWLlinkReasoner createReasoner(OWLOntology ontology) {
        return createReasoner(ontology, new OWLlinkReasonerConfiguration());
    }

    public OWLlinkReasoner createNonBufferingReasoner(OWLOntology ontology, OWLReasonerConfiguration config) throws IllegalConfigurationException {
        if (config instanceof OWLlinkReasonerConfiguration)
            return new OWLlinkHTTPXMLReasoner(ontology, (OWLlinkReasonerConfiguration) config, BufferingMode.NON_BUFFERING);
        else
            throw new IllegalConfigurationException("Configuration must be a OWLlinkReasonerConfiguration", config);
    }

    public OWLlinkReasoner createReasoner(OWLOntology ontology, OWLReasonerConfiguration config) throws IllegalConfigurationException {
        if (config instanceof OWLlinkReasonerConfiguration)
            return new OWLlinkHTTPXMLReasoner(ontology, (OWLlinkReasonerConfiguration) config, BufferingMode.BUFFERING);
        else
            throw new IllegalConfigurationException("Configuration must be a OWLlinkReasonerConfiguration", config);

    }
}

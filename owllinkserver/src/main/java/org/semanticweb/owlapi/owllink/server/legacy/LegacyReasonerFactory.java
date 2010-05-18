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

package org.semanticweb.owlapi.owllink.server.legacy;


import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.*;


/**
 * @author Olaf Noppens
 */
public class LegacyReasonerFactory implements OWLReasonerFactory {
    private org.semanticweb.owl.inference.OWLReasonerFactory v2Factory;


    public LegacyReasonerFactory(org.semanticweb.owl.inference.OWLReasonerFactory v2Factory) {
        this.v2Factory = v2Factory;
    }

    public String getReasonerName() {
        return v2Factory.getReasonerName() + " via OWLlink legacy mode";
    }

    public OWLReasoner createNonBufferingReasoner(OWLOntology ontology) {
        return createNonBufferingReasoner(ontology, new LegacyConfiguration());
    }

    public OWLReasoner createReasoner(OWLOntology ontology) {
        return createReasoner(ontology, new LegacyConfiguration());
    }

    public OWLReasoner createNonBufferingReasoner(OWLOntology ontology, OWLReasonerConfiguration config)
            throws IllegalConfigurationException {
        if (!(config instanceof LegacyConfiguration)) {
            throw new IllegalConfigurationException("OWLReasonerConfiguration must be of type LegacyConfiguation", config);
        }
        org.semanticweb.owl.model.OWLOntologyManager v2Manager = org.semanticweb.owl.apibinding.OWLManager.createOWLOntologyManager();
        org.semanticweb.owl.inference.OWLReasoner reasoner = v2Factory.createReasoner(v2Manager);
        OWLReasonerLegacyBridge bridge = new OWLReasonerLegacyBridge(ontology, config, BufferingMode.NON_BUFFERING,
                reasoner, v2Manager);

        return bridge;

    }

    public OWLReasoner createReasoner(OWLOntology ontology, OWLReasonerConfiguration config)
            throws IllegalConfigurationException {
        if (!(config instanceof LegacyConfiguration)) {
            throw new IllegalConfigurationException("OWLReasonerConfiguration must be of type LegacyConfiguation", config);
        }
        org.semanticweb.owl.model.OWLOntologyManager v2Manager = org.semanticweb.owl.apibinding.OWLManager.createOWLOntologyManager();
        org.semanticweb.owl.inference.OWLReasoner reasoner = v2Factory.createReasoner(v2Manager);
        OWLReasonerLegacyBridge bridge = new OWLReasonerLegacyBridge(ontology, config, BufferingMode.BUFFERING,
                reasoner, v2Manager);

        return bridge;
    }


}

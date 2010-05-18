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

package org.semanticweb.owllink.protege;

import org.protege.editor.owl.model.inference.ProtegeOWLReasonerFactoryAdapter;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.owllink.OWLlinkReasonerConfiguration;
import org.semanticweb.owlapi.owllink.OWLlinkReasonerRuntimeException;
import org.semanticweb.owlapi.reasoner.IndividualNodeSetPolicy;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.ReasonerProgressMonitor;

import javax.swing.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Author: Olaf Noppens
 * Date: 14.05.2010
 */
public class OWLlinkHTTPXMLReasonerFactory extends ProtegeOWLReasonerFactoryAdapter {

    private org.semanticweb.owlapi.owllink.OWLlinkHTTPXMLReasonerFactory factory;


    public void dispose() throws Exception {
        this.factory = null;
    }

    public void initialise() throws Exception {
        this.factory = new org.semanticweb.owlapi.owllink.OWLlinkHTTPXMLReasonerFactory();
    }

    public OWLReasoner createReasoner(OWLOntology owlOntology, ReasonerProgressMonitor progressMonitor) {
        OWLlinkHTTPXMLReasonerPreferences prefs = OWLlinkHTTPXMLReasonerPreferences.getInstance();
        try {
            URL reasonerURL = new URL(prefs.getServerEndpointURL() + ":" + prefs.getServerEndpointPort());
            OWLlinkReasonerConfiguration configuration = new OWLlinkReasonerConfiguration(progressMonitor, reasonerURL, IndividualNodeSetPolicy.BY_SAME_AS);
            return factory.createNonBufferingReasoner(owlOntology, configuration);
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null,
                    "The given OWLlink server endpoint URL is not valid.\nUsing defaults (localhost, 8080).",
                    "OWLlink server endpoint URL not valid",
                    JOptionPane.ERROR_MESSAGE);
        } catch (OWLlinkReasonerRuntimeException e) {
            if (e.getCause() instanceof IOException) {
                JOptionPane.showMessageDialog(null,
                        "Connection to the OWLlink server failed.",
                        "Connection failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        return factory.createNonBufferingReasoner(owlOntology);
    }
}

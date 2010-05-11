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

import org.semanticweb.owlapi.reasoner.*;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Author: Olaf Noppens
 * Date: 19.02.2010
 */
public class OWLlinkReasonerConfiguration extends SimpleConfiguration {
    private static URL defaultURL;
    URL reasonerURL;

    static {
        try {
            defaultURL = new URL("http://localhost:8080");
        } catch (MalformedURLException e) {
            defaultURL = null;
        }
    }


    public OWLlinkReasonerConfiguration(ReasonerProgressMonitor progressMonitor, URL reasonerURL, IndividualNodeSetPolicy individualNodeSetPolicy) {
        super(progressMonitor, FreshEntityPolicy.DISALLOW, Long.MAX_VALUE, individualNodeSetPolicy);
        this.reasonerURL = reasonerURL;
    }

    public OWLlinkReasonerConfiguration(ReasonerProgressMonitor progressMonitor, IndividualNodeSetPolicy policy) {
        this(progressMonitor, defaultURL, policy);
    }

    public OWLlinkReasonerConfiguration(URL reasonerURL, IndividualNodeSetPolicy policy) {
        this(new NullReasonerProgressMonitor(), reasonerURL, policy);
    }

    public OWLlinkReasonerConfiguration(IndividualNodeSetPolicy policy) {
        this(defaultURL, policy);
    }

     public OWLlinkReasonerConfiguration(URL reasonerURL) {
        this(defaultURL, IndividualNodeSetPolicy.BY_SAME_AS);
    }

    public OWLlinkReasonerConfiguration() {
        this(IndividualNodeSetPolicy.BY_SAME_AS);
    }


    public URL getReasonerURL() {
        return this.reasonerURL;
    }
}

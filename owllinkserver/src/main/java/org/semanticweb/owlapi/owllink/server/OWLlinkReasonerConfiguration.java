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

package org.semanticweb.owlapi.owllink.server;

import org.semanticweb.owlapi.owllink.builtin.response.Configuration;
import org.semanticweb.owlapi.owllink.builtin.response.ReasonerVersion;
import org.semanticweb.owlapi.owllink.builtin.response.Setting;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;

import java.util.Set;

/**
 * Author: Olaf Noppens
 */
public interface OWLlinkReasonerConfiguration {

    /**
     * Returns all Configurations.
     *
     * @return set of all Configurations.
     */
    Set<Configuration> getConfigurations();

    Set<Setting> getSettings();

    ReasonerVersion getReasonerVersion();

    // boolean set(Setting setting);

    /**
     * Returns the Configuration with the given key (if exists, otherwise returns <code>null</code>).
     *
     * @param key key
     * @return Configuration (or <code>null</code> if no configuration with the given key exists)
     */
    Configuration getConfiguration(String key);

    /**
     * Returns the {@link org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration OWLReasonerConfiguration} used
     * to create OWLReasoners for KB in the server
     * @return OWLReasonerConfiguration
     */
    OWLReasonerConfiguration getOWLReasonerConfiguration();

}

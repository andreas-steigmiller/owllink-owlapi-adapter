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

package org.semanticweb.owlapi.owllink.builtin.response;

import org.semanticweb.owlapi.model.IRI;

import java.util.Collections;
import java.util.Set;

/**
 * Author: Olaf Noppens
 * Date: 24.10.2009
 */
public class DescriptionImpl extends ConfirmationImpl implements Description {
    private final String name;
    private final String message;
    private final Set<IRI> supportedExtensions;
    private final Set<Configuration> configurations;
    private final Set<PublicKB> publicKBs;
    private ReasonerVersion rVersion;
    private ProtocolVersion pVersion;


    public DescriptionImpl(String warning, String name, String message, ReasonerVersion rVersion, ProtocolVersion pVersion, Set<IRI> supportedExtensions, Set<Configuration> configurations, Set<PublicKB> publicKBs) {
        super(warning);
        this.name = name;
        this.message = message;
        this.supportedExtensions = (supportedExtensions == null ? Collections.<IRI>emptySet() : Collections.unmodifiableSet(supportedExtensions));
        this.configurations = (configurations == null ? Collections.<Configuration>emptySet() : Collections.unmodifiableSet(configurations));
        this.publicKBs = (publicKBs == null ? Collections.<PublicKB>emptySet() : Collections.unmodifiableSet(publicKBs));
        this.pVersion = pVersion;
        this.rVersion = rVersion;
    }

    public DescriptionImpl(String name, String message, ReasonerVersion rVersion, ProtocolVersion pVersion, Set<IRI> supportedExtensions, Set<Configuration> configurations, Set<PublicKB> publicKBs) {
        this.name = name;
        this.message = message;
        this.supportedExtensions = (supportedExtensions == null ? Collections.<IRI>emptySet() : Collections.unmodifiableSet(supportedExtensions));
        this.configurations = (configurations == null ? Collections.<Configuration>emptySet() : Collections.unmodifiableSet(configurations));
        this.publicKBs = (publicKBs == null ? Collections.<PublicKB>emptySet() : Collections.unmodifiableSet(publicKBs));
        this.pVersion = pVersion;
        this.rVersion = rVersion;
    }

    public DescriptionImpl(String name, Set<Configuration> configurations, ReasonerVersion rVersion, ProtocolVersion pVersion, Set<IRI> supportedExtensions, Set<PublicKB> publicKBs) {
        this(null, name, null, rVersion, pVersion, supportedExtensions, configurations, publicKBs);
    }

    public String getName() {
        return this.name;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean hasMessage() {
        return getMessage() != null;
    }

    public Set<PublicKB> getPublicKBs() {
        return this.publicKBs;
    }

    public Set<Configuration> getDefaults() {
        return this.configurations;
    }

    public Set<IRI> getSupportedExtensions() {
        return this.supportedExtensions;
    }

    public ProtocolVersion getProtocolVersion() {
        return this.pVersion;
    }

    public ReasonerVersion getReasonerVersion() {
        return this.rVersion;
    }

    public <O> O accept(ResponseVisitor<O> visitor) {
        return visitor.visit(this);
    }
}

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

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.owllink.builtin.response.*;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.util.CollectionFactory;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import java.util.Collections;
import java.util.Set;

/**
 * Author: Olaf Noppens
 * Date: 27.11.2009
 */
public class AbstractOWLlinkReasonerConfiguration implements OWLlinkReasonerConfiguration {
    public static final String ABBREVIATES_IRIS = "abbreviatesIRIs";
    private static final String APPLIEDSEMANTICS = "appliedSemantics";
    private static final String SELECTED_PROFILE = "selectedProfile";
    private static final String SUPPORTED_DATATYPES = "supportedDatatypes";
    public static final String IGNORES_ANNOTATIONS = "ignoresAnnotations";
    public static final String IGNORES_DECLARATIONS = "ignoresDeclarations";
    public static final String UNIQUE_NAMEASSUMPTION = "uniqueNameAssumption";
    private Set<Configuration> configurations;
    private OWLReasonerConfiguration reasonerConfiguration;

    private ReasonerVersion reasonerversion;

    public AbstractOWLlinkReasonerConfiguration(OWLReasonerConfiguration configuration) {
        this.reasonerConfiguration = configuration;
        initDefaults();
    }

    public AbstractOWLlinkReasonerConfiguration( ) {
        this.reasonerConfiguration = null;
        initDefaults();
    }

    public AbstractOWLlinkReasonerConfiguration(OWLlinkReasonerConfiguration configuration) {
        this(configuration.getOWLReasonerConfiguration());
        add(configuration);
    }

    protected void initDefaults() {
        this.configurations = CollectionFactory.createSet();
        setAppliedSemantics("direct", "direct");
        setAbbreviatesIRIs(true);
        setIgnoresAnnotations(true);
        setSelectedProfile("OWL 2 DL", "OWL 2 DL");
        setIgnoresDeclarations(true);
        setUniqueNameAssumption(false);
        setSupportedDatatypes(OWL2Datatype.XSD_STRING.getIRI());
        setReasonerInfo(1, 1, 0);
    }


    public void add(OWLlinkReasonerConfiguration configuration) {
        this.configurations.addAll(configuration.getConfigurations());
    }

    public Configuration getAppliedSemantics() {
        return getConfiguration(APPLIEDSEMANTICS);
    }

    public Configuration getAbbreviatesIRIs() {
        return getConfiguration(ABBREVIATES_IRIS);
    }

    public Configuration getSelectedProfile() {
        return getConfiguration(SELECTED_PROFILE);
    }

    public Configuration getSupportedDatatypes() {
        return getConfiguration(SUPPORTED_DATATYPES);
    }

    public Configuration getIgnoresAnnotations() {
        return getConfiguration(IGNORES_ANNOTATIONS);
    }

    public Configuration getIgnoresDeclarations() {
        return getConfiguration(IGNORES_DECLARATIONS);
    }

    public Configuration getUniqueNamesAssumption() {
        return getConfiguration(UNIQUE_NAMEASSUMPTION);
    }

    public ReasonerVersion getReasonerVersion() {
        return this.reasonerversion;
    }

    public void setReasonerInfo(int major, int minor, int build) {
        this.reasonerversion = new ReasonerVersionImpl(major, minor, build);
    }

    public void setAppliedSemantics(String selected, String... ranges) {
        Set<OWLlinkLiteral> rangeValues = CollectionFactory.createSet();
        for (String range : ranges)
            rangeValues.add(new OWLlinkLiteralImpl(range));

        Set<OWLlinkLiteral> selectedValue = CollectionFactory.createSet();
        selectedValue.add(new OWLlinkLiteralImpl(selected));

        OWLlinkOneOf oneOf = new OWLlinkOneOfImpl(OWL2Datatype.XSD_STRING.getIRI(), rangeValues);
        replaceConfiguration(new PropertyImpl(APPLIEDSEMANTICS, oneOf, selectedValue));
    }

    public void setSelectedProfile(String selected, String... ranges) {
        Set<OWLlinkLiteral> rangeValues = CollectionFactory.createSet();
        for (String range : ranges)
            rangeValues.add(new OWLlinkLiteralImpl(range));

        Set<OWLlinkLiteral> selectedValue = CollectionFactory.createSet();
        selectedValue.add(new OWLlinkLiteralImpl(selected));

        OWLlinkOneOf oneOf = new OWLlinkOneOfImpl(OWL2Datatype.XSD_STRING.getIRI(), rangeValues);
        replaceConfiguration(new PropertyImpl(SELECTED_PROFILE, oneOf, selectedValue));
    }

    public void setSupportedDatatypes(IRI... iris) {
        OWLlinkList list = new OWLlinkListImpl(OWL2Datatype.XSD_ANY_URI.getIRI());
        Set<OWLlinkLiteral> literals = CollectionFactory.createSet();
        for (IRI iri : iris) {
            literals.add(new OWLlinkLiteralImpl(iri.toString()));
        }
        replaceConfiguration(new PropertyImpl(SUPPORTED_DATATYPES, list, literals));
    }

    public void setIgnoresAnnotations(boolean ignores) {
        Set<OWLlinkLiteral> literals = CollectionFactory.createSet();
        literals.add(new OWLlinkLiteralImpl(new Boolean(ignores).toString()));
        replaceConfiguration(new PropertyImpl(IGNORES_ANNOTATIONS, new OWLlinkDatatypeImpl(OWL2Datatype.XSD_BOOLEAN.getIRI()), literals));
    }

    public void setAbbreviatesIRIs(boolean ignores) {
        Set<OWLlinkLiteral> literals = CollectionFactory.createSet();
        literals.add(new OWLlinkLiteralImpl(new Boolean(ignores).toString()));
        replaceConfiguration(new PropertyImpl(ABBREVIATES_IRIS, new OWLlinkDatatypeImpl(OWL2Datatype.XSD_BOOLEAN.getIRI()), literals));
    }

    public void setIgnoresDeclarations(boolean ignores) {
        Set<OWLlinkLiteral> literals = CollectionFactory.createSet();
        literals.add(new OWLlinkLiteralImpl(new Boolean(ignores).toString()));
        replaceConfiguration(new PropertyImpl(IGNORES_DECLARATIONS, new OWLlinkDatatypeImpl(OWL2Datatype.XSD_BOOLEAN.getIRI()), literals));
    }

    public void setUniqueNameAssumption(boolean ignores) {
        Set<OWLlinkLiteral> literals = CollectionFactory.createSet();
        literals.add(new OWLlinkLiteralImpl(new Boolean(ignores).toString()));
        replaceConfiguration(new PropertyImpl(UNIQUE_NAMEASSUMPTION, new OWLlinkDatatypeImpl(OWL2Datatype.XSD_BOOLEAN.getIRI()), literals));
    }

    public Set<Configuration> getConfigurations() {
        return Collections.unmodifiableSet(this.configurations);
    }

    public Set<Setting> getSettings() {
        Set<Setting> settings = CollectionFactory.createSet();
        for (Configuration conf : getConfigurations())
            if (conf.isSetting())
                settings.add(conf.asSetting());
        return settings;
    }


    public Configuration getConfiguration(String key) {
        for (Configuration configuration : getConfigurations()) {
            if (configuration.getKey().equals(key))
                return configuration;
        }
        return null;
    }

    protected void replaceConfiguration(Configuration newConfiguration) {
        Configuration oldConfiguration = getConfiguration(newConfiguration.getKey());
        if (oldConfiguration != null)
            this.configurations.remove(oldConfiguration);
        this.configurations.add(newConfiguration);
    }

    public boolean set(String key, Set<OWLlinkLiteral> values) {
        Configuration configuration = getConfiguration(key);
        if (configuration != null && configuration.isSetting()) {
            SettingImpl setting = new SettingImpl(key, configuration.getType(), values);
            configurations.remove(configuration);
            configurations.add(setting);
            return true;
        }
        return false;
    }

    public OWLReasonerConfiguration getOWLReasonerConfiguration() {
        return this.reasonerConfiguration;
    }

    public void setReasonerConfiguration(OWLReasonerConfiguration configuration) {
        this.reasonerConfiguration = configuration;
    }
}

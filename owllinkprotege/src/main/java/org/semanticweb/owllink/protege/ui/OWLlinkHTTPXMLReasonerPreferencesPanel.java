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

package org.semanticweb.owllink.protege.ui;

import org.protege.editor.core.ui.util.ComponentFactory;
import org.protege.editor.owl.ui.preferences.OWLPreferencesPanel;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.owllink.OWLlinkHTTPXMLReasoner;
import org.semanticweb.owlapi.owllink.OWLlinkHTTPXMLReasonerFactory;
import org.semanticweb.owlapi.owllink.OWLlinkReasonerConfiguration;
import org.semanticweb.owlapi.util.Version;
import org.semanticweb.owllink.protege.OWLlinkHTTPXMLReasonerPreferences;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Author: Olaf Noppens
 * Date: 14.05.2010
 */
public class OWLlinkHTTPXMLReasonerPreferencesPanel extends OWLPreferencesPanel {

    //URL panel
    private JTextField urlField;
    private JTextField portNumberField;
    private JButton checkConnection;


    private static final int HORIZONTAL_SPACE = 25;


    public void dispose() throws Exception {
    }

    public void initialise() throws Exception {
        JComponent urlPanel = createURLPanel();

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(urlPanel);
    }

    @Override
    public void applyChanges() {
        OWLlinkHTTPXMLReasonerPreferences prefs = OWLlinkHTTPXMLReasonerPreferences.getInstance();
        try {
            URL url = new URL(urlField.getText());
            prefs.setServerEndpointURL(url.toString());
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null,
                    "The given URL is not valid",
                    "Not valid URL",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                int port = Integer.parseInt(this.portNumberField.getText());
                prefs.setServerEndpointPort(port);
            } catch (Exception e) {
            }
        }
    }

    private JComponent createURLPanel() {
        OWLlinkHTTPXMLReasonerPreferences prefs = OWLlinkHTTPXMLReasonerPreferences.getInstance();
        JComponent c = createPane("Server end-point", BoxLayout.PAGE_AXIS);


        this.urlField = new JTextField() {
            public Dimension getPreferredSize() {
                return new Dimension(0, super.getPreferredSize().height);
            }
        };
        this.urlField.setText(prefs.getServerEndpointURL());
        this.portNumberField = new JTextField();
        this.portNumberField.setText(Integer.toString(prefs.getServerEndpointPort()));
        this.checkConnection = new JButton("Check connection now");
        this.checkConnection.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkConnection();
            }
        });

        JComponent urlPanel = new JPanel();
        urlPanel.setLayout(new BoxLayout(urlPanel, BoxLayout.LINE_AXIS));
        urlPanel.setAlignmentX(0.0f);
        urlPanel.add(Box.createHorizontalStrut(HORIZONTAL_SPACE));
        urlPanel.add(new JLabel("URL "));
        urlPanel.add(urlField);

        JComponent portNumberPanel = new JPanel();
        portNumberPanel.setLayout(new BoxLayout(portNumberPanel, BoxLayout.LINE_AXIS));
        portNumberPanel.setAlignmentX(0.0f);
        portNumberPanel.add(Box.createHorizontalStrut(HORIZONTAL_SPACE));
        portNumberPanel.add(new JLabel("HTTP Port"));
        portNumberPanel.add(portNumberField);

        JComponent checkConnectionPanel = new JPanel();
        checkConnectionPanel.setLayout(new BoxLayout(checkConnectionPanel, BoxLayout.LINE_AXIS));
        checkConnectionPanel.setAlignmentX(0.0f);
        checkConnectionPanel.add(checkConnection);
        checkConnectionPanel.add(Box.createHorizontalGlue());


        JPanel customLabelPane = new JPanel();
        customLabelPane.setLayout(new BoxLayout(customLabelPane, BoxLayout.PAGE_AXIS));
        customLabelPane.setAlignmentX(0.0f);
        customLabelPane.add(urlPanel);
        customLabelPane.add(portNumberPanel);
        customLabelPane.add(checkConnectionPanel);
        c.add(customLabelPane);

        return c;
    }

    private JComponent createPane(String title, int orientation) {
        JComponent c = new Box(orientation) {
            public Dimension getMaximumSize() {
                return new Dimension(super.getMaximumSize().width, getPreferredSize().height);
            }
        };
        c.setAlignmentX(0.0f);
        if (title != null) {
            c.setBorder(ComponentFactory.createTitledBorder(title));
        }
        return c;
    }

    protected void checkConnection() {
        try {
            URL reasonerURL = new URL(urlField.getText() + ":" + this.portNumberField.getText());
            OWLlinkReasonerConfiguration configuration = new OWLlinkReasonerConfiguration(reasonerURL);
            OWLlinkHTTPXMLReasonerFactory factory = new OWLlinkHTTPXMLReasonerFactory();
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OWLOntology owlOntology = null;
            try {
                owlOntology = manager.createOntology();
                OWLlinkHTTPXMLReasoner reasoner = (OWLlinkHTTPXMLReasoner) factory.createNonBufferingReasoner(owlOntology, configuration);
                String reasonerName = reasoner.getReasonerName();
                Version version = reasoner.getReasonerVersion();
                reasoner.dispose();
                JOptionPane.showMessageDialog(null, "Reasoner " + reasonerName + "(" + version.getMajor() + "." + version.getMinor() + ")" + " is available", "OK", JOptionPane.INFORMATION_MESSAGE);
            } catch (OWLOntologyCreationException e) {

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "An OWLlink server endpoint could not be found at " + reasonerURL,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null,
                    "The given URL is not valid",
                    "Not valid URL",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}

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

package org.semanticweb.owlapi.owllink.server.serverfactory;

import org.semanticweb.owlapi.owllink.server.AbstractOWLlinkReasonerConfiguration;
import org.semanticweb.owlapi.owllink.server.OWLlinkHTTPXMLServer;
import org.semanticweb.owlapi.reasoner.IndividualNodeSetPolicy;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import java.lang.reflect.Field;

/**
 * Author: Olaf Noppens
 * Date: 25.02.2010
 */
public class HermiTServerFactory implements OWLlinkServerFactory {

    public static final String FACTORY_CLASSNAME = "org.semanticweb.HermiT.Reasoner$ReasonerFactory";
    public static final String HERMIT_REASONERFACTORY = "org.semanticweb.HermiT.Configuration";

    public OWLlinkHTTPXMLServer createServer(int port) {
       OWLReasonerConfiguration configuration = null;
        try {
            Class reasonerFactoryClass = Class.forName(HERMIT_REASONERFACTORY);
            configuration = (OWLReasonerConfiguration) reasonerFactoryClass.newInstance();
            Field field = reasonerFactoryClass.getDeclaredField("individualNodeSetPolicy");
            field.set(configuration, IndividualNodeSetPolicy.BY_SAME_AS);

        } catch (Exception e) {
            System.err.println("Can't set individualNodeSetPolicy to SAME_AS");
        }

        AbstractOWLlinkReasonerConfiguration config = new AbstractOWLlinkReasonerConfiguration(configuration);
        config.setSupportedDatatypes(OWL2Datatype.XSD_LONG.getIRI(),
                OWL2Datatype.XSD_INT.getIRI(),
                OWL2Datatype.XSD_NON_POSITIVE_INTEGER.getIRI(),
                OWL2Datatype.XSD_NON_NEGATIVE_INTEGER.getIRI(),
                OWL2Datatype.XSD_SHORT.getIRI(),
                OWL2Datatype.OWL_REAL.getIRI());
        try {
            Class factoryClass = Class.forName(FACTORY_CLASSNAME);
            OWLReasonerFactory factory = (OWLReasonerFactory) factoryClass.newInstance();
            OWLlinkHTTPXMLServer server = new OWLlinkHTTPXMLServer(factory, config, port);
            return server;
        } catch (ClassNotFoundException e) {
            throw new OWLlinkServerSetupException(e);
        } catch (InstantiationException e) {
            throw new OWLlinkServerSetupException(e);
        } catch (IllegalAccessException e) {
            throw new OWLlinkServerSetupException(e);
        }

    }

    static void usage() {
        System.out.println("FaCT++ as OWLlink Server");
        System.out.println("OWLlink server that is backed by FaCT++ reasoner");
        System.out.println("");
        System.out.println("Usage: java FactServerFactory [-port portNum]");
        System.out.println("   -port portNum           The port number user by the server (default");
        System.out.println("                           port number used is 8080)");
        System.out.println("   -help                   Print this information");
        System.out.println("Make sure that the FaCT++ binaries are in your classpath or set it via \"-cp\"");
    }


    public static void main(String args[]) {
        int port = 8080;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.equalsIgnoreCase("-help")) {
                usage();
                System.exit(0);
            } else if (arg.equalsIgnoreCase("-port")) {
                try {
                    port = Integer.parseInt(args[++i]);
                } catch (NumberFormatException e1) {
                    System.err.println("Invalid port number: " + args[i]);
                    System.exit(1);
                }
            } else {
                System.err.println("Unrecognized option: " + arg);
                usage();
                System.exit(1);
            }
        }
        HermiTServerFactory factory = new HermiTServerFactory();
        OWLlinkHTTPXMLServer server = factory.createServer(port);
        server.run();
    }
}

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

package org.semanticweb.owlapi.owllink.server;

import org.mortbay.http.*;
import org.mortbay.http.handler.AbstractHttpHandler;
import org.mortbay.util.MultiException;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import java.net.BindException;

/**
 * @author Olaf Noppens
 */
public class OWLlinkHTTPXMLServer extends AbstractHttpHandler implements HttpHandler, OWLlinkServer {
    private static final long serialVersionUID = 5605350732186236386L;
    public static int DEFAULT_PORT = 8080;

    private int port;
    private OWLlinkReasonerBridge bridge;

    public OWLlinkHTTPXMLServer(OWLReasonerFactory reasonerFactory, OWLReasonerConfiguration configuration, int port) {
        this(reasonerFactory, new AbstractOWLlinkReasonerConfiguration(configuration), port);
    }

    public OWLlinkHTTPXMLServer(OWLReasonerFactory reasonerFactory, OWLlinkReasonerConfiguration configuration, int port) {
        this.bridge = new OWLlinkReasonerBridge(reasonerFactory, configuration);
        this.port = port;
    }

    public OWLlinkHTTPXMLServer(OWLReasonerFactory reasonerFactory, OWLReasonerConfiguration configuration) {
        this(reasonerFactory, configuration, DEFAULT_PORT);
    }

    public void handle(String pathInContext, String pathParams, HttpRequest request, HttpResponse response) {
        try {
            response.setContentType("text/xml");
            bridge.process(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void run() {
        try {
            // Create the server
            HttpServer server = new HttpServer();
            // Create a port listener
            SocketListener listener = new SocketListener();
            listener.setHost("localhost");
            listener.setPort(port);
            listener.setMinThreads(2);
            listener.setMaxThreads(10);
            server.addListener(listener);
            // Create a context
            HttpContext context = server.addContext("/");
            context.addHandler(this);
            // Start the http server
            server.start();
        }
        catch (Exception e) {
            if (e instanceof MultiException && ((MultiException) e).getException(0) instanceof BindException)
                System.err.println("Cannot start server. Port " + port + " is already in use!");
            else
                e.printStackTrace();
            System.exit(0);
        }
    }


}

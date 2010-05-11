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

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.owllink.builtin.response.OWLlinkErrorResponseException;
import org.semanticweb.owlapi.owllink.builtin.response.ResponseMessage;
import org.semanticweb.owlapi.owllink.builtin.response.ResponseMessageImpl;
import org.semanticweb.owlapi.owllink.parser.OWLlinkXMLParserHandler;
import org.semanticweb.owlapi.owllink.renderer.OWLlinkXMLRenderer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * @author Olaf Noppens
 */
public class HTTPSessionImpl implements HTTPSession {
    /**
     * threshold for using gzip, the value corresponds to characters not bytes.
     */
    private int gzipThreshold = 5000;
    private boolean useCompression = true;

    private URL reasonerURL;

    private XMLSerializer serializer;

    private OutputFormat format;
    OWLOntologyManager manager;
    PrefixManagerProvider prov;
    private boolean serverAcceptsGzipEncoding = false;

    /**
     * @deprecated Use DIGReasonerPreferences to set logging
     */
    public static boolean log = true;


    public HTTPSessionImpl(OWLOntologyManager manager, PrefixManagerProvider prov) throws MalformedURLException {
        this(manager, new URL("http://localhost:8080"), prov);
        serializer = new XMLSerializer(format);
    }

    public HTTPSessionImpl(OWLOntologyManager manaager, URL reasonerURL, PrefixManagerProvider prov) {
        this.reasonerURL = reasonerURL;
        this.manager = manaager;
        format = new OutputFormat();
        format.setIndent(4);
        format.setIndenting(true);
        format.setPreserveSpace(false);
        this.prov = prov;
    }

    public void setThresholdForCompressedContent(int threshold) {
        this.gzipThreshold = threshold;
    }

    public int getThresholdForCompressedContent() {
        return this.gzipThreshold;
    }

    /**
     * Determines whether content encoding for request should be used if (a) the server supports it and (b)
     * the content is above the {@link #getThresholdForCompressedContent()}.
     *
     * @param compression determines whether compression should be used or not
     */
    public void setUseCompression(boolean compression) {
        this.useCompression = compression;
    }


    /**
     * Sets the URL of the inference.
     *
     * @param url The URL
     */
    public void setReasonerURL(URL url) {
        // Pass this on to the reasoner connection
        reasonerURL = url;
    }


    /**
     * Gets the URL of the reasoner.
     * @return url of the reasoner
     */
    public String getReasonerURL() {
        return reasonerURL.toString();
    }

    public boolean serverAcceptsGzipEncoding() {
        return this.serverAcceptsGzipEncoding;
    }


    public ResponseMessage performRequests(Request... request) throws Exception {
        OWLlinkXMLFactoryRegistry registry = OWLlinkXMLFactoryRegistry.getInstance();
        try {
            //Handle the request
            StringWriter writer = new StringWriter();
            OWLlinkXMLRenderer renderer = new OWLlinkXMLRenderer();
            renderer.addFactories(registry.getRequestRendererFactories());

            Request[] askedRequests = renderer.render(writer, prov, request);

            HttpURLConnection conn = (HttpURLConnection) reasonerURL.openConnection();
            conn.setRequestProperty("Content-Type", "text/xml");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            StringBuffer buffer = writer.getBuffer();

            conn.setRequestProperty("Content-Length", "" + buffer.length());   //todo length is wrong when compressing but we don't want to cache all the stuff in a buffer!
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.connect();

            OutputStream os;
            if (this.useCompression && this.serverAcceptsGzipEncoding() && buffer.length() > getThresholdForCompressedContent()) {
                os = new GZIPOutputStream(conn.getOutputStream());
            } else {
                os = conn.getOutputStream();
            }
            OutputStreamWriter osw = new OutputStreamWriter(os);
            osw.write(buffer.toString());
            if (os instanceof GZIPOutputStream) {
                ((GZIPOutputStream) os).finish();
            }
            osw.flush();
            osw.close();
            if (!serverAcceptsGzipEncoding) {
                Map map = conn.getHeaderFields();
                if (map.containsKey("Accept-Encoding")) {
                    String field = map.get("Accept-Encoding").toString();
                    if (field != null) {
                        StringTokenizer tokenizer = new StringTokenizer(field, ",");
                        while (tokenizer.hasMoreTokens()) {
                            if ("gzip".equals(tokenizer.nextToken())) {
                                serverAcceptsGzipEncoding = true;
                                break;
                            }
                        }
                    }
                }
            }
            // Get the response  if it is gzip or deflate handle it appropriate.
            InputStream is;
            if ("gzip".equals(conn.getContentEncoding())) {
                is = new BufferedInputStream(new GZIPInputStream(conn.getInputStream()));
            } else if ("deflate".equals(conn.getContentEncoding())) {
                is = new BufferedInputStream(new InflaterInputStream(conn.getInputStream(), new Inflater(true)));
            } else {
                is = conn.getInputStream();
            }
            Reader reader = new InputStreamReader(is);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            SAXParser parser = factory.newSAXParser();
            OWLlinkXMLParserHandler handler = new OWLlinkXMLParserHandler(manager, prov, askedRequests, null);
            handler.addFactories(registry.getParserFactories());
            parser.parse(is, handler);
            reader.close();
            conn.disconnect();
            List<Object> responses = handler.getResponses();

            ResponseMessageImpl responseMessage = new ResponseMessageImpl(request);
            int i = 0;
            for (Object response : responses) {
                //if additional inserts prefix query do not add it to responseMessage
                if (!(request[i] instanceof OWLlinkXMLRenderer.InternalRequest)) {
                    if (response instanceof Response)
                        responseMessage.add((Response) response, i);
                    else if (response instanceof OWLlinkErrorResponseException)
                        responseMessage.add((OWLlinkErrorResponseException) response, i);
                } else
                    throw new IllegalArgumentException();
                i++;
            }
            return responseMessage;
        }
        catch (IOException e) {
            throw new OWLlinkReasonerRuntimeException(e.getMessage(), e);
        }
    }


}


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

package org.semanticweb.owlapi.owllink.renderer;

import org.semanticweb.owlapi.io.OWLRendererException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.owllink.KBRequest;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.*;
import org.semanticweb.owlapi.owllink.PrefixManagerProvider;
import org.semanticweb.owlapi.owllink.Request;
import org.semanticweb.owlapi.owllink.builtin.requests.CreateKB;
import org.semanticweb.owlapi.owllink.builtin.requests.GetPrefixes;
import org.semanticweb.owlapi.owllink.builtin.requests.RequestVisitor;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.io.Writer;
import java.util.*;

/**
 * @author Olaf Noppens
 *         todo find requestrenderFactories via property files in user-directory, working-directory, jar-directory
 */
public class OWLlinkXMLRenderer extends BuiltinRequestRenderer {

    protected Map<String, OWLlinkRequestRendererFactory> rendererByRequest;
    private boolean fetchPrefixes = true;

    PrefixManagerProvider prov;

    public OWLlinkXMLRenderer() {
        this.rendererByRequest = new HashMap<String, OWLlinkRequestRendererFactory>();
    }

    public void addFactory(OWLlinkRequestRendererFactory factory) {
        this.rendererByRequest.put(factory.getRequestName(), factory);
    }

    public void addFactories(Collection<OWLlinkRequestRendererFactory> factories) {
        for (OWLlinkRequestRendererFactory factory : factories) {
            addFactory(factory);
        }
    }


    public Request[] render(final Writer writer, final PrefixManagerProvider prov, final Request... requests) throws OWLRendererException {
        final OWLlinkXMLWriter w = new OWLlinkXMLWriter(writer, prov);
        w.startDocument(true);
        int i = 0;
        BitSet additionalQueryIndex = new BitSet();
        this.prov = prov;
        List<Request> realRequests = new ArrayList<Request>();

        for (Request request : requests) {
            realRequests.add(request);
            if (request instanceof KBRequest) {
                //check for kb!
                final KBRequest kbRequest = (KBRequest) request;
                final IRI kb = kbRequest.getKB();
                if (!prov.contains(kb) && fetchPrefixes) {
                    KBGetPrefixes query = new KBGetPrefixes(kb);
                    render(query, w);
                    additionalQueryIndex.set(i);
                    realRequests.add(query);
                }
            }
            render(request, w);
            i++;
        }
        w.endDocument();
        this.prov = null;
        return realRequests.toArray(new Request[realRequests.size()]);
    }

    @Override
    public void answer(Request request) {
        final OWLlinkRequestRendererFactory factory = this.rendererByRequest.get(request.getClass().getName());
        if (factory != null) {
            try {
                factory.createRenderer().render(request, writer);
            } catch (OWLRendererException e) {
                e.printStackTrace();
            }
        }
    }

    public void answer(CreateKB query) {
        writer.writeStartElement(BuiltinRequestVocabulary.CREATEKB.getURI());
        if (query.getName() != null)
            writer.writeAttribute(NAME_Attribute.getURI().toString(), query.getName());
        if (query.getKB() != null)
            writer.writeAttribute(KB_ATTRIBUTE.getURI().toString(), query.getKB().toString());
        if (query.getPrefixes() != null) {
            final DefaultPrefixManager manager = new DefaultPrefixManager();
            final Map<String, String> prefixName2PrefixMap = manager.getPrefixName2PrefixMap();
            for (Map.Entry<String, String> prefix : query.getPrefixes().entrySet()) {
                writer.writeStartElement(PREFIX.getURI());
                writer.writeAttribute(NAME_Attribute.getURI().toString(), prefix.getKey());
                writer.writeAttribute(FULLIRI.getURI().toString(), prefix.getValue());
                writer.writeEndElement();
                prefixName2PrefixMap.put(prefix.getValue(), prefix.getKey());
            }
            if (query.getKB() != null) {
                //if kb is known we can update prefix information
                prov.putPrefixes(query.getKB(), manager);
            }
        }
        writer.writeEndElement();
    }

    public interface InternalRequest {

    }

    public class KBGetPrefixes extends GetPrefixes implements InternalRequest {

        public KBGetPrefixes(IRI kb) {
            super(kb);
        }

        public void accept(RequestVisitor visitor) {
            visitor.answer((GetPrefixes) this);
        }
    }


}

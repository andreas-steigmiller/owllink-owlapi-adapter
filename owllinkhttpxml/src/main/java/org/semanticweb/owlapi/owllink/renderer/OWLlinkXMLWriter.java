/*
 * This file is part of the OWLlink API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, derivo GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2011, derivo GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.semanticweb.owlapi.owllink.renderer;


import org.semanticweb.owlapi.io.OWLRendererException;
import org.semanticweb.owlapi.io.OWLRendererIOException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyWriterConfiguration;
import org.semanticweb.owlapi.model.OWLRuntimeException;
import org.semanticweb.owlapi.owllink.OWLlinkNamespaces;
import org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary;
import org.semanticweb.owlapi.owllink.PrefixManagerProvider;
import org.semanticweb.owlapi.owlxml.renderer.OWLXMLObjectRenderer;
import org.semanticweb.owlapi.owlxml.renderer.OWLXMLWriter;
import org.semanticweb.owlapi.rdf.rdfxml.renderer.XMLWriter;
import org.semanticweb.owlapi.rdf.rdfxml.renderer.XMLWriterNamespaceManager;
import org.semanticweb.owlapi.util.CollectionFactory;
import org.semanticweb.owlapi.vocab.Namespaces;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;
import java.util.Map;

/**
 * @author Olaf Noppens
 */
public class OWLlinkXMLWriter {
    private XMLWriter writer;
    private PrefixManagerProvider prefixProvider;
    Map<IRI, OWLXMLObjectRenderer> rendererByIRI;
    OWLXMLObjectRenderer defaultRenderer;
    Writer baseWriter;
    OWLOntology ontology;

    public OWLlinkXMLWriter(Writer writer, PrefixManagerProvider prefixProvider, OWLOntology ontology) {
        XMLWriterNamespaceManager nsm = new XMLWriterNamespaceManager(OWLlinkNamespaces.OWLLink.toString() + "#");
        nsm.setPrefix("xsd", Namespaces.XSD.toString());
        nsm.setPrefix("owl", Namespaces.OWL.toString());
        String base = OWLlinkNamespaces.OWLLink.toString();
        this.ontology = ontology;
        //we need an own xml writer because in OWL attribute's NS are not allowed.
        this.writer = new MyXMLWriterImpl(writer, nsm, base, ontology.getOWLOntologyManager().getOntologyWriterConfiguration()) {
            @Override
            public void writeAttribute(String attr, String val) {
                if (attr.startsWith(Namespaces.OWL.toString())) {
                    String localName = attr.substring(Namespaces.OWL.toString().length(), attr.length());
                    super.writeAttribute(localName, val);
                } else
                    super.writeAttribute(attr, val);
            }

            @Override
            public void writeStartElement(String name) {
                super.writeStartElement(name);    //To change body of overridden methods use File | Settings | File Templates.
            }
        };
        this.writer.setEncoding("UTF-8");
        OWLXMLWriter owlxmlWriter = new MyOWLXMLWriter(new PrintWriter(writer), this.writer, ontology);
        this.defaultRenderer = new OWLXMLObjectRenderer(owlxmlWriter);
        this.baseWriter = writer;
        rendererByIRI = CollectionFactory.createMap();
        this.prefixProvider = prefixProvider;
    }

    public void startDocument(final boolean isRequest) throws OWLRendererException {
        if (isRequest)
		    writer.startDocument(OWLlinkXMLVocabulary.REQUEST_MESSAGE.getIRI());
		else
		    writer.startDocument(OWLlinkXMLVocabulary.RESPONSE_MESSAGE.getIRI());
    }


    public void endDocument() {
        writer.endDocument();
    }

    public final void writeStartElement(OWLlinkXMLVocabulary v) {
        this.writeStartElement(v.getURI());
    }

    public void writeStartElement(URI name) {
        writer.writeStartElement(IRI.create(name));
    }


    public void writeEndElement() {
        writer.writeEndElement();
    }

    public void writeAttribute(String attribute, String value) {
        writer.writeAttribute(attribute, value);
    }

    public void writeAttribute(URI attribute, String value) {
        writer.writeAttribute(attribute.toString(), value);
    }

    public void writeNegativeAttribute(boolean isNegative) {
        writer.writeAttribute(OWLlinkXMLVocabulary.NEGATIVE_ATTRIBUTE.getURI().toString(), Boolean.toString(isNegative));
    }

    public void writeDirectAttribute(boolean isNegative) {
        writer.writeAttribute(OWLlinkXMLVocabulary.DIRECT_ATTRIBUTE.getURI().toString(), Boolean.toString(isNegative));
    }

    public void writeKBAttribute(IRI kb) {
        writer.writeAttribute(OWLlinkXMLVocabulary.KB_ATTRIBUTE.getURI().toString(), kb.toString());
    }

    public void writeFullIRIAttribute(IRI iri) {
        writer.writeAttribute(OWLlinkXMLVocabulary.IRI_ATTRIBUTE.getURI().toString(), iri.toString());
    }

    /* public void writeIRIAttribute(IRI iri) {
  try {
      String attName = OWLXMLVocabulary.IRI_ATTRIBUTE.getURI().toString();
      String value = iri.toString();
      if (value.startsWith(writer.getXMLBase())) {
          writer.writeAttribute(attName, value.substring(writer.getXMLBase().length(), value.length()));
      } else {
          String val = getIRIString(iri.toURI());
          if (!val.equals(iri.toString())) {
              writer.writeAttribute(OWLXMLVocabulary.ABBREVIATED_IRI_ATTRIBUTE.getURI().toString(), val);
          } else {
              writer.writeAttribute(attName, val);
          }
      }
  }
  catch (IOException e) {
      throw new RuntimeException(e);
  }
}      */


    public void writeOWLObject(OWLObject object, IRI KB) {
        if (KB == null) {
            object.accept(defaultRenderer);
        } else {
            OWLXMLObjectRenderer renderer = rendererByIRI.get(KB);
            if (renderer == null) {
                //OWLXMLWriter writer = new OWLXMLWriter(baseWriter, null);
                OWLXMLWriter writer = new MyOWLXMLWriter(new PrintWriter(this.baseWriter), this.writer, this.ontology);
                if (prefixProvider.contains(KB)) {
                    Map<String, String> map = prefixProvider.getPrefixes(KB).getPrefixName2PrefixMap();
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        writer.getIRIPrefixMap().put(entry.getValue(), entry.getKey());
                    }
                }
                renderer = new OWLXMLObjectRenderer(writer);
                rendererByIRI.put(KB, renderer);
            }
            object.accept(renderer);
        }
    }

    public void writeTextContent(String text) {
        writer.writeTextContent(text);
    }

    public PrefixManagerProvider getPrefixManagerProvider() {
        return this.prefixProvider;
    }
}

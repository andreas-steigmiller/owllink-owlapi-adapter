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

import org.semanticweb.owlapi.owllink.builtin.response.OWLlinkErrorResponseException;
import org.semanticweb.owlapi.owllink.builtin.response.ResponseMessage;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

/**
 * @author Olaf Noppens
 */
public interface OWLlinkReasoner extends OWLReasoner {

    /**
     * Answers the given request. If the OWLlink server answers with an Error response (or
     * any subtype thereof) an OWLlinkErrorResponseException (or an appropriate
     * subtype thereof) will be thrown.
     *
     * @param request Request to be performed
     * @return Response object of the given request
     * @throws OWLlinkErrorResponseException In case that the OWLlink server answers with
     *                                       an Error response an approproate OWLlinkErrorResponseException will be thrown.
     */
    <R extends Response> R answer(Request<R> request) throws OWLlinkErrorResponseException;

    /**
     * Answers the given list of {@link Request requests} in
     * exactly the given ordering.
     *
     * @param request Requests to be answered
     * @return ResponseMessage for the given requests.
     */
    ResponseMessage answer(Request... request);
}

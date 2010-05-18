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

/**
 * Represents an OWLlink BooleanResponse. It encapsulated also the UnknownResponse: if an OWLlink
 * server answers with an UnknownResponse instead of a BooleanResponse a BooleanResponse JAVA object
 * will be created which returns <code>true</code> for {@link #isUnknown()} and throws
 * an {@link org.semanticweb.owlapi.owllink.builtin.response.UnknownResponseException UnknownResponse}
 * if {@link #getResult()} is called.
 *
 * @author Olaf Noppens
 */
public interface BooleanResponse extends KBResponse {
    /**
     * Returns the boolean result. If the result is
     * unknown (according to the OWLlinkSpec this represents an UnknownResponse from OWLlink)
     * an UnknownResponseException will be thrown.
     *
     * @return result
     * @throws UnknownResponseException in case of an OWLlink UnknownResponse instead of an
     *                                  OWLlink BooleanResponse.
     * @see #isUnknown()
     */
    Boolean getResult() throws UnknownResponseException;

    /**
     * Returns <code>true</code> if this response encapsulates an OWLlink UnknownResponse, otherwise
     * <code>true</code>.
     *
     * @return <code>true</code> if this response encapsulates an OWLlink UnknownResponse, otherwise
     *         <code>true</code>
     * @see #getResult()
     */
    boolean isUnknown();
}

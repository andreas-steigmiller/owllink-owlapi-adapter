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

package org.semanticweb.owlapi.owllink.builtin.requests;

import org.semanticweb.owlapi.owllink.Request;


/**
 * Visitor interface for requests. For OWLlink extension queries a
 * general {@link #answer(org.semanticweb.owlapi.owllink.Request) answer} method
 * exists.
 *
 * @author Olaf Noppens
 */
public interface RequestVisitor {

    /**
     * Common answer method for all queries where no specific answer method
     * exists, i.e. for all non-core queries.
     *
     * @param query
     */
    void answer(Request query);

    void answer(Classify query);

    void answer(CreateKB query);

    void answer(GetAllAnnotationProperties query);

    void answer(GetAllClasses query);

    void answer(GetAllDataProperties query);

    void answer(GetAllDatatypes query);

    void answer(GetAllIndividuals query);

    void answer(GetAllObjectProperties query);

    void answer(GetDataPropertiesBetween query);

    void answer(GetDataPropertiesOfLiteral query);

    void answer(GetDataPropertiesOfSource query);

    void answer(GetDataPropertySources query);

    void answer(GetDataPropertyTargets query);

    void answer(GetDescription query);

    void answer(GetDisjointClasses query);

    void answer(GetDisjointDataProperties query);

    void answer(GetDifferentIndividuals query);

    void answer(GetDisjointObjectProperties query);

    void answer(GetEquivalentClasses query);

    void answer(GetEquivalentDataProperties query);

    void answer(GetSameIndividuals query);

    void answer(GetEquivalentObjectProperties query);

    void answer(GetFlattenedDataPropertySources query);

    void answer(GetFlattenedDifferentIndividuals query);

    void answer(GetFlattenedInstances query);

    void answer(GetFlattenedObjectPropertySources query);

    void answer(GetFlattenedObjectPropertyTargets query);

    void answer(GetFlattenedTypes query);

    void answer(GetInstances query);

    void answer(GetKBLanguage query);

    void answer(GetObjectPropertiesBetween query);

    void answer(GetObjectPropertiesOfSource query);

    void answer(GetObjectPropertiesOfTarget query);

    void answer(GetObjectPropertySources query);

    void answer(GetObjectPropertyTargets query);

    void answer(GetPrefixes query);

    void answer(GetSettings query);

    void answer(GetSubClasses query);

    void answer(GetSubClassHierarchy query);

    void answer(GetSubDataProperties query);

    void answer(GetSubDataPropertyHierarchy query);

    void answer(GetSubObjectProperties query);

    void answer(GetSubObjectPropertyHierarchy query);

    void answer(GetSuperClasses query);

    void answer(GetSuperDataProperties query);

    void answer(GetSuperObjectProperties query);

    void answer(GetTypes query);

    void answer(IsClassSatisfiable query);

    void answer(IsDataPropertySatisfiable query);

    void answer(IsKBConsistentlyDeclared query);

    void answer(IsKBSatisfiable query);

    void answer(IsEntailed query);

    void answer(IsEntailedDirect query);

    void answer(IsObjectPropertySatisfiable query);

    void answer(LoadOntologies query);

    void answer(Realize query);

    void answer(ReleaseKB query);

    void answer(Set query);

    void answer(Tell request);
}

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

package org.semanticweb.owlapi.owllink;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.owllink.builtin.requests.CreateKB;
import org.semanticweb.owlapi.owllink.builtin.requests.GetDescription;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSettings;
import org.semanticweb.owlapi.owllink.builtin.response.*;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.Collections;
import java.util.Set;

/**
 * Author: Olaf Noppens
 * Date: 30.11.2009
 */
public class OWLlinkDescriptionTestCase extends AbstractOWLlinkTestCase {

    @Override
    protected Set<? extends OWLAxiom> createAxioms() {
        return CollectionFactory.createSet();
    }

    public void testDescription() throws Exception {
        GetDescription desc = new GetDescription();
        Description answer = reasoner.answer(desc);

        assertTrue(answer.getName().length() > 0);
        assertTrue(answer.getDefaults().size() > 6);
        assertTrue(answer.getProtocolVersion().getMajor() == 1);
    }

    public void testSettings() throws Exception {
        GetDescription desc = new GetDescription();
        Description answer = reasoner.answer(desc);
        Set<OWLlinkLiteral> values = null;
        for (Configuration config : answer.getDefaults()) {
            if ("abbreviatesIRIs".equalsIgnoreCase(config.getKey())) {
                values = config.getValues();
                break;
            }
        }
        assertFalse(values != null && values.isEmpty());
        CreateKB createKB = new CreateKB();
        KB kb = reasoner.answer(createKB);
        GetSettings getSettings = new GetSettings(kb.getKB());
        Settings settings = reasoner.answer(getSettings);
        for (Setting setting : settings) {
            if ("abbreviatesIRIs".equalsIgnoreCase(setting.getKey())) {
                assertTrue(setting.getValues().equals(values));
                String value = setting.getValues().iterator().next().getValue();
                boolean abbreviatesIRIs = Boolean.valueOf(value);
                OWLlinkLiteral lit = new OWLlinkLiteralImpl(Boolean.toString(!abbreviatesIRIs));
                org.semanticweb.owlapi.owllink.builtin.requests.Set query = new org.semanticweb.owlapi.owllink.builtin.requests.Set(kb.getKB(), "abbreviatesIRIs", Collections.singleton(lit));
                OK ok = reasoner.answer(query);

                settings = reasoner.answer(getSettings);
                for (Setting newSetting : settings) {
                    if ("abbreviatesIRIs".equalsIgnoreCase(newSetting.getKey())) {
                        String newValue = newSetting.getValues().iterator().next().getValue();
                        boolean newAbrev = Boolean.valueOf(newValue);
                        assertTrue(abbreviatesIRIs != newAbrev);
                        break;
                    }
                }
                break;
            }
        }


    }
}

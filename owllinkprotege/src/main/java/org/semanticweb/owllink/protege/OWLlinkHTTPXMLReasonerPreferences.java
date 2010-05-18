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

package org.semanticweb.owllink.protege;

import org.protege.editor.core.prefs.Preferences;
import org.protege.editor.core.prefs.PreferencesManager;

/**
 * Author: Olaf Noppens
 * Date: 14.05.2010
 */
public class OWLlinkHTTPXMLReasonerPreferences {
    private static String KEY = "org.semanticweb.owllink";
    private static OWLlinkHTTPXMLReasonerPreferences INSTANCE;

    public static synchronized OWLlinkHTTPXMLReasonerPreferences getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OWLlinkHTTPXMLReasonerPreferences();
        }
        return INSTANCE;
    }


    private Preferences getPreferences() {
        return PreferencesManager.getInstance().getApplicationPreferences(KEY);
    }

    public boolean isUseCompression() {
        return getPreferences().getBoolean("useCompresseion", true);
    }

    public void setUseCompression(boolean useCompression) {
        getPreferences().getBoolean("useCompression", useCompression);
    }

    public void setServerEndpointURL(String url) {
        getPreferences().getString("serverURL", url);
    }

    public String getServerEndpointURL() {
        return getPreferences().getString("serverURL", "http://localhost");
    }

    public int getServerEndpointPort() {
        return getPreferences().getInt("serverPort", 8080);
    }

    public void setServerEndpointPort(int port) {
        getPreferences().putInt("serverPort", port);
    }

}

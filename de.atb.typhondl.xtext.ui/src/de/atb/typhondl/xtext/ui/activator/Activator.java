package de.atb.typhondl.xtext.ui.activator;

/*-
 * #%L
 * de.atb.typhondl.xtext.ui
 * %%
 * Copyright (C) 2018 - 2020 ATB
 * %%
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 * #L%
 */

import org.eclipse.jface.resource.ImageRegistry;
import org.osgi.framework.BundleContext;

import de.atb.typhondl.xtext.ui.internal.XtextActivator;

public class Activator extends XtextActivator {

    private static Activator plugin;
    public final static String IMAGE_PATH = "icons/typhon_icon.png";

    public Activator() {
        super();
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    public static Activator getDefault() {
        return plugin;
    }

    @Override
    protected void initializeImageRegistry(final ImageRegistry reg) {
        reg.put(IMAGE_PATH, imageDescriptorFromPlugin(PLUGIN_ID, IMAGE_PATH));
    }
}

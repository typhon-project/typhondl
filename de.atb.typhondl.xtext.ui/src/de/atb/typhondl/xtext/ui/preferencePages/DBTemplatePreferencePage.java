package de.atb.typhondl.xtext.ui.preferencePages;

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

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.xtext.ui.codetemplates.ui.preferences.AdvancedTemplatesPreferencePage;

import com.google.inject.Inject;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DBType;

/**
 * The Template view of TyphonDL Templates in the Eclipse Preferences is split
 * into {@link DB}s and {@link DBType}s. This class sets the ContentProvider of
 * the TableViewer to {@link DB}.
 * 
 * @author flug
 *
 */
public class DBTemplatePreferencePage extends AdvancedTemplatesPreferencePage {

    @Inject
    public DBTemplatePreferencePage(IPreferenceStore preferenceStore, ContextTypeRegistry registry,
            TemplateStore templateStore) {
        super(preferenceStore, registry, templateStore);
    }

    @Override
    protected Control createContents(Composite ancestor) {
        Control result = super.createContents(ancestor);
        ancestor.layout();
        getTableViewer().setContentProvider(new MyTemplateContentProvider("de.atb.typhondl.xtext.TyphonDL.DB"));
        return result;
    }

}

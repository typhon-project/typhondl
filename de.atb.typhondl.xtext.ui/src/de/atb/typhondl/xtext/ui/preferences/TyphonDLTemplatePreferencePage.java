/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package de.atb.typhondl.xtext.ui.preferences;

import java.util.Iterator;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.text.templates.TemplatePersistenceData;
import org.eclipse.ui.texteditor.templates.TemplatePreferencePage;

import com.google.inject.Inject;

import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;

/**
 * @author Sven Efftinge - Initial contribution and API
 * @since 2.1
 * @author eddited by Marie-Saphira Flug
 */
public class TyphonDLTemplatePreferencePage extends TemplatePreferencePage {
	@Inject
	public TyphonDLTemplatePreferencePage(IPreferenceStore preferenceStore, ContextTypeRegistry registry,
			TemplateStore templateStore) {
		setPreferenceStore(preferenceStore);
		setContextTypeRegistry(registry);
		setTemplateStore(templateStore);
		//addInitialTemplates(); TODO somewhere else: in default Preferences?
	}

	@Override
	protected boolean isShowFormatterSetting() {
		return false;
	}

	/**
	 * @since 2.1
	 */
	@Override
	protected Control createContents(Composite ancestor) {
		Control result = super.createContents(ancestor);
		ancestor.layout();
		return result;
	}

	/**
	 * @since 2.1
	 */
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		// set the with of the leftmost column ('name')
		getTableViewer().getTable().getColumns()[0].pack();
	}

	private void addInitialTemplates() {

//		CheckboxTableViewer fTableViewer = (CheckboxTableViewer) getTableViewer();
		ContextTypeRegistry contextTypeRegistry = getContextTypeRegistry();
		TemplateContextType contextTypeDB = contextTypeRegistry.getContextType("de.atb.typhondl.xtext.TyphonDL.DB");
		Template newTemplate = new Template("TestTemplate", "just testing", contextTypeDB.getId(),
				"database Test : mongo", true);

		getTemplateStore().restoreDefaults(true);
		TemplatePersistenceData data = new TemplatePersistenceData(newTemplate, true);
		if (getTemplateStore().findTemplate(newTemplate.getName())!=null){
			getTemplateStore().add(data);
		}
//		fTableViewer.refresh();
//		fTableViewer.setChecked(data, true);
//		fTableViewer.setSelection(new StructuredSelection(data));

	}
}

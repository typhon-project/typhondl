/**
 * 
 */
package de.atb.typhondl.xtext.ui.preferencePages;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.xtext.ui.editor.templates.XtextTemplatePreferencePage;

import com.google.inject.Inject;

/**
 * @author flug
 *
 */
public class DBTypeTemplatePreferencePage extends XtextTemplatePreferencePage {

	@Inject
	public DBTypeTemplatePreferencePage(IPreferenceStore preferenceStore, ContextTypeRegistry registry,
			TemplateStore templateStore) {
		super(preferenceStore, registry, templateStore);
	}
	
	@Override
	protected Control createContents(Composite ancestor) {
		Control result = super.createContents(ancestor);
		ancestor.layout();
		super.getTableViewer().setContentProvider(new MyTemplateContentProvider("de.atb.typhondl.xtext.TyphonDL.DBType"));
		return result;
	}

}

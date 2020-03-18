package de.atb.typhondl.xtext.ui.preferencePages;

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
 * the TableViewer to {@link DBType}.
 * 
 * @author flug
 *
 */
public class DBTypeTemplatePreferencePage extends AdvancedTemplatesPreferencePage {

	@Inject
	public DBTypeTemplatePreferencePage(IPreferenceStore preferenceStore, ContextTypeRegistry registry,
			TemplateStore templateStore) {
		super(preferenceStore, registry, templateStore);
	}

	@Override
	protected Control createContents(Composite ancestor) {
		Control result = super.createContents(ancestor);
		ancestor.layout();
		super.getTableViewer()
				.setContentProvider(new MyTemplateContentProvider("de.atb.typhondl.xtext.TyphonDL.DBType"));
		return result;
	}

}

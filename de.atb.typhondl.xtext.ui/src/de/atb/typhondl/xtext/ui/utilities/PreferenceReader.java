package de.atb.typhondl.xtext.ui.utilities;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.texteditor.templates.TemplatePreferencePage;
import org.eclipse.xtext.ui.editor.templates.XtextTemplatePreferencePage;

import de.atb.typhondl.xtext.ui.activator.Activator;

public class PreferenceReader {

	/**
	 * @param metatype \in {relationaldb, documentdb, graphdb, keyvaluedb}
	 */
	public static DBMS[] readDBs(String metatype) {
		TemplateStore templateStore = Activator.getDefault().getInjector("de.atb.typhondl.xtext.TyphonDL")
				.getInstance(TemplateStore.class);
		Template[] templates = templateStore.getTemplates();
		DBMS[] dbmss = new DBMS[templates.length];
		for (int i = 0; i < templates.length; i++) {
			DBMS dbms = new DBMS(templates[i].getName(), getAbstractType(templates[i]));
		}
		// TODO Auto-generated method stub
		return null;
	}

	private static String getAbstractType(Template template) {
		template.getPattern();//serialize?
		// TODO Auto-generated method stub
		return null;
	}

}

package de.atb.typhondl.xtext.ui.utilities;

import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.persistence.TemplateStore;

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
			dbmss[i] = new DBMS(templates[i].getName(), getAbstractType(templates[i]));
		}
		return dbmss;
	}

	private static String getAbstractType(Template template) {
		String pattern = template.getPattern();// serialize?
		String dbtype = pattern.substring(pattern.indexOf(':'), pattern.indexOf('{') - 1);
		dbtype.replaceAll(" ", "");
		return dbtype;
	}

}

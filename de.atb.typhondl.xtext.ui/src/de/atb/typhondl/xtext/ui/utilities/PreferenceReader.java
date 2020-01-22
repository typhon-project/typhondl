package de.atb.typhondl.xtext.ui.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.persistence.TemplateStore;

import de.atb.typhondl.xtext.typhonDL.DBType;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.activator.Activator;
import de.atb.typhondl.xtext.ui.creationWizard.ModelCreator;

public class PreferenceReader {

	/**
	 * @param metatype \in {relationaldb, documentdb, graphdb, keyvaluedb}
	 */
	public static DBMS[] readDBs(String metatype) {
		TemplateStore templateStore = Activator.getDefault().getInjector("de.atb.typhondl.xtext.TyphonDL")
				.getInstance(TemplateStore.class);
		Template[] templates = templateStore.getTemplates("de.atb.typhondl.xtext.TyphonDL.DB");
		List<String> supportedTypes = new ArrayList<>();
		String PATH_TO_PROPERTIES = "de/atb/typhondl/xtext/ui/properties/polystore.properties";
		Properties properties = new Properties();
		InputStream inStream = ModelCreator.class.getClassLoader().getResourceAsStream(PATH_TO_PROPERTIES);
		try {
			properties.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		supportedTypes = Arrays.asList(((String) properties.get(metatype)).split(" "));
		ArrayList<DBMS> dbmss = new ArrayList<>();
		for (int i = 0; i < templates.length; i++) {
			String type = getType(templates[i]);
			if (type != null && supportedTypes.contains(type)) { //TODO test
				DBType dbType = TyphonDLFactory.eINSTANCE.createDBType();
				dbType.setName(type);
				dbmss.add(new DBMS(templates[i].getName(), dbType, metatype));
			} 
		}
		return dbmss.toArray(new DBMS[0]);
	}

	private static String getType(Template template) {
		String pattern = template.getPattern();
		int indexOfColon = pattern.indexOf(':');
		if (indexOfColon == -1) { // the template not valid
			return null;
		}
		String dbtype = pattern.substring(indexOfColon+1, pattern.indexOf('{', indexOfColon));
		dbtype = dbtype.replaceAll("\\s", "");
		return dbtype;
	}

}

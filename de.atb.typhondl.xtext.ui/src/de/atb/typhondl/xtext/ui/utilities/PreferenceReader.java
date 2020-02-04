package de.atb.typhondl.xtext.ui.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.conversion.IValueConverterService;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.parser.IParser;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceFactory;
import org.eclipse.xtext.xtext.RuleNames;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DBType;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.activator.Activator;
import de.atb.typhondl.xtext.ui.creationWizard.ModelCreator;

public class PreferenceReader {

	/**
	 * @param metatype \in {relationaldb, documentdb, graphdb, keyvaluedb}
	 */
	@SuppressWarnings("restriction")
	public static DBMS[] readDBs(String metatype) {
		TemplateStore templateStore = Activator.getDefault().getInjector("de.atb.typhondl.xtext.TyphonDL")
				.getInstance(TemplateStore.class);
		IValueConverterService valueConverterService = Activator.getDefault()
				.getInjector("de.atb.typhondl.xtext.TyphonDL").getInstance(IValueConverterService.class);
		RuleNames ruleNames = Activator.getDefault().getInjector("de.atb.typhondl.xtext.TyphonDL")
				.getInstance(RuleNames.class);
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
			DB db = getDB(templates[i]);
			if (db != null) {
				String type = db.getType().getName();
				if (supportedTypes.contains(type)) {
					DBType dbType = TyphonDLFactory.eINSTANCE.createDBType();
					dbType.setName(type);
					dbmss.add(new DBMS(dbType, metatype, templates[i].getName()));
				} else {
					// TODO error message, template is not well defined, only supported DBMS can be
					// used
				}
			} else {
				// TODO error message, template is not well defined, each database must have a
				// type
			}
		}
		return dbmss.toArray(new DBMS[0]);
	}

	private static DB getDB(Template template) {
		String pattern = template.getPattern();
		IParser parser = Activator.getDefault().getInjector("de.atb.typhondl.xtext.TyphonDL")
				.getInstance(IParser.class);
		pattern = pattern.replace("$", "q");
		IParseResult result = parser.parse(createReader(pattern));
		// TODO dbtype = null after parsing because it's a reference to an object that does not exist
		int indexOfColon = pattern.indexOf(':');
		if (indexOfColon == -1) { // the template is not valid TODO warning?
			return null;
		}
		String dbtypeName = pattern.substring(indexOfColon + 1, pattern.indexOf('{', indexOfColon));
		DBType dbtype = TyphonDLFactory.eINSTANCE.createDBType();
		dbtype.setName(dbtypeName);
		List<DB> dbs = ((DeploymentModel) result.getRootASTElement()).getElements().stream()
				.filter(element -> DB.class.isInstance(element)).map(element -> (DB) element).collect(Collectors.toList());
		if (dbs.size() != 1) {
			return null; // there should only be one database in a template TODO warning!
		}
		DB db = dbs.get(0);
		if (db.getType() == null) {
			db.setType(dbtype);
		}
		return db;
	}

	private static Reader createReader(String pattern) {
		return new StringReader(pattern);
	}

}

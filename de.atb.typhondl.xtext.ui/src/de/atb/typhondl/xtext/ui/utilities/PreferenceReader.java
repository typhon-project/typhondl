package de.atb.typhondl.xtext.ui.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.jface.text.templates.TemplateTranslator;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.parser.IParser;

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
		IParser parser = Activator.getDefault().getInjector("de.atb.typhondl.xtext.TyphonDL")
				.getInstance(IParser.class);
		ArrayList<DBMS> dbmss = new ArrayList<>();
		for (int i = 0; i < templates.length; i++) {
			TemplateBuffer buffer = getTemplateBuffer(templates[i]);
			// for now the buffer variables do not contain the DBType
			// parsed like this, the DBType is null
			DB db = getDB(parser.parse(new StringReader(buffer.getString())));
			if (db != null) {
				for (String supportedType : supportedTypes) {
					if (buffer.getString().contains(supportedType)) {
						DBType dbType = TyphonDLFactory.eINSTANCE.createDBType();
						dbType.setName(supportedType);
						db.setType(dbType);
					}
				}
				if (db.getType() != null) {
					dbmss.add(new DBMS(db.getType(), metatype, templates[i].getName()));
				} else {
					// TODO error message, template is not well defined, only supported DBMS can be
					// used and each database must have a type
				}
			} else {
				// TODO error message, db could not be parsed
			}
		}
		return dbmss.toArray(new DBMS[0]);
	}

	private static TemplateBuffer getTemplateBuffer(Template template) {
		TemplateTranslator templateTranslator = new TemplateTranslator();
		TemplateBuffer buffer = null;
		try {
			buffer = templateTranslator.translate(template);
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	private static DB getDB(IParseResult result) {
		List<DB> dbs = ((DeploymentModel) result.getRootASTElement()).getElements().stream()
				.filter(element -> DB.class.isInstance(element)).map(element -> (DB) element)
				.collect(Collectors.toList());
		if (dbs.size() != 1) {
			return null; // there should only be one database in a template TODO warning!
		}
		DB db = dbs.get(0);
		return db;
	}

}

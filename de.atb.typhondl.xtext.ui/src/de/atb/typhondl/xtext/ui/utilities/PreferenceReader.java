package de.atb.typhondl.xtext.ui.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
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

/**
 * The PreferenceReader reads the templates defined in Eclipse Preferences ->
 * TyphonDL -> DB Templates and DBType Templates for each metatype
 * (relationaldb, documentdb, graphdb, keyvaluedb). Only DBTypes listed in the
 * plugins polystore.properties can be used.
 * 
 * @author flug
 *
 */
public class PreferenceReader {

	/**
	 * read all DBs fitting the <code>metatype</code> from the Eclipse Preferences
	 * TyphonDL Template pages. Also adds the fitting DBType (also taken from the templates).
	 * 
	 * @param metatype \in {relationaldb, documentdb, graphdb, keyvaluedb}
	 */
	public static DBMS[] readDBs(String metatype) {
		TemplateStore templateStore = Activator.getDefault().getInjector("de.atb.typhondl.xtext.TyphonDL")
				.getInstance(TemplateStore.class);
		// load the DB and DBType templates
		Template[] dbTemplates = templateStore.getTemplates("de.atb.typhondl.xtext.TyphonDL.DB");
		Template[] dbTypeTemplates = templateStore.getTemplates("de.atb.typhondl.xtext.TyphonDL.DBType");
		// get polystore.properties to link the metatype to the actual DBMSs
		String PATH_TO_PROPERTIES = "de/atb/typhondl/xtext/ui/properties/polystore.properties";
		Properties properties = new Properties();
		InputStream inStream = ModelCreator.class.getClassLoader().getResourceAsStream(PATH_TO_PROPERTIES);
		try {
			properties.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<String> possibleTypes = Arrays.asList(((String) properties.get(metatype)).split(" "));
		// get the parser to transalte the template pattern into a TyphonDL model
		// instance
		IParser parser = Activator.getDefault().getInjector("de.atb.typhondl.xtext.TyphonDL")
				.getInstance(IParser.class);
		// get all dbTypes that match the metatype
		ArrayList<DBType> dbTypes = new ArrayList<>();
		for (int i = 0; i < dbTypeTemplates.length; i++) {
			TemplateBuffer buffer = getTemplateBuffer(dbTypeTemplates[i]);
			DBType dbtype = getModelObject(TyphonDLFactory.eINSTANCE.createDBType(),
					parser.parse(new StringReader(buffer.getString())));
			if (possibleTypes.contains(dbtype.getName())) {
				dbTypes.add(dbtype);
			}
		}
		ArrayList<DBMS> dbmss = new ArrayList<>();
		for (int i = 0; i < dbTemplates.length; i++) {
			TemplateBuffer buffer = getTemplateBuffer(dbTemplates[i]);
			// for now the buffer variables do not contain the DBType
			// parsed like this, the DBType is null
			DB db = getModelObject(TyphonDLFactory.eINSTANCE.createDB(),
					parser.parse(new StringReader(buffer.getString())));
			if (db != null) {
				for (DBType supportedType : dbTypes) {
					if (buffer.getString().contains(supportedType.getName())) {
						db.setType(supportedType);
					}
				}
				if (db.getType() != null) {
					dbmss.add(new DBMS(db.getType(), metatype, dbTemplates[i].getName()));
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

	/**
	 * 
	 * @param template
	 * @return the TemplateBuffer containing the translated template pattern as a
	 *         String and an array of the template's variables
	 */
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

	/**
	 * Transforms the parsed <code>result</code> into a model object of given Type
	 * <code>T</code>
	 * 
	 * @param <T>         the type of the wanted model object (here DB or DBType)
	 * @param modelObject a dummy modelObject to pass the wanted Type
	 * @param result      the parsed template pattern
	 * @return the model object or <code>null</code> if the template does not
	 *         contain exactly one definition
	 */
	private static <T extends EObject> T getModelObject(T modelObject, IParseResult result) {
		@SuppressWarnings("unchecked")
		List<T> elements = ((DeploymentModel) result.getRootASTElement()).getElements().stream()
				.filter(element -> modelObject.getClass().isInstance(element)).map(element -> (T) element)
				.collect(Collectors.toList());
		if (elements.size() != 1) {
			return null; // there should only be one model definition in a template TODO warning!
		}
		return elements.get(0);
	}
}

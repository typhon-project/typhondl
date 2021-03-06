package de.atb.typhondl.xtext.ui.utilities;

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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.jface.text.templates.TemplateTranslator;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.xtext.EcoreUtil2;
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
     * TyphonDL Template pages. Also checks the dbType
     * 
     * @param metatype \in {relationaldb, documentdb, graphdb, keyvaluedb}
     * @return A list of valid template DBs
     */
    public static ArrayList<DB> getBuffers(String metatype) {
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
        List<String> possibleTypes = Arrays.asList(((String) properties.get(metatype)).toLowerCase().split(" "));
        // get all dbTypes that match the metatype
        ArrayList<DBType> dbTypes = new ArrayList<>();
        for (int i = 0; i < dbTypeTemplates.length; i++) {
            TemplateBuffer buffer = getTemplateBuffer(dbTypeTemplates[i]);
            DBType dbtype = getModelObject(TyphonDLFactory.eINSTANCE.createDBType(), buffer);
            if (possibleTypes.contains(dbtype.getName().toLowerCase())) {
                dbTypes.add(dbtype);
            }
        }
        ArrayList<DB> templates = new ArrayList<>();
        for (int i = 0; i < dbTemplates.length; i++) {
            TemplateBuffer buffer = getTemplateBuffer(dbTemplates[i]);
            // for now the buffer variables do not contain the DBType
            // parsed like this, the DBType is null
            DB db = getModelObject(TyphonDLFactory.eINSTANCE.createDB(), buffer);
            if (db != null) {
                for (DBType supportedType : dbTypes) {
                    if (buffer.getString().toLowerCase().contains(supportedType.getName().toLowerCase() + " ")
                            || buffer.getString().toLowerCase().contains(supportedType.getName().toLowerCase() + "{")) {
                        db.setName(dbTemplates[i].getName());
                        db.setType(supportedType);
                        templates.add(db);
                    }
                }
            } else {
                // TODO warning about error in template?
            }
        }
        return templates;
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
     * Transforms the parsed <code>pattern</code> from the TemplateBuffer into a
     * model object of given Type <code>T</code>
     * 
     * @param <T>         the type of the wanted model object (here DB or DBType)
     * @param modelObject a dummy modelObject to pass the wanted Type
     * @param buffer      the TemplateBuffer
     * @return the model object or <code>null</code> if the template does not
     *         contain exactly one definition
     */
    public static <T extends EObject> T getModelObject(T modelObject, TemplateBuffer buffer) {
        // get the parser to translate the template pattern into a TyphonDL model
        // instance
        IParseResult result = Activator.getDefault().getInjector("de.atb.typhondl.xtext.TyphonDL")
                .getInstance(IParser.class).parse(new StringReader(buffer.getString()));
        DeploymentModel deploymentModel = (DeploymentModel) result.getRootASTElement();
        @SuppressWarnings("unchecked")
        List<T> elements = (List<T>) EcoreUtil2.getAllContentsOfType(deploymentModel, modelObject.getClass());
        // there should only be one model definition in a template
        return elements.size() == 1 ? elements.get(0) : null;
    }
}

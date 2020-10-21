package de.atb.typhondl.xtext.ui.modelUtils;

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

import java.util.List;
import java.util.Properties;

import org.eclipse.xtext.EcoreUtil2;

import de.atb.typhondl.xtext.typhonDL.Credentials;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DBType;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.IMAGE;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

/**
 * Utility class for easier {@link DB} model object handling
 * 
 * @author flug
 *
 */
public class DBService {

    /**
     * Creates the polystore-mongo metadata DB to store the XMI representations of
     * the ML and DL model
     * 
     * @param properties
     * @param clusterType
     * @param mongo
     * @return a new DB object to be used as polystore-mongo
     */
    public static DB createPolystoreDB(Properties properties, SupportedTechnologies clusterType, DBType mongo) {
        DB polystoredb = create(properties.getProperty(PropertiesService.DB_NAME), mongo);
        if (clusterType == SupportedTechnologies.DockerCompose) { // TODO TYP-186
            polystoredb.setEnvironment(SoftwareService.createEnvironment(new String[] { "MONGO_INITDB_DATABASE",
                    properties.getProperty(PropertiesService.DB_ENVIRONMENT_MONGO_INITDB_DATABASE) }));
        }
        polystoredb.setCredentials(createCredentials("admin", "admin"));
        return polystoredb;
    }

    /**
     * Create new {@link Credentials} for a {@link DB}
     * 
     * @param username
     * @param password
     * @return a new Credentials object with username and password
     */
    public static Credentials createCredentials(String username, String password) {
        Credentials credentials = TyphonDLFactory.eINSTANCE.createCredentials();
        credentials.setUsername(username);
        credentials.setPassword(password);
        return credentials;
    }

    /**
     * Create a new {@link DB} object
     * 
     * @param name   name of the database
     * @param dbType type, i.e. DBMS e.g. mongo, mariadb, neo4j, cassandra
     * @return a new DB object
     */
    public static DB create(String name, DBType dbType) {
        DB db = TyphonDLFactory.eINSTANCE.createDB();
        db.setName(name);
        db.setType(dbType);
        return db;
    }

    /**
     * Adds the {@link DBType} "Mongo" to the given {@link DeploymentModel} if not
     * present
     * 
     * @param model the DeploymentModel to add mongo to
     * @return the given DeploymentModel with added DBType mongo
     */
    public static DeploymentModel addMongoIfNotExists(DeploymentModel model) {
        // if Mongo is not allready a DBType, add Mongo
        DBType mongo = getMongoDBType(model);
        if (mongo == null) {
            mongo = TyphonDLFactory.eINSTANCE.createDBType();
            mongo.setName("Mongo");
            IMAGE mongoImage = TyphonDLFactory.eINSTANCE.createIMAGE();
            mongoImage.setValue("mongo:latest");
            mongo.setImage(mongoImage);
            model.getElements().add(mongo);
        }
        return model;
    }

    /**
     * Retrieves {@link DBType} mongo from a {@link DeploymentModel}
     * 
     * @param model the DeploymentModel to find mongo in
     * @return the found DBType mongo
     */
    public static DBType getMongoDBType(DeploymentModel model) {
        List<DBType> dbTypes = EcoreUtil2.getAllContentsOfType(model, DBType.class);
        return dbTypes.stream().filter(dbType -> dbType.getName().equalsIgnoreCase("mongo")).findFirst().orElse(null);
    }

}

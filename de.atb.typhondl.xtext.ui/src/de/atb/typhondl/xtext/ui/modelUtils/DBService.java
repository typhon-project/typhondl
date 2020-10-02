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

public class DBService {

    public static DB createPolystoreDB(Properties properties, SupportedTechnologies clusterType, DBType mongo) {
        DB polystoredb = create(properties.getProperty("db.name"), mongo);
        if (clusterType == SupportedTechnologies.DockerCompose) {
            polystoredb.setEnvironment(SoftwareService.createEnvironment(new String[] { "MONGO_INITDB_DATABASE",
                    properties.getProperty(PropertiesService.DB_ENVIRONMENT_MONGO_INITDB_DATABASE) }));
        }
        polystoredb.setCredentials(createCredentials("admin", "admin"));
        return polystoredb;
    }

    public static Credentials createCredentials(String username, String password) {
        Credentials credentials = TyphonDLFactory.eINSTANCE.createCredentials();
        credentials.setUsername(username);
        credentials.setPassword(password);
        return credentials;
    }

    public static DB create(String name, DBType dbType) {
        DB db = TyphonDLFactory.eINSTANCE.createDB();
        db.setName(name);
        db.setType(dbType);
        return db;
    }

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

    public static DBType getMongoDBType(DeploymentModel model) {
        List<DBType> dbTypes = EcoreUtil2.getAllContentsOfType(model, DBType.class);
        return dbTypes.stream().filter(dbType -> dbType.getName().equalsIgnoreCase("mongo")).findFirst().orElse(null);
    }

}

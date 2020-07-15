package de.atb.typhondl.xtext.ui.modelUtils;

import java.util.List;
import java.util.Properties;

import org.eclipse.xtext.EcoreUtil2;

import de.atb.typhondl.xtext.typhonDL.Credentials;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DBType;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.IMAGE;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;

public class DBService {

    public static DB createPolystoreDB(Properties properties, String clusterType, DBType mongo) {
        DB polystoredb = create(properties.getProperty("db.name"), mongo);
        if (clusterType.equalsIgnoreCase("DockerCompose")) {
            polystoredb.setEnvironment(SoftwareService.createEnvironment(new String[] { "MONGO_INITDB_DATABASE",
                    properties.getProperty("db.environment.MONGO_INITDB_DATABASE") }));
        }
        polystoredb.setCredentials(createCredentials("admin", "admin"));
        return null;
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

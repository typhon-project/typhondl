package de.atb.typhondl.xtext.ui.modelUtils;

import java.util.List;
import java.util.Properties;

import org.eclipse.xtext.EcoreUtil2;

import de.atb.typhondl.xtext.typhonDL.Credentials;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DBType;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Environment;
import de.atb.typhondl.xtext.typhonDL.IMAGE;
import de.atb.typhondl.xtext.typhonDL.Key_Values;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;

public class DBService {

    public static DB createPolystoreDB(Properties properties, String clusterType, DBType mongo) {
        DB polystoredb = create(properties.getProperty("db.name"), mongo);
        if (clusterType.equalsIgnoreCase("DockerCompose")) {
            Environment polystoredb_environment = TyphonDLFactory.eINSTANCE.createEnvironment();
            Key_Values polystoredb_environment_1 = TyphonDLFactory.eINSTANCE.createKey_Values();
            polystoredb_environment_1.setName("MONGO_INITDB_DATABASE");
            polystoredb_environment_1.setValue(properties.getProperty("db.environment.MONGO_INITDB_DATABASE"));
            polystoredb_environment.getParameters().add(polystoredb_environment_1);
            polystoredb.setEnvironment(polystoredb_environment);
        }
        Credentials credentials = TyphonDLFactory.eINSTANCE.createCredentials();
        credentials.setUsername("admin");
        credentials.setPassword("admin");
        polystoredb.setCredentials(credentials);
        return null;
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

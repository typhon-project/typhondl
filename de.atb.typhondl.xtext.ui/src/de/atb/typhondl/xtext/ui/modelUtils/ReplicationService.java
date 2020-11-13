package de.atb.typhondl.xtext.ui.modelUtils;

import java.util.Properties;

import de.atb.typhondl.xtext.typhonDL.Modes;
import de.atb.typhondl.xtext.typhonDL.Replication;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.technologies.ITechnology;

public class ReplicationService {

    /**
     * db.getType().getName().toLowerCase()
     * 
     * @param dbType
     * @param chosenTechnology
     * @param properties
     * @return
     */
    public static String getReplicationProperty(String dbType, ITechnology chosenTechnology, Properties properties) {
        String propertyName = dbType + ".replication" + "." + chosenTechnology.getType().name().toLowerCase();
        return properties.getProperty(propertyName) != null ? properties.getProperty(propertyName) : "";
    }

    public static Replication createDefaultReplication(String replicationProperty) {
        Replication replication;
        replication = TyphonDLFactory.eINSTANCE.createReplication();
        replication.setReplicas(3);
        replication.setMode(Modes.getByName(replicationProperty));
        return replication;
    }
}

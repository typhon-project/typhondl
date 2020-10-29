package de.atb.typhondl.xtext.ui.technologies;

import java.util.List;
import java.util.Properties;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.ui.utilities.InputField;

public interface ITechnology {

    public void setConnectionDefaults(Properties properties);

    public void insertModelsToMetadata(Container polystoreMongoContainer, String outputFolder,
            String mongoInsertStatement, Properties properties);

    public List<InputField> kafkaInputFields();

    public String displayedName();

    public String containerType();

    public boolean createAllAnalyticsContainers();

    public String defaultVolumesType();

    public boolean canUseHelm();

    public boolean canUseKubeConfig();

    public boolean canDoStatelessReplication();

    public int minPort();

    public int maxPort();

    public boolean setInitDB();

    public boolean waitForMetadata();

    public String kafkaInternalURI();

    public boolean restartIsDefault();

    public SupportedTechnologies getType();

}

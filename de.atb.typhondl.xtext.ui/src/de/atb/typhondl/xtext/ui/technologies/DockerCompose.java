package de.atb.typhondl.xtext.ui.technologies;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.scriptGeneration.DeploymentModelService;
import de.atb.typhondl.xtext.ui.utilities.InputField;

/**
 * Implements DockerCompose
 * 
 * @author flug
 *
 */
public class DockerCompose implements ITechnology {

    private SupportedTechnologies type;

    public DockerCompose(SupportedTechnologies type) {
        this.type = type;
    }

    @Override
    public void setConnectionDefaults(Properties properties) {
        properties.setProperty(PropertiesService.API_PUBLISHEDPORT, "8080");
        properties.setProperty(PropertiesService.UI_PUBLISHEDPORT, "4200");
    }

    @Override
    public void insertModelsToMetadata(Container polystoreMongoContainer, String outputFolder,
            String mongoInsertStatement, Properties properties) {
        DeploymentModelService.writeInsertStatementToJavaScriptFile(outputFolder, mongoInsertStatement, properties);
    }

    @Override
    public List<InputField> kafkaInputFields() {
        return Arrays.asList(new InputField("Kafka version: ", "analytics.kafka.version"));
    }

    @Override
    public String displayedName() {
        return "Docker Compose";
    }

    @Override
    public String containerType() {
        return "Docker";
    }

    @Override
    public boolean createAllAnalyticsContainers() {
        return true;
    }

    @Override
    public String defaultVolumesType() {
        return "volume";
    }

    @Override
    public boolean canUseHelm() {
        return false;
    }

    @Override
    public boolean canUseKubeConfig() {
        return false;
    }

    @Override
    public boolean canDoStatelessReplication() {
        return false;
    }

    @Override
    public int minPort() {
        return 1;
    }

    @Override
    public int maxPort() {
        return 65535;
    }

    @Override
    public boolean setInitDB() {
        return true;
    }

    @Override
    public boolean waitForMetadata() {
        return true;
    }

    @Override
    public String kafkaInternalURI() {
        return "localhost:29092";
    }

    @Override
    public boolean restartIsDefault() {
        return false;
    }

    @Override
    public SupportedTechnologies getType() {
        return this.type;
    }

    @Override
    public boolean canDeployEvolution() {
        return true;
    }

}

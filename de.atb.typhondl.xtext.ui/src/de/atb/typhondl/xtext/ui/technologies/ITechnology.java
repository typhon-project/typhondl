package de.atb.typhondl.xtext.ui.technologies;

import java.util.List;
import java.util.Properties;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.ui.utilities.InputField;

/**
 * If another Technology shall be added, this interface has to be implemented,
 * {@link SupportedTechnologies} and {@link TechnologyFactory} have to be
 * extended.
 * 
 * @author flug
 *
 */
public interface ITechnology {

    /**
     * Sets default connection parameters:
     * <li>API_PUBLISHEDPORT</li>
     * <li>UI_PUBLISHEDPORT</li>
     * 
     * @param properties
     */
    public void setConnectionDefaults(Properties properties);

    /**
     * Inserts the ML and DL model to the metadata container
     * 
     * @param polystoreMongoContainer the Container in which the metadata database
     *                                will be deployed
     * @param outputFolder            the folder that will contain the deployment
     *                                scripts
     * @param mongoInsertStatement    the db.database.insert(...) statement
     * @param properties              the polystore properties
     */
    public void insertModelsToMetadata(Container polystoreMongoContainer, String outputFolder,
            String mongoInsertStatement, Properties properties);

    /**
     * @return A List of {@link InputField}s to configure the Analytics component
     */
    public List<InputField> kafkaInputFields();

    /**
     * @return The name to show in Wizard's combo
     */
    public String displayedName();

    /**
     * @return The type of container associated with this technology (e.g. Docker)
     */
    public String containerType();

    /**
     * @return true if all five Analytics containers (kafka, zookeeper, authAll,
     *         flink jobmanager, flink taskmanager) should be created, false if just
     *         a kafka container for the API to reference should be created.
     */
    public boolean createAllAnalyticsContainers();

    /**
     * @return Default Volumes Type for the technology (e.g. "volume" or
     *         "persistentVolumeClaim")
     */
    public String defaultVolumesType();

    /**
     * @return true if Helm Charts can be used
     */
    public boolean canUseHelm();

    /**
     * @return true if a kubeconfig file can be used
     */
    public boolean canUseKubeConfig();

    /**
     * @return true if stateless replication is provided
     */
    public boolean canDoStatelessReplication();

    /**
     * @return minimal number in published port range
     */
    public int minPort();

    /**
     * @return maximal number in published port range
     */
    public int maxPort();

    /**
     * @return true if the ML and DL model can be inserted into the metadata
     *         container via a script in docker-entrypoint-initdb.d when the
     *         container is first created
     */
    public boolean setInitDB();

    /**
     * @return true if the API container has to wait for the metadata container to
     *         be ready and have the models uploaded
     */
    public boolean waitForMetadata();

    /**
     * @return internal kafka URI
     */
    public String kafkaInternalURI();

    /**
     * @return if false, "restart = always" is added to QL and API containers
     */
    public boolean restartIsDefault();

    /**
     * @return enum constant associated with this technology
     */
    public SupportedTechnologies getType();

    /**
     * @return true if evolution component is configured to be deployed with this
     *         technology
     */
    public boolean canDeployEvolution();

}

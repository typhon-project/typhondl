package de.atb.typhondl.xtext.ui.creationWizard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.ui.resource.XtextLiveScopeResourceSetProvider;

import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.Cluster;
import de.atb.typhondl.xtext.typhonDL.Cluster_Network;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.ContainerType;
import de.atb.typhondl.xtext.typhonDL.Container_Network;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DBType;
import de.atb.typhondl.xtext.typhonDL.Dependency;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.IMAGE;
import de.atb.typhondl.xtext.typhonDL.Import;
import de.atb.typhondl.xtext.typhonDL.Key_KeyValueList;
import de.atb.typhondl.xtext.typhonDL.Key_Value;
import de.atb.typhondl.xtext.typhonDL.Key_ValueArray;
import de.atb.typhondl.xtext.typhonDL.NonDB;
import de.atb.typhondl.xtext.typhonDL.Platform;
import de.atb.typhondl.xtext.typhonDL.PlatformType;
import de.atb.typhondl.xtext.typhonDL.Reference;
import de.atb.typhondl.xtext.typhonDL.Software;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.activator.Activator;
import de.atb.typhondl.xtext.ui.creationWizard.CreationAnalyticsPage.InputField;
import de.atb.typhondl.xtext.ui.utilities.Database;
import de.atb.typhondl.xtext.ui.utilities.SavingOptions;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

public class ModelCreator {

	// the source ML model from which a DL model is created
	private IFile MLmodel;
	// the resourceSet containing all tdl resources
	private XtextResourceSet resourceSet;
	// path to folder in which to save all model files
	private IPath folder;
	private String DLmodelName;

	public ModelCreator(IFile MLmodel, String DLmodelName) {
		this.MLmodel = MLmodel;
		this.folder = this.MLmodel.getFullPath().removeLastSegments(1);
		this.DLmodelName = DLmodelName;
		addResources();
	}

	/**
	 * Gets the provided ResourceSet and adds all .tdl files to the ResourceSet
	 */
	private void addResources() {
		this.resourceSet = (XtextResourceSet) Activator.getInstance()
				.getInjector(Activator.DE_ATB_TYPHONDL_XTEXT_TYPHONDL)
				.getInstance(XtextLiveScopeResourceSetProvider.class).get(this.MLmodel.getProject());
		this.resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		IResource members[] = null;
		try {
			members = this.MLmodel.getProject().members();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		for (IResource member : members) {
			if (member instanceof IFile) {
				if (((IFile) member).getFileExtension().equals("tdl")) {
					resourceSet.getResource(URI.createPlatformResourceURI(member.getFullPath().toString(), true), true);
				}
			}
		}
	}

	public IFile createDLmodel(HashMap<String, InputField> analyticsSettings, ArrayList<Database> databases,
			int chosenTemplate) {

		// create main model
		DeploymentModel DLmodel = TyphonDLFactory.eINSTANCE.createDeploymentModel();
		// add reference to ML model
		Import MLmodelImport = TyphonDLFactory.eINSTANCE.createImport();
		MLmodelImport.setRelativePath(this.MLmodel.getName());
		DLmodel.getGuiMetaInformation().add(MLmodelImport);

		final boolean useAnalytics = analyticsSettings != null;

		// create dummy platform type
		PlatformType platformType = TyphonDLFactory.eINSTANCE.createPlatformType();
		platformType.setName("default");
		DLmodel.getElements().add(platformType);

		// Add selected container type (chosen template in wizard)
		ContainerType containerType = TyphonDLFactory.eINSTANCE.createContainerType();
		containerType.setName(SupportedTechnologies.values()[chosenTemplate].getContainerType());
		DLmodel.getElements().add(containerType);

		ArrayList<DB> dbs = new ArrayList<DB>();
		ArrayList<DBType> dbTypes = new ArrayList<DBType>();

		for (Database database : databases) {
			Import importedDB = TyphonDLFactory.eINSTANCE.createImport();
			DB db;
			DeploymentModel dbModel;
			if (database.getPathToDBModelFile() != null) { // use existing .tdl file
				URI dbURI = URI.createPlatformResourceURI(
						this.folder.append(database.getPathToDBModelFile()).toString(), true);
				dbModel = (DeploymentModel) resourceSet.getResource(dbURI, true).getContents().get(0);
				db = getDB(dbModel);
				importedDB.setRelativePath(database.getPathToDBModelFile());
			} else {
				db = TyphonDLFactory.eINSTANCE.createDB();
				db.setName(database.getName());
				db.setType(database.getDbms());
				IMAGE image = TyphonDLFactory.eINSTANCE.createIMAGE();
				image.setValue(db.getType().getName() + ":latest");
				importedDB.setRelativePath(db.getName() + ".tdl");
				db.setImage(image);
				db = addEnvironment(db);
				save(db);
			}
			dbs.add(db);
			DLmodel.getGuiMetaInformation().add(importedDB);
			boolean containsType = false;
			for (DBType dbType : dbTypes) {
				if (dbType.getName().equals(db.getType().getName())) {
					containsType = true;
				}
			}
			if (!containsType) {
				dbTypes.add(db.getType());
			}
		}

		// mongo DB is always needed for the polystore api
		DBType mongo = null;
		for (DB db : dbs) { // types need to be the same instance
			for (DBType dbtype : dbTypes) {
				if (dbtype.getName().equals("mongo")) {
					mongo = dbtype;
				}
				if (dbtype.getName().equals(db.getType().getName())) {
					db.setType(dbtype);
				}
			}
		}
		if (mongo == null) {
			mongo = TyphonDLFactory.eINSTANCE.createDBType();
			mongo.setName("mongo");
			dbTypes.add(mongo);
		}

		/*
		 * Polystore DB for WP7 Polystore API
		 */
		DB polystoredb;
		if (checkExist(createSoftwareURI("polystoredb"))) {
			polystoredb = getDB((DeploymentModel) resourceSet.getResource(createSoftwareURI("polystoredb"), true)
					.getContents().get(0));
			polystoredb.setType(mongo);
		} else {
			polystoredb = TyphonDLFactory.eINSTANCE.createDB();
			polystoredb.setName("polystoredb");
			polystoredb.setType(mongo);
			IMAGE polystoredb_image = TyphonDLFactory.eINSTANCE.createIMAGE();
			polystoredb_image.setValue("mongo:latest");
			polystoredb.setImage(polystoredb_image);
			Key_KeyValueList polystoredb_environment = TyphonDLFactory.eINSTANCE.createKey_KeyValueList();
			polystoredb_environment.setName("environment");
			Key_Value polystoredb_environment_1 = TyphonDLFactory.eINSTANCE.createKey_Value();
			polystoredb_environment_1.setName("MONGO_INITDB_ROOT_USERNAME");
			polystoredb_environment_1.setValue("admin");
			polystoredb_environment.getKey_Values().add(polystoredb_environment_1);
			Key_Value polystoredb_environment_2 = TyphonDLFactory.eINSTANCE.createKey_Value();
			polystoredb_environment_2.setName("MONGO_INITDB_ROOT_PASSWORD");
			polystoredb_environment_2.setValue("admin");
			polystoredb_environment.getKey_Values().add(polystoredb_environment_2);
			polystoredb.getParameters().add(polystoredb_environment);
			save(polystoredb);
		}
		Reference poystoredbReference = TyphonDLFactory.eINSTANCE.createReference();
		poystoredbReference.setReference(polystoredb);

		Container polystoredb_container = TyphonDLFactory.eINSTANCE.createContainer();
		polystoredb_container.setName("polystoredb");
		polystoredb_container.setType(containerType);
		polystoredb_container.setDeploys(poystoredbReference);
		Key_Value polystoredb_container_container_name = TyphonDLFactory.eINSTANCE.createKey_Value();
		polystoredb_container_container_name.setName("container_name");
		polystoredb_container_container_name.setValue("polystore.mongo");
		polystoredb_container.getProperties().add(polystoredb_container_container_name);
		Key_ValueArray polystoredb_container_ports = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
		polystoredb_container_ports.setName("ports");
		polystoredb_container_ports.setValue("27017:27017");
		polystoredb_container.getProperties().add(polystoredb_container_ports);

		Dependency polystoredb_dependency = TyphonDLFactory.eINSTANCE.createDependency();
		polystoredb_dependency.setReference(polystoredb_container);

		/*
		 * polystore_api
		 */
		NonDB polystore_api;
		if (checkExist(createSoftwareURI("polystore_api"))) {
			polystore_api = getNonDB((DeploymentModel) resourceSet.getResource(createSoftwareURI("polystore_api"), true)
					.getContents().get(0));
		} else {
			polystore_api = TyphonDLFactory.eINSTANCE.createNonDB();
			polystore_api.setName("polystore_api");
			IMAGE polystore_api_image = TyphonDLFactory.eINSTANCE.createIMAGE();
			polystore_api_image.setValue("clms/typhon-polystore-api:latest");
			polystore_api.setImage(polystore_api_image);
			Key_Value polystore_api_restart = TyphonDLFactory.eINSTANCE.createKey_Value();
			polystore_api_restart.setName("restart");
			polystore_api_restart.setValue("always");
			polystore_api.getParameters().add(polystore_api_restart);
			Key_Value polystore_api_hostname = TyphonDLFactory.eINSTANCE.createKey_Value();
			polystore_api_hostname.setName("hostname");
			polystore_api_hostname.setValue("polystore_api");
			polystore_api.getParameters().add(polystore_api_hostname);
			save(polystore_api);
		}
		Reference polystore_api_reference = TyphonDLFactory.eINSTANCE.createReference();
		polystore_api_reference.setReference(polystore_api);

		Container polystore_api_container = TyphonDLFactory.eINSTANCE.createContainer();
		polystore_api_container.setName("polystore_api");
		polystore_api_container.setType(containerType);
		polystore_api_container.setDeploys(polystore_api_reference);
		polystore_api_container.getDepends_on().add(polystoredb_dependency);
		Key_Value polystore_api_container_container_name = TyphonDLFactory.eINSTANCE.createKey_Value();
		polystore_api_container_container_name.setName("container_name");
		polystore_api_container_container_name.setValue("typhonml.polystore.service");
		polystore_api_container.getProperties().add(polystore_api_container_container_name);
		Key_ValueArray polystore_api_container_ports = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
		polystore_api_container_ports.setName("ports");
		polystore_api_container_ports.setValue("8080:8080");
		polystore_api_container.getProperties().add(polystore_api_container_ports);
		Key_ValueArray polystore_api_container_volumes = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
		polystore_api_container_volumes.setName("volumes");
		polystore_api_container_volumes.setValue("./models:/models");
		polystore_api_container.getProperties().add(polystore_api_container_volumes);

		Dependency polystore_api_dependency = TyphonDLFactory.eINSTANCE.createDependency();
		polystore_api_dependency.setReference(polystore_api_container);

		/*
		 * polystore ui
		 */
		NonDB polystore_ui;
		if (checkExist(createSoftwareURI("polystore_ui"))) {
			polystore_ui = getNonDB((DeploymentModel) resourceSet.getResource(createSoftwareURI("polystore_ui"), true)
					.getContents().get(0));
		} else {
			polystore_ui = TyphonDLFactory.eINSTANCE.createNonDB();
			polystore_ui.setName("polystore_ui");
			IMAGE polystore_ui_image = TyphonDLFactory.eINSTANCE.createIMAGE();
			polystore_ui_image.setValue("clms/typhon-polystore-ui:latest");
			polystore_ui.setImage(polystore_ui_image);
			Key_Value polystore_ui_restart = TyphonDLFactory.eINSTANCE.createKey_Value();
			polystore_ui_restart.setName("restart");
			polystore_ui_restart.setValue("always");
			polystore_ui.getParameters().add(polystore_ui_restart);
			Key_Value polystore_ui_hostname = TyphonDLFactory.eINSTANCE.createKey_Value();
			polystore_ui_hostname.setName("hostname");
			polystore_ui_hostname.setValue("polystore_ui");
			polystore_ui.getParameters().add(polystore_ui_hostname);
			save(polystore_ui);
		}
		Reference polystore_ui_reference = TyphonDLFactory.eINSTANCE.createReference();
		polystore_ui_reference.setReference(polystore_ui);

		Container polystore_ui_container = TyphonDLFactory.eINSTANCE.createContainer();
		polystore_ui_container.setName("polystore_ui");
		polystore_ui_container.setType(containerType);
		polystore_ui_container.setDeploys(polystore_ui_reference);
		polystore_ui_container.getDepends_on().add(polystore_api_dependency);
		Key_Value polystore_ui_container_container_name = TyphonDLFactory.eINSTANCE.createKey_Value();
		polystore_ui_container_container_name.setName("container_name");
		polystore_ui_container_container_name.setValue("polystore.ui");
		polystore_ui_container.getProperties().add(polystore_ui_container_container_name);
		Key_ValueArray polystore_ui_container_ports = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
		polystore_ui_container_ports.setName("ports");
		polystore_ui_container_ports.setValue("4200:4200");
		polystore_ui_container.getProperties().add(polystore_ui_container_ports);
		Key_KeyValueList polystore_ui_container_build = TyphonDLFactory.eINSTANCE.createKey_KeyValueList();
		polystore_ui_container_build.setName("build");
		Key_Value polystore_ui_container_build_context = TyphonDLFactory.eINSTANCE.createKey_Value();
		polystore_ui_container_build_context.setName("context");
		polystore_ui_container_build_context.setValue("Typhon Service UI");
		polystore_ui_container_build.getKey_Values().add(polystore_ui_container_build_context);

		/*
		 * Analytics, see https://github.com/typhon-project/typhondl/issues/6
		 */
		Container zookeeper_container = null;
		Container kafka_container = null;
		Cluster_Network typhonNetwork = null;
		if (useAnalytics) {
			String zookeeperPort = analyticsSettings.get("zookeeperPort").value;
			String kafkaPort = analyticsSettings.get("kafkaPort").value;
			String[] kafkaListeners = analyticsSettings.get("kafkaListeners").value.split("\\s*,\\s*");
			String kafkaListenerName = analyticsSettings.get("kafkaListenerName").value;
			String kafkaListenersString = "";
			String kafkaAdvertisedListenerString = "";
			for (int i = 0; i < kafkaListeners.length; i++) {
				kafkaListenersString += kafkaListenerName + "://:" + kafkaListeners[i] + ", ";
				kafkaAdvertisedListenerString += kafkaListenerName + "://kafka:" + kafkaListeners[i] + ", ";
			}
			kafkaListenersString += "PLAINTEXT_HOST://:" + kafkaPort;
			kafkaAdvertisedListenerString += "PLAINTEXT_HOST://localhost:" + kafkaPort;

			NonDB zookeeper;
			if (checkExist(createSoftwareURI("zookeeper"))) {
				zookeeper = getNonDB((DeploymentModel) resourceSet.getResource(createSoftwareURI("zookeeper"), true)
						.getContents().get(0));
			} else {
				zookeeper = TyphonDLFactory.eINSTANCE.createNonDB();
				zookeeper.setName("zookeeper");
				IMAGE zookeeper_image = TyphonDLFactory.eINSTANCE.createIMAGE();
				zookeeper_image.setValue("wurstmeister/zookeeper");
				zookeeper.setImage(zookeeper_image);
				save(zookeeper);
			}
			Reference zookeeper_reference = TyphonDLFactory.eINSTANCE.createReference();
			zookeeper_reference.setReference(zookeeper);

			typhonNetwork = TyphonDLFactory.eINSTANCE.createCluster_Network();
			typhonNetwork.setName("typhon");
			zookeeper_container = TyphonDLFactory.eINSTANCE.createContainer();
			zookeeper_container.setName("zookeeper");
			zookeeper_container.setType(containerType);
			zookeeper_container.setDeploys(zookeeper_reference);
			Key_Value zookeeper_container_name = TyphonDLFactory.eINSTANCE.createKey_Value();
			zookeeper_container_name.setName("container_name");
			zookeeper_container_name.setValue("zookeeper");
			zookeeper_container.getProperties().add(zookeeper_container_name);
			Key_ValueArray zookeeper_container_ports = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
			zookeeper_container_ports.setName("ports");
			zookeeper_container_ports.setValue("2181:" + zookeeperPort);
			zookeeper_container.getProperties().add(zookeeper_container_ports);
			Container_Network zookeeper_container_networks = TyphonDLFactory.eINSTANCE.createContainer_Network();
			zookeeper_container_networks.setReference(typhonNetwork);
			zookeeper_container.setNetworks(zookeeper_container_networks);

			Dependency zookeeper_dependency = TyphonDLFactory.eINSTANCE.createDependency();
			zookeeper_dependency.setReference(zookeeper_container);

			kafka_container = TyphonDLFactory.eINSTANCE.createContainer();
			kafka_container.setName("kafka");
			kafka_container.setType(containerType);
			Key_Value kafka_container_build = TyphonDLFactory.eINSTANCE.createKey_Value();
			kafka_container_build.setName("build");
			kafka_container_build.setValue(".");
			kafka_container.getProperties().add(kafka_container_build);
			kafka_container.getDepends_on().add(zookeeper_dependency);
			Key_Value kafka_container_name = TyphonDLFactory.eINSTANCE.createKey_Value();
			kafka_container_name.setName("container_name");
			kafka_container_name.setValue("kafka");
			kafka_container.getProperties().add(kafka_container_name);
			Key_ValueArray kafka_container_ports = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
			kafka_container_ports.setName("ports");
			kafka_container_ports.setValue("9092:" + kafkaPort);
			kafka_container.getProperties().add(kafka_container_ports);
			Container_Network kafka_container_networks = TyphonDLFactory.eINSTANCE.createContainer_Network();
			kafka_container_networks.setReference(typhonNetwork);
			kafka_container.setNetworks(kafka_container_networks);
			Key_ValueArray kafka_container_volumes = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
			kafka_container_volumes.setName("volumes");
			kafka_container_volumes.setValue("/var/run/docker.sock:/var/run/docker.sock");
			kafka_container.getProperties().add(kafka_container_volumes);
			Key_KeyValueList kafka_environment = TyphonDLFactory.eINSTANCE.createKey_KeyValueList();
			kafka_environment.setName("environment");
			Key_Value KAFKA_ZOOKEEPER_CONNECT = TyphonDLFactory.eINSTANCE.createKey_Value();
			KAFKA_ZOOKEEPER_CONNECT.setName("KAFKA_ZOOKEEPER_CONNECT");
			KAFKA_ZOOKEEPER_CONNECT.setValue("zookeeper:" + zookeeperPort);
			kafka_environment.getKey_Values().add(KAFKA_ZOOKEEPER_CONNECT);
			Key_Value KAFKA_ADVERTISED_HOST_NAME = TyphonDLFactory.eINSTANCE.createKey_Value();
			KAFKA_ADVERTISED_HOST_NAME.setName("KAFKA_ADVERTISED_HOST_NAME");
			KAFKA_ADVERTISED_HOST_NAME.setValue("kafka");
			kafka_environment.getKey_Values().add(KAFKA_ADVERTISED_HOST_NAME);
			Key_Value KAFKA_LISTENERS = TyphonDLFactory.eINSTANCE.createKey_Value();
			KAFKA_LISTENERS.setName("KAFKA_LISTENERS");
			KAFKA_LISTENERS.setValue(kafkaListenersString);
			kafka_environment.getKey_Values().add(KAFKA_LISTENERS);
			Key_Value KAFKA_LISTENER_SECURITY_PROTOCOL_MAP = TyphonDLFactory.eINSTANCE.createKey_Value();
			KAFKA_LISTENER_SECURITY_PROTOCOL_MAP.setName("KAFKA_LISTENER_SECURITY_PROTOCOL_MAP");
			KAFKA_LISTENER_SECURITY_PROTOCOL_MAP.setValue(kafkaListenerName + ":PLAINTEXT, PLAINTEXT_HOST:PLAINTEXT");
			kafka_environment.getKey_Values().add(KAFKA_LISTENER_SECURITY_PROTOCOL_MAP);
			Key_Value KAFKA_INTER_BROKER_LISTENER_NAME = TyphonDLFactory.eINSTANCE.createKey_Value();
			KAFKA_INTER_BROKER_LISTENER_NAME.setName("KAFKA_INTER_BROKER_LISTENER_NAME");
			KAFKA_INTER_BROKER_LISTENER_NAME.setValue(kafkaListenerName);
			kafka_environment.getKey_Values().add(KAFKA_INTER_BROKER_LISTENER_NAME);
			Key_Value KAFKA_ADVERTISED_LISTENERS = TyphonDLFactory.eINSTANCE.createKey_Value();
			KAFKA_ADVERTISED_LISTENERS.setName("KAFKA_ADVERTISED_LISTENERS");
			KAFKA_ADVERTISED_LISTENERS.setValue(kafkaAdvertisedListenerString);
			kafka_environment.getKey_Values().add(KAFKA_ADVERTISED_LISTENERS);
			Key_Value KAFKA_AUTO_CREATE_TOPICS_ENABLE = TyphonDLFactory.eINSTANCE.createKey_Value();
			KAFKA_AUTO_CREATE_TOPICS_ENABLE.setName("KAFKA_AUTO_CREATE_TOPICS_ENABLE");
			KAFKA_AUTO_CREATE_TOPICS_ENABLE.setValue("\"true\"");
			kafka_environment.getKey_Values().add(KAFKA_AUTO_CREATE_TOPICS_ENABLE);
			kafka_container.getProperties().add(kafka_environment);
		}

		/*
		 * start container structure
		 */
		Platform deployment = TyphonDLFactory.eINSTANCE.createPlatform();
		deployment.setName("platformName");
		deployment.setType(platformType);
		DLmodel.getElements().add(deployment);

		Cluster cluster = TyphonDLFactory.eINSTANCE.createCluster();
		cluster.setName("clusterName");
		if (useAnalytics) {
			cluster.getNetworks().add(typhonNetwork);
		}
		deployment.getClusters().add(cluster);

		Application application = TyphonDLFactory.eINSTANCE.createApplication();
		application.setName("Polystore");
		cluster.getApplications().add(application);

		application.getContainers().add(polystore_api_container);
		application.getContainers().add(polystore_ui_container);
		application.getContainers().add(polystoredb_container);
		if (useAnalytics) {
			application.getContainers().add(zookeeper_container);
			application.getContainers().add(kafka_container);
		}

		for (DB db : dbs) {
			Container container = TyphonDLFactory.eINSTANCE.createContainer();
			container.setName(db.getName());
			container.setType(containerType);
			Reference reference = TyphonDLFactory.eINSTANCE.createReference();
			reference.setReference(db);
			container.setDeploys(reference);
			Key_ValueArray db_ports = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
			db_ports.setName("ports");
			db_ports.setValue(getStandardPorts(db.getType().getName()));
			container.getProperties().add(db_ports);
			application.getContainers().add(container);
		}

		/*
		 * save main model file
		 */
		URI DLmodelURI = createSoftwareURI(DLmodelName);
		Resource DLmodelResource = resourceSet.createResource(DLmodelURI);
		DLmodelResource.getContents().add(DLmodel);
		try {
			DLmodelResource.save(SavingOptions.getTDLoptions());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// return main model file to be opened in editor
		return ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(DLmodelURI.toPlatformString(true)));
	}

	// TODO This should be from an external config file
	private DB addEnvironment(DB db) {
		switch (db.getType().getName()) {
		case "mariadb":
			Key_KeyValueList mariadbenvironment = TyphonDLFactory.eINSTANCE.createKey_KeyValueList();
			mariadbenvironment.setName("environment");
			Key_Value mariadbenv1 = TyphonDLFactory.eINSTANCE.createKey_Value();
			mariadbenv1.setName("MYSQL_ROOT_PASSWORD");
			mariadbenv1.setValue("example");
			mariadbenvironment.getKey_Values().add(mariadbenv1);
			db.getParameters().add(mariadbenvironment);
			break;
		case "mysql":
			Key_KeyValueList mysqlenvironment = TyphonDLFactory.eINSTANCE.createKey_KeyValueList();
			mysqlenvironment.setName("environment");
			Key_Value mysqlenv1 = TyphonDLFactory.eINSTANCE.createKey_Value();
			mysqlenv1.setName("MYSQL_ROOT_PASSWORD");
			mysqlenv1.setValue("example");
			mysqlenvironment.getKey_Values().add(mysqlenv1);
			Key_Value mysqlcommand = TyphonDLFactory.eINSTANCE.createKey_Value();
			mysqlcommand.setName("command");
			mysqlcommand.setValue("--default-authentication-plugin=mysql_native_password");
			db.getParameters().add(mysqlcommand);
			db.getParameters().add(mysqlenvironment);
			break;
		case "mongo":
			Key_KeyValueList mongoenvironment = TyphonDLFactory.eINSTANCE.createKey_KeyValueList();
			mongoenvironment.setName("environment");
			Key_Value mongoenv1 = TyphonDLFactory.eINSTANCE.createKey_Value();
			mongoenv1.setName("MONGO_INITDB_ROOT_USERNAME");
			mongoenv1.setValue("admin");
			Key_Value mongoenv2 = TyphonDLFactory.eINSTANCE.createKey_Value();
			mongoenv2.setName("MONGO_INITDB_ROOT_PASSWORD");
			mongoenv2.setValue("admin");
			mongoenvironment.getKey_Values().add(mongoenv1);
			mongoenvironment.getKey_Values().add(mongoenv2);
			db.getParameters().add(mongoenvironment);
			break;
		default:
			break;
		}
		return db;
	}

	// TODO This should be from an external config file
	private String getStandardPorts(String name) {
		switch (name) {
		case "mariadb":
			return "3306:3306";
		case "mysql":
			return "3306:3306";
		case "mongo":
			return "27017:27018"; // 27017 is occupied by documentdbs
		default:
			return "0:0";
		}
	}

	private void save(Software software) {
		DeploymentModel softwareModel = TyphonDLFactory.eINSTANCE.createDeploymentModel();
		if (DB.class.isInstance(software)) {
			softwareModel.getElements().add(((DB) software).getType());
		}
		softwareModel.getElements().add(software);
		URI softwareURI = createSoftwareURI(software.getName());
		// delete resource if it already exists
		if (checkExist(softwareURI)) {
			try {
				resourceSet.getResource(softwareURI, true).delete(Collections.EMPTY_MAP);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Resource softwareResource = resourceSet.createResource(softwareURI);
		softwareResource.getContents().add(software.eContainer());
		try {
			softwareResource.save(SavingOptions.getTDLoptions());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private URI createSoftwareURI(String name) {
		return URI.createPlatformResourceURI(this.folder.append(name + ".tdl").toString(), true);
	}

	private boolean checkExist(URI softwareURI) {
		return resourceSet.getResource(softwareURI, false) != null;
	}

	private DB getDB(DeploymentModel model) {
		ArrayList<DB> dbs = new ArrayList<DB>();
		dbs.addAll(model.getElements().stream().filter(element -> DB.class.isInstance(element))
				.map(element -> (DB) element).collect(Collectors.toList()));
		return dbs.get(0);
	}

	private NonDB getNonDB(DeploymentModel model) {
		ArrayList<NonDB> Nondbs = new ArrayList<NonDB>();
		Nondbs.addAll(model.getElements().stream().filter(element -> NonDB.class.isInstance(element))
				.map(element -> (NonDB) element).collect(Collectors.toList()));
		return Nondbs.get(0);
	}

}

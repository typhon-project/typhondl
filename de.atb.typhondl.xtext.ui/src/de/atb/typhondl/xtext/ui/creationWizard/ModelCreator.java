package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.ui.resource.XtextLiveScopeResourceSetProvider;

import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.Cluster;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.ContainerType;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DBType;
import de.atb.typhondl.xtext.typhonDL.Dependency;
import de.atb.typhondl.xtext.typhonDL.Deployment;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.IMAGE;
import de.atb.typhondl.xtext.typhonDL.Import;
import de.atb.typhondl.xtext.typhonDL.Key_KeyValueList;
import de.atb.typhondl.xtext.typhonDL.Key_Value;
import de.atb.typhondl.xtext.typhonDL.Key_ValueArray;
import de.atb.typhondl.xtext.typhonDL.NonDB;
import de.atb.typhondl.xtext.typhonDL.PlatformType;
import de.atb.typhondl.xtext.typhonDL.Reference;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.activator.Activator;
import de.atb.typhondl.xtext.ui.creationWizard.CreationAnalyticsPage.InputField;
import de.atb.typhondl.xtext.ui.utilities.DLmodelReader;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

public class ModelCreator {

	public static DeploymentModel createDLmodel(HashMap<String, InputField> analyticsSettings,
			Set<Database> databaseInfo, IFile MLmodelPath, int chosenTemplate) {

		XtextResourceSet resourceSet = (XtextResourceSet) Activator.getInstance()
				.getInjector(Activator.DE_ATB_TYPHONDL_XTEXT_TYPHONDL)
				.getInstance(XtextLiveScopeResourceSetProvider.class).get(MLmodelPath.getProject());
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);

//URI modelURI = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
//
//Resource resource = resourceSet.getResource(modelURI, true);
		
		DeploymentModel DLmodel = TyphonDLFactory.eINSTANCE.createDeploymentModel();
		Import MLmodel = TyphonDLFactory.eINSTANCE.createImport();
		MLmodel.setRelativePath(getModelName(MLmodelPath));
		DLmodel.getGuiMetaInformation().add(MLmodel);

		final boolean useAnalytics = analyticsSettings != null;

		PlatformType platformType = TyphonDLFactory.eINSTANCE.createPlatformType();
		platformType.setName("default");
		DLmodel.getElements().add(platformType);

		ContainerType containerType = TyphonDLFactory.eINSTANCE.createContainerType();
		containerType.setName(SupportedTechnologies.values()[chosenTemplate].getContainerType());
		DLmodel.getElements().add(containerType);

		ArrayList<Database> databases = new ArrayList<Database>(databaseInfo);
		ArrayList<DB> dbs = new ArrayList<DB>();
		ArrayList<DBType> dbTypes = new ArrayList<DBType>();
		DBType mongo = TyphonDLFactory.eINSTANCE.createDBType();
		mongo.setName("mongo");
		dbTypes.add(mongo);
		for (Database database : databases) {
			Import importedDB = TyphonDLFactory.eINSTANCE.createImport();
			DB db;
			
			if (database.getPathToDBModelFile() != null) { // use existing .tdl file
				DeploymentModel existing = DLmodelReader
						.readDLmodel(MLmodelPath.getFullPath().removeLastSegments(1).append(database.getPathToDBModelFile()));
				db = DLmodelReader.getDBs(existing).get(0);
				dbs.add(db);
				importedDB.setRelativePath(database.getPathToDBModelFile());
			} else if (database.getDbms() != null) {
				db = TyphonDLFactory.eINSTANCE.createDB();
				db.setName(database.getName());
				db.setType(database.getDbms());
				IMAGE image = TyphonDLFactory.eINSTANCE.createIMAGE();
				image.setValue(db.getType().getName() + "image");
				db.setImage(image);
				dbs.add(db);
				importedDB.setRelativePath(database.getName() + ".tdl");
			} else {
				db = null; // this never happens
				System.out.println("ModelCreator.createDLmodel -> dbms == null && PathToDBModelFile == null");
			}
			boolean containsType = false;
			for (DBType dbType : dbTypes) {
				if (dbType.getName().equals(db.getType().getName())) {
					containsType = true;
				}
			}
			if (!containsType) {
				dbTypes.add(db.getType());
			}
			DLmodel.getGuiMetaInformation().add(importedDB);
		}

		DLmodel.getElements().addAll(dbTypes);

		for (DB db : dbs) { // types need to be the same instance
			for (DBType dbtype : dbTypes) {
				if (dbtype.getName().equals(db.getType().getName())) {
					db.setType(dbtype);
				}
			}
		}
		DLmodel.getElements().addAll(dbs);// TODO cross refence between files does not work yet

		/**
		 * Polystore DB for WP7 Polystore API
		 */
		DB polystoredb = TyphonDLFactory.eINSTANCE.createDB();
		polystoredb.setName("polystoredb");
		polystoredb.setType(mongo);
		IMAGE polystoredb_image = TyphonDLFactory.eINSTANCE.createIMAGE();
		polystoredb_image.setValue("mongo:latest");
		polystoredb.setImage(polystoredb_image);
		DLmodel.getElements().add(polystoredb);

		Container polystoredb_container = TyphonDLFactory.eINSTANCE.createContainer();
		polystoredb_container.setName("polystoredb");
		polystoredb_container.setType(containerType);
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

		/**
		 * polystore_api
		 */
		NonDB polystore_api = TyphonDLFactory.eINSTANCE.createNonDB();
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
		DLmodel.getElements().add(polystore_api); // TODO cross refence between files does not work yet

		Reference polystore_api_reference = TyphonDLFactory.eINSTANCE.createReference();
		polystore_api_reference.setReference(polystore_api);

		Container polystore_api_container = TyphonDLFactory.eINSTANCE.createContainer();
		polystore_api_container.setName("polystore_api");
		polystore_api_container.setType(containerType);
		polystore_api_container.getDeploys().add(polystore_api_reference);
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

		/**
		 * polystore ui
		 */
		NonDB polystore_ui = TyphonDLFactory.eINSTANCE.createNonDB();
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
		DLmodel.getElements().add(polystore_ui); // TODO cross refence between files does not work yet

		Reference polystore_ui_reference = TyphonDLFactory.eINSTANCE.createReference();
		polystore_ui_reference.setReference(polystore_ui);

		Container polystore_ui_container = TyphonDLFactory.eINSTANCE.createContainer();
		polystore_ui_container.setName("polystore_ui");
		polystore_ui_container.setType(containerType);
		polystore_ui_container.getDeploys().add(polystore_ui_reference);
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

		/**
		 * Analytics, see https://github.com/typhon-project/typhondl/issues/6
		 */
		Container zookeeper_container = null;
		Container kafka_container = null;
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

			NonDB zookeeper = TyphonDLFactory.eINSTANCE.createNonDB();
			zookeeper.setName("zookeeper");
			IMAGE zookeeper_image = TyphonDLFactory.eINSTANCE.createIMAGE();
			zookeeper_image.setValue("wurstmeister/zookeeper");
			zookeeper.setImage(zookeeper_image);
			DLmodel.getElements().add(zookeeper);

			Reference zookeeper_reference = TyphonDLFactory.eINSTANCE.createReference();
			zookeeper_reference.setReference(zookeeper);

			zookeeper_container = TyphonDLFactory.eINSTANCE.createContainer();
			zookeeper_container.setName("zookeeper");
			zookeeper_container.setType(containerType);
			zookeeper_container.getDeploys().add(zookeeper_reference);
			Key_Value zookeeper_container_name = TyphonDLFactory.eINSTANCE.createKey_Value();
			zookeeper_container_name.setName("container_name");
			zookeeper_container_name.setValue("zookeeper");
			zookeeper_container.getProperties().add(zookeeper_container_name);
			Key_ValueArray zookeeper_container_ports = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
			zookeeper_container_ports.setName("ports");
			zookeeper_container_ports.setValue("2181:" + zookeeperPort);
			zookeeper_container.getProperties().add(zookeeper_container_ports);
			Key_ValueArray zookeeper_container_networks = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
			zookeeper_container_networks.setName("networks");
			zookeeper_container_networks.setValue("typhon");// TODO this should reference to top level "networks"
			zookeeper_container.getProperties().add(zookeeper_container_networks);

			Dependency zookeeper_dependency = TyphonDLFactory.eINSTANCE.createDependency();
			zookeeper_dependency.setReference(zookeeper_container);

			kafka_container = TyphonDLFactory.eINSTANCE.createContainer();
			kafka_container.setName("kafka");
			kafka_container.setType(containerType);
			kafka_container.getDepends_on().add(zookeeper_dependency);
			Key_Value kafka_container_name = TyphonDLFactory.eINSTANCE.createKey_Value();
			kafka_container_name.setName("container_name");
			kafka_container_name.setValue("kafka");
			kafka_container.getProperties().add(kafka_container_name);
			Key_ValueArray kafka_container_ports = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
			kafka_container_ports.setName("ports");
			kafka_container_ports.setValue("9092:" + kafkaPort);
			kafka_container.getProperties().add(kafka_container_ports);
			Key_ValueArray kafka_container_networks = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
			kafka_container_networks.setName("networks");
			kafka_container_networks.setValue("typhon");// TODO this should reference to top level "networks"
			kafka_container.getProperties().add(kafka_container_networks);
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
		}
		/**
		 * start container structure
		 */
		Deployment deployment = TyphonDLFactory.eINSTANCE.createDeployment();
		deployment.setName("platformName");
		deployment.setType(platformType);
		DLmodel.getElements().add(deployment);

		Cluster cluster = TyphonDLFactory.eINSTANCE.createCluster();
		cluster.setName("clusterName");
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
			container.getDeploys().add(reference);
			application.getContainers().add(container);
		}

		return DLmodel;
	}

	private static String getModelName(IPath path) {
		String string = path.toString();
		return string.substring(string.lastIndexOf('/') + 1, string.length());
	}

}

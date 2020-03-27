package de.atb.typhondl.acceleo.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.ui.resource.XtextLiveScopeResourceSetProvider;

import de.atb.typhondl.acceleo.main.Generate;
import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.ContainerType;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DBType;
import de.atb.typhondl.xtext.typhonDL.Dependency;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.IMAGE;
import de.atb.typhondl.xtext.typhonDL.Import;
import de.atb.typhondl.xtext.typhonDL.Key_KeyValueList;
import de.atb.typhondl.xtext.typhonDL.Key_ValueArray;
import de.atb.typhondl.xtext.typhonDL.Key_Values;
import de.atb.typhondl.xtext.typhonDL.Platform;
import de.atb.typhondl.xtext.typhonDL.Ports;
import de.atb.typhondl.xtext.typhonDL.Reference;
import de.atb.typhondl.xtext.typhonDL.Software;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;

/**
 * Utility class for the script generation process
 * 
 * @author flug
 *
 */
public class Services {

	/**
	 * Starts the script generation process, deletes old files
	 * 
	 * @param file     The main DL model from which to generate the scripts
	 * @param provider The injected resourceSetProvider
	 * @return A result String informing the user whether the generation succeeded
	 */
	public static String generateDeployment(IFile file, XtextLiveScopeResourceSetProvider provider) {
		String result = "";
		try {
			String outputFolder = file.getLocation().toOSString().replace("." + file.getFileExtension(), "");
			deleteOldGeneratedFiles(new File(outputFolder));
			DeploymentModel model = loadXtextModel(file, provider);
			new Generate(model, new File(outputFolder), new ArrayList<String>()).doGenerate(new BasicMonitor());
			result = getResult(new File(outputFolder));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Checks if a yml file exists in projects subfolders TODO do better
	 * 
	 * @param folder the folder to check for yml files
	 * @return Informative String for the user.
	 */
	private static String getResult(File folder) {
		if (!folder.exists()) {
			return "Something went wrong";
		} else {
			for (File subFile : folder.listFiles()) {
				if (subFile.getName().contains("yml") || subFile.getName().contains("yaml")) { // TODO horrible
					return "Deployment script generated";
				}
				if (subFile.getName().endsWith("xmi")) {
					return "Only the model to export was generated";
				}
			}
		}
		return "";
	}

	/**
	 * Deletes all files and folders in given folder
	 * 
	 * @param folder All files an folders in this folder are deleted
	 */
	private static void deleteOldGeneratedFiles(File folder) {
		if (folder.exists()) {
			for (File subFile : folder.listFiles()) {
				if (subFile.isDirectory()) {
					deleteOldGeneratedFiles(subFile);
				} else {
					subFile.delete();
				}
			}
		}
	}

	/**
	 * Reads the TyphonDL model from the given main model, using the
	 * {@link XtextLiveScopeResourceSetProvider} to find other model files in the
	 * folder. Also saves the model in a .xmi file.
	 * 
	 * @param file     The main TyphonDL model
	 * @param provider Provides the correct resourceSet for finding other model
	 *                 files
	 * @return The TyphonDL model including all information taken from other related
	 *         model files
	 */
	public static DeploymentModel loadXtextModel(IFile file, XtextLiveScopeResourceSetProvider provider) {

		XtextResourceSet resourceSet = (XtextResourceSet) provider.get(file.getProject());
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		// adds all .tdl files in project folder to resourceSet
		IResource members[] = null;
		try {
			members = file.getProject().members();
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
		// read DL model
		URI modelURI = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
		Resource DLmodelResource = resourceSet.getResource(modelURI, true);
		DeploymentModel model = (DeploymentModel) DLmodelResource.getContents().get(0);
		String path = Paths.get(file.getLocationURI()).getParent().resolve("polystore.properties").toString();
		model = addDBsToModel(model);
		model = addHostnamesToModel(model);
		Properties properties = new Properties();
		try {
			InputStream input = new FileInputStream(path);
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();// TODO popup if nonexistent
		}
		model = addPolystoreToModel(path, model, properties);
		URI DLmodelXMI = saveModelAsXMI(DLmodelResource);
		createMongoCommands(
				Paths.get(file.getLocation().toOSString().replace(file.getName(),
						DLmodelXMI.segment(DLmodelXMI.segmentCount() - 2) + File.separator + DLmodelXMI.lastSegment())),
				Paths.get(file.getLocation().toOSString().replace(file.getName(), getMLmodelPath(model))), properties);
		return model;
	}

	/**
	 * Creates a "addModels.js" file inside the db.volume directory. This script is
	 * executed the first time the polystore-mongo container is started and adds
	 * both the ML and DL model to the database db.environment.MONGO_INITDB_DATABASE
	 * 
	 * @param DLmodel    The path to the newly created DLmodel.xmi
	 * @param MLmodel    The path to the source MLmodel.xmi
	 * @param properties The polystore.properties saved in the project folder
	 */
	private static void createMongoCommands(Path DLmodel, Path MLmodel, Properties properties) {
		String DLmodelContent = "";
		String MLmodelContent = "";
		try {
			DLmodelContent = String.join("", Files.readAllLines(DLmodel));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			MLmodelContent = String.join("", Files.readAllLines(MLmodel));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String DLUUID = UUID.randomUUID().toString();
		String MLUUID = UUID.randomUUID().toString();
		long unixTime = System.currentTimeMillis();
		String mongoDL = "db.models.insert({ \"_id\": \"" + DLUUID + "\", \"version\": 1, \"initializedDatabases\": "
				+ "false, \"initializedConnections\": true, \"contents\": \""
				+ DLmodelContent.replaceAll("\"", "\\\\\"") + "\", \"type\": \"DL\", \"dateReceived\": new Date("
				+ unixTime + "), " + "\"_class\": \"com.clms.typhonapi.models.Model\" });\r\n";
		String mongoML = "db.models.insert({ \"_id\": \"" + MLUUID + "\", \"version\": 1, \"initializedDatabases\": "
				+ "false, \"initializedConnections\": false, \"contents\": \""
				+ MLmodelContent.replaceAll("\"", "\\\\\"") + "\", \"type\": \"ML\", \"dateReceived\": new Date("
				+ unixTime + "), " + "\"_class\": \"com.clms.typhonapi.models.Model\" });\r\n";
		String folder = DLmodel.toString().replace(DLmodel.getFileName().toString(),
				properties.getProperty("db.volume"));
		String path = folder + File.separator + "addModels.js";
		if (!Files.exists(Paths.get(folder))) {
			try {
				Files.createDirectory(Paths.get(folder));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			Files.write(Paths.get(path), (mongoDL + mongoML).getBytes(), StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads the relative MLmodel path from the Import section of the DL model
	 * 
	 * @param model the DL model
	 * @return The relative MLmodel path as a String
	 */
	private static String getMLmodelPath(DeploymentModel model) {
		return model.getGuiMetaInformation().stream().filter(imortedModel -> Import.class.isInstance(imortedModel))
				.map(importedModel -> (Import) importedModel)
				.filter(info -> (info.getRelativePath().endsWith("xmi") || info.getRelativePath().endsWith("tmlx")))
				.map(info -> info.getRelativePath()).collect(Collectors.toList()).get(0);
	}

	/**
	 * Adds Key_Value named "hostname" with value container.name to each container
	 * of the polystore
	 * 
	 * @param model
	 * @return The Deployment model with every db container having the Key_Value
	 *         hostname
	 */
	private static DeploymentModel addHostnamesToModel(DeploymentModel model) {
		ArrayList<Container> containers = new ArrayList<>();
		model.getElements().stream().filter(element -> Platform.class.isInstance(element))
				.map(element -> (Platform) element)
				.forEach(platform -> platform.getClusters().forEach(cluster -> cluster.getApplications()
						.forEach(application -> containers.addAll(application.getContainers()))));
		for (Container container : containers) {
			Key_Values db_hostname = TyphonDLFactory.eINSTANCE.createKey_Values();
			db_hostname.setName("hostname");
			db_hostname.setValue(container.getName());
			container.getProperties().add(db_hostname);
		}
		return model;
	}

	/**
	 * Finds and adds the DB models from additional files if given as {@link Import}
	 * in the main model file
	 * 
	 * @param model The model to add the DBs to
	 * @return The model with the DB models added
	 */
	private static DeploymentModel addDBsToModel(DeploymentModel model) {
		Resource resource = model.eResource();
		URI uri = resource.getURI().trimSegments(1);

		model.getGuiMetaInformation().stream().filter(imortedModel -> Import.class.isInstance(imortedModel))
				.map(importedModel -> (Import) importedModel).filter(info -> info.getRelativePath().endsWith("tdl"))
				.forEach(info -> {
					model.getElements()
							.addAll(((DeploymentModel) resource.getResourceSet()
									.getResource(uri.appendSegment(info.getRelativePath()), true).getContents().get(0))
											.getElements());
				});
		;
		return model;
	}

	/**
	 * Creates a .xmi file representing the given DL model
	 * 
	 * @param DLmodelResource The given DL model
	 * @return
	 */
	private static URI saveModelAsXMI(Resource DLmodelResource) {
		XtextResourceSet resourceSet = (XtextResourceSet) DLmodelResource.getResourceSet();
		DeploymentModel DLmodel = (DeploymentModel) DLmodelResource.getContents().get(0);
		URI folder = DLmodelResource.getURI().trimFileExtension();
		// creates a xmi resource with the same name as the model in a folder named like
		// the model, so example/test.tdl -> example/test/test.xmi
		Resource xmiResource = resourceSet.createResource(folder.appendSegment(folder.lastSegment() + ".xmi"));

		// add model
		xmiResource.getContents().add(DLmodel);

		EcoreUtil.resolveAll(resourceSet);
		try {
			xmiResource.save(Options.getXMIoptions());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xmiResource.getURI();
	}

	/**
	 * Adds the three polystore containers and corresponding {@link Services} to the
	 * TyphonDL model
	 * 
	 * @param path       The path to the polystore.properties
	 * @param model      The given TyphonDL model
	 * @param properties The polystore.properties saved in the project folder
	 * @return The given TyphonDL model plus the polystore component
	 */
	private static DeploymentModel addPolystoreToModel(String path, DeploymentModel model, Properties properties) {

		List<DBType> dbTypes = model.getElements().stream().filter(element -> DBType.class.isInstance(element))
				.map(element -> (DBType) element).collect(Collectors.toList());
		// if Mongo is not allready a DBType, add Mongo
		DBType mongo = dbTypes.stream().filter(dbType -> dbType.getName().equalsIgnoreCase("mongo")).findFirst()
				.orElse(null);
		if (mongo == null) {
			mongo = TyphonDLFactory.eINSTANCE.createDBType();
			mongo.setName("Mongo");
			IMAGE mongoImage = TyphonDLFactory.eINSTANCE.createIMAGE();
			mongoImage.setValue("mongo:latest");
			mongo.setImage(mongoImage);
			model.getElements().add(mongo);
		}

		// will be removed -----------------------------------------v
		// get Application for polystore containers
		Application application = null;
		if (!properties.get("polystore.inApplication").equals("default")) {
			application = getApplication(model, (String) properties.get("polystore.inApplication"));
		} else if (properties.get("polystore.inApplication").equals("default") || application == null) {
			// get first application in first cluster on first platform
			application = ((Platform) model.getElements().stream().filter(element -> Platform.class.isInstance(element))
					.collect(Collectors.toList()).get(0)).getClusters().get(0).getApplications().get(0);
		}
		// will be removed -----------------------------------------^

		// get containertype
		ContainerType containerType = application.getContainers().get(0).getType();

		// polystore_db
		DB polystoredb = TyphonDLFactory.eINSTANCE.createDB();
		polystoredb.setName(properties.getProperty("db.name"));
		polystoredb.setType(mongo);
		Key_KeyValueList polystoredb_environment = TyphonDLFactory.eINSTANCE.createKey_KeyValueList();
		polystoredb_environment.setName("environment");
		Key_Values polystoredb_environment_1 = TyphonDLFactory.eINSTANCE.createKey_Values();
		polystoredb_environment_1.setName("MONGO_INITDB_ROOT_USERNAME");
		polystoredb_environment_1.setValue(properties.getProperty("db.environment.MONGO_INITDB_ROOT_USERNAME"));
		polystoredb_environment.getProperties().add(polystoredb_environment_1);
		Key_Values polystoredb_environment_2 = TyphonDLFactory.eINSTANCE.createKey_Values();
		polystoredb_environment_2.setName("MONGO_INITDB_ROOT_PASSWORD");
		polystoredb_environment_2.setValue(properties.getProperty("db.environment.MONGO_INITDB_ROOT_PASSWORD"));
		polystoredb_environment.getProperties().add(polystoredb_environment_2);
		Key_Values polystoredb_environment_3 = TyphonDLFactory.eINSTANCE.createKey_Values();
		polystoredb_environment_3.setName("MONGO_INITDB_DATABASE");
		polystoredb_environment_3.setValue(properties.getProperty("db.environment.MONGO_INITDB_DATABASE"));
		polystoredb_environment.getProperties().add(polystoredb_environment_3);
		polystoredb.getParameters().add(polystoredb_environment);
		model.getElements().add(polystoredb);
		Reference poystoredbReference = TyphonDLFactory.eINSTANCE.createReference();
		poystoredbReference.setReference(polystoredb);

		Container polystoredb_container = TyphonDLFactory.eINSTANCE.createContainer();
		polystoredb_container.setName(properties.getProperty("db.containername"));
		polystoredb_container.setType(containerType);
		polystoredb_container.setDeploys(poystoredbReference);
		Key_Values polystoredb_container_hostname = TyphonDLFactory.eINSTANCE.createKey_Values();
		polystoredb_container_hostname.setName("hostname");
		polystoredb_container_hostname.setValue(properties.getProperty("db.hostname"));
		polystoredb_container.getProperties().add(polystoredb_container_hostname);
		Key_ValueArray polystoredb_container_volume = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
		polystoredb_container_volume.setName("volumes");
		polystoredb_container_volume.getValues()
				.add("./" + properties.getProperty("db.volume") + "/:/docker-entrypoint-initdb.d");
		polystoredb_container.getProperties().add(polystoredb_container_volume);

		Key_Values polystoredb_container_ports1 = TyphonDLFactory.eINSTANCE.createKey_Values();
		polystoredb_container_ports1.setName("published");
		polystoredb_container_ports1.setValue(properties.getProperty("db.publishedPort"));
		Key_Values polystoredb_container_ports2 = TyphonDLFactory.eINSTANCE.createKey_Values();
		polystoredb_container_ports2.setName("target");
		polystoredb_container_ports2.setValue(properties.getProperty("db.port"));
		Ports polystoredb_container_ports = TyphonDLFactory.eINSTANCE.createPorts();
		polystoredb_container_ports.getKey_values().add(polystoredb_container_ports1);
		polystoredb_container_ports.getKey_values().add(polystoredb_container_ports2);
		polystoredb_container.setPorts(polystoredb_container_ports);

		Dependency polystoredb_dependency = TyphonDLFactory.eINSTANCE.createDependency();
		polystoredb_dependency.setReference(polystoredb_container);

		// polystore_api
		Software polystore_api;
		polystore_api = TyphonDLFactory.eINSTANCE.createSoftware();
		polystore_api.setName(properties.getProperty("api.name"));
		IMAGE polystore_api_image = TyphonDLFactory.eINSTANCE.createIMAGE();
		polystore_api_image.setValue(properties.getProperty("api.image"));
		polystore_api.setImage(polystore_api_image);
		model.getElements().add(polystore_api);
		Reference polystore_api_reference = TyphonDLFactory.eINSTANCE.createReference();
		polystore_api_reference.setReference(polystore_api);

		Container polystore_api_container = TyphonDLFactory.eINSTANCE.createContainer();
		polystore_api_container.setName(properties.getProperty("api.containername"));
		polystore_api_container.setType(containerType);
		polystore_api_container.setDeploys(polystore_api_reference);
		polystore_api_container.getDepends_on().add(polystoredb_dependency);
		Key_Values polystore_api_hostname = TyphonDLFactory.eINSTANCE.createKey_Values();
		polystore_api_hostname.setName("hostname");
		polystore_api_hostname.setValue(properties.getProperty("api.hostname"));
		polystore_api_container.getProperties().add(polystore_api_hostname);
		Key_Values polystore_api_container_ports1 = TyphonDLFactory.eINSTANCE.createKey_Values();
		polystore_api_container_ports1.setName("published");
		polystore_api_container_ports1.setValue(properties.getProperty("api.publishedPort"));
		Key_Values polystore_api_container_ports2 = TyphonDLFactory.eINSTANCE.createKey_Values();
		polystore_api_container_ports2.setName("target");
		polystore_api_container_ports2.setValue(properties.getProperty("api.port"));
		Ports polystore_api_container_ports = TyphonDLFactory.eINSTANCE.createPorts();
		polystore_api_container_ports.getKey_values().add(polystore_api_container_ports1);
		polystore_api_container_ports.getKey_values().add(polystore_api_container_ports2);
		polystore_api_container.setPorts(polystore_api_container_ports);

		Dependency polystore_api_dependency = TyphonDLFactory.eINSTANCE.createDependency();
		polystore_api_dependency.setReference(polystore_api_container);

		// polystore ui
		Software polystore_ui = TyphonDLFactory.eINSTANCE.createSoftware();
		polystore_ui.setName(properties.getProperty("ui.name"));
		IMAGE polystore_ui_image = TyphonDLFactory.eINSTANCE.createIMAGE();
		polystore_ui_image.setValue(properties.getProperty("ui.image"));
		polystore_ui.setImage(polystore_ui_image);
		Key_KeyValueList polystore_ui_environment = TyphonDLFactory.eINSTANCE.createKey_KeyValueList();
		polystore_ui_environment.setName("environment");
		Key_Values polystore_ui_environment1 = TyphonDLFactory.eINSTANCE.createKey_Values();
		polystore_ui_environment1.setName("API_PORT");
		polystore_ui_environment1.setValue(properties.getProperty("ui.environment.API_PORT"));
		polystore_ui_environment.getProperties().add(polystore_ui_environment1);
		Key_Values polystore_ui_environment2 = TyphonDLFactory.eINSTANCE.createKey_Values();
		polystore_ui_environment2.setName("API_HOST");
		polystore_ui_environment2.setValue(properties.getProperty("ui.environment.API_HOST"));
		polystore_ui_environment.getProperties().add(polystore_ui_environment2);
		polystore_ui.getParameters().add(polystore_ui_environment);
		model.getElements().add(polystore_ui);
		Reference polystore_ui_reference = TyphonDLFactory.eINSTANCE.createReference();
		polystore_ui_reference.setReference(polystore_ui);

		Container polystore_ui_container = TyphonDLFactory.eINSTANCE.createContainer();
		polystore_ui_container.setName(properties.getProperty("ui.containername"));
		polystore_ui_container.setType(containerType);
		polystore_ui_container.setDeploys(polystore_ui_reference);
		polystore_ui_container.getDepends_on().add(polystore_api_dependency);
		Key_Values polystore_ui_container_ports1 = TyphonDLFactory.eINSTANCE.createKey_Values();
		polystore_ui_container_ports1.setName("published");
		polystore_ui_container_ports1.setValue(properties.getProperty("ui.publishedPort"));
		Key_Values polystore_ui_container_ports2 = TyphonDLFactory.eINSTANCE.createKey_Values();
		polystore_ui_container_ports2.setName("target");
		polystore_ui_container_ports2.setValue(properties.getProperty("ui.port"));
		Ports polystore_ui_container_ports = TyphonDLFactory.eINSTANCE.createPorts();
		polystore_ui_container_ports.getKey_values().add(polystore_ui_container_ports1);
		polystore_ui_container_ports.getKey_values().add(polystore_ui_container_ports2);
		polystore_ui_container.setPorts(polystore_ui_container_ports);
		Key_Values polystore_ui_hostname = TyphonDLFactory.eINSTANCE.createKey_Values();
		polystore_ui_hostname.setName("hostname");
		polystore_ui_hostname.setValue(properties.getProperty("ui.hostname"));
		polystore_ui_container.getProperties().add(polystore_ui_hostname);
		Key_KeyValueList polystore_ui_container_build = TyphonDLFactory.eINSTANCE.createKey_KeyValueList();
		polystore_ui_container_build.setName("build");
		Key_Values polystore_ui_container_build_context = TyphonDLFactory.eINSTANCE.createKey_Values();
		polystore_ui_container_build_context.setName("context");
		polystore_ui_container_build_context.setValue("Typhon Service UI");
		polystore_ui_container_build.getProperties().add(polystore_ui_container_build_context);

		application.getContainers().add(polystoredb_container);
		application.getContainers().add(polystore_api_container);
		application.getContainers().add(polystore_ui_container);

		// QL server
		Software qlserver = TyphonDLFactory.eINSTANCE.createSoftware();
		qlserver.setName("QLServer");
		IMAGE qlserver_image = TyphonDLFactory.eINSTANCE.createIMAGE();
		qlserver_image.setValue("swatengineering/typhonql-server");
		qlserver.setImage(qlserver_image);
		Reference qlserver_reference = TyphonDLFactory.eINSTANCE.createReference();
		qlserver_reference.setReference(qlserver);
		model.getElements().add(qlserver);
		Container qlserver_container = TyphonDLFactory.eINSTANCE.createContainer();
		qlserver_container.setName("typhonql-server");
		Key_Values qlserver_container_hostname = TyphonDLFactory.eINSTANCE.createKey_Values();
		qlserver_container_hostname.setName("hostname");
		qlserver_container_hostname.setValue("typhonql-server");
		qlserver_container.getProperties().add(qlserver_container_hostname);
		qlserver_container.setDeploys(qlserver_reference);
		application.getContainers().add(qlserver_container);

		// Analytics, see https://github.com/typhon-project/typhondl/issues/6
		if (properties.get("polystore.useAnalytics").equals("true")) {
			String zookeeperPort = properties.getProperty("analytics.zookeeper.publishedPort");
			String kafkaPort = properties.getProperty("analytics.kafka.publishedPort");
			String[] kafkaListeners = properties.getProperty("analytics.kafka.listeners").split("\\s*,\\s*");
			String kafkaListenerName = properties.getProperty("analytics.kafka.listenerName");
			String kafkaListenersString = "";
			String kafkaAdvertisedListenerString = "";
			for (int i = 0; i < kafkaListeners.length; i++) {
				kafkaListenersString += kafkaListenerName + "://:" + kafkaListeners[i] + ", ";
				kafkaAdvertisedListenerString += kafkaListenerName + "://kafka:" + kafkaListeners[i] + ", ";
			}
			kafkaListenersString += "PLAINTEXT_HOST://:" + kafkaPort;
			kafkaAdvertisedListenerString += "PLAINTEXT_HOST://localhost:" + kafkaPort;

			Software zookeeper = TyphonDLFactory.eINSTANCE.createSoftware();
			zookeeper.setName("zookeeper");
			IMAGE zookeeper_image = TyphonDLFactory.eINSTANCE.createIMAGE();
			zookeeper_image.setValue(properties.getProperty("analytics.zookeeper.image"));
			zookeeper.setImage(zookeeper_image);
			model.getElements().add(zookeeper);
			Reference zookeeper_reference = TyphonDLFactory.eINSTANCE.createReference();
			zookeeper_reference.setReference(zookeeper);

			Container zookeeper_container = TyphonDLFactory.eINSTANCE.createContainer();
			zookeeper_container.setName(properties.getProperty("analytics.zookeeper.containername"));
			zookeeper_container.setType(containerType);
			zookeeper_container.setDeploys(zookeeper_reference);
			Key_Values zookeeper_container_ports1 = TyphonDLFactory.eINSTANCE.createKey_Values();
			zookeeper_container_ports1.setName("published");
			zookeeper_container_ports1.setValue(zookeeperPort);
			Key_Values zookeeper_container_ports2 = TyphonDLFactory.eINSTANCE.createKey_Values();
			zookeeper_container_ports2.setName("target");
			zookeeper_container_ports2.setValue(properties.getProperty("analytics.zookeeper.port"));
			Ports zookeeper_container_port = TyphonDLFactory.eINSTANCE.createPorts();
			zookeeper_container_port.getKey_values().add(zookeeper_container_ports1);
			zookeeper_container_port.getKey_values().add(zookeeper_container_ports2);
			zookeeper_container.setPorts(zookeeper_container_port);

			Dependency zookeeper_dependency = TyphonDLFactory.eINSTANCE.createDependency();
			zookeeper_dependency.setReference(zookeeper_container);

			application.getContainers().add(zookeeper_container);

			Container kafka_container = TyphonDLFactory.eINSTANCE.createContainer();
			kafka_container.setName(properties.getProperty("analytics.kafka.containername"));
			kafka_container.setType(containerType);
			Key_Values kafka_container_build = TyphonDLFactory.eINSTANCE.createKey_Values();
			kafka_container_build.setName("build");
			kafka_container_build.setValue(".");
			kafka_container.getProperties().add(kafka_container_build);
			kafka_container.getDepends_on().add(zookeeper_dependency);
			Key_Values kafka_container_ports1 = TyphonDLFactory.eINSTANCE.createKey_Values();
			kafka_container_ports1.setName("published");
			kafka_container_ports1.setValue(kafkaPort);
			Key_Values kafka_container_ports2 = TyphonDLFactory.eINSTANCE.createKey_Values();
			kafka_container_ports2.setName("target");
			kafka_container_ports2.setValue(properties.getProperty("analytics.kafka.port"));
			Ports kafka_container_ports = TyphonDLFactory.eINSTANCE.createPorts();
			kafka_container_ports.getKey_values().add(kafka_container_ports1);
			kafka_container_ports.getKey_values().add(kafka_container_ports2);
			kafka_container.setPorts(kafka_container_ports);
			Key_ValueArray kafka_container_volumes = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
			kafka_container_volumes.setName("volumes");
			kafka_container_volumes.getValues().add("/var/run/docker.sock:/var/run/docker.sock");
			kafka_container.getProperties().add(kafka_container_volumes);
			Key_KeyValueList kafka_environment = TyphonDLFactory.eINSTANCE.createKey_KeyValueList();
			kafka_environment.setName("environment");
			Key_Values KAFKA_ZOOKEEPER_CONNECT = TyphonDLFactory.eINSTANCE.createKey_Values();
			KAFKA_ZOOKEEPER_CONNECT.setName("KAFKA_ZOOKEEPER_CONNECT");
			KAFKA_ZOOKEEPER_CONNECT.setValue("zookeeper:" + zookeeperPort);
			kafka_environment.getProperties().add(KAFKA_ZOOKEEPER_CONNECT);
			Key_Values KAFKA_ADVERTISED_HOST_NAME = TyphonDLFactory.eINSTANCE.createKey_Values();
			KAFKA_ADVERTISED_HOST_NAME.setName("KAFKA_ADVERTISED_HOST_NAME");
			KAFKA_ADVERTISED_HOST_NAME.setValue("kafka");
			kafka_environment.getProperties().add(KAFKA_ADVERTISED_HOST_NAME);
			Key_Values KAFKA_LISTENERS = TyphonDLFactory.eINSTANCE.createKey_Values();
			KAFKA_LISTENERS.setName("KAFKA_LISTENERS");
			KAFKA_LISTENERS.setValue(kafkaListenersString);
			kafka_environment.getProperties().add(KAFKA_LISTENERS);
			Key_Values KAFKA_LISTENER_SECURITY_PROTOCOL_MAP = TyphonDLFactory.eINSTANCE.createKey_Values();
			KAFKA_LISTENER_SECURITY_PROTOCOL_MAP.setName("KAFKA_LISTENER_SECURITY_PROTOCOL_MAP");
			KAFKA_LISTENER_SECURITY_PROTOCOL_MAP.setValue(kafkaListenerName + ":PLAINTEXT, PLAINTEXT_HOST:PLAINTEXT");
			kafka_environment.getProperties().add(KAFKA_LISTENER_SECURITY_PROTOCOL_MAP);
			Key_Values KAFKA_INTER_BROKER_LISTENER_NAME = TyphonDLFactory.eINSTANCE.createKey_Values();
			KAFKA_INTER_BROKER_LISTENER_NAME.setName("KAFKA_INTER_BROKER_LISTENER_NAME");
			KAFKA_INTER_BROKER_LISTENER_NAME.setValue(kafkaListenerName);
			kafka_environment.getProperties().add(KAFKA_INTER_BROKER_LISTENER_NAME);
			Key_Values KAFKA_ADVERTISED_LISTENERS = TyphonDLFactory.eINSTANCE.createKey_Values();
			KAFKA_ADVERTISED_LISTENERS.setName("KAFKA_ADVERTISED_LISTENERS");
			KAFKA_ADVERTISED_LISTENERS.setValue(kafkaAdvertisedListenerString);
			kafka_environment.getProperties().add(KAFKA_ADVERTISED_LISTENERS);
			Key_Values KAFKA_AUTO_CREATE_TOPICS_ENABLE = TyphonDLFactory.eINSTANCE.createKey_Values();
			KAFKA_AUTO_CREATE_TOPICS_ENABLE.setName("KAFKA_AUTO_CREATE_TOPICS_ENABLE");
			KAFKA_AUTO_CREATE_TOPICS_ENABLE.setValue("\"true\"");
			kafka_environment.getProperties().add(KAFKA_AUTO_CREATE_TOPICS_ENABLE);
			kafka_container.getProperties().add(kafka_environment);

			application.getContainers().add(kafka_container);
		}
		return model;
	}

	/**
	 * Gets application TODO remove
	 * 
	 * @param model   The TyphonDL model
	 * @param appName The name of the Application to find
	 * @return The Application to put the polystore in
	 */
	private static Application getApplication(DeploymentModel model, String appName) {
		List<Application> list = new ArrayList<Application>();
		model.getElements().stream().filter(element -> Platform.class.isInstance(element))
				.map(element -> (Platform) element)
				.forEach(platform -> platform.getClusters().forEach(cluster -> cluster.getApplications().stream()
						.filter(app -> app.getName().equals(appName)).map(app -> list.add(app)))); // TODO not nice?
		return (list.size() == 1) ? list.get(0) : null;
	}
}

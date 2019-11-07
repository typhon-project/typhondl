package de.atb.typhondl.acceleo.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import de.atb.typhondl.xtext.typhonDL.Cluster;
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
import de.atb.typhondl.xtext.typhonDL.MetaModel;
import de.atb.typhondl.xtext.typhonDL.Platform;
import de.atb.typhondl.xtext.typhonDL.Reference;
import de.atb.typhondl.xtext.typhonDL.Software;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;

public class Services {

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

	private static String getResult(File folder) {
		if (!folder.exists()) {
			return "Something went wrong";
		} else {
			for (File subFile : folder.listFiles()) {
				if (subFile.isDirectory()) {
					for (File subSubFile : subFile.listFiles()) {
						if (subSubFile.getName().equals("docker-compose.yml")) { // TODO horrible
							return "Docker Compose File generated";
						}
					}
				}
				if (subFile.getName().endsWith("xmi")) {
					return "Only the model to export was generated";
				}
			}
		}
		return null;
	}

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
		URI modelURI = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
		Resource DLmodelResource = resourceSet.getResource(modelURI, true);
		DeploymentModel model = (DeploymentModel) DLmodelResource.getContents().get(0);
		String location = file.getLocation().toString();
		String path = location.substring(0, location.lastIndexOf('/') + 1) + "polystore.properties";
		model = addDBsToModel(model);
		model = addPolystoreToModel(path, model);
		saveModelAsXMI(DLmodelResource);
		return model;
	}

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

	private static void saveModelAsXMI(Resource DLmodelResource) {
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
	}

	private static DeploymentModel addPolystoreToModel(String path, DeploymentModel model) {
		Properties properties = new Properties();
		try {
			InputStream input = new FileInputStream(path);
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();// TODO popup if nonexistent
		}
		// TODO analytics

		List<DBType> dbTypes = model.getElements().stream().filter(element -> DBType.class.isInstance(element))
				.map(element -> (DBType) element).collect(Collectors.toList());
		DBType mongo = null;
		for (DBType dbType : dbTypes) {
			if (dbType.getName().equals("mongo")) {
				mongo = dbType;
			}
		}
		if (mongo == null) {
			mongo = TyphonDLFactory.eINSTANCE.createDBType();
			mongo.setName("mongo");
			IMAGE mongoImage = TyphonDLFactory.eINSTANCE.createIMAGE();
			mongoImage.setValue("mongo:latest");
			mongo.setImage(mongoImage);
			model.getElements().add(mongo);
		}
		
		// get Application for polystore containers
		Application application = null;
		if (!properties.get("polystore.inApplication").equals("default")) {
			application = getApplication(model, (String) properties.get("polystore.inApplication"));
		} else if (properties.get("polystore.inApplication").equals("default") || application == null) {
			// get first application in first cluster on first platform
			application = ((Platform) model.getElements().stream().filter(element -> Platform.class.isInstance(element))
					.collect(Collectors.toList()).get(0)).getClusters().get(0).getApplications().get(0);
		}

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
		polystoredb_environment.getKey_Values().add(polystoredb_environment_1);
		Key_Values polystoredb_environment_2 = TyphonDLFactory.eINSTANCE.createKey_Values();
		polystoredb_environment_2.setName("MONGO_INITDB_ROOT_PASSWORD");
		polystoredb_environment_2.setValue(properties.getProperty("db.environment.MONGO_INITDB_ROOT_PASSWORD"));
		polystoredb_environment.getKey_Values().add(polystoredb_environment_2);
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
		Key_ValueArray polystoredb_container_ports = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
		polystoredb_container_ports.setName("exposedPort");
		polystoredb_container_ports.getValues().add(properties.getProperty("db.port"));
		polystoredb_container.getProperties().add(polystoredb_container_ports);

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
		Key_Values polystore_api_restart = TyphonDLFactory.eINSTANCE.createKey_Values();
		polystore_api_restart.setName("restart");
		polystore_api_restart.setValue(properties.getProperty("api.restart"));
		polystore_api_container.getProperties().add(polystore_api_restart);
		Key_Values polystore_api_hostname = TyphonDLFactory.eINSTANCE.createKey_Values();
		polystore_api_hostname.setName("hostname");
		polystore_api_hostname.setValue(properties.getProperty("api.hostname"));
		polystore_api_container.getProperties().add(polystore_api_hostname);
		Key_ValueArray polystore_api_container_ports = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
		polystore_api_container_ports.setName("exposedPort");
		polystore_api_container_ports.getValues().add(properties.getProperty(properties.getProperty("api.port")));
		polystore_api_container.getProperties().add(polystore_api_container_ports);

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
		polystore_ui_environment.getKey_Values().add(polystore_ui_environment1);
		Key_Values polystore_ui_environment2 = TyphonDLFactory.eINSTANCE.createKey_Values();
		polystore_ui_environment2.setName("API_HOST");
		polystore_ui_environment2.setValue(properties.getProperty("ui.environment.API_HOST"));
		polystore_ui_environment.getKey_Values().add(polystore_ui_environment2);
		polystore_ui.getParameters().add(polystore_ui_environment);
		model.getElements().add(polystore_ui);
		Reference polystore_ui_reference = TyphonDLFactory.eINSTANCE.createReference();
		polystore_ui_reference.setReference(polystore_ui);

		Container polystore_ui_container = TyphonDLFactory.eINSTANCE.createContainer();
		polystore_ui_container.setName(properties.getProperty("ui.containername"));
		polystore_ui_container.setType(containerType);
		polystore_ui_container.setDeploys(polystore_ui_reference);
		polystore_ui_container.getDepends_on().add(polystore_api_dependency);
		Key_ValueArray polystore_ui_container_ports = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
		polystore_ui_container_ports.setName("exposedPort");
		polystore_ui_container_ports.getValues().add(properties.getProperty("ui.port"));
		polystore_ui_container.getProperties().add(polystore_ui_container_ports);
		Key_Values polystore_ui_restart = TyphonDLFactory.eINSTANCE.createKey_Values();
		polystore_ui_restart.setName("restart");
		polystore_ui_restart.setValue(properties.getProperty("ui.restart"));
		polystore_ui_container.getProperties().add(polystore_ui_restart);
		Key_Values polystore_ui_hostname = TyphonDLFactory.eINSTANCE.createKey_Values();
		polystore_ui_hostname.setName("hostname");
		polystore_ui_hostname.setValue(properties.getProperty("ui.hostname"));
		polystore_ui_container.getProperties().add(polystore_ui_hostname);
		Key_KeyValueList polystore_ui_container_build = TyphonDLFactory.eINSTANCE.createKey_KeyValueList();
		polystore_ui_container_build.setName("build");
		Key_Values polystore_ui_container_build_context = TyphonDLFactory.eINSTANCE.createKey_Values();
		polystore_ui_container_build_context.setName("context");
		polystore_ui_container_build_context.setValue("Typhon Service UI");
		polystore_ui_container_build.getKey_Values().add(polystore_ui_container_build_context);

		application.getContainers().add(polystoredb_container);
		application.getContainers().add(polystore_api_container);
		application.getContainers().add(polystore_ui_container);
		return model;
	}

	private static Application getApplication(DeploymentModel model, String appName) {
		List<Application> list = new ArrayList<Application>();
		model.getElements().stream().filter(element -> Platform.class.isInstance(element))
				.map(element -> (Platform) element)
				.forEach(platform -> platform.getClusters().forEach(cluster -> cluster.getApplications().stream()
						.filter(app -> app.getName().equals(appName)).map(app -> list.add(app)))); //TODO not nice?
		return (list.size() == 1)? list.get(0) : null;
	}
}

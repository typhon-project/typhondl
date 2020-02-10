package de.atb.typhondl.xtext.ui.creationWizard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
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
import de.atb.typhondl.xtext.typhonDL.ClusterType;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.ContainerType;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DBType;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Import;
import de.atb.typhondl.xtext.typhonDL.Key_Values;
import de.atb.typhondl.xtext.typhonDL.Platform;
import de.atb.typhondl.xtext.typhonDL.PlatformType;
import de.atb.typhondl.xtext.typhonDL.Ports;
import de.atb.typhondl.xtext.typhonDL.Reference;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.activator.Activator;
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

	/*
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

	public IFile createDLmodel(ArrayList<DB> dbs, int chosenTemplate, Properties properties) {

		// create main model
		DeploymentModel DLmodel = TyphonDLFactory.eINSTANCE.createDeploymentModel();
		// add reference to ML model
		Import MLmodelImport = TyphonDLFactory.eINSTANCE.createImport();
		MLmodelImport.setRelativePath(this.MLmodel.getName());
		DLmodel.getGuiMetaInformation().add(MLmodelImport);

		// Add selected container type (chosen template in wizard)
		ContainerType containerType = TyphonDLFactory.eINSTANCE.createContainerType();
		containerType.setName(SupportedTechnologies.values()[chosenTemplate].getContainerType());
		DLmodel.getElements().add(containerType);

		// Add selected cluster type (chosen template in wizard)
		ClusterType clusterType = TyphonDLFactory.eINSTANCE.createClusterType();
		clusterType.setName(SupportedTechnologies.values()[chosenTemplate].getClusterType());
		DLmodel.getElements().add(clusterType);

		// create platform type from API HOST
		PlatformType platformType = TyphonDLFactory.eINSTANCE.createPlatformType();
		switch (SupportedTechnologies.values()[chosenTemplate].getClusterType()) {
		case "DockerCompose":
			platformType.setName("localhost");
			break;
		case "Kubernetes":
			platformType.setName("minikube");
			break;
		default:
			platformType.setName("localhost");
			break;
		}
//		platformType.setName("localhost");
//		platformType.setName(properties.getProperty("ui.environment.API_HOST"));
		DLmodel.getElements().add(platformType);

		ArrayList<DBType> dbTypes = new ArrayList<DBType>();
		// create import for each db, use given db or load from file
		for (DB db : dbs) {
			Import importedDB = TyphonDLFactory.eINSTANCE.createImport();
			DeploymentModel dbModel;
			if (db.getType() == null) { // use existing .tdl file
				String path = db.getName() + ".tdl";
				URI dbURI = URI.createPlatformResourceURI(this.folder.append(path).toString(), true);
				dbModel = (DeploymentModel) resourceSet.getResource(dbURI, true).getContents().get(0);
				addModelToDB(db, getDB(dbModel));
				importedDB.setRelativePath(path);
			} else {
				importedDB.setRelativePath(db.getName() + ".tdl");
			}
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

		for (DB db : dbs) { // types need to be the same instance
			for (DBType dbtype : dbTypes) {
				if (dbtype.getName().equals(db.getType().getName())) {
					db.setType(dbtype);
				}
			}
		}
		DeploymentModel dbTypesModel = TyphonDLFactory.eINSTANCE.createDeploymentModel();
		for (DBType dbType : dbTypes) {
			dbTypesModel.getElements().add(dbType);
		}
		save(dbTypesModel, "dbTypes.tdl");
		Import dbTypesImport = TyphonDLFactory.eINSTANCE.createImport();
		dbTypesImport.setRelativePath("dbTypes.tdl");
		DLmodel.getGuiMetaInformation().add(dbTypesImport);
		for (DB db : dbs) {
			DeploymentModel dbModel = TyphonDLFactory.eINSTANCE.createDeploymentModel();
			dbModel.getElements().add(db);
			save(dbModel, db.getName() + ".tdl");
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
		cluster.setType(clusterType);
		deployment.getClusters().add(cluster);

		Application application = TyphonDLFactory.eINSTANCE.createApplication();
		application.setName("Polystore");
		cluster.getApplications().add(application);

		for (DB db : dbs) {
			Container container = TyphonDLFactory.eINSTANCE.createContainer();
			container.setName(db.getName());
			container.setType(containerType);
			Reference reference = TyphonDLFactory.eINSTANCE.createReference();
			reference.setReference(db);
			container.setDeploys(reference);

			Ports db_ports = TyphonDLFactory.eINSTANCE.createPorts();
			Key_Values db_port = TyphonDLFactory.eINSTANCE.createKey_Values();
			db_port.setName("target");
			db_port.setValue(getStandardPort(db.getType().getName())); // TODO can be removed later
			Key_Values publishedDB_port = TyphonDLFactory.eINSTANCE.createKey_Values();
			publishedDB_port.setName("published");
			// TODO can be removed later
			publishedDB_port.setValue(getStandardPublishedPort(db.getType().getName(), clusterType));
			db_ports.getKey_values().add(db_port);
			db_ports.getKey_values().add(publishedDB_port);
			container.setPorts(db_ports);

			Key_Values hostname = TyphonDLFactory.eINSTANCE.createKey_Values();
			hostname.setName("hostname");
			hostname.setValue(properties.getProperty("ui.environment.API_HOST"));
			container.getProperties().add(hostname);

			application.getContainers().add(container);
		}

		/*
		 * save main model file
		 */
		String filename = DLmodelName + ".tdl";
		save(DLmodel, filename);
		URI DLmodelURI = URI.createPlatformResourceURI(this.folder.append(filename).toString(), true);
		// return main model file to be opened in editor
		return ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(DLmodelURI.toPlatformString(true)));
	}

	private void addModelToDB(DB db, DB input) {
		db.setType(input.getType());
		db.getParameters().addAll(input.getParameters());
	}

	// TODO This should not be needed, since the databases should only be reachable
	// inside the same network/cluster
	private String getStandardPort(String name) {
		switch (name.toLowerCase()) {
		case "mariadb":
			return "3306";
		case "mysql":
			return "3306";
		case "mongo":
			return "27017";
		default:
			return "0:0";
		}
	}

	// TODO This should not be needed, since the databases should only be reachable
	// inside the same network/cluster
	private String getStandardPublishedPort(String name, ClusterType type) {
		if (type.getName().equals("Kubernetes")) {
			return "" + (31000 + ThreadLocalRandom.current().nextInt(1, 100));
		} else {
			switch (name.toLowerCase()) {
			case "mariadb":
				return "3306";
			case "mysql":
				return "3306";
			case "mongo":
				return "27018"; // 27017 is occupied by polystoredb
			default:
				return "0:0";
			}
		}

	}

	private void save(DeploymentModel model, String filename) {
		URI uri = URI.createPlatformResourceURI(this.folder.append(filename).toString(), true);
		if (checkExist(uri)) {
			try {
				resourceSet.getResource(uri, true).delete(Collections.EMPTY_MAP);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Resource resource = resourceSet.createResource(uri);
		resource.getContents().add(model);
		try {
			resource.save(SavingOptions.getTDLoptions());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean checkExist(URI servicesURI) {
		return resourceSet.getResource(servicesURI, false) != null;
	}

	private DB getDB(DeploymentModel model) {
		ArrayList<DB> dbs = new ArrayList<DB>();
		dbs.addAll(model.getElements().stream().filter(element -> DB.class.isInstance(element))
				.map(element -> (DB) element).collect(Collectors.toList()));
		return dbs.get(0);
	}
}

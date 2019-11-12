package de.atb.typhondl.xtext.ui.creationWizard;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.eclipse.core.internal.resources.File;
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
import de.atb.typhondl.xtext.typhonDL.IMAGE;
import de.atb.typhondl.xtext.typhonDL.Import;
import de.atb.typhondl.xtext.typhonDL.Key_KeyValueList;
import de.atb.typhondl.xtext.typhonDL.Key_Values;
import de.atb.typhondl.xtext.typhonDL.Platform;
import de.atb.typhondl.xtext.typhonDL.PlatformType;
import de.atb.typhondl.xtext.typhonDL.Ports;
import de.atb.typhondl.xtext.typhonDL.Reference;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.activator.Activator;
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

	public IFile createDLmodel(ArrayList<Database> databases, int chosenTemplate) {

		// create main model
		DeploymentModel DLmodel = TyphonDLFactory.eINSTANCE.createDeploymentModel();
		// add reference to ML model
		Import MLmodelImport = TyphonDLFactory.eINSTANCE.createImport();
		MLmodelImport.setRelativePath(this.MLmodel.getName());
		DLmodel.getGuiMetaInformation().add(MLmodelImport);

		// create dummy platform type
		PlatformType platformType = TyphonDLFactory.eINSTANCE.createPlatformType();
		platformType.setName("local");
		DLmodel.getElements().add(platformType);

		// Add selected container type (chosen template in wizard)
		ContainerType containerType = TyphonDLFactory.eINSTANCE.createContainerType();
		containerType.setName(SupportedTechnologies.values()[chosenTemplate].getContainerType());
		DLmodel.getElements().add(containerType);

		// Add selected cluster type (chosen template in wizard)
		ClusterType clusterType = TyphonDLFactory.eINSTANCE.createClusterType();
		clusterType.setName(SupportedTechnologies.values()[chosenTemplate].getClusterType());
		DLmodel.getElements().add(clusterType);

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
				Properties dbProperties = new Properties();
				InputStream inStream = ModelCreator.class.getClassLoader()
						.getResourceAsStream("de/atb/typhondl/xtext/ui/properties/"
								+ database.getDbms().getName().toLowerCase() + ".properties");
				try {
					dbProperties.load(inStream);
				} catch (IOException e) {
					e.printStackTrace();
				}
				db = TyphonDLFactory.eINSTANCE.createDB();
				db.setName(database.getName());
				DBType dbType = database.getDbms();
				IMAGE image = TyphonDLFactory.eINSTANCE.createIMAGE();
				image.setValue(dbProperties.getProperty("image"));
				dbType.setImage(image);
				db.setType(dbType);
				importedDB.setRelativePath(db.getName() + ".tdl");
				// add environment:
				List<String> environmentKeys = dbProperties.keySet().stream().map(key -> (String) key)
						.filter(key -> key.contains("environment")).collect(Collectors.toList());
				if (!environmentKeys.isEmpty()) {
					Key_KeyValueList environment = TyphonDLFactory.eINSTANCE.createKey_KeyValueList();
					environment.setName("environment");
					environmentKeys.forEach(key -> {
						Key_Values key_value = TyphonDLFactory.eINSTANCE.createKey_Values();
						key_value.setName(key.substring(key.lastIndexOf('.')+1));
						key_value.setValue((String) dbProperties.get(key));
						environment.getKey_Values().add(key_value);
					});
					db.getParameters().add(environment);
				}
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
			db_port.setValue(getStandardPort(db.getType().getName())); //TODO can be removed later
			Key_Values publishedDB_port = TyphonDLFactory.eINSTANCE.createKey_Values();
			publishedDB_port.setName("published");
			publishedDB_port.setValue(getStandardPublishedPort(db.getType().getName())); //TODO can be removed later
			db_ports.getKey_values().add(db_port);
			db_ports.getKey_values().add(publishedDB_port);
			container.setPorts(db_ports);
			
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
	
	// TODO This should not be needed, since the databases should only be reachable inside the same network/cluster
	private String getStandardPort(String name) {
		switch (name) {
		case "mariadb":
			return "3306";
		case "mysql":
			return "3306";
		case "mongo":
			return "27017"; // 27017 is occupied by polystoredb
		default:
			return "0:0";
		}
	}
	// TODO This should not be needed, since the databases should only be reachable inside the same network/cluster
		private String getStandardPublishedPort(String name) {
			switch (name) {
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
	
	
	private void save(DeploymentModel model, String filename) {
		URI uri = URI.createPlatformResourceURI(this.folder.append(filename).toString(), true);
		if (checkExist(uri)) {
			try {
				resourceSet.getResource(uri, true).delete(Collections.EMPTY_MAP);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File file;
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

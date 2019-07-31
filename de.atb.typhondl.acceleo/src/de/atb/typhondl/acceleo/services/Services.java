package de.atb.typhondl.acceleo.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import de.atb.typhondl.xtext.typhonDL.Cluster;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.Deployment;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Import;

public class Services {

	public static void generateDeployment(IFile file, XtextLiveScopeResourceSetProvider provider) {
		try {
			String outputFolder = file.getLocation().toOSString().replace("." + file.getFileExtension(), "");
			DeploymentModel model = loadXtextModel(file, provider);
			new Generate(model, new File(outputFolder), new ArrayList<String>()).doGenerate(new BasicMonitor());
		} catch (IOException e) {
			e.printStackTrace();
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
		saveModelAsXMI(DLmodelResource);
		return model;
	}

	private static void saveModelAsXMI(Resource DLmodelResource) {
		XtextResourceSet resourceSet = (XtextResourceSet) DLmodelResource.getResourceSet();
		DeploymentModel DLmodel = (DeploymentModel) DLmodelResource.getContents().get(0);
		URI folder = DLmodelResource.getURI().trimFileExtension();
		// creates a xmi resource with the same name as the model in a folder named like
		// the model
		// so example/test.tdl -> example/test/test.xmi
		Resource xmiResource = resourceSet.createResource(folder.appendSegment(folder.lastSegment() + ".xmi"));
		// add main model
		xmiResource.getContents().add(DLmodel);
		// add polystore_ui, polystore_api and polystoredb to xmi model
		URI uri = DLmodelResource.getURI().trimSegments(1);
		xmiResource.getContents()
				.add(resourceSet.getResource(uri.appendSegment("polystore_ui.tdl"), true).getContents().get(0));
		xmiResource.getContents()
				.add(resourceSet.getResource(uri.appendSegment("polystore_api.tdl"), true).getContents().get(0));
		xmiResource.getContents()
				.add(resourceSet.getResource(uri.appendSegment("polystoredb.tdl"), true).getContents().get(0));
		// add DBs
		for (String dbFileName : getDBs(DLmodel)) {
			xmiResource.getContents()
					.add(resourceSet.getResource(uri.appendSegment(dbFileName), true).getContents().get(0));
		}
		// add analytics if used
		if (analyticsIsUsed(DLmodel)) {
			xmiResource.getContents()
					.add(resourceSet.getResource(uri.appendSegment("zookeeper.tdl"), true).getContents().get(0));
		}
		EcoreUtil.resolveAll(resourceSet);
		try {
			xmiResource.save(Options.getXMIoptions());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean analyticsIsUsed(DeploymentModel DLmodel) {
		Deployment deployment = DLmodel.getElements().stream().filter(element -> Deployment.class.isInstance(element))
				.map(element -> (Deployment) element).collect(Collectors.toList()).get(0);
		for (Cluster cluster : deployment.getClusters()) {
			for (Application application : cluster.getApplications()) {
				for (Container container : application.getContainers()) {
					if (container.getName().equals("zookeeper")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static ArrayList<String> getDBs(DeploymentModel DLmodel) {
		ArrayList<String> dbs = new ArrayList<String>();
		DLmodel.getGuiMetaInformation().stream().filter(metaModel -> Import.class.isInstance(metaModel))
				.map(metaModel -> (Import) metaModel).filter(info -> info.getRelativePath().endsWith(".tdl"))
				.forEach(info -> dbs.add(info.getRelativePath()));
		return dbs;
	}
}

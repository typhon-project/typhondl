package de.atb.typhondl.xtext.ui.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;

import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.Cluster;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.Deployment;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Import;
import de.atb.typhondl.xtext.ui.activator.Activator;
import de.atb.typhondl.xtext.ui.editor.pages.DBOverview;

public class TyphonEditorDialog extends PreferenceDialog {

	// path to model resource
	private IPath path;

	public TyphonEditorDialog(Shell parentShell, IPath path) {
		this(parentShell, createManager(parentShell, path));
		this.path = path;
	}

	public TyphonEditorDialog(Shell parentShell, PreferenceManager manager) {
		super(parentShell, manager);
	}

	private static PreferenceManager createManager(Shell parentShell, IPath path) {
		Injector injector = Activator.getInstance().getInjector(Activator.DE_ATB_TYPHONDL_XTEXT_TYPHONDL);
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);

		URI modelURI = URI.createFileURI(path.toString());

		Resource resource = resourceSet.getResource(modelURI, true);

		EcoreUtil.resolveAll(resource); // doesn't change anything
		/*
		 * DeploymentModel model only includes the model parts written in the
		 * File(modelUri), not any parts that are saved in different files like a
		 * database.tdl
		 */
		DeploymentModel model = (DeploymentModel) resource.getContents().get(0);
		PreferenceManager preferenceManager = createPages(model, resource);
		return preferenceManager;
	}

	private static PreferenceManager createPages(DeploymentModel model, Resource resource) {

		PreferenceManager preferenceManager = new PreferenceManager();

		ArrayList<DB> dbs = getDBs(model, resource);
		PreferenceNode databaseNode = PreferenceNodeFactory.createPreferenceNode(new DBOverview(dbs));
		preferenceManager.addToRoot(databaseNode);
		for (DB db : dbs) {
			databaseNode.add(PreferenceNodeFactory.createPreferenceNode(EditorPageFactory.createEditorPage(db)));
		}

		PreferenceNode deploymentNode = PreferenceNodeFactory
				.createPreferenceNode(EditorPageFactory.createEditorPage(getDeployment(model)));

		for (Cluster cluster : getClusters(model)) {
			PreferenceNode clusterNode = PreferenceNodeFactory
					.createPreferenceNode(EditorPageFactory.createEditorPage(cluster));
			deploymentNode.add(clusterNode);
			for (Application application : cluster.getApplications()) {
				PreferenceNode appNode = PreferenceNodeFactory
						.createPreferenceNode(EditorPageFactory.createEditorPage(application));
				clusterNode.add(appNode);
				for (Container container : application.getContainers()) {
					appNode.add(
							PreferenceNodeFactory.createPreferenceNode(EditorPageFactory.createEditorPage(container)));
				}
			}
		}

		preferenceManager.addToRoot(deploymentNode);
		return preferenceManager;
	}

	private static EList<Cluster> getClusters(DeploymentModel model) {
		return getDeployment(model).getClusters();
	}

	private static Deployment getDeployment(DeploymentModel model) {
		return model.getElements().stream().filter(element -> Deployment.class.isInstance(element)).findFirst()
				.map(deployment -> (Deployment) deployment).orElse(null);
	}

	private static ArrayList<DB> getDBs(DeploymentModel model, Resource resource) {
		ArrayList<DB> dbs = new ArrayList<DB>();
		List<Import> importedInfos = model.getGuiMetaInformation().stream()
				.filter(metaModel -> Import.class.isInstance(metaModel)).map(metaModel -> (Import) metaModel)
				.collect(Collectors.toList());
		importedInfos.stream().filter(info -> info.getRelativePath().endsWith(".tdl")).forEach(importedInfo -> {
			Resource dbResource = openImport(resource, importedInfo.getRelativePath()); // otherwise DB is null
			DeploymentModel dbModel = (DeploymentModel) dbResource.getContents().get(0);
			List<DB> dbList = dbModel.getElements().stream().filter(element -> DB.class.isInstance(element))
					.map(element -> (DB) element).collect(Collectors.toList());
			dbs.addAll(dbList);
		});
		return dbs;
	}

	/**
	 * see http://www.cs.kun.nl/J.Hooman/DSL/AdvancedXtextManual.pdf
	 * 
	 * @param currentResource
	 * @param importedURIAsString
	 * @return
	 */
	public static Resource openImport(final Resource currentResource, final String importedURIAsString) {
		URI _uRI = null;
		if (currentResource != null) {
			_uRI = currentResource.getURI();
		}
		final URI currentURI = _uRI;
		URI _createURI = null;
		if (URI.class != null) {
			_createURI = URI.createURI(importedURIAsString);
		}
		final URI importedURI = _createURI;
		URI _resolve = null;
		if (importedURI != null) {
			_resolve = importedURI.resolve(currentURI);
		}
		final URI resolvedURI = _resolve;
		ResourceSet _resourceSet = null;
		if (currentResource != null) {
			_resourceSet = currentResource.getResourceSet();
		}
		final ResourceSet currentResourceSet = _resourceSet;
		Resource _resource = null;
		if (currentResourceSet != null) {
			_resource = currentResourceSet.getResource(resolvedURI, true);
		}
		final Resource resource = _resource;
		return resource;
	}

	public void start() {
		this.setPreferenceStore(Activator.getDefault().getPreferenceStore());
		this.create();
		final Image image = Activator.getDefault().getImageRegistry().get(Activator.IMAGE_PATH);
		this.getShell().setImage(image);
		String modelName = path.lastSegment().substring(0, path.lastSegment().lastIndexOf('.'));
		this.getShell().setText("Change TyphonDL Model \"" + modelName + "\"");
		this.open();
	}
}
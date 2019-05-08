package de.atb.typhondl.xtext.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.ui.util.FileOpener;
import org.eclipse.xtext.ui.wizard.template.TemplateLabelProvider;

import com.google.inject.Inject;
import com.google.inject.Injector;

import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.Cluster;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.Deployment;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Element;
import de.atb.typhondl.xtext.typhonDL.SupportedDBMS;
import de.atb.typhondl.xtext.ui.activator.Activator;
import de.atb.typhondl.xtext.ui.editor.EditorPageFactory;
import de.atb.typhondl.xtext.ui.editor.PreferenceNodeFactory;

@SuppressWarnings("restriction")
public class OpenEditorHandler extends AbstractHandler {

	@Inject
	IGrammarAccess grammarAccess;
	@Inject
	private TemplateLabelProvider labelProvider;
	@Inject
	private FileOpener fileOpener;

	IPath path = null;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		ISelection selection = page.getSelection();

		if (selection != null & selection instanceof IStructuredSelection) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			Object firstElement = strucSelection.getFirstElement();
			if (firstElement instanceof IAdaptable) {
				IFile file = ((IAdaptable) firstElement).getAdapter(IFile.class);
				path = file.getLocation();
			}
			Shell activeShell = HandlerUtil.getActiveShell(event);

			Injector injector = Activator.getInstance().getInjector(Activator.DE_ATB_TYPHONDL_XTEXT_TYPHONDL);
			XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
			resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);

			URI modelURI = URI.createFileURI(path.toString());
			String modelName = path.lastSegment().substring(0, path.lastSegment().lastIndexOf('.'));
			DeploymentModel model = (DeploymentModel) resourceSet.getResource(modelURI, true).getContents().get(0);

			PreferenceManager preferenceManager = createPages(model);
			final Image image = Activator.getDefault().getImageRegistry().get(Activator.IMAGE_PATH);
			PreferenceDialog preferenceDialog = new PreferenceDialog(activeShell, preferenceManager);
			preferenceDialog.setPreferenceStore(Activator.getDefault().getPreferenceStore());
			preferenceDialog.create();
			preferenceDialog.getShell().setImage(image);
			preferenceDialog.getShell().setText("Change TyphonDL Model \"" + modelName + "\"");
			preferenceDialog.open();
		}
		return null;
	}

	private PreferenceManager createPages(DeploymentModel model) {
		
		PreferenceManager preferenceManager = new PreferenceManager();

		preferenceManager.addToRoot(PreferenceNodeFactory
				.createPreferenceNode(EditorPageFactory.createEditorPage(model)));

		PreferenceNode databaseNode = PreferenceNodeFactory
				.createPreferenceNode(EditorPageFactory.createEditorPage(getDB(model)));
		preferenceManager.addToRoot(databaseNode);

		EList<SupportedDBMS> db = getDB(model).getDbs();
		for (SupportedDBMS supportedDBMS : db) {
			databaseNode.add(PreferenceNodeFactory
					.createPreferenceNode(EditorPageFactory.createEditorPage(supportedDBMS)));
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
					appNode.add(PreferenceNodeFactory
							.createPreferenceNode(EditorPageFactory.createEditorPage(container)));
				}
			}
		}

		preferenceManager.addToRoot(deploymentNode);
		return preferenceManager;
	}

	private EList<Cluster> getClusters(DeploymentModel model) {
		return getDeployment(model).getClusters();
	}

	private Deployment getDeployment(DeploymentModel model) {
		for (Element element : model.getElements()) {
			// TODO not nice
			if (element.eClass().getInstanceClassName().equals("de.atb.typhondl.xtext.typhonDL.Deployment")) {
				return (Deployment) element;
			}
		}
		return null;
	}

	private DB getDB(DeploymentModel model) {
		for (Element element : model.getElements()) {
			// TODO not nice
			if (element.eClass().getInstanceClassName().equals("de.atb.typhondl.xtext.typhonDL.DB")) {
				return (DB) element;
			}
		}
		return null;
	}

}

package de.atb.typhondl.xtext.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.ui.XtextProjectHelper;
import org.eclipse.xtext.ui.resource.XtextLiveScopeResourceSetProvider;

import com.google.inject.Inject;

import de.atb.typhondl.acceleo.services.Services;

/**
 * This Handler is called when clicking "Generate Deployment Scripts" in the
 * TyphonDL context menu.
 * {@link Services#generateDeployment(IFile, XtextLiveScopeResourceSetProvider)}
 * is called. Adds Xtext nature if it's missing from the project in which the
 * selection (i.e. the Typhon DL model) is contained.
 * 
 * @author flug
 *
 */
public class GenerateScriptsHandler extends AbstractHandler {

	@Inject
	XtextLiveScopeResourceSetProvider provider;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		String result = "";
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelectionChecked(event);
		Object object = selection.getFirstElement();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		if (object instanceof IFile) {
			IFile file = (IFile) object;

			// add Xtext Nature
			IProject project = file.getProject();
			boolean hasXtextNature;
			try {
				hasXtextNature = project.hasNature(XtextProjectHelper.NATURE_ID);
				if (!hasXtextNature) {
					IProjectDescription projectDescription = project.getDescription();
					String[] oldNatures = projectDescription.getNatureIds();
					String[] newNatures = new String[oldNatures.length + 1];
					System.arraycopy(oldNatures, 0, newNatures, 1, oldNatures.length);
					newNatures[0] = XtextProjectHelper.NATURE_ID;
					projectDescription.setNatureIds(newNatures);
					project.setDescription(projectDescription, null);
				}
			} catch (CoreException e) {
				MessageDialog.openWarning(HandlerUtil.getActiveWorkbenchWindow(event).getShell(), "UI",
						"Please add Xtext Project Nature to your project");
				e.printStackTrace();
			}

			result = Services.generateDeployment(file, provider);

			for (IProject iproject : root.getProjects()) {
				try {
					iproject.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(window.getShell(), "UI", result);
		return null;
	}

}

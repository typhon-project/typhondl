package de.atb.typhondl.xtext.ui.handlers;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import de.atb.typhondl.acceleo.services.Services;

public class GenerateScriptsHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelectionChecked(event);
		Object object = selection.getFirstElement();

		if (object instanceof IFile) {
			IFile file = (IFile) object;
			String pathToXTextModel = file.getFullPath().toString();
			String modelName = file.getName().replace("." + file.getFileExtension(), "");
			File folder = new File(ResourcesPlugin.getWorkspace().getRoot()
					+ File.separator + modelName); // TODO
			Services.generateDeployment(pathToXTextModel, folder.getAbsolutePath());

			for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
				try {
					project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(window.getShell(), "UI", "Deployment Scripts were generated");
		return null;
	}

}

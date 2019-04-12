package de.atb.typhondl.xtext.ui.handlers;


import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
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
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot(); //doesnt have to be workspace, but folder of modeling project
		if (object instanceof IFile) {
			//IFile ifile = (IFile) object;
			IFile file = (IFile) object;
			File tempFile = new File(root + File.separator);
			String fileAbsolutPath = tempFile.getAbsolutePath();
			System.out.println("absolutPath = " + fileAbsolutPath); 
			String folder = tempFile.getAbsolutePath(); //TODO really not nice, its root, but that doesn't work
			System.out.println("folder = " + folder);
			System.out.println("Services.generateDeployment(" + file.getFullPath().toOSString()+ ", " + folder + ")");
			Services.generateDeployment(file.getFullPath().toString(), folder);
			for (IProject project : root.getProjects()) { //TODO
				System.out.println("projectName: " + project.getName());
				try {
					project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(window.getShell(), "UI", "Deployment Scripts were generated");
		System.out.println("numberOfProjects in " + root + " :" + root.getProjects().length);
		return null;
	}

}

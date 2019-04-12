package de.atb.typhondl.xtext.ui.handlers;

import java.io.File;

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
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;

import de.atb.typhondl.acceleo.services.Services;

public class GenerateScriptsHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelectionChecked(event);
		Object object = selection.getFirstElement();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		if (object instanceof IFile) {
			IFile file = (IFile) object;
			String folder = new File(root + File.separator).getAbsolutePath();

			Services.generateDeployment(file.getFullPath().toString(), folder);

			String projectName = "deployment";
			IProgressMonitor progressMonitor = new NullProgressMonitor();
			IProject project = root.getProject(projectName);
			IProjectDescription description = workspace.newProjectDescription(projectName);
			String[] natures = {JavaCore.NATURE_ID, "org.eclipse.m2e.core.maven2Nature"};
			description.setNatureIds(natures);
			System.out.println("project: " + (project == null) + ", " + project.getName());
			try {
				project.create(progressMonitor);
				project.open(progressMonitor);
			} catch (CoreException e1) {
				e1.printStackTrace();
			}
			
			for (IProject iproject : root.getProjects()) { // TODO
				System.out.println("projectName: " + iproject.getName());
				try {
					iproject.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
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

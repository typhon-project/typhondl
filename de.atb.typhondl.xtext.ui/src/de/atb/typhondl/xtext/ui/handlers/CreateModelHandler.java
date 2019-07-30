package de.atb.typhondl.xtext.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.IGrammarAccess;

import com.google.inject.Inject;

import de.atb.typhondl.xtext.ui.creationWizard.CreateModelWizard;

public class CreateModelHandler extends AbstractHandler {

	@Inject
	IGrammarAccess grammarAccess;

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

				CreateModelWizard fileWizard = new CreateModelWizard(file);
				WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), fileWizard);

				dialog.open();
			}

		}

		for (IProject iproject : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			try {
				iproject.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}

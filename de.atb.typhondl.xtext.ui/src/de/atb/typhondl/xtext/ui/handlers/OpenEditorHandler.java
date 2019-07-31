package de.atb.typhondl.xtext.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

//import de.atb.typhondl.xtext.ui.editor.TyphonEditorDialog;

public class OpenEditorHandler extends AbstractHandler {

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

			//TyphonEditorDialog preferenceDialog = new TyphonEditorDialog(activeShell, path);
			//preferenceDialog.start();
		}
		return null;
	}
}

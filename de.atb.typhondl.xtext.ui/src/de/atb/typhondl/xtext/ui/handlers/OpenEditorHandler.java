package de.atb.typhondl.xtext.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.ui.util.FileOpener;
import org.eclipse.xtext.ui.wizard.template.TemplateLabelProvider;
import com.google.inject.Inject;
import de.atb.typhondl.xtext.ui.activator.Activator;
import de.atb.typhondl.xtext.ui.editor.TyphonEditorDialog;

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

			PreferenceDialog preferenceDialog = new TyphonEditorDialog(activeShell, path);
			preferenceDialog.setPreferenceStore(Activator.getDefault().getPreferenceStore());
			addListenerToStore();
			preferenceDialog.create();
			final Image image = Activator.getDefault().getImageRegistry().get(Activator.IMAGE_PATH);
			preferenceDialog.getShell().setImage(image);
			String modelName = path.lastSegment().substring(0, path.lastSegment().lastIndexOf('.'));
			preferenceDialog.getShell().setText("Change TyphonDL Model \"" + modelName + "\"");
			preferenceDialog.open();
		}
		return null;
	}

	private void addListenerToStore() {

		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent event) {
				System.out.println(event.getProperty() + ": " + event.getNewValue().toString());
			}
		});

	}
}

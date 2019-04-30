package de.atb.typhondl.xtext.ui.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.AbstractRule;
import org.eclipse.xtext.Grammar;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.ui.util.FileOpener;
import org.eclipse.xtext.ui.wizard.template.TemplateLabelProvider;

import com.google.inject.Inject;
import com.google.inject.Injector;

import de.atb.typhondl.xtext.TyphonDLStandaloneSetup;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.ui.activator.Activator;
import de.atb.typhondl.xtext.ui.editor.MyPreference;

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
			DeploymentModel model = (DeploymentModel) resourceSet.getResource(modelURI, true).getContents().get(0);
			
			PreferenceManager preferenceManager = createPages(model);

			PreferenceDialog preferenceDialog = new PreferenceDialog(activeShell, preferenceManager); 
			preferenceDialog.setPreferenceStore(Activator.getDefault().getPreferenceStore());
			preferenceDialog.create();
			preferenceDialog.open();
		}
		return null;
	}	
	private PreferenceManager createPages(DeploymentModel model) {
		IPreferencePage page1 = new MyPreference(model); 
		page1.setTitle("Testing");
		PreferenceNode node = new PreferenceNode("1", page1);
		PreferenceManager preferenceManager = new PreferenceManager();
		preferenceManager.addToRoot(node);
//		List<IPreferencePage> listOfPages = new ArrayList<IPreferencePage>();
//		List<PreferenceNode> listOfNodes = new ArrayList<PreferenceNode>();
//		
//		for (int i = 0; i < listOfPages.size(); i++) {
//			listOfNodes.add(new PreferenceNode("0"+i, listOfPages.get(i)));
//		}
//		return null;
		return preferenceManager;
	}

}

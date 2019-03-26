package de.atb.typhondl.xtext.ui.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.xtext.AbstractRule;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.Parameter;
import org.eclipse.xtext.ParserRule;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.generator.trace.node.GeneratorNodeExtensions;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.ui.util.FileOpener;
import org.eclipse.xtext.ui.wizard.template.TemplateLabelProvider;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Injector;

import de.atb.typhondl.xtext.TyphonDLStandaloneSetup;
import de.atb.typhondl.xtext.typhonDL.Application;

@SuppressWarnings("restriction")
public class WorkbenchPropertyPage1 extends PropertyPage implements IWorkbenchPropertyPage {

	@Inject
	private TemplateLabelProvider labelProvider;
	@Inject
	private FileOpener fileOpener;
	@Inject
	private IGrammarAccess grammarAccess;

	private Resource resource;

	public WorkbenchPropertyPage1() {
		super();
	}

	@Override
	protected Control createContents(Composite parent) {

		Composite main = new Composite(parent, SWT.NONE);
		main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		main.setLayout(new GridLayout(2, false));

		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);

		this.resource = getResource();

		createDatabaseArea(main, gridData);

		Label folderLabel = new Label(main, SWT.NONE);
		folderLabel.setText("Test1");
		Label folderText = new Label(main, SWT.NONE);
		folderText.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, true, false));

		Label fileLabel = new Label(main, SWT.NONE);
		fileLabel.setText("Test2");
		Text fileText = new Text(main, SWT.BORDER);
		fileText.setLayoutData(gridData);

		return null;
	}

	private Resource getResource() {
		String path = "file:/C:/Users/flug/eclipse-workspace-current/typhondl/example.typhondl/docker-compose-example.tdl"; // TODO
		// getSelection()
		Injector injector = new TyphonDLStandaloneSetup().createInjectorAndDoEMFRegistration();
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		return resource = resourceSet.getResource(URI.createURI(path), true);
	}

	private void createDatabaseArea(Composite main, GridData gridData) {

//		Iterable<DB> _filter = Iterables.<DB>filter(IteratorExtensions.<EObject>toIterable(resource.getAllContents()),
//				DB.class);
//		List<SupportedDBMS> dbs = new ArrayList<SupportedDBMS>();
//		_filter.iterator().forEachRemaining(db -> dbs.addAll(db.getDbs()));
//		
//		for (SupportedDBMS db : dbs) {
//			new Label(main, SWT.NONE).setText("Name: ");
//			new Label(main, SWT.NONE).setText(db.getName());
//			
//		}
		
//		Label folderText = new Label(main, SWT.NONE);
//		folderText.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, true, false));
//
//		Label fileLabel = new Label(main, SWT.NONE);
//		fileLabel.setText("Test2");
//		Text fileText = new Text(main, SWT.BORDER);
//		fileText.setLayoutData(gridData);
	}

}

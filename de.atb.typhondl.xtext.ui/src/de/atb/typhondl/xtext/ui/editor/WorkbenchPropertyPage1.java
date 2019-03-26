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
import de.atb.typhondl.xtext.typhonDL.Database;
import de.atb.typhondl.xtext.typhonDL.MariaDB;
import de.atb.typhondl.xtext.typhonDL.OptionalEntries;

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

		Iterable<Database> _filter = Iterables
				.<Database>filter(IteratorExtensions.<EObject>toIterable(resource.getAllContents()), Database.class);
		_filter.iterator().forEachRemaining(db -> createDatabaseArea(main, gridData, db));

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

	private void createDatabaseArea(Composite main, GridData gridData, Database db) {

		new Label(main, SWT.NONE).setText("________________________");
		new Label(main, SWT.NONE).setText("1");
		new Label(main, SWT.NONE).setText("Name: ");
		new Label(main, SWT.NONE).setText(db.getName());
		new Label(main, SWT.NONE).setText("DBMS: ");
		new Label(main, SWT.NONE).setText(db.eClass().getInstanceClass().getSimpleName());
		if (db.eClass().getInstanceClass().equals(MariaDB.class)) {
			MariaDB thisdb = (MariaDB) db;
			new Label(main, SWT.NONE).setText("________________________");
			new Label(main, SWT.NONE).setText("1.1");
			thisdb.getMandatoryEntry().eContents().forEach(content -> {
				new Label(main, SWT.NONE).setText(content.eClass().getInstanceClass().getSimpleName());
				new Label(main, SWT.NONE).setText("don't know");
			});
			new Label(main, SWT.NONE).setText("________________________");
			new Label(main, SWT.NONE).setText("1.2");
			for (OptionalEntries optEntry : thisdb.getOptionalEntries()) {
				optEntry.getValue();
			}
			thisdb.getOptionalEntries().iterator().forEachRemaining(entry -> {
				new Label(main, SWT.NONE).setText(entry.eClass().getInstanceClass().getSimpleName());
				var newEntry = entry.eClass().getInstanceClass().cast(entry);
				new Label(main, SWT.NONE).setText(newEntry.getClass().getSimpleName() + newEntry.toString());
			});
			new Label(main, SWT.NONE).setText("________________________");
			new Label(main, SWT.NONE).setText("1.3");
			thisdb.eAllContents().forEachRemaining(content -> {
				new Label(main, SWT.NONE).setText(content.eClass().getName());
				new Label(main, SWT.NONE).setText(content.eContainer().eClass().getName());
			});
		}
		new Label(main, SWT.NONE).setText("________________________");
		new Label(main, SWT.NONE).setText("2");
		db.eAllContents().forEachRemaining(content -> {
			new Label(main, SWT.NONE).setText(content.eClass().getInstanceClass().getSimpleName() + " = ");
			new Label(main, SWT.NONE).setText("");
		});

		// Label folderText = new Label(main, SWT.NONE);
		// folderText.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, true,
		// false));
		//
		// Label fileLabel = new Label(main, SWT.NONE);
		// fileLabel.setText("Test2");
		// Text fileText = new Text(main, SWT.BORDER);
		// fileText.setLayoutData(gridData);
	}

}

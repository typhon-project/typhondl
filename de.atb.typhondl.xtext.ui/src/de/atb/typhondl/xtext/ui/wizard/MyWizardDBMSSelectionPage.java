/**
 * 
 */
package de.atb.typhondl.xtext.ui.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ide.IIDEHelpContextIds;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

/**
 * @author flug
 *
 */
public class MyWizardDBMSSelectionPage extends WizardPage {

	String modelPath;
	HashMap<DBType, List<String>> data;
	
	/**
	 * @param pageName
	 * @param modelPath 
	 */
	public MyWizardDBMSSelectionPage(String pageName, String modelPath) {
		super(pageName);
		setPageComplete(true);
		this.modelPath = modelPath;
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);


        initializeDialogUnits(parent);

        PlatformUI.getWorkbench().getHelpSystem().setHelp(composite,
                IIDEHelpContextIds.NEW_PROJECT_WIZARD_PAGE); //TODO

        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		selectDBMSArea(composite);
		// TODO
		
        setErrorMessage(null);
        setMessage(null);
        setControl(composite);
        Dialog.applyDialogFont(composite);

	}
	
	private final void selectDBMSArea(Composite composite) {
		
		Composite selectionGroup = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        selectionGroup.setLayout(layout);
        selectionGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		List<Database> dbs = loadData();
		//TODO for each Database a label and combo
		for (Database db : dbs) {
			databaseArea(db, selectionGroup);
		}
	}

	private void databaseArea(Database db, Composite parent) {
		
		String dbName = db.getName();
		DBType dbType = db.getType();
		
		Label name = new Label(parent, SWT.NONE);
		name.setText(dbName + " (" + dbType.toString() + ")");
		
//		Label type = new Label(parent, SWT.NONE);
//		type.setText(dbType.toString());
		
		Combo dbms = new Combo(parent, SWT.READ_ONLY);
		dbms.setItems(dbType.getPossibleDBMSs()); // not pretty!!
	}

	private List<Database> loadData() {
		// TODO read modelPath
		List<Database> dbs = new ArrayList<>();
		//test:
		dbs.add(new Database("Orders", DBType.relationaldb));
		dbs.add(new Database("Products", DBType.graphdb));
		dbs.add(new Database("Photos", DBType.keyvaluedb));
		dbs.add(new Database("Reviews", DBType.documentdb));
		dbs.add(new Database("MyOwn", DBType.relationaldb));
		return dbs;
	}

	public void setModelPath(String modelPath) {
		this.modelPath = modelPath;
	}

}

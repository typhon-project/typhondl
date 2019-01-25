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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

/**
 * @author flug
 *
 */
@SuppressWarnings("restriction")
public class MyWizardDBMSSelectionPage extends WizardPage {

	// QUESTION all private?
	String modelPath;
	private HashMap<String, Database> dbsMap;
	List<Combo> combos;
	
	/**
	 * @param pageName
	 * @param modelPath 
	 */
	public MyWizardDBMSSelectionPage(String pageName, String modelPath) {
		super(pageName);
		setPageComplete(false);
		this.modelPath = modelPath;
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);


        initializeDialogUnits(parent);

        PlatformUI.getWorkbench().getHelpSystem().setHelp(composite,
                IIDEHelpContextIds.NEW_PROJECT_WIZARD_PAGE); 
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		selectDBMSArea(composite);
		// TODO set pageComplete true when all combos have a selection
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

		loadDataToMap();
		
		combos = new ArrayList<>();
		// label + combo for each db
		for (Database db : dbsMap.values()) {
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
		combos.add(dbms);
		dbms.setItems(dbType.getPossibleDBMSs()); // TODO this needs to be aligned
		dbms.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		dbms.addModifyListener(new ModifyListener() {			
			@Override
			public void modifyText(ModifyEvent e) {
				db.setDbms(dbms.getText());			
				setPageComplete(checkComplete());
			}
		});
	}

	protected boolean checkComplete() {
		for (Combo combo : combos) {
			if (combo.getText() == null) {
				return false;
			}
		}
		return true;
	}

	private void loadDataToMap(){
		// TODO read modelPath
		dbsMap = new HashMap<String, Database>();
		//test:
		dbsMap.put("Orders", new Database("Orders", DBType.relationaldb, null));
		dbsMap.put("Products", new Database("Products", DBType.graphdb, null));
		dbsMap.put("Photos", new Database("Photos", DBType.keyvaluedb, null));
		dbsMap.put("Reviews", new Database("Reviews", DBType.documentdb, null));
		dbsMap.put("MyOwn", new Database("MyOwn", DBType.relationaldb, null));
	}

	public void setModelPath(String modelPath) {
		this.modelPath = modelPath;
	}

}

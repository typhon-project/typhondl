/**
 * 
 */
package de.atb.typhondl.xtext.ui.wizard;

import java.net.URI;
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
	private HashMap<String, Database> dbsMap;
	List<Combo> combos = new ArrayList<>();
	
	/**
	 * @param pageName
	 * @param modelPath 
	 */
	public MyWizardDBMSSelectionPage(String pageName, URI modelPath) {
		super(pageName);
		loadDataToMap(modelPath);
		setPageComplete(false);
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
		dbms.setItems(dbType.getPossibleDBMSs());
		dbms.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		dbms.setText(db.getDbms());
		dbms.addModifyListener(new ModifyListener() {			
			@Override
			public void modifyText(ModifyEvent e) {
				db.setDbms(dbms.getText());			
				setPageComplete(checkComplete());
			}
		});
	}

	protected boolean checkComplete() {
		return !combos.stream().anyMatch((combo -> combo.getText().equals("")));
	}

	private void loadDataToMap(URI modelPath){
		// TODO read modelPath
		ModelReader reader = new ModelReader(modelPath);
		dbsMap = reader.getData();
		//test:
		//dbsMap.put("Orders", new Database("Orders", DBType.relationaldb, ""));
		//dbsMap.put("Products", new Database("Products", DBType.graphdb, ""));
		//dbsMap.put("Photos", new Database("Photos", DBType.keyvaluedb, ""));
		//dbsMap.put("Reviews", new Database("Reviews", DBType.documentdb, ""));
		//dbsMap.put("MyOwn", new Database("MyOwn", DBType.relationaldb, ""));
	}

}

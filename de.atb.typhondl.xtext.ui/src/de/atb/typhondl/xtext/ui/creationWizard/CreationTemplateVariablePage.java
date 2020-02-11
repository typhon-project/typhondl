package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.text.templates.TemplateVariable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import de.atb.typhondl.xtext.typhonDL.DB;

public class CreationTemplateVariablePage extends MyWizardPage {

	/**
	 * TODO
	 */
	private HashMap<DB, TemplateVariable[]> result;

	/**
	 * TODO
	 * @param pageName
	 * @param result
	 */
	protected CreationTemplateVariablePage(String pageName, HashMap<DB, TemplateVariable[]> result) {
		super(pageName);
		this.result = result;
	}

	@Override
	public void createControl(Composite parent) {
		setTitle("Choose values for template variables");
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		main.setLayout(new GridLayout(1, false));
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gridData.horizontalSpan = 2;

		for (DB db : result.keySet()) {

			TemplateVariable[] variables = result.get(db);
			List<TemplateVariable> variablesList = new ArrayList<>(Arrays.asList(variables));
			
			if (variables.length != 0) {
				// this is the database.name:
				variablesList.removeIf(variable -> (variable.getOffsets()[0] == 9));
			}

			if (variablesList.size() != 0) {
				// create a group for each database that has templates
				Group group = new Group(main, SWT.READ_ONLY);
				group.setLayout(new GridLayout(2, false));
				group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
				group.setText(db.getName());
			}

		}

		setControl(main);

	}

	/**
	 * TODO
	 * @return
	 */
	public ArrayList<DB> getDBs() {
		return new ArrayList<>(result.keySet());
	}

}

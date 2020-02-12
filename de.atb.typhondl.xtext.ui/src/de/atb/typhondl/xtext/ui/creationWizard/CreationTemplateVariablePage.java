package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.text.templates.TemplateVariable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.utilities.PreferenceReader;

public class CreationTemplateVariablePage extends MyWizardPage {

	/**
	 * TODO
	 */
	private HashMap<DB, TemplateBuffer> result;

	/**
	 * TODO
	 * 
	 * @param pageName
	 * @param result
	 */
	protected CreationTemplateVariablePage(String pageName, HashMap<DB, TemplateBuffer> result) {
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

			TemplateBuffer templateBuffer = result.get(db);
			TemplateVariable[] variables = templateBuffer.getVariables();
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

				GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);

				// create a field for each variable:
				for (TemplateVariable templateVariable : variablesList) {
					new Label(group, NONE).setText(templateVariable.getName() + ":");
					Text text = new Text(group, SWT.BORDER);
					text.setText(templateVariable.getName());
					text.setLayoutData(gridDataFields);
					text.addModifyListener(e -> {
						String oldValue = templateVariable.getValues()[0];
						String newValue = text.getText();
						// replace old value in template variable
						templateVariable.setValue(newValue);
						// replace old value in pattern string
						String newPattern = templateBuffer.getString().replace(oldValue, newValue);
						templateBuffer.setContent(newPattern, variablesList.toArray(new TemplateVariable[0]));
						updateDB(db, templateBuffer);
					});
				}
			}

		}

		setControl(main);

	}

	private void updateDB(DB db, TemplateBuffer templateBuffer) {
		DB newDB = PreferenceReader.getModelObject(TyphonDLFactory.eINSTANCE.createDB(), templateBuffer);
		db.getParameters().clear();
		db.getParameters().addAll(newDB.getParameters());
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public ArrayList<DB> getDBs() {
		return new ArrayList<>(result.keySet());
	}

}

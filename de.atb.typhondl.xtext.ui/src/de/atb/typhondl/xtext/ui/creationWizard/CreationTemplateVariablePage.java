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

/**
 * Optional page for the TyphonDL {@link CreateModelWizard}. If the previously
 * selected DBMS template contains {@link TemplateVariable}s, they can be edited
 * here.
 * 
 * @author flug
 *
 */
public class CreationTemplateVariablePage extends MyWizardPage {

	/**
	 * A map with all {@link DB}s and their {@link TemplateBuffer} with updated
	 * {@link TemplateVariable}s
	 */
	private HashMap<DB, TemplateBuffer> result;

	/**
	 * Creates instance of {@link CreationTemplateVariablePage}.
	 * 
	 * @param pageName the name of the page
	 * @param result   the map, in which the {@link DB}s and their
	 *                 {@link TemplateBuffer} with updated {@link TemplateVariable}s
	 *                 is stored.
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
			if (templateBuffer != null) {
				TemplateVariable[] variables = templateBuffer.getVariables();
				List<TemplateVariable> variablesList = new ArrayList<>(Arrays.asList(variables));

				// this is the database.name, which should not be changed, so it is removed from
				// the list:
				variablesList.removeIf(variable -> (variable.getOffsets()[0] == 9));

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
						int variableIndex = variablesList.indexOf(templateVariable);
						int oldLenght = templateVariable.getLength();
						// replace old value in template variable
						templateVariable.setValue(text.getText());
						int newLength = templateVariable.getLength();
						// replace old value in pattern string
						String newPattern = updatePattern(templateVariable, templateBuffer.getString(), oldLenght);
						// correct offset of other variables if this variable is not the last one
						if (variableIndex != variablesList.size() - 1) {
							for (int i = variableIndex + 1; i < variablesList.size(); i++) {
								variablesList.get(i).setOffsets(
										new int[] { variablesList.get(i).getOffsets()[0] + newLength - oldLenght });
							}
						}
						templateBuffer.setContent(newPattern, variablesList.toArray(new TemplateVariable[0]));
						updateDB(db, templateBuffer);
					});
				}
			}
		}
		setControl(main);
	}

	/**
	 * Replaces old variable value with new one
	 * 
	 * @param templateVariable The TemplateVariable with the new value
	 * @param oldPattern       The old Pattern
	 * @param oldLength        The length of the old variable value
	 * @return The updated Pattern
	 */
	private String updatePattern(TemplateVariable templateVariable, String oldPattern, int oldLength) {
		int offset = templateVariable.getOffsets()[0];
		return oldPattern.substring(0, offset) + templateVariable.getValues()[0]
				+ oldPattern.substring(offset + oldLength);
	}

	/**
	 * updates the {@link DB} model entity in the <code>result</code> map
	 * 
	 * @param db             The {@link DB} to be updated
	 * @param templateBuffer The source for the updated {@link TemplateVariable}s
	 */
	private void updateDB(DB db, TemplateBuffer templateBuffer) {
		DB newDB = PreferenceReader.getModelObject(TyphonDLFactory.eINSTANCE.createDB(), templateBuffer);
		db.getParameters().clear();
		db.getParameters().addAll(newDB.getParameters());
	}

	/**
	 * Get all updated {@link DB}s
	 * 
	 * @return A List of all {@link DB}s with updated {@link TemplateVariable}s
	 */
	public ArrayList<DB> getDBs() {
		return new ArrayList<>(result.keySet());
	}

}

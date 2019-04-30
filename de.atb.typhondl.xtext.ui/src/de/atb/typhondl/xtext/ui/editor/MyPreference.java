package de.atb.typhondl.xtext.ui.editor;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.internal.IPreferenceConstants;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Element;

public class MyPreference extends FieldEditorPreferencePage implements IPreferencePage {

	private DeploymentModel model;

	public MyPreference(DeploymentModel model) {
		super();
		System.out.println("in constructor");
		this.model = model;
	}

	@Override
	protected void createFieldEditors() {
		System.out.println("inCreateFieldEditors");
		DB allDatabases = getDBs(model);
		
		IntegerFieldEditor indentSpaces = new IntegerFieldEditor(
				"testField",
				allDatabases.getDbs().get(0).getName(),
		 		getFieldEditorParent());
			indentSpaces.setValidRange(0, 10);
			addField(indentSpaces);	

	}
	
	private static DB getDBs(DeploymentModel model) {
		for (Element element : model.getElements()) {
			// TODO not nice
			if (element.eClass().getInstanceClassName().equals("de.atb.typhondl.xtext.typhonDL.DB")) {
				return (DB) element;
			}
		}
		return null;
	}

}

package de.atb.typhondl.xtext.ui.editor.pages;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.Key_Value;
import de.atb.typhondl.xtext.typhonDL.Key_ValueArray;
import de.atb.typhondl.xtext.typhonDL.Key_ValueList;
import de.atb.typhondl.xtext.typhonDL.Property;
import de.atb.typhondl.xtext.ui.activator.Activator;
import de.atb.typhondl.xtext.ui.editor.EditorPage;
import de.atb.typhondl.xtext.ui.editor.TyphonFieldEditor;

public class DBPage extends EditorPage {

	private DB db;
	private List<TyphonFieldEditor> fieldEditorList = new ArrayList<TyphonFieldEditor>();

	public DBPage(DB db) {
		super(db.getName());
		this.db = db;
	}

	@Override
	protected Control createContents(Composite parent) {
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		TyphonFieldEditor field3 = new TyphonFieldEditor(db.getName() + ".image", "image", parent);
		field3.setStringValue(db.getImage().getValue());
		field3.setPreferenceStore(preferenceStore);
		fieldEditorList.add(field3);
		for (Property property : db.getParameters()) {
			createPropertyFields(property, parent);
		}
		return parent;
	}

	private void createPropertyFields(Property property, Composite parent) {
		switch (property.eClass().getInstanceClassName()) {
		case "de.atb.typhondl.xtext.typhonDL.Key_Value":
			Key_Value keyValue = (Key_Value) property;
			TyphonFieldEditor field = new TyphonFieldEditor(db.getName() + "." + keyValue.getName(), keyValue.getName(),
					parent);
			field.setStringValue(keyValue.getValue());
			fieldEditorList.add(field);
			break;
		case "de.atb.typhondl.xtext.typhonDL.Key_ValueArray":
			Key_ValueArray array = (Key_ValueArray) property;
			TyphonFieldEditor field1 = new TyphonFieldEditor(db.getName() + "." + array.getName(), array.getName(),
					parent);
			String string = array.getValue();
			for (String value : array.getValues()) {
				string += ", " + value;
			}
			field1.setStringValue(string);
			fieldEditorList.add(field1);
			break;
		case "de.atb.typhondl.xtext.typhonDL.Key_ValueList":
			Key_ValueList list = (Key_ValueList) property;
			Group group = new Group(parent, SWT.NONE);

			GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.horizontalSpan = 2;
			group.setLayoutData(gridData);

            GridLayout layout = new GridLayout();
            layout.numColumns = 2;
            layout.horizontalSpacing = 8;
			group.setLayout(layout);
			group.setText(property.getName());
			for (String environmentVar : list.getEnvironmentVars()) {
				String key = environmentVar.substring(1, environmentVar.lastIndexOf('='));
				String value = environmentVar.substring(environmentVar.lastIndexOf('=') + 1,
						environmentVar.length() - 1);
				TyphonFieldEditor field2 = new TyphonFieldEditor(db.getName() + "." + property.getName() + "." + key,
						key, group);
				field2.setStringValue(value);
				fieldEditorList.add(field2);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean performOk() {
		for (TyphonFieldEditor editor : fieldEditorList) {
			editor.store();
		}
		changeResource();
		return super.performOk();
	}

	private void changeResource() {
		Resource resource = db.eResource();
		System.out.println(resource.getURI().toString());

	}
}

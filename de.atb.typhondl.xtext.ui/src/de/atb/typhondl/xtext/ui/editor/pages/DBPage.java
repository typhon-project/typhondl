package de.atb.typhondl.xtext.ui.editor.pages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
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
import de.atb.typhondl.xtext.ui.editor.TyphonFieldEditor;

public class DBPage extends EditorPage {

	private DB db;
	private List<TyphonFieldEditor> fieldEditorList = new ArrayList<TyphonFieldEditor>();
	private IPreferenceStore preferenceStore;

	public DBPage(DB db) {
		super(db.getName());
		this.db = db;
	}

	@Override
	protected Control createContents(Composite parent) {
		this.preferenceStore = Activator.getDefault().getPreferenceStore();
		TyphonFieldEditor field3 = new TyphonFieldEditor(db.getName() + ".image", "image", parent);
		field3.setStringValue(db.getImage().getValue());
		field3.setPreferenceStore(preferenceStore);
		field3.store(); // is needed in case model was changed in textual editor
		fieldEditorList.add(field3);
		for (Property property : db.getParameters()) {
			createPropertyFields(property, parent);
		}
		return parent;
	}

	private void createPropertyFields(Property property, Composite parent) {
		if (Key_Value.class.isInstance(property)) {
			Key_Value keyValue = (Key_Value) property;
			TyphonFieldEditor field = new TyphonFieldEditor(db.getName() + ".Key_Value." + keyValue.getName(),
					keyValue.getName(), parent);
			field.setStringValue(keyValue.getValue());
			field.setPreferenceStore(preferenceStore);
			field.store();
			fieldEditorList.add(field);
		} else if (Key_ValueArray.class.isInstance(property)) {
			Key_ValueArray array = (Key_ValueArray) property;
			TyphonFieldEditor field1 = new TyphonFieldEditor(db.getName() + ".Key_ValueArray." + array.getName(),
					array.getName(), parent);
			String string = array.getValue();
			for (String value : array.getValues()) {
				string += ", " + value;
			}
			field1.setStringValue(string);
			field1.setPreferenceStore(preferenceStore);
			field1.store();
			fieldEditorList.add(field1);
		} else if (Key_ValueList.class.isInstance(property)) {
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
			for (Key_Value key_value : list.getKey_Values()) {
				String key = key_value.getName();
				String value = key_value.getValue();
				TyphonFieldEditor field2 = new TyphonFieldEditor(
						db.getName() + ".Key_ValueList." + property.getName() + "." + key, key, group);
				field2.setStringValue(value);
				field2.setPreferenceStore(preferenceStore);
				field2.store();
				fieldEditorList.add(field2);
			}
		}
	}

	@Override
	public boolean performOk() {
		for (TyphonFieldEditor editor : fieldEditorList) {
			String preference = preferenceStore.getString(editor.getPreferenceName());
			String value = editor.getStringValue();
			if (!preference.equals(value)) {
				editor.store();
				preference = editor.getPreferenceName();
				int point = preference.indexOf('.') > 0 ? preference.indexOf('.') : 0;
				String key = preference.substring(point + 1);
				value = editor.getStringValue();
				changedb(key, value);
				changeResource();
			}
		}
		return super.performOk();
	}

	private void changedb(String key, String value) {
		if (key.equals("image")) {
			db.getImage().setValue(value);
			return;
		}
		String type = key.indexOf('.') > 0 ? key.substring(0, key.indexOf('.')) : key;
		String temp = key.indexOf('.') > 0 ? key.substring(key.indexOf('.') + 1) : key;
		String name = temp.indexOf('.') > 0 ? temp.substring(0, temp.indexOf('.')) : temp;
		Property prop = db.getParameters().stream()
				.filter(property -> property.getName().equals(name))
				.findAny().orElse(null);
		switch (type) {
		case "Key_Value":
			((Key_Value) prop).setValue(value);
			break;
		case "Key_ValueList":
			// Key_ValueList list = (Key_ValueList) prop;
			MessageDialog.openError(this.getShell(), "TODO",
					"Sorry, only the image and Key-Value pairs can be saved in the GUI at the moment. "
					+ "Please use the textual editor and wait for the next version.");
			// TODO implement saving changes in Key_ValueList
			break;
		case "Key_ValueArray":
			// Key_ValueArray array = (Key_ValueArray) prop;
			MessageDialog.openError(this.getShell(), "TODO",
					"Sorry, only the image and Key-Value pairs can be saved in the GUI at the moment. "
					+ "Please use the textual editor and wait for the next version.");
			// TODO implement saving changes in Key_ValueArray
			break;
		default:
			break;
		}

	}

	private void changeResource() {
		Resource resource = db.eResource();
		try {
			resource.save(Collections.EMPTY_MAP);
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			for (IProject iproject : root.getProjects()) {
				try {
					iproject.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			MessageDialog.openError(this.getShell(), "Error",
					"Sorry, something went wrong while saving " + resource.getURI().lastSegment() + ".");
			e.printStackTrace();
		}
	}
}

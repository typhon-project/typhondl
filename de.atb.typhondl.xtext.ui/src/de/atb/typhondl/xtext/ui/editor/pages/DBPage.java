package de.atb.typhondl.xtext.ui.editor.pages;

import org.eclipse.jface.preference.StringFieldEditor;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.ui.editor.EditorPage;

public class DBPage extends EditorPage {
	
	private DB db;

	public DBPage(DB db) {
		super(db.getName());
		this.db = db;
	}

	@Override
	protected void createFieldEditors() {
		StringFieldEditor field1 = new StringFieldEditor("name", "Name", getFieldEditorParent());
		field1.setStringValue(db.getName());
		//field1.setPreferenceStore(MyPreferenceStore.getStore());
		System.out.println(field1.getPreferenceStore()==null);
		
		StringFieldEditor field2 = new StringFieldEditor("image", "Image", getFieldEditorParent());
		
	}
}

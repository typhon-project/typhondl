package de.atb.typhondl.xtext.ui.editor.pages;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.ui.activator.Activator;
import de.atb.typhondl.xtext.ui.editor.EditorPage;
import de.atb.typhondl.xtext.ui.editor.TyphonFieldEditor;

public class DBPage extends EditorPage {
	
	private DB db;
	private List<TyphonFieldEditor> fieldEditorList= new ArrayList<TyphonFieldEditor>();

	public DBPage(DB db) {
		super(db.getName());
		this.db = db;
	}

	@Override
	protected Control createContents(Composite parent) {
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		System.out.println(preferenceStore==null);
		TyphonFieldEditor field3 = new TyphonFieldEditor(db.getName() + ".image", "Image", parent);
		field3.setStringValue(db.getImage().getValue());
		field3.setPreferenceStore(preferenceStore);
		fieldEditorList.add(field3);
		return parent;
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
		// TODO Auto-generated method stub
		
	}
}

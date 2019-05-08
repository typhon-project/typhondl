package de.atb.typhondl.xtext.ui.editor.pages;

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
		// TODO Auto-generated method stub
		
	}
}

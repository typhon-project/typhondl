package de.atb.typhondl.xtext.ui.editor.pages;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.ui.editor.EditorPage;

public class DBOverview extends EditorPage {

	private DB db;
	
	public DBOverview(DB db) {
		super("Databases");
		this.db = db;
	}

	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub

	}

}

package de.atb.typhondl.xtext.ui.editor.pages;

import de.atb.typhondl.xtext.typhonDL.SupportedDBMS;
import de.atb.typhondl.xtext.ui.editor.EditorPage;

public class DBPage extends EditorPage {
	
	private SupportedDBMS supportedDBMS;

	public DBPage(SupportedDBMS supportedDBMS) {
		super(supportedDBMS.getName());
		this.supportedDBMS = supportedDBMS;
	}

	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub
		
	}
}

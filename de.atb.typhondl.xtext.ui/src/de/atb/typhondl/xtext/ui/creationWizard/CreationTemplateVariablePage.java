package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.HashMap;

import org.eclipse.jface.text.templates.TemplateVariable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import de.atb.typhondl.xtext.typhonDL.DB;

public class CreationTemplateVariablePage extends MyWizardPage {

	protected CreationTemplateVariablePage(String pageName, HashMap<DB, TemplateVariable[]> result) {
		super(pageName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createControl(Composite parent) {
		setTitle("Choose values for template variables");
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		main.setLayout(new GridLayout(1, false));
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gridData.horizontalSpan = 2;
		
		setControl(main);
		
	}


}

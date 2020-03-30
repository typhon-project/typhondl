package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DB;

public class CreationContainerPage extends MyWizardPage {

	/**
	 * list of DBs taken from the MLmodel enriched with wizard and template input
	 * and the corresponding TemplateVariables
	 */
	private ArrayList<DB> dbs;

	/**
	 * each DB has a container //TODO list of containers for master/slave setup?
	 */
	private HashMap<DB, Container> result;

	/**
	 * polystore.properties from classpath
	 */
	private Properties properties;

	protected CreationContainerPage(String pageName, ArrayList<DB> dbs) {
		super(pageName);
		this.dbs = dbs;
		this.result = new HashMap<>();

	}

	@Override
	public void createControl(Composite parent) {
		setTitle("Configure database containers");
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		main.setLayout(new GridLayout(1, false));
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gridData.horizontalSpan = 2;

		for (DB db : dbs) {
			// create a group for each database
			Group group = new Group(main, SWT.READ_ONLY);
			group.setLayout(new GridLayout(2, false));
			group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			group.setText(db.getName());

		}

	}

}

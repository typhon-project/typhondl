package de.atb.typhondl.xtext.ui.creationWizard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.ContainerType;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.Key_Values;
import de.atb.typhondl.xtext.typhonDL.Ports;
import de.atb.typhondl.xtext.typhonDL.Reference;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.utilities.PropertiesLoader;

/**
 * Page for the TyphonDL {@link CreateModelWizard}. Container specific
 * configuration can be inserted here.
 * 
 * @author flug
 *
 */
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

	/**
	 * Docker Compose or Kubernetes
	 */
	private int chosenTemplate;

	/**
	 * Creates instance of {@link CreationContainerPage}
	 * 
	 * @param pageName
	 * @param dbs            databases with selected DBMSs
	 * @param chosenTemplate
	 */
	protected CreationContainerPage(String pageName, ArrayList<DB> dbs, int chosenTemplate) {
		super(pageName);
		this.dbs = dbs;
		this.chosenTemplate = chosenTemplate;
		this.result = new HashMap<>();
		try {
			this.properties = PropertiesLoader.loadProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

			GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);

			// create a Container Model Object
			Container container = TyphonDLFactory.eINSTANCE.createContainer();
			container.setName(db.getName());
			ContainerType docker = TyphonDLFactory.eINSTANCE.createContainerType();
			docker.setName("Docker");
			container.setType(docker);
			Reference reference = TyphonDLFactory.eINSTANCE.createReference();
			reference.setReference(db);
			container.setDeploys(reference);
			Ports ports = TyphonDLFactory.eINSTANCE.createPorts();
			Key_Values port = TyphonDLFactory.eINSTANCE.createKey_Values();
			port.setName("target");
			String targetPort = properties.getProperty(db.getType().getName().toLowerCase() + ".port");
			port.setValue(targetPort);
			ports.getKey_values().add(port);

			new Label(group, NONE).setText("Container port: ");
			Text portText = new Text(group, SWT.BORDER);
			portText.setText(targetPort);
			portText.setToolTipText("This is the port that will be exposed inside the network/cluster");
			portText.setLayoutData(gridDataFields);
			portText.addModifyListener(e -> {
				container.getPorts().getKey_values().stream().filter(test -> test.getName().equalsIgnoreCase("target"))
						.collect(Collectors.toList()).get(0).setValue(((Text) e.widget).getText());
			});
		}
		setControl(main);
	}

	public HashMap<DB, Container> getResult() {
		return result;
	}

}

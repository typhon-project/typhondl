package de.atb.typhondl.xtext.ui.creationWizard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

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
		setDescription("The container port has to be exposed in the Dockerfile, here the default ports are used.");
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		main.setLayout(new GridLayout(1, false));

		String reservationWord;
		String cpuText;
		switch (SupportedTechnologies.values()[chosenTemplate].getClusterType()) {
		case "DockerCompose":
			reservationWord = "reservations";
			cpuText = "cpus";
			break;
		case "Kubernetes":
			reservationWord = "requests";
			cpuText = "cpu";
		default:
			reservationWord = "error";
			cpuText = "error";
			break;
		}

		for (DB db : dbs) {
			// create a group for each database
			Group group = new Group(main, SWT.READ_ONLY);
			group.setLayout(new GridLayout(2, false));
			group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			group.setText(db.getName());

			GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
			GridData gridDataChecks = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
			gridDataChecks.horizontalSpan = 2;

			// create a Container Model Object
			Container container = TyphonDLFactory.eINSTANCE.createContainer();
			container.setName(db.getName());
			ContainerType containerType = TyphonDLFactory.eINSTANCE.createContainerType();
			containerType.setName(SupportedTechnologies.values()[chosenTemplate].getContainerType());
			container.setType(containerType);
			Reference reference = TyphonDLFactory.eINSTANCE.createReference();
			reference.setReference(db);
			container.setDeploys(reference);

			// Ports
			Ports ports = TyphonDLFactory.eINSTANCE.createPorts();
			Key_Values port = TyphonDLFactory.eINSTANCE.createKey_Values();
			port.setName("target");
			String targetPort = properties.getProperty(db.getType().getName().toLowerCase() + ".port");
			port.setValue(targetPort);
			ports.getKey_values().add(port);
			container.setPorts(ports);

			new Label(group, NONE).setText("Container port: ");
			Text portText = new Text(group, SWT.BORDER);
			portText.setText(targetPort);
			portText.setToolTipText("This is the port that will be exposed inside the network/cluster");
			portText.setLayoutData(gridDataFields);
			portText.addModifyListener(e -> { // TODO
				port.setValue(portText.getText());
			});

			// Resources
			Button limitCheck = new Button(group, SWT.CHECK);
			limitCheck.setText("Set resource limits");
			limitCheck.setLayoutData(gridDataChecks);

			Composite limitComposite = new Composite(group, NONE);
			limitComposite.setLayout(new GridLayout(2, true));
			GridData limitData = new GridData(SWT.FILL, SWT.FILL, true, true);
			limitData.exclude = true;
			limitComposite.setLayoutData(limitData);

			Label limMemLabel = new Label(limitComposite, NONE);
			limMemLabel.setText("memory");
			Label limCPULabel = new Label(limitComposite, NONE);
			limCPULabel.setText(cpuText);

			Button reservationCheck = new Button(group, SWT.CHECK);
			reservationCheck.setText("Set resource " + reservationWord);
			reservationCheck.setLayoutData(gridDataChecks);

			Composite reservationComposite = new Composite(group, NONE);
			reservationComposite.setLayout(new GridLayout(2, true));
			GridData reservationData = new GridData(SWT.FILL, SWT.FILL, true, true);
			reservationData.exclude = true;
			reservationComposite.setLayoutData(reservationData);

			Label resMemLabel = new Label(reservationComposite, NONE);
			resMemLabel.setText("memory");
			Label resCPULabel = new Label(reservationComposite, NONE);
			resCPULabel.setText(cpuText);

			limitCheck.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					limitData.exclude = !limitCheck.getSelection();
					limitComposite.setVisible(limitCheck.getSelection());
					main.pack();
				}
			});
			reservationCheck.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					reservationData.exclude = !reservationCheck.getSelection();
					reservationComposite.setVisible(reservationCheck.getSelection());
					main.pack();
				}
			});

			/**
			 * Kubernetes: resources: limits: memory: "128Mi" cpu: "500m" requests, Docker
			 * Compose: deploy: resources: limits: cpus: '0.50' memory: 50M reservations:
			 * cpus: '0.25' memory: 20M
			 */

			result.put(db, container);
		}
		setControl(main);

	}

	/**
	 * Get pairs of DB and Container with input from wizard
	 * 
	 * @return A map with a Container for every DB
	 */
	public HashMap<DB, Container> getResult() {
		return result;
	}

}

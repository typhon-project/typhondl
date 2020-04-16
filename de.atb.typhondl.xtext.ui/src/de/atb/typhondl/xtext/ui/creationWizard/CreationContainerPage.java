package de.atb.typhondl.xtext.ui.creationWizard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
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
import de.atb.typhondl.xtext.typhonDL.Key_KeyValueList;
import de.atb.typhondl.xtext.typhonDL.Key_Values;
import de.atb.typhondl.xtext.typhonDL.Ports;
import de.atb.typhondl.xtext.typhonDL.Property;
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
	 * To have a Scrollbar, a minSize has to be set. Somehow the page's width is
	 * always 607, independent of what's given here
	 */
	private static final int WIDTH = 607;

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
		ScrolledComposite scrolling = new ScrolledComposite(parent, SWT.V_SCROLL);
		Composite main = new Composite(scrolling, SWT.NONE);
		scrolling.setContent(main);
		scrolling.setExpandVertical(true);
		main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		main.setLayout(new GridLayout(1, false));
		String reservationWord;
		String cpuText;
		String limMem;
		String limCPU;
		String resMem;
		String resCPU;
		switch (SupportedTechnologies.values()[chosenTemplate].getClusterType()) {
		case "DockerCompose":
			reservationWord = "reservations";
			cpuText = "cpus";
			limMem = "50M";
			limCPU = "'0.25'";
			resMem = "20M";
			resCPU = "'0.1'";
			break;
		case "Kubernetes":
			reservationWord = "requests";
			cpuText = "cpu";
			limMem = "\"128Mi\"";
			limCPU = "\"500m\"";
			resMem = "\"64Mi\"";
			resCPU = "\"250m\"";
			break;
		default:
			reservationWord = "error";
			cpuText = "error";
			limMem = "error";
			limCPU = "error";
			resMem = "error";
			resCPU = "error";
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

			// check if the database is external
			boolean externalDatabse = (db.getType() == null); // TODO

			if (externalDatabse) {
				container.setExternal(true);
				Key_Values address = TyphonDLFactory.eINSTANCE.createKey_Values();
				address.setName("address");
				address.setValue("testAddress");
				container.getProperties().add(address);

			} else {

				// deploy for docker swarm
				Key_KeyValueList deployList = TyphonDLFactory.eINSTANCE.createKey_KeyValueList();
				deployList.setName("deploy");

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
				portText.addModifyListener(e -> {
					port.setValue(portText.getText());
				});

				// Replicas
				Key_Values replicas = TyphonDLFactory.eINSTANCE.createKey_Values();
				replicas.setName("replicas");
				replicas.setValue("1");

				new Label(group, NONE).setText(replicas.getName() + ": ");
				Text replicasText = new Text(group, SWT.BORDER);
				replicasText.setText(replicas.getValue());
				replicasText.setLayoutData(gridDataFields);
				replicasText.addModifyListener(e -> {
					replicas.setValue(replicasText.getText());
					if (replicas.getValue().equals("1")) {
						switch (SupportedTechnologies.values()[chosenTemplate].getClusterType()) {
						case "DockerCompose":
							removePropertyFromList(deployList, replicas);
							if (deployList.getProperties().isEmpty()) {
								removePropertyFromContainer(container, deployList);
							}
							break;
						case "Kubernetes":
							removePropertyFromContainer(container, replicas);
						default:
							break;
						}
					} else {
						switch (SupportedTechnologies.values()[chosenTemplate].getClusterType()) {
						case "DockerCompose":
							addKeyValueToList(deployList, replicas);
							addListToContainer(container, deployList);
							break;
						case "Kubernetes":
							addKeyValueToContainer(container, replicas);
						default:
							break;
						}
					}
				});

				// Resources
				Key_KeyValueList resourceList = TyphonDLFactory.eINSTANCE.createKey_KeyValueList();
				resourceList.setName("resources");
				Key_KeyValueList limitList = TyphonDLFactory.eINSTANCE.createKey_KeyValueList();
				limitList.setName("limits");
				Key_Values limMemKeyValue = TyphonDLFactory.eINSTANCE.createKey_Values();
				limMemKeyValue.setName("memory");
				limMemKeyValue.setValue(limMem);
				Key_Values limCPUKeyValue = TyphonDLFactory.eINSTANCE.createKey_Values();
				limCPUKeyValue.setName(cpuText);
				limCPUKeyValue.setValue(limCPU);
				limitList.getProperties().add(limMemKeyValue);
				limitList.getProperties().add(limCPUKeyValue);
				Key_KeyValueList reservationList = TyphonDLFactory.eINSTANCE.createKey_KeyValueList();
				reservationList.setName(reservationWord);
				Key_Values resMemKeyValue = TyphonDLFactory.eINSTANCE.createKey_Values();
				resMemKeyValue.setName("memory");
				resMemKeyValue.setValue(resMem);
				Key_Values resCPUKeyValue = TyphonDLFactory.eINSTANCE.createKey_Values();
				resCPUKeyValue.setName(cpuText);
				resCPUKeyValue.setValue(resCPU);
				reservationList.getProperties().add(resMemKeyValue);
				reservationList.getProperties().add(resCPUKeyValue);

				// create Resource composite in each group
				GridData resourceGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
				resourceGridData.horizontalSpan = 2;
				Composite resourceComposite = new Composite(group, NONE);
				resourceComposite.setLayout(new GridLayout(1, false));
				resourceComposite.setLayoutData(resourceGridData);

				Button limitCheck = new Button(resourceComposite, SWT.CHECK);
				limitCheck.setText("Set resource " + limitList.getName());
				limitCheck.setLayoutData(gridDataChecks);
				// create invisible Limit Composite in each resource composite
				Composite limitComposite = new Composite(resourceComposite, NONE);
				limitComposite.setLayout(new GridLayout(2, false));
				GridData limitData = new GridData(SWT.FILL, SWT.FILL, true, true);
				limitData.exclude = true;
				limitComposite.setLayoutData(limitData);
				Label limMemLabel = new Label(limitComposite, NONE);
				limMemLabel.setText(limMemKeyValue.getName() + ": ");
				Text limMemText = new Text(limitComposite, SWT.BORDER);
				limMemText.setText(limMemKeyValue.getValue());
				limMemText.setLayoutData(gridDataFields);
				Label limCPULabel = new Label(limitComposite, NONE);
				limCPULabel.setText(limCPUKeyValue.getName() + ": ");
				Text limCPUText = new Text(limitComposite, SWT.BORDER);
				limCPUText.setText(limCPUKeyValue.getValue());
				limCPUText.setLayoutData(gridDataFields);

				Button reservationCheck = new Button(resourceComposite, SWT.CHECK);
				reservationCheck.setText("Set resource " + reservationList.getName());
				reservationCheck.setLayoutData(gridDataChecks);
				// create invisible Reservation Composite in each resource composite
				Composite reservationComposite = new Composite(resourceComposite, NONE);
				reservationComposite.setLayout(new GridLayout(2, false));
				GridData reservationData = new GridData(SWT.FILL, SWT.FILL, true, true);
				reservationData.exclude = true;
				reservationComposite.setLayoutData(reservationData);
				Label resMemLabel = new Label(reservationComposite, NONE);
				resMemLabel.setText(resMemKeyValue.getName() + ": ");
				Text resMemText = new Text(reservationComposite, SWT.BORDER);
				resMemText.setText(resMemKeyValue.getValue());
				resMemText.setLayoutData(gridDataFields);
				Label resCPULabel = new Label(reservationComposite, NONE);
				resCPULabel.setText(resCPUKeyValue.getName() + ": ");
				Text resCPUText = new Text(reservationComposite, SWT.BORDER);
				resCPUText.setText(resCPUKeyValue.getValue());
				resCPUText.setLayoutData(gridDataFields);

				limitCheck.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						// set the textfields visible, resize window
						limitData.exclude = !limitCheck.getSelection();
						limitComposite.setVisible(limitCheck.getSelection());
						main.setSize(main.computeSize(WIDTH, SWT.DEFAULT));
						scrolling.setMinSize(main.computeSize(WIDTH, SWT.DEFAULT));
						// add or remove objects from model
						if (limitCheck.getSelection()) {
							resourceList.getProperties().add(limitList);
							switch (SupportedTechnologies.values()[chosenTemplate].getClusterType()) {
							case "DockerCompose":
								addListToList(deployList, resourceList);
								addListToContainer(container, deployList);
								break;
							case "Kubernetes":
								addListToContainer(container, resourceList);
							default:
								break;
							}
						} else {
							switch (SupportedTechnologies.values()[chosenTemplate].getClusterType()) {
							case "DockerCompose":
								removePropertyFromList(resourceList, limitList);
								if (resourceList.getProperties().isEmpty()) {
									removePropertyFromList(deployList, resourceList);
									if (deployList.getProperties().isEmpty()) {
										removePropertyFromContainer(container, deployList);
									}
								}
								break;
							case "Kubernetes":
								removePropertyFromList(resourceList, limitList);
								if (resourceList.getProperties().isEmpty()) {
									removePropertyFromContainer(container, resourceList);
								}
							default:
								break;
							}
						}
					}
				});
				reservationCheck.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						// set the textfields visible, resize window
						reservationData.exclude = !reservationCheck.getSelection();
						reservationComposite.setVisible(reservationCheck.getSelection());
						main.setSize(main.computeSize(WIDTH, SWT.DEFAULT));
						scrolling.setMinSize(main.computeSize(WIDTH, SWT.DEFAULT));
						// add or remove objects from model
						if (reservationCheck.getSelection()) {
							resourceList.getProperties().add(reservationList);
							switch (SupportedTechnologies.values()[chosenTemplate].getClusterType()) {
							case "DockerCompose":
								addListToList(deployList, resourceList);
								addListToContainer(container, deployList);
								break;
							case "Kubernetes":
								addListToContainer(container, resourceList);
							default:
								break;
							}
						} else {
							switch (SupportedTechnologies.values()[chosenTemplate].getClusterType()) {
							case "DockerCompose":
								removePropertyFromList(resourceList, reservationList);
								if (resourceList.getProperties().isEmpty()) {
									removePropertyFromList(deployList, resourceList);
									if (deployList.getProperties().isEmpty()) {
										removePropertyFromContainer(container, deployList);
									}
								}
								break;
							case "Kubernetes":
								removePropertyFromList(resourceList, reservationList);
								if (resourceList.getProperties().isEmpty()) {
									removePropertyFromContainer(container, resourceList);
								}
							default:
								break;
							}
						}
					}
				});
			}
			result.put(db, container);
		}
		main.setSize(main.computeSize(WIDTH, SWT.DEFAULT));
		scrolling.setMinSize(main.computeSize(WIDTH, SWT.DEFAULT));
		setControl(scrolling);
	}

	/**
	 * Adds a {@link Key_Values} to a {@link Key_KeyValueList}. If the key already
	 * exists, only the value is added.
	 * 
	 * @param Key_KeyValueList The list to which the keyValue pair shall be added
	 * @param key_values       The keyValue pair to be added to the list
	 */
	private void addKeyValueToList(Key_KeyValueList listToAddTo, Key_Values key_values) {
		List<Property> list = listToAddTo.getProperties().stream()
				.filter(property -> property.getName().equalsIgnoreCase(key_values.getName()))
				.collect(Collectors.toList());
		if (list.size() == 1) {
			((Key_Values) list.get(0)).setValue(key_values.getValue());
		} else {
			listToAddTo.getProperties().add(key_values);
		}
	}

	/**
	 * Adds a {@link Key_Values} to a {@link Container}. If the key already exists,
	 * only the value is added.
	 * 
	 * @param container  The Container to which the keyValue pair shall be added
	 * @param key_values The keyValue pair to be added to the Container
	 */
	private void addKeyValueToContainer(Container container, Key_Values key_values) {
		List<Property> temp = container.getProperties().stream()
				.filter(property -> property.getName().equalsIgnoreCase(key_values.getName()))
				.collect(Collectors.toList());
		if (temp.isEmpty()) {
			container.getProperties().add(key_values);
		} else {
			((Key_Values) temp.get(0)).setValue(key_values.getValue());
		}
	}

	/**
	 * Removes a {@link Property} from a {@link Container} if it exists.
	 * 
	 * @param container The Container from which the Property shall be removed
	 * @param property  The Property to be removed from the Container
	 */
	private void removePropertyFromContainer(Container container, Property property) {
		List<Property> temp = container.getProperties().stream()
				.filter(prop -> prop.getName().equalsIgnoreCase(property.getName())).collect(Collectors.toList());
		if (!temp.isEmpty()) {
			container.getProperties().remove(temp.get(0));
		}
	}

	/**
	 * Removes a {@link Property} from a {@link Key_KeyValueList}
	 * 
	 * @param listToRemoveFrom
	 * @param propertyToBeRemoved
	 */
	protected void removePropertyFromList(Key_KeyValueList listToRemoveFrom, Property propertyToBeRemoved) {
		List<Property> temp = listToRemoveFrom.getProperties().stream()
				.filter(property -> property.getName().equalsIgnoreCase(propertyToBeRemoved.getName()))
				.collect(Collectors.toList());
		if (!temp.isEmpty()) {
			listToRemoveFrom.getProperties().remove(temp.get(0));
		}
	}

	/**
	 * Adds a {@link Key_KeyValueList} to a {@link Container}. If the list already
	 * exists, just the list's content is added.
	 * 
	 * @param container     The Container to which the list shall be added
	 * @param listToBeAdded The List to be added to the Container
	 */
	protected void addListToContainer(Container container, Key_KeyValueList listToBeAdded) {
		List<Property> temp = container.getProperties().stream()
				.filter(property -> property.getName().equalsIgnoreCase(listToBeAdded.getName()))
				.collect(Collectors.toList());
		if (!temp.isEmpty()) {
			((Key_KeyValueList) temp.get(0)).getProperties().addAll(listToBeAdded.getProperties());
		} else {
			container.getProperties().add(listToBeAdded);
		}
	}

	/**
	 * Adds a {@link Key_KeyValueList} to another {@link Key_KeyValueList}. If the
	 * list already exists, just the list's content is added.
	 * 
	 * @param listToAddTo   The list to which the other list shall be added
	 * @param listToBeAdded The list to be added to the other list
	 */
	protected void addListToList(Key_KeyValueList listToAddTo, Key_KeyValueList listToBeAdded) {
		List<Property> temp = listToAddTo.getProperties().stream()
				.filter(property -> property.getName().equalsIgnoreCase(listToBeAdded.getName()))
				.collect(Collectors.toList());
		if (temp.size() == 1) {
			((Key_KeyValueList) temp.get(0)).getProperties().addAll(listToBeAdded.getProperties());
		} else {
			listToAddTo.getProperties().add(listToBeAdded);
		}
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

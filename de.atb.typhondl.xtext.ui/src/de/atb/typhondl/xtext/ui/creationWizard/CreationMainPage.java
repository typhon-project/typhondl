package de.atb.typhondl.xtext.ui.creationWizard;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.ui.utilities.PropertiesLoader;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

/**
 * First page of the TyphonDL {@link CreateModelWizard}.
 * <li>The name of the TyphonDL model to be created is entered,</li>
 * <li>a technology template (i.e. Docker Compose or Kubernetes) is chosen,</li>
 * <li>the Typhon Data Analytics component can be activated and</li>
 * <li>the API IP address and port can be entered.</li>
 * 
 * @author flug
 *
 */
public class CreationMainPage extends MyWizardPage {

	/**
	 * URI to the selected ML model
	 */
	private URI MLmodelPath;

	/**
	 * The textfield to enter the TyphonDL model name
	 */
	private Text fileText;

	/**
	 * The Combo to choose the technology (i.e. Docker Compose or Kubernetes)
	 */
	private Combo templateCombo;

	/**
	 * int representation of the chosen technology template from enum
	 * {@link SupportedTechnologies}
	 */
	private int chosenTemplate;

	/**
	 * The entered name for the DL model to be created
	 */
	private String DLmodelName;

	/**
	 * The polystore.properties
	 */
	private Properties properties;

	/**
	 * The checkbox to activate the use of the Typhon Analytics component
	 */
	private Button checkbox;

	/**
	 * The textfield to enter the API IP address
	 */
	private Text hostText;

	/**
	 * The textfield to enter the API port
	 */
	private Text portText;

	/**
	 * Creates an instance of the {@link CreationMainPage}
	 * 
	 * @param pageName    the name of the page
	 * @param MLmodelPath the URI to the selected ML model
	 */
	protected CreationMainPage(String pageName, URI MLmodelPath) {
		super(pageName);
		this.MLmodelPath = MLmodelPath;
		try {
			this.properties = PropertiesLoader.loadProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createControl(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		main.setLayout(new GridLayout(2, false));
		createHeader(main);
		createCombo(main);
		createAdditions(main);
		createPolystoreSpecs(main);
		setControl(main);
	}

	/**
	 * Creates the first two rows of the main page:
	 * <li>The folder label to show where the DL model will be saved.</li>
	 * <li>The textfield to enter the DL model name</li>
	 * 
	 * @param main the composite in which the fields are created
	 */
	private void createHeader(Composite main) {
		setTitle("Create a TyphonDL model");
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		main.setLayout(layout);

		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);

		Label folderLabel = new Label(main, SWT.NONE);
		folderLabel.setText("Folder: ");
		Label folderText = new Label(main, SWT.NONE);
		folderText.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, true, false));
		folderText.setText(Paths.get(MLmodelPath).getParent().toString());

		Label fileLabel = new Label(main, SWT.NONE);
		fileLabel.setText("Name: ");
		fileText = new Text(main, SWT.BORDER);
		fileText.setLayoutData(gridData);
		fileText.setFocus();
		fileText.addModifyListener(e -> {
			validate();
			this.DLmodelName = fileText.getText();
		});
		validate();
	}

	/**
	 * Creates technology choosing combo and sets initial polystore.properties.<br>
	 * TODO change to not hardcoded?
	 * 
	 * @param main the composite in which the fields are created
	 */
	private void createCombo(Composite main) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		main.setLayout(layout);

		Label templateLabel = new Label(main, SWT.NONE);
		templateLabel.setText("Template: ");
		templateCombo = new Combo(main, SWT.READ_ONLY);
		List<String> itemList = new ArrayList<String>();
		for (SupportedTechnologies tech : SupportedTechnologies.values()) {
			itemList.add(tech.getDisplayedName());
			// templateCombo.setItem(tech.ordinal(), tech.getDisplayedName()); somehow
			// doesn't work
		}
		templateCombo.setItems(itemList.toArray(new String[itemList.size()]));
		templateCombo.setText(templateCombo.getItem(0));
		chosenTemplate = templateCombo.getSelectionIndex();
		templateCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				chosenTemplate = templateCombo.getSelectionIndex();
				String templateName = SupportedTechnologies.values()[chosenTemplate].getClusterType();
				if (templateName.equals("Kubernetes")) {
					checkbox.setEnabled(false);
					checkbox.setSelection(false);
					properties.setProperty("polystore.useAnalytics", String.valueOf(checkbox.getSelection()));
					properties.setProperty("ui.environment.API_HOST", "\"192.168.99.101\"");
					properties.setProperty("ui.environment.API_PORT", "\"30061\"");
					properties.setProperty("api.publishedPort", "30061");
					properties.setProperty("ui.publishedPort", "30075");
					hostText.setText(properties.getProperty("ui.environment.API_HOST"));
					portText.setText(properties.getProperty("ui.environment.API_PORT"));
				} else {
					checkbox.setEnabled(true);
					properties.setProperty("ui.environment.API_HOST", "localhost");
					properties.setProperty("ui.environment.API_PORT", "8080");
					properties.setProperty("api.publishedPort", "8080");
					properties.setProperty("ui.publishedPort", "4200");
					hostText.setText(properties.getProperty("ui.environment.API_HOST"));
					portText.setText(properties.getProperty("ui.environment.API_PORT"));
				}
			}
		});
	}

	/**
	 * Creates the checkbox to select Typhon Analytics component
	 * 
	 * @param main the composite in which the fields are created
	 */
	private void createAdditions(Composite main) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		main.setLayout(layout);

		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gridData.horizontalSpan = 2;

		checkbox = new Button(main, SWT.CHECK);
		checkbox.setText("Use Typhon Data Analytics");
		checkbox.setSelection(false);
		checkbox.setLayoutData(gridData);
		// TODO delete this when analytics can be used:
		checkbox.setEnabled(false);
		checkbox.setToolTipText("Analytics is under development and will be available in a future update");
		// checkbox.setToolTipText("Check if you want to include Data Analytics in your
		// deployment");
		checkbox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				properties.setProperty("polystore.useAnalytics", String.valueOf(checkbox.getSelection()));
			}
		});

	}

	/**
	 * Creates fields to enter the API specs
	 * 
	 * @param main the composite in which the fields are created
	 */
	private void createPolystoreSpecs(Composite main) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		main.setLayout(layout);

		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);

		Label hostLabel = new Label(main, SWT.NONE);
		hostLabel.setText("Api Host: ");
		hostText = new Text(main, SWT.BORDER);
		hostText.setLayoutData(gridData);
		hostText.setText(properties.getProperty("ui.environment.API_HOST"));
		hostText.addModifyListener(e -> properties.setProperty("ui.environment.API_HOST", hostText.getText()));

		Label portLabel = new Label(main, SWT.NONE);
		portLabel.setText("Api Port: ");
		portText = new Text(main, SWT.BORDER);
		portText.setLayoutData(gridData);
		portText.setText(properties.getProperty("ui.environment.API_PORT"));
		portText.addModifyListener(e -> {
			properties.setProperty("ui.environment.API_PORT", portText.getText());
			properties.setProperty("api.publishedPort", portText.getText().replaceAll("\"", ""));
		});
	}

	/**
	 * Checks if the name for the DL model is correct, i.e. not empty or already
	 * existent
	 */
	private void validate() {
		setStatus(null);
		if ("".equals(fileText.getText().trim())) { //$NON-NLS-1$
			setStatus(new Status(IStatus.ERROR, "NewFileWizard", "Name must not be empty")); //$NON-NLS-1$
			return;
		}
		Path filePath = Paths.get(MLmodelPath).getParent().resolve(fileText.getText() + ".tdl");
		if (Files.exists(filePath)) {
			setStatus(new Status(IStatus.WARNING, "NewFileWizard", //$NON-NLS-1$
					"File '" + fileText.getText() + ".tdl"
							+ "' already exists and will be overwritten if you continue."));
			return;
		}
	}

	/**
	 * 
	 * @return int representation of the chosen technology from
	 *         {@link SupportedTechnologies}
	 */
	public int getChosenTemplate() {
		return chosenTemplate;
	}

	/**
	 * 
	 * @return true if the Typhon Analytics component should be used, false
	 *         otherwise
	 */
	public boolean getUseAnalytics() {
		return Boolean.parseBoolean((String) properties.get("polystore.useAnalytics"));
	}

	/**
	 * 
	 * @return the entered DL model name
	 */
	public String getDLmodelName() {
		return DLmodelName;
	}

	/**
	 * 
	 * @return the polystore.properties depending on the chosen technology
	 */
	public Properties getProperties() {
		return properties;
	}

}

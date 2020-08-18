package de.atb.typhondl.xtext.ui.creationWizard;

/*-
 * #%L
 * de.atb.typhondl.xtext.ui
 * %%
 * Copyright (C) 2018 - 2020 ATB
 * %%
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 * #L%
 */

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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.ui.properties.PropertiesService;
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

    private static final String ANALYTICS_DEPLOYMENT_CONTAINED = "analytics.deployment.contained";

    private static final String ANALYTICS_KAFKA_URI = "analytics.kafka.uri";

    private static final String POLYSTORE_USE_ANALYTICS = "polystore.useAnalytics";

    private static final String ANALYTICS_DEPLOYMENT_CREATE = "analytics.deployment.create";

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
     * The chosen technology template from enum {@link SupportedTechnologies}
     */
    private SupportedTechnologies chosenTemplate;

    /**
     * The entered name for the DL model to be created
     */
    private String DLmodelName;

    /**
     * The polystore.properties
     */
    private Properties properties;

    /**
     * The textfield to enter the API IP address
     */
    private Text hostText;

    /**
     * The textfield to enter the API port
     */
    private Text portText;

    private Composite main;

    private boolean useAnalytics;
    private boolean createScripts;
    private boolean analyticsContained;
    private boolean useNLAE;

    private Text analyticsURIText;

    /**
     * Creates an instance of the {@link CreationMainPage}
     * 
     * @param pageName    the name of the page
     * @param MLmodelPath the URI to the selected ML model
     * @param properties
     */
    protected CreationMainPage(String pageName, URI MLmodelPath, Properties properties) {
        super(pageName);
        this.MLmodelPath = MLmodelPath;
        this.properties = properties;
        this.useAnalytics = false;
        this.createScripts = false;
        this.analyticsContained = true;
        this.useNLAE = false;
    }

    @Override
    public void createControl(Composite parent) {
        setTitle("Create a TyphonDL model");
        setDescription("From ML model " + MLmodelPath.getPath());
        main = new Composite(parent, SWT.NONE);
        main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        main.setLayout(new GridLayout(1, false));

        addGroups();

        setControl(main);
    }

    private void addGroups() {
        createMainGroup();
        createAnalyticsGroup();
        createNLAEGroup();
        createConnectionGroup();
        createScalingGroup();
    }

    private void createNLAEGroup() {
        GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);

        Group mainGroup = new Group(main, SWT.READ_ONLY);
        mainGroup.setLayout(new GridLayout(1, false));
        mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        mainGroup.setText("Typhon Natural Language Analysis Engine (NLAE)");

        Button nlae = new Button(mainGroup, SWT.CHECK);
        nlae.setText("Use Typhon NLAE (needs Docker Swarm)");
        nlae.setSelection(useNLAE);
        nlae.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                useNLAE = nlae.getSelection();
                properties.setProperty(PropertiesService.POLYSTORE_USENLAE, Boolean.toString(useNLAE));
            }
        });

    }

    private void createScalingGroup() {
        GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);

        Group mainGroup = new Group(main, SWT.READ_ONLY);
        mainGroup.setLayout(new GridLayout(2, false));
        mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        mainGroup.setText("Scaling");

        new Label(mainGroup, SWT.NONE).setText("API replicas: ");
        Text repAPI = new Text(mainGroup, SWT.BORDER);
        repAPI.setText("1");
        repAPI.setLayoutData(gridData);
        repAPI.addModifyListener(e -> properties.setProperty(PropertiesService.API_REPLICAS, repAPI.getText()));
        new Label(mainGroup, SWT.NONE).setText("QL server replicas: ");
        Text repQL = new Text(mainGroup, SWT.BORDER);
        repQL.setText("1");
        repQL.setLayoutData(gridData);
        repQL.addModifyListener(e -> properties.setProperty(PropertiesService.QLSERVER_REPLICAS, repQL.getText()));
    }

    private void createMainGroup() {
        GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);

        Group mainGroup = new Group(main, SWT.READ_ONLY);
        mainGroup.setLayout(new GridLayout(2, false));
        mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        mainGroup.setText("General");

        new Label(mainGroup, SWT.NONE).setText("Folder: ");
        Label folderText = new Label(mainGroup, SWT.NONE);
        folderText.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, true, false));
        folderText.setText(Paths.get(MLmodelPath).getParent().toString());

        Label fileLabel = new Label(mainGroup, SWT.NONE);
        fileLabel.setText("Name: ");
        fileText = new Text(mainGroup, SWT.BORDER);
        fileText.setLayoutData(gridData);
        fileText.setFocus();
        fileText.addModifyListener(e -> {
            validate();
            this.DLmodelName = fileText.getText();
        });
        validate();

        new Label(mainGroup, SWT.NONE).setText("Template: ");
        templateCombo = new Combo(mainGroup, SWT.READ_ONLY);
        List<String> itemList = new ArrayList<String>();
        for (SupportedTechnologies tech : SupportedTechnologies.values()) {
            itemList.add(tech.getDisplayedName());
            // templateCombo.setItem(tech.ordinal(), tech.getDisplayedName()); somehow
            // doesn't work
        }
        templateCombo.setItems(itemList.toArray(new String[itemList.size()]));
        templateCombo.setText(templateCombo.getItem(0));
        chosenTemplate = SupportedTechnologies.values()[templateCombo.getSelectionIndex()];
        templateCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                chosenTemplate = SupportedTechnologies.values()[templateCombo.getSelectionIndex()];
                if (chosenTemplate == SupportedTechnologies.Kubernetes) {
                    properties.setProperty(PropertiesService.UI_ENVIRONMENT_API_HOST, "\"192.168.99.101\"");
                    properties.setProperty(PropertiesService.UI_ENVIRONMENT_API_PORT, "\"30061\"");
                    properties.setProperty(PropertiesService.API_PUBLISHEDPORT, "30061");
                    properties.setProperty(PropertiesService.UI_PUBLISHEDPORT, "30075");
                    hostText.setText(properties.getProperty(PropertiesService.UI_ENVIRONMENT_API_HOST));
                    portText.setText(properties.getProperty(PropertiesService.UI_ENVIRONMENT_API_PORT));
                } else if (chosenTemplate == SupportedTechnologies.DockerCompose) {
                    properties.setProperty(PropertiesService.UI_ENVIRONMENT_API_HOST, "localhost");
                    properties.setProperty(PropertiesService.UI_ENVIRONMENT_API_PORT, "8080");
                    properties.setProperty(PropertiesService.API_PUBLISHEDPORT, "8080");
                    properties.setProperty(PropertiesService.UI_PUBLISHEDPORT, "4200");
                    hostText.setText(properties.getProperty(PropertiesService.UI_ENVIRONMENT_API_HOST));
                    portText.setText(properties.getProperty(PropertiesService.UI_ENVIRONMENT_API_PORT));
                }
                setKafkaProperties();
            }
        });
    }

    private void createAnalyticsGroup() {
        Group analyticsGroup = new Group(main, SWT.READ_ONLY);
        analyticsGroup.setLayout(new GridLayout(1, false));
        analyticsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        analyticsGroup.setText("Analytics Component");
        GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);

        Button checkbox = new Button(analyticsGroup, SWT.CHECK);
        checkbox.setText("Use Typhon Data Analytics");
        checkbox.setSelection(false);
        checkbox.setLayoutData(gridData);
        checkbox.setToolTipText("Check if you want to include Data Analytics in your deployment");

        Composite hidden = new Composite(analyticsGroup, SWT.NONE);
        hidden.setLayout(new GridLayout(1, false));
        GridData hiddenData = new GridData(SWT.FILL, SWT.FILL, true, true);
        hiddenData.exclude = true;
        hidden.setLayoutData(hiddenData);

        Button createScriptsCheck = new Button(hidden, SWT.CHECK);
        createScriptsCheck.setText("Create new Analytics Deployment Scripts");
        createScriptsCheck.setSelection(true);
        createScriptsCheck.setLayoutData(gridData);
        createScriptsCheck.setToolTipText(
                "Check if you want to get the Analytics deployment generated to run on the same machine as the Polystore");

        Button createScriptsCheckExternal = new Button(hidden, SWT.CHECK);
        createScriptsCheckExternal
                .setText("Create new separate Analytics Deployment Scripts - to run on a different machine");
        createScriptsCheckExternal.setSelection(false);
        createScriptsCheckExternal.setLayoutData(gridData);
        createScriptsCheckExternal.setToolTipText(
                "Check if you want to get the Analytics deployment generated to run on a different machine than the Polystore");

        Button useExistingCheck = new Button(hidden, SWT.CHECK);
        useExistingCheck.setText("Use existing Analytics component (no scripts get generated for Analytics)");
        useExistingCheck.setSelection(false);
        useExistingCheck.setLayoutData(gridData);
        useExistingCheck.setToolTipText("Check if you already have the Analytics component running somewhere");

        Button useEvolutionCheck = new Button(hidden, SWT.CHECK);
        useEvolutionCheck.setText("Use Typhon Continuous Evolution");
        useEvolutionCheck.setSelection(false);
        useEvolutionCheck.setLayoutData(gridData);
        useEvolutionCheck.setToolTipText("Only possible with contained Analytics component in this version");

        new Label(hidden, SWT.NONE).setText("Analytics URI: ");
        analyticsURIText = new Text(hidden, SWT.BORDER);
        analyticsURIText.setLayoutData(gridData);
        analyticsURIText.setText(getKafkaURI());
        analyticsURIText.addModifyListener(e -> {
            properties.setProperty(ANALYTICS_KAFKA_URI, analyticsURIText.getText());
        });

        // TODO necessary for displaying the useEvolutionCheck checkbox. Only remove if
        // fixed:
        new Label(hidden, SWT.NONE).setText("test");

        createScriptsCheck.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (createScriptsCheck.getSelection()) {
                    useExistingCheck.setSelection(false);
                    createScriptsCheckExternal.setSelection(false);
                    createScripts = true;
                    analyticsContained = true;
                } else {
                    useExistingCheck.setSelection(false);
                    createScriptsCheckExternal.setSelection(true);
                    useEvolutionCheck.setSelection(false);
                    properties.setProperty(PropertiesService.POLYSTORE_USEEVOLUTION, "false");
                    createScripts = true;
                    analyticsContained = false;
                }
                setKafkaProperties();
            }
        });
        createScriptsCheckExternal.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (createScriptsCheckExternal.getSelection()) {
                    useExistingCheck.setSelection(false);
                    createScriptsCheck.setSelection(false);
                    useEvolutionCheck.setSelection(false);
                    properties.setProperty(PropertiesService.POLYSTORE_USEEVOLUTION, "false");
                    createScripts = true;
                    analyticsContained = false;
                } else {
                    useExistingCheck.setSelection(false);
                    createScriptsCheck.setSelection(true);
                    createScripts = true;
                    analyticsContained = true;
                }
                setKafkaProperties();
            }
        });
        useExistingCheck.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (useExistingCheck.getSelection()) {
                    createScriptsCheckExternal.setSelection(false);
                    createScriptsCheck.setSelection(false);
                    useEvolutionCheck.setSelection(false);
                    properties.setProperty(PropertiesService.POLYSTORE_USEEVOLUTION, "false");
                    createScripts = false;
                    analyticsContained = false;
                } else {
                    createScriptsCheckExternal.setSelection(false);
                    createScriptsCheck.setSelection(true);
                    createScripts = true;
                    analyticsContained = true;
                }
                setKafkaProperties();
            }
        });
        useEvolutionCheck.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (useEvolutionCheck.getSelection()) {
                    if (!analyticsContained) {
                        useEvolutionCheck.setSelection(false);
                    } else {
                        properties.setProperty(PropertiesService.POLYSTORE_USEEVOLUTION, "true");
                    }
                } else {
                    properties.setProperty(PropertiesService.POLYSTORE_USEEVOLUTION, "false");
                }
            }
        });

        checkbox.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                useAnalytics = checkbox.getSelection();
                properties.setProperty(POLYSTORE_USE_ANALYTICS, String.valueOf(useAnalytics));
                createScripts = createScriptsCheck.getSelection();
                properties.setProperty(ANALYTICS_DEPLOYMENT_CREATE, String.valueOf(createScripts));
                if (useAnalytics) {
                    hiddenData.exclude = false;
                    hidden.setVisible(true);
                    analyticsGroup.getParent().layout(true);
                    setKafkaProperties();
                } else {
                    hiddenData.exclude = true;
                    hidden.setVisible(false);
                    analyticsGroup.getParent().layout(true);
                }
            }
        });
    }

    private void setKafkaProperties() {
        properties.setProperty(ANALYTICS_DEPLOYMENT_CREATE, String.valueOf(createScripts));
        properties.setProperty(ANALYTICS_DEPLOYMENT_CONTAINED, String.valueOf(analyticsContained));
        if (createScripts) {
            if (analyticsContained) {
                properties.setProperty(ANALYTICS_KAFKA_URI, getKafkaURI());
                analyticsURIText.setText(properties.getProperty(ANALYTICS_KAFKA_URI));
                analyticsURIText.setEditable(false);
            } else {
                properties.setProperty(ANALYTICS_KAFKA_URI, "localhost:29092");
                analyticsURIText.setText(properties.getProperty(ANALYTICS_KAFKA_URI));
                analyticsURIText.setEditable(true);
            }
        } else {
            properties.setProperty(ANALYTICS_KAFKA_URI, "localhost:29092");
            analyticsURIText.setText(properties.getProperty(ANALYTICS_KAFKA_URI));
            analyticsURIText.setEditable(true);
        }
    }

    private String getKafkaURI() {
        switch (chosenTemplate) {
        case DockerCompose:
            return "localhost:29092";
        case Kubernetes:
            return "typhon-cluster-kafka-bootstrap:9092";
        default:
            return "";
        }
    }

    /**
     * Creates fields to enter the API specs
     */
    private void createConnectionGroup() {
        GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        Group connectionGroup = new Group(main, SWT.READ_ONLY);
        connectionGroup.setLayout(new GridLayout(2, false));
        connectionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        connectionGroup.setText("API connection");

        new Label(connectionGroup, SWT.NONE).setText("Api Host: ");
        hostText = new Text(connectionGroup, SWT.BORDER);
        hostText.setLayoutData(gridData);
        hostText.setText(properties.getProperty(PropertiesService.UI_ENVIRONMENT_API_HOST));
        hostText.addModifyListener(
                e -> properties.setProperty(PropertiesService.UI_ENVIRONMENT_API_HOST, hostText.getText()));

        new Label(connectionGroup, SWT.NONE).setText("Api Port: ");
        portText = new Text(connectionGroup, SWT.BORDER);
        portText.setLayoutData(gridData);
        portText.setText(properties.getProperty(PropertiesService.UI_ENVIRONMENT_API_PORT));
        portText.addModifyListener(e -> {
            properties.setProperty(PropertiesService.UI_ENVIRONMENT_API_PORT, portText.getText());
            properties.setProperty(PropertiesService.API_PUBLISHEDPORT, portText.getText().replaceAll("\"", ""));
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
    public SupportedTechnologies getChosenTemplate() {
        return this.chosenTemplate;
    }

    /**
     * 
     * @return true if the Typhon Analytics component should be used, false
     *         otherwise
     */
    public boolean getUseAnalytics() {
        return useAnalytics;
    }

    public boolean getCreateScripts() {
        return createScripts;
    }

    public boolean getUseNLAE() {
        return useNLAE;
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

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

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.ui.modelUtils.ContainerService;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.technologies.ITechnology;
import de.atb.typhondl.xtext.ui.technologies.SupportedTechnologies;
import de.atb.typhondl.xtext.ui.utilities.InputField;

/**
 * Page of TyphonDL {@link CreateModelWizard}. Offers possibility to adjust
 * polystore components
 * 
 * @author flug
 *
 */
public class CreationPolystorePage extends MyWizardPage {

    private Properties properties;
    private Composite main;

    // Maps for validation
    private HashMap<Text, String> resourceFields;
    private HashMap<Text, String> scalingFields;
    private HashMap<Text, String> portFields;

    private ITechnology chosenTechnology;
    private Composite hidden;
    private GridData hiddenData;

    private static final int pageWidth = 607;

    /**
     * Creates an instance of {@link CreationPolystorePage}
     * 
     * @param pageName         name of the page
     * @param properties       properties, updated from main page
     * @param chosenTechnology DockerCompose, DockerSwatm or Kubernetes
     */
    protected CreationPolystorePage(String pageName, Properties properties, ITechnology chosenTechnology) {
        super(pageName);
        this.properties = properties;
        this.resourceFields = new HashMap<>();
        this.scalingFields = new HashMap<>();
        this.portFields = new HashMap<>();
        this.chosenTechnology = chosenTechnology;
    }

    @Override
    public void createControl(Composite parent) {
        setTitle("Configure Polystore Components");
        setDescription("API, UI, QL Server");
        ScrolledComposite scrolling = new ScrolledComposite(parent, SWT.V_SCROLL);
        main = new Composite(scrolling, SWT.NONE);
        scrolling.setContent(main);
        scrolling.setExpandVertical(true);
        scrolling.setExpandHorizontal(true);
        main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        main.setLayout(new GridLayout(1, false));

        ResourceEditor editor = new ResourceEditor();
        createQLGroup(editor.getResourceFieldsQL());
        createAPIGroup(editor.getResourceFieldsAPI());
        createUIGroup();

        main.setSize(main.computeSize(pageWidth, SWT.DEFAULT));
        scrolling.setMinSize(main.computeSize(pageWidth, SWT.DEFAULT));
        validate();
        setControl(scrolling);

    }

    /**
     * Field(s) for
     * <li>published port</li>
     */
    private void createUIGroup() {
        Group uiGroup = createGroup("UI settings", main);
        GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        createPortFields(uiGroup, gridData, new InputField("Published port: ", PropertiesService.UI_PUBLISHEDPORT));
    }

    private void createPortFields(Group group, GridData gridData, InputField inputField) {
        Text text = createFields(group, gridData, inputField);
        portFields.put(text, inputField.propertyName);
    }

    /**
     * Field(s) for
     * <li>published port</li>
     * <li>scaling</li>
     * <li>resources</li>
     * 
     * @param list resource input fields
     */
    private void createAPIGroup(List<InputField> list) {
        Group apiGroup = createGroup("API settings", main);
        GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        hidden = new Composite(apiGroup, SWT.NONE);
        hidden.setLayout(new GridLayout(2, false));
        hiddenData = new GridData(SWT.FILL, SWT.FILL, true, true);
        hiddenData.exclude = !chosenTechnology.canDoStatelessReplication();
        hiddenData.horizontalSpan = 2;
        hidden.setLayoutData(hiddenData);
        createScalingFields(hidden, "API", PropertiesService.API_REPLICAS);
        createPortFields(apiGroup, gridData, new InputField("Published port: ", PropertiesService.API_PUBLISHEDPORT));
        Group resourceGroup = createGroupInGroup("Resources (can be left empty)", apiGroup);
        for (InputField inputField : list) {
            createResourceFields(resourceGroup, gridData, inputField);
        }
    }

    /**
     * Field(s) for
     * <li>scaling</li>
     * <li>resources</li>
     * <li>timeZone</li>
     * 
     * @param list resource input fields
     */
    private void createQLGroup(List<InputField> list) {
        Group qlGroup = createGroup("QL Settings", main);
        GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        createScalingFields(qlGroup, "QL Server", PropertiesService.QLSERVER_REPLICAS);
        Group resourceGroup = createGroupInGroup("Resources (can be left empty)", qlGroup);
        for (InputField inputField : list) {
            createResourceFields(resourceGroup, gridData, inputField);
        }
        new Label(qlGroup, NONE).setText("Timezone");
        Combo combo = new Combo(qlGroup, SWT.READ_ONLY);
        ArrayList<String> timeZones = new ArrayList<>();
        timeZones.addAll(ZoneId.getAvailableZoneIds());
        Collections.sort(timeZones);
        combo.setItems(timeZones.toArray(new String[0]));
        combo.setText(ZoneId.systemDefault().getId());
        properties.setProperty(PropertiesService.QLSERVER_TIMEZONE, combo.getText());
        combo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                properties.setProperty(PropertiesService.QLSERVER_TIMEZONE, combo.getText());
            }
        });
    }

    private void createResourceFields(Group resourceGroup, GridData gridData, InputField inputField) {
        Text text = createFields(resourceGroup, gridData, inputField);
        this.resourceFields.put(text, inputField.propertyName);
    }

    private Text createFields(Group group, GridData gridData, InputField inputField) {
        // TODO put in hidden composite, activate with checkbox
        new Label(group, NONE).setText(inputField.label);
        Text text = new Text(group, SWT.BORDER);
        text.setText(properties.getProperty(inputField.propertyName));
        text.setLayoutData(gridData);
        text.addModifyListener(e -> {
            properties.setProperty(inputField.propertyName, text.getText());
            validate();
        });
        return text;
    }

    private Group createGroupInGroup(String name, Group parent) {
        Group group = createGroup(name, parent);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 2;
        group.setLayoutData(gridData);
        return group;
    }

    private Group createGroup(String name, Composite parent) {
        Group group = new Group(parent, SWT.READ_ONLY);
        group.setLayout(new GridLayout(2, false));
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        group.setText(name);
        return group;
    }

    private void createScalingFields(Composite hidden, String component, String property) {
        GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        new Label(hidden, SWT.NONE).setText(component + " replicas: ");
        Text text = new Text(hidden, SWT.BORDER);
        text.setText("1");
        text.setLayoutData(gridData);
        text.addModifyListener(e -> {
            properties.setProperty(property, text.getText());
            validate();
        });
        this.scalingFields.put(text, property);
    }

    private void validate() {
        setStatus(null);
        checkMemorySyntax(getTexts("memory"));
        checkCPUSyntax(getTexts("cpu"));
        if (chosenTechnology.getType() == SupportedTechnologies.DockerCompose) {
            for (Text text : resourceFields.keySet()) {
                if (!text.getText().isEmpty()) {
                    setStatus(new Status(IStatus.WARNING, "Wizard",
                            "Setting resources requires at least Docker Compose version 1.27.0 or Docker Swarm"));
                }
            }
        }
        for (Text text : scalingFields.keySet()) {
            int parseInt = 0;
            try {
                parseInt = Integer.parseInt(text.getText());
            } catch (NumberFormatException exp) {
                raiseError("replication");
            }
            if (!this.chosenTechnology.canDoStatelessReplication() && parseInt != 1) {
                setStatus(new Status(IStatus.WARNING, "Wizard",
                        "Setting stateless replication may lead to errors when using "
                                + chosenTechnology.displayedName()));
            }
            if (parseInt == 0) {
                raiseError("replication");
            }
        }
        for (Text text : portFields.keySet()) {
            if (!ContainerService.isPortValidRange(text.getText(), chosenTechnology)) {
                setStatus(new Status(IStatus.ERROR, "Wizard",
                        "Choose a port between " + chosenTechnology.minPort() + " and " + chosenTechnology.maxPort()));
            }
        }
    }

    private void checkCPUSyntax(ArrayList<Text> texts) {
        Pattern pattern = Pattern.compile("(0)(\\.)[1-9]+");
        for (Text text : texts) {
            if (!text.getText().isEmpty()) {
                Matcher matcher = pattern.matcher(text.getText());
                if (!matcher.matches()) {
                    raiseSyntaxError("for CPU is 0.x+");
                }
            }
        }
    }

    private void raiseSyntaxError(String string) {
        setStatus(new Status(IStatus.ERROR, "Wizard", "Correct Syntax " + string));
    }

    private void checkMemorySyntax(ArrayList<Text> texts) {
        Pattern pattern = Pattern.compile("[0-9]+[M]");
        for (Text text : texts) {
            if (!text.getText().isEmpty()) {
                Matcher matcher = pattern.matcher(text.getText());
                if (!matcher.matches()) {
                    raiseSyntaxError("for Memory is (x+)M");
                }
            }
        }
    }

    private ArrayList<Text> getTexts(String string) {
        ArrayList<Text> texts = new ArrayList<>();
        for (Text text : resourceFields.keySet()) {
            if (resourceFields.get(text).contains(string)) {
                texts.add(text);
            }
        }
        return texts;
    }

    private void raiseError(String context) {
        setStatus(new Status(IStatus.ERROR, "Wizard", "Please set a value bigger than 0 as " + context));

    }

    private class ResourceEditor {
        public List<InputField> resourceFieldsQL = Arrays.asList(
                new InputField("qlserver.limit.memory: ", PropertiesService.QLSERVER_LIMIT_MEMORY),
                new InputField("qlserver.limit.cpu: ", PropertiesService.QLSERVER_LIMIT_CPU),
                new InputField("qlserver.reservation.memory: ", PropertiesService.QLSERVER_RESERVATION_MEMORY),
                new InputField("qlserver.reservation.cpu: ", PropertiesService.QLSERVER_RESERVATION_CPU));

        public List<InputField> resourceFieldsAPI = Arrays.asList(
                new InputField("api.limit.memory: ", PropertiesService.API_LIMIT_MEMORY),
                new InputField("api.limit.cpu: ", PropertiesService.API_LIMIT_CPU),
                new InputField("api.reservation.memory: ", PropertiesService.API_RESERVATION_MEMORY),
                new InputField("api.reservation.cpu: ", PropertiesService.API_RESERVATION_CPU));

        public List<InputField> getResourceFieldsQL() {
            return resourceFieldsQL;
        }

        public List<InputField> getResourceFieldsAPI() {
            return resourceFieldsAPI;
        }
    }

    public void updateData(Properties properties, ITechnology chosenTechnology) {
        this.properties = properties;
        final boolean canDoStatelessReplication = chosenTechnology.canDoStatelessReplication();
        if (this.chosenTechnology.canDoStatelessReplication() != canDoStatelessReplication) {
            hidden.setVisible(canDoStatelessReplication);
            hiddenData.exclude = !canDoStatelessReplication;
        }
        setStatus(null);
        this.chosenTechnology = chosenTechnology;
        if (this.isControlCreated()) {
            for (Text text : this.resourceFields.keySet()) {
                text.setText(properties.getProperty(this.resourceFields.get(text)));
            }
            for (Text text : this.portFields.keySet()) {
                text.setText(properties.getProperty(this.portFields.get(text)));
            }
        }
        main.layout(true);
        main.setSize(main.computeSize(pageWidth, SWT.DEFAULT));
        ((ScrolledComposite) main.getParent()).setMinSize(main.computeSize(pageWidth, SWT.DEFAULT));
        validate();
    }
}

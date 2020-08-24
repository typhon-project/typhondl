package de.atb.typhondl.xtext.ui.refactoring;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.xtext.EcoreUtil2;

import de.atb.typhondl.xtext.typhonDL.ClusterType;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Key_Values;
import de.atb.typhondl.xtext.typhonDL.Ports;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.modelUtils.ContainerService;
import de.atb.typhondl.xtext.ui.modelUtils.ModelService;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

public class ClusterTypeDialog extends StatusDialog {

    private Properties properties;
    private DeploymentModel model;
    private ClusterType oldClusterType;
    private ClusterType newClusterType;
    private ArrayList<Key_Values> portList;

    public ClusterTypeDialog(Shell parent, Properties properties, DeploymentModel model, ClusterType clusterType) {
        super(parent);
        this.properties = properties;
        this.model = model;
        this.oldClusterType = clusterType;
        this.newClusterType = EcoreUtil.copy(oldClusterType);
        this.portList = new ArrayList<>();
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite main = new Composite(parent, parent.getStyle());
        main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        main.setLayout(new GridLayout(2, false));
        GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, true);

        new Label(main, SWT.NONE).setText("clustertype: ");
        Combo typeCombo = new Combo(main, SWT.READ_ONLY);
        List<String> itemList = new ArrayList<String>();
        for (SupportedTechnologies tech : SupportedTechnologies.values()) {
            itemList.add(tech.getDisplayedName());
            // templateCombo.setItem(tech.ordinal(), tech.getDisplayedName()); somehow
            // doesn't work
        }
        typeCombo.setItems(itemList.toArray(new String[0]));
        typeCombo.setText(typeCombo.getItem(ModelService.getSupportedTechnology(oldClusterType).ordinal()));
        typeCombo.addModifyListener(e -> {
            newClusterType.setName(SupportedTechnologies.values()[typeCombo.getSelectionIndex()].name());
            validatePorts();
        });

        new Label(main, SWT.NONE).setText("API host: ");
        Text apiHostText = new Text(main, SWT.BORDER);
        apiHostText.setText(properties.getProperty(PropertiesService.API_HOST));
        apiHostText.setLayoutData(gridDataFields);
        apiHostText.addModifyListener(e -> properties.setProperty(PropertiesService.API_HOST, apiHostText.getText()));

        Key_Values apiPort = TyphonDLFactory.eINSTANCE.createKey_Values();
        apiPort.setName("apiPort");
        apiPort.setValue(properties.getProperty(PropertiesService.API_PUBLISHEDPORT));
        portList.add(apiPort);
        new Label(main, SWT.NONE).setText("API published port: ");
        Text apiPortText = new Text(main, SWT.BORDER);
        apiPortText.setText(properties.getProperty(PropertiesService.API_PUBLISHEDPORT));
        apiPortText.setLayoutData(gridDataFields);
        apiPortText.addModifyListener(e -> {
            properties.setProperty(PropertiesService.API_PUBLISHEDPORT, apiPortText.getText());
            apiPort.setValue(apiPortText.getText());
            validatePorts();
        });

        EcoreUtil2.getAllContentsOfType(model, Ports.class).stream().flatMap(ports -> ports.getKey_values().stream())
                .filter(port -> port.getName().equalsIgnoreCase("published")).forEach(port -> {
                    new Label(main, SWT.NONE)
                            .setText(((Container) port.eContainer().eContainer()).getName() + " published port: ");
                    Text text = new Text(main, SWT.BORDER);
                    text.setText(port.getValue());
                    text.setLayoutData(gridDataFields);
                    portList.add(port);
                    text.addModifyListener(e -> {
                        port.setValue(text.getText());
                        validatePorts();
                    });
                });
        validatePorts();
        return main;
    }

    private void validatePorts() {
        this.updateStatus(new Status(IStatus.OK, "Change clusterType", ""));
        for (Key_Values port : portList) {
            if (ModelService.getSupportedTechnology(newClusterType) == SupportedTechnologies.Kubernetes
                    && !ContainerService.isPortInKubernetesRange(port.getValue())) {
                this.updateStatus(
                        new Status(IStatus.ERROR, "Change clusterType", "Choose port between 30000 and 32767"));
            }
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public ClusterType getClusterType() {
        return newClusterType;
    }

}

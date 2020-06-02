package de.atb.typhondl.xtext.ui.wizardPageAreas;

import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.Key_Values;
import de.atb.typhondl.xtext.typhonDL.Ports;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;

public class PortArea extends Area {

    public PortArea(DB db, Container container, int chosenTechnology, Composite parent, Properties properties) {
        super(db, container, chosenTechnology, parent, "Ports", properties);
    }

    @Override
    public void createArea() {
        if (!db.isExternal()) {
            if (group == null) {
                createGroup("Ports");
            }
            GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            String targetPort = "";
            Key_Values port;
            if (this.container.getPorts() == null) {
                Ports ports = TyphonDLFactory.eINSTANCE.createPorts();
                port = TyphonDLFactory.eINSTANCE.createKey_Values();
                port.setName("target");
                targetPort = properties.getProperty(db.getType().getName().toLowerCase() + ".port");
                port.setValue(targetPort);
                ports.getKey_values().add(port);
                container.setPorts(ports);
            } else {
                port = container.getPorts().getKey_values().stream()
                        .filter(key -> key.getName().equalsIgnoreCase("target")).findFirst().orElse(null);
                if (port != null) {
                    targetPort = port.getValue();
                }
            }

            new Label(group, SWT.NONE).setText("Container port: ");
            Text portText = new Text(group, SWT.BORDER);
            portText.setText(targetPort);
            portText.setToolTipText("This is the port that will be exposed inside the network/cluster");
            portText.setLayoutData(gridDataFields);
            portText.addModifyListener(e -> {
                port.setValue(portText.getText());
            });
        }
    }

}

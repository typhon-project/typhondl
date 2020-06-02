package de.atb.typhondl.xtext.ui.wizardPageAreas;

import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

public class ReplicaArea extends Area {

    public ReplicaArea(DB db, Container container, int chosenTechnology, Composite parent, Properties properties) {
        super(db, container, chosenTechnology, parent, "Replication", properties);
    }

    @Override
    public void createArea() {
        if (!db.isExternal() && !getReplicationProperty().isEmpty()) {
            switch (getReplicationProperty()) {
            case "backup":
                createBackupArea();
                break;
            case "multi":
                createMultiPrimaryArea();
                break;
            default:
                break;
            }
        }
    }

    private String getReplicationProperty() {
        String propertyName = db.getType().getName().toLowerCase() + ".replication" + "."
                + SupportedTechnologies.values()[chosenTechnology].getClusterType().toLowerCase();
        return properties.getProperty(propertyName) != null ? properties.getProperty(propertyName) : "";
    }

    private void createMultiPrimaryArea() {
        new Label(group, SWT.NONE).setText("multi");

    }

    private void createBackupArea() {
        new Label(group, SWT.NONE).setText("backup");

    }

}

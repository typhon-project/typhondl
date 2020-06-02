package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.HelmList;
import de.atb.typhondl.xtext.typhonDL.Property;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

public class HelmArea extends Area {

    public HelmArea(DB db, Container container, int chosenTechnology, Composite parent) {
        super(db, container, chosenTechnology, parent, "Helm Charts");
    }

    @Override
    public void createArea() {
        if (SupportedTechnologies.values()[chosenTechnology].getClusterType().equalsIgnoreCase("Kubernetes")
                && db.getHelm() != null) {
            if (group == null) {
                createGroup("Helm Charts");
            }
            HelmList helmList = db.getHelm();
            GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            new Label(group, SWT.NONE).setText("Repository Address:");
            Text addressText = new Text(group, SWT.BORDER);
            addressText.setText(helmList.getRepoAddress());
            addressText.setLayoutData(gridDataFields);
            addressText.addModifyListener(e -> helmList.setRepoAddress(addressText.getText()));
            new Label(group, SWT.NONE).setText("Repository Name:");
            Text repoNameText = new Text(group, SWT.BORDER);
            repoNameText.setText(helmList.getRepoName());
            repoNameText.setLayoutData(gridDataFields);
            repoNameText.addModifyListener(e -> helmList.setRepoName(repoNameText.getText()));
            new Label(group, SWT.NONE).setText("Chart Name:");
            Text nameText = new Text(group, SWT.BORDER);
            nameText.setText(helmList.getChartName());
            nameText.setLayoutData(gridDataFields);
            nameText.addModifyListener(e -> helmList.setChartName(nameText.getText()));
            HashMap<String, Property> properties = new HashMap<>();
            for (Property property : helmList.getParameters()) {
                addPropertyToList(property.getName(), property, properties);
            }
            if (!helmList.getParameters().isEmpty()) {
                addPropertyFieldsToGroup(group, properties);
            }
        }
    }
}

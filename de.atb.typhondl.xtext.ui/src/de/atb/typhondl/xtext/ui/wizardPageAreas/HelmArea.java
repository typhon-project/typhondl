package de.atb.typhondl.xtext.ui.wizardPageAreas;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.HelmList;
import de.atb.typhondl.xtext.typhonDL.Key_Values;
import de.atb.typhondl.xtext.typhonDL.Property;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

public class HelmArea extends Area {

    public HelmArea(DB db, int chosenTechnology, Composite parent) {
        super(db, null, chosenTechnology, parent, "Helm Charts", null);
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
            new Label(group, SWT.NONE).setText("valuesFile:");
            Text valueText = new Text(group, SWT.BORDER);
            valueText.setText(helmList.getRepoName());
            valueText.setLayoutData(gridDataFields);
            valueText.setToolTipText("give the path to your own values.yaml");
            valueText.addModifyListener(e -> {
                if (!valueText.getText().equalsIgnoreCase(helmList.getRepoName())) {
                    if (hasValuesFile(helmList)) {
                        updateValuesFile(helmList, valueText.getText());
                    } else {
                        helmList.getParameters().add(createValueFileKey_Values(valueText.getText()));
                    }
                } else {
                    removeValuesFile(helmList);
                }
            });
            for (Property property : helmList.getParameters()) {
                if (!property.equals(getValuesFile(helmList))) {
                    addPropertyToList(property.getName(), property, properties);
                }
            }
            if (!helmList.getParameters().isEmpty()) {
                addPropertyFieldsToGroup(group, properties);
            }
        }
    }

    private void removeValuesFile(HelmList helmList) {
        if (hasValuesFile(helmList)) {
            helmList.getParameters().remove(getValuesFile(helmList));
        }
    }

    private void updateValuesFile(HelmList helmList, String text) {
        Key_Values valuesFile = getValuesFile(helmList);
        if (valuesFile != null) {
            valuesFile.setValue(text);
        }
    }

    private Key_Values getValuesFile(HelmList helmList) {
        return (Key_Values) helmList.getParameters().stream()
                .filter(parameter -> parameter.getName().equalsIgnoreCase("valuesFile")).findFirst().orElse(null);
    }

    private boolean hasValuesFile(HelmList helmList) {
        return getValuesFile(helmList) != null;
    }

    private Key_Values createValueFileKey_Values(String text) {
        Key_Values valuesFile = TyphonDLFactory.eINSTANCE.createKey_Values();
        valuesFile.setName("valuesFile");
        valuesFile.setValue(text);
        return valuesFile;
    }

}

package de.atb.typhondl.xtext.ui.refactoring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

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

import de.atb.typhondl.xtext.typhonDL.ClusterType;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.utilities.InputField;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

public class ClusterTypeDialog extends StatusDialog {

    private Properties properties;
    private DeploymentModel model;
    private ClusterType clusterType;

    public ClusterTypeDialog(Shell parent, Properties properties, DeploymentModel model, ClusterType clusterType) {
        super(parent);
        this.properties = properties;
        this.model = model;
        this.clusterType = clusterType;
    }

    public class ClusterTypeConfigEditor {
        public List<InputField> fields = Arrays.asList(new InputField("API host: ", PropertiesService.API_HOST),
                new InputField("API port: ", PropertiesService.API_PUBLISHEDPORT));
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
        typeCombo.addModifyListener(
                e -> clusterType.setName(SupportedTechnologies.values()[typeCombo.getSelectionIndex()].name()));
        for (InputField inputField : new ClusterTypeConfigEditor().fields) {
            new Label(main, SWT.NONE).setText(inputField.label);
            Text text = new Text(main, SWT.BORDER);
            text.setText(properties.getProperty(inputField.propertyName));
            text.setLayoutData(gridDataFields);
            text.addModifyListener(e -> {
                properties.setProperty(inputField.propertyName, text.getText());
            });
        }
        return main;
    }

    public Properties getProperties() {
        return properties;
    }

}

package de.atb.typhondl.xtext.ui.refactoring;

import java.util.Properties;

import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.ui.modelUtils.ModelService;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.utilities.InputField;
import de.atb.typhondl.xtext.ui.utilities.KafkaConfigEditor;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

public class ChangeAnalyticsDialog extends StatusDialog {

    private Properties properties;
    private Shell shell;
    private SupportedTechnologies clusterType;
    private boolean analyticsContained;

    public ChangeAnalyticsDialog(Shell shell, Properties properties, SupportedTechnologies clusterType,
            boolean analyticsContained) {
        super(shell);
        this.setTitle("Change Analytics Component");
        this.properties = properties;
        this.shell = shell;
        this.clusterType = clusterType;
        this.analyticsContained = analyticsContained;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite main = new Composite(parent, parent.getStyle());
        main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        main.setLayout(new GridLayout(2, false));
        GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, true);

        new Label(main, SWT.NONE).setText("Kafka URI: ");
        Text kafkaURIText = new Text(main, SWT.BORDER);
        kafkaURIText.setText(getKafkaUri());
//        kafkaURIText.setLayoutData(gridDataFields);
        properties.setProperty(PropertiesService.ANALYTICS_KAFKA_URI, kafkaURIText.getText());
        kafkaURIText.addModifyListener(
                e -> properties.setProperty(PropertiesService.ANALYTICS_KAFKA_URI, kafkaURIText.getText()));
        KafkaConfigEditor editor = new KafkaConfigEditor();
        for (InputField inputField : editor.getInputFields(clusterType)) {
            new Label(main, SWT.NONE).setText(inputField.label);
            Text text = new Text(main, SWT.BORDER);
            text.setText(properties.getProperty(inputField.propertyName));
            text.setLayoutData(gridDataFields);
            text.addModifyListener(e -> properties.setProperty(inputField.propertyName, text.getText()));
        }
        return main;
    }

    private String getKafkaUri() {
        if (analyticsContained) {
            return ModelService.getKafkaInternalURI(clusterType);
        } else {
            return properties.getProperty(PropertiesService.ANALYTICS_KAFKA_URI);
        }
    }

    public Properties getProperties() {
        return properties;
    }

}

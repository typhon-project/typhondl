package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CreationAnalyticsPage extends MyWizardPage {

	private Properties properties;

	protected CreationAnalyticsPage(String pageName, Properties properties) {
		super(pageName);
		this.properties = properties;
	}

	public class InputField {
		public final String label;
		public final String propertyName;

		public InputField(String label, String propertyName) {
			this.label = label;
			this.propertyName = propertyName;
		}
	}

	public class KafkaConfigEditor {
		public List<InputField> fields = Arrays.asList(new InputField("Zookeeper Port: ", "analytics.zookeeper.publishedPort"),
				new InputField("Kafka Port: ", "analytics.kafka.publishedPort"),
				new InputField("Kafka Listeners: ", "analytics.kafka.listeners"),
				new InputField("Kafka Inter Broker Listener Name: ", "analytics.kafka.listenerName"));
	}

	@Override
	public void createControl(Composite parent) {

		setTitle("Configure Data Analytics");
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		main.setLayout(new GridLayout(2, false));
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		
		KafkaConfigEditor editor = new KafkaConfigEditor();

		for (InputField inputField : editor.fields) {
			new Label(main, NONE).setText(inputField.label);
			Text text = new Text(main, SWT.BORDER);
			text.setText(properties.getProperty(inputField.propertyName));
			text.setLayoutData(gridData);
			text.addModifyListener(e -> {
				properties.setProperty(inputField.propertyName, text.getText());
			});
		}

		setControl(main);
	}

	public Properties getProperties() {
		return properties;
	}

}

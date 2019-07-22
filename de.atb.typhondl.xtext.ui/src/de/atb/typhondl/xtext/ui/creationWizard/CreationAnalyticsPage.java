package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CreationAnalyticsPage extends MyWizardPage {

	private HashMap<String, InputField> analyticsSettings;

	protected CreationAnalyticsPage(String pageName) {
		super(pageName);
		this.analyticsSettings = new HashMap<String, InputField>();
	}

	public class InputField {
		public final String label;
		public final String name;
		public String value;
		public final String defaultValue;

		public InputField(String label, String name, String defaultValue) {
			this.label = label;
			this.name = name;
			this.defaultValue = defaultValue;
		}
	}

	public class KafkaConfigEditor {
		public List<InputField> fields = Arrays.asList(
				new InputField("Zookeeper Port: ", "zookeeperPort", "2181"),
				new InputField("Kafka Port: ", "kafkaPort", "9092"),
				new InputField("Kafka Listeners: ", "kafkaListeners", "29092, 29093"),
				new InputField("Kafka Inter Broker Listener Name: ", "kafkaListenerName", "PLAINTEXT"));
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
			text.setText(inputField.defaultValue);
			text.setLayoutData(gridData);
			inputField.value = inputField.defaultValue;
			analyticsSettings.put(inputField.name, inputField);
			text.addModifyListener(e -> {
				inputField.value = text.getText();
			});
		}

		setControl(main);
	}

	public HashMap<String, InputField> getAnalyticsSettings() {
		return analyticsSettings;
	}

}

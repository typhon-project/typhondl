package de.atb.typhondl.xtext.ui.creationWizard;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CreationAnalyticsPage extends MyWizardPage {
	
	private HashMap<String, String> analyticsSettings;

	protected CreationAnalyticsPage(String pageName) {
		super(pageName);
		this.analyticsSettings = new HashMap<String,String>();
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
				new InputField("Kafka Port: ", "kafkaPort"),
				new InputField("Kafka Listeners: ", "kafkaListeners"),
				new InputField("Kafka Inter Broker Listener Name: ", "kafkaListenerName")
				);
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
			Text text =  new Text(main, SWT.BORDER);
			text.setText(string);
		}

		new Label(main, NONE).setText("Zookeeper Port: ");
		Text zookeeperPort = new Text(main, SWT.BORDER);
		zookeeperPort.setText("2181");
		zookeeperPort.setLayoutData(gridData);
		analyticsSettings.put("zookeeperPort", zookeeperPort.getText());
		zookeeperPort.addModifyListener(e -> analyticsSettings.put("zookeeperPort", zookeeperPort.getText()));
		
		new Label(main, NONE).setText("Kafka Port: ");
		Text kafkaPort = new Text(main, SWT.BORDER);
		kafkaPort.setText("9092");
		kafkaPort.setLayoutData(gridData);
		analyticsSettings.put("kafkaPort", kafkaPort.getText());
		
		new Label(main, NONE).setText("Kafka Listeners: ");
		Text kafkaListeners = new Text(main, SWT.BORDER);
		kafkaListeners.setText("29092, 29093");
		kafkaListeners.setLayoutData(gridData);
		analyticsSettings.put("kafkaListeners", kafkaListeners.getText());
		
		new Label(main, NONE).setText("Kafka Inter Broker Listener Name: ");
		Text kafkaListenerName = new Text(main, SWT.BORDER);
		kafkaListenerName.setText("PLAINTEXT");
		kafkaListenerName.setLayoutData(gridData);
		
		setControl(main);
	}
	
	public HashMap<String, String> getAnalyticsSettings() {
		return analyticsSettings;
	}

}

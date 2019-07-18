package de.atb.typhondl.xtext.ui.creationWizard;

import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.typhonDL.DBType;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.utilities.WizardFields;
import de.atb.typhondl.xtext.ui.wizard.Messages;

public class CreationMainPage extends MyWizardPage {

	private URI modelPath;
	private Text fileText;
	private Combo templateCombo;
	private String chosenTemplate;
	private boolean useAnalytics;

	protected CreationMainPage(String pageName, URI modelPath) {
		super(pageName);
		this.modelPath = modelPath;
	}

	@Override
	public void createControl(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		main.setLayout(new GridLayout(2, false));
		createHeader(main);
		createCombo(main);
		createAdditions(main);
		setControl(main);
	}

	private void createHeader(Composite main) {
		setTitle("Create a TyphonDL model");
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		main.setLayout(layout);

		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);

		Label folderLabel = new Label(main, SWT.NONE);
		folderLabel.setText("Folder:");
		Label folderText = new Label(main, SWT.NONE);
		folderText.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, true, false));
		folderText.setText(getFolder());
		
		Label fileLabel = new Label(main, SWT.NONE);
		fileLabel.setText("Name");
		fileText = new Text(main, SWT.BORDER);
		fileText.setLayoutData(gridData);
		fileText.setFocus();
		fileText.addModifyListener(e -> validate());
		validate();
	}

	private void createCombo(Composite main) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		main.setLayout(layout);

		Label templateLabel = new Label(main, SWT.NONE);
		templateLabel.setText(Messages.NewFileWizardPrimaryPage_template_label);
		templateCombo = new Combo(main, SWT.READ_ONLY);
		templateCombo.setItems("Docker Compose","Kubernetes with Docker"); //TODO specify this somewhere else
		templateCombo.setText("Docker Compose");
		chosenTemplate = templateCombo.getText();
		templateCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				chosenTemplate = templateCombo.getText();
			}
		});
	}

	private void createAdditions(Composite main) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		main.setLayout(layout);
		
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gridData.horizontalSpan = 2;
		
		Button checkbox = new Button(main, SWT.CHECK);
		checkbox.setText("Use Typhon Data Analytics");
		checkbox.setSelection(false);
		this.useAnalytics = false;
		checkbox.setLayoutData(gridData);
		checkbox.setToolTipText("Check if you want to include Data Analytics in your deployment");
		checkbox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				useAnalytics = checkbox.getSelection();
			}
		});

	}

	private void validate() {
		setStatus(null);
		if ("".equals(fileText.getText().trim())) { //$NON-NLS-1$
			setStatus(new Status(IStatus.ERROR, "NewFileWizard", Messages.NewFileWizardPrimaryPage_empty_name)); //$NON-NLS-1$
			return;
		}
		IFile file = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new Path(getFolder() + "/" + fileText.getText() + ".tdl"));
		if (file.exists()) {
			setStatus(new Status(IStatus.ERROR, "NewFileWizard", //$NON-NLS-1$
					Messages.NewFileWizardPrimaryPage_file_already_exist_pre + fileText.getText() + ".tdl"
							+ Messages.NewFileWizardPrimaryPage_file_already_exist_post));
			return;
		}
	}


	private String getFolder() {
		String path = modelPath.toString();
		String pathWithoutFile = path.substring(0, path.lastIndexOf("/"));
		return pathWithoutFile.substring(pathWithoutFile.lastIndexOf("/") + 1);
	}

	public String getChosenTemplate() {
		return chosenTemplate;
	}
	
	public boolean getUseAnalytics() {
		return useAnalytics;
	}

}

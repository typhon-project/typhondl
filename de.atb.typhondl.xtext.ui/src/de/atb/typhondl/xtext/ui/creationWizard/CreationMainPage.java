package de.atb.typhondl.xtext.ui.creationWizard;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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

import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

public class CreationMainPage extends MyWizardPage {

	private URI MLmodelPath;
	private Text fileText;
	private Combo templateCombo;
	private int chosenTemplate;
	private boolean useAnalytics;

	protected CreationMainPage(String pageName, URI MLmodelPath) {
		super(pageName);
		this.MLmodelPath = MLmodelPath;
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
		folderLabel.setText("Folder: ");
		Label folderText = new Label(main, SWT.NONE);
		folderText.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, true, false));
		folderText.setText(getFolder());
		
		Label fileLabel = new Label(main, SWT.NONE);
		fileLabel.setText("Name: ");
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
		templateLabel.setText("Template: ");
		templateCombo = new Combo(main, SWT.READ_ONLY);
		List<String> itemList = new ArrayList<String>();
		for (SupportedTechnologies tech : SupportedTechnologies.values()) {
			itemList.add(tech.getDisplayedName());
			//templateCombo.setItem(tech.ordinal(), tech.getDisplayedName()); somehow doesn't work
		}
		templateCombo.setItems(itemList.toArray(new String[itemList.size()]));
		templateCombo.setText(templateCombo.getItem(0));
		chosenTemplate = templateCombo.getSelectionIndex();
		templateCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				chosenTemplate = templateCombo.getSelectionIndex();
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
			setStatus(new Status(IStatus.ERROR, "NewFileWizard", "Name must not be empty")); //$NON-NLS-1$
			return;
		}
		IFile file = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new Path(getFolder() + "/" + fileText.getText() + ".tdl"));
		if (file.exists()) {
			setStatus(new Status(IStatus.ERROR, "NewFileWizard", //$NON-NLS-1$
					"File '" + fileText.getText() + ".tdl"
							+ "' already exists."));
			return;
		}
	}


	private String getFolder() {
		String path = MLmodelPath.toString();
		String pathWithoutFile = path.substring(0, path.lastIndexOf("/"));
		return pathWithoutFile.substring(pathWithoutFile.lastIndexOf("/") + 1);
	}

	public int getChosenTemplate() {
		return chosenTemplate;
	}
	
	public boolean getUseAnalytics() {
		return useAnalytics;
	}
	
	public String getModelName() {
		return fileText.getText() + ".tdl";
	}

}

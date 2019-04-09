package de.atb.typhondl.xtext.ui.wizard;

import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.xtext.ui.wizard.template.AbstractFileTemplate;
import org.eclipse.xtext.ui.wizard.template.NewFileWizardPrimaryPage;
import org.eclipse.xtext.ui.wizard.template.TemplateFileInfo;
import org.eclipse.xtext.ui.wizard.template.TemplateLabelProvider;

@SuppressWarnings("restriction")
public class MyMainPage extends NewFileWizardPrimaryPage {

	private URI modelPath;
	private AbstractFileTemplate[] templates;
	private TemplateLabelProvider labelProvider;
	private ComboViewer templateCombo;
	private Text fileText;

	public MyMainPage(String pageName, AbstractFileTemplate[] templates, IStructuredSelection selection,
			TemplateLabelProvider labelProvider, URI modelPath) {
		super(pageName, templates, selection, labelProvider);
		this.modelPath = modelPath;
		this.templates = templates;
		this.labelProvider = labelProvider;
	}

	@Override
	public void createControl(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		main.setLayout(new GridLayout(2, false));

		createHeader(main);
		createTemplateWidgets(main);

		setControl(main);

	}

	private void createHeader(Composite main) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		main.setLayout(layout);

		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);

		Label folderLabel = new Label(main, SWT.NONE);
		folderLabel.setText(Messages.NewFileWizardPrimaryPage_folder_label);
		Label folderText = new Label(main, SWT.NONE);
		folderText.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, true, false));
		folderText.setText(getFolder());

		Label fileLabel = new Label(main, SWT.NONE);
		fileLabel.setText(Messages.NewFileWizardPrimaryPage_name_label);
		fileText = new Text(main, SWT.BORDER);
		fileText.setLayoutData(gridData);
		fileText.setFocus();
		fileText.addModifyListener(e -> validate());
	}

	private void createTemplateWidgets(Composite main) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		main.setLayout(layout);

		Label templateLabel = new Label(main, SWT.NONE);
		templateLabel.setText(Messages.NewFileWizardPrimaryPage_template_label);

		templateCombo = new ComboViewer(main);
		templateCombo.setLabelProvider(labelProvider);
		templateCombo.setContentProvider(new ArrayContentProvider());
		templateCombo.setInput(templates);
		templateCombo.setSelection(new StructuredSelection(templates[0]));
		templateCombo.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		templateCombo.getCombo().setToolTipText(getSelectedTemplate().getDescription());
		templateCombo.addSelectionChangedListener(e -> {
			templateCombo.getCombo().setToolTipText(getSelectedTemplate().getDescription());
			validate();
			getContainer().updateButtons();
		});

	}

	private void validate() {
		setStatus(null);
		if ("".equals(fileText.getText().trim())) { //$NON-NLS-1$
			setStatus(new Status(IStatus.ERROR, "NewFileWizard", Messages.NewFileWizardPrimaryPage_empty_name)); //$NON-NLS-1$
			return;
		}
		IFile file = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new Path(getFolder() + "/" + getFileName() + ".tdl"));
		if (file.exists()) {
			setStatus(new Status(IStatus.ERROR, "NewFileWizard", //$NON-NLS-1$
					Messages.NewFileWizardPrimaryPage_file_already_exist_pre + getFileName() + ".tdl"
							+ Messages.NewFileWizardPrimaryPage_file_already_exist_post));
			return;
		}
	}

	private String getFolder() {
		String path = modelPath.toString();
		String pathWithoutFile = path.substring(0, path.lastIndexOf("/"));
		return pathWithoutFile.substring(pathWithoutFile.lastIndexOf("/") + 1);
	}

	public String getFileName() {
		return fileText.getText();
	}

	/**
	 * copied from superclass because it uses templateCombo (which is not set in
	 * super)
	 */
	public AbstractFileTemplate getSelectedTemplate() {
		if (templates.length == 1) {
			return templates[0];
		}
		ISelection selection = templateCombo.getSelection();
		if (selection instanceof IStructuredSelection) {
			return (AbstractFileTemplate) ((IStructuredSelection) selection).getFirstElement();
		}
		return null;
	}

	public TemplateFileInfo getFileInfo() {
		return new TemplateFileInfo(getFolder(), getFileName(), getSelectedTemplate());
	}
}

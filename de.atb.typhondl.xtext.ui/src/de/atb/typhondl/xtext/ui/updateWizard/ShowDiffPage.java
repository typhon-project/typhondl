package de.atb.typhondl.xtext.ui.updateWizard;

import java.net.URI;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.xtext.ui.wizard.template.AbstractFileTemplate;
import org.eclipse.xtext.ui.wizard.template.NewFileWizardPrimaryPage;
import org.eclipse.xtext.ui.wizard.template.TemplateLabelProvider;

@SuppressWarnings("restriction")
public class ShowDiffPage extends NewFileWizardPrimaryPage {

	private URI modelPath;
	private AbstractFileTemplate[] templates;
	private TemplateLabelProvider labelProvider;

	protected ShowDiffPage(String pageName, AbstractFileTemplate[] templates, IStructuredSelection selection,
			TemplateLabelProvider labelProvider, URI modelPath) {
		super(pageName, templates, selection, labelProvider);
		this.modelPath = modelPath;
		this.templates = templates;
		this.labelProvider = labelProvider;
	}

}

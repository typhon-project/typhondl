package de.atb.typhondl.xtext.ui.updateWizard;

import java.io.File;
import java.net.URI;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.ui.util.FileOpener;
import org.eclipse.xtext.ui.wizard.template.AbstractFileTemplate;
import org.eclipse.xtext.ui.wizard.template.NewFileWizardPrimaryPage;
import org.eclipse.xtext.ui.wizard.template.TemplateLabelProvider;
import org.eclipse.xtext.ui.wizard.template.TemplateNewFileWizard;

@SuppressWarnings("restriction")
public class UpdateModelWizard extends TemplateNewFileWizard {

	protected URI DLmodelURI;
	protected IPath DLmodelPath;
	private IGrammarAccess grammarAccess;
	private TemplateLabelProvider labelProvider;
	private FileOpener fileOpener;

	protected ShowDiffPage mainPage;

	public UpdateModelWizard(IGrammarAccess grammarAccess, TemplateLabelProvider labelProvider, FileOpener fileOpener,
			IPath path) {
		super();
		this.DLmodelPath = path;
		this.DLmodelURI = path.toFile().toURI();
		setWindowTitle("Update TyphonDL model " + getDLModelName());
		this.grammarAccess = grammarAccess;
		this.labelProvider = labelProvider;
		this.fileOpener = fileOpener;
	}

	private String getDLModelName() {
		return this.DLmodelPath.lastSegment().substring(0, this.DLmodelPath.lastSegment().lastIndexOf('.'));
	}

	@Override
	protected ShowDiffPage createMainPage(String pageName) {
		/*
		 * TODO read container type and create array with just one entry: container type
		 */
		AbstractFileTemplate[] templates = null; 
		mainPage = new ShowDiffPage(pageName, templates, selection, labelProvider, DLmodelURI);
		super.mainPage = mainPage;
		return mainPage;
	}

}

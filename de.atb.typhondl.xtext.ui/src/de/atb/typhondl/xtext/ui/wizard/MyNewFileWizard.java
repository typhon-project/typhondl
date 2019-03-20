package de.atb.typhondl.xtext.ui.wizard;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.ui.util.FileOpener;
import org.eclipse.xtext.ui.wizard.template.AbstractFileTemplate;
import org.eclipse.xtext.ui.wizard.template.IFileTemplateProvider;
import org.eclipse.xtext.ui.wizard.template.TemplateLabelProvider;
import org.eclipse.xtext.ui.wizard.template.TemplateNewFileWizard;
import org.eclipse.xtext.ui.wizard.template.TemplateParameterPage;

import com.google.inject.Inject;

@SuppressWarnings("restriction")
public class MyNewFileWizard extends TemplateNewFileWizard {
	
	private static final String FILE_TEMPLATE_PROVIDER_EXTENSION_POINT_ID = "org.eclipse.xtext.ui.fileTemplate"; //$NON-NLS-1$
	private static final String FILE_TEMPLATE_PROVIDER_ID = "fileTemplateProvider"; //$NON-NLS-1$
	private static final String FILE_TEMPLATE_PROVIDER_GRAMMAR_NAME_ATTRIBUTE = "grammarName"; //$NON-NLS-1$
	private static final String FILE_TEMPLATE_PROVIDER_GRAMMAR_CLASS_ATTRIBUTE = "class"; //$NON-NLS-1$
	private static final Logger logger = Logger.getLogger(TemplateNewFileWizard.class);
	
	protected URI modelPath;
	protected MyMainPage mainPage;
	
	@Inject
	private TemplateLabelProvider labelProvider;
	@Inject
	private FileOpener fileOpener;
	@Inject
	private IGrammarAccess grammarAccess;
	
	public MyNewFileWizard(IPath path, IGrammarAccess grammarAccess, TemplateLabelProvider labelProvider,
			FileOpener fileOpener) {
		super();
		this.modelPath = path.toFile().toURI();
		setNeedsProgressMonitor(true);
		setWindowTitle(Messages.MyNewFileWizard_title);
		this.grammarAccess = grammarAccess;
		this.labelProvider = labelProvider;
		this.fileOpener = fileOpener;
	}
	
	@Override
	protected MyMainPage createMainPage(String pageName) {
		System.out.println("MyMainPage is created");
		mainPage = new MyMainPage(pageName, loadTemplatesFromExtensionPoint(), selection, labelProvider, modelPath);
		super.mainPage = mainPage;
		return mainPage;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) { // TODO

		if (page instanceof MyMainPage) {
			System.out.println("in MyNewFileWizard: getNextPage");
			IWizardPage nextPage = super.getNextPage(page);
			System.out.println("nextPage == null? " + (nextPage == null));
			if (nextPage instanceof TemplateParameterPage) {
				System.out.println("templateParameterPage!");
				templateParameterPage = (TemplateParameterPage) nextPage;
			}
			return nextPage;
		} else {
			return null;
		}		
	}
	
	/**
	 * 
	 */
	private AbstractFileTemplate[] loadTemplatesFromExtensionPoint() {
		List<AbstractFileTemplate> result = new ArrayList<>();
		for (IConfigurationElement element : Platform.getExtensionRegistry()
				.getConfigurationElementsFor(FILE_TEMPLATE_PROVIDER_EXTENSION_POINT_ID)) {
			if (FILE_TEMPLATE_PROVIDER_ID.equals(element.getName())
					&& getGrammarName().equals(element.getAttribute(FILE_TEMPLATE_PROVIDER_GRAMMAR_NAME_ATTRIBUTE))) {
				try {
					IFileTemplateProvider provider = (IFileTemplateProvider) element
							.createExecutableExtension(FILE_TEMPLATE_PROVIDER_GRAMMAR_CLASS_ATTRIBUTE);
					MyFileTemplateProvider myProvider = (MyFileTemplateProvider) provider;
					result.addAll(Arrays.asList(myProvider.getFileTemplates(modelPath)));
				} catch (CoreException e) {
					logger.error(
							"Can not instantiate '" //$NON-NLS-1$
									+ element.getAttribute(FILE_TEMPLATE_PROVIDER_GRAMMAR_CLASS_ATTRIBUTE) + "'", //$NON-NLS-1$
							e);
				}
			}
		}
		System.out.println("loadTemplates: " + result.size());
		return result.toArray(new AbstractFileTemplate[0]);
	}
	
	/**
	 * super class can't access the injected grammarAccess
	 */
	protected String getGrammarName() {
		return grammarAccess.getGrammar().getName();
	}
}

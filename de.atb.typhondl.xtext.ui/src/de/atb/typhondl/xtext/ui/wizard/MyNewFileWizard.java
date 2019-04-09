package de.atb.typhondl.xtext.ui.wizard;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.ui.util.FileOpener;
import org.eclipse.xtext.ui.wizard.template.AbstractFileTemplate;
import org.eclipse.xtext.ui.wizard.template.IFileTemplateProvider;
import org.eclipse.xtext.ui.wizard.template.TemplateFileInfo;
import org.eclipse.xtext.ui.wizard.template.TemplateLabelProvider;
import org.eclipse.xtext.ui.wizard.template.TemplateNewFileWizard;
import org.eclipse.xtext.ui.wizard.template.TemplateParameterPage;
import org.eclipse.xtext.ui.wizard.template.WorkspaceFileGenerator;

/**
 * This class <strong>MUST</strong> be in this package!!!
 * 
 * DO NOT TOUCH
 * 
 * @author flug
 *
 */
@SuppressWarnings("restriction")
public class MyNewFileWizard extends TemplateNewFileWizard {

	private static final String FILE_TEMPLATE_PROVIDER_EXTENSION_POINT_ID = "org.eclipse.xtext.ui.fileTemplate"; //$NON-NLS-1$
	private static final String FILE_TEMPLATE_PROVIDER_ID = "fileTemplateProvider"; //$NON-NLS-1$
	private static final String FILE_TEMPLATE_PROVIDER_GRAMMAR_NAME_ATTRIBUTE = "grammarName"; //$NON-NLS-1$
	private static final String FILE_TEMPLATE_PROVIDER_GRAMMAR_CLASS_ATTRIBUTE = "class"; //$NON-NLS-1$
	private static final Logger logger = Logger.getLogger(TemplateNewFileWizard.class);

	private IGrammarAccess grammarAccess;
	private TemplateLabelProvider labelProvider;
	private FileOpener fileOpener;

	public MyNewFileWizard(IGrammarAccess grammarAccess, TemplateLabelProvider labelProvider, FileOpener fileOpener) {
		super();
		setWindowTitle(Messages.MyNewFileWizard_title);
		this.grammarAccess = grammarAccess;
		this.labelProvider = labelProvider;
		this.fileOpener = fileOpener;
	}

	protected URI modelPath;
	protected MyMainPage mainPage;

	public URI getModelPath() {
		return modelPath;
	}

	public void setModelPath(URI modelPath) {
		this.modelPath = modelPath;
	}

	@Override
	protected MyMainPage createMainPage(String pageName) {
		mainPage = new MyMainPage(pageName, loadTemplatesFromExtensionPoint(), selection, labelProvider, modelPath);
		super.mainPage = mainPage;
		return mainPage;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {

		if (page instanceof MyMainPage) {
			IWizardPage nextPage = super.getNextPage(page);
			if (nextPage instanceof TemplateParameterPage) {
				templateParameterPage = (TemplateParameterPage) nextPage;
			}
			return nextPage;
		} else {
			return null;
		}
	}

	/**
	 * Has to be overridden because the fileOpener is not injected to superclass.
	 * TODO file is not opened. Will be possible when
	 * <code>org.eclipse.xtext.ui.wizard.template.AbstractFileTemplate.setTemplateInfo(TemplateFileInfo)</code>
	 * is not package protected anymore (promised for next update, see
	 * https://www.eclipse.org/forums/index.php/t/1097997/)
	 */
	@Override
	protected void doFinish(final TemplateFileInfo info, final IProgressMonitor monitor) {
		try {
			super.doFinish(info, monitor);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
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
					logger.error("Can not instantiate '" //$NON-NLS-1$
							+ element.getAttribute(FILE_TEMPLATE_PROVIDER_GRAMMAR_CLASS_ATTRIBUTE) + "'", //$NON-NLS-1$
							e);
				}
			}
		}
		return result.toArray(new AbstractFileTemplate[0]);
	}

	/**
	 * super class can't access the NOT injected grammarAccess
	 */
	protected String getGrammarName() {
		return grammarAccess.getGrammar().getName();
	}
}

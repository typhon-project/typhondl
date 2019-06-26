package de.atb.typhondl.xtext.ui.updateWizard;

import java.net.URI;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IPath;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.ui.util.FileOpener;
import org.eclipse.xtext.ui.wizard.template.AbstractFileTemplate;
import org.eclipse.xtext.ui.wizard.template.TemplateLabelProvider;
import org.eclipse.xtext.ui.wizard.template.TemplateNewFileWizard;

import de.atb.typhondl.xtext.typhonDL.ContainerType;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Type;
import de.atb.typhondl.xtext.ui.activator.Activator;
import de.atb.typhondl.xtext.ui.service.Service;

@SuppressWarnings("restriction")
public class UpdateModelWizard extends TemplateNewFileWizard {

	protected URI DLmodelURI;
	protected IPath DLmodelPath;
	private IGrammarAccess grammarAccess;
	private TemplateLabelProvider labelProvider;
	private FileOpener fileOpener;

	protected ShowDiffPage mainPage;

	private DeploymentModel DLmodelOld;

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
		this.DLmodelOld = Service.readDLmodel(this.DLmodelPath);
		ContainerType containerType = getContainerType();
		AbstractFileTemplate[] templates = null;
		mainPage = new ShowDiffPage(pageName, templates, selection, labelProvider, DLmodelURI);
		super.mainPage = mainPage;
		return mainPage;
	}

	private ContainerType getContainerType() {		
		return DLmodelOld.getElements().stream().filter(element -> Type.class.isInstance(element))
				.map(element -> (Type) element).filter(type -> ContainerType.class.isInstance(type))
				.map(type -> (ContainerType) type).collect(Collectors.toList()).get(0);
	}

}

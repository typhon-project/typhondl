package de.atb.typhondl.xtext.ui.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Import;
import de.atb.typhondl.xtext.ui.activator.Activator;

public class Service {

	public static DeploymentModel readDLmodel(IPath path) {

		Injector injector = Activator.getInstance().getInjector(Activator.DE_ATB_TYPHONDL_XTEXT_TYPHONDL);
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);

		URI modelURI = URI.createFileURI(path.toString());

		Resource resource = resourceSet.getResource(modelURI, true);

		/*
		 * DeploymentModel model only includes the model parts written in the
		 * File(modelUri), not any parts that are saved in different files like a
		 * database.tdl
		 */
		DeploymentModel model = (DeploymentModel) resource.getContents().get(0);

		addAllResources(model);
		return model;
	}

	public static void addAllResources(DeploymentModel model) {
		List<Import> modelList = model.getGuiMetaInformation().stream()
				.filter(metaModel -> Import.class.isInstance(metaModel)).map(metaModel -> (Import) metaModel)
				.filter(info -> info.getRelativePath().endsWith(".tdl")).collect(Collectors.toList());
		for (Import modelFile : modelList) {
			Resource resource = model.eResource();
			String relativePath = modelFile.getRelativePath();
			openImport(resource, relativePath);
		}
//		model.getGuiMetaInformation().stream().filter(metaModel -> Import.class.isInstance(metaModel))
//				.map(metaModel -> (Import) metaModel).filter(info -> info.getRelativePath().endsWith(".tdl"))
//				.map(tdlFile -> openImport(model.eResource(), tdlFile.getRelativePath()));
	}

	public static ArrayList<DB> getDBs(DeploymentModel model) {
		ArrayList<DB> dbs = new ArrayList<DB>();
		model.eResource().getResourceSet().getResources().forEach(resource -> {
			dbs.addAll(((DeploymentModel) resource.getContents().get(0)).getElements().stream()
					.filter(element -> DB.class.isInstance(element)).map(element -> (DB) element)
					.collect(Collectors.toList()));
		});
		return dbs;
	}

	/**
	 * see http://www.cs.kun.nl/J.Hooman/DSL/AdvancedXtextManual.pdf
	 * 
	 * @param currentResource
	 * @param importedURIAsString
	 * @return
	 */
	private static Resource openImport(final Resource currentResource, final String importedURIAsString) {
		URI _uRI = null;
		if (currentResource != null) {
			_uRI = currentResource.getURI();
		}
		final URI currentURI = _uRI;
		URI _createURI = null;
		if (URI.class != null) {
			_createURI = URI.createURI(importedURIAsString);
		}
		final URI importedURI = _createURI;
		URI _resolve = null;
		if (importedURI != null) {
			_resolve = importedURI.resolve(currentURI);
		}
		final URI resolvedURI = _resolve;
		ResourceSet _resourceSet = null;
		if (currentResource != null) {
			_resourceSet = currentResource.getResourceSet();
		}
		final ResourceSet currentResourceSet = _resourceSet;
		Resource _resource = null;
		if (currentResourceSet != null) {
			_resource = currentResourceSet.getResource(resolvedURI, true);
		}
		final Resource resource = _resource;
		return resource;
	}
}

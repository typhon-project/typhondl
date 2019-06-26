package de.atb.typhondl.xtext.ui.activator;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.osgi.framework.BundleContext;

import com.google.inject.Injector;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Import;
import de.atb.typhondl.xtext.ui.internal.XtextActivator;

public class Activator extends XtextActivator {
	
	private static Activator plugin;
	public final static String IMAGE_PATH = "icons/typhon_icon.png";

	public Activator() {
		super();
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return plugin;
	}
	
	@Override
	protected void initializeImageRegistry(final ImageRegistry reg) {
	    reg.put(IMAGE_PATH, imageDescriptorFromPlugin(PLUGIN_ID, IMAGE_PATH));
	}
	
	public static DeploymentModel readDLmodel(IPath path) {
		
		Injector injector = getInstance().getInjector(Activator.DE_ATB_TYPHONDL_XTEXT_TYPHONDL);
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

	private static void addAllResources(DeploymentModel model) {
		List<Import> importedInfos = model.getGuiMetaInformation().stream()
				.filter(metaModel -> Import.class.isInstance(metaModel)).map(metaModel -> (Import) metaModel)
				.collect(Collectors.toList());
		importedInfos.stream().filter(info -> info.getRelativePath().endsWith(".tdl")).forEach(importedInfo -> {
			openImport(model.eResource(), importedInfo.getRelativePath()); // adds all imports to resourceSet
		});		
	}
	
	/**
	 * see http://www.cs.kun.nl/J.Hooman/DSL/AdvancedXtextManual.pdf
	 * 
	 * @param currentResource
	 * @param importedURIAsString
	 * @return
	 */
	public static Resource openImport(final Resource currentResource, final String importedURIAsString) {
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

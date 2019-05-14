package de.atb.typhondl.xtext.generator

import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.mwe2.language.mwe2.Import
import de.atb.typhondl.xtext.typhonDL.DeploymentModel
import de.atb.typhondl.xtext.typhonDL.ImportDB

class ResourceUtils {
	public def static EObject resourceToEObject(Resource resource) {
		return resource?.allContents?.head;
	}

	public def static Resource openImport(Resource currentResource, String importedURIAsString) {
		val URI currentURI = currentResource?.getURI;
		val URI importedURI = URI?.createURI(importedURIAsString);
		val URI resolvedURI = importedURI?.resolve(currentURI);
		val ResourceSet currentResourceSet = currentResource?.resourceSet;
		val Resource resource = currentResourceSet?.getResource(resolvedURI, true);
		return resource;
	}

	public def static test(ImportDB imp) {
		val DeploymentModel importedPerson = ResourceUtils.resourceToEObject(
			ResourceUtils.openImport(imp.eResource, imp.name)) as DeploymentModel;
	}
}

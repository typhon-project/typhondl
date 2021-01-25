package de.atb.typhondl.xtext.scoping;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.impl.DefaultGlobalScopeProvider;

import com.google.common.base.Predicate;

public class MyGlobalScopeProvider extends DefaultGlobalScopeProvider {
    @Override
    public IScope getScope(Resource resource, EReference reference, Predicate<IEObjectDescription> filter) {
        filter = new Predicate<IEObjectDescription>() {
            @Override
            public boolean apply(IEObjectDescription input) {
                final URI uriResource = resource.getURI();
                final URI uriReference = input.getEObjectURI();
                final String folderResource = uriResource.segment(uriResource.segmentCount() - 2);
                final String folderReference = uriReference.segment(uriReference.segmentCount() - 2);
                return !uriReference.equals(uriResource) && folderResource.equals(folderReference);
            }
        };
        return super.getScope(resource, reference, filter);
    }
}

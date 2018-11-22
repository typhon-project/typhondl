/*
 * generated by Xtext 2.15.0
 */
package org.typhon.dsls.xtext.ui;

import com.google.inject.Injector;
import org.eclipse.core.runtime.Platform;
import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;
import org.typhon.dsls.xtext.typhondl.ui.internal.TyphondlActivator;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass. 
 */
public class TyphonDLExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

	@Override
	protected Bundle getBundle() {
		return Platform.getBundle(TyphondlActivator.PLUGIN_ID);
	}
	
	@Override
	protected Injector getInjector() {
		TyphondlActivator activator = TyphondlActivator.getInstance();
		return activator != null ? activator.getInjector(TyphondlActivator.ORG_TYPHON_DSLS_XTEXT_TYPHONDL) : null;
	}

}

package de.atb.typhondl.xtext.ui.activator;

import org.osgi.framework.BundleContext;

import de.atb.typhondl.xtext.ui.internal.XtextActivator;

public class Activator extends XtextActivator {
	
	private static Activator plugin;

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
}

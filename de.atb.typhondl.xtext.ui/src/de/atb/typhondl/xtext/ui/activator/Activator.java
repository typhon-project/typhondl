package de.atb.typhondl.xtext.ui.activator;

import org.eclipse.jface.resource.ImageRegistry;
import org.osgi.framework.BundleContext;

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
}

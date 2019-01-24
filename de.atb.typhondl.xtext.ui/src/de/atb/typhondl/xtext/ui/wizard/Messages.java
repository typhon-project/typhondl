package de.atb.typhondl.xtext.ui.wizard;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "de.atb.typhondl.xtext.ui.wizard.messages"; //$NON-NLS-1$
	
	public static String HelloWorldProject_Label;
	public static String HelloWorldProject_Description;
	public static String WizardLoadModel_nameLabel;
	public static String WizardLoadModel_useModel;
	public static String WizardLoadModel_browseLabel;
	public static String WizardLoadModel_fileLocationEmpty;
	public static String WizardLoadModel_locationError;
	public static String WizardLoadModel_directoryLabel;
	public static String WizardLoadModel_existError;
	public static String WizardLoadModel_fileError;
	public static String WizardLoadModel_wrongExtension;
	public static String TemplateNewProjectWizard_title_suffix;
	public static String TemplateNewProjectWizard_create_new_prefix;
	public static String TemplateNewProjectWizard_create_new_suffix;
	public static String TemplateNewProjectWizard_title;
	public static String WizardSelectionPage_title_suffix;
	public static String WizardSelectionPage_description;
	
	static {
	// initialize resource bundle
	NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}

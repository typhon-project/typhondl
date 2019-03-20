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
	public static String DockerCompose_Label;
	public static String DockerCompose_Description;
	public static String Kubernetes_Label;
	public static String Kubernetes_Description;
	public static String EmptyProject_Label;
	public static String EmptyProject_Description;
	public static String HelloWorldFile_Label;
	public static String HelloWorldFile_Description;
	public static String DockerComposeFile_Label;
	public static String DockerComposeFile_Description;
	public static String KubernetesFile_Label;
	public static String KubernetesFile_Description;
	public static String MyNewFileWizard_title;
	public static String MyNewFileWizard_title_suffix;
	public static String TemplateNewFileWizard_title;
	public static String TemplateNewFileWizard_create_new;
	public static String TemplateNewFileWizard_create_new_prefix;
	public static String TemplateNewFileWizard_create_new_suffix;
	public static String NewFileWizardPrimaryPage_folder_label;
	public static String NewFileWizardPrimaryPage_browse_button;
	public static String NewFileWizardPrimaryPage_name_label;
	public static String NewFileWizardPrimaryPage_selection_description;
	public static String NewFileWizardPrimaryPage_template_label;
	public static String NewFileWizardPrimaryPage_unexistint_folder_pre;
	public static String NewFileWizardPrimaryPage_unexistent_folder_post;
	public static String NewFileWizardPrimaryPage_empty_name;
	public static String NewFileWizardPrimaryPage_file_already_exist_pre;
	public static String NewFileWizardPrimaryPage_file_already_exist_post;
	public static String DockerComposeFileTest_Label;
	public static String DockerComposeFileTest_Description;
	
	static {
	// initialize resource bundle
	NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}

/**
 * 
 */
package de.atb.typhondl.xtext.ui.wizard;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.dialogs.FileFolderSelectionDialog;
import org.eclipse.ui.internal.ide.dialogs.ProjectContentsLocationArea.IErrorMessageReporter;
import org.eclipse.ui.internal.ide.filesystem.FileSystemConfiguration;
import org.eclipse.ui.internal.ide.filesystem.FileSystemSupportRegistry;

import com.google.inject.spi.Message;

/**
 * @author flug
 *
 */
@SuppressWarnings("restriction")
public class MyFileLocationArea {
	
	
	
	private IErrorMessageReporter errorReporter;
	
	private boolean useModel = false;
	private Button useModelButton;
	private Label locationLabel;
	private Text locationPathField;
	private Button browseButton;
	private static final int SIZING_TEXT_FIELD_WIDTH = 250;

	public MyFileLocationArea(IErrorMessageReporter errorReporter, Composite parent) {
		this.errorReporter = errorReporter;

		createContents(parent);
		
	}

	/**
	 * Create the contents of the FileLocationArea.
	 * 
	 * @param parent
	 */
	private void createContents(Composite parent) {

		int columns = 4;
		
		Composite loadGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = columns;
		loadGroup.setLayout(layout);
		loadGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		useModelButton = new Button(loadGroup, SWT.CHECK | SWT.RIGHT);
		useModelButton.setText(Messages.WizardLoadModel_useModel);
		useModelButton.setSelection(useModel);
		
		GridData buttonData = new GridData();
		buttonData.horizontalSpan = columns;
		useModelButton.setLayoutData(buttonData);
		
		createUserEntryArea(loadGroup);
		
		useModelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				useModel = useModelButton.getSelection();
				setUserEntryAreaEnabled();
				checkValidLocation();
			}
		});
		
		
	}

	/**
	 * Set the enablement state of the Typhon ML UserEntryArea
	 */
	protected void setUserEntryAreaEnabled() {
		locationLabel.setEnabled(useModel);
		locationPathField.setEnabled(useModel);
		browseButton.setEnabled(useModel);
		
	}

	/**
	 * Create the area for user entry.
	 * 
	 * @param parent
	 */
	private void createUserEntryArea(Composite parent) {
		
		locationLabel = new Label(parent, SWT.NONE);
		locationLabel.setText(Messages.WizardLoadModel_nameLabel);
		
		locationPathField = new Text(parent, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		//data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		data.horizontalSpan = 2;
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		locationPathField.setLayoutData(data);
		locationPathField.addModifyListener(e -> errorReporter.reportError(checkValidLocation(), false));
		
		browseButton = new Button(parent, SWT.PUSH);
		browseButton.setText(Messages.WizardLoadModel_browseLabel);
		// TODO actionListener
		
		setUserEntryAreaEnabled();
		
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleBrowseButtonPressed();
			}
		});
	}

	/**
	 * Open an appropriate directory browser
	 */
	protected void handleBrowseButtonPressed() {
		String selectedFile = null;

		FileDialog dialog = new FileDialog(locationPathField.getShell(), SWT.SHEET);
		dialog.setText("Select the TyphonML-file");
		String[] extensions = {"*.tml", "*.xmi"};
		dialog.setFilterExtensions(extensions);
		selectedFile = dialog.open();
		
		if (selectedFile != null) {
			updateLocationField(selectedFile);
		}
	}
	
	/**
	 * Update the location field based on the selected path.
	 *
	 * @param selectedPath
	 */
	private void updateLocationField(String selectedFile) {
		locationPathField.setText(TextProcessor.process(selectedFile));
	}

	/**
	 * Return the path on the location field.
	 *
	 * @return the path or the field's text if the path is invalid
	 */
	private String getPathFromLocationField() {
		URI fieldURI;
		try {
			fieldURI = new URI(locationPathField.getText());
		} catch (URISyntaxException e) {
			return locationPathField.getText();
		}
		String path= fieldURI.getPath();
		return path != null ? path : locationPathField.getText();
	}

	/**
	 * Return the browse button. Referenced in order to set the layout
	 * data in MyWizardNewProjectCreationPage.
	 *
	 * @return Button
	 */
	public Button getBrowseButton() {
		return browseButton;
	}
	
	/**
	 * Return the location for the TyphonML file. 
	 * 
	 * @return String
	 */
	public String getFileLocation() {
		return locationPathField.getText();
	}

	/**
	 * Check if the entry in the widget location is valid. If it is valid return
	 * null. Otherwise return a string that indicates the problem.
	 *
	 * @return String
	 */
	public String checkValidLocation() {

		if (!useModel) {
			return null;
		}
		
		String locationFieldContents = locationPathField.getText();
		if (locationFieldContents.length() == 0) {
			return Messages.WizardLoadModel_fileLocationEmpty;
		}

		URI newPath = getFileURI();
		if (newPath == null) {
			return Messages.WizardLoadModel_locationError;
		} else {
			//System.out.println(newPath.toString());
		}

		return null;
	}
	
	/**
	 * Get the URI for the location field if possible.
	 *
	 * @return URI or <code>null</code> if it is not valid.
	 */
	public URI getFileURI() {

		FileSystemConfiguration configuration = FileSystemSupportRegistry.getInstance().getDefaultConfiguration(); //TODO maybe chose other configuration?
		if (configuration == null) {
			return null;
		}
		return configuration.getContributor().getURI(locationPathField.getText());
	}

	public boolean useModel() {
		return useModel;
	}
	
	
	
	

}

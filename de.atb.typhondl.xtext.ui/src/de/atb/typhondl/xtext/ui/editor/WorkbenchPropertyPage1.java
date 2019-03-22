package de.atb.typhondl.xtext.ui.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

public class WorkbenchPropertyPage1 extends PropertyPage implements IWorkbenchPropertyPage {

	public WorkbenchPropertyPage1() {
		super();
	}

	@Override
	protected Control createContents(Composite parent) {
		
		
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		main.setLayout(new GridLayout(2, false));
		
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);

		Label folderLabel = new Label(main, SWT.NONE);
		folderLabel.setText("Test1");
		Label folderText = new Label(main, SWT.NONE);
		folderText.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, true, false));
		folderText.setText("is this working");

		Label fileLabel = new Label(main, SWT.NONE);
		fileLabel.setText("Test2");
		Text fileText = new Text(main, SWT.BORDER);
		fileText.setLayoutData(gridData);
		
		
		return null; 
	}

}

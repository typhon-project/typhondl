package de.atb.typhondl.xtext.ui.creationWizard;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.xtext.ui.wizard.template.TemplateParameterPage;

/**
 * Parent class for the pages of the {@link CreateModelWizard}. Sets the page's
 * status as in {@link TemplateParameterPage}
 * 
 * @author flug
 *
 */
public abstract class MyWizardPage extends WizardPage {

	/**
	 * Creates a WizardPage, only difference to {@link WizardPage} is the
	 * <code>setStatus()</code> method taken from {@link TemplateParameterPage}
	 * 
	 * @param pageName
	 */
	protected MyWizardPage(String pageName) {
		super(pageName);
	}

	/**
	 * Copyright (c) 2017, 2018 itemis AG (http://www.itemis.de) and others. All
	 * rights reserved. This program and the accompanying materials are made
	 * available under the terms of the Eclipse Public License v1.0 which
	 * accompanies this distribution, and is available at
	 * http://www.eclipse.org/legal/epl-v10.html
	 * 
	 * {@link org.eclipse.xtext.ui.wizard.template.TemplateParameterPage#setStatus(IStatus)}
	 *******************************************************************************/
	public void setStatus(IStatus status) {
		if (status == null || status.getSeverity() == IStatus.OK) {
			setErrorMessage(null);
			setMessage(null);
			setPageComplete(true);
		} else if (status.getSeverity() == IStatus.ERROR) {
			setErrorMessage(status.getMessage());
			setPageComplete(false);
		} else if (status.getSeverity() == IStatus.WARNING) {
			setErrorMessage(null);
			setMessage(status.getMessage(), IMessageProvider.WARNING);
			setPageComplete(true);
		} else {
			setErrorMessage(null);
			setMessage(status.getMessage(), IMessageProvider.INFORMATION);
			setPageComplete(true);
		}
	}
}

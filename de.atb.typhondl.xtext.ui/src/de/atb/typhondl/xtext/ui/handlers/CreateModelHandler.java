package de.atb.typhondl.xtext.ui.handlers;

/*-
 * #%L
 * de.atb.typhondl.xtext.ui
 * %%
 * Copyright (C) 2018 - 2020 ATB
 * %%
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 * #L%
 */

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.ui.XtextProjectHelper;

import com.google.inject.Inject;

import de.atb.typhondl.xtext.ui.creationWizard.CreateModelWizard;

/**
 * This handler is called when clicking Create TyphonDL model in the TyphonDL
 * context menu. The {@link CreateModelWizard} is opened. Adds Xtext nature if
 * it's missing from the project in which the selection (i.e. the Typhon ML
 * model) is contained.
 * 
 * @author flug
 *
 */
public class CreateModelHandler extends AbstractHandler {

    @Inject
    IGrammarAccess grammarAccess;

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {

        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
        IWorkbenchPage page = window.getActivePage();
        ISelection selection = page.getSelection();

        if (selection != null & selection instanceof IStructuredSelection) {
            IStructuredSelection strucSelection = (IStructuredSelection) selection;
            Object firstElement = strucSelection.getFirstElement();
            if (firstElement instanceof IAdaptable) {
                IFile file = ((IAdaptable) firstElement).getAdapter(IFile.class);
                IProject project = file.getProject();
                boolean hasXtextNature;
                try {
                    hasXtextNature = project.hasNature(XtextProjectHelper.NATURE_ID);
                    if (!hasXtextNature) {
                        IProjectDescription projectDescription = project.getDescription();
                        String[] oldNatures = projectDescription.getNatureIds();
                        String[] newNatures = new String[oldNatures.length + 1];
                        System.arraycopy(oldNatures, 0, newNatures, 1, oldNatures.length);
                        newNatures[0] = XtextProjectHelper.NATURE_ID;
                        projectDescription.setNatureIds(newNatures);
                        project.setDescription(projectDescription, null);
                    }
                } catch (CoreException e) {
                    MessageDialog.openWarning(window.getShell(), "UI",
                            "Please add Xtext Project Nature to your project");
                    e.printStackTrace();
                }
                CreateModelWizard fileWizard = new CreateModelWizard(file);
                WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), fileWizard);

                dialog.open();
            }

        }

        for (IProject iproject : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
            try {
                iproject.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

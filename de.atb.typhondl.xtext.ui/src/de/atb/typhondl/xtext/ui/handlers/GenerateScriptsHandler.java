package de.atb.typhondl.xtext.ui.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

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
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.ui.XtextProjectHelper;
import org.eclipse.xtext.ui.resource.XtextLiveScopeResourceSetProvider;

import com.google.inject.Inject;

import de.atb.typhondl.xtext.ui.scriptGeneration.GenerationService;

/**
 * This Handler is called when clicking "Generate Deployment Scripts" in the
 * TyphonDL context menu. Adds Xtext nature if it's missing from the project in
 * which the selection (i.e. the Typhon DL model) is contained.
 * 
 * @author flug
 *
 */
public class GenerateScriptsHandler extends AbstractHandler {

    @Inject
    XtextLiveScopeResourceSetProvider provider;

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {

        IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelectionChecked(event);
        Object object = selection.getFirstElement();
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();
        if (object instanceof IFile) {
            IFile file = (IFile) object;

            // add Xtext Nature
            IProject project = file.getProject();
            try {
                if (!project.hasNature(XtextProjectHelper.NATURE_ID)) {
                    IProjectDescription projectDescription = project.getDescription();
                    ArrayList<String> natureIds = new ArrayList<>(Arrays.asList(projectDescription.getNatureIds()));
                    natureIds.add(0, XtextProjectHelper.NATURE_ID);
                    projectDescription.setNatureIds(natureIds.toArray(new String[0]));
                    project.setDescription(projectDescription, null);
                }
            } catch (CoreException e) {
                MessageDialog.openWarning(HandlerUtil.getActiveWorkbenchWindow(event).getShell(), "UI",
                        "Please add Xtext Project Nature to your project");
                e.printStackTrace();
            }

            GenerationService generationService;
            try {
                generationService = new GenerationService(file, provider);
                String fileLocation = file.getLocation().toOSString();
                generationService.deleteOldGeneratedFiles(
                        new File(fileLocation.substring(0, fileLocation.lastIndexOf("." + file.getFileExtension()))));
                generationService.startGeneration();
            } catch (Exception e2) {
                if (e2 instanceof NullPointerException) {
                    MessageDialog.openError(HandlerUtil.getActiveWorkbenchWindow(event).getShell(), "UI",
                            "The \".properties\" file does not contain all necessary properties (probably due to an "
                                    + "update of the DL plugin), please recreate your DL model using the TyphonDL "
                                    + "Creation Wizard.");
                    return null;
                }
                MessageDialog.openError(HandlerUtil.getActiveWorkbenchWindow(event).getShell(), "UI",
                        "Please select the main model file containing the Platform definition and make sure there is"
                                + " a <mainModelName>.properties file.");
            }
            for (IProject iproject : root.getProjects()) {
                try {
                    iproject.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
                } catch (CoreException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

}

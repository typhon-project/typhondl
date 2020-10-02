package de.atb.typhondl.xtext.ui.refactoring;

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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;

import de.atb.typhondl.xtext.typhonDL.ClusterType;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Volume_Properties;
import de.atb.typhondl.xtext.ui.modelUtils.ModelService;
import de.atb.typhondl.xtext.ui.modelUtils.VolumesService;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.utilities.PropertiesLoader;
import de.atb.typhondl.xtext.ui.utilities.SavingOptions;

public class ClusterTypeRefactor {

    public static void refactor(EObject selectedElement, XtextEditor editor, XtextResource resource) {
        if (ClusterType.class.isInstance(selectedElement)) {

            DeploymentModel model = (DeploymentModel) resource.getContents().get(0);
            Properties properties = null;
            final String pathToProperties = ((FileEditorInput) editor.getEditorInput()).getFile().getLocationURI()
                    .toString().replace("file:", "").concat(".properties");
            try {
                properties = PropertiesLoader.loadPropertiesFromModelFile(pathToProperties);
            } catch (IOException e) {
                e.printStackTrace();
            }
            final ClusterType clusterType = (ClusterType) selectedElement;
            ClusterTypeDialog clusterTypeDialog = new ClusterTypeDialog(editor.getShell(), properties, model,
                    clusterType);
            if (clusterTypeDialog.open() == Window.OK) {
                Properties newProperties = clusterTypeDialog.getProperties();
                ClusterType newClusterType = clusterTypeDialog.getClusterType();
                if (properties.getProperty(PropertiesService.POLYSTORE_USEANALYTICS).equals("true")
                        && properties.get(PropertiesService.ANALYTICS_DEPLOYMENT_CREATE).equals("true")
                        && !clusterType.getName().equals(newClusterType.getName())) {
                    ChangeAnalyticsDialog analyticsDialog = new ChangeAnalyticsDialog(editor.getShell(), properties,
                            ModelService.getSupportedTechnology(newClusterType), Boolean.parseBoolean(
                                    properties.getProperty(PropertiesService.ANALYTICS_DEPLOYMENT_CONTAINED)));
                    if (shouldChangeAnalytics(editor.getShell(), properties)) {
                        if (analyticsDialog.open() == Window.OK) {
                            newProperties = analyticsDialog.getProperties();
                        } else {
                            MessageDialog.openError(editor.getShell(), "Refactor clustertype", "Refactoring failed");
                            return;
                        }
                    }
                }
                clusterType.setName(newClusterType.getName());
                changeVolumeTypes(model, clusterType);
                try {
                    resource.save(SavingOptions.getTDLoptions());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    OutputStream output = new FileOutputStream(pathToProperties);
                    newProperties.store(output, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private static void changeVolumeTypes(DeploymentModel model, ClusterType clusterType) {
        final List<Volume_Properties> allVolumeProperties = EcoreUtil2.getAllContentsOfType(model,
                Volume_Properties.class);
        for (Volume_Properties volume_Properties : allVolumeProperties) {
            if (volume_Properties.getVolumeType() != null) {
                volume_Properties.setVolumeType(
                        VolumesService.getDefaultVolumesType(ModelService.getSupportedTechnology(clusterType)));
            }
        }
    }

    private static boolean shouldChangeAnalytics(Shell shell, Properties properties) {
        if (properties.get(PropertiesService.ANALYTICS_DEPLOYMENT_CONTAINED).equals("true")) {
            return true;
        } else {
            return MessageDialog.openQuestion(shell, "Change Analytics",
                    "Would you like to change the clustertype for the Analytics component as well?");
        }
    }

}

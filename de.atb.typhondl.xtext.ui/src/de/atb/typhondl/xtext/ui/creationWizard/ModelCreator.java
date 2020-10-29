package de.atb.typhondl.xtext.ui.creationWizard;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.ui.resource.XtextLiveScopeResourceSetProvider;

import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.Cluster;
import de.atb.typhondl.xtext.typhonDL.ClusterType;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.ContainerType;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DBType;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Import;
import de.atb.typhondl.xtext.typhonDL.Platform;
import de.atb.typhondl.xtext.typhonDL.PlatformType;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.typhonDL.Volume_Toplevel;
import de.atb.typhondl.xtext.ui.activator.Activator;
import de.atb.typhondl.xtext.ui.modelUtils.ModelService;
import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.technologies.SupportedTechnologies;
import de.atb.typhondl.xtext.ui.utilities.SavingOptions;

/**
 * This class creates the new TyphonDL model from the selected ML model and the
 * given input from the {@link CreateModelWizard}. The following files are
 * created:
 * <li>The main model file</li>
 * <li>A dbtypes.tdl containing all used {@link DBType}s</li>
 * <li>A model file for each {@link DB} extracted from the ML model</li>
 * 
 * @author flug
 *
 */
public class ModelCreator {

    /**
     * The selected source ML model from which a DL model is created
     */
    private IFile MLmodel;

    /**
     * The resourceSet containing all tdl resources
     */
    private XtextResourceSet resourceSet;

    /**
     * The path to folder in which to save all model files
     */
    private IPath folder;

    /**
     * The DL model name entered on the {@link CreationMainPage}
     */
    private String DLmodelName;

    /**
     * Creates an instance of the {@link ModelCreator}
     * 
     * @param MLmodel     The selected ML model
     * @param DLmodelName The entered name for the DL model
     */
    public ModelCreator(IFile MLmodel, String DLmodelName) {
        this.MLmodel = MLmodel;
        this.folder = this.MLmodel.getFullPath().removeLastSegments(1);
        this.DLmodelName = DLmodelName;
        addResources();
    }

    /**
     * Gets the provided ResourceSet with all .tdl files
     */
    private void addResources() {
        this.resourceSet = ModelService
                .getResourceSet(Activator.getInstance().getInjector(Activator.DE_ATB_TYPHONDL_XTEXT_TYPHONDL)
                        .getInstance(XtextLiveScopeResourceSetProvider.class), MLmodel);
    }

    /**
     * Creates the new DL model
     * 
     * @param result           The DBs and Containers to add to the new model
     * @param chosenTechnology The int representation of the chosen technology
     *                         Template from {@link SupportedTechnologies}
     * @param properties       The polystore.properties
     * @return The main model file to be opened by the Xtext editor after creation
     */
    public IFile createDLmodel(HashMap<DB, Container> result, SupportedTechnologies chosenTechnology,
            Properties properties) {

        // create main model
        DeploymentModel DLmodel = TyphonDLFactory.eINSTANCE.createDeploymentModel();
        // add reference to ML model
        Import MLmodelImport = TyphonDLFactory.eINSTANCE.createImport();
        MLmodelImport.setRelativePath(this.MLmodel.getName());
        DLmodel.getGuiMetaInformation().add(MLmodelImport);

        // Add selected container type (chosen template in wizard)
        ContainerType containerType = TyphonDLFactory.eINSTANCE.createContainerType();
        containerType.setName(chosenTechnology.containerType());
        DLmodel.getElements().add(containerType);

        // Add selected cluster type (chosen template in wizard)
        ClusterType clusterType = TyphonDLFactory.eINSTANCE.createClusterType();
        clusterType.setName(chosenTechnology.name());
        DLmodel.getElements().add(clusterType);

        // create platform type. Since it has no influence on script generation
        // "localhost" is default
        // TODO add meaning to "platformtype"
        PlatformType platformType = TyphonDLFactory.eINSTANCE.createPlatformType();
        platformType.setName("localhost");
        DLmodel.getElements().add(platformType);

        ArrayList<DBType> dbTypes = new ArrayList<DBType>();
        // create import for each db
        for (DB db : result.keySet()) {
            Import importedDB = TyphonDLFactory.eINSTANCE.createImport();
            importedDB.setRelativePath(db.getName() + ".tdl");
            DLmodel.getGuiMetaInformation().add(importedDB);

            // types need to be the same instance
            boolean containsType = false;
            for (DBType dbType : dbTypes) {
                if (dbType.getName().equals(db.getType().getName())) {
                    containsType = true;
                    db.setType(dbType);
                }
            }
            if (!containsType) {
                dbTypes.add(db.getType());
            }
        }

        // save dbTypes in file
        DeploymentModel dbTypesModel = TyphonDLFactory.eINSTANCE.createDeploymentModel();
        for (DBType dbType : dbTypes) {
            dbTypesModel.getElements().add(dbType);
        }
        save(dbTypesModel, "dbTypes.tdl");
        Import dbTypesImport = TyphonDLFactory.eINSTANCE.createImport();
        dbTypesImport.setRelativePath("dbTypes.tdl");
        DLmodel.getGuiMetaInformation().add(dbTypesImport);

        // save DBs in file
        for (DB db : result.keySet()) {
            DeploymentModel dbModel = TyphonDLFactory.eINSTANCE.createDeploymentModel();
            dbModel.getElements().add(db);
            save(dbModel, db.getName() + ".tdl");
        }

        /*
         * start container structure
         */
        Platform deployment = TyphonDLFactory.eINSTANCE.createPlatform();
        deployment.setName("platformName");
        deployment.setType(platformType);
        DLmodel.getElements().add(deployment);

        Cluster cluster = TyphonDLFactory.eINSTANCE.createCluster();
        cluster.setName("clusterName");
        cluster.setType(clusterType);
        final String property = properties.getProperty(PropertiesService.POLYSTORE_KUBECONFIG);
        if (!property.isEmpty()) {
            cluster.getProperties().add(ModelService.createKubeconfig(property));
        }
        deployment.getClusters().add(cluster);

        Application application = TyphonDLFactory.eINSTANCE.createApplication();
        application.setName("Polystore");
        cluster.getApplications().add(application);

        Volume_Toplevel toplevelVolume = TyphonDLFactory.eINSTANCE.createVolume_Toplevel();

        // Create containers for non-external DBs
        for (DB db : result.keySet()) {
            Container container = result.get(db);
            if (!db.isExternal()) {
                container.setType(containerType);
                application.getContainers().add(container);
                // add toplevel volume if volume is named
                if (container.getVolumes() != null) {
                    String volumeName = container.getVolumes().getDecls().get(0).getVolumeName();
                    if (!volumeName.isEmpty()) {
                        toplevelVolume.getNames().add(volumeName);
                    }
                }
            }
        }

        if (!toplevelVolume.getNames().isEmpty()) {
            application.setVolumes(toplevelVolume);
        }

        /*
         * save main model file
         */
        String filename = DLmodelName + ".tdl";
        save(DLmodel, filename);
        URI DLmodelURI = URI.createPlatformResourceURI(this.folder.append(filename).toString(), true);
        // return main model file to be opened in editor
        return ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(DLmodelURI.toPlatformString(true)));
    }

    /**
     * Save the given model as {@link Resource}
     * 
     * @param model    The model to save
     * @param filename The name of the file to create
     */
    private void save(DeploymentModel model, String filename) {
        URI uri = URI.createPlatformResourceURI(this.folder.append(filename).toString(), true);
        // delete resource in case it already exists
        if (resourceSet.getResource(uri, false) != null) {
            try {
                resourceSet.getResource(uri, true).delete(Collections.EMPTY_MAP);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Resource resource = resourceSet.createResource(uri);
        resource.getContents().add(model);
        try {
            resource.save(SavingOptions.getTDLoptions());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

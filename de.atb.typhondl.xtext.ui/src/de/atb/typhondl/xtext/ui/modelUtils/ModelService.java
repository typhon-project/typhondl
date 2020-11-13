package de.atb.typhondl.xtext.ui.modelUtils;

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

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.ui.resource.XtextLiveScopeResourceSetProvider;

import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.ClusterType;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Key_ValueArray;
import de.atb.typhondl.xtext.typhonDL.Key_Values;
import de.atb.typhondl.xtext.typhonDL.Platform;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.activator.Activator;
import de.atb.typhondl.xtext.ui.technologies.ITechnology;
import de.atb.typhondl.xtext.ui.technologies.SupportedTechnologies;
import de.atb.typhondl.xtext.ui.technologies.TechnologyFactory;

/**
 * Utility class for easier model object handling
 * 
 * @author flug
 *
 */
public class ModelService {

    /**
     * Finds first {@link Application} in a {@link DeploymentModel}
     * 
     * @param model the model to search through
     * @return the first {@link Application}
     */
    public static Application getFirstApplication(DeploymentModel model) {
        return EcoreUtil2.getAllContentsOfType(model, Application.class).get(0);
    }

    /**
     * Finds a {@link Container} by name
     * 
     * @param model         the model to search through
     * @param containerName name of container to be found
     * @return the found {@link Container} or null
     */
    public static Container getContainer(DeploymentModel model, String containerName) {
        return EcoreUtil2.getAllContentsOfType(model, Container.class).stream()
                .filter(container -> container.getName().equalsIgnoreCase(containerName)).findFirst().orElse(null);
    }

    /**
     * Finds first {@link Platform}
     * 
     * @param model the model to search through
     * @return the first {@link Platform}
     */
    public static Platform getPlatform(DeploymentModel model) {
        return EcoreUtil2.getAllContentsOfType(model, Platform.class).get(0);
    }

    /**
     * Transforms a {@link ClusterType} into its corresponding {@link ITechnology}
     * 
     * @param clusterType the type to transform
     * @return the corresponding {@link ITechnology}
     */
    public static ITechnology getTechnology(ClusterType clusterType) {
        return TechnologyFactory.createTechnology(SupportedTechnologies.valueOf(clusterType.getName()));
    }

    /**
     * Finds first {@link ClusterType}
     * 
     * @param model the model to search through
     * @return the first {@link ClusterType}
     */
    public static ClusterType getClusterType(DeploymentModel model) {
        return EcoreUtil2.getAllContentsOfType(model, ClusterType.class).get(0);
    }

    /**
     * Create a new {@link Key_Values} object
     * 
     * @param name   key
     * @param value  value
     * @param values additional values
     * @return a new {@link Key_Values} object
     */
    public static Key_Values createKey_Values(String name, String value, String[] values) {
        Key_Values key_Values = TyphonDLFactory.eINSTANCE.createKey_Values();
        key_Values.setName(name);
        key_Values.setValue(value);
        if (values != null) {
            for (String string : values) {
                key_Values.getValues().add(string);
            }
        }
        return key_Values;
    }

    /**
     * Create a new {@link Key_ValueArray} object
     * 
     * @param name   key
     * @param values values
     * @return a new {@link Key_ValueArray} object
     */
    public static Key_ValueArray createKeyValuesArray(String name, String[] values) {
        Key_ValueArray array = TyphonDLFactory.eINSTANCE.createKey_ValueArray();
        array.setName(name);
        for (int i = 0; i < values.length; i++) {
            array.getValues().add(values[i]);
        }
        return array;
    }

    /**
     * Create a List containing {@link Key_Values}s with one value each
     * 
     * @param strings [name, value, name, value, ...]
     * @return a List containing {@link Key_Values}s
     */
    public static ArrayList<Key_Values> createListOfKey_Values(String[] strings) {
        ArrayList<Key_Values> list = new ArrayList<>();
        for (int i = 0; i < strings.length; i = i + 2) {
            Key_Values keyValues = TyphonDLFactory.eINSTANCE.createKey_Values();
            keyValues.setName(strings[i]);
            keyValues.setValue(strings[i + 1]);
            list.add(keyValues);
        }
        return list;
    }

    /**
     * Creates a {@link Key_Values} for given path to kubeconfig
     * 
     * @param path the path to the kubeconfig file
     * @return a new {@link Key_Values} with name "kubeconfig" and value path
     */
    public static Key_Values createKubeconfig(String path) {
        Key_Values kubeconfig = TyphonDLFactory.eINSTANCE.createKey_Values();
        kubeconfig.setName("kubeconfig");
        kubeconfig.setValue(addQuotes(path));
        return kubeconfig;
    }

    /**
     * Adds quotes around a String if it doesn't already contains quotes
     * 
     * @param property the String to put quotes around
     * @return the input String with quotes around
     */
    private static String addQuotes(String property) {
        return property.contains("\"") ? property : "\"" + property + "\"";
    }

    /**
     * Gets the XtextResourceSet from given provider and adds all .tdl files
     * contained in the parent folder of given file
     * 
     * @param provider the XtextLiveScopeResourceSetProvider (injected or obtained
     *                 through {@link Activator})
     * @param file     all .tdl siblings of this file get added to the ResourceSet
     * @return the XtextResourceSet containing all .tdl files contained in the
     *         parent folder of given file
     */
    public static XtextResourceSet getResourceSet(XtextLiveScopeResourceSetProvider provider, IResource file) {
        XtextResourceSet resourceSet = (XtextResourceSet) provider.get(file.getProject());
        resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
        // adds all .tdl files in project folder to resourceSet
        IResource members[] = null;
        try {
            members = file.getParent().members();
        } catch (CoreException e) {
            e.printStackTrace();
        }
        for (IResource member : members) {
            if (member instanceof IFile) {
                if (((IFile) member).getFileExtension().equals("tdl")) {
                    resourceSet.getResource(URI.createPlatformResourceURI(member.getFullPath().toString(), true), true);
                }
            }
        }
        return resourceSet;
    }
}

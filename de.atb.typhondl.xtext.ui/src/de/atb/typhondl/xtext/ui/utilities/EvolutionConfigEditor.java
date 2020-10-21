package de.atb.typhondl.xtext.ui.utilities;

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
import java.util.Arrays;
import java.util.List;

import de.atb.typhondl.xtext.ui.properties.PropertiesService;

public class EvolutionConfigEditor {
    public List<InputField> dockerComposeFields = Arrays.asList(
            new InputField("Backend published port: ", PropertiesService.EVOLUTION_BACKEND_PUBLISHEDPORT),
            new InputField("Frontend published port: ", PropertiesService.EVOLUTION_FRONTEND_PUBLISHEDPORT));
    public List<InputField> kubernetesFields = Arrays.asList(
            new InputField("Backend published port: ", PropertiesService.EVOLUTION_BACKEND_PUBLISHEDPORT),
            new InputField("Frontend published port: ", PropertiesService.EVOLUTION_FRONTEND_PUBLISHEDPORT));

    public List<InputField> getInputFields(SupportedTechnologies chosenTemplate) {
        switch (chosenTemplate) {
        // TODO TYP-186
        case DockerCompose:
            return dockerComposeFields;
        case Kubernetes:
            return kubernetesFields;
        default:
            return new ArrayList<>();
        }
    }
}

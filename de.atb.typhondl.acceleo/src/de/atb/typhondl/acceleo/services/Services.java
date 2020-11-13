package de.atb.typhondl.acceleo.services;

/*-
 * #%L
 * de.atb.typhondl.acceleo
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.emf.common.util.BasicMonitor;

import de.atb.typhondl.acceleo.main.Generate;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;

/**
 * Utility class for the script generation process
 * 
 * @author flug
 *
 */
public class Services {

    public static void generateDeployment(DeploymentModel model, File file) throws IOException {
        new Generate(model, file, new ArrayList<String>()).doGenerate(new BasicMonitor());
    }
}

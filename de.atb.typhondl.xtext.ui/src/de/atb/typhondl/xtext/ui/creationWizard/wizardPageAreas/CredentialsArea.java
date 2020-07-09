package de.atb.typhondl.xtext.ui.creationWizard.wizardPageAreas;

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

import java.security.SecureRandom;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.typhonDL.Credentials;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.utilities.Pair;

public class CredentialsArea extends Area {

    private Text usernameText;
    private Text passwordText;

    public CredentialsArea(DB db, Composite parent, Properties properties) {
        super(db, null, -1, parent, "Credentials", properties);
    }

    @Override
    public void createArea() {
        group.setLayout(new GridLayout(3, false));
        GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        GridData gridDataLabels = new GridData(SWT.FILL, SWT.BEGINNING, true, true);
        gridDataLabels.horizontalSpan = 2;

        Credentials credentials;
        if (db.getCredentials() != null) {
            credentials = db.getCredentials();
        } else {
            credentials = setCredentials();
            db.setCredentials(credentials);
        }
        if (credentials != null) {
            new Label(group, SWT.NONE).setText("Username:");
            if (credentials.getUsername().equalsIgnoreCase("chooseUsername")) {
                usernameText = new Text(group, SWT.BORDER);
                usernameText.setLayoutData(gridDataFields);
                usernameText.setText("choseUsername");
                usernameText.addModifyListener(e -> {
                    credentials.setUsername(usernameText.getText());
                });
                new Label(group, SWT.NONE).setText("Can't be \"username\" / \"password\"");
            } else {
                new Label(group, SWT.NONE).setText(credentials.getUsername());
                new Label(group, SWT.NONE).setText("Password can't be \"password\"");
            }

            new Label(group, SWT.NONE).setText("Password:");
            passwordText = new Text(group, SWT.BORDER);
            passwordText.setLayoutData(gridDataFields);
            passwordText.setText(credentials.getPassword());
            passwordText.addModifyListener(e -> {
                credentials.setPassword(passwordText.getText());
            });
            Button createPassword = new Button(group, SWT.PUSH);
            createPassword.setText("Create a random password");
            createPassword.setLayoutData(gridDataFields);
            createPassword.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    passwordText.setText(createPassword(16));
                    credentials.setPassword(passwordText.getText());
                }
            });
        } else {
            setGroupVisible();
        }
    }

    private Credentials setCredentials() {
        if (readCredentials() != null) {
            Credentials credentials = TyphonDLFactory.eINSTANCE.createCredentials();
            credentials.setPassword(readCredentials().firstValue);
            credentials.setUsername(readCredentials().secondValue);
            return credentials;
        } else {
            return null;
        }

    }

    private Pair<String, String> readCredentials() {
        String key = db.getType().getName().toLowerCase() + ".credentials";
        String property = properties.getProperty(key);
        String[] credentials = property.split(",");
        if (credentials == null || credentials.length != 2) {
            return null;
        } else {
            return new Pair<String, String>(credentials[0], credentials[1]);
        }
    }

    private static String createPassword(int length) {
        String dic = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        String result = "";
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(dic.length());
            result += dic.charAt(index);
        }
        return result;
    }
}

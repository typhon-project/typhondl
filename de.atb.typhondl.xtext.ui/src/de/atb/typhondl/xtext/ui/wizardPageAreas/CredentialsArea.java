package de.atb.typhondl.xtext.ui.wizardPageAreas;

import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.Credentials;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.utilities.Pair;

public class CredentialsArea extends Area {

    public CredentialsArea(DB db, Container container, int chosenTechnology, Composite parent, Properties properties) {
        super(db, container, chosenTechnology, parent, "Credentials", properties);
    }

    @Override
    public void createArea() {
        GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);

        Credentials credentials;
        if (db.getCredentials() != null) {
            credentials = db.getCredentials();
        } else {
            credentials = setCredentials();
        }
        if (credentials != null) {
            new Label(group, SWT.NONE).setText("Username:");
            if (credentials.getUsername().equalsIgnoreCase("username")) {
                Text usernameText = new Text(group, SWT.BORDER);
                usernameText.setLayoutData(gridDataFields);
                usernameText.setText(credentials.getUsername());
                usernameText.addModifyListener(e -> {
                    credentials.setUsername(usernameText.getText());
                });
            } else {
                Label userNameLabel = new Label(group, SWT.NONE);
                userNameLabel.setText(credentials.getUsername());
                userNameLabel.setLayoutData(gridDataFields);
            }

            new Label(group, SWT.NONE).setText("Password:");
            Text passwordText = new Text(group, SWT.BORDER);
            passwordText.setLayoutData(gridDataFields);
            passwordText.setText(credentials.getPassword());
            passwordText.addModifyListener(e -> {
                credentials.setPassword(passwordText.getText());
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

}

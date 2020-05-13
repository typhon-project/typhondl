package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.text.templates.TemplateVariable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.IMAGE;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.ui.utilities.PreferenceReader;

public class CreationDatabasePage extends MyWizardPage {

    private DB db;
    private TemplateBuffer buffer;

    protected CreationDatabasePage(String pageName, DB db, TemplateBuffer buffer) {
        super(pageName);
        this.db = db;
        this.buffer = buffer;
    }

    @Override
    public void createControl(Composite parent) {
        setTitle("Database settings for " + db.getName());
        Composite main = new Composite(parent, SWT.NONE);
        main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        main.setLayout(new GridLayout(1, false));

        templateVariableArea(main);

        imageArea(main);

        parameterArea(main);

        setControl(main);
    }

    private void parameterArea(Composite main) {
        // TODO Auto-generated method stub

    }

    private void imageArea(Composite main) {
        Group group = new Group(main, SWT.READ_ONLY);
        group.setLayout(new GridLayout(2, false));
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        group.setText("Image");

        GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        new Label(group, NONE).setText("image used:");
        Text text = new Text(group, SWT.BORDER);
        String imageText = db.getImage() == null ? db.getType().getImage().getValue() : db.getImage().getValue();
        text.setText(imageText);
        text.setLayoutData(gridDataFields);
        text.addModifyListener(e -> {
            if (text.getText().equalsIgnoreCase(db.getType().getImage().getValue())) {
                db.setImage(null);
            } else {
                IMAGE image = TyphonDLFactory.eINSTANCE.createIMAGE();
                image.setValue(text.getText());
                db.setImage(image);
            }
        });

    }

    private void templateVariableArea(Composite main) {
        if (buffer != null) {
            TemplateVariable[] variables = buffer.getVariables();
            List<TemplateVariable> variablesList = new ArrayList<>(Arrays.asList(variables));

            // this is the database.name, which should not be changed, so it is removed from
            // the list:
            if (db.isExternal()) {
                variablesList.removeIf(variable -> variable.getOffsets()[0] == 17);
            } else {
                variablesList.removeIf(variable -> variable.getOffsets()[0] == 9);
            }
            // create a group
            Group group = new Group(main, SWT.READ_ONLY);
            group.setLayout(new GridLayout(2, false));
            group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
            group.setText("Template Variables");

            GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);

            // create a field for each variable:
            for (TemplateVariable templateVariable : variablesList) {
                new Label(group, NONE).setText(templateVariable.getName() + ":");
                Text text = new Text(group, SWT.BORDER);
                text.setText(templateVariable.getName());
                text.setLayoutData(gridDataFields);
                text.addModifyListener(e -> {
                    int variableIndex = variablesList.indexOf(templateVariable);
                    int oldLenght = templateVariable.getLength();
                    // replace old value in template variable
                    templateVariable.setValue(text.getText());
                    int newLength = templateVariable.getLength();
                    // replace old value in pattern string
                    String newPattern = updatePattern(templateVariable, buffer.getString(), oldLenght);
                    // correct offset of other variables if this variable is not the last one
                    if (variableIndex != variablesList.size() - 1) {
                        for (int i = variableIndex + 1; i < variablesList.size(); i++) {
                            variablesList.get(i).setOffsets(
                                    new int[] { variablesList.get(i).getOffsets()[0] + newLength - oldLenght });
                        }
                    }
                    buffer.setContent(newPattern, variablesList.toArray(new TemplateVariable[0]));
                    updateDB(db, buffer);
                });
            }
        }
    }

    /**
     * Replaces old variable value with new one
     * 
     * @param templateVariable The TemplateVariable with the new value
     * @param oldPattern       The old Pattern
     * @param oldLength        The length of the old variable value
     * @return The updated Pattern
     */
    private String updatePattern(TemplateVariable templateVariable, String oldPattern, int oldLength) {
        int offset = templateVariable.getOffsets()[0];
        return oldPattern.substring(0, offset) + templateVariable.getValues()[0]
                + oldPattern.substring(offset + oldLength);
    }

    /**
     * updates the {@link DB} model entity in the <code>result</code> map
     * 
     * @param db             The {@link DB} to be updated
     * @param templateBuffer The source for the updated {@link TemplateVariable}s
     */
    private void updateDB(DB db, TemplateBuffer templateBuffer) {
        DB newDB = PreferenceReader.getModelObject(TyphonDLFactory.eINSTANCE.createDB(), templateBuffer);
        db.getParameters().clear();
        db.getParameters().addAll(newDB.getParameters());
    }

    public String getDBName() {
        return db.getName();
    }

    public void setDB(DB db) {
        this.db = db;
    }

    public void setBuffer(TemplateBuffer templateBuffer) {
        this.buffer = templateBuffer;
    }

}

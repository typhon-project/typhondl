package de.atb.typhondl.xtext.ui.utilities;

import java.util.ArrayList;

import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.ui.creationWizard.CreationDBMSPage;

/**
 * Utility class for {@link CreationDBMSPage}
 * 
 * @author flug
 *
 */
public class WizardFields {

    /**
     * Checkbox for using an existing file or not
     */
    private Button existingModelCheck;

    /**
     * Checkbox for using an external database or not
     */
    private Button externalDatabaseCheck;

    /**
     * Combo to choose {@link DB} template from
     */
    private Combo combo;

    /**
     * List of Pairs of {@link DB} and the {@link TemplateBuffer} of the chosen
     * Template
     */
    private ArrayList<Pair<DB, TemplateBuffer>> templateBuffers;

    /**
     * Creates instance of {@link WizardFields}
     * 
     * @param existingModelCheck    Checkbox for using an existing file or not
     * @param combo                 Combo to choose {@link DB} template from
     * @param templates             List of Pairs of {@link DB} and the
     *                              {@link TemplateBuffer} of the chosen Template
     * @param externalDatabaseCheck Checkbox for using an external database or not
     */
    public WizardFields(Button existingModelCheck, Button externalDatabaseCheck, Combo combo,
            ArrayList<Pair<DB, TemplateBuffer>> templates) {
        this.existingModelCheck = existingModelCheck;
        this.setExternalDatabaseCheck(externalDatabaseCheck);
        this.combo = combo;
        this.templateBuffers = templates;
    }

    /**
     * @param existingModelCheck Checkbox for using an existing file or not
     */
    public void setExistingModelCheck(Button existingModelCheck) {
        this.existingModelCheck = existingModelCheck;
    }

    /**
     * @return Checkbox for using an existing file or not
     */
    public Button getExistingModelCheck() {
        return existingModelCheck;
    }

    /**
     * @param combo Combo to choose {@link DB} template from
     */
    public void setCombo(Combo combo) {
        this.combo = combo;
    }

    /**
     * 
     * @return Combo to choose {@link DB} template from
     */
    public Combo getCombo() {
        return combo;
    }

    /**
     * 
     * @return List of Pairs of {@link DB} and the {@link TemplateBuffer} of the
     *         chosen Template
     */
    public ArrayList<Pair<DB, TemplateBuffer>> getTemplateBuffers() {
        return templateBuffers;
    }

    /**
     * 
     * @return Checkbox for using an external database or not
     */
    public Button getExternalDatabaseCheck() {
        return externalDatabaseCheck;
    }

    /**
     * 
     * @param externalDatabaseCheck Checkbox for using an external database or not
     */
    public void setExternalDatabaseCheck(Button externalDatabaseCheck) {
        this.externalDatabaseCheck = externalDatabaseCheck;
    }
}

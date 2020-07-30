package de.atb.typhondl.xtext.ui.creationWizard.wizardPageAreas;

import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.widgets.Shell;

import de.atb.typhondl.xtext.typhonDL.Volume_Properties;
import de.atb.typhondl.xtext.ui.modelUtils.VolumesService;

public class VolumesDialog extends StatusDialog {

    private Volume_Properties volumeDefinition;
    private boolean edit;

    public VolumesDialog(Volume_Properties columeDefinition, Shell parent, boolean edit) {
        super(parent);
        this.volumeDefinition = columeDefinition;
        this.edit = edit;
        String title = edit ? "Edit Volume" : "New Volume";
        setTitle(title);
    }

    public Volume_Properties getVolumeDefinition() {
        return VolumesService.createTestVolumeProperties();

//        return this.volumeDefinition;
    }

}

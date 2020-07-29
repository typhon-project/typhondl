package de.atb.typhondl.xtext.ui.creationWizard.wizardPageAreas;

import org.eclipse.swt.widgets.Composite;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

public class VolumesArea extends Area {

    public VolumesArea(Container container, SupportedTechnologies chosenTechnology, Composite parent) {
        super(null, container, chosenTechnology, parent, "Volumes", null);
    }

    @Override
    public void createArea() {
        if (!db.isExternal() && db.getHelm() == null) {

        }

    }

}

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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.widgets.TextFactory;
import org.eclipse.jface.widgets.WidgetFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.xtext.common.services.DefaultTerminalConverters;
import org.eclipse.xtext.conversion.ValueConverterException;

import de.atb.typhondl.xtext.typhonDL.Volume_Properties;
import de.atb.typhondl.xtext.ui.activator.Activator;
import de.atb.typhondl.xtext.ui.modelUtils.VolumesService;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

/**
 * Dialog to add and edit Volumes in {@link VolumesArea}
 * 
 * @author flug
 *
 */
public class VolumesDialog extends StatusDialog {

    private Volume_Properties volumeDefinition;
    private Text nameText;
    private Text typeText;
    private Text mountPathText;
    private SupportedTechnologies chosenTechnology;
    private Volume_Properties tempVolumeDefinition;
//    private Text hostPathText;

    public VolumesDialog(Volume_Properties volumeDefinition, Shell parent, boolean edit,
            SupportedTechnologies chosenTechnology) {
        super(parent);
        this.volumeDefinition = volumeDefinition;
        this.tempVolumeDefinition = EcoreUtil.copy(volumeDefinition);
        this.chosenTechnology = chosenTechnology;
        String title = edit ? "Edit Volume" : "New Volume";
        setTitle(title);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite main = new Composite(parent, parent.getStyle());
        main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        main.setLayout(new GridLayout(2, false));
        GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        TextFactory textFactory = WidgetFactory.text(SWT.BORDER).onModify(event -> textModified(event))
                .layoutData(gridDataFields);

        new Label(main, SWT.NONE).setText("Name:");
        this.nameText = textFactory.create(main);
        nameText.setData("type", "name");
        nameText.setText(VolumesService.getVolumesName(this.volumeDefinition));
        new Label(main, SWT.NONE).setText("Type:");
        this.typeText = textFactory.create(main);
        typeText.setData("type", "type");
        if (this.volumeDefinition.getVolumeType() == null) {
            this.volumeDefinition.setVolumeType(chosenTechnology.defaultVolumesType());
        }
        typeText.setText(VolumesService.getVolumesType(this.volumeDefinition));
        new Label(main, SWT.NONE).setText("Path:");
        this.mountPathText = textFactory.create(main);
        if (this.volumeDefinition.getVolumePath() == null) {
            this.volumeDefinition.setVolumePath(VolumesService.createPath("./example/"));
        }
        mountPathText.setData("type", "mountPath");
        this.mountPathText.setText(VolumesService.getVolumesPath(this.volumeDefinition));

//        new Label(main, SWT.NONE).setText("HostPath:");
//        this.hostPathText = textFactory.create(main);
//        hostPathText.setData("type", "hostPath");
//        hostPathText.setText(VolumesService.getHostPath(this.volumeDefinition));

        return main;
    }

    private void textModified(ModifyEvent event) {
        Text now = ((Text) event.widget);
        this.tempVolumeDefinition = VolumesService.setProperty(now, this.tempVolumeDefinition);
        validate();
    }

    @Override
    protected void okPressed() {
        this.volumeDefinition = EcoreUtil.copy(this.tempVolumeDefinition);
        super.okPressed();
    }

    private void validate() {
        this.updateStatus(new Status(IStatus.OK, "VolumesDialog", ""));
        if (this.volumeDefinition.getVolumePath() == null) {
            this.updateStatus(new Status(IStatus.ERROR, "VolumesDialog", "Path has to be given"));
            return;
        }
        if (this.volumeDefinition.getVolumeType() == null) {
            this.updateStatus(
                    new Status(IStatus.ERROR, "VolumesDialog", "If no special volumes type should be given, enter \""
                            + chosenTechnology.defaultVolumesType() + "\" for default volume definition"));
            return;
        }
        if (this.volumeDefinition.getVolumeName() != null) {
            // TODO improve with:
            // https://stackoverflow.com/questions/63341736/how-to-validate-terminal-rule-id-wtih-xtext-iconcretesyntaxvalidator
            String text = this.volumeDefinition.getVolumeName();
            DefaultTerminalConverters valueConverter = Activator.getInstance()
                    .getInjector("de.atb.typhondl.xtext.TyphonDL").getInstance(DefaultTerminalConverters.class);
            try {
                valueConverter.ID().toString(text);
            } catch (ValueConverterException e) {
                this.updateStatus(new Status(IStatus.ERROR, "VolumesDialog", "Invalid Name"));
            }
        }
    }

    public Volume_Properties getVolumeDefinition() {
        return this.volumeDefinition;
    }
}

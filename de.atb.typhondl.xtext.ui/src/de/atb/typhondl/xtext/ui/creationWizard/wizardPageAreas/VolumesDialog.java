package de.atb.typhondl.xtext.ui.creationWizard.wizardPageAreas;

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

import de.atb.typhondl.xtext.typhonDL.Volume_Properties;
import de.atb.typhondl.xtext.ui.modelUtils.VolumesService;

public class VolumesDialog extends StatusDialog {

    private Volume_Properties volumeDefinition;
    private Text nameText;
    private Text typeText;
    private Text mountPathText;
//    private Text hostPathText;

    public VolumesDialog(Volume_Properties volumeDefinition, Shell parent, boolean edit) {
        super(parent);
        this.volumeDefinition = volumeDefinition;
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
        VolumesService.setProperty(now, this.volumeDefinition);
    }

    public Volume_Properties getVolumeDefinition() {
        return this.volumeDefinition;
    }
}

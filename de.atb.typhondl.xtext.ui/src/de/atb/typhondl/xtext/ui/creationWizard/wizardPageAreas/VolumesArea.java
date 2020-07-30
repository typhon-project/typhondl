package de.atb.typhondl.xtext.ui.creationWizard.wizardPageAreas;

import java.util.ArrayList;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;
import de.atb.typhondl.xtext.typhonDL.Volume_Properties;
import de.atb.typhondl.xtext.typhonDL.Volumes;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

public class VolumesArea extends Area {

    private VolumeStore volumesStore;

    public VolumesArea(DB db, Container container, SupportedTechnologies chosenTechnology, Composite parent) {
        super(db, container, chosenTechnology, parent, "Volumes", null);
    }

    @Override
    public void createArea() {
        if (!db.isExternal() && db.getHelm() == null) {

            this.volumesStore = new VolumeStore();
            Volumes testVolume = TyphonDLFactory.eINSTANCE.createVolumes();
//            final Volume_Properties testProps = VolumesService.createTestVolumeProperties();
//            testVolume.getDecls().add(testProps);
//            this.volumesStore.addVolume(testProps);

            Composite tableComposite = new Composite(group, SWT.NONE);
            GridData data = new GridData(GridData.FILL_BOTH);
            data.widthHint = 250;
            data.heightHint = 100;
            tableComposite.setLayoutData(data);

            TableColumnLayout columnLayout = new TableColumnLayout();
            tableComposite.setLayout(columnLayout);
            Table table = new Table(tableComposite,
                    SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);

            table.setHeaderVisible(true);
            table.setLinesVisible(true);

            TableColumn column1 = new TableColumn(table, SWT.NONE);
            column1.setText("Volume path");
            columnLayout.setColumnData(column1, new ColumnWeightData(1, 200));

            TableColumn column2 = new TableColumn(table, SWT.NONE);
            column2.setText("Volume name");
            columnLayout.setColumnData(column2, new ColumnWeightData(2, 100));

            TableColumn column3 = new TableColumn(table, SWT.NONE);
            column3.setText("Volume type");
            columnLayout.setColumnData(column3, new ColumnWeightData(3, 100));

            TableViewer tableViewer = new TableViewer(table);
            tableViewer.setLabelProvider(new VolumesLabelProvider());
            tableViewer.setContentProvider(new VolumesContentProvider(volumesStore));
            tableViewer.setInput(volumesStore);

            Button addButton = new Button(group, SWT.PUSH);
            addButton.setText("Add...");
            addButton.addListener(SWT.Selection, e -> {
                final Volume_Properties decls = editVolumes(TyphonDLFactory.eINSTANCE.createVolume_Properties(), false);
                this.volumesStore.addVolume(decls);
                testVolume.getDecls().add(decls);
                addOrRemoveVolumesFromContainer(testVolume, container);
                tableViewer.refresh();
            });
            Button removeButton = new Button(group, SWT.PUSH);

            tableViewer.addDoubleClickListener(e -> {
                IStructuredSelection selection = tableViewer.getStructuredSelection();
                Object[] objects = selection.toArray();
                if ((objects == null) || (objects.length != 1))
                    return;
                final Volume_Properties decls = editVolumes((Volume_Properties) selection.getFirstElement(), true);
                testVolume.getDecls().add(decls);
                addOrRemoveVolumesFromContainer(testVolume, container);
                tableViewer.refresh();
            });
        }
    }

    private void addOrRemoveVolumesFromContainer(Volumes volume, Container container) {
        if (!volume.getDecls().isEmpty()) {
            container.setVolumes(volume);
        } else {
            container.setVolumes(null);
        }
    }

    private Volume_Properties editVolumes(Volume_Properties volumeDefinition, boolean edit) {
        VolumesDialog dialog = new VolumesDialog(volumeDefinition, this.parent.getShell(), edit);
        if (dialog.open() == Window.OK) {
            return dialog.getVolumeDefinition();
        }
        return null;
    }

    /**
     * Label provider for templates.
     */
    private class VolumesLabelProvider extends LabelProvider implements ITableLabelProvider {

        @Override
        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        @Override
        public String getColumnText(Object element, int columnIndex) {
            Volume_Properties volumeDefinition = (Volume_Properties) element;

            switch (columnIndex) {
            case 0:
                return volumeDefinition.getVolumePath().getValue();
            case 1:
                return volumeDefinition.getVolumeName() != null ? volumeDefinition.getVolumeName() : "";
            case 2:
                return volumeDefinition.getVolumeType() != null ? volumeDefinition.getVolumeType() : "";
            default:
                return ""; //$NON-NLS-1$
            }
        }
    }

    private class VolumesContentProvider implements IStructuredContentProvider {

        private VolumeStore fStore;

        public VolumesContentProvider(VolumeStore volumesStore) {
            this.fStore = volumesStore;
        }

        @Override
        public Object[] getElements(Object input) {
            return fStore.getVolumes(false);
        }

        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            fStore = (VolumeStore) newInput;
        }

        @Override
        public void dispose() {
            fStore = null;
        }

    }

    private class VolumeStore {

        ArrayList<Volume_Properties> volumes;

        private VolumeStore() {
            this.volumes = new ArrayList<>();
        }

        public void addVolume(Volume_Properties volume) {
            this.volumes.add(volume);
        }

        public Volume_Properties[] getVolumes(boolean b) {
            return volumes.toArray(new Volume_Properties[0]);
        }

    }

}

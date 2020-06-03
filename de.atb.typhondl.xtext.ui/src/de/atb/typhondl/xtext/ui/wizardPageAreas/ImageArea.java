package de.atb.typhondl.xtext.ui.wizardPageAreas;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.IMAGE;
import de.atb.typhondl.xtext.typhonDL.TyphonDLFactory;

public class ImageArea extends Area {

    public ImageArea(DB db, Container container, int chosenTechnology, Composite parent) {
        super(db, container, chosenTechnology, parent, "Image used", null);
    }

    @Override
    public void createArea() {
        if (!db.isExternal()) {
            if (group == null) {
                createGroup("Image used");
            }
            GridData gridDataFields = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            new Label(group, SWT.NONE).setText("image used:");
            Text imageText = new Text(group, SWT.BORDER);
            String imageTextValue = getImageValue();
            imageText.setText(imageTextValue);
            imageText.setLayoutData(gridDataFields);
            imageText.addModifyListener(e -> {
                if (imageText.getText().equalsIgnoreCase(db.getType().getImage().getValue())) {
                    db.setImage(null);
                } else {
                    IMAGE image = TyphonDLFactory.eINSTANCE.createIMAGE();
                    image.setValue(imageText.getText());
                    db.setImage(image);
                }
            });
        }
    }

    /**
     * Returns the correct image value
     * 
     * @return The database's image value if an image is given in the database,
     *         otherwise the dbtype's image value
     */
    private String getImageValue() {
        return db.getImage() == null ? db.getType().getImage().getValue() : db.getImage().getValue();
    }
}

package de.atb.typhondl.xtext.ui.creationWizard;

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

import java.util.ArrayList;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.ui.creationWizard.wizardPageAreas.AddressArea;
import de.atb.typhondl.xtext.ui.creationWizard.wizardPageAreas.Area;
import de.atb.typhondl.xtext.ui.creationWizard.wizardPageAreas.CredentialsArea;
import de.atb.typhondl.xtext.ui.creationWizard.wizardPageAreas.EnvironmentArea;
import de.atb.typhondl.xtext.ui.creationWizard.wizardPageAreas.HelmArea;
import de.atb.typhondl.xtext.ui.creationWizard.wizardPageAreas.ImageArea;
import de.atb.typhondl.xtext.ui.creationWizard.wizardPageAreas.PortArea;
import de.atb.typhondl.xtext.ui.creationWizard.wizardPageAreas.PropertyArea;
import de.atb.typhondl.xtext.ui.creationWizard.wizardPageAreas.ReplicaArea;
import de.atb.typhondl.xtext.ui.creationWizard.wizardPageAreas.ResourceArea;
import de.atb.typhondl.xtext.ui.creationWizard.wizardPageAreas.VolumesArea;
import de.atb.typhondl.xtext.ui.modelUtils.ContainerService;
import de.atb.typhondl.xtext.ui.modelUtils.ReplicationService;
import de.atb.typhondl.xtext.ui.technologies.ITechnology;
import de.atb.typhondl.xtext.ui.utilities.Pair;

/**
 * Each Database has a page to define and/or change the image and other
 * properties
 * 
 * @author flug
 *
 */
public class CreationDatabasePage extends MyWizardPage {
    private DB db;
    private Container container;
    private ITechnology chosenTechnology;
    private int pageWidth;
    private Properties properties;
    private ArrayList<Area> areas;
    private Composite main;

    public CreationDatabasePage(String pageName, DB db, ITechnology chosenTechnology, Properties properties,
            int pageWidth) {
        super(pageName);
        this.db = db;
        this.chosenTechnology = chosenTechnology;
        this.properties = properties;
        this.container = createDBContainer();
        this.pageWidth = pageWidth;
        areas = new ArrayList<>();
    }

    private Container createDBContainer() {
        if (db.isExternal()) {
            return null;
        }
        String containerName = ContainerService.createContainerName(db.getName());
        // the containerType get's created and added to the container in ModelCreator,
        // so that all containers have the same containerType instance
        Container newContainer = ContainerService.create(containerName, null, db, containerName + ":" + getPort(),
                db.getHelm() == null ? getVolumeTarget() : null, chosenTechnology);
        return newContainer;
    }

    private String getPort() {
        return properties.getProperty(db.getType().getName().toLowerCase() + ".port");
    }

    private String getVolumeTarget() {
        return properties.getProperty(db.getType().getName().toLowerCase() + ".volumeTarget");
    }

    @Override
    public void createControl(Composite parent) {
        setTitle("Database settings for " + db.getName());
        setDescription("Database Type: " + db.getType().getName());
        ScrolledComposite scrolling = new ScrolledComposite(parent, SWT.V_SCROLL);
        main = new Composite(scrolling, SWT.NONE);
        scrolling.setContent(main);
        scrolling.setExpandVertical(true);
        scrolling.setExpandHorizontal(true);
        main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        main.setLayout(new GridLayout(1, false));

        this.container = createDBContainer();

        addAreasToList();

        main.setSize(main.computeSize(pageWidth, SWT.DEFAULT));
        scrolling.setMinSize(main.computeSize(pageWidth, SWT.DEFAULT));

        setControl(scrolling);
    }

    private void addAreasToList() {
        if (!isInList(CredentialsArea.class)) {
            areas.add(new CredentialsArea(db, main, properties));
        }

        if (!isInList(EnvironmentArea.class) && !db.isExternal() && db.getEnvironment() != null
                && db.getHelm() == null) {
            areas.add(new EnvironmentArea(db, chosenTechnology, main));
        }

        if (!isInList(HelmArea.class) && chosenTechnology.canUseHelm() && db.getHelm() != null && !db.isExternal()) {
            areas.add(new HelmArea(db, chosenTechnology, main));
        }

        if (!isInList(ImageArea.class) && !db.isExternal() && db.getHelm() == null) {
            areas.add(new ImageArea(db, main));
        }

        if (!isInList(PropertyArea.class) && !db.getParameters().isEmpty() && db.getHelm() == null) {
            areas.add(new PropertyArea(db, main));
        }

        if (!isInList(AddressArea.class) && db.isExternal() && db.getHelm() == null) {
            areas.add(new AddressArea(db, main));
        }

        if (!isInList(ResourceArea.class) && !db.isExternal() && db.getHelm() == null) {
            areas.add(new ResourceArea(db, container, main));
        }

        if (!isInList(PortArea.class) && !db.isExternal() && db.getHelm() == null) {
            areas.add(new PortArea(db, container, chosenTechnology, main, properties, this));
        }

        if (!isInList(ReplicaArea.class) && !db.isExternal() && !ReplicationService
                .getReplicationProperty(db.getType().getName().toLowerCase(), chosenTechnology, properties).isEmpty()
                && db.getHelm() == null) {
            areas.add(new ReplicaArea(db, container, chosenTechnology, main, properties));
        }
        if (!isInList(VolumesArea.class) && !db.isExternal() && db.getHelm() == null) {
            areas.add(new VolumesArea(db, container, chosenTechnology, main));
        }
    }

    private <T> boolean isInList(Class<T> areaClass) {
        for (Area area : areas) {
            if (areaClass.isInstance(area)) {
                return true;
            }
        }
        return false;
    }

    public String getDBName() {
        return db.getName();
    }

    /**
     * Updates the database page. <br>
     * After something has changed on the DBMS page, the widgets on the database
     * page have to be updated.
     */
    public void updateAllAreas() {
        setDescription("Database Type: " + db.getType().getName());
        this.container = createDBContainer();
        addAreasToList();
        for (Area area : areas) {
            area.updateData(db, container);
            area.updateArea();
        }
        ((Composite) this.getControl()).layout();
    }

    public ITechnology getChosenTechnology() {
        return this.chosenTechnology;
    }

    public void setChosenTechnology(ITechnology chosenTechnology) {
        this.chosenTechnology = chosenTechnology;
    }

    public void setDB(DB db) {
        this.db = db;
    }

    public Pair<DB, Container> getResultPair() {
        return new Pair<DB, Container>(db, container);
    }

}

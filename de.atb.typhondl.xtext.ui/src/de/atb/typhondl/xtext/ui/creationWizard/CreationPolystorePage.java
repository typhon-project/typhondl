package de.atb.typhondl.xtext.ui.creationWizard;

import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.atb.typhondl.xtext.ui.properties.PropertiesService;
import de.atb.typhondl.xtext.ui.utilities.SupportedTechnologies;

public class CreationPolystorePage extends MyWizardPage {

    private Properties properties;
    private Composite main;
    private SupportedTechnologies chosenTechnology;
    private static final int pageWidth = 607;

    protected CreationPolystorePage(String pageName, Properties properties, SupportedTechnologies chosenTechnology) {
        super(pageName);
        this.properties = properties;
        this.chosenTechnology = chosenTechnology;
    }

    @Override
    public void createControl(Composite parent) {
        setTitle("Configure Polystore Components");
        setDescription("API, QL");
        ScrolledComposite scrolling = new ScrolledComposite(parent, SWT.V_SCROLL);
        main = new Composite(scrolling, SWT.NONE);
        scrolling.setContent(main);
        scrolling.setExpandVertical(true);
        scrolling.setExpandHorizontal(true);
        main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        main.setLayout(new GridLayout(1, false));

        createQLGroup();
        createAPIGroup();

        if (chosenTechnology == SupportedTechnologies.Kubernetes) {
            createLoggingAndMonitoringGroup();
        }

        main.setSize(main.computeSize(pageWidth, SWT.DEFAULT));
        scrolling.setMinSize(main.computeSize(pageWidth, SWT.DEFAULT));

        setControl(scrolling);

    }

    private void createAPIGroup() {

        Group apiGroup = new Group(main, SWT.READ_ONLY);
        apiGroup.setLayout(new GridLayout(2, false));
        apiGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        apiGroup.setText("API settings");

        createScalingFields(apiGroup, "API", PropertiesService.API_REPLICAS);
    }

    private void createQLGroup() {

        Group qlGroup = new Group(main, SWT.READ_ONLY);
        qlGroup.setLayout(new GridLayout(2, false));
        qlGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        qlGroup.setText("QL Settings");

        createScalingFields(qlGroup, "QL Server", PropertiesService.QLSERVER_REPLICAS);
    }

    private void createLoggingAndMonitoringGroup() {
        // TODO Auto-generated method stub

    }

    private void createScalingFields(Group group, String component, String property) {
        GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        new Label(group, SWT.NONE).setText(component + " replicas: ");
        Text rep = new Text(group, SWT.BORDER);
        rep.setText("1");
        rep.setLayoutData(gridData);
        rep.addModifyListener(e -> properties.setProperty(property, rep.getText()));
    }

}

package de.atb.typhondl.xtext.ui.refactoring;

import java.util.Properties;

import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.widgets.Shell;

import de.atb.typhondl.xtext.typhonDL.ClusterType;

public class ChangeAnalyticsDialog extends StatusDialog {

    private Properties properties;
    private Shell parent;
    private ClusterType clusterType;

    public ChangeAnalyticsDialog(Shell parent, Properties properties, ClusterType clusterType) {
        super(parent);
        this.properties = properties;
        this.parent = parent;
        this.clusterType = clusterType;
    }

    public Properties getProperties() {
        return properties;
    }

}

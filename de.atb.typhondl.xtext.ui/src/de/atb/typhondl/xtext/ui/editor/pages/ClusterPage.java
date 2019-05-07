package de.atb.typhondl.xtext.ui.editor.pages;

import de.atb.typhondl.xtext.typhonDL.Cluster;
import de.atb.typhondl.xtext.ui.editor.EditorPage;

public class ClusterPage extends EditorPage {

	public ClusterPage(Cluster cluster) {
		super("Cluster " + cluster.getName());
	}

}

package de.atb.typhondl.xtext.ui.editor.pages;

import de.atb.typhondl.xtext.typhonDL.Cluster;

public class ClusterPage extends EditorPage {

	public ClusterPage(Cluster cluster) {
		super("Cluster " + cluster.getName());
	}

}

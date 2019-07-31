package de.atb.typhondl.xtext.ui.utilities;

import java.util.ArrayList;
import java.util.stream.Collectors;

import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.Cluster;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.Deployment;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Reference;
import de.atb.typhondl.xtext.typhonDL.Software;

public class DLmodelReader {

	public static ArrayList<DB> getDBs(DeploymentModel model) {
		ArrayList<DB> dbs = new ArrayList<DB>();
		Deployment deployment = model.getElements().stream().filter(element -> Deployment.class.isInstance(element))
				.map(element -> (Deployment) element).collect(Collectors.toList()).get(0);
		for (Cluster cluster : deployment.getClusters()) {
			for (Application application : cluster.getApplications()) {
				for (Container container : application.getContainers()) {
					for (Reference reference : container.getDeploys()) {
						Software software = reference.getReference();
						if (DB.class.isInstance(software) && !software.getName().equals("polystoredb")) {
							dbs.add((DB) software);
						}
					}
				}
			}
		}
		return dbs;
	}
}

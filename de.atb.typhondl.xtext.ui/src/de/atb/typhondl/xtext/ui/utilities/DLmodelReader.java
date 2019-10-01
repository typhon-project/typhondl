package de.atb.typhondl.xtext.ui.utilities;

import java.util.ArrayList;
import java.util.stream.Collectors;

import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.Cluster;
import de.atb.typhondl.xtext.typhonDL.Container;
import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Platform;
import de.atb.typhondl.xtext.typhonDL.Services;

public class DLmodelReader {

	public static ArrayList<DB> getDBs(DeploymentModel model) {
		ArrayList<DB> dbs = new ArrayList<DB>();
		Platform platform = model.getElements().stream().filter(element -> Platform.class.isInstance(element))
				.map(element -> (Platform) element).collect(Collectors.toList()).get(0);
		for (Cluster cluster : platform.getClusters()) {
			for (Application application : cluster.getApplications()) {
				for (Container container : application.getContainers()) {
					Services services = container.getDeploys().getReference();
					if (DB.class.isInstance(services) && !services.getName().equals("polystoredb")) {
						dbs.add((DB) services);
					}
				}
			}
		}
		return dbs;
	}
}

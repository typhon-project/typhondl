package de.atb.typhondl.xtext.ui.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Import;

public class DLmodelReader {

	public static ArrayList<DB> getDBs(DeploymentModel model) {
		ArrayList<DB> dbs = new ArrayList<DB>();
		List<Import> dbModelList = model.getGuiMetaInformation().stream()
				.filter(metaModel -> Import.class.isInstance(metaModel)).map(metaModel -> (Import) metaModel)
				.filter(info -> info.getRelativePath().endsWith(".tdl")).collect(Collectors.toList());
		for (Import importedDB : dbModelList) {
			URI uri = model.eResource().getURI().trimSegments(1).appendSegment(importedDB.getRelativePath());
			dbs.addAll(
					((DeploymentModel) model.eResource().getResourceSet().getResource(uri, true).getContents().get(0))
							.getElements().stream().filter(element -> DB.class.isInstance(element))
							.map(element -> (DB) element).collect(Collectors.toList()));
		}
		return dbs;
	}
}

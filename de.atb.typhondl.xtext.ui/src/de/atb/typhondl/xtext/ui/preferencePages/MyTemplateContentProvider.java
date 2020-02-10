package de.atb.typhondl.xtext.ui.preferencePages;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.templates.persistence.TemplatePersistenceData;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

@SuppressWarnings("deprecation")
class MyTemplateContentProvider implements IStructuredContentProvider {

	/** The template store. */
	private TemplateStore fStore;
	private String contextID;

	public MyTemplateContentProvider(String contextID) {
		this.contextID = contextID;
	}

	@Override
	public Object[] getElements(Object input) {
		TemplatePersistenceData[] templateData = fStore.getTemplateData(false);
		List<TemplatePersistenceData> onlyContextTemplateData = new ArrayList<TemplatePersistenceData>();
		for (int i = 0; i < templateData.length; i++) {
			if (templateData[i].getTemplate().getContextTypeId().equalsIgnoreCase(this.contextID)) {
				onlyContextTemplateData.add(templateData[i]);
			}
		}
		return onlyContextTemplateData.toArray();
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		fStore= (TemplateStore) newInput;
	}

	@Override
	public void dispose() {
		fStore= null;
	}

}

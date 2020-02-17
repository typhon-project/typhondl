package de.atb.typhondl.xtext.ui.preferencePages;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.eclipse.jface.text.templates.persistence.TemplatePersistenceData;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Provides the TableViewer with content. Only provides content from
 * {@link TemplatePersistenceData} with fitting contextID.
 * 
 * @author flug
 *
 */
@SuppressWarnings("deprecation")
class MyTemplateContentProvider implements IStructuredContentProvider {

	/**
	 * The template store containing the {@link TemplatePersistenceData}
	 */
	private TemplateStore fStore;

	/**
	 * The contextID to filter the data
	 */
	private String contextID;

	/**
	 * Creates instance of {@link MyTemplateContentProvider}.
	 * 
	 * @param contextID The context (i.e. Model type) to use as a filter.
	 */
	public MyTemplateContentProvider(String contextID) {
		this.contextID = contextID;
	}

	@Override
	public Object[] getElements(Object input) {
		TemplatePersistenceData[] templateData = fStore.getTemplateData(false);
		return Arrays.asList(templateData).stream()
				.filter(data -> data.getTemplate().getContextTypeId().equalsIgnoreCase(this.contextID))
				.collect(Collectors.toList()).toArray(new TemplatePersistenceData[0]);
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		fStore = (TemplateStore) newInput;
	}

	@Override
	public void dispose() {
		fStore = null;
	}

}

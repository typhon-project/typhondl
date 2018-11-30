package org.typhon.dsls.xtext.formatting;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.formatting2.FormatterPreferenceKeys;
import org.eclipse.xtext.preferences.IPreferenceValues;
import org.eclipse.xtext.preferences.IPreferenceValuesProvider;
import org.eclipse.xtext.preferences.PreferenceKey;

import com.google.inject.Inject;

@SuppressWarnings("restriction")
public class PooslFormatterPreferenceProvider implements IPreferenceValuesProvider {
	
	@Inject
	private IPreferenceValuesProvider valuesProvider;
	
	@Override
	public IPreferenceValues getPreferenceValues(final Resource resource) {
		// TODO Auto-generated method stub
		final String indent = "  ";
		final IPreferenceValues preferenceValues = valuesProvider.getPreferenceValues(resource);
		return new IPreferenceValues() {
			
			@Override
			public String getPreference(PreferenceKey key) {
				if (key == FormatterPreferenceKeys.indentation) {
					return indent;
				}
				if (key == FormatterPreferenceKeys.tabWidth) {
					return "2";
				}
				return preferenceValues.getPreference(key);
			}
		};
	}

}

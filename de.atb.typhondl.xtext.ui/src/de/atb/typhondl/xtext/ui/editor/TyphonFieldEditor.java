package de.atb.typhondl.xtext.ui.editor;

import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;

public class TyphonFieldEditor extends StringFieldEditor {

	/**
	 * Creates a string field editor. Use the method <code>setTextLimit</code> to
	 * limit the text.
	 *
	 * @param name      the name of the preference this field editor works on
	 * @param labelText the label text of the field editor
	 * @param width     the width of the text input field in characters, or
	 *                  <code>UNLIMITED</code> for no limit
	 * @param strategy  either <code>VALIDATE_ON_KEY_STROKE</code> to perform on the
	 *                  fly checking (the default), or
	 *                  <code>VALIDATE_ON_FOCUS_LOST</code> to perform validation
	 *                  only after the text has been typed in
	 * @param parent    the parent of the field editor's control
	 * @since 2.0
	 */
	public TyphonFieldEditor(String name, String labelText, int width, int strategy, Composite parent) {
		super(name, labelText, width, strategy, parent);
	}

	/**
	 * Creates a string field editor. Use the method <code>setTextLimit</code> to
	 * limit the text.
	 *
	 * @param name      the name of the preference this field editor works on
	 * @param labelText the label text of the field editor
	 * @param width     the width of the text input field in characters, or
	 *                  <code>UNLIMITED</code> for no limit
	 * @param parent    the parent of the field editor's control
	 */
	public TyphonFieldEditor(String name, String labelText, int width, Composite parent) {
		this(name, labelText, width, VALIDATE_ON_KEY_STROKE, parent);
	}

	/**
	 * Creates a string field editor of unlimited width. Use the method
	 * <code>setTextLimit</code> to limit the text.
	 *
	 * @param name      the name of the preference this field editor works on
	 * @param labelText the label text of the field editor
	 * @param parent    the parent of the field editor's control
	 */
	public TyphonFieldEditor(String name, String labelText, Composite parent) {
		this(name, labelText, UNLIMITED, parent);
	}

	@Override
	protected void doStore() {
		if (!this.getPreferenceStore().getString(getPreferenceName()).equals(getTextControl().getText())) {
			super.doStore();
		}
	}
}

package de.atb.typhondl.xtext.ui.editor.pages;

import java.util.ArrayList;

import org.eclipse.jface.preference.IntegerFieldEditor;

import de.atb.typhondl.xtext.typhonDL.DB;
import de.atb.typhondl.xtext.typhonDL.DeploymentModel;
import de.atb.typhondl.xtext.typhonDL.Element;
import de.atb.typhondl.xtext.ui.editor.EditorPage;

public class MyOverview extends EditorPage {

	private DeploymentModel model;

	public MyOverview(DeploymentModel model) {
		super("Overview");
		this.model = model;
	}

	/**
	 * JFace provides nine concrete subclasses of FieldEditor. Each corresponds to a
	 * common widget grouping used to present a preference. To illustrate some of
	 * these field editors, a screenshot of a preference page for an imaginary HTML
	 * editor has been included. The available concrete field editors are:
	 * 
	 * BooleanFieldEditor - This field editor takes the form of a checkbox with a
	 * label. It is used for presenting boolean preferences. IntegerFieldEditor -
	 * This field editor takes the form of a label and a text field. It is intended
	 * for presenting integers, so any non-integer input is considered invalid. You
	 * can further refine the field validation by specifying a valid range for the
	 * integer using setValidRange(int, int).
	 * 
	 * StringFieldEditor - This field editor is very similar to IntegerFieldEditor.
	 * StringFieldEditor is a more general-purpose field editor with simpler
	 * validation rules. Normally, any string is valid. However, you can specify
	 * whether an empty string is valid or not by calling
	 * setEmptyStringAllowed(boolean).
	 * 
	 * 
	 * RadioGroupFieldEditor - This field editor takes the form of a group label and
	 * a set of radio buttons, each with a label of its own. It is used to present a
	 * set of mutually exclusive choices. A RadioGroupFieldEditor may be optionally
	 * contained by a Group control which adds an etched border around the field
	 * editor.
	 * 
	 * ColorFieldEditor - This field editor is used to present color preferences. It
	 * consists of a label and a button which displays the currently selected color.
	 * Clicking on the button opens a ColorDialog, allowing the user to choose
	 * another color.
	 * 
	 * FontFieldEditor - This field editor is used to select font preferences. It
	 * consists of a label for the preference, a read-only text field containing the
	 * name, style, and size of the font, and a button with the label "Change...".
	 * When the button is clicked, a FontDialog opens, allowing the user to choose a
	 * new font, change the font's style, and change it's size.
	 * 
	 * DirectoryFieldEditor - This field editor is used to display a directory path
	 * in the file system. It consists of a label, a text field containing the
	 * directory path, and a button with the label "Browse...". Clicking on this
	 * button allows the user to browse the file system and choose a new directory.
	 * 
	 * FileFieldEditor - This field editor is similar to a DirectoryFieldEditor, but
	 * it allows the user to select a file instead of a directory.
	 * 
	 * PathEditor - This field editor is the most complex concrete field editor
	 * supplied by JFace. It consists of a title label, a list of directory paths,
	 * and a button bank to the left of the list with four buttons: "New...",
	 * "Remove", "Up", and "Down". The "New..." button allows the user to browse the
	 * file system and add a new path to the list. The "Remove" button removes the
	 * current selection from the list. The "Up" and "Down" buttons allow the user
	 * to change the order of the list items by moving the selected item up or down.
	 * This field editor is not often used, but it is approprite when a preference
	 * keeps track of several paths such as the class paths for a compiler.
	 */

	@Override
	protected void createFieldEditors() {
		ArrayList<DB> allDatabases = getDBs();

		for (DB db : allDatabases) {
			IntegerFieldEditor indentSpaces = new IntegerFieldEditor("testField",
					db.getName(), getFieldEditorParent());
			indentSpaces.setValidRange(0, 10);
			addField(indentSpaces);
		}

	}

	private ArrayList<DB> getDBs() {
		ArrayList<DB> dbs = new ArrayList<DB>();
		for (Element element : model.getElements()) {
			// TODO not nice
			if (element.eClass().getInstanceClassName().equals("de.atb.typhondl.xtext.typhonDL.DB")) {
				dbs.add((DB) element);
			}
		}
		return dbs;
	}

}

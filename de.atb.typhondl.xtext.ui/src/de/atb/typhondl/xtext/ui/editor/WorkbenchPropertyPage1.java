package de.atb.typhondl.xtext.ui.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.xtext.AbstractRule;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.Parameter;
import org.eclipse.xtext.ParserRule;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.ui.util.FileOpener;
import org.eclipse.xtext.ui.wizard.template.TemplateLabelProvider;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;

import de.atb.typhondl.xtext.typhonDL.Application;
import de.atb.typhondl.xtext.typhonDL.DB;

@SuppressWarnings("restriction")
public class WorkbenchPropertyPage1 extends PropertyPage implements IWorkbenchPropertyPage {


	@Inject
	private TemplateLabelProvider labelProvider;
	@Inject
	private FileOpener fileOpener;
	@Inject
	private IGrammarAccess grammarAccess;
	
	public WorkbenchPropertyPage1() {
		super();
	}

	@Override
	protected Control createContents(Composite parent) {
		
		
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		main.setLayout(new GridLayout(2, false));
		
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		
		createDatabaseArea(main, gridData);

		Label folderLabel = new Label(main, SWT.NONE);
		folderLabel.setText("Test1");
		Label folderText = new Label(main, SWT.NONE);
		folderText.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, true, false));

		Label fileLabel = new Label(main, SWT.NONE);
		fileLabel.setText("Test2");
		Text fileText = new Text(main, SWT.BORDER);
		fileText.setLayoutData(gridData);
		
		
		return null; 
	}

	private void createDatabaseArea(Composite main, GridData gridData) {
		
		EList<AbstractRule> rules = grammarAccess.getGrammar().getRules();
		//rules.iterator().forEachRemaining(rule -> new Label(main, SWT.NONE).setText(rule.getName()));
		ParserRule dbRule = null;
		for (AbstractRule rule : rules) {
			System.out.println("Rule: " + rule.getName());
			if (rule.getName().equals("DB")) {
				System.out.println("Yaaay");
				dbRule = (ParserRule) rule;
			}
		}
		System.out.println("dbRule: " + dbRule.getName());
		EList<Parameter> objects = dbRule.getParameters();
		System.out.println("------------------------ Objects ------------------------");
		objects.iterator().forEachRemaining(object -> System.out.println(object.getName()));
		System.out.println("------------------------- Calls -------------------------");
		List<RuleCall> dbCalls = grammarAccess.findRuleCalls(dbRule);
		dbCalls.iterator().forEachRemaining(call -> System.out.println(call.toString()));
		
		Resource resource = grammarAccess.getGrammar().eResource();
		Iterable<DB> _filter = Iterables.<DB>filter(IteratorExtensions.<EObject>toIterable(resource.getAllContents()), DB.class);
		
		
	}

}

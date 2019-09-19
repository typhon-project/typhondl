	
package org.typhon.client.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.typhon.client.model.*;
import org.typhon.client.service.*;

public class RelationTest {

	AppService appService;
	TextWarningService textwarningService;
	WarningService warningService;

	@Before
	public void init() {
		appService  = new AppService("http://localhost:8091");
		textwarningService  = new TextWarningService("http://localhost:8092");
		warningService  = new WarningService("http://localhost:8093");
	}
	
	@Test
	public void testFindAppById() {
		App p = appService.findById("1"
		);
		List<Warning> warnings = p.getWarningObj();
		System.out.println(String.format("The App is related with Warning:"));
		for (Warning iter : warnings) {
				System.out.println("\t- " + iter.getTime());
				System.out.println("\t- " + iter.getWarning_id());
				System.out.println("\t- " + iter.getWarningType());
				System.out.println("\t- " + iter.getSeverity());
				System.out.println("\t- " + iter.getArea());
		}	
		List<TextWarning> textwarnings = p.getTextWarningObj();
		System.out.println(String.format("The App is related with TextWarning:"));
		for (TextWarning iter : textwarnings) {
				System.out.println("\t- " + iter.getTimeStamp());
				System.out.println("\t- " + iter.getTextWarning_id());
				System.out.println("\t- " + iter.getText());
		}	
	}
	@Test
	public void testFindTextWarningById() {
		TextWarning p = textwarningService.findById("1"
		);
	}
	@Test
	public void testFindWarningById() {
		Warning p = warningService.findById("1"
		);
	}
}

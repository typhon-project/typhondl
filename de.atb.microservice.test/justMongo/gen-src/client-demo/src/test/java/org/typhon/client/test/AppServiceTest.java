package org.typhon.client.test;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.hateoas.PagedResources;
import org.typhon.client.model.*;
import org.typhon.client.service.*;

public class AppServiceTest {
	AppService appService;
	WarningService warningsService;
	TextWarningService textwarningsService;

	@Before
	public void init() {
		appService = new AppService("http://localhost:8080");
		warningsService = new WarningService("http://localhost:8080");
		textwarningsService = new TextWarningService("http://localhost:8080");
	}

	//Stub for object creation
	@Test
	@Ignore
	public void testCreateApp() {
		App objToCreate = new App();
		//TODO set the objToCreate attributes

		objToCreate.setTimeStamp(new Date());
		objToCreate.setVehicle_id("v_01");
		objToCreate.setVehicle_position(new Double(123456));
		List<String> warnings = new ArrayList<String>();
		Warning warning1 = new Warning();
		warning1.setArea(new Double(12345));
		warning1.setSeverity(2);
		warning1.setTime(new Date());
		warning1.setWarning_id("w_01");
		warning1.setWarningType("wind");
		Warning warning1_created = warningsService.create(warning1);
		warnings.add(warning1_created.getWarning_id());
		objToCreate.setWarnings(warnings);
		List<String> textwarnings = new ArrayList<String>();
		objToCreate.setTextwarnings(textwarnings);
		appService.create(objToCreate);
	}
		
	@Test
	public void testFindAll() {
		assertNotNull(appService.findAll(1,5,"ASC"));
		PagedResources<App> v = appService.findAll(1,5,"ASC");
		v.getLinks();
	}
	
}

	

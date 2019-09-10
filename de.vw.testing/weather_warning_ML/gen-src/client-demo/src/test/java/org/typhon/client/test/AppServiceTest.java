package org.typhon.client.test;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.PagedResources;
import org.typhon.client.model.*;
import org.typhon.client.service.*;

public class AppServiceTest {
	AppService appService;
	WarningService warningsService;

	@Before
	public void init() {
		appService = new AppService("http://localhost:8080");
		warningsService = new WarningService("http://localhost:8080");
	}

	//Stub for object creation
	@Test
	public void testCreateApp() {
		App objToCreate = new App();
		//TODO set the objToCreate attributes
		
		appService.create(objToCreate);
	}
		
	@Test
	public void testFindAll() {
		assertNotNull(appService.findAll(1,5,"ASC"));
		PagedResources<App> v = appService.findAll(1,5,"ASC");
		v.getLinks();
	}
	
}

	

package org.typhon.client.test;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.PagedResources;
import org.typhon.client.model.*;
import org.typhon.client.service.*;

public class WarningServiceTest {
	WarningService warningService;

	@Before
	public void init() {
		warningService = new WarningService("http://localhost:8080");
	}

	//Stub for object creation
	@Test
	public void testCreateWarning() {
		Warning objToCreate = new Warning();
		//TODO set the objToCreate attributes
		
		warningService.create(objToCreate);
	}
		
	@Test
	public void testFindAll() {
		assertNotNull(warningService.findAll(1,5,"ASC"));
		PagedResources<Warning> v = warningService.findAll(1,5,"ASC");
		v.getLinks();
	}
	
}

	

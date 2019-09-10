package org.typhon.client.test;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.PagedResources;
import org.typhon.client.model.*;
import org.typhon.client.service.*;

public class TextWarningServiceTest {
	TextWarningService textwarningService;

	@Before
	public void init() {
		textwarningService = new TextWarningService("http://localhost:8080");
	}

	//Stub for object creation
	@Test
	public void testCreateTextWarning() {
		TextWarning objToCreate = new TextWarning();
		//TODO set the objToCreate attributes
		
		textwarningService.create(objToCreate);
	}
		
	@Test
	public void testFindAll() {
		assertNotNull(textwarningService.findAll(1,5,"ASC"));
		PagedResources<TextWarning> v = textwarningService.findAll(1,5,"ASC");
		v.getLinks();
	}
	
}

	

package org.typhon.client.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.typhon.client.model.App;
import org.typhon.client.model.dto.AppDTO;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AppService {
	private static final Logger logger = LoggerFactory.getLogger(AppService.class);
	private String baseUrl;
	ModelMapper modelMapper;
	private RestTemplate restTemplate;

	public AppService(String baseUrl) {
		this.baseUrl = baseUrl;
		restTemplate = restTemplate();
		modelMapper = new ModelMapper();
	}

	RestTemplate restTemplate() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new Jackson2HalModule());
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
		messageConverter.setObjectMapper(objectMapper);
		messageConverter.setSupportedMediaTypes(Arrays.asList(MediaTypes.HAL_JSON));
		return new RestTemplate(Arrays.asList(messageConverter));
	}

	public App findById(String
	 id) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/app/" + id);
		String uriBuilder = builder.build().encode().toUriString();
		AppDTO appDTO = restTemplate
				.exchange(uriBuilder, HttpMethod.GET, null, new ParameterizedTypeReference<AppDTO>() {
				}).getBody();
		App app = modelMapper.map(appDTO, App.class);
		return app;
	}
	
	public void delete(App objToDelete) {
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/app/" + objToDelete.getId());
			String uriBuilder = builder.build().encode().toUriString();
			restTemplate.delete(uriBuilder);
		}
		catch(HttpClientErrorException e) {
			logger.error(e.getMessage());
		}
	}

	public PagedResources<App> findAll(int page, int size, String order) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/app").queryParam("page", page)
				.queryParam("size", size).queryParam("order", order);
		String uriBuilder = builder.build().encode().toUriString();
		PagedResources<AppDTO> queryResult = restTemplate.exchange(uriBuilder, HttpMethod.GET, null,
				new ParameterizedTypeReference<PagedResources<AppDTO>>() {
				}).getBody();
		List<App> objList = new ArrayList<App>();
		queryResult.forEach(z -> objList.add(modelMapper.map(z, App.class)));
		PagedResources<App> result = new PagedResources<App>(objList, queryResult.getMetadata(),
				new ArrayList<Link>());
		return result;
	}

	public App create(App objToCreate) {
		AppDTO p = modelMapper.map(objToCreate, AppDTO.class);
		HttpEntity<AppDTO> request = new HttpEntity<>(p);
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/app");
		String uriBuilder = builder.build().encode().toUriString();
		ResponseEntity<AppDTO> response = restTemplate.exchange(uriBuilder, HttpMethod.POST, request, AppDTO.class);
		AppDTO foo = response.getBody();
		objToCreate = modelMapper.map(foo, App.class);
		return objToCreate;
	}	
	
	public App update(App objToUpdate) {
		AppDTO p = modelMapper.map(objToUpdate, AppDTO.class);
		HttpEntity<AppDTO> request = new HttpEntity<>(p);
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/app/" + objToUpdate.getId());
		String uriBuilder = builder.build().encode().toUriString();
		ResponseEntity<AppDTO> response = restTemplate.exchange(uriBuilder, HttpMethod.PUT, request, AppDTO.class);
		AppDTO foo = response.getBody();
		objToUpdate = modelMapper.map(foo, App.class);
		return objToUpdate;
	}
}

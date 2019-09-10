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
import org.typhon.client.model.Warning;
import org.typhon.client.model.dto.WarningDTO;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WarningService {
	private static final Logger logger = LoggerFactory.getLogger(WarningService.class);
	private String baseUrl;
	ModelMapper modelMapper;
	private RestTemplate restTemplate;

	public WarningService(String baseUrl) {
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

	public Warning findById(int
	 id) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/warning/" + id);
		String uriBuilder = builder.build().encode().toUriString();
		WarningDTO warningDTO = restTemplate
				.exchange(uriBuilder, HttpMethod.GET, null, new ParameterizedTypeReference<WarningDTO>() {
				}).getBody();
		Warning warning = modelMapper.map(warningDTO, Warning.class);
		return warning;
	}
	
	public void delete(Warning objToDelete) {
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/warning/" + objToDelete.getId());
			String uriBuilder = builder.build().encode().toUriString();
			restTemplate.delete(uriBuilder);
		}
		catch(HttpClientErrorException e) {
			logger.error(e.getMessage());
		}
	}

	public PagedResources<Warning> findAll(int page, int size, String order) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/warning").queryParam("page", page)
				.queryParam("size", size).queryParam("order", order);
		String uriBuilder = builder.build().encode().toUriString();
		PagedResources<WarningDTO> queryResult = restTemplate.exchange(uriBuilder, HttpMethod.GET, null,
				new ParameterizedTypeReference<PagedResources<WarningDTO>>() {
				}).getBody();
		List<Warning> objList = new ArrayList<Warning>();
		queryResult.forEach(z -> objList.add(modelMapper.map(z, Warning.class)));
		PagedResources<Warning> result = new PagedResources<Warning>(objList, queryResult.getMetadata(),
				new ArrayList<Link>());
		return result;
	}

	public Warning create(Warning objToCreate) {
		WarningDTO p = modelMapper.map(objToCreate, WarningDTO.class);
		HttpEntity<WarningDTO> request = new HttpEntity<>(p);
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/warning");
		String uriBuilder = builder.build().encode().toUriString();
		ResponseEntity<WarningDTO> response = restTemplate.exchange(uriBuilder, HttpMethod.POST, request, WarningDTO.class);
		WarningDTO foo = response.getBody();
		objToCreate = modelMapper.map(foo, Warning.class);
		return objToCreate;
	}	
	
	public Warning update(Warning objToUpdate) {
		WarningDTO p = modelMapper.map(objToUpdate, WarningDTO.class);
		HttpEntity<WarningDTO> request = new HttpEntity<>(p);
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/warning/" + objToUpdate.getId());
		String uriBuilder = builder.build().encode().toUriString();
		ResponseEntity<WarningDTO> response = restTemplate.exchange(uriBuilder, HttpMethod.PUT, request, WarningDTO.class);
		WarningDTO foo = response.getBody();
		objToUpdate = modelMapper.map(foo, Warning.class);
		return objToUpdate;
	}
}

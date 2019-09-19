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
import org.typhon.client.model.TextWarning;
import org.typhon.client.model.dto.TextWarningDTO;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TextWarningService {
	private static final Logger logger = LoggerFactory.getLogger(TextWarningService.class);
	private String baseUrl;
	ModelMapper modelMapper;
	private RestTemplate restTemplate;

	public TextWarningService(String baseUrl) {
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

	public TextWarning findById(String
	 id) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/textwarning/" + id);
		String uriBuilder = builder.build().encode().toUriString();
		TextWarningDTO textwarningDTO = restTemplate
				.exchange(uriBuilder, HttpMethod.GET, null, new ParameterizedTypeReference<TextWarningDTO>() {
				}).getBody();
		TextWarning textwarning = modelMapper.map(textwarningDTO, TextWarning.class);
		return textwarning;
	}
	
	public void delete(TextWarning objToDelete) {
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/textwarning/" + objToDelete.getId());
			String uriBuilder = builder.build().encode().toUriString();
			restTemplate.delete(uriBuilder);
		}
		catch(HttpClientErrorException e) {
			logger.error(e.getMessage());
		}
	}

	public PagedResources<TextWarning> findAll(int page, int size, String order) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/textwarning").queryParam("page", page)
				.queryParam("size", size).queryParam("order", order);
		String uriBuilder = builder.build().encode().toUriString();
		PagedResources<TextWarningDTO> queryResult = restTemplate.exchange(uriBuilder, HttpMethod.GET, null,
				new ParameterizedTypeReference<PagedResources<TextWarningDTO>>() {
				}).getBody();
		List<TextWarning> objList = new ArrayList<TextWarning>();
		queryResult.forEach(z -> objList.add(modelMapper.map(z, TextWarning.class)));
		PagedResources<TextWarning> result = new PagedResources<TextWarning>(objList, queryResult.getMetadata(),
				new ArrayList<Link>());
		return result;
	}

	public TextWarning create(TextWarning objToCreate) {
		TextWarningDTO p = modelMapper.map(objToCreate, TextWarningDTO.class);
		HttpEntity<TextWarningDTO> request = new HttpEntity<>(p);
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/textwarning");
		String uriBuilder = builder.build().encode().toUriString();
		ResponseEntity<TextWarningDTO> response = restTemplate.exchange(uriBuilder, HttpMethod.POST, request, TextWarningDTO.class);
		TextWarningDTO foo = response.getBody();
		objToCreate = modelMapper.map(foo, TextWarning.class);
		return objToCreate;
	}	
	
	public TextWarning update(TextWarning objToUpdate) {
		TextWarningDTO p = modelMapper.map(objToUpdate, TextWarningDTO.class);
		HttpEntity<TextWarningDTO> request = new HttpEntity<>(p);
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/textwarning/" + objToUpdate.getId());
		String uriBuilder = builder.build().encode().toUriString();
		ResponseEntity<TextWarningDTO> response = restTemplate.exchange(uriBuilder, HttpMethod.PUT, request, TextWarningDTO.class);
		TextWarningDTO foo = response.getBody();
		objToUpdate = modelMapper.map(foo, TextWarning.class);
		return objToUpdate;
	}
}

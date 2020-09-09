package com.memcache.controller;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import com.memcache.service.MemCacheService;

@SpringJUnitConfig(classes = {MemCacheControllerTest.Config.class})
@WebMvcTest
public class MemCacheControllerTest {
	private static final Logger log = LoggerFactory.getLogger(MemCacheControllerTest.class);
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private MemCacheController controller;

	
	@Configuration
	static class Config{
			
		@Bean 
		public MemCacheController getController() {
			return new MemCacheController(getService());
		}
		
		private MemCacheService getService() {
			MemCacheService mockService =  mock(MemCacheService.class);
			when(mockService.save(any(String.class))).thenReturn("abcd");
			when(mockService.get(any(String.class))).thenReturn("{\"name\":\"ABC\"}");
			return mockService;
		}
	}
	
	@Test
	void testInit() {
		assertNotNull(controller);
	}
	
	@Test
	void testSave() throws Exception{
		String jsonRequest = "{\r\n" + 
				"\"name\": \"ABHI\"\r\n" +  
				"}";
		mvc.perform(post("/save").contentType(MediaType.APPLICATION_JSON_VALUE)
									   .content(jsonRequest)).andExpect(status().isCreated())
				.andReturn();
	}
	
	@Test
	void testSave_BadRequest() throws Exception{
		String jsonRequest = "{}";
		mvc.perform(post("/save").contentType(MediaType.APPLICATION_JSON_VALUE)
									   .content(jsonRequest)).andExpect(status().isBadRequest())
				.andReturn();
	}
}

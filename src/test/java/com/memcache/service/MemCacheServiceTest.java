package com.memcache.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemCacheServiceTest {
	private static final Logger log = LoggerFactory.getLogger(MemCacheServiceTest.class);
	private static MemCacheService service;
	private static ScheduledExecutorService executor;
	

	@BeforeAll
	static void setupData() {
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new MemCacheService.CacheCleaner(), 1, 1, TimeUnit.SECONDS);
		service = new MemCacheService(executor,10000L);
	}
	
	@Test
	void testInit() {
		assertNotNull(service);
	}



	@Test
	public void testSave() {		
		String st = "{\"name\" : \"John Myke\"}";
		String id = service.save(st);
		assertNotNull(service.get(id));
		service.cleanUp();
	}
	
	
	@Test
	public void testSaveQuote_Time1Sec() throws Exception{
		String st = "{\"name\" : \"John Myke\"}";
		String id = service.save(st);
		assertNotNull(id);
		assertEquals(st,service.get(id));
		try {
			Thread.sleep(12000);
		}catch(Exception e) {
			log.error(e.getMessage());
		}
		assertNull(service.get(id));
		executor.shutdown();
		service.cleanUp();
	}
	
	
}

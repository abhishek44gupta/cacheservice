package com.memcache.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.memcache.dto.General;

public class DtoUtilsTest {
	
	
	@Test
	public void createObjectTest() {
		String abc = "abc";
		General gen = DtoUtil.createObject(abc);
		assertNotNull(gen);
		assertEquals("abc", gen.getJson());
	}

}

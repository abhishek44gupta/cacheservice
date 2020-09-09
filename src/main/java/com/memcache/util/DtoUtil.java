package com.memcache.util;

import java.util.UUID;

import com.memcache.dto.General;

public class DtoUtil {
	
	public static General createObject(String json) {
		General general = new General();
		general.setId(UUID.randomUUID().toString());
		general.setJson(json);
		return general;
	}

}

package com.memcache.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.memcache.exception.BadRequestException;
import com.memcache.exception.NotFoundException;
import com.memcache.service.MemCacheService;

@RestController
public class MemCacheController {
	private static Logger logger = LoggerFactory.getLogger(MemCacheController.class);

	private final MemCacheService cacheService;

	@Autowired
	public MemCacheController(MemCacheService cacheService) {
		this.cacheService = cacheService;
	}

	@PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.CREATED)
	public String save(@RequestBody String json) {
		logger.info("saving json-> {}", json);
		if (StringUtils.isEmpty(json))
			throw new BadRequestException("Bad request:" + json);
		return cacheService.save(json);
	}

	@GetMapping(path = "/get/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.OK)
	public String get(@PathVariable String key) {
		logger.info("getting json for key {}", key);
		String json = cacheService.get(key);
		if (json == null) {
			throw new NotFoundException("key : " + key + "  not found");
		}
		return json;
	}
}

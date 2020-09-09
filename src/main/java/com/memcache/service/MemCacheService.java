package com.memcache.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.memcache.dto.General;
import com.memcache.util.DtoUtil;

@Service
public class MemCacheService {
	private static Logger logger = LoggerFactory.getLogger(MemCacheService.class);
	private static final Map<String,General> cache = new ConcurrentHashMap<>();
	private static final DelayQueue<CacheEvictEvent> queue = new DelayQueue<>();
	private final ScheduledExecutorService executor; 
	private final long evictionTime;

	@Autowired
	public MemCacheService(@Value("${application.object.evictionTime:1000}") long evictionTime) {
		executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(new CacheCleaner(), 1, 1, TimeUnit.SECONDS);
		this.evictionTime = evictionTime;
		logger.info("eviction time:"+evictionTime);
	}
	
	
	public  void cleanUp() {
		cache.clear();
		queue.clear();
	}

	public MemCacheService(ScheduledExecutorService executor, long evictionTime) {
		this.executor = executor;
		this.evictionTime = evictionTime;
	}
	
	public String save(@NonNull String json) {
		General general = DtoUtil.createObject(json);
		cache.put(general.getId(),general);
	    queue.add(new CacheEvictEvent(general.getId(),evictionTime));
	    return general.getId();
	}
	
	public String get(@NonNull String key){
		General general = cache.get(key);
		return general != null ? general.getJson() : null ;
	}
	
	public static class CacheCleaner implements Runnable 
	{ 
	    @Override
	    public void run() 
	    {        
	        logger.debug("cache cleaner queue {}",queue);
	        int size = queue.size();
	        List<CacheEvictEvent> events = new ArrayList<CacheEvictEvent>();
	        queue.drainTo(events);
	        logger.debug("{} elements are drained from queue of size {}",events.size(),size);
	        events.stream().forEach( event -> {
	        	logger.info("removing key :"+event);
	        	cache.remove(event.getKey());
	        });
	    }
	}
}

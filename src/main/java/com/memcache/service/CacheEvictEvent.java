package com.memcache.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class CacheEvictEvent implements Delayed {
	private final String key;
	private final LocalDateTime loadDatetime = LocalDateTime.now();
	private final long lifeInMillis;
	
	public CacheEvictEvent(String key, long lifeInMillis ) {
		this.key = key;
		this.lifeInMillis = lifeInMillis;
	}
	
	public String getKey() {
		return key;
	}

	@Override
	public int compareTo(Delayed that) {
		long result = this.getDelay(TimeUnit.NANOSECONDS) - that.getDelay(TimeUnit.NANOSECONDS);
		if (result < 0) {
			return -1;
		} else if (result > 0) {
			return 1;
		}
		return 0;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		 LocalDateTime now = LocalDateTime.now();		
		 long diff = now.until(loadDatetime.plus(lifeInMillis,ChronoUnit.MILLIS), ChronoUnit.MILLIS);
		 return unit.convert(diff, TimeUnit.MILLISECONDS);
	}

	@Override
	public String toString() {
		return "CacheEvictEvent [key=" + key + ", added at=" + loadDatetime + "]";
	}
}

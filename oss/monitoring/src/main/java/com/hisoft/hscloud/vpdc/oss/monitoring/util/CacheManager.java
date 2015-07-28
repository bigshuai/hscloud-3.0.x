package com.hisoft.hscloud.vpdc.oss.monitoring.util;

import java.util.Date;
import java.util.HashMap;

/**
 * 
 * @description
 * @version 1.0
 * @author YuezhouLi
 * @update 2012-5-24 下午5:39:01
 */
public class CacheManager {

	private static HashMap cacheMap = new HashMap();

	/**
	 * This class is singleton so private constructor is used.
	 */
	public CacheManager() {
		super();
	}

	/**
	 * 
	 * 此方法描述的是： get the cache through the key=vmId or hostName
	 * 
	 * @author: hongqin.li
	 * @version: 2012-5-23 上午9:52:26
	 */
	private synchronized static Cache getCache(String key) {
		return (Cache) cacheMap.get(key);
	}

	/**
	 * 
	 * 此方法描述的是：find if there is cache of vm or host
	 * 
	 * @author: hongqin.li
	 * @version: 2012-5-23 上午9:53:27
	 */
	public synchronized static boolean hasCache(String key) {
		return cacheMap.containsKey(key);
	}

	/**
	 * 
	 * 此方法描述的是：remove all the cahce date
	 * 
	 * @author: hongqin.li
	 * @version: 2012-5-23 上午9:53:57
	 */
	public synchronized static void removeAll() {
		cacheMap.clear();
	}

	/**
	 * 
	 * 此方法描述的是： remove one cacheItem which key = **
	 * 
	 * @author: hongqin.li
	 * @version: 2012-5-23 上午9:54:22
	 */
	public synchronized static void remove(String key) {
		System.out.println("remove cache . The key is :"+key);
		Object a = cacheMap.remove(key);
		System.out.println("The removed cache"+a);
	}

	/**
	 * Adds new item to cache hashmap
	 * 
	 * @param key
	 * @return Cache
	 */
	private synchronized static void putCache(String key, Cache object) {
		cacheMap.put(key, object);
	}

	/**
	 * Reads a cache item's content
	 * 
	 * @param key
	 * @return
	 */
	public static Cache getContent(String key) {
		if (hasCache(key)) {
			Cache cache = getCache(key);
			System.out.println("touch cache when read");
			touchCache(cache);
			return cache;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param key
	 * @param content
	 * @param ttl
	 */
	public static boolean putContent(String key, Object content) {
		Cache cache = null;
		if (hasCache(key)) {
			System.out.println("The cache has exist ,so just update"+key);
			System.out.println("Currently,There are "+cacheMap.size()+" cache item");
			for(Object c : cacheMap.keySet()){
				System.out.println("key:"+c);
			}
			cache = getCache(key);
			cache.setValue(content);
			putCache(key, cache);
			return cacheExpired(cache);
		} else {
			System.out.println("The cache is new ,so create one"+key);
			System.out.println("Currently,There are "+cacheMap.size()+" cache item");
			for(Object c : cacheMap.keySet()){
				System.out.println("key:"+c);
			}
			cache = new Cache();
			cache.setKey(key);
			cache.setValue(content);
			cache.setTimeOut(HostResourceConstants.CACHE_TIMEOUT);
			cache.setTouchTime(new Date().getTime());
			putCache(key, cache);
			return false;
		}
	}

	public static boolean putContent(String key, Object content, long timeout) {
		Cache cache = null;
		if (hasCache(key)) {
			cache = getCache(key);
			cache.setValue(content);
			putCache(key, cache);
			return cacheExpired(cache);
		} else {
			cache = new Cache();
			cache.setKey(key);
			cache.setValue(content);
			cache.setTimeOut(timeout);
			putCache(key, cache);
			return false;
		}
	}

	/** @modelguid {172828D6-3AB2-46C4-96E2-E72B34264031} */
	private static boolean cacheExpired(Cache cache) {
		if (cache == null) {
			return false;
		}
		long milisNow = new Date().getTime();
		long lastTouch = cache.getTouchTime();
		long milisExpire = cache.getTimeOut();
		long expireTime = lastTouch + milisExpire;
		System.out.println("The timeout is:" + milisExpire
				+ " lastTouch time is :" + lastTouch + " now is:" + milisNow
				+ " expireTime is:" + expireTime);
		if (milisExpire <= 0) { // Cache never expires
			return false;
		} else if (milisNow > expireTime) {
			return true;
		} else {
			return false;
		}
	}

	private static void touchCache(Cache cache) {
		cache.setTouchTime(new Date().getTime());
	}
}

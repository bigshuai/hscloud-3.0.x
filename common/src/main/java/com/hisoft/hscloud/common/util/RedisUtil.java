/* 
 * 文 件 名:  RedisUtil.java 
 * 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
 * 描    述:  <描述> 
 * 修 改 人:  dinghb 
 * 修改时间:  2013-4-18 
 * 跟踪单号:  <跟踪单号> 
 * 修改单号:  <修改单号> 
 * 修改内容:  <修改内容> 
 */
package com.hisoft.hscloud.common.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.ShardedJedisPool;

/**
 * <Redis> <功能详细描述>
 * 
 * @author dinghb
 * @version [版本号, 2013-4-18]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class RedisUtil {
	private static String host;
	private static int port = 6379;
	private static String name = "hscloud";
	private static int maxActive = 20;
	private static int MaxIdle = 1000;
	private static int MaxWait = 5;
	private static int timeout = Protocol.DEFAULT_TIMEOUT;
	private static String password = null;
	private static int database = Protocol.DEFAULT_DATABASE;
	//private ShardedJedis shardedJedis;
	private static JedisPool jedisPool;
	public static ShardedJedisPool shardedJedisPool;
	public static Map<String, String> redisMap = new HashMap<String, String>();
	
	private static Logger logger = Logger.getLogger(RedisUtil.class); 
	
	/**
	 * 初始化 Redis
	 */ 
	public static void initPool()  {
	    redisMap = PropertiesUtils.getPropertiesMap("redis.properties", redisMap);
	    host = redisMap.get("redis.host");
	    maxActive = Integer.valueOf(redisMap.get("redis.maxActive"));
	    MaxIdle = Integer.valueOf(redisMap.get("redis.maxIdle"));
	    MaxWait = Integer.valueOf(redisMap.get("redis.maxWait"));
	    port = Integer.valueOf(redisMap.get("redis.port"));
	    timeout = Integer.valueOf(redisMap.get("redis.timeout"));
	    database = Integer.valueOf(redisMap.get("redis.database"));
	    
	    // 池基本配置
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(maxActive);
		config.setMaxIdle(MaxIdle);
		config.setMaxWait(MaxWait);
		config.setTestOnBorrow(false);
		jedisPool = new JedisPool(config, host, port,timeout,password,database);
//		if(jedisPool==null){
//			jedisPool = new JedisPool(config, host);
//			jedisPool = new JedisPool(config, host, port,timeout,password,database);
//		}
		// slave链接
		/*List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo(host, port, name));
		// 构造池
		if(shardedJedisPool==null){
			shardedJedisPool = new ShardedJedisPool(config, shards);
		}*/
	}
	public static Jedis getJedis() {
	    if (jedisPool == null)
	      initPool();
	    return jedisPool.getResource();
	}
	public static void returnJedis(Jedis jedis) {
	    if (jedis != null)
	    	jedisPool.returnResource(jedis);
	}
	
	public static String getValue(String key) {
		Jedis jedis = null;
	    try{
//	        Jedis jedis = jedisPool.getResource();
	        jedis = RedisUtil.getJedis();
	        return jedis.get(key);
	    } catch(Exception ex) {
	        logger.error("读Redis异常", ex);
	        return null;
	    }finally{
	    	RedisUtil.returnJedis(jedis);
	    }
	    
	}

	public static String setValue(String key,String value){
		Jedis jedis = null;
	    try{
	        jedis = RedisUtil.getJedis();
	        return jedis.set(key, value);
	    } catch(Exception ex) {
	        logger.error("写Redis异常", ex);
	        return null;
	    }finally{
	    	RedisUtil.returnJedis(jedis);
	    }
	}
	/**
	 * 关闭 Redis
	 */
	public void destory() {
		shardedJedisPool.destroy();
	}
	
	public ShardedJedisPool getJedisPool(){
		return shardedJedisPool;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMaxIdle() {
		return MaxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		MaxIdle = maxIdle;
	}

	public int getMaxWait() {
		return MaxWait;
	}

	public void setMaxWait(int maxWait) {
		MaxWait = maxWait;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

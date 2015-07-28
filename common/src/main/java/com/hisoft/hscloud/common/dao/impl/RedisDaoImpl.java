/* 
 * 文 件 名:  RedisDaoImpl.java 
 * 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
 * 描    述:  <描述> 
 * 修 改 人:  dinghb 
 * 修改时间:  2013-4-19 
 * 跟踪单号:  <跟踪单号> 
 * 修改单号:  <修改单号> 
 * 修改内容:  <修改内容> 
 */
package com.hisoft.hscloud.common.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisException;
import com.hisoft.hscloud.common.dao.RedisDao;
import com.hisoft.hscloud.common.util.RedisUtil;

/**
 * <一句话功能简述>
	<功能详细描述>
 * @author dinghb
 * @version [版本号, 2013-4-19]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Repository
public class RedisDaoImpl implements RedisDao {

	private ShardedJedis shardedJedis;
	private ShardedJedisPool shardedJedisPool;
	
	/**
	 * 
	* @param key
	* @param map
	 */
	@Override
	public void setMap(String key,Map<String,String> map) {
		try {
			shardedJedisPool = RedisUtil.shardedJedisPool;
			shardedJedis = shardedJedisPool.getResource();
			shardedJedis.hmset(key, map);
			shardedJedisPool.returnResource(shardedJedis);
		} catch (Exception e) {
			if (shardedJedis != null) {
				shardedJedisPool.returnBrokenResource(shardedJedis);
			}
			throw new JedisException(e);
		}
	}
	/** 
	* @param key
	* @return 
	*/
	@Override
	public Map<String, String> getMap(String key) {
		// TODO Auto-generated method stub 
		try {
			shardedJedisPool = RedisUtil.shardedJedisPool;
			shardedJedis = shardedJedisPool.getResource();  
			Map<String, String> objMap = shardedJedis.hgetAll(key);  
			return objMap;
		} catch (Exception e) {
			if (shardedJedis != null) {
				shardedJedisPool.returnBrokenResource(shardedJedis);
			}
			throw new JedisException(e);
		}
	}
	/** 
	* @param key
	* @param value 
	*/
	@Override
	public void setValue(String key, String value) {
		// TODO Auto-generated method stub 
		try {
			shardedJedisPool = RedisUtil.shardedJedisPool;
			shardedJedis = shardedJedisPool.getResource();
			shardedJedis.set(key, value);
			shardedJedisPool.returnResource(shardedJedis);
		} catch (Exception e) {
			if (shardedJedis != null) {
				shardedJedisPool.returnBrokenResource(shardedJedis);
			}
			throw new JedisException(e);
		}
	}
	/** 
	* @param key
	* @return 
	*/
	@Override
	public String getValue(String key) {
		// TODO Auto-generated method stub 
		try {
			shardedJedisPool = RedisUtil.shardedJedisPool;
			shardedJedis = shardedJedisPool.getResource();
			return shardedJedis.get(key);
		} catch (Exception e) {
			if (shardedJedis != null) {
				shardedJedisPool.returnBrokenResource(shardedJedis);
			}
			throw new JedisException(e);
		}
	}
}

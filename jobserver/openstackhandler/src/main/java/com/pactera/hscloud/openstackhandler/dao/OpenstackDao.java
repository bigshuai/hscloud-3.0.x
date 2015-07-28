package com.pactera.hscloud.openstackhandler.dao;

import java.util.List;

/**
 * 
 * @author Minggang
 *
 */
public interface OpenstackDao {
	
	/**
	 * 保存方法（插入，更新）
	 * 当object中字段为空时，会更新到数据库，
	 * @param object  保存对象
	 * @param keyName 执行sql健值
	 * @exception  IllegalArgumentException  OpenstackHandlerException
	 * @return LONG 主健
	 */
	public Long save(Object object,String keyName);
	
	/**
	 * 保存方法（插入，更新）
	 * 当object中字段为空时，会更新到数据库，
	 * @param object 保存对象
	 * @param keyName 执行sql健值
	 * @param sql
	 * @exception  IllegalArgumentException  OpenstackHandlerException
	 * @return LONG 主健
	 */
	public Long save(Object object, String keyName,String sql);

	/**
	 * 当object中字段为空时，会更新到数据库，
	 * 保存日志（插入，更新）
	 * @param object   保存对象
	 * @param keyName  执行sql健值
	 * @exception  IllegalArgumentException  OpenstackHandlerException
	 * @return
	 */
	public Long saveSyncEvent(Object object,String keyName);
	
	/**
	 * 获得唯一值。当值为null或大于一时报错。
	 * @param keyName 执行sql健值
	 * @param sql     查询条件
	 * @exception  IllegalArgumentException  OpenstackHandlerException
	 * @return
	 */
	public Object getUnique(String keyName,String sql);
	
	/**
	 * 查询    结果为空时返回null,当大于一条记录时，返回第一条记录。
	 * @param keyName 执行sql健值
	 * @param sql 查询条件
	 * @exception  IllegalArgumentException  OpenstackHandlerException
	 * @return
	 */
	public Object get(String keyName,String sql);
	
	/**
	 * 查询   返回List
	 * @param keyName 执行sql健值
	 * @param sql 查询条件
	 * @exception  IllegalArgumentException  OpenstackHandlerException
	 * @return
	 */
	public List<Object> gets(String keyName, String sql);

}

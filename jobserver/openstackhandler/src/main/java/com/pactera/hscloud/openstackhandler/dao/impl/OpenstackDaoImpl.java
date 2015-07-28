package com.pactera.hscloud.openstackhandler.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.pactera.hscloud.common4j.util.DBUtil;
import com.pactera.hscloud.openstackhandler.dao.OpenstackDao;
import com.pactera.hscloud.openstackhandler.exception.OpenstackHandlerException;

/**
 * 数据库查询，保存 OpenstackDao
 * 
 * @author Minggang
 * 
 */
public class OpenstackDaoImpl implements OpenstackDao {

	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public Long save(Object object, String keyName) {
		try {
			return DBUtil.save(object, keyName);
		} catch (IllegalArgumentException iae) {
			logger.error("date error" + iae);
			logger.error("StackTrace"+iae.getStackTrace());
			throw new OpenstackHandlerException(iae.getMessage());
		} catch (Exception e) {
			logger.error("date error" + e);
			logger.error("StackTrace"+e.getStackTrace());
			throw new OpenstackHandlerException(e.getMessage());
		}
	}

	@Override
	public Long save(Object object, String keyName, String sql) {
		try {
			return DBUtil.save(object, keyName, sql);
		} catch (IllegalArgumentException iae) {
			logger.error("date error" + iae);
			logger.error("StackTrace"+iae.getStackTrace());
			throw new OpenstackHandlerException(iae.getMessage());
		} catch (Exception e) {
			logger.error("date error" + e);
			logger.error("StackTrace"+e.getStackTrace());
			throw new OpenstackHandlerException(e.getMessage());
		}
	}

	@Override
	public Long saveSyncEvent(Object syncEvent, String keyName) {

		try {
			return DBUtil.save(syncEvent, keyName);
		} catch (IllegalArgumentException iae) {
			logger.error("date error" + iae);
			logger.error("StackTrace"+iae.getStackTrace());
			throw new OpenstackHandlerException(iae.getMessage());
		} catch (Exception e) {
			logger.error("date error" + e);
			logger.error("StackTrace"+e.getStackTrace());
			throw new OpenstackHandlerException(e.getMessage());
		}

	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getUnique(String keyName, String sql) {
		try {
			List<Object> os = DBUtil.getResult(keyName, sql);
			if (null == os || os.isEmpty() || os.size() > 1) {
				logger.error("date error:" + "No result or not Unique");
				throw new OpenstackHandlerException();
			}
			return os.get(0);
		} catch (IllegalArgumentException iae) {
			logger.error("date error" + iae);
			logger.error("StackTrace"+iae.getStackTrace());
			throw new OpenstackHandlerException(iae.getMessage());
		} catch (Exception e) {
			logger.error("date error" + e);
			logger.error("StackTrace"+e.getStackTrace());
			throw new OpenstackHandlerException(e.getMessage());
		}

	}

	@Override
	@SuppressWarnings("unchecked")
	public Object get(String keyName, String sql) {
		try {
			List<Object> os = DBUtil.getResult(keyName, sql);
			if (null == os || os.isEmpty()) {
				logger.error("date :" + "No result");
				return null;
			} else {
				return os.get(0);
			}
		} catch (IllegalArgumentException iae) {
			logger.error("date error" + iae);
			logger.error("StackTrace"+iae.getStackTrace());
			throw new OpenstackHandlerException(iae.getMessage());
		} catch (Exception e) {
			logger.error("date error" + e);
			logger.error("StackTrace"+e.getStackTrace());
			throw new OpenstackHandlerException(e.getMessage());
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Object> gets(String keyName, String sql) {
		try {
			List<Object> os = DBUtil.getResult(keyName, sql);
	        return os;
		} catch (IllegalArgumentException iae) {
			logger.error("date error" + iae);
			logger.error("StackTrace"+iae.getStackTrace());
			throw new OpenstackHandlerException(iae.getMessage());
		} catch (Exception e) {
			logger.error("date error" + e);
			logger.error("StackTrace"+e.getStackTrace());
			throw new OpenstackHandlerException(e.getMessage());
		}
	}

}

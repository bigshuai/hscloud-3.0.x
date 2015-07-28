package com.pactera.hscloud.openstackhandler.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;

import com.pactera.hscloud.common4j.util.CommonThreadLocal;
import com.pactera.hscloud.openstackhandler.bo.SyncEvent;
import com.pactera.hscloud.openstackhandler.dao.OpenstackDao;
import java.lang.reflect.Method;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pactera.hscloud.openstackhandler.dao.impl.OpenstackDaoImpl;
import com.pactera.hscloud.openstackhandler.exception.OpenstackHandlerException;

/**
 * 代理类，记录日志。
 * @author Minggang
 *
 */
public class HandlerProxy implements InvocationHandler {

	private static Log logger = LogFactory.getLog(HandlerProxy.class);
	
	OpenstackDao openstackDao = new OpenstackDaoImpl();

	Object target;

	public HandlerProxy(Object obj) {
		super();
		this.target = obj;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

		try {
			this.doBefore();
			method.invoke(target, args);
			this.doAfter();
		}catch (InvocationTargetException ite) {
			logger.error("invocation error" + ite);
			throw new OpenstackHandlerException(ite.getMessage());
		}catch (IllegalArgumentException iae) {
			logger.error("date error" + iae);
			throw new OpenstackHandlerException(iae.getMessage());
		}catch (OpenstackHandlerException oshe) {
			logger.error("" + oshe.getMessage(), oshe);
			throw oshe;
		}
		return null;

	}

	// 操作前保存日志
	private void doBefore() {
		// 操作前保存日志
		SyncEvent syncEvent = (SyncEvent) CommonThreadLocal.getSyncEvent();
		syncEvent.setDeal_time(new Date());
		syncEvent.setUpdate_time(new Date());
		Long id = openstackDao.saveSyncEvent(syncEvent, "sync_event.new");
		syncEvent.setId(id);
	}

	// 操作后保存日志
	private void doAfter() {
		SyncEvent syncEvent = (SyncEvent) CommonThreadLocal.getSyncEvent();
		// 更新日志信息 当方法为error，则消息解析错误。
		if(!"error".equals(syncEvent.getMethod())){
			syncEvent.setFinish_time(new Date());
		}
		syncEvent.setUpdate_time(new Date());
		openstackDao.saveSyncEvent(syncEvent, "sync_event.update");
	}

}

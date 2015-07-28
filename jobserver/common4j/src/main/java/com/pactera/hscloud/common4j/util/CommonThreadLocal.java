package com.pactera.hscloud.common4j.util;


public class CommonThreadLocal {

	private final static ThreadLocal<Object> syncEvent = new ThreadLocal<Object>();

	private final static ThreadLocal<Object[]> param = new ThreadLocal<Object[]>();
	
	private final static ThreadLocal<Object> message = new ThreadLocal<Object>();

	public static void setSyncEvent(Object o) {
		syncEvent.set(o);
	}

	public static Object getSyncEvent() {
		return syncEvent.get();
	}

	private static void removeSyncEvent() {
		syncEvent.remove();
	}

	public static void setParam(Object[] o) {
		param.set(o);
	}

	public static Object[] getParam() {
		return param.get();
	}
	
	private static void removeParam() {
		param.remove();
	}
	
	public static void setMessage(Object o) {
		message.set(o);
	}
	
	public static Object getMessage() {
		return message.get();
	}
	
	private static void removeMessage(){
		message.remove();
	}

	public static void remove(){
		removeParam();
		removeSyncEvent();
		removeMessage();
	}

}

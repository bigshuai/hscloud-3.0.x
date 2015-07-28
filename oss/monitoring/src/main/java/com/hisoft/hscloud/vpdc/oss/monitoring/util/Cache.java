package com.hisoft.hscloud.vpdc.oss.monitoring.util;

import java.util.Date;

public class Cache {
	private String key;
	private Object value;
	private long timeOut;
	private boolean expired;
	private long touchTime;

	public Cache() {
		super();
	}

	public Cache(String key, String value, long timeOut, boolean expired) {
		this.key = key;
		this.value = value;
		this.timeOut = timeOut;
		this.expired = expired;
	}

	public String getKey() {
		return key;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public Object getValue() {
		return value;
	}

	public void setKey(String string) {
		key = string;
	}

	public void setTimeOut(long l) {
		timeOut = l;
	}

	public void setValue(Object object) {
		value = object;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean b) {
		expired = b;
	}

	public long getTouchTime() {
		return touchTime;
	}

	public void setTouchTime(long touchTime) {
		this.touchTime = touchTime;
	}

}

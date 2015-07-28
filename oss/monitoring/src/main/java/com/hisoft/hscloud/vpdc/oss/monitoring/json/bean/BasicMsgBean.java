/**
 * @title BasicMsgBean.java
 * @package com.hisoft.hscloud.vpdc.oss.monitoring.json.bean
 * @description 
 * @author YuezhouLi
 * @update 2012-5-24 上午10:16:46
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.oss.monitoring.json.bean;

import java.util.Date;

/**
 * @description
 * @version 1.0
 * @author YuezhouLi
 * @update 2012-5-24 上午10:16:46
 */
public class BasicMsgBean {
	private String type;
	private String format;
	private Date timestamp;
	private String task_ident;
	private String location;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getTask_ident() {
		return task_ident;
	}

	public void setTask_ident(String task_ident) {
		this.task_ident = task_ident;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "BasicMsgBean [type=" + type + ", format=" + format
				+ ", timestamp=" + timestamp + "]";
	}

}

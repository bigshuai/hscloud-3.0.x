/**
 * 
 */
package com.hisoft.hscloud.message.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
* <消息> 
* <功能详细描述> 
* 
* @author  lihonglei 
* @version  [版本号, 2013-3-8] 
* @see  [相关类/方法] 
* @since  [产品/模块版本]
 */
@Entity
@Table(name = "hc_message")
public class Message {
	// 主健
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	//1-未阅读,2-已阅读
	@Column(name = "status", length = 4, nullable = false)
	private int status = 1;
	
	@Column(name = "create_time", nullable = false)
	private Date createTime = new Date();
	
	@Column(name = "user_id", length = 20, nullable = false)
	private Long userId;
	
	//1-续订提醒, 2-账务提醒, 3-系统通知, 4-欠费提醒   5-加入邀请  6-发票审核
	@Column(name = "messge_type", length = 2, nullable = false)
	private int messageType;
	
	@Column(name = "message", length = 255, nullable = false)
	private String message;
	
	@Column(name = "remark", length = 64, nullable = true)
	private String remark;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}

/**
 * @title VmSnapShot.java
 * @package com.hisoft.hscloud.vpdc.ops.entity
 * @description 用一句话描述该文件做什么
 * @author hongqin.li
 * @update 2012-6-7 下午5:33:04
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.ops.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author haibin.ding
 * @update 2013-5-16 下午13:33:04
 */
@Entity
@Table(name = "hc_vm_expiremail_log")
public class VmExpireEmailLog {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "createTime")
	private Date createTime;

	@Column(name = "referenceId")
	private Long referenceId;

	@Column(name = "mailId")
	private Long mailId;

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public Long getReferenceId() {
		return referenceId;
	}


	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}


	public Long getMailId() {
		return mailId;
	}


	public void setMailId(Long mailId) {
		this.mailId = mailId;
	}


	@Override
	public String toString() {
		return "VmSnapShot [id=" + id + ", referenceId=" + referenceId
				+ ",createTime=" + createTime+ ",mailId=" + mailId + "]";
	}
	
}

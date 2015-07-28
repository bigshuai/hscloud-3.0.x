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
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author hongqin.li
 * @update 2012-6-7 下午5:33:04
 */
@Entity
@Table(name = "hc_vm_snapshot")
public class VmSnapShot {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@OneToMany(mappedBy = "vmSnapShot", targetEntity = VmSnapShotTask.class, cascade = {
		CascadeType.ALL }, fetch = FetchType.EAGER)
	@OrderBy("id asc")
	private Set<VmSnapShotTask> snapShotTasks;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH }, optional = true)
	@JoinColumn(name = "instanceId")
	private VpdcInstance instance;

	@Column(name = "createTime")
	private Date createTime;

	//openstack创建snapshost后生成的实际快照名称（根据该名称可以进行还原）
	@Column(name = "snapShot_id")
	private String snapShot_id;

	// 数据库内快照名称；add by dinghaibin
	@Column(name = "snapShot_name")
	private String snapShot_name;

	// 备份备注；add by dinghaibin
	@Column(name = "snapShot_comments")
	private String snapShot_comments;

	// 备份类型：0为手动，1为自动；add by dinghaibin
	@Column(name = "snapShot_type")
	private int snapShot_type;
	
	//是否备份完成状态（0:未完成；1：已完成）
	@Column(name = "status")
	private int status;

	/**
	 * @return id : return the property id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : set the property id.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return createTime : return the property createTime.
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            : set the property createTime.
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return snapShot_id : return the property snapShot_id.
	 */
	public String getSnapShot_id() {
		return snapShot_id;
	}

	/**
	 * @param snapShot_id
	 *            : set the property snapShot_id.
	 */
	public void setSnapShot_id(String snapShot_id) {
		this.snapShot_id = snapShot_id;
	}

	/**
	 * @return snapShot_name : return the property snapShot_name.
	 */
	public String getSnapShot_name() {
		return snapShot_name;
	}

	/**
	 * @param snapShot_name
	 *            : set the property snapShot_name.
	 */
	public void setSnapShot_name(String snapShot_name) {
		this.snapShot_name = snapShot_name;
	}

	/**
	 * 
	 * @return snapShot_comments : return the property snapShot_comments.
	 */
	public String getSnapShot_comments() {
		return snapShot_comments;
	}

	/**
	 * 
	 * @param snapShot_comments
	 *            : set the property snapShot_comments.
	 */
	public void setSnapShot_comments(String snapShot_comments) {
		this.snapShot_comments = snapShot_comments;
	}

	/**
	 * 
	 * @return snapShot_type : return the property snapShot_type.
	 */
	public int getSnapShot_type() {
		return snapShot_type;
	}

	/**
	 * @param snapShot_type
	 *            : set the property snapShot_type.
	 */
	public void setSnapShot_type(int snapShot_type) {
		this.snapShot_type = snapShot_type;
	}

	public VpdcInstance getInstance() {
		return instance;
	}

	public void setInstance(VpdcInstance instance) {
		this.instance = instance;
	}
	
	
	public Set<VmSnapShotTask> getSnapShotTasks() {
		return snapShotTasks;
	}

	public void setSnapShotTasks(Set<VmSnapShotTask> snapShotTasks) {
		this.snapShotTasks = snapShotTasks;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "VmSnapShot [id=" + id + ", instance=" + instance
				+ ",snapShot_name=" + snapShot_name + ",snapShot_comments="
				+ snapShot_comments + ",snapShot_type=" + snapShot_type
				+ ", createTime=" + createTime + ", snapShot_id=" + snapShot_id
				+ "]";
	}
	
}

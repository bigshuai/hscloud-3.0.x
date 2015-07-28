/* 
* 文 件 名:  VmSnapShotTask.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  dinghb 
* 修改时间:  2012-12-11 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.entity; 

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  dinghb 
 * @version  [版本号, 2012-12-11] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Entity
@Table(name="hc_vm_snapshot_task")
public class VmSnapShotTask {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, optional = true)
	@JoinColumn(name = "vmSnapShotId")
	private VmSnapShot vmSnapShot;
	
	@Column(name = "backupName_stack")
	private String backupName_stack;//openstack备份根据该字段生成新的唯一快照名称
	
	@Column(name = "status")
	private int status;//是否备份完成(0:未完成；1:已完成)

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public VmSnapShot getVmSnapShot() {
		return vmSnapShot;
	}

	public void setVmSnapShot(VmSnapShot vmSnapShot) {
		this.vmSnapShot = vmSnapShot;
	}
	
	public String getBackupName_stack() {
		return backupName_stack;
	}

	public void setBackupName_stack(String backupName_stack) {
		this.backupName_stack = backupName_stack;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}

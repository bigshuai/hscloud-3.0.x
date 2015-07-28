/**
 * @title VmSnapShot.java
 * @package com.hisoft.hscloud.vpdc.ops.entity
 * @description 用一句话描述该文件做什么
 * @author hongqin.li
 * @update 2012-6-7 下午5:33:04
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.ops.entity;

import java.sql.Time;
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
 * @update 2012-9-10 上午10:33:04
 */
@Entity
@Table(name="hc_vm_snapshotplan")
public class VmSnapShotPlan {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;
	/**
	 * 0:不自动 1:按周 2:按月
	 */
	@Column(name = "plan_type")
	private int planType;
	/**
	 * 备份日期：星期/日期
	 */
	@Column(name = "plan_date")
	private int planDate;
	/**
	 * 备份具体时间（小时：分）
	 */
	@Column(name = "plan_time")
	private Time planTime;
	
	@Column(name = "vm_id")
	private String vmId;
	
	@Column(name = "modifyUser")
	private long modifyUser;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPlanType() {
		return planType;
	}

	public void setPlanType(int planType) {
		this.planType = planType;
	}

	public String getVmId() {
		return vmId;
	}
	public int getPlanDate() {
		return planDate;
	}

	public void setPlanDate(int planDate) {
		this.planDate = planDate;
	}

	public Time getPlanTime() {
		return planTime;
	}

	public void setPlanTime(Time planTime) {
		this.planTime = planTime;
	}

	public void setVmId(String vmId) {
		this.vmId = vmId;
	}

	public long getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(long modifyUser) {
		this.modifyUser = modifyUser;
	}

	@Override
	public String toString() {
		return "VmSnapShotPlan [id=" + id + ", planType=" + planType
				+ ", playDate=" + planDate + ", playTime=" + planTime + ", vmId=" + vmId
				+ "]";
	}
}

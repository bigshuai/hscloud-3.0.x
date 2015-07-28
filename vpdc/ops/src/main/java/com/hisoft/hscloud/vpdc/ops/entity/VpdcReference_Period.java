package com.hisoft.hscloud.vpdc.ops.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * @author dinghb
 *
 */
@Entity
@Table(name = "hc_vpdcReference_period")
public class VpdcReference_Period {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	//虚拟机启用时间
	@Column(name = "start_time")
	private Date startTime;
	
	//虚拟机停用时间
	@Column(name = "end_time")
	private Date endTime;
	
	@Column(name = "renferenceId")
	private long vpdcreferenceId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getVpdcreferenceId() {
		return vpdcreferenceId;
	}

	public void setVpdcreferenceId(long vpdcreferenceId) {
		this.vpdcreferenceId = vpdcreferenceId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}

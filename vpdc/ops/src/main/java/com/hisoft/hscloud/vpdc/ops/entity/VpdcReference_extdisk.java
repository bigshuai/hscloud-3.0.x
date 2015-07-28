/* 
* 文 件 名:  VpdcReference_Os.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  dinghb 
* 修改时间:  2012-12-13 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.entity; 

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hisoft.hscloud.common.entity.AbstractEntity;

/** 
 * <instance与volume关联> 
 * <功能详细描述> 
 * 
 * @author  dinghb 
 * @version  [版本号, 2013-02-19] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Entity
@Table(name = "hc_vpdcReference_extdisk")
public class VpdcReference_extdisk extends AbstractEntity{
	@Column(name = "ed_capacity")
	private int ed_capacity;
	
	@Column(name = "vmId")
	private String vmId;
	
	@Column(name = "initVmId")
	private String initVmId;
	
	@Column(name = "volumeId")
	private Integer volumeId;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH }, optional = true)
	@JoinColumn(name = "referenceId")
	private VpdcReference ed_reference;
	
	public int getEd_capacity() {
		return ed_capacity;
	}

	public void setEd_capacity(int ed_capacity) {
		this.ed_capacity = ed_capacity;
	}

	public String getVmId() {
		return vmId;
	}

	public void setVmId(String vmId) {
		this.vmId = vmId;
	}
	
	public String getInitVmId() {
		return initVmId;
	}

	public void setInitVmId(String initVmId) {
		this.initVmId = initVmId;
	}

	public Integer getVolumeId() {
		return volumeId;
	}

	public void setVolumeId(Integer volumeId) {
		this.volumeId = volumeId;
	}

	public VpdcReference getEd_reference() {
		return ed_reference;
	}

	public void setEd_reference(VpdcReference ed_reference) {
		this.ed_reference = ed_reference;
	}

}

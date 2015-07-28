/* 
* 文 件 名:  VpdcFlavor.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  dinghb 
* 修改时间:  2013-4-23 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.entity; 

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.hisoft.hscloud.common.entity.AbstractEntity;

/** 
 * <与openstack的flavor对应> 
 * <功能详细描述> 
 * 
 * @author  dinghb 
 * @version  [版本号, 2013-4-23] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Entity
@Table(name = "hc_vm_flavor")
public class VpdcFlavor extends AbstractEntity{
	@Column(name = "memory_mb")
	private Integer memory_mb;//内存（M）
	@Column(name = "vcpus")
	private Integer vcpus;//CPU核数
	@Column(name = "swap")
	private Integer swap;
	@Column(name = "vcpu_weight")
	private Integer vcpu_weight;
	@Column(name = "rxtx_factor")
	private Float rxtx_factor;
	@Column(name = "root_gb")
	private Integer root_gb;//磁盘大小
	@Column(name = "ephemeral_gb")
	private Integer ephemeral_gb;

	public Integer getMemory_mb() {
		return memory_mb;
	}
	public void setMemory_mb(Integer memory_mb) {
		this.memory_mb = memory_mb;
	}
	public Integer getVcpus() {
		return vcpus;
	}
	public void setVcpus(Integer vcpus) {
		this.vcpus = vcpus;
	}
	public Integer getSwap() {
		return swap;
	}
	public void setSwap(Integer swap) {
		this.swap = swap;
	}
	public Integer getVcpu_weight() {
		return vcpu_weight;
	}
	public void setVcpu_weight(Integer vcpu_weight) {
		this.vcpu_weight = vcpu_weight;
	}
	public Float getRxtx_factor() {
		return rxtx_factor;
	}
	public void setRxtx_factor(Float rxtx_factor) {
		this.rxtx_factor = rxtx_factor;
	}
	public Integer getRoot_gb() {
		return root_gb;
	}
	public void setRoot_gb(Integer root_gb) {
		this.root_gb = root_gb;
	}
	public Integer getEphemeral_gb() {
		return ephemeral_gb;
	}
	public void setEphemeral_gb(Integer ephemeral_gb) {
		this.ephemeral_gb = ephemeral_gb;
	}
	
}

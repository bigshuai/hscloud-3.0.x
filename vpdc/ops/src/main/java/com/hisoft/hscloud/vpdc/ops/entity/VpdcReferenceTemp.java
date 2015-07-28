package com.hisoft.hscloud.vpdc.ops.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 用于解决管理员根据权限查询VM数据量大的问题，将resource中的VMID存入该表，使用关联查询（去除in查询）
 * @author dinghb
 *
 */
@Entity
@Table(name = "hc_vpdc_reference_temp")
public class VpdcReferenceTemp {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	// 虚拟机referenceid
	@Column(name = "referenceId")
	private Long referenceId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

}

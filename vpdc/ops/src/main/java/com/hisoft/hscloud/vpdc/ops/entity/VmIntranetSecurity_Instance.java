package com.hisoft.hscloud.vpdc.ops.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @className: VmIntranetSecurity
 * @package: com.hisoft.hscloud.vpdc.ops.entity
 * @description: 内网安全
 * @author: liyunhui
 * @createTime: Aug 5, 2013 4:13:32 PM
 * @company: Pactera Technology International Ltd
 */
@Entity
@Table(name = "hc_vm_intranetsecurity_instance")
public class VmIntranetSecurity_Instance {
	
	// 数据库表的记录的序号
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	// 虚拟机的instance_id
	@Column(name = "instance_id")
	private Long instance_id;
	// 同一组的虚拟机所属的组的id,外键
	@Column(name = "intranetsecurity_id")
	private Long intranetsecurity_id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getInstance_id() {
		return instance_id;
	}

	public void setInstance_id(Long instance_id) {
		this.instance_id = instance_id;
	}

	public Long getIntranetsecurity_id() {
		return intranetsecurity_id;
	}

	public void setIntranetsecurity_id(Long intranetsecurity_id) {
		this.intranetsecurity_id = intranetsecurity_id;
	}

	@Override
	public String toString() {
		return "VmIntranetSecurity_Instance [id=" + id + ", instance_id="
				+ instance_id + ", intranetsecurity_id=" + intranetsecurity_id
				+ "]";
	}

}
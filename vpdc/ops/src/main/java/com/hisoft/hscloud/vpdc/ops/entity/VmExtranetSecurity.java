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
 * @className: VmExtranetSecurity
 * @package: com.hisoft.hscloud.vpdc.ops.entity
 * @description: 外网安全
 * @author: liyunhui
 * @createTime: Aug 5, 2013 3:49:39 PM
 * @company: Pactera Technology International Ltd
 */
@Entity
@Table(name = "hc_vm_extranetsecurity")
public class VmExtranetSecurity extends AbstractEntity {

	// 协议: 0-ALL 1-TCP 2-UDP
	@Column(name = "protocal")
	private Integer protocal;

	// 开始的端口: port_from
	@Column(name = "port_from")
	private Integer port_from;

	// 终止的端口: port_to
	@Column(name = "port_to")
	private Integer port_to;

	// 虚拟机的uuid
	@Column(name = "uuid")
	private String uuid;

	// instanceId外键
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH }, optional = true)
	@JoinColumn(name = "instanceId")
	private VpdcInstance instance;

	public Integer getProtocal() {
		return protocal;
	}

	public void setProtocal(Integer protocal) {
		this.protocal = protocal;
	}

	public Integer getPort_from() {
		return port_from;
	}

	public void setPort_from(Integer port_from) {
		this.port_from = port_from;
	}

	public Integer getPort_to() {
		return port_to;
	}

	public void setPort_to(Integer port_to) {
		this.port_to = port_to;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public VpdcInstance getInstance() {
		return instance;
	}

	public void setInstance(VpdcInstance instance) {
		this.instance = instance;
	}

	@Override
	public String toString() {
		return "VmExtranetSecurity [protocal=" + protocal + ", port_from="
				+ port_from + ", port_to=" + port_to + ", uuid=" + uuid
				+ ", instance=" + instance + "]";
	}

}
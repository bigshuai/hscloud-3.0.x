package com.hisoft.hscloud.vpdc.ops.vo;

/**
 * @className: VmExtranetSecurityVO
 * @package: com.hisoft.hscloud.vpdc.ops.entity
 * @description: 外网安全VO
 * @author: liyunhui
 * @createTime: Aug 5, 2013 3:49:39 PM
 * @company: Pactera Technology International Ltd
 */
public class VmExtranetSecurityVO {

	// 协议: 0-all(tcp&udp);1-tcp;2-udp;3-icmp;
	private Integer protocal;
	// 开始的端口: port_from
	private Integer port_from;
	// 终止的端口: port_to
	private Integer port_to;
	// 虚拟机的uuid
	private String uuid;

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

	@Override
	public String toString() {
		return "VmExtranetSecurityVO [protocal=" + protocal + ", port_from="
				+ port_from + ", port_to=" + port_to + ", uuid=" + uuid + "]";
	}

}
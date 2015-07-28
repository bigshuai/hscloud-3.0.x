package com.hisoft.hscloud.vpdc.ops.vo;

/**
 * @className: VmIntranetSecurityVO
 * @package: com.hisoft.hscloud.vpdc.ops.vo
 * @description: 外网安全VO
 * @author: liyunhui
 * @createTime: Aug 7, 2013 4:19:54 PM
 * @company: Pactera Technology International Ltd
 */
public class VmIntranetSecurityVO {

	// 虚拟机的主机名
	private String vmName;
	// 虚拟机的内网IP
	private String innerIp;
	// 虚拟机的外网Ip
	private String outerIp;
	// 虚拟机的instacneId
	private Long instanceId;
	// 虚拟机所属的同一组的intranetsecurity_id 组号
	private Long groupId;
	// 虚拟机的uuid
	private String uuid;

	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	public String getInnerIp() {
		return innerIp;
	}

	public void setInnerIp(String innerIp) {
		this.innerIp = innerIp;
	}

	public String getOuterIp() {
		return outerIp;
	}

	public void setOuterIp(String outerIp) {
		this.outerIp = outerIp;
	}

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "VmIntranetSecurityVO [vmName=" + vmName + ", innerIp="
				+ innerIp + ", outerIp=" + outerIp + ", instanceId="
				+ instanceId + ", groupId=" + groupId + ", uuid=" + uuid + "]";
	}

}
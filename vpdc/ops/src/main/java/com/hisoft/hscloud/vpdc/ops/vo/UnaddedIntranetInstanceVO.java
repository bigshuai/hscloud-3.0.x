package com.hisoft.hscloud.vpdc.ops.vo;

/**
 * @className: UnaddedIntranetSecurityInstanceVO
 * @package: com.hisoft.hscloud.vpdc.ops.vo
 * @description: 未添加内网安全的虚拟机
 * @author: liyunhui
 * @createTime: Aug 14, 2013 5:10:23 PM
 * @company: Pactera Technology International Ltd
 */
public class UnaddedIntranetInstanceVO {

	private Long instanceId;
	private long referenceId;
	private String name;
	private String ip;
	private int isEnable;// 0:正常；1：手动禁用；2：到期禁用
	private String status;
	private String id;// 虚拟机的uuid

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(long referenceId) {
		this.referenceId = referenceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
package com.pactera.hscloud.openstackhandler.vo;

import com.pactera.hscloud.openstackhandler.bo.O_VPDCReference;

@SuppressWarnings("serial")
public class VPDCReferenceVO extends O_VPDCReference {
	
	private String uuid;
	
	private String initVmId;
	
	private Long instanceId;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getInitVmId() {
		return initVmId;
	}

	public void setInitVmId(String initVmId) {
		this.initVmId = initVmId;
	}

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}
	
	
	

}

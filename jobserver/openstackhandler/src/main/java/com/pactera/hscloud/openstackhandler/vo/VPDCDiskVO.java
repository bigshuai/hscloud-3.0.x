package com.pactera.hscloud.openstackhandler.vo;

import com.pactera.hscloud.openstackhandler.bo.O_VPDC_Extdisk;

public class VPDCDiskVO extends O_VPDC_Extdisk {

	private String uuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}

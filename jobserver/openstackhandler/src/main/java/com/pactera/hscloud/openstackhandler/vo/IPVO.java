package com.pactera.hscloud.openstackhandler.vo;

import com.pactera.hscloud.openstackhandler.bo.O_IP;

public class IPVO extends O_IP{

	private String nodeName;
	
	private String uuid;

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	
	
}

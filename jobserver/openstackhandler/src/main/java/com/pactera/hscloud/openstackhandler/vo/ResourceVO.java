package com.pactera.hscloud.openstackhandler.vo;

import com.pactera.hscloud.openstackhandler.bo.ResourceResult;

public class ResourceVO extends ResourceResult{
	
	public String method;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

}

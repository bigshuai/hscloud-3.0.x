package com.pactera.hscloud.openstackhandler.vo;

import com.hisoft.hscloud.common.entity.LogOPSType;
import com.hisoft.hscloud.common.entity.LogOperatorType;

public class VMOpsVO {
	
	private String uuid;//路由uuid
	
	private String obj_name;//路由名称
	
	private short operator_type = LogOperatorType.PROCESS.getIndex();

	private short ops = LogOPSType.BINDLAN.getIndex();

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getObj_name() {
		return obj_name;
	}

	public void setObj_name(String obj_name) {
		this.obj_name = obj_name;
	}

	public short getOperator_type() {
		return operator_type;
	}

	public void setOperator_type(short operator_type) {
		this.operator_type = operator_type;
	}

	public short getOps() {
		return ops;
	}

	public void setOps(short ops) {
		this.ops = ops;
	}
	
	
}

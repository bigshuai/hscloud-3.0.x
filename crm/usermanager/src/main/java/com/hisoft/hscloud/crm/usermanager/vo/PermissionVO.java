package com.hisoft.hscloud.crm.usermanager.vo;


public class PermissionVO{

	// 权限id
	private long permissionId;
	// 资源id
	private long resourceId;
	//资源表中的primKey
	private String primKey;
	// 操作id
	private long actionId;
	//资源类型
	private String resourceType;


	public long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(long permissionId) {
		this.permissionId = permissionId;
	}

	public long getResourceId() {
		return resourceId;
	}

	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}

	public long getActionId() {
		return actionId;
	}

	public void setActionId(long actionId) {
		this.actionId = actionId;
	}

	public String getPrimKey() {
		return primKey;
	}

	public void setPrimKey(String primKey) {
		this.primKey = primKey;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	
	
	
	

}

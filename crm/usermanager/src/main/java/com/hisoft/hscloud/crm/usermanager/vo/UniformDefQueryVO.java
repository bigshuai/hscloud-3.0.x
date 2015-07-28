/**
 * 
 */
package com.hisoft.hscloud.crm.usermanager.vo;

/**
 * @author lihonglei
 *
 */
public class UniformDefQueryVO {
	private Long permissionId;
	private Long actionId;
	private Long resourceId;
	private Long checkId;
	
	public Long getPermissionId() {
		return permissionId;
	}
	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}
	public Long getActionId() {
		return actionId;
	}
	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}
	public Long getResourceId() {
		return resourceId;
	}
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
	public Long getCheckId() {
		return checkId;
	}
	public void setCheckId(Long checkId) {
		this.checkId = checkId;
	}
}

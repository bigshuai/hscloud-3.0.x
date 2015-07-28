/**
 * 
 */
package com.hisoft.hscloud.crm.usermanager.vo;

/**
 * @author lihonglei
 *
 */
public class CheckboxVO {
	private Long permissionId;
	private Long actionId;
	private Long resourceId;
	private boolean checked = false;
	
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
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public Long getResourceId() {
		return resourceId;
	}
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
}

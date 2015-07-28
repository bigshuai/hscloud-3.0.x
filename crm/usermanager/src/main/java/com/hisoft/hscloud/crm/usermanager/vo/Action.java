package com.hisoft.hscloud.crm.usermanager.vo;

public class Action {
	
    private Long actionId;
    private String acitonName;
    private Long permissionId;
    private Long level;
    
    private boolean checked = false;
    
	public Long getActionId() {
		return actionId;
	}
	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}
	public String getAcitonName() {
		return acitonName;
	}
	public void setAcitonName(String acitonName) {
		this.acitonName = acitonName;
	}
	public Long getPermissionId() {
		return permissionId;
	}
	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public Long getLevel() {
		return level;
	}
	public void setLevel(Long level) {
		this.level = level;
	}
	
	
    

}

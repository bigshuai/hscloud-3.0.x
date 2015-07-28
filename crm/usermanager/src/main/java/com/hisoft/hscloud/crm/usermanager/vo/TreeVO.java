/* 
* 文 件 名:  TreeVO.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2012-10-15 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.crm.usermanager.vo;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2012-10-15] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class TreeVO {
	
	private String id;//各资源表id ,如 vm usergroup menu 的id
    private String primKey;
    private String resourceName;
    private Long permissionId;
    private Long actionId;
    private String acitonName;
    private Long resourceId;
    private String resourceType;
    private Long level;
    private boolean checked;
    
    
    
    public String getPrimKey() {
        return primKey;
    }
    public void setPrimKey(String primKey) {
        this.primKey = primKey;
    }
    public String getResourceName() {
        return resourceName;
    }
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
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
    public Long getActionId() {
        return actionId;
    }
    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }
    public String getResourceType() {
        return resourceType;
    }
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
    public Long getResourceId() {
        return resourceId;
    }
    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getLevel() {
		return level;
	}
	public void setLevel(Long level) {
		this.level = level;
	}
	
    
    
    
    
}

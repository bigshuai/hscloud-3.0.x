package com.hisoft.hscloud.crm.usermanager.vo;

import java.util.ArrayList;
import java.util.List;

public class PerGroupVO {
	
    private Long groupId;
    
    private String groupName;
    //resourceType:primKey:actionId:resourceId
    private String permissionStr;
    
    List<TreeVO> p = new ArrayList<TreeVO>();
  
	public List<TreeVO> getP() {
		return p;
	}
	public void setP(List<TreeVO> p) {
		this.p = p;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getPermissionStr() {
		return permissionStr;
	}
	
	public void setPermissionStr(String permissionStr) {
		this.permissionStr = permissionStr;
		String[] perStr = this.permissionStr.split(",");
		for (String ps : perStr) {
			TreeVO tv = new TreeVO();
			String [] pStr = ps.split(":");
			tv.setResourceType(pStr[0]);
			tv.setPrimKey("null".equals(pStr[1])?"":pStr[1]);
			tv.setActionId("null".equals(pStr[2])?null:Long.valueOf(pStr[2]));
			tv.setResourceId("null".equals(pStr[3])?null:Long.valueOf(pStr[3]));
			tv.setId("null".equals(pStr[4])?"":pStr[4]);
			tv.setPermissionId("null".equals(pStr[5])?null:Long.valueOf(pStr[5]));
			p.add(tv);
		}
	}

}

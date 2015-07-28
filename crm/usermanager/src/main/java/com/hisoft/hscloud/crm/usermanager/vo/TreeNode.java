package com.hisoft.hscloud.crm.usermanager.vo; 

import java.util.ArrayList;
import java.util.List;

import com.hisoft.hscloud.crm.usermanager.entity.Permission;

public class TreeNode {
	
	 /*private String id; //priKey
	    private String text; //resourceName
	    private boolean leaf; //false
	    private String cls; //
	    private List<TreeNode> children = new ArrayList<TreeNode>(); //permission   actionId, acitonName, permissionId
	    private boolean checked; //role_permission
	    private Long resourceId;
	    private String resourceType;
	    private boolean expanded = true;
	    private List<ChildNodeVO> actionList = new ArrayList<ChildNodeVO>();;*/
	
	private String id;
	private String text;
	private boolean leaf;
	
	private String voId;
	private String name;
	private String type;
	private String icon;
	private Long resourceId = -1l;
	private boolean checked; 
	private boolean expanded = true;
	private List<Permission> permissionList = new ArrayList<Permission>();
	private List<TreeNode> children = new ArrayList<TreeNode>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Permission> getPermissionList() {
		return permissionList;
	}
	public void setPermissionList(List<Permission> permissionList) {
		this.permissionList = permissionList;
	}
	public Long getResourceId() {
		return resourceId;
	}
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public String getVoId() {
		return voId;
	}
	public void setVoId(String voId) {
		this.voId = voId;
	}
	public List<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public boolean isExpanded() {
		return expanded;
	}
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	
	
}

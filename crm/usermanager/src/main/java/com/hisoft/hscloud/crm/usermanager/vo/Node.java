package com.hisoft.hscloud.crm.usermanager.vo;

import java.util.LinkedList;
import java.util.List;


public class Node {
    
	private String id;
	private String primKey; // priKey
	private String resourceName; // resourceName
	private Long resourceId;
	private String resourceType;
	private boolean checked = false;
	
	
	List<Action> actions = new LinkedList<Action>();
	List<Node> children = new LinkedList<Node>();
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
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

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}
	
	
	

}

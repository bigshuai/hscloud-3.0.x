package com.hisoft.hscloud.crm.usermanager.util;

import java.util.ArrayList;
import java.util.List;

import com.hisoft.hscloud.crm.usermanager.entity.Action;

public class ResourceTree {
	
	private long resourceId;

	private long primKey;

	private String resourceType;
	
	private List<Action> actions = new ArrayList<Action>();
	
	private List<ResourceTree> children = new ArrayList<ResourceTree>();
	

	public long getResourceId() {
		return resourceId;
	}

	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}

	public long getPrimKey() {
		return primKey;
	}

	public void setPrimKey(long primKey) {
		this.primKey = primKey;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public List<ResourceTree> getChildren() {
		return children;
	}

	public void setChildren(List<ResourceTree> children) {
		this.children = children;
	}
	

}

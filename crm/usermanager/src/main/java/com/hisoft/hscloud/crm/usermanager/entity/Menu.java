package com.hisoft.hscloud.crm.usermanager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.hisoft.hscloud.common.entity.AbstractEntity;

@Entity
@Table(name = "hc_menu")
public class Menu extends AbstractEntity {

	@Column(name = "parent_id")
	private long parentId;
	@Column(name = "position")
	private int position;
	@Column(name = "icon")
	private String icon;
	
	@Column(name = "url")
	private String url;

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "Menu[" + super.toString() + "\",parentId=\"" + this.parentId
				+ "\",position=\"" + this.position + "\",icon=\"" + this.icon
				+ "\"]";
	}
}

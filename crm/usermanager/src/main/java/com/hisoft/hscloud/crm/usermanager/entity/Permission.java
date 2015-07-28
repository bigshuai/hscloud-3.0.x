package com.hisoft.hscloud.crm.usermanager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;



@Entity
@Table(name = "hc_permission",uniqueConstraints={@UniqueConstraint(columnNames={"resource_id","action_id"})})
public class Permission {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@OrderBy("id")
	long id;
	// 资源id
	@Column(name = "resource_id", nullable = false, length = 20)
	long resourceId;
	// 操作id
	@Column(name = "action_id", nullable = false, length = 20)
	long actionId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	@Override
	public String toString() {

		return "Permission[id=\"" + this.id + "\",resourceId=\""
				+ this.resourceId + "\",actionId=\"" + this.actionId + "\"]";

	}

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof Permission)) {
			return false;
		} else {

			final Permission p = (Permission) obj;
			if (this.actionId == p.actionId
					&& this.resourceId == p.resourceId) {
				return true;
			} else {
				return false;
			}

		}

	}

	
	

}

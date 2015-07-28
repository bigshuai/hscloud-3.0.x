package com.hisoft.hscloud.crm.usermanager.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hisoft.hscloud.common.entity.AbstractEntity;

@Entity
@Table(name = "hc_action")
public class Action extends AbstractEntity {

	long level;
	String classKey;
	@Column(nullable = false)
	String actionType;

	short front;// 前台（0，"前后台都显示" 1."前台显示" 2."后台显示"）

	public String getClassKey() {
		return classKey;
	}

	public void setClassKey(String classKey) {
		this.classKey = classKey;
	}

	@Transient
	public Date getUpdateDate() {
		return super.getUpdateDate();
	}

	public void setUpdateDate(Date updateDate) {
		super.setUpdateDate(updateDate);
	}

	public long getLevel() {
		return level;
	}

	public void setLevel(long level) {
		this.level = level;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public short getFront() {
		return front;
	}

	public void setFront(short front) {
		this.front = front;
	}

	@Override
	public String toString() {

		return "Action=[" + super.toString() + "\",classKey=\"" + this.classKey
				+ "\",actionType=\"" + this.actionType + "\",level=\""
				+ this.level + "\",front=\"" + this.front + "\"]";

	}

}

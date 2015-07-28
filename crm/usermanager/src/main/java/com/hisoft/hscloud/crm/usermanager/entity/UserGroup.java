package com.hisoft.hscloud.crm.usermanager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.hisoft.hscloud.common.entity.AbstractEntity;
import com.hisoft.hscloud.crm.usermanager.constant.UserGroupState;

@Entity
@Table(name = "hc_usergroup")
public class UserGroup extends AbstractEntity {

	// delete flag
	@Column(name = "flag")
	private short flag = UserGroupState.ACTIVE.getIndex();

	public short getFlag() {
		return flag;
	}

	public void setFlag(short flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {

		return "UserGroup[" + super.toString() + ",\"flag=\"" + this.flag
				+ "\"]";

	}

}

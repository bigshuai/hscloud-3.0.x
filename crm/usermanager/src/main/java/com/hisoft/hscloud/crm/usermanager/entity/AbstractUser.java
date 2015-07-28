/**
 * @title AbstractUser.java
 * @package com.hisoft.hscloud.crm.usermanager.entity
 * @description 用一句话描述该文件做什么
 * @author guole.liang
 * @update 2012-6-13 下午3:27:23
 * @version V1.1
 */
package com.hisoft.hscloud.crm.usermanager.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.hisoft.hscloud.common.entity.AbstractEntity;
import com.hisoft.hscloud.crm.usermanager.constant.UserState;

@MappedSuperclass
public abstract class AbstractUser extends AbstractEntity {

	@Column(nullable = false,columnDefinition="varchar(255) binary")
	private String password;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(name = "is_enable")
	private Short enable = UserState.CREATE.getIndex();
	@Column(name = "last_login_date")
	private Date lastLoginDate;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Short getEnable() {
		return enable;
	}

	public void setEnable(Short enable) {
		this.enable = enable;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public boolean isActive() {
		if (this.enable == UserState.ACTIVE.getIndex()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isApproved(){
		if (this.enable == UserState.APPROVED.getIndex()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {

		return super.toString() + "\",password=\"" + this.password
				+ "\",email=\"" + this.email + "\",enable=\"" + this.enable
				+ "\",lastLoginDate=\"" + "\"";

	}

}

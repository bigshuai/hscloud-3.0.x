package com.hisoft.hscloud.crm.usermanager.entity;

import com.hisoft.hscloud.crm.usermanager.entity.AbstractUser;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "hc_admin")
public class Admin extends AbstractUser {

	@Column(name = "is_super")
	private Boolean isSuper; // 判断是否为超级管理员
	private String telephone;
	private Integer type; // 1-分平台 2-大客户

	
	public Boolean getIsSuper() {
		return isSuper;
	}

	public void setIsSuper(Boolean isSuper) {
		this.isSuper = isSuper;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Admin() {

	}

	public Admin(Long id, String name) {
		super();
		super.setId(id);
	}

	@Override
	public String toString() {
		return "Admin [" + super.toString() + ",isSuper=\"" + this.isSuper
				+ ",type=\"" + this.type + "\",telephone=" + this.telephone
				+ "\"]";
	}

}

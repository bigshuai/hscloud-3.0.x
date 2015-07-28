package com.hisoft.hscloud.crm.usermanager.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import com.hisoft.hscloud.common.entity.AbstractEntity;

@Entity
@Table(name = "hc_company")
public class Company extends AbstractEntity {

	@Override
	public String toString() {
		return "Company[" + super.toString() +"]";
	}

}

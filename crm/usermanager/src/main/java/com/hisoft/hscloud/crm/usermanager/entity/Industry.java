/**
 * @title Industry.java
 * @package com.hisoft.hscloud.crm.usermanager.entity
 * @description 用一句话描述该文件做什么
 * @author guole.liang
 * @update 2012-5-7 上午10:06:45
 * @version V1.0
 */
package com.hisoft.hscloud.crm.usermanager.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.hisoft.hscloud.common.entity.AbstractEntity;

/**
 * @description 行业
 * @version 1.0
 * @author guole.liang
 * @update 2012-5-7 上午10:06:45
 */
@Entity
@Table(name = "hc_industry")
public class Industry extends AbstractEntity {

    @Column(name="name_code")
	private String nameCode;

	public String getNameCode() {
		return nameCode;
	}

	public void setNameCode(String nameCode) {
		this.nameCode = nameCode;
	}


	@Override
	public String toString() {
		return "Industry=[" + super.toString() + ",name=\"" + this.nameCode + "]";
	}

}

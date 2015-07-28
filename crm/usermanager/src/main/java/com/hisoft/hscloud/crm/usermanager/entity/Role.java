/**
 * @title Role.java
 * @package com.hisoft.hscloud.crm.usermanager.entity
 * @description 用一句话描述该文件做什么
 * @author guole.liang
 * @update 2012-5-9 下午4:14:43
 * @version V1.1
 */
package com.hisoft.hscloud.crm.usermanager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.hisoft.hscloud.common.entity.AbstractEntity;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.1
 * @author guole.liang
 * @update 2012-5-9 下午4:14:43
 */
@Entity
@Table(name = "hc_role")
public class Role extends AbstractEntity {

	@Column(name = "code")
	private String code;
	
	//0表示其他，1是前台初始角色，2是后台初始角色
	@Column(name = "status")
	private int status = 0;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {

		return "Role[" + super.toString() + ",code=\"" + this.code + "\"" + ",status=\"" + this.status + "\"]";

	}

}

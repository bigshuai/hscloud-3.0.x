/**
 * @title Region.java
 * @package com.hisoft.hscloud.crm.usermanager.entity
 * @description 用一句话描述该文件做什么
 * @author guole.liang
 * @update 2012-5-7 上午10:07:11
 * @version V1.0
 */
package com.hisoft.hscloud.crm.usermanager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.hisoft.hscloud.common.entity.AbstractEntity;
import com.hisoft.hscloud.crm.usermanager.entity.Country;

/**
 * @description 地区
 * @version 1.0
 * @author guole.liang
 * @update 2012-5-7 上午10:07:11
 */
@Entity
@Table(name = "hc_region")
public class Region extends AbstractEntity {
	@Column(name = "name_code")
	private String nameCode;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;

	public String getNameCode() {
		return nameCode;
	}

	public void setNameCode(String nameCode) {
		this.nameCode = nameCode;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Region(Long id, String nameCode) {
		super();
		super.setId(id);
		this.nameCode = nameCode;
	}

	public Region() {
	}

	@Override
	public String toString() {

		return "Region[" + super.toString() + "\",nameCode=\"" + this.nameCode
				+ "\"]";

	}

}

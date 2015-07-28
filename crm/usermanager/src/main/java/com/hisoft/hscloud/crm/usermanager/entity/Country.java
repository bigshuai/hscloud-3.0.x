/**
 * @title Country.java
 * @package com.hisoft.hscloud.crm.usermanager.entity
 * @description 用一句话描述该文件做什么
 * @author guole.liang
 * @update 2012-5-7 上午10:07:00
 * @version V1.0
 */
package com.hisoft.hscloud.crm.usermanager.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.hisoft.hscloud.common.entity.AbstractEntity;

/**
 * @description 国家
 * @version 1.0
 * @author guole.liang
 * @update 2012-5-7 上午10:07:00
 */
@Entity
@Table(name = "hc_country")
public class Country extends AbstractEntity {

    @Column(name="name_code")
	private String nameCode;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
    @OrderBy("id")
	private Set<Region> regions = new HashSet<Region>();



	


	public String getNameCode() {
		return nameCode;
	}

	public void setNameCode(String nameCode) {
		this.nameCode = nameCode;
	}

	public Set<Region> getRegions() {
		return regions;
	}

	public void setRegions(Set<Region> regions) {
		this.regions = regions;
	}

	@Override
	public String toString() {
		return "Country[" + super.toString() + ",name_ch=\"" 
				+ "\",name_code=\"" + this.nameCode + "\"]";
	}

}

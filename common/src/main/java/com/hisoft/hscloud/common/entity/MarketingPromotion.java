//$Id: MarketingPromotion.java Sep 18, 2013 2:03:10 PM liyunhui  $begin:~
package com.hisoft.hscloud.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @className: MarketingPromotion
 * @package: com.hisoft.hscloud.common.entity
 * @description: 市场推广实体类
 * @author: liyunhui
 * @since: 1.0
 * @createTime: Sep 18, 2013 2:03:10 PM
 * @company: Pactera Technology International Ltd
 */
@Entity
@Table(name = "hc_marketing_promotion")
public class MarketingPromotion extends AbstractEntity {

	@Column(nullable = false)
	private String code;// 推广代码
	@Column(nullable = false)
	private String address;// 推广地址
	@Column(nullable = false)
	private Long domain_id;// 所属分平台的id
	@Column(nullable = false)
	private String brand_code;// 关联品牌的code
	@Column(nullable = false)
	private short status = (short) 1;// 状态: 0为停用，1为启用
	private String description;// 推广描述

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getDomain_id() {
		return domain_id;
	}

	public void setDomain_id(Long domain_id) {
		this.domain_id = domain_id;
	}

	public String getBrand_code() {
		return brand_code;
	}

	public void setBrand_code(String brand_code) {
		this.brand_code = brand_code;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "MarketingPromotion [code=" + code + ", address=" + address
				+ ", domain_id=" + domain_id + ", brand_code=" + brand_code
				+ ", status=" + status + ", description=" + description + "]";
	}

}
//$Id: MarketingPromotion.java Sep 18, 2013 2:03:10 PM liyunhui  $end:~
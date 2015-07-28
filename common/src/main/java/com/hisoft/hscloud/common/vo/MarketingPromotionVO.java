//$Id: MarketingPromotionVO.java Sep 26, 2013 3:04:04 PM liyunhui  $begin:~
package com.hisoft.hscloud.common.vo;

import java.util.Date;

/**
 * @className: MarketingPromotionVO
 * @package: com.hisoft.hscloud.common.vo
 * @description: TODO
 * @author: liyunhui
 * @since: 1.0
 * @createTime: Sep 26, 2013 3:04:04 PM
 * @company: Pactera Technology International Ltd
 */
public class MarketingPromotionVO {

	private long id;// 主键id
	private String name;// 推广名称
	private String code;// 推广代码
	private String address;// 推广地址
	private Long domain_id;// 所属分平台的id
	private String domain_abbreviation;// 所属分平台的缩写名称
	private String brand_code;// 关联品牌的code
	private String brand_name;// 关联品牌的名称
	private short status = (short) 1;// 状态: 0为停用，1为启用
	private String description;// 推广描述
	private Date createDate = new Date();

	public MarketingPromotionVO() {
		super();
	}

	public MarketingPromotionVO(long id, String name, String code,
			String address, Long domain_id, String brand_code, short status,
			String description, Date createDate) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.address = address;
		this.domain_id = domain_id;
		this.brand_code = brand_code;
		this.status = status;
		this.description = description;
		this.createDate = createDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public String getDomain_abbreviation() {
		return domain_abbreviation;
	}

	public void setDomain_abbreviation(String domain_abbreviation) {
		this.domain_abbreviation = domain_abbreviation;
	}

	public String getBrand_code() {
		return brand_code;
	}

	public void setBrand_code(String brand_code) {
		this.brand_code = brand_code;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "MarketingPromotionVO [id=" + id + ", name=" + name + ", code="
				+ code + ", address=" + address + ", domain_id=" + domain_id
				+ ", domain_abbreviation=" + domain_abbreviation
				+ ", brand_code=" + brand_code + ", brand_name=" + brand_name
				+ ", status=" + status + ", description=" + description
				+ ", createDate=" + createDate + "]";
	}

}
//$Id: MarketingPromotionVO.java Sep 26, 2013 3:04:04 PM liyunhui  $end:~
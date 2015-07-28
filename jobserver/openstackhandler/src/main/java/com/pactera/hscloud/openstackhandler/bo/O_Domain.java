package com.pactera.hscloud.openstackhandler.bo;


public class O_Domain {

	private Long id;

	private String code;

	private String name;

	// 简称
	private String abbreviation;

	// 描述
	private String description;

	// 地址
	private String address;

	// 分平台网址
	private String url;

	// 电话
	private String telephone;

	// 状态 1:有效； 0：无效
	private String status;

	// 银行
	private String bank;

	// 卡号
	private String card_no;

	// 服务热线
	private String service_hotline;

	// 发布地址
	private String publishing_address;
	
	//控制面板url
	private String publishing_address_cp;

	// 备注
	private String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getService_hotline() {
		return service_hotline;
	}

	public void setService_hotline(String service_hotline) {
		this.service_hotline = service_hotline;
	}

	public String getPublishing_address() {
		return publishing_address;
	}

	public void setPublishing_address(String publishing_address) {
		this.publishing_address = publishing_address;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

    public String getPublishing_address_cp() {
        return publishing_address_cp;
    }

    public void setPublishing_address_cp(String publishing_address_cp) {
        this.publishing_address_cp = publishing_address_cp;
    }

}

package com.hisoft.hscloud.crm.usermanager.vo;

/**
 * 分平台对象
 * @author lihonglei
 *
 */
public class DomainVO {

    private long id;
    private String code;//编码
    private String name;//名称
    private String abbreviation;//简称
    private String description;//描述
    private String address;//地址
    private String url;//分平台网址
    private String telephone;//电话
    private String bank;//银行
    private String cardNo;//卡号
    private String serviceHotline;//服务热线
    private String publishingAddress;//发布地址
    private String publishing_address_cp;//控制面板URL
    private String copyright_zh_CN;//版权信息中文
    private String copyright_en_US;//版权信息英文
    private boolean onlinePayFlag;//在线支付状态

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getServiceHotline() {
        return serviceHotline;
    }

    public void setServiceHotline(String serviceHotline) {
        this.serviceHotline = serviceHotline;
    }

    public String getPublishingAddress() {
        return publishingAddress;
    }

    public void setPublishingAddress(String publishingAddress) {
        this.publishingAddress = publishingAddress;
    }

    public boolean isOnlinePayFlag() {
        return onlinePayFlag;
    }

    public void setOnlinePayFlag(boolean onlinePayFlag) {
        this.onlinePayFlag = onlinePayFlag;
    }

	public String getPublishing_address_cp() {
		return publishing_address_cp;
	}

	public void setPublishing_address_cp(String publishing_address_cp) {
		this.publishing_address_cp = publishing_address_cp;
	}

	public String getCopyright_zh_CN() {
		return copyright_zh_CN;
	}

	public void setCopyright_zh_CN(String copyright_zh_CN) {
		this.copyright_zh_CN = copyright_zh_CN;
	}

	public String getCopyright_en_US() {
		return copyright_en_US;
	}

	public void setCopyright_en_US(String copyright_en_US) {
		this.copyright_en_US = copyright_en_US;
	}

    
}
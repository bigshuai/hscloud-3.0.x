package com.hisoft.hscloud.crm.usermanager.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.hisoft.hscloud.common.entity.UserBrand;

/**
 * 分平台
 * @author lihonglei
 *  
 */
@Entity
@Table(name = "hc_domain")
public class Domain {
 
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;// 主健
    @Column(name = "code", nullable = false)
    private String code;//编码
    @Column(name = "name", nullable = false)
    private String name;//名称
    @Column(name = "abbreviation", nullable = false)
    private String abbreviation;//简称
    @Column(name = "description", nullable = false)
    private String description;//描述
    @Column(name = "address", nullable = false)
    private String address;//地址
    @Column(name = "url", nullable = false)
    private String url;//分平台网址
    @Column(name = "telephone", nullable = false)
    private String telephone;//电话
    @Column(name = "status", nullable = false)
    private String status;//状态 1:有效； 0：无效
    @Column(name = "create_time", nullable = false)
    private Date createTime = new Date();//创建时间
    @Column(name = "create_id", nullable = false)
    private long createId;//创建者
    @Column(name = "update_time", nullable = false)
    private Date updateTime = new Date();//更新时间
    @Column(name = "update_id", nullable = false)
    private long updateId;//更新用户id
    @Column(name = "bank", nullable = false)
    private String bank;//银行
    @Column(name = "card_no", nullable = false)
    private String cardNo;//卡号
    @Column(name = "service_hotline", nullable = false)
    private String serviceHotline;//服务热线
    @Column(name = "publishing_address", nullable = false)
    private String publishingAddress;//发布地址
    @Column(name = "publishing_address_cp", nullable = false)
    private String publishing_address_cp;//控制面板URL
    @Column(name = "copyright_zh_CN", nullable = true)
    private String copyright_zh_CN;//版权信息中文
    @Column(name = "copyright_en_US", nullable = true)
    private String copyright_en_US;//版权信息英文   
    @Column(name = "online_pay_flag", nullable = true)
    private Boolean onlinePayFlag;//在线支付状态
    @Column(name = "remark", nullable = true)
    private String remark;//备注
    @Column(name = "transfer_flag", nullable = true,columnDefinition="INT default 0")
    private Integer transferFlag;//是否允许跨平台转账（0：不允许；1：允许）
    
    @ManyToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name="hc_domain_userbrand",
	joinColumns=@JoinColumn(name="domain_id",referencedColumnName="id"),
	inverseJoinColumns=@JoinColumn(name="userbrand_id",referencedColumnName="id"))
	private List<UserBrand> userBrandList;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getCreateId() {
        return createId;
    }

    public void setCreateId(long createId) {
        this.createId = createId;
    }
    
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(long updateId) {
        this.updateId = updateId;
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

	public List<UserBrand> getUserBrandList() {
		return userBrandList;
	}

	public void setUserBrandList(List<UserBrand> userBrandList) {
		this.userBrandList = userBrandList;
	}

	public Boolean getOnlinePayFlag() {
		return onlinePayFlag;
	}

	public void setOnlinePayFlag(Boolean onlinePayFlag) {
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

	public Integer getTransferFlag() {
		return transferFlag;
	}

	public void setTransferFlag(Integer transferFlag) {
		this.transferFlag = transferFlag;
	}

}
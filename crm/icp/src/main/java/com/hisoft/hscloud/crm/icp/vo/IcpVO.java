/* 
* 文 件 名:  IcpVO.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-1-7 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.crm.icp.vo; 

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-1-7] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class IcpVO {
	
    
    private String registered;
	private String domain;
	private String ip;
	
	private String memberLogin;
	private String memberPwd;
	
	private String memberType;
	private String name;
	
	private String orgcertId;
	private String icpcertId;
	private String icpUrl;
	private String icpwebTitle;
	private String contactName;
	
	private String country;
	private String province;
	private String city;
	private String address;
	private String postcode;
	private String telno;
	private String mobile;
	
	private String idType;
	private String idNumber;
	
	private String email;

	public String getRegistered() {
		return registered;
	}

	public void setRegistered(String registered) {
		this.registered = registered;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMemberLogin() {
		return memberLogin;
	}

	public void setMemberLogin(String memberLogin) {
		this.memberLogin = memberLogin;
	}

	public String getMemberPwd() {
		return memberPwd;
	}

	public void setMemberPwd(String memberPwd) {
		this.memberPwd = memberPwd;
	}

	public String getOrgcertId() {
        return orgcertId;
    }

    public void setOrgcertId(String orgcertId) {
        this.orgcertId = orgcertId;
    }

    public String getIcpcertId() {
        return icpcertId;
    }

    public void setIcpcertId(String icpcertId) {
        this.icpcertId = icpcertId;
    }

    public String getIcpUrl() {
        return icpUrl;
    }

    public void setIcpUrl(String icpUrl) {
        this.icpUrl = icpUrl;
    }

    public String getIcpwebTitle() {
        return icpwebTitle;
    }

    public void setIcpwebTitle(String icpwebTitle) {
        this.icpwebTitle = icpwebTitle;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getTelno() {
		return telno;
	}

	public void setTelno(String telno) {
		this.telno = telno;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
	
}

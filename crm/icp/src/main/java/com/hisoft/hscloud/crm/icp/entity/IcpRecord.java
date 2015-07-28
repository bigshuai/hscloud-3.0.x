/* 
* 文 件 名:  IcpRecord.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-1-9 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.crm.icp.entity; 

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-1-9] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Entity
@Table(name = "hc_icp_record")
public class IcpRecord {
    // 主健
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @Column(name = "registered", nullable = false)
    private String registered;
    
    @Column(name = "domain", nullable = false)
    private String domain;
    
    @Column(name = "ip", nullable = false)
    private String ip;
    
    @Column(name = "member_login", nullable = false)
    private String memberLogin;
    
    @Column(name = "member_pwd", nullable = false)
    private String memberPwd;
    
    @Column(name = "orgcert_id", nullable = true)
    private String orgcertId;
    
    @Column(name = "icpcert_id", nullable = true)
    private String icpcertId;
    
    @Column(name = "icp_url", nullable = true)
    private String icpUrl;
    
    @Column(name = "icpweb_title", nullable = true)
    private String icpwebTitle;
    
    @Column(name = "contact_name", nullable = true)
    private String contactName;
    
    @Column(name = "city", nullable = true)
    private String city;
    
    @Column(name = "address", nullable = true)
    private String address;
    
    @Column(name = "postcode", nullable = true)
    private String postcode;
    
    @Column(name = "telno", nullable = true)
    private String telno;
    
    @Column(name = "mobile", nullable = true)
    private String mobile;
    
    @Column(name = "create_time", nullable = true)
    private Date createTime = new Date();
    
    @Column(name = "memberType", nullable = true)
    private String memberType;
    
    @Column(name = "name", nullable = true)
    private String name;
    
    @Column(name = "country", nullable = true)
    private String country;
    
    @Column(name = "province", nullable = true)
    private String province;
    
    @Column(name = "idType", nullable = true)
    private String idType;
    
    @Column(name = "idNumber", nullable = true)
    private String idNumber;
    
    @Column(name = "email", nullable = true)
    private String email;
    
    @Column(name = "remark", nullable = true)
    private String remark;


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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
    
    
}

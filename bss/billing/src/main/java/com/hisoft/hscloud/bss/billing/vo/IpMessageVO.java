package com.hisoft.hscloud.bss.billing.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


public class IpMessageVO {

	private String providerCode="756041849";//接入网络服务商组织机构代码

	private String userName;//使用用户(单位名称)
	
	private String userProperties;//用户性质(页面上写0/1)，数据库中是norUser
	
	private String userAddress;//用户地址
	
	private String contactName ;//联系人姓名
	
	private String contactPhone;//联系人联系电话
	
	private int contactDocumentType=111;//联系人证件类型(111只有身份证)
	
//	private String contactDocumentNumStr;//联系人证件号码
	private String contactDocumentNum;//联系人证件号码

	
	private String installedAddress;//装机地址
	
	private String installedDateStr;//装机日期

	private String startIp;//起始IP地址
	
	private String endIp;//结束ip地址
	
	private String usingType="09";//使用类型
	
	private String iSDCode="110000000000";//Internet staff department code管辖地网安部门代码 110000000000

	private Date installedDate;//装机日期
		
	public String getProviderCode() {
		return providerCode;
	}

	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserProperties() {
		if(userProperties.endsWith("NorUser")){
			return "1";
		}else{
			return "0";
		}		
	}

	public void setUserProperties(String userProperties) {
		this.userProperties = userProperties;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public int getContactDocumentType() {
		return contactDocumentType;
	}

	public void setContactDocumentType(int contactDocumentType) {
		this.contactDocumentType = contactDocumentType;
	}

	public String getContactDocumentNum() {
		return contactDocumentNum;
	}

	public void setContactDocumentNum(String contactDocumentNum) {
		this.contactDocumentNum = contactDocumentNum;
	}

	public String getInstalledAddress() {
		return installedAddress;
	}

	public void setInstalledAddress(String installedAddress) {
		this.installedAddress = installedAddress;
	}	

	public Date getInstalledDate() {				
//		Date date = new Date(installedDate.getTime());
		return installedDate;
	}

	public void setInstalledDate(Date installedDate) {
		this.installedDate = installedDate;
	}
	
	public String getInstalledDateStr() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		return format.format(installedDate);
	}

	public void setInstalledDateStr(String installedDateStr) {
		this.installedDateStr = installedDateStr;
	}

	public String getStartIp() {
		return startIp;
	}

	public void setStartIp(String startIp) {
		this.startIp = startIp;
	}

	public String getEndIp() {
		return endIp;
	}

	public void setEndIp(String endIp) {
		this.endIp = endIp;
	}

	public String getUsingType() {
		return usingType;
	}

	public void setUsingType(String usingType) {
		this.usingType = usingType;
	}

	public String getISDCode() {
		return iSDCode;
	}

	public void setISDCode(String iSDCode) {
		this.iSDCode = iSDCode;
	}

//	public String getContactDocumentNumStr() {
//		return String.valueOf(this.contactDocumentNum);
//	}
//
//	public void setContactDocumentNumStr(String contactDocumentNumStr) {
//		this.contactDocumentNumStr = contactDocumentNumStr;
//	}	
//	
	
	
}

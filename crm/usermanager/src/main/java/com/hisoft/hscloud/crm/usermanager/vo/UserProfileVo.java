package com.hisoft.hscloud.crm.usermanager.vo;

import com.hisoft.hscloud.common.entity.AbstractVO;
import com.hisoft.hscloud.crm.usermanager.entity.Country;
import com.hisoft.hscloud.crm.usermanager.entity.Industry;
import com.hisoft.hscloud.crm.usermanager.entity.Region;

public class UserProfileVo extends AbstractVO {
	private String email;
	private String name;
	private long id;
	private long userId;
	private long domainId;
	private String userType;
	private boolean sex;
	private String idCard;
	private String telephone;
	private String address;
	private String company;
	private Industry industry;
	private Country country;
	private Region region;
	private String level;
	//修改人：张建伟  修改时间：20130906
	private String description;
	//企业号
	private String companyNO;
	
	//供应商
	private boolean supplier;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public boolean getSex() {
		return sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Industry getIndustry() {
		return industry;
	}

	public void setIndustry(Industry industry) {
		this.industry = industry;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public long getDomainId() {
		return domainId;
	}

	public void setDomainId(long domainId) {
		this.domainId = domainId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	//修改人：张建伟  修改时间：20130906
	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getCompanyNO() {
		return companyNO;
	}

	public void setCompanyNO(String companyNO) {
		this.companyNO = companyNO;
	}

	public boolean isSupplier() {
		return supplier;
	}

	public void setSupplier(boolean supplier) {
		this.supplier = supplier;
	}

	//修改人：张建伟  修改时间：20130906
	@Override
	public String toString() {

		return "UserProfile[" + super.toString() + ",sex=\"" + this.sex
				+ "\",idCard=\"" + this.idCard + "\",telephone=\""
				+ this.telephone + "\",address=\"" + this.address
				+ "\",level=\"" + this.level + "\",userId=\"" + this.userId
				+ "\",supplier=\"" + this.supplier + "\",companyNO=\"" + this.companyNO
				+ "\",description=\"" + this.description + "\"]";

	}

}

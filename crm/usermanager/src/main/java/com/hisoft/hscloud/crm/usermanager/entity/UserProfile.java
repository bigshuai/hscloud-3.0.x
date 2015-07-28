package com.hisoft.hscloud.crm.usermanager.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.hisoft.hscloud.common.entity.AbstractEntity;
import com.hisoft.hscloud.crm.usermanager.entity.Country;
import com.hisoft.hscloud.crm.usermanager.entity.Industry;
import com.hisoft.hscloud.crm.usermanager.entity.Region;



@Entity
@Table(name = "hc_user_profile")
public class UserProfile extends AbstractEntity {

	private boolean sex;
	@Column(name = "id_card")
	private String idCard;
	private String telephone;
	private String company;
	private String address;
	//wgsg 企业号
	private String companyNO="";
	//0:默认值（未申请）1:申请中 2:已拒绝
	private int supplierStatus=0;
	//wgsg 是否是供销商
	private boolean supplier=false;
	//修改人：张建伟  修改时间：20130906
	@Column(name = "description",length=250)
	private String description;
	@OneToOne
	private Industry industry;
	@OneToOne
	private Country country;
	@OneToOne
	private Region region;
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Boolean getSex() {
		return sex;
	}

	public void setSex(Boolean sex) {
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
	//修改人：张建伟  修改时间：20130906
	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		this.description=description;
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
	
	public int getSupplierStatus() {
		return supplierStatus;
	}

	public void setSupplierStatus(int supplierStatus) {
		this.supplierStatus = supplierStatus;
	}

	@Override
	public String toString() {
		return "UserProfile [sex=" + sex + ", idCard=" + idCard
				+ ", telephone=" + telephone + ", company=" + company
				+ ", address=" + address + ", companyNO=" + companyNO
				+ ", supplierStatus=" + supplierStatus + ", supplier="
				+ supplier + ", description=" + description + ", industry="
				+ industry + ", country=" + country + ", region=" + region
				+ "]";
	}
}

package com.hisoft.hscloud.crm.usermanager.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.hisoft.hscloud.common.entity.AbstractVO;

@Entity
@Table(name="hc_company_invite")
public class CompanyInviteVO extends AbstractVO{
	
	@Column(nullable=false)
	private Long senderId;
	
	@Column(nullable=false)
	private Long receiverId;
	
	private short status;
	
	private long companyId;
	
	//公司名称
	private String companyName;

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
	
	
	

}

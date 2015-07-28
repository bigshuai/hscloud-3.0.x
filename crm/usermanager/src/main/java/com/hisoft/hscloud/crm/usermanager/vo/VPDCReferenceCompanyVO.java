package com.hisoft.hscloud.crm.usermanager.vo;

public class VPDCReferenceCompanyVO{
	
	private int id;
	
	private long vpdcRenferenceId;
	
//	private long renferenceId;
	
	private long companyId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getVpdcRenferenceId() {
		return vpdcRenferenceId;
	}

	public void setVpdcRenferenceId(long vpdcRenferenceId) {
		this.vpdcRenferenceId = vpdcRenferenceId;
	}
	

	public long getCompanyId() {
		return companyId;
	}

//	public long getRenferenceId() {
//		return renferenceId;
//	}
//
//	public void setRenferenceId(long renferenceId) {
//		this.renferenceId = renferenceId;
//	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
	

}

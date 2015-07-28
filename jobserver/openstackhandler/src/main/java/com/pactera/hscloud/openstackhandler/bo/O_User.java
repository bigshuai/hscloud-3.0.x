package com.pactera.hscloud.openstackhandler.bo;

public class O_User {

	private Long id;

	private Long domain_id;
	
	private String email;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDomain_id() {
		return domain_id;
	}

	public void setDomain_id(Long domain_id) {
		this.domain_id = domain_id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}

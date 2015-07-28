package com.pactera.hscloud.openstackhandler.bo;

public class O_Network {
	
	private Long id;
	
	private Long objectId;
	
	private Long networkId;
	
	private Long objectType;
	
	private String objectUUID;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Long getNetworkId() {
		return networkId;
	}

	public void setNetworkId(Long networkId) {
		this.networkId = networkId;
	}

	public Long getObjectType() {
		return objectType;
	}

	public void setObjectType(Long objectType) {
		this.objectType = objectType;
	}

	public String getObjectUUID() {
		return objectUUID;
	}

	public void setObjectUUID(String objectUUID) {
		this.objectUUID = objectUUID;
	}

}

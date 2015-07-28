package com.hisoft.hscloud.crm.usermanager.vo;
import com.hisoft.hscloud.common.entity.AbstractVO;

public class VPDCReferenceVO extends AbstractVO {
	

	private String imageId;

	private String flavorId;

	private int volume_id;

	private int status;//0:正常；1：删除/停用
	
	

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getFlavorId() {
		return flavorId;
	}

	public void setFlavorId(String flavorId) {
		this.flavorId = flavorId;
	}

	public int getVolume_id() {
		return volume_id;
	}

	public void setVolume_id(int volume_id) {
		this.volume_id = volume_id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	

}

package com.pactera.hscloud.openstackhandler.bo;


public class O_VPDC_Extdisk {

	private Integer id;

	private String name;

	private String vmId;

	private Long referenceId;

	private Integer volumeId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVmId() {
		return vmId;
	}

	public void setVmId(String vmId) {
		this.vmId = vmId;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

	public Integer getVolumeId() {
		return volumeId;
	}

	public void setVolumeId(Integer volumeId) {
		this.volumeId = volumeId;
	}

	public O_VPDC_Extdisk copy(O_VPDC_Extdisk replaceVO) {
		if(null == replaceVO){
			return this;
		}
		this.setName((null == replaceVO.getName() || "".equals(replaceVO
				.getName())) ? this.getName() : replaceVO.getName());
		this.setReferenceId((null == replaceVO.getReferenceId() || ""
				.equals(replaceVO.getReferenceId())) ? this.getReferenceId()
				: replaceVO.getReferenceId());
		this.setVmId((null == replaceVO.getVmId() || "".equals(replaceVO
				.getVmId())) ? this.getVmId() : replaceVO.getVmId());
		this.setVolumeId((null == replaceVO.getVolumeId() || ""
				.equals(replaceVO.getVolumeId())) ? this.getVolumeId()
				: replaceVO.getVolumeId());
		return this;
	}

}

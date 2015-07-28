package com.pactera.hscloud.openstackhandler.bo;


public class O_VPDCInstance {
	
	private Long id;

	private String vm_id;

	private Long VpdcRefrenceId;
	
	private String nodeName;
	
	private Long status;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVm_id() {
		return vm_id;
	}

	public void setVm_id(String vm_id) {
		this.vm_id = vm_id;
	}

	public Long getVpdcRefrenceId() {
		return VpdcRefrenceId;
	}

	public void setVpdcRefrenceId(Long vpdcRefrenceId) {
		VpdcRefrenceId = vpdcRefrenceId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public O_VPDCInstance copy(O_VPDCInstance replaceVO) {
		if(null == replaceVO){
			return this;
		}
		this.setVm_id((null == replaceVO.getVm_id() || "".equals(replaceVO
				.getVm_id())) ? this.getVm_id() : replaceVO.getVm_id());
		this.setVpdcRefrenceId((null == replaceVO.getVpdcRefrenceId() || ""
				.equals(replaceVO.getVpdcRefrenceId())) ? this
				.getVpdcRefrenceId() : replaceVO.getVpdcRefrenceId());
		this.setNodeName((null == replaceVO.getNodeName() || ""
				.equals(replaceVO.getNodeName())) ? this
				.getNodeName() : replaceVO.getNodeName());
		this.setStatus((null == replaceVO.getStatus() || ""
				.equals(replaceVO.getStatus()))?this.getStatus():replaceVO.getStatus());
		return this;
	}

}

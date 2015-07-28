package com.pactera.hscloud.openstackhandler.bo;

public class O_Snapshot {

	private Long id;

	private String snapShot_id;

	private Long instanceId;

	// 是否备份完成状态（0:未完成；1：已完成）
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSnapShot_id() {
		return snapShot_id;
	}

	public void setSnapShot_id(String snapShot_id) {
		this.snapShot_id = snapShot_id;
	}

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public O_Snapshot copy(O_Snapshot replaceVO) {
		if (null == replaceVO) {
			return this;
		}
		this.setStatus((null == replaceVO.getStatus() || "".equals(replaceVO
				.getStatus())) ? this.getStatus() : replaceVO.getStatus());
		this.setId((null == replaceVO.getId() || "".equals(replaceVO.getId())) ? this
				.getId() : replaceVO.getId());
		this.setInstanceId((null == replaceVO.getInstanceId()
				|| "".equals(replaceVO.getInstanceId()) ? this.getInstanceId()
				: replaceVO.getInstanceId()));
		this.setSnapShot_id((null == replaceVO.getSnapShot_id()
				|| "".equals(replaceVO.getSnapShot_id()) ? this
				.getSnapShot_id() : replaceVO.getSnapShot_id()));
		return this;
	}

}

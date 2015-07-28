package org.openstack.model.compute.nova.volume;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("volumeAttachment")
public class NovaVolumeAttachment implements Serializable {

	private Integer id;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	private String serverId;

	public String getServerId() {
		return this.serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	private Integer volumeId;

	private String device;

	public Integer getVolumeId() {
		return volumeId;
	}

	public void setVolumeId(Integer volumeId) {
		this.volumeId = volumeId;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	@Override
	public String toString() {
		return "NovaVolumeAttachment [id=" + id + ", serverId=" + serverId
				+ ", volumeId=" + volumeId + ", device=" + device + "]";
	}

}

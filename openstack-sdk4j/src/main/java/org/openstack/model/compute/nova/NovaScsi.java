package org.openstack.model.compute.nova;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

public class NovaScsi implements Serializable {

	private String status;

	@JsonProperty("display_name")
	private String displayName;

	private String name;

	private int deleted;

	@JsonProperty("created_at")
	private String createdAt;

	private String mountpoint;

	private int id;

	private int size;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getMountpoint() {
		return mountpoint;
	}

	public void setMountpoint(String mountpoint) {
		this.mountpoint = mountpoint;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "NovaScsi [status=" + status + ", displayName=" + displayName
				+ ", name=" + name + ", deleted=" + deleted + ", createdAt="
				+ createdAt + ", mountpoint=" + mountpoint + ", id=" + id
				+ ", size=" + size + "]";
	}

}

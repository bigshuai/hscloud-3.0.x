package org.openstack.model.compute.nova.snapshot;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@XmlRootElement(name="snapshot")
@XmlAccessorType(XmlAccessType.NONE)
@JsonRootName("snapshot")
public class Snapshot implements Serializable {

	@JsonProperty("name")
	private String name;

	@JsonProperty("create_at")
	private String createTime;

	@JsonProperty("state")
	private String status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Snapshot [name=" + name + ", createTime=" + createTime
				+ ", status=" + status + "]";
	}

}

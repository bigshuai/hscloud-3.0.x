package org.openstack.model.hscloud.impl;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("disableHostService")
public class HostServiceDisable implements Serializable {
	@JsonProperty("topic")
	private String topic;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	@Override
	public String toString() {
		return "HostServiceDisable [topic=" + topic + "]";
	}
	
}

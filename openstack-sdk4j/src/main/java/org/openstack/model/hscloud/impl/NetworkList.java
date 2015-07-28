package org.openstack.model.hscloud.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name = "networks")
public class NetworkList implements Serializable {
	@XmlElement(name = "network")
	@JsonProperty("networks")
	private List<Network> lsl = new ArrayList<Network>();

	public List<Network> getLsl() {
		return lsl;
	}

	public void setLsl(List<Network> lsl) {
		this.lsl = lsl;
	}

	@Override
	public String toString() {
		return "NetworkList [lsl=" + lsl + "]";
	}
	
}

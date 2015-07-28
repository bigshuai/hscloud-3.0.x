package org.openstack.model.hscloud.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name = "security_lans")
public class SecurityLanList implements Serializable{
	
	//@XmlElement(name = "security_lan")
	@JsonProperty("security_lans")
	private List<SecurityLan> lsl = new ArrayList<SecurityLan>();

	public List<SecurityLan> getLsl() {
		return lsl;
	}

	public void setLsl(List<SecurityLan> lsl) {
		this.lsl = lsl;
	}

	@Override
	public String toString() {
		return "SecurityLanList [lsl=" + lsl + "]";
	}
	
}

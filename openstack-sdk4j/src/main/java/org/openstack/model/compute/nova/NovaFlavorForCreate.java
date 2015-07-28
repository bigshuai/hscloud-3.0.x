package org.openstack.model.compute.nova;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.openstack.model.compute.FlavorForCreate;
@XmlRootElement(name = "flavor")
@XmlAccessorType(XmlAccessType.NONE)
@JsonRootName("flavor")
public class NovaFlavorForCreate implements Serializable, FlavorForCreate{
	@XmlAttribute(name="id")
	@JsonProperty("id")
	private int id;
	
	@XmlAttribute(name="name")
	@JsonProperty("name")
	private String name;
	
	@XmlAttribute(name="ram")
	@JsonProperty("ram")
	private int ram;
	
	@XmlAttribute(name="vcpus")
	@JsonProperty("vcpus")
	private int vcpus;
	
	@XmlAttribute(name="rxtx_factor")
	@JsonProperty("rxtx_factor")
	private int rxTxFactor;
	
	@XmlAttribute(name="disk")
	@JsonProperty("disk")
	private int disk;
	
	@XmlAttribute(name="swap")
	@JsonProperty("swap")
	private int swap;

	@XmlAttribute(name="OS-FLV-EXT-DATA:ephemeral")
	@JsonProperty("OS-FLV-EXT-DATA:ephemeral")
	private int ephemeral;	
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getId() {
		return this.id;
	}
	
	@Override
	public int getRam() {
		return this.ram;
	}

	@Override
	public int getVcpus() {
		return this.vcpus;
	}

	@Override
	public int getRxTxFactor() {
		return this.rxTxFactor;
	}

	@Override
	public int getDisk() {
		return this.disk;
	}

	@Override
	public int getSwap() {
		return swap;
	}

	@Override
	public int getEphemeral() {
		return this.ephemeral;
	}
	
	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setRam(int ram) {
		this.ram = ram;
	}

	@Override
	public void setVcpus(int vcpus) {
		this.vcpus = vcpus;
	}

	@Override
	public void setRxTxFactor(int rxTxFactor) {
		this.rxTxFactor = rxTxFactor;
	}

	@Override
	public void setDisk(int disk) {
		this.disk = disk;
	}

	public void setSwap(int swap) {
		this.swap = swap;
	}
	
	@Override
	public void setEphemeral(int ephemeral) {
		this.ephemeral = ephemeral;
	}
}

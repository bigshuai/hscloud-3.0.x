package org.openstack.model.compute.nova.floatingip;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.openstack.model.compute.FloatingIp;

@XmlRootElement(name = "floating_ip", namespace = "")
@XmlAccessorType(XmlAccessType.NONE)
@JsonRootName("floating_ip")
public class NovaFloatingIp implements Serializable, FloatingIp {

	@XmlAttribute
	private Integer id;

	@XmlAttribute
	private String ip;

	@XmlAttribute
	private String pool;

	@XmlAttribute(name = "instance_id")
	@JsonProperty("instance_id")
	private String instanceId;

	@XmlAttribute(name = "fixed_ip")
	@JsonProperty("fixed_ip")
	private String fixedIp;

	private FloatingIpStatus floatingIpStatus;

	public NovaFloatingIp() {

	}

	public NovaFloatingIp(Integer id, String ip, String fixedIp, String pool,
			String instanceId) {
		this.id = id;
		this.ip = ip;
		this.fixedIp = fixedIp;
		this.pool = pool;
		this.instanceId = instanceId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openstack.model.compute.nova.floatingip.FloatingIp#getId()
	 */
	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openstack.model.compute.nova.floatingip.FloatingIp#getIp()
	 */
	@Override
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openstack.model.compute.nova.floatingip.FloatingIp#getPool()
	 */
	@Override
	public String getPool() {
		return pool;
	}

	public void setPool(String pool) {
		this.pool = pool;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openstack.model.compute.nova.floatingip.FloatingIp#getInstanceId()
	 */
	@Override
	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openstack.model.compute.nova.floatingip.FloatingIp#getFixedIp()
	 */
	@Override
	public String getFixedIp() {
		return fixedIp;
	}

	public void setFixedIp(String fixedIp) {
		this.fixedIp = fixedIp;
	}

	@Override
	public String toString() {
		return "NovaFloatingIp [id=" + id + ", ip=" + ip + ", pool=" + pool
				+ ", instanceId=" + instanceId + ", fixedIp=" + fixedIp + "]";
	}

	/*
	 * (非 Javadoc) <p>Title: getFloatingIpStatus</p> <p>Description: </p>
	 * 
	 * @return
	 * 
	 * @see org.openstack.model.compute.FloatingIp#getFloatingIpStatus()
	 */
	@Override
	public FloatingIpStatus getFloatingIpStatus() {
		return this.floatingIpStatus;
	}

	public void setFloatingIpStatus(FloatingIpStatus floatingIpStatus) {
		this.floatingIpStatus = floatingIpStatus;
	}

}

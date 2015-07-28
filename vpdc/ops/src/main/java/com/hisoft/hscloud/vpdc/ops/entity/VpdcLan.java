package com.hisoft.hscloud.vpdc.ops.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


import com.hisoft.hscloud.common.entity.AbstractEntity;
@Entity
@Table(name = "hc_vpdc_lan")
public class VpdcLan  extends AbstractEntity {
	@Column(name = "lanId")
	private int lanId;

	@Column(name = "type")
	private String type;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = true)
	@JoinColumn(name="vpdc_id") 
	private Vpdc vpdc;
	
	@OneToMany(mappedBy = "securytyVlan", targetEntity = VpdcNetwork.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<VpdcNetwork> lanNetwork;
	
	@Column(name = "deleted")
	private int deleted;

	public int getLanId() {
		return lanId;
	}

	public void setLanId(int lanId) {
		this.lanId = lanId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Vpdc getVpdc() {
		return vpdc;
	}

	public void setVpdc(Vpdc vpdc) {
		this.vpdc = vpdc;
	}
	
	public Set<VpdcNetwork> getLanNetwork() {
		return lanNetwork;
	}

	public void setLanNetwork(Set<VpdcNetwork> lanNetwork) {
		this.lanNetwork = lanNetwork;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	
}

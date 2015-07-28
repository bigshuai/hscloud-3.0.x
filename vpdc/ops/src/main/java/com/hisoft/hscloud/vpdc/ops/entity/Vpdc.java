package com.hisoft.hscloud.vpdc.ops.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.hisoft.hscloud.common.entity.AbstractEntity;

@Entity
@Table(name = "hc_vpdc")
public class Vpdc extends AbstractEntity{

	/*@Column(name = "user_id", length = 128)
	private int userId;*/

	@OneToMany(mappedBy = "vpdc", targetEntity = VpdcReference.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<VpdcReference> references;
	
	@OneToMany(mappedBy = "vpdc", targetEntity = VpdcRouter.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<VpdcRouter> routers;
	
	@OneToMany(mappedBy = "vpdc", targetEntity = VpdcLan.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<VpdcLan> lans;

	/*@OneToMany(mappedBy = "vpdc", targetEntity = VpdcConfig.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<VpdcConfig> configs;*/

	/*@Column(name = "createTime")
	private Date createTime;*/

	@Column(name = "status", length = 24)
	private String status;

	@Lob
	@Column(name = "description", length = 256)
	private String description;
	
	@Column(name = "owner")
	private Long owner;
	
	@Column(name = "type")
	private int type;//0：（非路由）；1：（路由）
	
	@Column(name = "zoneGroup_id")
	private Long zoneGroupId;//VPDC所属机房线路
	
	@Column(name = "deleted")
	private int deleted;//未删除(0)；已删除(1)

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<VpdcReference> getReferences() {
		return references;
	}

	public void setReferences(Set<VpdcReference> references) {
		this.references = references;
	}

	public Set<VpdcRouter> getRouters() {
		return routers;
	}

	public void setRouters(Set<VpdcRouter> routers) {
		this.routers = routers;
	}

	public Set<VpdcLan> getLans() {
		return lans;
	}

	public void setLans(Set<VpdcLan> lans) {
		this.lans = lans;
	}
	
	public Long getOwner() {
		return owner;
	}

	public void setOwner(Long owner) {
		this.owner = owner;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public Long getZoneGroupId() {
		return zoneGroupId;
	}

	public void setZoneGroupId(Long zoneGroupId) {
		this.zoneGroupId = zoneGroupId;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	
}

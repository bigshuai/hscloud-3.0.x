package com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "hc_ip_detail")
public class IPDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private long id;	
	
	@Column(name = "ip", length = 11)
	private long ip;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "ip_range_id")	
	private IPRange ipRange;
	
	@Column(name = "status", length = 4)
	private int status;//状态标示（0：空闲；1：待分配；2：已分配；3：禁用；4：待释放）
	
	@Column(name = "object_id", length = 20)
	private long objectId=0L;
	
	@Column(name = "object_type", length = 4)
	private int objectType=0;//类型标示（0：VM；1：VRouter）
	
	@Column(name = "create_time")
	private Date createTime;	
	
	@Column(name = "create_uid", length = 20)	
	private long createUid=0L;
	
	@Column(name = "modify_time")
	private Date modifyTime;	
	
	@Column(name = "modify_uid", length = 20)	
	private long modifyUid=0L;
	
	@Column(name = "remark", length = 64)
	private String remark;
	
	@Column(name = "host_id", length = 20)
	private int hostId=0;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}	

	public long getIp() {
		return ip;
	}

	public void setIp(long ip) {
		this.ip = ip;
	}

	public IPRange getIpRange() {
		return ipRange;
	}

	public void setIpRange(IPRange ipRange) {
		this.ipRange = ipRange;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public int getObjectType() {
		return objectType;
	}

	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}	

	public long getCreateUid() {
		return createUid;
	}

	public void setCreateUid(long createUid) {
		this.createUid = createUid;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}		

	public long getModifyUid() {
		return modifyUid;
	}

	public void setModifyUid(long modifyUid) {
		this.modifyUid = modifyUid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public int getHostId() {
		return hostId;
	}

	public void setHostId(int hostId) {
		this.hostId = hostId;
	}

	@Override
	public String toString() {
		return "IPRange [id=" + id + ", ip=" + ip + ", ipRange=" + ipRange
				+ ", status=" + status + ", objectId=" + objectId + ", objectType=" + objectType
				+ ", createTime=" + createTime + ", createUid=" + createUid
				+ ", modifyTime=" + modifyTime + ", modifyUid=" + modifyUid
				+ ", remark=" + remark + ", hostId=" + hostId + "]";
	}
}

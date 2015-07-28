package com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.hisoft.hscloud.bss.sla.sc.entity.ServerZone;

@Entity
@Table(name = "hc_ip_range")
public class IPRange {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private long id;
	
	@Column(name = "start_ip", length = 11)
	private long startIP;//起始IP
	
	@Column(name = "end_ip", length = 11)
	private long endIP;//终止IP
	
	@Column(name = "create_time")
	private Date createTime;//创建时间	
	
	@Column(name = "create_uid", length = 20)	
	private long createUid=0L;//创建者
	
	@Column(name = "remark", length = 64)
	private String remark;//备注	
	
	//0:普通ip段, 1:外网ip段
	@Column(name = "type", length = 1)
	private int type = 0;
	
	@OneToMany(cascade=CascadeType.ALL,targetEntity=IPDetail.class,fetch = FetchType.LAZY, mappedBy = "ipRange")
    @JoinColumn(name="ip_range_id",referencedColumnName="id")
    private List<IPDetail> iPDetails = new ArrayList<IPDetail>();
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.LAZY)
	@JoinTable(name="hc_ip_zone",
	joinColumns=@JoinColumn(name="ip_id",referencedColumnName="id"),
	inverseJoinColumns=@JoinColumn(name="zone_id",referencedColumnName="id"))
	private ServerZone serverZone;//Zone信息
	private String gateway;//网关

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}	

	public long getStartIP() {
		return startIP;
	}

	public void setStartIP(long startIP) {
		this.startIP = startIP;
	}
	

	public long getEndIP() {
		return endIP;
	}

	public void setEndIP(long endIP) {
		this.endIP = endIP;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public List<IPDetail> getiPDetails() {
		return iPDetails;
	}

	public void setiPDetails(List<IPDetail> iPDetails) {
		this.iPDetails = iPDetails;
	}

	public ServerZone getServerZone() {
		return serverZone;
	}

	public void setServerZone(ServerZone serverZone) {
		this.serverZone = serverZone;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	@Override
	public String toString() {
		return "IPRange [id=" + id + ", startIP=" + startIP + ", endIP=" + endIP
				+ ", createTime=" + createTime + ", createUid=" + createUid
				+ ", remark=" + remark + ", gateway=" + gateway+ "]";
	}

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}

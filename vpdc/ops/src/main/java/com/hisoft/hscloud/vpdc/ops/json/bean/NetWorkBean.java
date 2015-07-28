package com.hisoft.hscloud.vpdc.ops.json.bean;
/**
 * @author hb ding
 * *@version  [版本号, 2014-01-17] 
 * @see  [相关类/方法] 
 */
public class NetWorkBean {
    private long id;
    private String cidr;
	private String dns1;
	private String gateway;
	private Long zoneGroup;
	private Long ipRangeId;
	private String networkId;
	private String useIPstart;
	private String useIPend;
	
	private int totalIPs;//ip总数
    private int usedIPs;//已使用ip数    
    private int assignedIPs;//已分配ip数
    private int freeIPs;
    
	public String getCidr() {
		return cidr;
	}
	public void setCidr(String cidr) {
		this.cidr = cidr;
	}
	public String getDns1() {
		return dns1;
	}
	public void setDns1(String dns1) {
		this.dns1 = dns1;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	public Long getZoneGroup() {
		return zoneGroup;
	}
	public void setZoneGroup(Long zoneGroup) {
		this.zoneGroup = zoneGroup;
	}
	public Long getIpRangeId() {
		return ipRangeId;
	}
	public void setIpRangeId(Long ipRangeId) {
		this.ipRangeId = ipRangeId;
	}
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getNetworkId() {
        return networkId;
    }
    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }
    public int getTotalIPs() {
        return totalIPs;
    }
    public void setTotalIPs(int totalIPs) {
        this.totalIPs = totalIPs;
    }
    public int getUsedIPs() {
        return usedIPs;
    }
    public void setUsedIPs(int usedIPs) {
        this.usedIPs = usedIPs;
    }
    public int getAssignedIPs() {
        return assignedIPs;
    }
    public void setAssignedIPs(int assignedIPs) {
        this.assignedIPs = assignedIPs;
    }
    public int getFreeIPs() {
        return freeIPs;
    }
    public void setFreeIPs(int freeIPs) {
        this.freeIPs = freeIPs;
    }
	public String getUseIPstart() {
		return useIPstart;
	}
	public void setUseIPstart(String useIPstart) {
		this.useIPstart = useIPstart;
	}
	public String getUseIPend() {
		return useIPend;
	}
	public void setUseIPend(String useIPend) {
		this.useIPend = useIPend;
	}
    
}

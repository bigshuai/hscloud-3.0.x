package com.hisoft.hscloud.vpdc.ops.vo;

import java.util.ArrayList;
import java.util.List;

public class VpdcVo {

	private long id;
	private String name;//VPDC名称
	private Integer vpdcType;//VPDC类型（0：非路由；1：路由） 
	private Long zoneGroup;//机房线路
	private String zoneGroupName;//机房线路名称
	private String zones;//机房下的所有zone(格式：BJ,SH,GZ)
	private Integer useLong;//时长
	private Long routerTemplateId;//router配置模板ID
	private int cpuCore;//router CPU核数
	private int diskCapacity;//router磁盘容量
	private int memSize;//router内存大小
	private int bandWidth;//router带宽
	private int osId;//router操作系统ID
	private String routerImage;//router的镜像
	private List<VlanNetwork> vlans = new ArrayList<VlanNetwork>();
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getVpdcType() {
		return vpdcType;
	}

	public void setVpdcType(Integer vpdcType) {
		this.vpdcType = vpdcType;
	}

	public Long getZoneGroup() {
		return zoneGroup;
	}

	public void setZoneGroup(Long zoneGroup) {
		this.zoneGroup = zoneGroup;
	}

	public String getZoneGroupName() {
		return zoneGroupName;
	}

	public void setZoneGroupName(String zoneGroupName) {
		this.zoneGroupName = zoneGroupName;
	}

	public Integer getUseLong() {
		return useLong;
	}
	public void setUseLong(Integer useLong) {
		this.useLong = useLong;
	}
	
	public String getZones() {
		return zones;
	}

	public void setZones(String zones) {
		this.zones = zones;
	}

	public Long getRouterTemplateId() {
		return routerTemplateId;
	}

	public void setRouterTemplateId(Long routerTemplateId) {
		this.routerTemplateId = routerTemplateId;
	}

	public int getBandWidth() {
		return bandWidth;
	}

	public void setBandWidth(int bandWidth) {
		this.bandWidth = bandWidth;
	}

	public List<VlanNetwork> getVlans() {
		return vlans;
	}

	public void setVlans(List<VlanNetwork> vlans) {
		this.vlans = vlans;
	}
	
	public String getRouterImage() {
		return routerImage;
	}

	public void setRouterImage(String routerImage) {
		this.routerImage = routerImage;
	}

	public int getOsId() {
		return osId;
	}

	public void setOsId(int osId) {
		this.osId = osId;
	}

	public int getCpuCore() {
		return cpuCore;
	}

	public void setCpuCore(int cpuCore) {
		this.cpuCore = cpuCore;
	}

	public int getDiskCapacity() {
		return diskCapacity;
	}

	public void setDiskCapacity(int diskCapacity) {
		this.diskCapacity = diskCapacity;
	}

	public int getMemSize() {
		return memSize;
	}

	public void setMemSize(int memSize) {
		this.memSize = memSize;
	}

	public class VlanNetwork{
		private String dns1;
		private String dns2;
		private int size;
		public String getDns1() {
			return dns1;
		}
		public void setDns1(String dns1) {
			this.dns1 = dns1;
		}
		public String getDns2() {
			return dns2;
		}
		public void setDns2(String dns2) {
			this.dns2 = dns2;
		}
		public int getSize() {
			return size;
		}
		public void setSize(int size) {
			this.size = size;
		}
	}
}
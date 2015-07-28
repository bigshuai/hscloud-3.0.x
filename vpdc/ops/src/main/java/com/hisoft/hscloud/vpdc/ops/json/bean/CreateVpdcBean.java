package com.hisoft.hscloud.vpdc.ops.json.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateVpdcBean {
	private Long id;
	private String name;//VPDC名称
	private Integer vpdcType;//VPDC类型（0：非路由；1：路由） 
	private Long zoneGroup;//机房线路
	private String zones;//机房下的所有zone(格式：BJ,SH,GZ)
	private int useLong;//时长
	private Date start_time;//路由的启用日期
	private Date end_time;//路由的到期日期
	private Long routerTemplateId;//router配置模板ID
	private int cpuCore;//router CPU核数
	private int diskCapacity;//router磁盘容量
	private int memSize;//router内存大小
	private int bandWidth;//router带宽
	private int osId;//router操作系统ID
	private String routerImage;//router的镜像
	private String order_item_id;//订单明细ID
	private List<VlanNetwork> vlans = new ArrayList<VlanNetwork>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public int getUseLong() {
		return useLong;
	}
	public void setUseLong(int useLong) {
		this.useLong = useLong;
	}
	
	public Date getStart_time() {
		return start_time;
	}

	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
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
	
	public String getOrder_item_id() {
		return order_item_id;
	}

	public void setOrder_item_id(String order_item_id) {
		this.order_item_id = order_item_id;
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

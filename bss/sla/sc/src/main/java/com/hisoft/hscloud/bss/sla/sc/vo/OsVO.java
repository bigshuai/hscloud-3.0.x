package com.hisoft.hscloud.bss.sla.sc.vo;

import java.sql.Blob;
import java.util.List;

import com.hisoft.hscloud.bss.sla.sc.entity.ZoneGroup;

public class OsVO {
	

	private String name;
	private int os_id;
	private List<ZoneGroup> zoneGroupList;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOs_id() {
		return os_id;
	}
	public void setOs_id(int os_id) {
		this.os_id = os_id;
	}
	public List<ZoneGroup> getZoneGroupList() {
		return zoneGroupList;
	}
	public void setZoneGroupList(List<ZoneGroup> zoneGroupList) {
		this.zoneGroupList = zoneGroupList;
	}
}

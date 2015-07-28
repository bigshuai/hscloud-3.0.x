/* 
 * 文 件 名:  ServiceItemVo.java 
 * 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
 * 描    述:  <描述> 
 * 修 改 人:  houyh 
 * 修改时间:  2012-12-4 
 * 跟踪单号:  <跟踪单号> 
 * 修改单号:  <修改单号> 
 * 修改内容:  <修改内容> 
 */
package com.hisoft.hscloud.bss.sla.sc.vo;

import java.math.BigDecimal;
import java.util.List;

import com.hisoft.hscloud.bss.sla.sc.entity.ZoneGroup;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author houyh
 * @version [1.4.1, 2012-12-4]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ServiceItemVo {
	private int id;
	private int serviceType;
	private String name;
	private String version;
	private String arch;
	private String language;
	private String family;
	private String vendor;
	private String imageId;
	private String platform;
	private String type;
	private String imageSize;
	private String description;
	private String port;
	private List<ZoneGroup> zoneGroupList;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getServiceType() {
		return serviceType;
	}

	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getArch() {
		return arch;
	}

	public void setArch(String arch) {
		this.arch = arch;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getImageSize() {
		return imageSize;
	}

	public void setImageSize(String imageSize) {
		this.imageSize = imageSize;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

	public List<ZoneGroup> getZoneGroupList() {
		return zoneGroupList;
	}

	public void setZoneGroupList(List<ZoneGroup> zoneGroupList) {
		this.zoneGroupList = zoneGroupList;
	}

	
}

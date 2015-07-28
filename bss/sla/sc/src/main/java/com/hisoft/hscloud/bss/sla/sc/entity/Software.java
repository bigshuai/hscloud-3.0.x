/**
 * @title Software.java
 * @package com.hisoft.hscloud.bss.sla.sc.entity
 * @description 用一句话描述该文件做什么
 * @author jiaquan.hu
 * @update 2012-5-4 上午10:29:05
 * @version V1.0
 */
package com.hisoft.hscloud.bss.sla.sc.entity;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author jiaquan.hu
 * @update 2012-5-4 上午10:29:05
 */
@Entity
@Table(name = "hc_software")
@PrimaryKeyJoinColumn(name="software_id",referencedColumnName="item_id")
public class Software extends ServiceItem {

	private String version;
	
	private String arch;
	
	private String language;
	
	private String platform;
		
	private String type;
	
	private String vendor;
	
	private Blob icon;
	
	private String imageType;
	
	@Column(name="image_type")
	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	@JsonIgnore
	public Blob getIcon() {
		return icon;
	}

	public void setIcon(Blob icon) {
		this.icon = icon;
	}

	@Column(length=50)
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Column(length=50)
	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	@Column(length=20)
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Column(length=5)
	public String getArch() {
		return arch;
	}

	public void setArch(String arch) {
		this.arch = arch;
	}

	@Column(length=50)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(length=50)
	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

}

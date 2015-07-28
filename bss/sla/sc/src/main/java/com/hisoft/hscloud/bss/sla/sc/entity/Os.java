package com.hisoft.hscloud.bss.sla.sc.entity;

import java.sql.Blob;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.hisoft.hscloud.crm.usermanager.entity.Domain;

/**
 * @description os的实体类
 * @version 1.0
 * @author jiaquan.hu
 * @update 2012-3-31 上午9:58:52
 */
@Entity
@Table(name = "hc_os")
@PrimaryKeyJoinColumn(name = "os_id", referencedColumnName = "item_id")
public class Os extends ServiceItem {
	private String version;
	
	private String arch;
	
	private String language;
	
	private String family;
	
	private String vendor;
	
	private String imageId;
	
	private String imageSize;
	
	//操作系统类型 0-为普通操作系统 1-vrouter操作系统
	private int osType;
	
	@Column(name="port")
	private String port;

	private Blob icon;
	
	private String imageType;
	
	
	private List<ZoneGroup> zoneGroupList;
	
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
	public String getArch() {
		return arch;
	}

	public void setArch(String arch) {
		this.arch = arch;
	}

	@Column(length=50)
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Column(length=50)
	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	@Column(length=50)
	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	@Column(name = "image_id", length = 50)
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

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
    
    @Column(name="os_type")
    public int getOsType() {
        return osType;
    }

    public void setOsType(int osType) {
        this.osType = osType;
    }
    
    @ManyToMany( cascade = {CascadeType.ALL})
	@JoinTable(name="hc_os_zonegroup",
	joinColumns=@JoinColumn(name="os_id",referencedColumnName="os_id"),
	inverseJoinColumns=@JoinColumn(name="zoneGroup_id",referencedColumnName="id"))
	public List<ZoneGroup> getZoneGroupList() {
		return zoneGroupList;
	}

	public void setZoneGroupList(List<ZoneGroup> zoneGroupList) {
		this.zoneGroupList = zoneGroupList;
	}
}

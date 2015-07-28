package com.hisoft.hscloud.crm.usermanager.entity; 

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hc_resource_type")
public class ResourceType {
	
	// 主健
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(nullable = false)
	private String name;
	
	@Column(name = "resource_table", nullable = false)
	private String resourceTable;
	
	@Column(name = "resource_condition", nullable = false)
	private String resourceCondition;
	
	@Column(name = "relation_table", nullable = false)
	private String relationTable;
	
	@Column(name = "relation_condition", nullable = false)
	private String relationConditon;
	
	@Column(name = "resource_type", nullable = false)
	private String resourceType;
	
	@Column(name = "icon", nullable = false)
	private String icon;
	
	//0表示其他，1是前台资源类型，2是后台资源类型
	@Column(name = "status", nullable = false)
	private int status;

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

	public String getResourceTable() {
		return resourceTable;
	}

	public void setResourceTable(String resourceTable) {
		this.resourceTable = resourceTable;
	}

	public String getResourceCondition() {
		return resourceCondition;
	}

	public void setResourceCondition(String resourceCondition) {
		this.resourceCondition = resourceCondition;
	}

	public String getRelationTable() {
		return relationTable;
	}

	public void setRelationTable(String relationTable) {
		this.relationTable = relationTable;
	}

	public String getRelationConditon() {
		return relationConditon;
	}

	public void setRelationConditon(String relationConditon) {
		this.relationConditon = relationConditon;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}

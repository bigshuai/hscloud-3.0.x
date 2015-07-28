/**
* @title ServiceType.java
* @package com.hisoft.hscloud.bss.sla.sc.entity
* @description 用一句话描述该文件做什么
* @author jiaquan.hu
* @update 2012-5-7 下午4:59:38
* @version V1.0
*/
package com.hisoft.hscloud.bss.sla.sc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.transaction.annotation.Transactional;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author jiaquan.hu
 * @update 2012-5-7 下午4:59:38
 */
@Entity
@Table(name="hc_service_type")
public class ServiceType {
	private int id;
	
	private String serviceName;
	
	private String className;
	@Id
	@Column
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(length=50)
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Column(length=100)
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}

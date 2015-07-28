/* 
* 文 件 名:  VpdcReference_Os.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  dinghb 
* 修改时间:  2012-12-13 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.entity; 

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  dinghb 
 * @version  [版本号, 2012-12-13] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Entity
@Table(name = "hc_vpdcReference_os")
public class VpdcReference_Os {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;
	
	@Column(name = "referenceId")
	private long vpdcReferenceId;

	@Column(name = "osId")
	private long osId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getVpdcReferenceId() {
		return vpdcReferenceId;
	}

	public void setVpdcReferenceId(long vpdcReferenceId) {
		this.vpdcReferenceId = vpdcReferenceId;
	}

	public long getOsId() {
		return osId;
	}

	public void setOsId(long osId) {
		this.osId = osId;
	}

}

/**
 * @title FlavorVO.java
 * @package com.hisoft.hscloud.vpdc.ops.vo
 * @description 用一句话描述该文件做什么
 * @author hongqin.li
 * @update 2012-5-10 下午5:31:45
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.ops.vo;

import org.openstack.model.compute.nova.NovaFlavor;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author hongqin.li
 * @update 2012-5-10 下午5:31:45
 */
public class FlavorVO extends NovaFlavor{
	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = 7108772633315561216L;
	private String vcpusType;//cpu型号
	private String extDisk;//扩展盘，以英文逗号","分割的字符串
	private String bandwidth;//带宽
	private String ip;//IP
	public String getVcpusType() {
		return vcpusType;
	}
	public void setVcpusType(String vcpusType) {
		this.vcpusType = vcpusType;
	}
	public String getExtDisk() {
		return extDisk;
	}
	public void setExtDisk(String extDisk) {
		this.extDisk = extDisk;
	}
	public String getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

}

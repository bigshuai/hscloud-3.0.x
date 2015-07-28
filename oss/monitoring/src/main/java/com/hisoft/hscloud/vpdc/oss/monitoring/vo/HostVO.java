/* 
* 文 件 名:  HostVO.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-11-8 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.oss.monitoring.vo; 

import java.io.Serializable;

/** 
 * <节点基本信息> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-11-8] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class HostVO implements Serializable {

	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = -5444119410991063278L;
	public long getHostId() {
		return hostId;
	}
	public void setHostId(long hostId) {
		this.hostId = hostId;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getNodeAliases() {
		return nodeAliases;
	}
	public void setNodeAliases(String nodeAliases) {
		this.nodeAliases = nodeAliases;
	}
	public String getIpOuter() {
		return ipOuter;
	}
	public void setIpOuter(String ipOuter) {
		this.ipOuter = ipOuter;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	public String getZoneCode() {
		return zoneCode;
	}
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	private long hostId;
	private String hostName;
	private String nodeAliases;//node 别名
	private String ipOuter;
	private String zoneName;
	private String zoneCode;
}

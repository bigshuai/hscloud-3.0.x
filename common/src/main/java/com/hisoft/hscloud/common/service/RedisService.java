/* 
* 文 件 名:  RedisService.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  dinghb 
* 修改时间:  2013-4-19 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.common.service; 

import java.util.List;
import java.util.Map;
import org.openstack.model.hscloud.impl.HostAcquisition;
import org.openstack.model.hscloud.impl.ServerAcquisition;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  dinghb 
 * @version  [版本号, 2013-4-19] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface RedisService {
	/**
	 * <更新VM信息> 
	* <功能详细描述> 
	* @param vms 
	* @see [类、类#方法、类#成员]
	 */
	public void setVM(List<Object> vms);
	/**
	 * <获取VM信息> 
	* <功能详细描述> 
	* @param key
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public Object getVM(String key);
	/**
	 * <更新节点信息数据集> 
	* <功能详细描述> 
	* @param lhost 
	* @see [类、类#方法、类#成员]
	 */
	public void setHostAcquisition (List<HostAcquisition> lhost);
	/**
	 * <获取节点信息数据集> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<HostAcquisition> getHostAcquisition();
	/**
	 * <获取虚拟机信息数据集> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<ServerAcquisition> getServerAcquisition();
	/**
	 * <获取到的虚拟机信息数据集按节点进行统计处理> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public Map<String,List<ServerAcquisition>> getHostInfoMap();
}

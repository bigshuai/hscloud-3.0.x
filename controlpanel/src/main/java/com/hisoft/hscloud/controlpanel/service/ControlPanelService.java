/* 
* 文 件 名:  ControlPanelService.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  dinghb 
* 修改时间:  2013-1-4 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.controlpanel.service; 

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.controlpanel.entity.ControlPanelUser;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  dinghb 
 * @version  [版本号, 2013-1-4] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface ControlPanelService {
	/**
	 * <创建控制面板用户> 
	* <功能详细描述> 
	* @param ip
	* @param userId
	* @param instanceId
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void createControlUser(String ip,String pwd,long userId,String vmId)throws HsCloudException;
	/**
	 * <根据instance删除控制面板登录信息> 
	* <功能详细描述> 
	* @param instanceId 
	* @see [类、类#方法、类#成员]
	 */
	public void deleCPByVmId(String vmId)throws HsCloudException;
	public ControlPanelUser loginByVMid(String vmId,String password);
	public ControlPanelUser loginByIp(String ip, String password);
	public void saveControlPanelUser(ControlPanelUser controlPanelUser);
	
	/**
	 * 通过id查询ControlUser
	 * @param id
	 * @return
	 */
	public ControlPanelUser findControlUserByID(long id);
	/**
	 * <根据虚拟机id获取控制面板登录信息> 
	* <功能详细描述> 
	* @param vmid
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public ControlPanelUser findControlUserByVmID(String vmid);
}

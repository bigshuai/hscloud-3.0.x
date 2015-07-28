/* 
* 文 件 名:  ControlPanelDao.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  dinghb 
* 修改时间:  2013-1-4 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.controlpanel.dao; 

import java.util.List;
import java.util.Map;

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
public interface ControlPanelDao {
	/**
	 * <保存控制面板用户> 
	* <功能详细描述> 
	* @param cuser 
	* @see [类、类#方法、类#成员]
	 */
	public void saveControlUser(ControlPanelUser cuser)throws HsCloudException;
	/**
	 * <根据vmId获取控制面板登录对象> 
	* <功能详细描述> 
	* @param vmId
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public ControlPanelUser getCPByVmId(String vmId)throws HsCloudException;
	
	public ControlPanelUser getRecycleCPByVmId(String vmId)throws HsCloudException;
	
	public List<ControlPanelUser> findBySQL(String sql,Map<String,?> map);
	
	public ControlPanelUser findUniqueBySQL(String sql,Map<String,?> map);
	
	public ControlPanelUser findUniqueBy(String propertyName, Object value);
}

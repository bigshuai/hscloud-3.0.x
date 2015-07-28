/* 
* 文 件 名:  IPMIConfigDao.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-3-19 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.sc.dao; 

import com.hisoft.hscloud.bss.sla.sc.entity.IPMIConfig;
import com.hisoft.hscloud.bss.sla.sc.entity.ServerZone;
import com.hisoft.hscloud.common.util.HsCloudException;

/** 
 * <IPMI配置信息持久层> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-3-19] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface IPMIConfigDao {

	/**
	 * <根据ID查询IPMI配置信息> 
	* <功能详细描述> 
	* @param id
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public IPMIConfig getIPMIConfigById(long id) throws HsCloudException;
}

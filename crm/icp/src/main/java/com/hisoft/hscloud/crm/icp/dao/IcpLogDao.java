/* 
* 文 件 名:  IcpLogDao.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-1-9 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.crm.icp.dao; 

import com.hisoft.hscloud.crm.icp.entity.IcpLog;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-1-9] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface IcpLogDao {
    public long saveIcpLog(IcpLog icpLog);
}

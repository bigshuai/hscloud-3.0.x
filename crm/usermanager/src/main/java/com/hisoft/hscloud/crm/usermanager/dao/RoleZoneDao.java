/* 
* 文 件 名:  RoleZoneDao.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-10-21 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.crm.usermanager.dao; 

import java.util.List;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-10-21] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface RoleZoneDao {
	public boolean addZoneOfRole(long roleId, List<Object> zoneIds);
	public boolean deleteZoneOfRole(long roleId, List<Object> zoneIds);
	public boolean hasZoneOfRolePermission(long roleId, long zoneId);
	public List<Object> getZoneIdsByAdminId(long adminId);
	public boolean deleteZoneOfRole(long zoneId);
}

/* 
* 文 件 名:  ZoneGroupDao.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-6-17 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.sc.dao; 

import java.util.List;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.sc.entity.ZoneGroup;
import com.hisoft.hscloud.bss.sla.sc.vo.ZoneGroupVO;
import com.hisoft.hscloud.common.util.HsCloudException;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-6-17] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface ZoneGroupDao {
	/**
	 * <添加ZoneGroup> 
	* <功能详细描述> 
	* @param zoneGroup
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public long createZoneGroup(ZoneGroup zoneGroup) throws HsCloudException;
	/**
	 * <删除ZoneGroup> 
	* <功能详细描述> 
	* @param id
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean deleteZoneGroup(long id) throws HsCloudException;
	/**
	 * <修改ZoneGroup> 
	* <功能详细描述> 
	* @param zoneGroup
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean updateZoneGroup(ZoneGroup zoneGroup) throws HsCloudException;
	/**
	 * <根据Id查询ZoneGroup> 
	* <功能详细描述> 
	* @param id
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public ZoneGroup getZoneGroupById(long id) throws HsCloudException;
	/**
	 * <分页查询ZoneGroup> 
	* <功能详细描述> 
	* @param page
	* @param field
	* @param fieldValue
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<ZoneGroup> findZoneGroup(Page<ZoneGroup> page,String field, String fieldValue) throws HsCloudException;
	/**
	 * <查询所有的ZoneGroup> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ZoneGroup> getAllZoneGroups() throws HsCloudException;
	/**
	 * <根据条件查询zoneGroup> 
	* <功能详细描述> 
	* @param field
	* @param fieldValue
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ZoneGroup> getZoneGroupsByCondition(String field,Object fieldValue) throws HsCloudException;
	/**
	 * <根据资源域Id查询资源域组Id列表> 
	* <功能详细描述> 
	* @param zoneIds
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<Long> getAllZoneGroupIdsByZoneIds(List<Long> zoneIds) throws HsCloudException;
	/**
	 * <查询所有可按套餐购买的机房线路，将套餐信息作为关联条件> 
	* <功能详细描述> 
	* @param brandCode
	* @param domainId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ZoneGroupVO> getAllZoneGroupByUser(String brandCode,Long domainId) throws HsCloudException;
	/**
	 * <查询所有的可按需购买的机房线路> 
	* <功能详细描述> 
	* @param brandCode
	* @param domainId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ZoneGroupVO> getCustomZoneGroupByUser(String brandCode,Long domainId) throws HsCloudException;
}

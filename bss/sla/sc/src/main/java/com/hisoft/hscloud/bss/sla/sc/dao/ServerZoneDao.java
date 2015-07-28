/* 
* 文 件 名:  ServerZoneDao.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-3-11 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.sc.dao; 

import java.util.List;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.sc.entity.Os;
import com.hisoft.hscloud.bss.sla.sc.entity.ServerZone;
import com.hisoft.hscloud.bss.sla.sc.vo.OsVO;
import com.hisoft.hscloud.bss.sla.sc.vo.ZoneVO;
import com.hisoft.hscloud.common.util.HsCloudException;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-3-11] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface ServerZoneDao {

	/**
	 * <添加Zone> 
	* <功能详细描述> 
	* @param serverZone
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public long createServerZone(ServerZone serverZone) throws HsCloudException;
	/**
	 * <删除Zone> 
	* <功能详细描述> 
	* @param id
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean deleteServerZone(long id) throws HsCloudException;
	/**
	 * <修改Zone> 
	* <功能详细描述> 
	* @param serverZone
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean updateServerZone(ServerZone serverZone) throws HsCloudException;
	/**
	 * <根据Id查询Zone> 
	* <功能详细描述> 
	* @param id
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public ServerZone getServerZoneById(long id) throws HsCloudException;
	/**
	 * <分页查询Zone> 
	* <功能详细描述> 
	* @param page
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<ServerZone> findServerZone(Page<ServerZone> page,String field, Object fieldValue) throws HsCloudException;
	/**
	 * <查询所有的Zone> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ServerZone> getAllZones() throws HsCloudException;
	/**
	 * <批量更新Zone> 
	* <功能详细描述> 
	* @param isDefault
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean batchUpdateServerZone(int isDefault) throws HsCloudException;
	/**
	 * <根据条件查询zone> 
	* <功能详细描述> 
	* @param field
	* @param fieldValue
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ServerZone> getServerZonesByCondition(String field,Object fieldValue) throws HsCloudException;
	/**
	 * <获取默认的Zone> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public ServerZone getDefaultServerZone() throws HsCloudException;
	/**
	 * <根据code查询Zone> 
	* <功能详细描述> 
	* @param zoneCode
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public ServerZone getServerZoneByCode(String zoneCode) throws HsCloudException;
	/**
	 * <根据资源域Id查询资源域列表> 
	* <功能详细描述> 
	* @param zoneIds
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ServerZone> getAllZones(List<Object> zoneIds) throws HsCloudException;
	/**
	 * <根据资源域组Id查询资源域列表> 
	* <功能详细描述> 
	* @param zoneGroupId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ServerZone> getAllZonesByGroupId(long zoneGroupId) throws HsCloudException;
	/**
	 * <查询已经关联的资源域> 
	* <功能详细描述> 
	* @param page
	* @param zoneGroupId
	* @param zoneName
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<ZoneVO> getRelatedServerZone(Page<ZoneVO> page,long zoneGroupId, String zoneName) throws HsCloudException;
	/**
	 * <查询未关联的资源域> 
	* <功能详细描述> 
	* @param page
	* @param zoneGroupId
	* @param zoneName
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<ZoneVO> getUnRelatedServerZone(Page<ZoneVO> page,long zoneGroupId, String zoneName) throws HsCloudException;
	/**
	 * <根据资源域code查询资源域列表> 
	* <功能详细描述> 
	* @param codes
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ServerZone> getAllZones(String codes) throws HsCloudException;
	/**
	 * 管理员根据权限获取对应的zone
	 * @param adminId
	 * @return
	 * @throws HsCloudException
	 */
	public List<ZoneVO> getZoneByAdmin(long adminId,boolean needFilter)throws HsCloudException;
	/**
	 * <根据资源域组Id查询(支持/不支持)按需的资源域列表> 
	* <功能详细描述> 
	* @param zoneGroupId
	* @param isCustom
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ServerZone> getAllZonesByCondition(long zoneGroupId,String isCustom) throws HsCloudException;
	/**
	 * <根据资源域组code查询(支持/不支持)按需的资源域列表> 
	* <功能详细描述> 
	* @param zoneGroupCode
	* @param isCustom
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ServerZone> getAllZonesByCondition(String zoneGroupCode,String isCustom) throws HsCloudException;
	/**
	 * <查询未关联的OS>
	 * @param page
	 * @param zoneGroupId
	 * @param zoneName
	 * @return
	 * @throws HsCloudException
	 */
	public Page<OsVO> getUnRelatedServerOs(Page<OsVO> page,long zoneGroupId, String OsName) throws HsCloudException;
	/**
	 * <查询已关联的OS>
	 * @param page
	 * @param zoneGroupId
	 * @param zoneName
	 * @return
	 * @throws HsCloudException
	 */
	public Page<OsVO> getRelatedServerOs(Page<OsVO> page,long zoneGroupId, String OsName) throws HsCloudException;
}

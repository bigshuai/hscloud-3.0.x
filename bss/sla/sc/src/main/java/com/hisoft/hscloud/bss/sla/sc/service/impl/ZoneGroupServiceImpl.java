/* 
* 文 件 名:  ZoneGroupServiceImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-6-17 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.sc.service.impl; 

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.sc.dao.ZoneGroupDao;
import com.hisoft.hscloud.bss.sla.sc.entity.ZoneGroup;
import com.hisoft.hscloud.bss.sla.sc.service.ZoneGroupService;
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
@Service
public class ZoneGroupServiceImpl implements ZoneGroupService {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private ZoneGroupDao zoneGroupDao;

	@Override
	public long createZoneGroup(ZoneGroup zoneGroup) throws HsCloudException {
		return zoneGroupDao.createZoneGroup(zoneGroup);
	}

	@Override
	public boolean deleteZoneGroup(long id) throws HsCloudException {
		return zoneGroupDao.deleteZoneGroup(id);
	}

	@Override
	public boolean updateZoneGroup(ZoneGroup zoneGroup) throws HsCloudException {
		return zoneGroupDao.updateZoneGroup(zoneGroup);
	}

	@Override
	public Page<ZoneGroup> findZoneGroup(Page<ZoneGroup> page, String field,
			String fieldValue) throws HsCloudException {
		return zoneGroupDao.findZoneGroup(page, field, fieldValue);
	}

	@Override
	public List<ZoneGroup> getAllZoneGroups() throws HsCloudException {
		return zoneGroupDao.getAllZoneGroups();
	}

	@Override
	public boolean hasSameZoneGroupName(ZoneGroup zoneGroup)
			throws HsCloudException {
		boolean result = false;
		try{
			List<ZoneGroup> listZoneGroups = zoneGroupDao.getZoneGroupsByCondition("name", zoneGroup.getName());
			if(listZoneGroups!=null && listZoneGroups.size()>0){
				result = true;
			}
		}catch (Exception e) {
			logger.error("hasSameZoneGroupName Exception:", e);
		}		
		return result;
	}

	@Override
	public boolean hasSameZoneGroupCode(ZoneGroup zoneGroup)
			throws HsCloudException {
		boolean result = false;
		try{
			List<ZoneGroup> listZoneGroups = zoneGroupDao.getZoneGroupsByCondition("code", zoneGroup.getCode());
			if(listZoneGroups!=null && listZoneGroups.size()>0){
				result = true;
			}
		}catch (Exception e) {
			logger.error("hasSameZoneGroupCode Exception:", e);
		}		
		return result;
	}

	@Override
	public ZoneGroup getZoneGroupById(long id) throws HsCloudException {
		return zoneGroupDao.getZoneGroupById(id);
	}

	@Override
	public List<Long> getAllZoneGroupIdsByZoneIds(List<Long> zoneIds)
			throws HsCloudException {
		return zoneGroupDao.getAllZoneGroupIdsByZoneIds(zoneIds);
	}

	@Override
	public List<ZoneGroupVO> getAllZoneGroupByUser(String brandCode,Long domainId)
			throws HsCloudException {
		return zoneGroupDao.getAllZoneGroupByUser(brandCode,domainId);
	}

	@Override
	public List<ZoneGroupVO> getCustomZoneGroupByUser(String brandCode,
			Long domainId) throws HsCloudException {
		return zoneGroupDao.getCustomZoneGroupByUser(brandCode, domainId);
	}
	

}

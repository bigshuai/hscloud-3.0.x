/* 
* 文 件 名:  ServerZoneServiceImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-3-11 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.sc.service.impl; 

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.sc.dao.ServerZoneDao;
import com.hisoft.hscloud.bss.sla.sc.entity.Os;
import com.hisoft.hscloud.bss.sla.sc.entity.ServerZone;
import com.hisoft.hscloud.bss.sla.sc.jsonBean.ZoneAcquisitionBean;
import com.hisoft.hscloud.bss.sla.sc.service.IServerZoneService;
import com.hisoft.hscloud.bss.sla.sc.vo.OsVO;
import com.hisoft.hscloud.bss.sla.sc.vo.ZoneVO;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.RedisUtil;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-3-11] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class ServerZoneServiceImpl implements IServerZoneService {

	@Autowired
	private ServerZoneDao serverZoneDao;
	private Logger logger = Logger.getLogger(this.getClass());
	/** 
	 * @param zone
	 * @return
	 * @throws HsCloudException 
	 */
	@Override
	public long createServerZone(ServerZone zone) throws HsCloudException {			
		return serverZoneDao.createServerZone(zone);
	}

	/** 
	 * @param id
	 * @return
	 * @throws HsCloudException 
	 */
	@Override
	public boolean deleteServerZone(long id) throws HsCloudException {
		return serverZoneDao.deleteServerZone(id);
	}

	/** 
	 * @param serverZone
	 * @return
	 * @throws HsCloudException 
	 */
	@Override
	public boolean updateServerZone(ServerZone serverZone)
			throws HsCloudException {
		return serverZoneDao.updateServerZone(serverZone);
	}

	/** 
	 * @param id
	 * @return
	 * @throws HsCloudException 
	 */
	@Override
	public ServerZone getServerZoneById(long id) throws HsCloudException {
		return serverZoneDao.getServerZoneById(id);
	}

	/** 
	 * @param page
	 * @return
	 * @throws HsCloudException 
	 */
	@Override
	public Page<ServerZone> findServerZone(Page<ServerZone> page,String field, Object fieldValue)
			throws HsCloudException {
		return serverZoneDao.findServerZone(page,field,fieldValue);
	}

	@Override
	public List<ServerZone> getAllZones() throws HsCloudException {
		return serverZoneDao.getAllZones();
	}

	@Override
	public boolean setDefaultServerZone(ServerZone serverZone)
			throws HsCloudException {
		boolean result = false;
		try{
			serverZoneDao.batchUpdateServerZone(0);
			serverZone=this.getServerZoneById(serverZone.getId());
			serverZone.setIsDefault(1);
			serverZoneDao.updateServerZone(serverZone);
			result = true;
		}catch (Exception e) {
			logger.error("setDefaultServerZone Exception:", e);
		}		
		return result;
	}

	@Override
	public boolean hasSameZoneName(ServerZone serverZone)
			throws HsCloudException {
		boolean result = false;
		try{
			List<ServerZone> listZone = serverZoneDao.getServerZonesByCondition("name", serverZone.getName());
			if(listZone!=null && listZone.size()>0){
				result = true;
			}
		}catch (Exception e) {
			logger.error("getServerZonesByCondition Exception:", e);
		}		
		return result;
	}

	@Override
	public boolean hasSameZoneCode(ServerZone serverZone)
			throws HsCloudException {
		boolean result = false;
		try{
			List<ServerZone> listZone = serverZoneDao.getServerZonesByCondition("code", serverZone.getCode());
			if(listZone!=null && listZone.size()>0){
				result = true;
			}
		}catch (Exception e) {
			logger.error("getServerZonesByCondition Exception:", e);
		}
		return result;
	}

	@Override
	public List<ServerZone> getServerZonesByCondition(String field,
			Object fieldValue) throws HsCloudException {
		return serverZoneDao.getServerZonesByCondition(field, fieldValue);
	}

	@Override
	public ServerZone getDefaultServerZone() throws HsCloudException {
		return serverZoneDao.getDefaultServerZone();
	}

	@Override
	public ServerZone getServerZoneByCode(String zoneCode)
			throws HsCloudException {
		return serverZoneDao.getServerZoneByCode(zoneCode);
	}

	@Override
	public List<ServerZone> getAllZones(List<Object> zoneIds)
			throws HsCloudException {
		return serverZoneDao.getAllZones(zoneIds);
	}

	@Override
	public List<ServerZone> getAllZonesByGroupId(long zoneGroupId)
			throws HsCloudException {
		return serverZoneDao.getAllZonesByGroupId(zoneGroupId);
	}

	@Override
	public ServerZone getRightServerZoneOfGroup(long zoneGroupId) throws HsCloudException {
		ServerZone serverZone = null;
		int theoreticalValue = 0;//预计还能创建的虚拟机数
		int theoreticalMaxValue = 0;//预计还能创建的最多虚拟机数
		try{
			List<ServerZone> zonelist = serverZoneDao.getAllZonesByGroupId(zoneGroupId);
			for(ServerZone zone : zonelist){
				String jsonStr = RedisUtil.getValue("ZoneAcquisition_"+ zone.getCode());
				if(StringUtils.isNotBlank(jsonStr)){
					JSONObject jsonObject = JSONObject.fromObject(jsonStr);	
					ZoneAcquisitionBean zoneAcquisitionBean=(ZoneAcquisitionBean) JSONObject.toBean(jsonObject, ZoneAcquisitionBean.class);
					//预计还能创建的虚拟机数
        	        theoreticalValue = (zoneAcquisitionBean.getTheoreticalValue()!=null?zoneAcquisitionBean.getTheoreticalValue():0);
				}
				if(theoreticalValue>=theoreticalMaxValue){
					theoreticalMaxValue = theoreticalValue;
					serverZone = zone;
				}
			}
		}catch (Exception ex) {
			logger.error("redis getZoneOverviewInfo Exception:",ex);
		}
		return serverZone;
	}

	@Override
	public ServerZone getRightServerZoneOfGroup(String codes)
			throws HsCloudException {
		ServerZone serverZone = null;
		int theoreticalValue = 0;//预计还能创建的虚拟机数
		int theoreticalMaxValue = 0;//预计还能创建的最多虚拟机数
		try{
			List<ServerZone> zonelist = serverZoneDao.getAllZones(codes);
			for(ServerZone zone : zonelist){
				String jsonStr = RedisUtil.getValue("ZoneAcquisition_"+ zone.getCode());
				if(StringUtils.isNotBlank(jsonStr)){
					JSONObject jsonObject = JSONObject.fromObject(jsonStr);	
					ZoneAcquisitionBean zoneAcquisitionBean=(ZoneAcquisitionBean) JSONObject.toBean(jsonObject, ZoneAcquisitionBean.class);
					//预计还能创建的虚拟机数
        	        theoreticalValue = (zoneAcquisitionBean.getTheoreticalValue()!=null?zoneAcquisitionBean.getTheoreticalValue():0);
				}
				if(theoreticalValue>=theoreticalMaxValue){
					theoreticalMaxValue = theoreticalValue;
					serverZone = zone;
				}
			}
		}catch (Exception ex) {
			logger.error("redis getZoneOverviewInfo Exception:",ex);
		}
		return serverZone;
	}

	@Override
	public Page<ZoneVO> getRelatedServerZone(Page<ZoneVO> page,
			long zoneGroupId, String zoneName) throws HsCloudException {
		return serverZoneDao.getRelatedServerZone(page, zoneGroupId, zoneName);
	}

	@Override
	public Page<ZoneVO> getUnRelatedServerZone(Page<ZoneVO> page,
			long zoneGroupId, String zoneName) throws HsCloudException {
		return serverZoneDao.getUnRelatedServerZone(page, zoneGroupId, zoneName);
	}

	@Override
	public List<ZoneVO> getZoneByAdmin(long adminId, boolean needFilter)
			throws HsCloudException {
		return serverZoneDao.getZoneByAdmin(adminId, needFilter);
	}

	@Override
	public List<ServerZone> getCustomZonesByGroupId(long zoneGroupId)
			throws HsCloudException {
		return serverZoneDao.getAllZonesByCondition(zoneGroupId, "1");
	}

	@Override
	public List<ServerZone> getCustomZonesByGroupCode(String zoneGroupCode)
			throws HsCloudException {
		return serverZoneDao.getAllZonesByCondition(zoneGroupCode, "1");
	}

	@Override
	public Page<OsVO> getUnRelatedServerOs(Page<OsVO> page, long zoneGroupId,
			String OsName) throws HsCloudException {
		return serverZoneDao.getUnRelatedServerOs(page, zoneGroupId, OsName);
	}

	@Override
	public Page<OsVO> getRelatedServerOs(Page<OsVO> page, long zoneGroupId,
			String OsName) throws HsCloudException {
		return serverZoneDao.getRelatedServerOs(page, zoneGroupId, OsName);
	}
	
}

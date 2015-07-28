/* 
* 文 件 名:  ServerZoneDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-3-11 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.sc.dao.impl; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.bss.sla.sc.dao.ServerZoneDao;
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
@Repository
public class ServerZoneDaoImpl extends HibernateDao<ServerZone, Long> implements ServerZoneDao {

	private Logger logger = Logger.getLogger(this.getClass());
	@Override
	public long createServerZone(ServerZone serverZone) throws HsCloudException {
		long id = 0L;
		try{
			this.save(serverZone);
			id = serverZone.getId();
		}catch (Exception e) {
			logger.error("createServerZone Exception:", e);
		}		
		return id;
	}

	@Override
	public boolean deleteServerZone(long id) throws HsCloudException {
		boolean result = false;
		try{
			ServerZone serverZone=this.getServerZoneById(id);
			if(serverZone != null){
				this.delete(id);
				result = true;
			}
		}catch (Exception e) {
			logger.error("deleteServerZone Exception:", e);
		}		
		return result;
	}

	@Override
	public boolean updateServerZone(ServerZone serverZone)
			throws HsCloudException {
		boolean result = false;
		try{
			//ServerZone zone = this.getServerZoneById(serverZone.getId());
			//if(zone != null){
				this.save(serverZone);
				result = true;
			//}			
		}catch (Exception e) {
			logger.error("updateServerZone Exception:", e);
		}		
		return result;
	}

	@Override
	public ServerZone getServerZoneById(long id) throws HsCloudException {		
		return this.findUniqueBy("id", id);
	}

	@Override
	public Page<ServerZone> findServerZone(Page<ServerZone> page,String field, Object fieldValue)
			throws HsCloudException {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("from ServerZone as sz where 2>1 ");
		if(field != null && !"".equals(field)){
			hql.append(" and sz.").append(field).append(" =:fieldValue ");			
			map.put("fieldValue", fieldValue);
		}else if(fieldValue != null && !"".equals(fieldValue)){
			hql.append(" and sz.code like :code or sz.name like :name ");			
			map.put("code", "%"+fieldValue+"%");
			map.put("name", "%"+fieldValue+"%");
		}
		hql.append(" order by id desc");
		page = this.findPage(page, hql.toString(), map);
		return page;
	}

	@Override
	public List<ServerZone> getAllZones() throws HsCloudException {
		StringBuffer hql = new StringBuffer("from ServerZone as sz where sz.isEnable=1 order by sz.isDefault desc");
		return this.find(hql.toString());
	}

	@Override
	public boolean batchUpdateServerZone(int isDefault) throws HsCloudException {
		boolean result = false;
		StringBuffer hql = new StringBuffer("update ServerZone  sz set sz.isDefault =:isDefault where 2>1");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("isDefault", isDefault);
		try{
			this.batchExecute(hql.toString(), map);
			result = true;
		}catch (Exception e) {
			logger.error("updateServerZone Exception:", e);
		}		
		return result;
	}

	@Override
	public List<ServerZone> getServerZonesByCondition(String field,
			Object fieldValue) throws HsCloudException {
		List<ServerZone> listZone = null;
		StringBuffer hql = new StringBuffer("from ServerZone as sz where 2>1 ");
		try{
			if(StringUtils.isNotEmpty(field.trim()) && fieldValue != null){			
				hql.append(" and sz.").append(field).append(" =:fieldValue");
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("fieldValue", fieldValue);
				listZone = this.find(hql.toString(), map);
			}
		}catch (Exception e) {
			logger.error("getServerZonesByCondition Exception:", e);
		}				
		return listZone;
	}

	@Override
	public ServerZone getDefaultServerZone() throws HsCloudException {
		StringBuffer hql = new StringBuffer("from ServerZone as sz where sz.isEnable=1 and sz.isDefault=1");		
		return this.findUnique(hql.toString());
	}

	@Override
	public ServerZone getServerZoneByCode(String zoneCode)
			throws HsCloudException {		
		return this.findUniqueBy("code", zoneCode);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServerZone> getAllZones(List<Object> zoneIds)
			throws HsCloudException {
		List<ServerZone> serverZoneList = null;
		try{
			StringBuffer hql = new StringBuffer("from ServerZone sz where sz.isEnable=1");
			if(zoneIds !=null && zoneIds.size()==0){
				zoneIds.add(0L);
			}			
			if(zoneIds ==null){
				serverZoneList = this.find(hql.toString());
			}else{				
				hql.append(" AND sz.id in (:list)");
				Query query = getSession().createQuery(hql.toString());
				query.setParameterList("list", zoneIds);
				serverZoneList = query.list();
			}			
		} catch (Exception e) {
			logger.error("getAllZones Exception:", e);
        }		
		return serverZoneList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServerZone> getAllZonesByGroupId(long zoneGroupId)
			throws HsCloudException {
		List<ServerZone> listZone = null;//
		StringBuffer sql = new StringBuffer("select DISTINCT sz.id,sz.name,sz.code,sz.description,");
		sql.append("sz.create_id as createId,sz.create_date as createDate,");
		sql.append("sz.update_Id as updateId,sz.update_date as updateDate,");
		sql.append("sz.is_default as isDefault,sz.is_enable as isEnable");
		sql.append(" from hc_zone as sz ");
		sql.append(" INNER JOIN hc_node_zone AS nz ON (nz.zone_id = sz.id) ");
		sql.append(" INNER JOIN hc_zone_zonegroup AS zzp ON (sz.id = zzp.zone_id)");
		sql.append(" where sz.is_enable=1 ");	
		if(zoneGroupId!=0){
			sql.append(" and zzp.zonegroup_id =:zoneGroupId ");
		}
		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
		if(zoneGroupId!=0){
			sqlQuery.setParameter("zoneGroupId", zoneGroupId);
		}
		
		try{
			sqlQuery.addScalar("id",Hibernate.LONG);
			sqlQuery.addScalar("name",Hibernate.STRING);
			sqlQuery.addScalar("code",Hibernate.STRING);
			sqlQuery.addScalar("description",Hibernate.STRING);
			sqlQuery.addScalar("createId",Hibernate.LONG);
			sqlQuery.addScalar("createDate",Hibernate.DATE);
			sqlQuery.addScalar("updateId",Hibernate.LONG);
			sqlQuery.addScalar("updateDate",Hibernate.DATE);
			sqlQuery.addScalar("isDefault",Hibernate.INTEGER);
			sqlQuery.addScalar("isEnable",Hibernate.INTEGER);
			sqlQuery.setResultTransformer(Transformers.aliasToBean(ServerZone.class));
			listZone = sqlQuery.list();
		}catch (Exception e) {
			logger.error("getAllZonesByGroupId Exception:", e);
		}
		return listZone;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<ZoneVO> getRelatedServerZone(Page<ZoneVO> page,
			long zoneGroupId, String zoneName) throws HsCloudException {
		int totalCount = 0;
		StringBuffer sql=new StringBuffer("SELECT zone.id AS zoneId,zone.name AS zoneName FROM  hc_zone AS zone");
		sql.append(" INNER JOIN hc_zone_zonegroup AS zzp ON (zone.id = zzp.zone_id)");
		sql.append(" WHERE zone.is_enable = 1 AND zzp.zonegroup_id =:groupId");
		if(zoneName != null && !"".equals(zoneName)){
			sql.append(" and zone.name like :name ");
		}
		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
		sqlQuery.setParameter("groupId", zoneGroupId);
		if(zoneName != null && !"".equals(zoneName)){
			sqlQuery.setParameter("name", "%"+zoneName+"%");
		}
		sqlQuery.addScalar("zoneId",Hibernate.LONG);
		sqlQuery.addScalar("zoneName",Hibernate.STRING);
		sqlQuery.setResultTransformer(Transformers.aliasToBean(ZoneVO.class));		
		totalCount = sqlQuery.list().size();
		sqlQuery.setFirstResult((page.getPageNo() - 1) * page.getPageSize());
		sqlQuery.setMaxResults(page.getPageSize());
		List<ZoneVO> zoneList = sqlQuery.list();
		page.setTotalCount(totalCount);
		page.setResult(zoneList);
		return page;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<ZoneVO> getUnRelatedServerZone(Page<ZoneVO> page,
			long zoneGroupId, String zoneName) throws HsCloudException {
		int totalCount = 0;
		StringBuffer sql=new StringBuffer("SELECT zone.id AS zoneId,zone.name AS zoneName FROM  hc_zone AS zone WHERE zone.is_enable = 1");
		sql.append(" AND zone.id NOT IN ( SELECT zzp.zone_id FROM hc_zone_zonegroup AS zzp WHERE zzp.zonegroup_id=:groupId)");
		if(zoneName != null && !"".equals(zoneName)){
			sql.append(" and zone.name like :name ");
		}
		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
		sqlQuery.setParameter("groupId", zoneGroupId);
		if(zoneName != null && !"".equals(zoneName)){
			sqlQuery.setParameter("name", "%"+zoneName+"%");
		}
		sqlQuery.addScalar("zoneId",Hibernate.LONG);
		sqlQuery.addScalar("zoneName",Hibernate.STRING);
		sqlQuery.setResultTransformer(Transformers.aliasToBean(ZoneVO.class));		
		totalCount = sqlQuery.list().size();
		sqlQuery.setFirstResult((page.getPageNo() - 1) * page.getPageSize());
		sqlQuery.setMaxResults(page.getPageSize());
		List<ZoneVO> zoneList = sqlQuery.list();
		page.setTotalCount(totalCount);
		page.setResult(zoneList);
		return page;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServerZone> getAllZones(String codes) throws HsCloudException {
		List<ServerZone> serverZoneList = null;
		String[]codeArray = null;		
		try{
			StringBuffer hql = new StringBuffer("from ServerZone sz where sz.isEnable=1");					
			if(StringUtils.isEmpty(codes.trim())){
				serverZoneList = this.find(hql.toString());
			}else{
				codeArray = codes.split(",");
				hql.append(" AND sz.code in (:list)");
				Query query = getSession().createQuery(hql.toString());
				query.setParameterList("list", codeArray);
				serverZoneList = query.list();
			}			
		} catch (Exception e) {
			logger.error("getAllZones Exception:", e);
        }		
		return serverZoneList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ZoneVO> getZoneByAdmin(long adminId,boolean needFilter) throws HsCloudException {
		StringBuilder hql = new StringBuilder(
				"select t.code as zoneCode,t.name as zoneName from hc_zone t");
		Map<String, Long> params = null;
		if (needFilter) {
			params = new HashMap<String, Long>();
			hql.append(" left join hc_role_zone t1 on t.id = t1.zone_id ")
					.append(" left join hc_admin_role t2 on t1.role_id = t2.roleId ")
					.append(" left join hc_admin t3 on t2.adminId = t3.id ")
					.append(" where t3.id = :adminId and t.is_enable=1 ");
			params.put("adminId", adminId);
		} else {
			hql.append(" where t.is_enable=1 ");
		}
		try {
			SQLQuery sqlQuery = getSession().createSQLQuery(hql.toString());
			sqlQuery.setProperties(params);
			sqlQuery.addScalar("zoneCode", Hibernate.STRING);
			sqlQuery.addScalar("zoneName", Hibernate.STRING);
			sqlQuery.setResultTransformer(Transformers
					.aliasToBean(ZoneVO.class));
			List<ZoneVO> result = sqlQuery.list();
			if (result != null && result.size() > 0) {
				return result;
			} else {
				return new ArrayList<ZoneVO>();
			}
		} catch (Exception e) {
			logger.error("getZoneByAdmin Exception:", e);
			throw new HsCloudException("",logger,e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServerZone> getAllZonesByCondition(long zoneGroupId,
			String isCustom) throws HsCloudException {
		List<ServerZone> listZone = null;//
		StringBuffer sql = new StringBuffer("select DISTINCT sz.id,sz.name,sz.code,sz.description,");
		sql.append("sz.create_id as createId,sz.create_date as createDate,");
		sql.append("sz.update_Id as updateId,sz.update_date as updateDate,");
		sql.append("sz.is_default as isDefault,sz.is_enable as isEnable,sz.is_custom as isCustom");
		sql.append(" from hc_zone as sz ");
		sql.append(" INNER JOIN hc_node_zone AS nz ON (nz.zone_id = sz.id) ");
		sql.append(" INNER JOIN hc_zone_zonegroup AS zzp ON (sz.id = zzp.zone_id)");
		sql.append(" where sz.is_enable=1 ");	
		if(zoneGroupId!=0){
			sql.append(" and zzp.zonegroup_id =:zoneGroupId ");
		}
		if(StringUtils.isNotEmpty(isCustom)){
			sql.append(" and sz.is_custom =:isCustom ");
		}
		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
		if(zoneGroupId!=0){
			sqlQuery.setParameter("zoneGroupId", zoneGroupId);
		}
		if(StringUtils.isNotEmpty(isCustom)){
			sqlQuery.setParameter("isCustom", isCustom);
		}
		try{
			sqlQuery.addScalar("id",Hibernate.LONG);
			sqlQuery.addScalar("name",Hibernate.STRING);
			sqlQuery.addScalar("code",Hibernate.STRING);
			sqlQuery.addScalar("description",Hibernate.STRING);
			sqlQuery.addScalar("createId",Hibernate.LONG);
			sqlQuery.addScalar("createDate",Hibernate.DATE);
			sqlQuery.addScalar("updateId",Hibernate.LONG);
			sqlQuery.addScalar("updateDate",Hibernate.DATE);
			sqlQuery.addScalar("isDefault",Hibernate.INTEGER);
			sqlQuery.addScalar("isEnable",Hibernate.INTEGER);
			sqlQuery.addScalar("isCustom",Hibernate.INTEGER);
			sqlQuery.setResultTransformer(Transformers.aliasToBean(ServerZone.class));
			listZone = sqlQuery.list();
		}catch (Exception e) {
			logger.error("getAllZonesByCondition Exception:", e);
		}
		return listZone;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServerZone> getAllZonesByCondition(String zoneGroupCode,
			String isCustom) throws HsCloudException {
		List<ServerZone> listZone = null;//
		StringBuffer sql = new StringBuffer("select DISTINCT sz.id,sz.name,sz.code,sz.description,");
		sql.append("sz.create_id as createId,sz.create_date as createDate,");
		sql.append("sz.update_Id as updateId,sz.update_date as updateDate,");
		sql.append("sz.is_default as isDefault,sz.is_enable as isEnable,sz.is_custom as isCustom");
		sql.append(" from hc_zone as sz ");
		sql.append(" INNER JOIN hc_node_zone AS nz ON (nz.zone_id = sz.id) ");
		sql.append(" INNER JOIN hc_zone_zonegroup AS zzp ON (sz.id = zzp.zone_id)");
		sql.append(" INNER JOIN hc_zonegroup AS zg ON (zg.id = zzp.zonegroup_id)");
		sql.append(" where sz.is_enable=1 ");	
		if(!"".equals(zoneGroupCode)){
			sql.append(" and zg.code =:zoneGroupCode ");
		}
		if(StringUtils.isNotEmpty(isCustom)){
			sql.append(" and sz.is_custom =:isCustom ");
		}
		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
		if(!"".equals(zoneGroupCode)){
			sqlQuery.setParameter("zoneGroupCode", zoneGroupCode);
		}
		if(StringUtils.isNotEmpty(isCustom)){
			sqlQuery.setParameter("isCustom", isCustom);
		}
		try{
			sqlQuery.addScalar("id",Hibernate.LONG);
			sqlQuery.addScalar("name",Hibernate.STRING);
			sqlQuery.addScalar("code",Hibernate.STRING);
			sqlQuery.addScalar("description",Hibernate.STRING);
			sqlQuery.addScalar("createId",Hibernate.LONG);
			sqlQuery.addScalar("createDate",Hibernate.DATE);
			sqlQuery.addScalar("updateId",Hibernate.LONG);
			sqlQuery.addScalar("updateDate",Hibernate.DATE);
			sqlQuery.addScalar("isDefault",Hibernate.INTEGER);
			sqlQuery.addScalar("isEnable",Hibernate.INTEGER);
			sqlQuery.addScalar("isCustom",Hibernate.INTEGER);
			sqlQuery.setResultTransformer(Transformers.aliasToBean(ServerZone.class));
			listZone = sqlQuery.list();
		}catch (Exception e) {
			logger.error("getAllZonesByCondition Exception:", e);
		}
		return listZone;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<OsVO> getUnRelatedServerOs(Page<OsVO> page, long zoneGroupId,
			String OsName) throws HsCloudException {
		int totalCount=0;
		StringBuffer sb = new StringBuffer("SELECT DISTINCT ho.os_id,hsi.name FROM hc_os ho ");
		sb.append("LEFT JOIN hc_service_item hsi on ho.os_id=hsi.item_id ");
		sb.append("WHERE ho.os_id NOT IN (SELECT oz.os_id from hc_os_zonegroup oz WHERE oz.zoneGroup_id = :zoneGroupId)");
		if(OsName != null && !"".equals(OsName)){
			sb.append(" AND hsi.name like :OsName");
		}
		
		SQLQuery sqlQuery = getSession().createSQLQuery(sb.toString());
		sqlQuery.setParameter("zoneGroupId", zoneGroupId);
		if(OsName != null && !"".equals(OsName)){
			sqlQuery.setParameter("OsName", "%"+OsName+"%");
		}
		sqlQuery.addScalar("os_id",Hibernate.INTEGER);
		sqlQuery.addScalar("name",Hibernate.STRING);
		
		sqlQuery.setResultTransformer(Transformers.aliasToBean(OsVO.class));	
		totalCount = sqlQuery.list().size();
		sqlQuery.setFirstResult((page.getPageNo() - 1) * page.getPageSize());
		sqlQuery.setMaxResults(page.getPageSize());
		List<OsVO> osList = sqlQuery.list();
		page.setTotalCount(totalCount);
		page.setResult(osList);
		return page;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<OsVO> getRelatedServerOs(Page<OsVO> page, long zoneGroupId,
			String OsName) throws HsCloudException {
		int totalCount=0;
		StringBuffer sb = new StringBuffer("SELECT DISTINCT ho.os_id,hsi.name FROM hc_os ho ");
		sb.append("LEFT JOIN hc_service_item hsi on ho.os_id=hsi.item_id ");
		sb.append("RIGHT JOIN hc_os_zonegroup hoz ON hoz.os_id = ho.os_id ");
		sb.append("WHERE hoz.zonegroup_id = :zoneGroupId ");
		if(OsName != null && !"".equals(OsName)){
			sb.append(" AND hsi.name like :OsName");
		}
		
		SQLQuery sqlQuery = getSession().createSQLQuery(sb.toString());
		sqlQuery.setParameter("zoneGroupId", zoneGroupId);
		if(OsName != null && !"".equals(OsName)){
			sqlQuery.setParameter("OsName", "%"+OsName+"%");
		}
		sqlQuery.addScalar("os_id",Hibernate.INTEGER);
		sqlQuery.addScalar("name",Hibernate.STRING);
		
		sqlQuery.setResultTransformer(Transformers.aliasToBean(OsVO.class));		
		totalCount = sqlQuery.list().size();
		sqlQuery.setFirstResult((page.getPageNo() - 1) * page.getPageSize());
		sqlQuery.setMaxResults(page.getPageSize());
		List<OsVO> osList = sqlQuery.list();
		page.setTotalCount(totalCount);
		page.setResult(osList);
		return page;
	}

	
}

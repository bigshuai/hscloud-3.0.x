/* 
* 文 件 名:  ZoneGroupDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-6-17 
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
import org.junit.Test;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.bss.sla.sc.dao.ZoneGroupDao;
import com.hisoft.hscloud.bss.sla.sc.entity.ZoneGroup;
import com.hisoft.hscloud.bss.sla.sc.vo.ZoneGroupVO;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.vo.AdminVO;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-6-17] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class ZoneGroupDaoImpl extends HibernateDao<ZoneGroup, Long> implements ZoneGroupDao {

	private Logger logger = Logger.getLogger(this.getClass());
	@Override
	public long createZoneGroup(ZoneGroup zoneGroup) throws HsCloudException {
		long id = 0L;
		try{
			this.save(zoneGroup);
			id = zoneGroup.getId();
		}catch (Exception e) {
			logger.error("createZoneGroup Exception:", e);
		}		
		return id;
	}

	@Override
	public boolean deleteZoneGroup(long id) throws HsCloudException {
		boolean result = false;
		try{
			ZoneGroup zoneGroup=this.getZoneGroupById(id);
			if(zoneGroup != null){
				this.delete(id);
				result = true;
			}
		}catch (Exception e) {
			logger.error("deleteZoneGroup Exception:", e);
		}		
		return result;
	}

	@Override
	public boolean updateZoneGroup(ZoneGroup zoneGroup) throws HsCloudException {
		boolean result = false;
		try{
			this.save(zoneGroup);
			result = true;
		}catch (Exception e) {
			logger.error("updateZoneGroup Exception:", e);
		}		
		return result;
	}

	@Override
	public ZoneGroup getZoneGroupById(long id) throws HsCloudException {
		return this.findUniqueBy("id", id);
	}

	@Override
	public Page<ZoneGroup> findZoneGroup(Page<ZoneGroup> page, String field,
			String fieldValue) throws HsCloudException {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("from ZoneGroup as zg where 2>1");
		if(fieldValue != null && !"".equals(fieldValue)){
			hql.append("  and zg.code like :code or zg.name like :name ").append(" order by id desc");
			map.put("code", "%"+fieldValue+"%");
			map.put("name", "%"+fieldValue+"%");			
			page = this.findPage(page, hql.toString(), map);
		}else if(field != null && !"".equals(field)){
			hql.append(" and zg.").append(field).append(" =:fieldValue").append(" order by id desc");			
			map.put("fieldValue", fieldValue);
			page = this.findPage(page, hql.toString(), map);
		}else{
			hql.append(" order by id desc");
			page = this.findPage(page,hql.toString());
		}
		return page;
	}

	@Override
	public List<ZoneGroup> getAllZoneGroups() throws HsCloudException {
		StringBuffer hql = new StringBuffer("from ZoneGroup as zg where zg.isEnable=1 order by zg.id desc");
		return this.find(hql.toString());
	}

	@Override
	public List<ZoneGroup> getZoneGroupsByCondition(String field,
			Object fieldValue) throws HsCloudException {
		List<ZoneGroup> listZoneGroups = null;
		StringBuffer hql = new StringBuffer("from ZoneGroup as zg where 2>1 ");
		try{
			if(StringUtils.isNotEmpty(field.trim()) && fieldValue != null){			
				hql.append(" and zg.").append(field).append(" =:fieldValue");
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("fieldValue", fieldValue);			
				listZoneGroups = this.find(hql.toString(), map);
			}
		}catch (Exception e) {
			logger.error("getZoneGroupsByCondition Exception:", e);
		}				
		return listZoneGroups;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getAllZoneGroupIdsByZoneIds(List<Long> zoneIds)
			throws HsCloudException {
		List<Long> groupIdList = new ArrayList<Long>();
		try{
			StringBuffer hql = new StringBuffer("select distinct zg.id from ZoneGroup as zg,ServerZone as sz where zg.isEnable=1");
			if(zoneIds !=null && zoneIds.size()==0){
				zoneIds.add(0L);
			}			
			if(zoneIds !=null&&zoneIds.size()>0){
				hql.append(" and zg in elements( sz.zoneGroupList ) ");
				hql.append(" AND sz.id in (:list) ");
				Query query = getSession().createQuery(hql.toString());
				query.setParameterList("list", zoneIds);
				groupIdList = query.list();
			}			
		} catch (Exception e) {
			logger.error("getAllZones Exception:", e);
        }		
		return groupIdList;
	}

	@Override
	public List<ZoneGroupVO> getAllZoneGroupByUser(String brandCode,Long domainId)
			throws HsCloudException {
		List<ZoneGroupVO> result = new ArrayList<ZoneGroupVO>();
		try {    
			StringBuilder sql = new StringBuilder("");
			sql.append("select DISTINCT t5.id,t5.`name`,t5.description from ")
					.append(" hc_service_catalog t1 ,hc_catalog_brand t2,hc_catalog_zone t3, ")
					.append(" hc_zone_zonegroup t4,hc_zonegroup t5,hc_zone t6,hc_user_brand t7 ")
					.append(" ,hc_catalog_domain t8,hc_domain t9,hc_domain_userbrand t10 ")
					.append(" where t1.id=t2.service_catalog_id and t2.brand_id=t7.id ")
					.append(" and t1.id=t3.service_catalog_id and t3.zone_id=t4.zone_id ")
					.append(" and t4.zonegroup_id=t5.id and t4.zone_id=t6.id ")
					.append(" and t1.`status` in (1,3) and t5.is_enable=1 ")
					.append(" and t6.is_enable=1 and t7.`status`=1 ")     
					.append(" and t1.id=t8.service_catalog_id and t8.domain_id= :domainId")
					.append(" and t7.`code`= :brandCode and t10.domain_id=t9.id and t10.userbrand_id=t7.id ");
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.addScalar("id", Hibernate.LONG);
			query.addScalar("name", Hibernate.STRING);
			query.addScalar("description", Hibernate.STRING);
			query.setResultTransformer(Transformers
					.aliasToBean(ZoneGroupVO.class));
			query.setString("brandCode", brandCode);
			query.setLong("domainId", domainId);
			result = query.list();
		} catch (Exception e) {
			throw new HsCloudException("getAllZoneGroupByUser Exception",
					logger, e);
		}
		return result;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ZoneGroupVO> getCustomZoneGroupByUser(String brandCode,
			Long domainId) throws HsCloudException {
		List<ZoneGroupVO> result = new ArrayList<ZoneGroupVO>();
		try {
			StringBuilder sql = new StringBuilder("SELECT DISTINCT zg.id,zg.`name`,zg.description,zg.code ");
			sql.append(" FROM hc_zone_zonegroup zzg, ");
			sql.append(" hc_zonegroup zg, ");
			sql.append(" hc_zone z, ");
			sql.append(" hc_user_brand ub, ");
			sql.append(" hc_domain d, ");
			sql.append(" hc_domain_userbrand du, ");
			sql.append(" hc_zonegroup_domain zgd ");
			sql.append(" WHERE zzg.zonegroup_id=zg.id ");
			sql.append(" AND zzg.zone_id=z.id ");
			sql.append(" AND zg.is_enable=1 ");
			sql.append(" AND z.is_enable=1 ");
			sql.append(" AND z.is_custom=1 ");
			sql.append(" AND ub.`status`=1 ");
			sql.append(" AND ub.`code`=:brandCode");
			sql.append(" AND du.domain_id=d.id ");
			sql.append(" AND du.userbrand_id=ub.id ");
			sql.append(" AND zgd.domain_id=d.id ");
			sql.append(" AND zgd.zonegroup_id=zg.id ");
			sql.append(" AND d.id=:domainId");
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.addScalar("id", Hibernate.LONG);
			query.addScalar("name", Hibernate.STRING);
			query.addScalar("description", Hibernate.STRING);
			query.addScalar("code", Hibernate.STRING);
			query.setResultTransformer(Transformers.aliasToBean(ZoneGroupVO.class));
			query.setString("brandCode", brandCode);
			query.setLong("domainId", domainId);
			result = query.list();
		} catch (Exception e) {
			throw new HsCloudException("getCustomZoneGroupByUser Exception",logger, e);
		}
		return result;
	}
	
	

}

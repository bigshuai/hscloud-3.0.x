package com.hisoft.hscloud.systemmanagement.dao;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.entity.HcEventResource;
import com.hisoft.hscloud.common.entity.HcEventVmOps;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.systemmanagement.vo.LogQueryVO;

@Repository
public class HcEventResourceDaoImpl extends HibernateDao<HcEventResource, Long>
		implements HcEventResourceDao {
	private Logger logger = Logger.getLogger(HcEventResourceDaoImpl.class);
	
	@SuppressWarnings("unchecked")
	private Page<HcEventResource> subGetVmOpsLog(Page<HcEventResource> paing,LogQueryVO param, List<Object> zoneIdArray) {
		StringBuilder sql = new StringBuilder("SELECT ev.id, ev.message, ev.obj_id, ev.event_time, ");
		sql.append("  ev.deal_time, ev.finish_time, ev.update_time, " );
		sql.append("  ev.type,ev.biz_type,ev.res_type,ev.error_info ");
		sql.append(" , ev.operator, ev.operator_id, ev.operator_type, ev.result ");
		sql.append(" , ev.remark, ev.job_server, ev.obj_name, ev.task_id  ");
		sql.append(" FROM hc_event_resource ev, hc_vpdc_reference vr, hc_zone zone ");
		sql.append(" where ev.obj_id = vr.id");
		sql.append(" and SUBSTRING_INDEX(vr.vm_zone, '$', 1) = zone.code AND zone.id IN(:list)");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("list", zoneIdArray);
		if (param.getStartTime() != null) {
			sql.append(" and ev.event_time >= :startTime ");
			params.put("startTime", param.getStartTime());
		}
		if (param.getEndTime() != null) {
			sql.append(" and ev.event_time <= :endTime ");
			params.put("endTime", param.getEndTime());
		}
		if (StringUtils.isNotBlank(param.getOperator())) {
			sql.append(" and ev.operator  like :operator ");
			params.put("operator", "%" + param.getOperator() + "%");
		}
		if (StringUtils.isNotBlank(param.getRemark())) {
			sql.append(" and ev.remark like :remark ");
			params.put("remark", "%" + param.getRemark() + "%");
		}
		if (StringUtils.isNotBlank(param.getMessage())) {
			sql.append(" and ( ev.message like :message or ev.obj_name like :vmName ) ");
			params.put("message", "%" + param.getMessage() + "%");
			params.put("vmName", "%" + param.getMessage() + "%");
		}
		if (param.getResult()!=0) {
			sql.append(" and ev.result = :result ");
			params.put("result", param.getResult());
		}
		if (param.getType() != 0) {
			sql.append(" and ev.type = :type ");
			params.put("type", param.getType());
		}
		if(param.getBiz_type()!=0){
			sql.append(" and her.biz_type = :biz_type ");
			params.put("biz_type", param.getBiz_type());
		}
		if(param.getRes_type()!=0){
			sql.append(" and ev.res_type = :res_type ");
			params.put("res_type", param.getRes_type());
		}
		String sqlStr = sql.toString();
		SQLQuery query = getSession().createSQLQuery(sqlStr);
		for (Entry<String, Object> entry : params.entrySet()) {
	    	  if(entry.getValue() instanceof Collection){
	    		  query.setParameterList(entry.getKey(), (Collection<?>)entry.getValue());
	    	  }else{
	    		  query.setParameter(entry.getKey(), entry.getValue());
	    	  }
		}
		query.addScalar("id", Hibernate.LONG);
		query.addScalar("message", Hibernate.STRING);
		query.addScalar("obj_id", Hibernate.LONG);
		query.addScalar("event_time", Hibernate.TIMESTAMP);
		query.addScalar("deal_time", Hibernate.TIMESTAMP);
		query.addScalar("finish_time", Hibernate.TIMESTAMP);
		query.addScalar("update_time", Hibernate.TIMESTAMP);
		query.addScalar("type", Hibernate.SHORT);
		query.addScalar("biz_type", Hibernate.SHORT);
		query.addScalar("res_type", Hibernate.SHORT);
		query.addScalar("operator", Hibernate.STRING);
		query.addScalar("operator_id", Hibernate.LONG);
		query.addScalar("operator_type", Hibernate.SHORT);
		query.addScalar("result", Hibernate.SHORT);
		query.addScalar("error_info", Hibernate.STRING);
		query.addScalar("remark", Hibernate.STRING);
		query.addScalar("job_server", Hibernate.STRING);
		query.addScalar("obj_name", Hibernate.STRING);
		query.addScalar("task_id", Hibernate.LONG);
		query.setResultTransformer(Transformers.aliasToBean(HcEventResource.class));
		query.setFirstResult((paing.getPageNo() - 1) * paing.getPageSize());
		query.setMaxResults(paing.getPageSize());
		
		
		String counterSql = "select count(1) " + sqlStr.substring(sqlStr.indexOf("FROM"));
		SQLQuery countQuery = getSession().createSQLQuery(counterSql.toString());
		for (Entry<String, Object> entry : params.entrySet()) {
			if (entry.getValue() instanceof Collection) {
				countQuery.setParameterList(entry.getKey(), (Collection<?>) entry.getValue());
			} else {
				countQuery.setParameter(entry.getKey(), entry.getValue());
			}
		}
		paing.setTotalCount(Long.valueOf(countQuery.uniqueResult().toString()));
		
		List<HcEventResource> object = query.list();
		paing.setResult(object);
		return paing;
	}

	@Override
	public Page<HcEventResource> getResourceLog(Page<HcEventResource> paing,LogQueryVO param)
			throws HsCloudException {
		try {
			List<Object> zoneIdArray = param.getZoneIdArray();
			if(zoneIdArray != null) {
				return subGetVmOpsLog(paing, param, zoneIdArray);
			}
			Date startTime = param.getStartTime();
			Date endTime = param.getEndTime();
			String operator = param.getOperator();
			short type = param.getType();
			short biz_type=param.getBiz_type();
			short res_type=param.getRes_type();
			short result = param.getResult();
			String remark = param.getRemark();
			String message=param.getMessage();
			StringBuilder hql = new StringBuilder();
			Map<String, Object> params = new HashMap<String, Object>();
			hql.append("from HcEventResource her where 1=1 ");
			if (startTime != null) {
				hql.append(" and her.event_time >= :startTime ");
				params.put("startTime", startTime);
			}
			if (endTime != null) {
				hql.append(" and her.event_time <= :endTime ");
				params.put("endTime", endTime);
			}
			if (StringUtils.isNotBlank(operator)) {
				hql.append(" and her.operator  like :operator ");
				params.put("operator", "%" + operator + "%");
			}
			if (StringUtils.isNotBlank(remark)) {
				hql.append(" and her.remark like :remark ");
				params.put("remark", "%" + remark + "%");
			}
			if (StringUtils.isNotBlank(message)) {
				hql.append(" and ( her.message like :message or her.obj_name like :vmName ) ");
				params.put("message", "%" + message + "%");
				params.put("vmName", "%" + message + "%");
			}
			if (result!=0) {
				hql.append(" and her.result = :result ");
				params.put("result", result);
			}
			if (type != 0) {
				hql.append(" and her.type = :type ");
				params.put("type", type);
			}
			if(biz_type!=0){
				hql.append(" and her.biz_type = :biz_type ");
				params.put("biz_type", biz_type);
			}
			if(res_type!=0){
				hql.append(" and her.res_type = :res_type ");
				params.put("res_type", res_type);
			}
			hql.append(" order by her.event_time desc ");
			return super.findPage(paing, hql.toString(), params);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public HcEventResource getResourceLogById(Long id) throws HsCloudException {
		try{
			HcEventResource resourceLog=super.findUniqueBy("id", id);
			return resourceLog;
		}catch(Exception e){
			throw new HsCloudException("", logger, e);
		}
	}
	
	

}

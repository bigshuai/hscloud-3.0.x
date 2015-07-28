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

import com.hisoft.hscloud.common.entity.HcEventVmOps;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.systemmanagement.vo.LogQueryVO;

@Repository
public class HcEventVmOpsDaoImpl extends HibernateDao<HcEventVmOps, Long>
		implements HcEventVmOpsDao {
	private Logger logger = Logger.getLogger(HcEventVmOpsDaoImpl.class);
	
	@SuppressWarnings("unchecked")
	private Page<HcEventVmOps> subGetVmOpsLog(Page<HcEventVmOps> paing,LogQueryVO param, List<Object> zoneIdArray) {
		
		StringBuilder sql = new StringBuilder("SELECT ev.id, ev.message, ev.reference_id, ev.event_time, ");
		sql.append("  ev.deal_time, ev.finish_time, ev.update_time, ev.ops, ev.uuid, ev.error_info ");
		sql.append(" , ev.operator, ev.operator_id, ev.operator_type, ev.result ");
		sql.append(" , ev.remark, ev.job_server, ev.obj_name, ev.task_id  ");
		sql.append(" FROM hc_event_vmops ev, hc_vpdc_instance vi, hc_vpdc_reference vr, hc_zone zone ");
		sql.append(" where ev.uuid = vi.vm_id and vi.VpdcRefrenceId = vr.id");
		sql.append(" and SUBSTRING_INDEX(vr.vm_zone, '$', 1) = zone.code AND zone.id IN(:list)");
		Date startTime = param.getStartTime();
		Date endTime = param.getEndTime();
		String operator = param.getOperator();
		short type = param.getType();
		short result = param.getResult();
		String message=param.getMessage();
		String remark = param.getRemark();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("list", zoneIdArray);
		if (startTime != null) {
			sql.append(" and ev.event_time >= :startTime ");
			params.put("startTime", startTime);
		}
		if (endTime != null) {
			sql.append(" and ev.event_time <= :endTime ");
			params.put("endTime", endTime);
		}
		if (StringUtils.isNotBlank(operator)) {
			sql.append(" and ev.operator  like :operator ");
			params.put("operator", "%" + operator + "%");
		}
		if (StringUtils.isNotBlank(message)) {
			sql.append(" and ( ev.message like :message or ev.obj_name like :vmName ) ");
			params.put("message", "%" + message + "%");
			params.put("vmName", "%" + message + "%");
		}
		if (StringUtils.isNotBlank(remark)) {
			sql.append(" and ev.remark like :remark ");
			params.put("remark", "%" + remark + "%");
		}
		if (result!=0) {
			sql.append(" and ev.result = :result ");
			params.put("result",result);
		}
		if (type != 0) {
			sql.append(" and ev.ops = :ops ");
			params.put("ops", type);
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
		query.addScalar("reference_id", Hibernate.LONG);
		query.addScalar("event_time", Hibernate.TIMESTAMP);
		query.addScalar("deal_time", Hibernate.TIMESTAMP);
		query.addScalar("finish_time", Hibernate.TIMESTAMP);
		query.addScalar("update_time", Hibernate.TIMESTAMP);
		query.addScalar("ops", Hibernate.SHORT);
		query.addScalar("uuid", Hibernate.STRING);
		query.addScalar("operator", Hibernate.STRING);
		query.addScalar("operator_id", Hibernate.LONG);
		query.addScalar("operator_type", Hibernate.SHORT);
		query.addScalar("result", Hibernate.SHORT);
		query.addScalar("error_info", Hibernate.STRING);
		query.addScalar("remark", Hibernate.STRING);
		query.addScalar("job_server", Hibernate.STRING);
		query.addScalar("obj_name", Hibernate.STRING);
		query.addScalar("task_id", Hibernate.LONG);
		query.setResultTransformer(Transformers.aliasToBean(HcEventVmOps.class));
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
		
		List<HcEventVmOps> object = query.list();
		paing.setResult(object);
		return paing;
	}

	@Override
	public Page<HcEventVmOps> getVmOpsLog(Page<HcEventVmOps> paing,LogQueryVO param)
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
			short result = param.getResult();
			String message=param.getMessage();
			String remark = param.getRemark();
			StringBuilder hql = new StringBuilder();
			Map<String, Object> params = new HashMap<String, Object>();
			hql.append("from HcEventVmOps hev where 1=1 ");
			if (startTime != null) {
				hql.append(" and hev.event_time >= :startTime ");
				params.put("startTime", startTime);
			}
			if (endTime != null) {
				hql.append(" and hev.event_time <= :endTime ");
				params.put("endTime", endTime);
			}
			if (StringUtils.isNotBlank(operator)) {
				hql.append(" and hev.operator  like :operator ");
				params.put("operator", "%" + operator + "%");
			}
			if (StringUtils.isNotBlank(message)) {
				hql.append(" and ( hev.message like :message or hev.obj_name like :vmName ) ");
				params.put("message", "%" + message + "%");
				params.put("vmName", "%" + message + "%");
			}
			if (StringUtils.isNotBlank(remark)) {
				hql.append(" and hev.remark like :remark ");
				params.put("remark", "%" + remark + "%");
			}
			if (result!=0) {
				hql.append(" and hev.result = :result ");
				params.put("result",result);
			}
			if (type != 0) {
				hql.append(" and hev.ops = :ops ");
				params.put("ops", type);
			}
			hql.append(" order by hev.event_time desc ");
			return super.findPage(paing,hql.toString(), params);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public HcEventVmOps getVMOpsLogById(Long id) throws HsCloudException {
		try{
			HcEventVmOps vmOps=super.findUniqueBy("id", id);
			return vmOps;
		}catch(Exception e){
			throw new HsCloudException("", logger, e);
		}
		
	}

	@Override
	public Page<HcEventVmOps> getVmOperationLog(Page<HcEventVmOps> paing,
			LogQueryVO param) throws HsCloudException {
		try {
			// 虚拟机的uuid
			String uuid = param.getUuid();
			//控制面板 或 web-site
			int operationType = param.getOperationType();
			// 操作类型
			Short ops = param.getOps();
			// 操作时间
			Date startTime = param.getStartTime();
			// 完成时间
			Date endTime = param.getEndTime();
			StringBuilder hql = new StringBuilder();
			Map<String, Object> params = new HashMap<String, Object>();
			hql.append("from HcEventVmOps hev where hev.uuid = :uuid ");
			// 虚拟机的uuid
			params.put("uuid", uuid);
			// 操作类型
			if (null != ops && 0 != ops) {
				hql.append(" and hev.ops = :type ");
				params.put("type", ops);
			}
			if(0!=param.getOperationType()){
				hql.append(" and hev.operator_type=:operator_type");
				params.put("operator_type", (short)operationType);
			}
			// 操作时间
			if (startTime != null) {
				hql.append(" and hev.event_time >= :startTime ");
				params.put("startTime", startTime);
			}
			// 完成时间
			if (endTime != null) {
				hql.append(" and hev.event_time <= :endTime ");
				params.put("endTime", endTime);
			}
			hql.append(" order by hev.event_time desc ");
			return super.findPage(paing, hql.toString(), params);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public HcEventVmOps getVMOperationLogById(Long opsId)
			throws HsCloudException {
		HcEventVmOps result=super.findUniqueBy("id", opsId);
		if(result!=null){
			return result;
		}
		return null;
	}
	
}

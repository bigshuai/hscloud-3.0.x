package com.hisoft.hscloud.common.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.dao.OperationLogDao;
import com.hisoft.hscloud.common.entity.OperationLog;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.vo.OperationLogQueryVo;

@Repository
public class OperationLogDaoImpl extends HibernateDao<OperationLog, Long>
		implements OperationLogDao {
	private Logger logger = Logger.getLogger(OperationLogDaoImpl.class);

	@Override
	public void insertOperationLog(OperationLog log) throws HsCloudException {
		try {
			this.save(log);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public Page<OperationLog> getOperationByPage(Page<OperationLog> paging,
			OperationLogQueryVo queryVo) throws HsCloudException {

		StringBuilder hql = new StringBuilder(
				" from OperationLog ol where 1=1 ");
		Map params = new HashMap<String, String>();
		try {
			String domainAbb = queryVo.getDomainAbb();
			if (StringUtils.isNotBlank(domainAbb)) {
				hql.append(" and ol.domainName = :domainAbb ");
				params.put("domainAbb", domainAbb);
			}
			String operator = queryVo.getOperator();
			if (StringUtils.isNotBlank(operator)) {
				hql.append(" and (ol.operator like :operator or ol.userName like :operator )");
				params.put("operator", "%" + operator + "%");
			}
			String action = queryVo.getAction();
			if (StringUtils.isNotBlank(action)) {
				hql.append(" and (ol.actionName like :action or ol.description like :action )");
				params.put("action", "%" + action + "%");
			}
			Date startTime=queryVo.getStartTime();
			if (startTime != null) {
				hql.append(" and ol.operationDate >= :startTime ");
				params.put("startTime", startTime);
			}
			Date endTime=queryVo.getEndTime();
			if (endTime != null) {
				hql.append(" and ol.operationDate <= :endTime ");
				params.put("endTime", endTime);
			}

			short operationResult = queryVo.getOperationResult();
			if (operationResult!=0) {
				hql.append(" and ol.operationResult = :operationResult ");
				params.put("operationResult", operationResult);

			}
			short operatorType = queryVo.getOperatorType();
			if (operatorType!=0) {
				hql.append(" and ol.operatorType = :operatorType");
				params.put("operatorType", operatorType);
			}
			
			hql.append("order by ol.operationDate desc ");
			return findPage(paging, hql.toString(), params);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

}

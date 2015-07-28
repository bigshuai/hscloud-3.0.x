package com.hisoft.hscloud.systemmanagement.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.systemmanagement.vo.BusinessPlatformVO;
import com.hisoft.hscloud.systemmanagement.vo.ControlPanelVO;

@Repository
public class ControlPanelMaintenanceDaoImpl extends HibernateDao<ControlPanelVO, Long>
		implements ControlPanelMaintenanceDao {
	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public List<ControlPanelVO> getAllControlPanelByPage(int pageSize,
			int pageNo, String queryCondition, List<Long> domainIds)
			throws HsCloudException {
		List<ControlPanelVO> result = null;
		try {
			StringBuilder sql = new StringBuilder("select ");
			sql.append("t1.id as referenceId, t1.name as vmName,")
					.append("t4.vmId as machineNo,concat(t1.vm_innerIp,',',t1.vm_outerIp) as ip,")
					.append("t3.start_time as startTime,t3.end_time as expirationTime")
					.append(" from hc_controlpanel_user t4 left join ")
					.append(" hc_vpdc_instance t2 on t4.vmId=t2.vm_id")
					.append(" left join hc_vpdc_reference t1 ")
					.append(" on t2.VpdcRefrenceId=t1.id left join ")
				    .append("hc_vpdcReference_period t3 on t1.id=t3.renferenceId"); 
			if (domainIds != null && domainIds.size() > 0) {
				sql.append(" left join hc_user t5 on t1.vm_owner= t5.id ");
			}
			sql.append(" where 1=1");
			Map<String, Object> params = new HashMap<String, Object>();

			if (StringUtils.isNotBlank(queryCondition)) {
				sql.append(" and (t1.name like :query or ").append(
						"t4.vmId like :query or t1.vm_outerIp like :query) ");
				params.put("query", "%"+queryCondition+"%");
			}

			if (domainIds != null && domainIds.size() > 0) {
				sql.append(" and t5.domain_id in (:domainIds)");
				params.put("domainIds", domainIds);
			}
			sql.append(" and t4.status=0 and t1.status=0 and t2.status=0 ");
			SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
			sqlQuery.setProperties(params);
			sqlQuery.addScalar("referenceId", Hibernate.LONG);
			sqlQuery.addScalar("vmName", Hibernate.STRING);
			sqlQuery.addScalar("machineNo", Hibernate.STRING);
			sqlQuery.addScalar("ip", Hibernate.STRING);
			sqlQuery.addScalar("startTime", Hibernate.TIMESTAMP);
			sqlQuery.addScalar("expirationTime", Hibernate.TIMESTAMP);
			sqlQuery.setResultTransformer(Transformers
					.aliasToBean(ControlPanelVO.class));
			result = sqlQuery.setFirstResult(pageNo * pageSize - pageSize)
					.setMaxResults(pageSize).list();
			return result;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public long getAllControlPanelCount(String queryCondition,
			List<Long> domainIds) throws HsCloudException {
		try {
			StringBuilder sql = new StringBuilder("select ");
			sql.append("t1.id as referenceId, t1.name as vmName,")
					.append("t4.vmId as machineNo,concat(t1.vm_innerIp,',',t1.vm_outerIp)  as ip,")
					.append("t3.start_time as startTime,t3.end_time as expirationTime")
					.append(" from hc_controlpanel_user t4 left join ")
					.append(" hc_vpdc_instance t2 on t4.vmId=t2.vm_id")
					.append(" left join hc_vpdc_reference t1 ")
					.append(" on t2.VpdcRefrenceId=t1.id left join ")
				    .append("hc_vpdcReference_period t3 on t1.id=t3.renferenceId"); 
			if (domainIds != null && domainIds.size() > 0) {
				sql.append(" left join hc_user t5 on t1.vm_owner= t5.id ");
			}
			sql.append(" where 1=1");

			Map<String, Object> params = new HashMap<String, Object>();

			if (StringUtils.isNotBlank(queryCondition)) {
				sql.append(" and (t1.name like :query or ").append(
						"t4.vmId like :query or t1.vm_outerIp like :query) ");
				params.put("query", "%"+queryCondition+"%");
			}

			if (domainIds != null && domainIds.size() > 0) {
				sql.append(" and t5.domain_id in (:domainIds)");
				params.put("domainIds", domainIds);
			}
			sql.append(" and t4.status=0 and t1.status=0 and t2.status=0 ");
			String count = "select COUNT(1) count  from (" + sql.toString()
					+ ") t";
			SQLQuery sqlQuery = getSession().createSQLQuery(count);
			sqlQuery.setProperties(params);
			sqlQuery.addScalar("count", Hibernate.LONG);
			List<Long> list = sqlQuery.list();
			return list.get(0);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

}

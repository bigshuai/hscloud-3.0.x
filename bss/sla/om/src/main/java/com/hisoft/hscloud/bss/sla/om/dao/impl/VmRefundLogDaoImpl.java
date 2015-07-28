package com.hisoft.hscloud.bss.sla.om.dao.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.bss.sla.om.dao.VmRefundLogDao;
import com.hisoft.hscloud.bss.sla.om.entity.VmRefundLog;
import com.hisoft.hscloud.bss.sla.om.vo.VmRefundLogVO;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;

@Repository
public class VmRefundLogDaoImpl extends HibernateDao<VmRefundLog, Long>
		implements VmRefundLogDao {
	private Logger logger = Logger.getLogger(VmRefundLogDaoImpl.class);

	@Override
	public void saveVmRefundLog(VmRefundLog vmRefundLog)
			throws HsCloudException {
		try {
			super.save(vmRefundLog);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}
	
	/**
	 * <更新退款日志信息>
	* <功能详细描述> 
	* @param uuid 
	* @param refundReasonType 
	* @param applyReason 
	* @see [类、类#方法、类#成员]
	 */
	@Override
	public void updateVmRefundLog(String referenceStr, Short refundReasonType,
			String applyReason)throws HsCloudException {
	    // 更新时根据uuid去判断是哪条记录,因为uuid字段是unique,需要更新的字段:
	    // refundReasonType,applyReason,status更新为1,applyDate和updateDate
		long referenceId=Long.valueOf(referenceStr.split("@")[0]);
		String uuid=referenceStr.split("@")[1];
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time_now = sdf.format(new Date());
			StringBuilder sql = new StringBuilder();
			sql.append(" update hc_vm_refund_log set refund_reason_type= ")
			        .append(refundReasonType)
					.append(" , apply_refund_reason='")
					.append(applyReason)
					.append("' , status=1 ")
					.append(" , apply_date='")
					.append(time_now)
					.append("' , update_date='")
					.append(time_now)
					.append("' ,uuid='")
					.append(uuid)
					.append("' where referenceId=")
					.append(referenceId);
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.executeUpdate();
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public VmRefundLog getVmRefundLogById(Long id) throws HsCloudException {
		try {
			return super.findUniqueBy("id", id);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	/**
	 * <根据uuid来获取虚拟机的云主机名和外网IP> <功能详细描述>
	 * 
	 * @param uuid
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	public Map<String, String> getVmNameVmOuterIpByUuid(String uuid)
			throws HsCloudException {
		Map<String, String> map = new HashMap<String, String>();
		StringBuilder sql = new StringBuilder("");
		sql.append(" select hvr.name,hvr.vm_outerIP from ")
				.append(" hc_vpdc_instance hvi,hc_vpdc_reference hvr ")
				.append(" where hvi.VpdcRefrenceId = hvr.id ")
				.append(" and hvi.vm_id = '").append(uuid).append("'");
		logger.info(sql.toString());
		try {
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			List<Object[]> objects_list = query.list();
			if (objects_list.size() == 1) {
				Object[] object_array = objects_list.get(0);
				map.put("vmName", (String) object_array[0]);
				map.put("outerIp", (String) object_array[1]);
			}
			return map;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public Page<VmRefundLog> vmRefundLogPaging(short status, String query,
			Page<VmRefundLog> paging, List<String> ownerEmails)
			throws HsCloudException {
		try {
			StringBuilder hql = new StringBuilder();
			hql.append(" from VmRefundLog where 1=1 ");
			Map<String, Object> params = new HashMap<String, Object>();
			if (status != 0) {
				hql.append(" and status = :status ");
				params.put("status", status);
			}
			if (StringUtils.isNotBlank(query)) {
				hql.append(" and ( applyRefundReason like :applyReason or rejectRefundReason like :rejectReason or uuid like :uuid ) ");
				params.put("applyReason", "%" + query + "%");
				params.put("rejectReason", "%" + query + "%");
				params.put("uuid", "%" + query + "%");
			}
			if (ownerEmails != null && ownerEmails.size() > 0) {
				hql.append(" and ownerEmail in ( :ownerEmail ) ");
				params.put("ownerEmail", ownerEmails);
			}
			hql.append(" order by applyDate desc ");
			return super.findPage(paging, hql.toString(), params);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public VmRefundLog getVmRefundLogByUUID(String uuid)
			throws HsCloudException {
		// 判断正式云主机是否存在退款申请,即hc_vm_refund_log表中有无uuid的记录
		try {
			String hql = " from VmRefundLog where uuid=?";
			VmRefundLog vrl = findUnique(hql, uuid);
			return vrl;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}
	/**
	 * <根据虚拟机uuid判断云主机是否为申请中> 
	* <功能详细描述> 
	* @author liyunhui
	* @param uuid
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	@Override
	public VmRefundLog isVMApplyingRefundByUuid(String referenceId)
			throws HsCloudException {
		// 判断正式云主机是否存在退款申请,即hc_vm_refund_log表中有无uuid且status为1的记录
		try {
			short status = 1;
			String hql = " from VmRefundLog where referenceId=? and status=?";
			VmRefundLog vrl = findUnique(hql, Long.valueOf(referenceId), status);
			return vrl;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}
	/**
	 * <根据虚拟机uuid来判断它的status是否为3.拒绝 4.已取消> 
	* <功能详细描述> 
	* @author liyunhui
	* @param uuid
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	@Override
	public VmRefundLog isExistedVMStatusEquals3Or4ByUuid(String referenceId)
			throws HsCloudException {
		// 根据虚拟机uuid来判断它的status是否为3.拒绝 4.已取消> 
		try {
			short status_3 = 3;
			short status_4 = 4;
//			String hql = " from VmRefundLog where uuid=? and status=? ";
			String hql = " from VmRefundLog where (status=? or status=?) and referenceId=? ";
			VmRefundLog vrl = findUnique(hql, status_3, status_4, Long.valueOf(referenceId));
			return vrl;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public VmRefundLog findUnique(String hql, Object... values) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("enter VmRefundLogDaoImpl findUnique method.");
				logger.debug("hql:" + hql);
				logger.debug("values:" + values);
			}
			VmRefundLog vrl = (VmRefundLog) super.findUnique(hql, values);
			if (logger.isDebugEnabled()) {
				logger.debug("exit VmRefundLogDaoImpl findUnique method");
			}
			return vrl;
		} catch (Exception e) {
			throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,
					e.getMessage(), logger, e);
		}

	}

	@Override
	public List<VmRefundLogVO> vmRefundLogPaging4Website(Short status,
			String query, Page<VmRefundLogVO> paging, Long userId)
			throws HsCloudException {
		try {
			StringBuilder sql=new StringBuilder();
			sql.append(" select t1.id as id,t1.referenceId,t1.status,t1.orderNo,")
					.append(" t1.open_date as openDate,t1.expiration_date as expirationDate, ")
					.append(" t1.apply_date as applyDate,t1.apply_refund_reason as applyRefundReason, ")
					.append(" t1.reject_refund_reason as rejectRefundReason,t1.owner_email as ownerEmail, ")
					.append(" t1.refund_date as refundDate,t1.update_date as updateDate,t1.refund_amount as refundAmount, ")
					.append(" t1.operator,t1.refund_type as refundType, ")
					.append(" t1.refund_reason_type as refundReasonType, ")
					.append("t2.`name` as vmName,t2.vm_outerIP as outerIp,t1.uuid from hc_vm_refund_log t1 ")
			   .append(",hc_vpdc_reference t2 where t1.referenceId=t2.id ");
			   if(StringUtils.isNotBlank(query)){
				   // 由于输入框默认包含 机器号/IP地址，因为需要判断
					sql.append("and t2.name like :vmName "); 
			   }
			   if(status!=null&&status.shortValue()!=0){
				   sql.append("and t1.`status`= :status ");  
			   }
			   sql.append(" and t1.owner_id = :ownerId order by t1.apply_date desc");
			SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
			if(StringUtils.isNotBlank(query)){
				 // 由于输入框默认包含 机器号/IP地址，因为需要判断
				sqlQuery.setParameter("vmName", "%"+query+"%");
			}
			if(status!=null&&status.shortValue()!=0){
				sqlQuery.setParameter("status", status); 
			}
			sqlQuery.setParameter("ownerId", userId); 
			sqlQuery.addScalar("id",Hibernate.LONG);
			sqlQuery.addScalar("referenceId",Hibernate.LONG);
			sqlQuery.addScalar("status",Hibernate.SHORT);
			sqlQuery.addScalar("orderNo",Hibernate.STRING);
			sqlQuery.addScalar("openDate",Hibernate.TIMESTAMP);
			sqlQuery.addScalar("expirationDate",Hibernate.TIMESTAMP);
			sqlQuery.addScalar("applyDate",Hibernate.TIMESTAMP);
			sqlQuery.addScalar("applyRefundReason",Hibernate.STRING);
			sqlQuery.addScalar("rejectRefundReason",Hibernate.STRING);
			sqlQuery.addScalar("ownerEmail",Hibernate.STRING);
			sqlQuery.addScalar("refundDate",Hibernate.TIMESTAMP);
			sqlQuery.addScalar("updateDate",Hibernate.TIMESTAMP);
			sqlQuery.addScalar("refundAmount",Hibernate.BIG_DECIMAL);
			sqlQuery.addScalar("operator",Hibernate.STRING);
			sqlQuery.addScalar("refundType",Hibernate.SHORT);
			sqlQuery.addScalar("refundReasonType",Hibernate.SHORT);
			sqlQuery.addScalar("vmName",Hibernate.STRING);
			sqlQuery.addScalar("uuid",Hibernate.STRING);
			sqlQuery.addScalar("outerIp",Hibernate.STRING);
			sqlQuery.setResultTransformer(Transformers.aliasToBean(VmRefundLogVO.class));
			List<VmRefundLogVO> result=sqlQuery.setFirstResult(paging.getPageNo()*paging.getPageSize()-paging.getPageSize()).setMaxResults(paging.getPageSize()).list();
			return result;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public Long getVmRefundLogCount(Short status, String query,Long userId) throws HsCloudException {
		StringBuilder sql=new StringBuilder();
		sql.append(" select count(*) as totalCount from hc_vm_refund_log t1 ")
		   .append(",hc_vpdc_reference t2 where t1.referenceId=t2.id ");
		   if(StringUtils.isNotBlank(query)){
			   sql.append("and t2.name like :vmName "); 
		   }
		   if(status!=null&&status.shortValue()!=0){
			   sql.append("and t1.`status`= :status ");  
		   }
		   sql.append(" and t1.owner_id = :ownerId");
		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
		sqlQuery.setParameter("ownerId", userId); 
		if(StringUtils.isNotBlank(query)){
			sqlQuery.setParameter("vmName", "%"+query+"%");
		}
		if(status!=null&&status.shortValue()!=0){
			sqlQuery.setParameter("status", status); 
		}
		sqlQuery.addScalar("totalCount", Hibernate.LONG);
		List<Long> list = sqlQuery.list();
		return list.get(0);
	}

}

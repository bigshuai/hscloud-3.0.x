package com.hisoft.hscloud.bss.billing.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.dao.IncomingReportDao;
import com.hisoft.hscloud.bss.billing.dao.TranscationLogDao;
import com.hisoft.hscloud.bss.billing.entity.TranscationLog;
import com.hisoft.hscloud.bss.billing.service.TranscationLogService;
import com.hisoft.hscloud.bss.billing.vo.QueryVO;
import com.hisoft.hscloud.bss.billing.vo.TranscationLogVO;
import com.hisoft.hscloud.bss.billing.vo.VMResponsibility;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;


@Service
public class TranscationLogServiceImpl implements TranscationLogService {
	
	private	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private TranscationLogDao transcationLogDao;
	
	@Autowired
	private IncomingReportDao incomingReportDao;


	@Override
	public Page<TranscationLogVO> searchPermissionTranLog(List<Sort> sorts,
			Page<TranscationLogVO> page,String query, List<Object> primKeys) {
		Map<String,Object> map = new HashMap<String, Object>();
		
        if(logger.isDebugEnabled()){
        	logger.info("enter searchPermissionTranLog method");
        	logger.info("sorts"+sorts);
        	logger.info("page"+page);
        	logger.info("query"+query);
        	logger.info("primKeys"+primKeys);
        }
		
		
		StringBuffer sb  = new StringBuffer();
		sb.append("select htl.id,htl.flow,htl.gifts,htl.giftsBalance,htl.transcation_on transcationOn,htl.create_date createDate,htl.transcation_type transcationType,htl.amount amount,htl.balance balance,htl.coupons,htl.couponsBalance,htl.remark,htl.description,htl.orderId, (select orderNo from hc_order o where o.id=htl.orderId) orderNo,hu.email,hu.`name` username,hd.abbreviation,case when htl.operator_Type=0 THEN (select name from hc_admin admin where admin.id=htl.create_id ) when htl.operator_Type=1 THEN (select name from hc_user user where user.id=htl.create_id ) else ''  end operator from hc_transcation_log htl left join hc_account ha on htl.accountId=ha.id LEFT JOIN hc_user hu on hu.id=ha.user_id LEFT JOIN hc_domain hd on hu.domain_id=hd.id where 1=1");
		if(null != primKeys && primKeys.isEmpty()){
			return page;
		}else if(null != primKeys && !primKeys.isEmpty()){
			sb.append("  and htl.id in(:ids)");
			map.put("ids", primKeys);
		}
		if(null != query && !"".equals(query)){
			sb.append(" and ( hu.email like'%");
			sb.append(query).append("%' or htl.description like '%").append(query).append("%')");
		}
		return transcationLogDao.findPageBySQL(sorts, page,sb.toString(),map);
		
	}


	@Override
	public Page<TranscationLogVO> searchPermissionTranLog(List<Sort> sort,
			long id, Page<TranscationLogVO> pageLog, String query,
			List<Object> primKeys) {
		
		Map<String,Object> map = new HashMap<String, Object>();
        if(logger.isDebugEnabled()){
        	logger.info("enter searchPermissionTranLog method");
        	logger.info("sorts"+sort);
        	logger.info("page"+pageLog);
        	logger.info("query"+query);
        	logger.info("primKeys"+primKeys);
        }
		
		
		StringBuffer sb  = new StringBuffer();
		sb.append("select htl.id,htl.flow,htl.gifts,htl.giftsBalance,htl.transcation_on transcationOn,htl.create_date createDate,htl.transcation_type transcationType,htl.amount amount,htl.balance balance,htl.coupons,htl.couponsBalance,htl.remark,htl.description,htl.orderId, (select orderNo from hc_order o where o.id=htl.orderId) orderNo,hu.email,hu.`name` username,hd.abbreviation,case when htl.operator_Type=0 THEN (select name from hc_admin admin where admin.id=htl.create_id ) when htl.operator_Type=1 THEN (select name from hc_user user where user.id=htl.create_id ) else ''  end operator from hc_transcation_log htl left join hc_account ha on htl.accountId=ha.id LEFT JOIN hc_user hu on hu.id=ha.user_id LEFT JOIN hc_domain hd on hu.domain_id=hd.id where 1=1");
		if(null != primKeys && primKeys.isEmpty()){
			return pageLog;
		}else if(null != primKeys && !primKeys.isEmpty()){
			sb.append("  and htl.id in(:ids)");
			map.put("ids", primKeys);
		}
		if(null != query && !"".equals(query)){
//			sb.append(" and ( hu.email like'%");
//			sb.append(query).append("%' or htl.description like '%").append(query).append("%')");
			sb.append(" and htl.description like '%").append(query).append("%'");
		}
		if(0!=id){
			sb.append(" and ha.user_id=").append(id);
		}
		return transcationLogDao.findPageBySQL(sort, pageLog,sb.toString(),map);
	}


	@Override
	public Page<TranscationLogVO> findTransactionLog(
			Page<TranscationLogVO> page, QueryVO queryVO, List<Sort> sorts,
			List<Object> primKeys,List<Long> domains) throws HsCloudException {
		Map<String,Object> map = new HashMap<String, Object>();
		if(logger.isDebugEnabled()){
        	logger.info("enter searchPermissionTranLog method");
        	logger.info("sorts"+sorts);
        	logger.info("page"+page);        	
        	logger.info("primKeys"+primKeys);
        }		
		StringBuffer sb  = new StringBuffer();
		sb.append("select htl.id,htl.flow,htl.gifts,htl.giftsBalance,htl.transcation_on transcationOn,htl.create_date createDate,htl.transcation_type transcationType,htl.amount amount,htl.balance balance,htl.coupons,htl.couponsBalance,htl.remark,htl.description,htl.orderId, (select orderNo from hc_order o where o.id=htl.orderId) orderNo,hu.email,hu.`name` username,hd.abbreviation,case when htl.operator_Type=0 THEN (select name from hc_admin admin where admin.id=htl.create_id ) when htl.operator_Type=1 THEN (select name from hc_user user where user.id=htl.create_id ) else ''  end operator");
		sb.append(" from hc_transcation_log htl ");
		sb.append(" left join hc_account ha on htl.accountId=ha.id ");
		sb.append(" LEFT JOIN hc_user hu on hu.id=ha.user_id LEFT JOIN hc_domain hd on hd.id=hu.domain_id ");
		sb.append(" where ha.id not in (select account_id from hc_test_account where `status`=1) ");
		if(null != primKeys && primKeys.isEmpty()){
			return page;
		}else if(null != primKeys && !primKeys.isEmpty()){
			sb.append("  and htl.id in(:ids)");
			map.put("ids", primKeys);
		}
		if(null!= domains){
			sb.append(" and hu.domain_id in (:domainIds)");
			map.put("domainIds", domains);
		}
		if(queryVO !=null && null != queryVO.getPlatformid() && !"".equals(queryVO.getPlatformid())){
			Long  domainId = Long.valueOf(queryVO.getPlatformid());
			sb.append(" and hu.domain_id =(:domainId)");
			map.put("domainId", domainId);
		}
		if(queryVO!=null){
			if(queryVO.getTransactionType()>0){
				sb.append("  and htl.transcation_type =:transactionType ");
				map.put("transactionType", queryVO.getTransactionType());
			}
			if(queryVO.getEmail()!=null && !"".equals(queryVO.getEmail())){
				sb.append("  and hu.email like :email ");
				map.put("email", "%"+queryVO.getEmail()+"%");
			}
			if(queryVO.getAccountId()>0){
				sb.append(" and htl.accountId =:accountId");
				map.put("accountId", queryVO.getAccountId());
			}
//			if(queryVO.getStartTime()!=null && queryVO.getEndTime()!=null){
//				sb.append("  and htl.transcation_on between :startTime and :endTime ");
//				map.put("startTime", queryVO.getStartTime());
//				map.put("endTime", queryVO.getEndTime());
//			}
			if(queryVO.getMonth()!=null & !"".equals(queryVO.getMonth())){
				sb.append(" and DATE_FORMAT(htl.transcation_on,'%Y%m')=:month");
				map.put("month", queryVO.getMonth());
			}
			if(queryVO.getStartTime()!=null ){
//				sb.append("  and htl.transcation_on between :startTime and :endTime ");
//				map.put("startTime", queryVO.getStartTime());
//				map.put("endTime", queryVO.getEndTime());
				sb.append(" and htl.transcation_on>=:startTime");
				
				map.put("startTime", queryVO.getStartTime());
			}
			if(queryVO.getEndTime()!=null){
				sb.append(" and htl.transcation_on<:endTime");
//				Calendar ce = Calendar.getInstance();
//				ce.setTime(queryVO.getEndTime());
//				ce.add(Calendar.DAY_OF_MONTH, 1);
//				Date end =ce.getTime();
				map.put("endTime", queryVO.getEndTime());
			}
			if(queryVO.getFuzzy()!= null && !"".equals(queryVO.getFuzzy())){
				sb.append(" and ( hu.email like'%");
				sb.append(queryVO.getFuzzy()).append("%' or htl.description like '%").append(queryVO.getFuzzy()).append("%')");
			}
		}
		return transcationLogDao.findPageBySQL(sorts, page,sb.toString(),map);
	}


	@Override
	public List<TranscationLogVO> findTransactionLog(List<Long> domains,QueryVO queryVO,
			List<Object> primKeys) {
		Map<String,Object> map = new HashMap<String, Object>();
		if(logger.isDebugEnabled()){
        	logger.info("enter searchPermissionTranLog method");
        	logger.info("primKeys"+primKeys);
        }		
		StringBuffer sb  = new StringBuffer();
		sb.append("select htl.id,htl.flow,htl.gifts,htl.giftsBalance,htl.transcation_on transcationOn,htl.create_date createDate,htl.transcation_type transcationType,htl.amount amount,htl.balance balance,htl.coupons,htl.couponsBalance,htl.remark,htl.description,htl.orderId, (select orderNo from hc_order o where o.id=htl.orderId) orderNo,hu.email,hu.`name` username,hd.abbreviation,case when htl.operator_Type=0 THEN (select name from hc_admin admin where admin.id=htl.create_id ) when htl.operator_Type=1 THEN (select name from hc_user user where user.id=htl.create_id ) else ''  end operator ");
		sb.append(" from hc_transcation_log htl ");
		sb.append(" left join hc_account ha on htl.accountId=ha.id ");
		sb.append(" LEFT JOIN hc_user hu on hu.id=ha.user_id LEFT JOIN hc_domain hd on hd.id=hu.domain_id ");
		sb.append(" where ha.id not in (select account_id from hc_test_account where `status`=1)  ");
		if(null != primKeys && primKeys.isEmpty()){
			return new ArrayList<TranscationLogVO>();
		}else if(null != primKeys && !primKeys.isEmpty()){
			sb.append("  and htl.id in(:ids)");
			map.put("ids", primKeys);
		}
		if(null!= domains){
			sb.append(" and hu.domain_id in (:domainIds)");
			map.put("domainIds", domains);
		}
		if(queryVO !=null && null != queryVO.getPlatformid() && !"".equals(queryVO.getPlatformid())){
			Long  domainId = Long.valueOf(queryVO.getPlatformid());
			sb.append(" and hu.domain_id =(:domainId)");
			map.put("domainId", domainId);
		}
		if(queryVO!=null){
			if(queryVO.getTransactionType()>0){
				sb.append("  and htl.transcation_type =:transactionType ");
				map.put("transactionType", queryVO.getTransactionType());
			}
			if(queryVO.getEmail()!=null && !"".equals(queryVO.getEmail())){
				sb.append("  and hu.email =:email ");
				map.put("email", queryVO.getEmail());
			}
			if(queryVO.getStartTime()!=null ){
				sb.append(" and htl.transcation_on>=:startTime");
				map.put("startTime", queryVO.getStartTime());
			}
			if(queryVO.getEndTime()!=null){
				sb.append(" and htl.transcation_on<:endTime");
				map.put("endTime", queryVO.getEndTime());
			}
			if(queryVO.getFuzzy()!= null && !"".equals(queryVO.getFuzzy())){
				sb.append(" and ( hu.email like'%");
				sb.append(queryVO.getFuzzy()).append("%' or htl.description like '%").append(queryVO.getFuzzy()).append("%')");
			}
		}
		sb.append(" order by createDate DESC ");
		return transcationLogDao.findBySQL(sb.toString(), map);
	}
	
	@Override
	public List<TranscationLog> findTransactionLog(Long id) {
		Map<String,Object> map = new HashMap<String, Object>();		
		StringBuffer sb  = new StringBuffer();
		
		sb.append(" from TranscationLog where id="+id);
		
//		sb.append("select * ");
//		sb.append(" from hc_transcation_log htl ");
//		sb.append(" where htl.id ="+id);
		//sb.append(" order by createDate DESC ");
		return transcationLogDao.findByHQL(sb.toString(), map);
	}
	
//	@Override
//    public List<Resource> findTransactionLog(Long id){
//        List<Resource> resource = new ArrayList<Resource>();
//        Map<String,Object> map = new HashMap<String, Object>();
//        map.put("id", id);
//        resource = resourceDao.findByHQL("from Resource r where resourceType='"+resourceType+"' and primKey!=0 and r.ownerId in (:ownerIds)", map);
//        return resource;
//        
//    }

	@Override
	public List<TranscationLogVO> findTransactionLog(QueryVO queryVO) {
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sb  = new StringBuffer();
		sb.append("SELECT htl.id,htl.flow,htl.gifts,htl.giftsBalance,htl.orderId, htl.accountId, htl.transcation_on transcationOn, htl.transcation_type transcationType, htl.amount amount, htl.balance balance, htl.coupons, htl.couponsBalance, htl.remark, htl.description, hu.email, hu.`name` username, ( SELECT orderNo FROM hc_order o WHERE o.id = htl.orderId ) orderNo, ( SELECT abbreviation FROM hc_domain hd WHERE htl.domain_id = hd.id ) abbreviation, CASE WHEN operator_Type = 0 THEN ( SELECT NAME FROM hc_admin admin WHERE admin.id = htl.create_id ) WHEN operator_Type = 1 THEN ( SELECT NAME FROM hc_user USER WHERE USER .id = create_id ) ELSE '' END operator FROM hc_transcation_log htl LEFT JOIN hc_account ha ON htl.accountId = ha.id LEFT JOIN hc_user hu ON hu.id = ha.user_id WHERE htl.domain_id = ").append(queryVO.getDomainId());
		if(null != queryVO && !"".equals(queryVO.getMonth())){
			//String month = new StringBuffer(new StringBuffer(queryVO.getMonth()).reverse().toString().replaceFirst("[0-9]{1}", "1")).reverse().toString();
			String month = queryVO.getMonth().substring(0, 4)+"01";
			sb.append(" AND DATE_FORMAT(htl.transcation_on, '%Y%m') >= '").append(month).append("' AND DATE_FORMAT(htl.transcation_on, '%Y%m') <='").append(queryVO.getMonth()).append("'");
		}
		sb.append(" AND accountId NOT IN ( SELECT account_id FROM hc_test_account WHERE `status` = 1 )");
		sb.append(" ORDER BY htl.transcation_on desc");
		return transcationLogDao.findBySQL(sb.toString(), map);
	}

	@Override
	public List<VMResponsibility> findVMResponsibility(QueryVO queryVO) {
		StringBuffer sb = new StringBuffer();
		String month ="";
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(new SimpleDateFormat("yyyyMM").parse(queryVO.getMonth()));
			c.add(Calendar.MONTH, 1);
			month = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
        sb.append("SELECT hil.amount itemAmout,hvr.id refrenceId,his.current_incoming currentIncoming, his.finished_incoming finishedIncoming, his.nonevent_incoming noneventIncoming, his.email, htl.id transcationId, htl.transcation_type type, htl.transcation_on transcationOn, htl.amount, htl.description description, hil.orderNo orderNo, hil.effective_date startTime, hil.expiration_date endTime, TO_DAYS(hil.expiration_date) - TO_DAYS(hil.effective_date) orderDuration, CASE WHEN TO_DAYS('")
        .append(month).append("') - TO_DAYS(hil.expiration_date) > 0 THEN IF ( TO_DAYS(hil.expiration_date) - TO_DAYS(hil.effective_date) < 0, 0, TO_DAYS(hil.expiration_date) - TO_DAYS(hil.effective_date)) ELSE IF ( TO_DAYS('") .append(month)
        .append("') - TO_DAYS(hil.effective_date) < 0, 0, TO_DAYS('") .append(month).append("') - TO_DAYS(hil.effective_date)) END usedDuration, hvr.id, ( SELECT vm_id FROM hc_vpdc_instance WHERE VpdcRefrenceId = hvr.id GROUP BY VpdcRefrenceId ORDER BY id ) vmNo FROM hc_incoming_statis his LEFT JOIN hc_incoming_log hil ON hil.id = his.incoming_log_id LEFT JOIN hc_transcation_log htl ON htl.id = hil.transaction_id LEFT JOIN hc_vpdc_reference hvr ON hil.object_id = hvr.id WHERE htl.accountId NOT IN ( SELECT account_id FROM hc_test_account WHERE `status` = 1 ) AND his.product_type = 1 AND his.domain_id = ")
        .append(queryVO.getDomainId()).append(" AND his.`month` = '").append(queryVO.getMonth()).append("'").append(" ORDER BY his.object_id");
		return transcationLogDao.findVMResponsibility(sb.toString());
	}


	@Override
	public Page<VMResponsibility> findVMResponsibility(
			Page<VMResponsibility> page, QueryVO queryVO, List<Sort> sorts,
			List<Object> primKeys, List<Long> domains) throws HsCloudException {
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		String month ="";
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(new SimpleDateFormat("yyyyMM").parse(queryVO.getMonth()));
			c.add(Calendar.MONTH, 1);
			month = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
        sb.append("SELECT hil.amount itemAmout,hvr.id refrenceId,his.current_incoming currentIncoming,his.object_id objectId,his.order_item_id orderItemId,his.product_type productType, his.finished_incoming finishedIncoming, his.nonevent_incoming noneventIncoming, his.email, htl.id transcationId, htl.transcation_type type, htl.transcation_on transcationOn, htl.amount, htl.description description, hil.orderNo orderNo, hil.effective_date startTime, hil.expiration_date endTime, TO_DAYS(hil.expiration_date) - TO_DAYS(hil.effective_date) orderDuration, CASE WHEN TO_DAYS('")
        .append(month).append("') - TO_DAYS(hil.expiration_date) > 0 THEN IF ( TO_DAYS(hil.expiration_date) - TO_DAYS(hil.effective_date) < 0, 0, TO_DAYS(hil.expiration_date) - TO_DAYS(hil.effective_date)) ELSE IF ( TO_DAYS('") .append(month)
        .append("') - TO_DAYS(hil.effective_date) < 0, 0, TO_DAYS('") .append(month).append("') - TO_DAYS(hil.effective_date)) END usedDuration, hvr.id, ( SELECT vm_id FROM hc_vpdc_instance WHERE VpdcRefrenceId = hvr.id GROUP BY VpdcRefrenceId ORDER BY id ) vmNo FROM hc_incoming_statis his LEFT JOIN hc_incoming_log hil ON hil.id = his.incoming_log_id LEFT JOIN hc_transcation_log htl ON htl.id = hil.transaction_id LEFT JOIN hc_vpdc_reference hvr ON hil.object_id = hvr.id WHERE htl.accountId NOT IN ( SELECT account_id FROM hc_test_account WHERE `status` = 1 ) AND his.product_type = 1 AND his.domain_id = ")
        .append(queryVO.getPlatformid()).append(" AND his.`month` = '").append(queryVO.getMonth()).append("'");
        if(null != queryVO.getDomainIds() && !queryVO.getDomainIds().isEmpty()){
        	sb.append(" and his.domain_id in (:domainIds)");
			map.put("domainIds", queryVO.getDomainIds());
        }
        if(null != queryVO.getEmail() && !"".equals(queryVO.getEmail())){
        	sb.append(" and (his.email like '%").append(queryVO.getEmail()).append("%'").append(" or hil.orderNo like '%").append(queryVO.getEmail()).append("%')");
        }
        sb.append(" ORDER BY his.object_id ");
		return transcationLogDao.findVMResponsibilityPageBySQL(sorts, page,sb.toString(),map);
	}

}

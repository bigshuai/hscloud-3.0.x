package com.hisoft.hscloud.bss.billing.dao.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.bss.billing.constant.BillConstant;
import com.hisoft.hscloud.bss.billing.dao.ReportDao;
import com.hisoft.hscloud.bss.billing.entity.Report;
import com.hisoft.hscloud.bss.billing.vo.OtherResponsibility;
import com.hisoft.hscloud.bss.billing.vo.ResponsibilityIncoming;
import com.hisoft.hscloud.bss.billing.vo.Statistics;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;

@Repository
public class ReportDaoImpl extends HibernateDao<Report, Long> implements ReportDao{
	
	private Logger logger = Logger.getLogger(ReportDaoImpl.class);
	
	@Override
	public void save(List<Report> reports){
		for (Report report : reports) {
			super.save(report);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Report> report(String yearMonth) {
		try {
			StringBuffer sb  = new StringBuffer();
	     	//sb.append("SELECT report.`MONTH` yearMonth, report.domain_id domainId, report.consume, deposit_alipay alipay, deposit_oldPlatform oldPlatform, deposit_cash cash, deposit_cheque cheque, deposit_easyMoney easyMoney, deposit_eBank eBank, ( deposit_alipay + deposit_cash + deposit_cheque + deposit_easyMoney + deposit_eBank + deposit_oldPlatform ) deposit, ( deposit_alipay + deposit_cash + deposit_cheque + deposit_easyMoney + deposit_eBank + deposit_oldPlatform ) prepay, report.refund, report.draw, report.express expressResponsibility, report.machine machineResponsibility, report.other otherResponsibility, ( report.machine + report.express + report.other ) responsibility, report.pre_consume_balance + ABS(report.consume) - ABS(report.refund) - ABS( report.machine + report.express + report.other ) consumeBalance, report.pre_deposit_balance + ABS( deposit_alipay + deposit_cash + deposit_cheque + deposit_easyMoney + deposit_eBank + deposit_oldPlatform ) - ABS(report.draw) - ABS(report.consume) + ABS(report.refund) depositBalance, report.pre_consume_balance preConsumeBalance, report.pre_deposit_balance preDepositBalance, IFNULL( report.monthResponsibilityBalance, 0 ) monthResponsibilityBalance FROM ( SELECT l. MONTH, l.domain_id, min( CASE WHEN transcation_type = 1 THEN amount ELSE 0.00 END ) consume, max( CASE WHEN transcation_type = 2 AND payment_type = 1 THEN amount ELSE 0.00 END ) deposit_alipay, max( CASE WHEN transcation_type = 2 AND payment_type = 2 THEN amount ELSE 0.00 END ) deposit_oldPlatform, 0.00 deposit_easyMoney, 0.00 deposit_eBank, 0.00 deposit_cheque, 0.00 deposit_cash, max( CASE WHEN transcation_type = 3 THEN amount ELSE 0.00 END ) refund, min( CASE WHEN transcation_type = 4 THEN amount ELSE 0.00 END ) draw, IFNULL(re.consume_balance, 0) pre_consume_balance, IFNULL(re.deposit_balance, 0) pre_deposit_balance, r.machine, r.express, r.other, i.monthResponsibilityBalance FROM ( SELECT max( CASE WHEN product_type = 1 THEN currentamount ELSE 0.00 END ) machine, max( CASE WHEN product_type = 2 THEN currentamount ELSE 0.00 END ) express, max( CASE WHEN product_type = 3 THEN currentamount ELSE 0.00 END ) other, t.`MONTH`, t.domain_id FROM ( SELECT `month`, sum(current_incoming) currentamount, product_type, domain_id FROM hc_incoming_statis his WHERE his.`month` = '").append(yearMonth).append("' GROUP BY domain_id, product_type, `month` ) t GROUP BY domain_id, MONTH ) r LEFT JOIN ( SELECT transcation_type, payment_type, DATE_FORMAT(transcation_on, '%Y%m') MONTH, sum(amount) amount, accountId, domain_id FROM hc_transcation_log WHERE DATE_FORMAT(transcation_on, '%Y%m') = '").append(yearMonth).append("' AND accountId NOT IN ( SELECT account_id FROM hc_test_account WHERE `status` = 1 ) GROUP BY transcation_type, deposit_source, domain_id, DATE_FORMAT(transcation_on, '%Y%m')) l ON l.`month` = r.`month` AND l.domain_id = r.domain_id LEFT JOIN ( SELECT `month`, domain_id, SUM(nonevent_incoming) monthResponsibilityBalance FROM hc_incoming_statis WHERE `month` = '").append(yearMonth).append("' GROUP BY `month`, domain_id ) i ON i.domain_id = l.domain_id AND i.`month` = l.`MONTH` LEFT JOIN hc_report re ON re.domain_id = l.domain_id AND PERIOD_ADD(r.`month` ,- 1) = (re.`yearmonth`) GROUP BY domain_id, MONTH ) report");
	     	sb.append("SELECT NOW(),0,'',NOW(),0,0,'")
	     	.append(yearMonth)
	     	.append("' yearmonth,hd.id domainId,IFNULL(report.consume,0) consume,IFNULL(deposit_alipay,0) alipay,IFNULL(deposit_oldPlatform,0) oldPlatform,IFNULL(deposit_cash,0) cash,IFNULL(deposit_cheque,0) cheque,IFNULL(deposit_easyMoney,0) easyMoney,IFNULL(deposit_eBank,0) eBank,IFNULL((deposit_alipay + deposit_cash + deposit_cheque + deposit_easyMoney + deposit_eBank + deposit_oldPlatform),0) deposit,IFNULL((deposit_alipay + deposit_cash + deposit_cheque + deposit_easyMoney + deposit_eBank + deposit_oldPlatform),0) prepay,IFNULL(report.refund,0) refund,IFNULL(report.draw,0) draw,IFNULL(report.express,0) expressResponsibility,IFNULL(report.machine,0) machineResponsibility,IFNULL(report.other,0) otherResponsibility,IFNULL((report.machine + report.express + report.other),0) responsibility,IFNULL(report.pre_consume_balance + ABS(report.consume) - ABS(report.refund) - ABS(report.machine + report.express + report.other),0) consumeBalance,IFNULL(report.pre_deposit_balance + ABS(deposit_alipay + deposit_cash + deposit_cheque + deposit_easyMoney + deposit_eBank + deposit_oldPlatform ) - ABS(report.draw) - ABS(report.consume) + ABS(report.refund),0)depositBalance,IFNULL(report.pre_consume_balance,0) preConsumeBalance,IFNULL(report.pre_deposit_balance,0) preDepositBalance,IFNULL(report.monthResponsibilityBalance,0) monthResponsibilityBalance FROM (SELECT l. MONTH,l.domain_id,min(CASE WHEN transcation_type = 1 THEN amount ELSE 0.00 END) consume,max(CASE WHEN transcation_type = 2 AND deposit_source = 1 THEN amount ELSE 0.00 END) deposit_oldPlatform,max(CASE WHEN transcation_type = 2 AND deposit_source = 2 THEN amount ELSE 0.00 END) deposit_alipay,max(CASE WHEN transcation_type = 2 AND deposit_source = 3 THEN amount ELSE 0.00 END) deposit_easyMoney,max(CASE WHEN transcation_type = 2 AND deposit_source = 4 THEN amount ELSE 0.00 END) deposit_eBank,max(CASE WHEN transcation_type = 2 AND deposit_source = 6 THEN amount ELSE 0.00 END ) deposit_cheque,max(CASE WHEN transcation_type = 2 AND deposit_source = 5 THEN amount ELSE 0.00 END) deposit_cash,max(CASE WHEN transcation_type = 3 THEN amount ELSE 0.00 END) refund,min(CASE WHEN transcation_type = 4 THEN amount ELSE 0.00 END) draw,IFNULL(re.consume_balance, 0) pre_consume_balance,IFNULL(re.deposit_balance, 0) pre_deposit_balance,r.machine,r.express,r.other,i.monthResponsibilityBalance FROM (SELECT max(CASE WHEN product_type = 1 THEN currentamount ELSE 0.00 END) machine,max(CASE WHEN product_type = 2 THEN currentamount ELSE 0.00 END) express,max(CASE WHEN product_type = 3 THEN currentamount ELSE 0.00 END) other,t.`MONTH`,t.domain_id FROM (SELECT `month`,sum(current_incoming) currentamount,product_type,domain_id FROM hc_incoming_statis his WHERE his.`month` = '")
	     	.append(yearMonth)
	     	.append("' GROUP BY domain_id,product_type,`month`) t GROUP BY domain_id,MONTH) r RIGHT JOIN (SELECT transcation_type,deposit_source,DATE_FORMAT(transcation_on, '%Y%m') MONTH,sum(amount) amount,accountId,domain_id FROM hc_transcation_log WHERE DATE_FORMAT(transcation_on, '%Y%m') = '")
	     	.append(yearMonth)
	     	.append("' AND accountId NOT IN (SELECT account_id FROM hc_test_account WHERE `status` = 1) GROUP BY transcation_type,deposit_source,domain_id,DATE_FORMAT(transcation_on, '%Y%m')) l ON l.`month` = r.`month` AND l.domain_id = r.domain_id LEFT JOIN (SELECT`month`,domain_id,SUM(nonevent_incoming) monthResponsibilityBalance FROM hc_incoming_statis WHERE `month` = '")
	     	.append(yearMonth)
	     	.append("' GROUP BY `month`,domain_id) i ON i.domain_id = l.domain_id AND i.`month` = l.`MONTH` LEFT JOIN hc_report re ON re.domain_id = l.domain_id AND PERIOD_ADD(r.`month` ,- 1) = (re.`yearmonth`) GROUP BY domain_id,MONTH) report RIGHT JOIN hc_domain hd on report.domain_id=hd.id");
			SQLQuery query = getSession().createSQLQuery(sb.toString());
		    query.addScalar("yearMonth", Hibernate.STRING).addScalar("domainId", Hibernate.LONG).addScalar("deposit", Hibernate.BIG_DECIMAL)
		    .addScalar("draw", Hibernate.BIG_DECIMAL).addScalar("refund", Hibernate.BIG_DECIMAL).addScalar("depositBalance", Hibernate.BIG_DECIMAL)
		    .addScalar("consume", Hibernate.BIG_DECIMAL).addScalar("consumeBalance", Hibernate.BIG_DECIMAL).addScalar("responsibility", Hibernate.BIG_DECIMAL)
		    .addScalar("oldPlatform", Hibernate.BIG_DECIMAL).addScalar("alipay", Hibernate.BIG_DECIMAL).addScalar("easyMoney", Hibernate.BIG_DECIMAL)
		    .addScalar("eBank", Hibernate.BIG_DECIMAL).addScalar("cash", Hibernate.BIG_DECIMAL).addScalar("cheque", Hibernate.BIG_DECIMAL)
		    .addScalar("prepay", Hibernate.BIG_DECIMAL).addScalar("machineResponsibility", Hibernate.BIG_DECIMAL).addScalar("expressResponsibility", Hibernate.BIG_DECIMAL)
		    .addScalar("otherResponsibility", Hibernate.BIG_DECIMAL).addScalar("monthResponsibilityBalance", Hibernate.BIG_DECIMAL)
		    .addScalar("preConsumeBalance", Hibernate.BIG_DECIMAL).addScalar("preDepositBalance", Hibernate.BIG_DECIMAL);
		    query.setResultTransformer(Transformers.aliasToBean(Report.class));
		    return query.list();
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,e.getMessage(),logger, e);
		}
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ResponsibilityIncoming> findResponsibilityIncoming(String sql) {
		try {
			
		      SQLQuery query = getSession().createSQLQuery(sql);
		      query.addScalar("yearMonth", Hibernate.STRING)
		           .addScalar("consume", Hibernate.BIG_DECIMAL)
		           .addScalar("prepay", Hibernate.BIG_DECIMAL)
		           .addScalar("refund", Hibernate.BIG_DECIMAL)
		           .addScalar("drawCash", Hibernate.BIG_DECIMAL)
		           .addScalar("machine", Hibernate.BIG_DECIMAL)
		           .addScalar("express", Hibernate.BIG_DECIMAL)
		           .addScalar("other", Hibernate.BIG_DECIMAL)
		           .addScalar("monthResponsibilityBalance", Hibernate.BIG_DECIMAL);
		      query.setResultTransformer(Transformers.aliasToBean(ResponsibilityIncoming.class));
		      return query.list();
		      
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,e.getMessage(),logger, e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Statistics> findStatistics(String sql) {
		try {
			SQLQuery query = getSession().createSQLQuery(sql);
		    query.addScalar("yearMonth", Hibernate.STRING).addScalar("deposit", Hibernate.BIG_DECIMAL).addScalar("draw", Hibernate.BIG_DECIMAL).addScalar("refund", Hibernate.BIG_DECIMAL).addScalar("responsibility", Hibernate.BIG_DECIMAL).addScalar("depositBalance", Hibernate.BIG_DECIMAL).addScalar("consumeBalance", Hibernate.BIG_DECIMAL).addScalar("consume", Hibernate.BIG_DECIMAL);
		    query.setResultTransformer(Transformers.aliasToBean(Statistics.class));
		    return query.list();
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,e.getMessage(),logger, e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OtherResponsibility> findOtherResponsibility(String sql) {
		try {
			
		      SQLQuery query = getSession().createSQLQuery(sql);
		      query.addScalar("transcationId", Hibernate.STRING)
		           .addScalar("type", Hibernate.SHORT)
		           .addScalar("transcationOn", Hibernate.TIMESTAMP)
		           .addScalar("amount", Hibernate.BIG_DECIMAL)
		           .addScalar("currentIncoming", Hibernate.BIG_DECIMAL)
		           .addScalar("noneventIncoming", Hibernate.BIG_DECIMAL)
		           .addScalar("email", Hibernate.STRING)
		           .addScalar("description", Hibernate.STRING);
		      query.setResultTransformer(Transformers.aliasToBean(OtherResponsibility.class));
		      return query.list();
		      
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,e.getMessage(),logger, e);
		}
	}

	@Override
	public Page<ResponsibilityIncoming> findPageBySQL(List<Sort> sorts,
			Page<ResponsibilityIncoming> page, String sql,
			Map<String, Object> map) {

		try{
			
			if(logger.isDebugEnabled()){
				logger.info("enter findPageBySQL method.");
				logger.info("sorts"+sorts);
				logger.info("page"+page);
				logger.info("sql:"+sql);
				logger.info("map:"+map);
			}
//			Page<TranscationLogVO> propage = new Page<TranscationLogVO>();
//			propage.setPageNo(page.getPageNo());
//			propage.setOrder(page.getOrder());
//			propage.setOrderBy(page.getOrderBy());
			Long total = this.findCountBySQL(sql, map);
			if(logger.isDebugEnabled()){
				logger.info("propage:"+page);
				logger.info("total:"+total);
			}
			page.setTotalCount(total.intValue());
			StringBuffer sb = new StringBuffer(sql);
			for (int i = 0; i < sorts.size(); i++) {
				Sort sort = sorts.get(i);
				if (i == 0) {
					sb.append(" order by ");
				}
				sb.append(sort.getProperty() + " " + sort.getDirection());
	
				if (i < sorts.size() - 1) {
					sb.append(",");
				}
			}
			map.put("page", page);
			List<ResponsibilityIncoming> list = this.findBySQL(sb.toString(), map);
			page.setResult(list);
			if(logger.isDebugEnabled()){
				logger.info("sql:"+sb.toString());
				logger.info("propage:"+page);
				logger.info("exit findPageBySQL method.");
			}
			return page;
		
	    } catch (Exception e) {
		    throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,e.getMessage(),logger, e);
	    }
		
	}
	

	@Override
	@SuppressWarnings({"rawtypes","unchecked"})
	public Long findCountBySQL(String sql, Map<String, ?> map) {
		
		try {
			
			String count = "select COUNT(1) count  from ("+sql +") t";
			SQLQuery query = getSession().createSQLQuery(count);
			query.addScalar("count", Hibernate.LONG);
			Set<String> keys = map.keySet();
		      for (String key : keys) {
		    	  Object o = map.get(key);
		    	  if( o instanceof Collection){
		    		  query.setParameterList(key, (Collection)map.get(key));
		    	  }else{
		    		  query.setParameter(key, map.get(key));
		    	  }
			  }
			List<Long> list = query.list();
//			if(null == list || list.isEmpty()){
//				return 0l;
//			}else{
//				Map<String,Object> m = (Map<String,Object>)list.get(0);
//				return (Long)m.get("count");
//			}
			return list.get(0);
			
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,e.getMessage(),logger, e);
		}

	}
	
	@Override
	@SuppressWarnings({"rawtypes","unchecked"})
	public List<ResponsibilityIncoming> findBySQL(String sql, Map<String, ?> map) {
		
		try {
			
		      SQLQuery query = getSession().createSQLQuery(sql);
		      Set<String> keys = map.keySet();
		      query.addScalar("yearMonth", Hibernate.STRING)
		       .addScalar("abbreviation",Hibernate.STRING)
		       .addScalar("domainId",Hibernate.LONG)
	           .addScalar("consume", Hibernate.BIG_DECIMAL)
	           .addScalar("prepay", Hibernate.BIG_DECIMAL)
	           .addScalar("refund", Hibernate.BIG_DECIMAL)
	           .addScalar("drawCash", Hibernate.BIG_DECIMAL)
	           .addScalar("machine", Hibernate.BIG_DECIMAL)
	           .addScalar("express", Hibernate.BIG_DECIMAL)
	           .addScalar("other", Hibernate.BIG_DECIMAL)
	           .addScalar("monthResponsibilityBalance", Hibernate.BIG_DECIMAL);
		      
		      query.setResultTransformer(Transformers.aliasToBean(ResponsibilityIncoming.class));
		      for (String key : keys) {
		    	  Object o = map.get(key);
		    	  if( o instanceof Collection){
		    		  query.setParameterList(key, (Collection)map.get(key));
		    	  }else if(o instanceof Page){
		    		  query.setFirstResult((((Page) o).getPageNo()-1)*((Page) o).getPageSize());
		    		  query.setMaxResults(((Page) o).getPageSize());
		    	  }else{
		    		  query.setParameter(key, map.get(key));
		    	  }
			  }
		      return query.list();
		      
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,e.getMessage(),logger, e);
		}

	}

}

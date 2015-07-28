/* 
* 文 件 名:  PaymentDBConfigDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  houyh 
* 修改时间:  2012-12-19 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.payment.alipay.dao.impl; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.payment.alipay.dao.PaymentInterfaceDao;
import com.hisoft.hscloud.payment.alipay.entity.PaymentInterface;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  houyh 
 * @version  [版本号, 2012-12-19] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class PaymentInterfaceDaoImpl extends HibernateDao<PaymentInterface, Long>
		implements PaymentInterfaceDao {
	private static Logger logger=Logger.getLogger(PaymentInterfaceDaoImpl.class);
	/** 
	 * @param id
	 * @return
	 * @throws HsCloudException 
	 */
	@Override
	public PaymentInterface getPaymentConfigById(Long id)
			throws HsCloudException {
		try{
			return super.findUniqueBy("id",id);
		}catch(Exception e){
			throw new HsCloudException("",logger,e);
		}
	}

	@Override
	public Page<PaymentInterface> getAllPaymentInterfaceByPage(String query,Page<PaymentInterface> paging,List<Domain> domains)
			throws HsCloudException {
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("from PaymentInterface ");
			Map<String, Object> params = new HashMap<String, Object>();
			
			hql.append(" pi ").append(" where 1=1 ");
			if (StringUtils.isNotBlank(query)) {
						hql.append(" and  pi.domain.abbreviation like :domainName ");
				params.put("domainName", "%" + query + "%");
			}
			
			if(domains!=null&&domains.size()>0){
				hql.append(" and pi.domain in ( :domains ) ");
				params.put("domains", domains);
			}
			Page<PaymentInterface> result = super.findPage(paging, hql.toString(), params);
			return result;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}
	@Override
	public void addPaymentInterface(PaymentInterface config)
			throws HsCloudException {
		try {
			super.save(config);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
		
	}

	@Override
	public PaymentInterface getPaymentInterfaceByDomain(Domain domain)
			throws HsCloudException {
		try {
			PaymentInterface result = null;
			StringBuilder hql = new StringBuilder();
			Map<String, Domain> params = new HashMap<String, Domain>();
			hql.append("from PaymentInterface pi ");
			hql.append("where pi.domain = :domainObj");
			params.put("domainObj",domain);
			List<PaymentInterface> paymentResultList = super.find(hql.toString(), params);
			if(paymentResultList!=null&&paymentResultList.size()==1){
			   result=paymentResultList.get(0);	
			}
			return result;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public PaymentInterface getPaymentInterfaceByDomainId(long domainId)
			throws HsCloudException {
		try {
			PaymentInterface result = null;
			StringBuilder hql = new StringBuilder();
			Map<String, Long> params = new HashMap<String, Long>();
			hql.append("from PaymentInterface pi ");
			hql.append("where pi.domain.id = :domainId and pi.dataStatus=1 ");
			params.put("domainId",domainId);
			List<PaymentInterface> paymentResultList = super.find(hql.toString(), params);
			if(paymentResultList!=null&&paymentResultList.size()==1){
			   result=paymentResultList.get(0);	
			}
			return result;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}
	
	
	
}

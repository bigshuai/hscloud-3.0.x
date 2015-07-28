package com.hisoft.hscloud.bss.billing.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.bss.billing.dao.PaymentLogDao;
import com.hisoft.hscloud.bss.billing.entity.PaymentLog;
@Repository
public class PaymentLogDaoImpl extends HibernateDao<PaymentLog, Long> implements PaymentLogDao{
	
	public void save(PaymentLog paymentLog){
		super.save(paymentLog);
	}
	
	@SuppressWarnings("unchecked")
	public PaymentLog findUnique(String hql,Object ... values){
		return (PaymentLog)super.findUnique(hql, values);
	}

	@SuppressWarnings("unchecked")
	public List<PaymentLog> find(String hql,Object ... values){
		return super.find(hql, values);
	}
	
}

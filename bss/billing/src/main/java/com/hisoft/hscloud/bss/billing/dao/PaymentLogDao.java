package com.hisoft.hscloud.bss.billing.dao;

import java.util.List;

import com.hisoft.hscloud.bss.billing.entity.PaymentLog;

public interface PaymentLogDao {
	
	public void save(PaymentLog paymentLog);
	
	public PaymentLog findUnique(String hql, Object ... values);
	
	public List<PaymentLog> find(String hql,Object ... values);

}

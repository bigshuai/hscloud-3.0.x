package com.hisoft.hscloud.bss.billing.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.bss.billing.dao.PaymentLogDao;
import com.hisoft.hscloud.bss.billing.entity.PaymentLog;
import com.hisoft.hscloud.bss.billing.service.PaymentLogService;
@Service
public class PaymentLogServiceImpl implements PaymentLogService{
	@Autowired
	private PaymentLogDao paymentLogDao;

	@Override
	public PaymentLog searchPaymentLog(String serialNumber, short tradeStatus) {
		
		String hql = "from PaymentLog pl where pl.serialNumber=? and pl.tradeStatus=?";
		PaymentLog paymentLog = paymentLogDao.findUnique(hql, serialNumber,tradeStatus);
		return paymentLog;
		
	}
	
	@Override
	public void savePaymentLog(PaymentLog paymentLog){
		paymentLogDao.save(paymentLog);
	}

}

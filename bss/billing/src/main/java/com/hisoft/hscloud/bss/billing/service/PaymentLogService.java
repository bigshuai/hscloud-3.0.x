package com.hisoft.hscloud.bss.billing.service;

import com.hisoft.hscloud.bss.billing.entity.PaymentLog;

public interface PaymentLogService {
	
	public PaymentLog searchPaymentLog(String serialNumber,short tradeStatus);
	
	public void savePaymentLog(PaymentLog paymentLog);

}

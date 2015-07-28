package com.hisoft.hscloud.payment.alipay.service.impl; 

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.payment.alipay.config.AlipayConfig;
import com.hisoft.hscloud.payment.alipay.dao.PaymentInterfaceDao;
import com.hisoft.hscloud.payment.alipay.entity.PaymentInterface;
import com.hisoft.hscloud.payment.alipay.service.AlipayService;
import com.hisoft.hscloud.payment.alipay.util.AlipayNotify;
import com.hisoft.hscloud.payment.alipay.util.AlipaySubmit;
import com.hisoft.hscloud.payment.alipay.util.UtilDate;
@Service
public class AlipayServiceImpl implements AlipayService {
	private static Logger logger=Logger.getLogger(AlipayServiceImpl.class);
	@Autowired
	private PaymentInterfaceDao paymentConfigDao;
	@Override
	public String genRequestStrForPay(Map<String, String> sParaTemp,String key) throws HsCloudException {
		// 增加基本配置
		try {
			String returnStr = "";
			String strButtonName = "确认";
			returnStr = AlipaySubmit.buildForm(sParaTemp,
					AlipayConfig.ALIPAY_GATEWAY_NEW, "get", strButtonName,key);
			return returnStr;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public Map<String, String> verifyPayResponse(Map<String, String> sParaTemp,String key,String partner)
			throws HsCloudException {
		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		try {
			Map<String, String> resultMap = null;
			String order_no = sParaTemp.get("out_trade_no"); // 获取订单号
			String total_fee = sParaTemp.get("total_fee"); // 获取总金额
			String body = "";
			if (sParaTemp.get("body") != null) {
				body = sParaTemp.get("body");
			}
			String trade_status = sParaTemp.get("trade_status"); // 交易状态
			// 计算得出通知验证结果
			boolean verify_result = AlipayNotify.verify(sParaTemp,key,partner);
			if (verify_result) {// 验证成功
				if (trade_status.equals("TRADE_FINISHED")
						|| trade_status.equals("TRADE_SUCCESS")) {
					resultMap = new HashMap<String, String>();
					resultMap.put("total_fee", total_fee);
					resultMap.put("out_trade_no", order_no);
					resultMap.put("body",body);
				}
			}
			return resultMap;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}

	}

//	@Override
//	public Map<String, String> getRequestNeedParams(
//			Map<String, String> sParaTemp,long domainId) throws HsCloudException {
//		// 增加基本配置
//		try {
//			PaymentInterface config=paymentConfigDao.getPaymentInterfaceByDomainId(domainId);
//			sParaTemp.put("service", "create_direct_pay_by_user");
//			sParaTemp.put("partner", config.getPartner());
//			sParaTemp.put("return_url",Constants.PAYMENT_RETURN_URL);
//			sParaTemp.put("notify_url", Constants.PAYMENT_NOTIFY_URL);
//			sParaTemp.put("seller_email", config.getSellerEmail());
//			sParaTemp.put("_input_charset", AlipayConfig.input_charset);
//			sParaTemp.put("payment_type",AlipayConfig.payment_type);
//			sParaTemp.put("token", AlipayConfig.token);
//			sParaTemp.put("subject",AlipayConfig.subject);
//			sParaTemp.put("out_trade_no", UtilDate.getOrderNum());
//			return sParaTemp;
//		} catch (Exception e) {
//			throw new HsCloudException("", logger, e);
//		}
//	}

	@Override
	public Page<PaymentInterface> getAllPaymentInterfaceByPage(String query,Page<PaymentInterface> paging,List<Domain> domains)
			throws HsCloudException {
		return paymentConfigDao.getAllPaymentInterfaceByPage(query,paging,domains);
	}

	@Override
	public void saveOrUpdatePaymentInterface(PaymentInterface config)
			throws HsCloudException {
		if(config.getId()!=0L){
			config.setUpdateDate(new Date());
		}
		paymentConfigDao.addPaymentInterface(config);
		
	}

	@Override
	public PaymentInterface getPaymentInterfaceByDomain(Domain domain)
			throws HsCloudException {
		return paymentConfigDao.getPaymentInterfaceByDomain(domain);
	}

	@Override
	public PaymentInterface getPaymentInterfaceById(long id)
			throws HsCloudException {
		return paymentConfigDao.getPaymentConfigById(id);
	}

	@Override
	public PaymentInterface getPaymentInterfaceByDomain(long domainId)
			throws HsCloudException {
		return paymentConfigDao.getPaymentInterfaceByDomainId(domainId);
	}
	
	
}

package com.hisoft.hscloud.payment.alipay.service; 

import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.payment.alipay.entity.PaymentInterface;

public interface AlipayService {
	/**
	 * <支付宝立即到账支付请求> 
	* <功能详细描述> 
	* @param sParaTemp
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public String genRequestStrForPay(Map<String, String> sParaTemp,String key)throws HsCloudException;
	/**
	 * <校验支付宝支付返回结果，根据结果决定hscloud侧业务操作> 
	* <功能详细描述> 
	* @param sParaTemp
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Map<String, String> verifyPayResponse(Map<String, String> sParaTemp,String key,String partner)throws HsCloudException;
	/**
	 * <根据查询条件获取所有支付接口信息> 
	* <功能详细描述> 
	* @param query
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<PaymentInterface> getAllPaymentInterfaceByPage(String query,Page<PaymentInterface> paging,List<Domain> domains)throws HsCloudException;
	/**
	 * <新增或修改支付接口信息> 
	* <功能详细描述> 
	* @param config
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void saveOrUpdatePaymentInterface(PaymentInterface config)throws HsCloudException;
	/**
	 * <根据分平台查询支付接口用于校验分平台是否重复> 
	* <功能详细描述> 
	* @param domainId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public PaymentInterface getPaymentInterfaceByDomain(Domain domain)throws HsCloudException;
	/**
	 * <根据物理主键获取实体bean> 
	* <功能详细描述> 
	* @param id
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public PaymentInterface getPaymentInterfaceById(long id)throws HsCloudException;
	/**
	 * <根据分平台查询支付接口> 
	* <功能详细描述> 
	* @param domainId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public PaymentInterface getPaymentInterfaceByDomain(long domainId)throws HsCloudException;
}

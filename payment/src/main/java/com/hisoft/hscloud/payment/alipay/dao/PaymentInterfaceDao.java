package com.hisoft.hscloud.payment.alipay.dao; 

import java.util.List;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.payment.alipay.entity.PaymentInterface;

/**
 * 
* <一句话功能简述> 
* <功能详细描述> 
* 
* @author  houyh 
* @version  [版本号, 2012-12-19] 
* @see  [相关类/方法] 
* @since  [产品/模块版本]
 */
public interface PaymentInterfaceDao {
	/**
	 * <一句话功能简述> 
	* <功能详细描述> 
	* @param id
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public PaymentInterface getPaymentConfigById(Long id)throws HsCloudException;
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
	 * <新增支付接口信息> 
	* <功能详细描述> 
	* @param config
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void addPaymentInterface(PaymentInterface config)throws HsCloudException;
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
	 * <一句话功能简述> 
	* <功能详细描述> 
	* @param domainId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public PaymentInterface getPaymentInterfaceByDomainId(long domainId)throws HsCloudException;
}

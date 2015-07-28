/* 
 * 文 件 名:  PayOnlineAction.java 
 * 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
 * 描    述:  <描述> 
 * 修 改 人:  houyh 
 * 修改时间:  2013-3-13 
 * 跟踪单号:  <跟踪单号> 
 * 修改单号:  <修改单号> 
 * 修改内容:  <修改内容> 
 */
package com.hisoft.hscloud.web.action;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Utils;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.payment.alipay.entity.PaymentInterface;
import com.hisoft.hscloud.payment.alipay.vo.PaymentInterfaceVO;
import com.hisoft.hscloud.web.facade.Facade;

/**
 * <支付接口管理> <功能详细描述>
 * 
 * @author houyh
 * @version [版本号, 2013-3-13]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class PayOnlineAction extends HSCloudAction {
	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = 5827899162250113196L;
	int start;
	int limit;
	long id;
	String query;
	@Autowired
	Facade facade;
	private static Logger logger = Logger.getLogger(PayOnlineAction.class);

	/**
	 * <获取所有分平台的支付接口设置信息> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void getAllPaymentInterfaceByPage() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAllPaymentInterfaceByPage method.");			
		}
		try {
			Admin admin = (Admin)super.getCurrentLoginUser();
			if(admin!=null){
				if (StringUtils.isNotBlank(query)) {
					query = new String(query.getBytes("iso8859-1"), "utf8");
				}
				Page<PaymentInterface> paging = new Page<PaymentInterface>(limit);
				super.fillActionResult(facade.getAllPaymentInterfaceByPage(query,
						paging,admin));
			}
			
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"getAllPaymentInterfaceByPage Exception", logger, e),
					Constants.PAYMENT_INTERFACE_GET_BY_PAGE_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAllPaymentInterfaceByPage method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <一句话功能简述> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void createPaymentConfig() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter createPaymentConfig method.");			
		}
		Admin admin = null;
		try {
			admin=(Admin)super.getCurrentLoginUser();
			PaymentInterface config = Utils
					.strutsJson2Object(PaymentInterface.class);
			facade.createPaymentInterface(config);
			facade.insertOperationLog(admin,"创建支付接口","创建支付接口",Constants.RESULT_SUCESS);
		} catch (Exception e) {
			facade.insertOperationLog(admin,"创建支付接口错误："+e.getMessage(),"创建支付接口",Constants.RESULT_SUCESS);
			dealThrow(new HsCloudException("",
					"getAllPaymentInterfaceByPage Exception", logger, e),
					Constants.PAYMENT_INTERFACE_CREATE_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit createPaymentConfig method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <一句话功能简述> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void disablePaymentConfig() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter disablePaymentConfig method.");			
		}
		Admin admin=null;
				
		try {
			admin=(Admin)super.getCurrentLoginUser();
			facade.disablePaymentInterface(id);
			facade.insertOperationLog(admin,"禁用支付接口","禁用支付接口",Constants.RESULT_SUCESS);
		} catch (Exception e) {
			facade.insertOperationLog(admin,"禁用支付接口错误："+e.getMessage(),"禁用支付接口",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException("",
					"getAllPaymentInterfaceByPage Exception", logger, e),
					Constants.PAYMENT_INTERFACE_DISABLE_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit disablePaymentConfig method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <一句话功能简述> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void enablePaymentConfig() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter enablePaymentConfig method.");			
		}
		Admin admin=null;
		try {
			admin=(Admin)super.getCurrentLoginUser();
			facade.enablePaymentInterface(id);
			facade.insertOperationLog(admin,"启用支付接口","启用支付接口",Constants.RESULT_SUCESS);
		} catch (Exception e) {
			facade.insertOperationLog(admin,"启用支付接口错误:"+e.getMessage(),"启用支付接口",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException("",
					"getAllPaymentInterfaceByPage Exception", logger, e),
					Constants.PAYMENT_INTERFACE_ENABLE_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit enablePaymentConfig method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <一句话功能简述> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void modifyPaymentConfig() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter modifyPaymentConfig method.");			
		}
		Admin admin=null;
		try {
			admin=(Admin)super.getCurrentLoginUser();
			PaymentInterfaceVO pio=Utils.strutsJson2Object(PaymentInterfaceVO.class);
			facade.modifyPaymentInterface(pio);
			facade.insertOperationLog(admin,"修改支付接口","修改支付接口",Constants.RESULT_SUCESS);
		} catch (Exception e) {
			facade.insertOperationLog(admin,"修改支付接口错误："+e.getMessage(),"修改支付接口",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException("",
					"getAllPaymentInterfaceByPage Exception", logger, e),
					Constants.PAYMENT_INTERFACE_MODIFY_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit modifyPaymentConfig method.takeTime:" + takeTime + "ms");
		}
	}
	
	public void checkDomainDup(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter checkDomainDup method.");			
		}
		try {
			boolean result=false;
			Domain domain=Utils.strutsJson2Object(Domain.class);
			result=facade.getPaymentInterfaceByDomain(domain);
			super.getActionResult().setResultObject(result);
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"getAllPaymentInterfaceByPage Exception", logger, e),
					Constants.PAYMENT_INTERFACE_CHECK_DUP_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit checkDomainDup method.takeTime:" + takeTime + "ms");
		}
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}

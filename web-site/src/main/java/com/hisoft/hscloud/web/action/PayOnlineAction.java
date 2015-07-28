/* 
 * 文 件 名:  PayAction.java 
 * 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
 * 描    述:  <描述> 
 * 修 改 人:  houyh 
 * 修改时间:  2012-12-13 
 * 跟踪单号:  <跟踪单号> 
 * 修改单号:  <修改单号> 
 * 修改内容:  <修改内容> 
 */
package com.hisoft.hscloud.web.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudDateUtil;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.web.facade.Facade;
import com.opensymphony.xwork2.ActionSupport;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author houyh
 * @version [版本号, 2012-12-13]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class PayOnlineAction extends ActionSupport {
	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = 1091228042645860708L;
	private String total_fee;
	private String defaultbank;
	private static Logger logger = Logger.getLogger(PayOnlineAction.class);
	@Autowired
	private Facade facade;
	/**
	 * <进行支付请求操作> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void alipayDirectPay() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter alipayDirectPay method.");			
		}
		try {
			ServletActionContext.getResponse().setContentType("text/html");
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			String userEmail = "";
			String requestStr = "";
			StringBuilder body = new StringBuilder();
			boolean result = false;
			if (session != null) {
				Object obj = session.getAttribute(Constants.LOGIN_CURRENTUSER);
				if (obj != null && (obj instanceof User)) {
					User user = (User) obj;
					userEmail = user.getEmail();
					Long userId = user.getId();
					long domainId=user.getDomain().getId();
					String paymethod = "";
					Map<String, String> params = new HashMap<String, String>();
					if (StringUtils.isNotBlank(defaultbank)
							&& !"directPay".equals(defaultbank)) {
						paymethod = "bankPay";
						params.put("defaultbank", defaultbank);
					} else {
						paymethod = "directPay";
						params.put("defaultbank", "");
					}
					body.append("用户：").append(userEmail).append(" 于")
							.append(HsCloudDateUtil.getNowStr()).append("，充值：")
							.append(total_fee).append("元");
					params.put("paymethod", paymethod);
					params.put("total_fee", total_fee);
					params.put("body", body.toString());
					requestStr = facade.genAlipayPayRequest(params, userId,domainId);
					result = true;
				}

			}
			if (result) {
				ServletActionContext.getResponse().getWriter()
						.print(requestStr);
			} else {
				ServletActionContext.getResponse().getWriter().print("failure");
			}
		} catch (Exception e) {
			try {
				ServletActionContext.getResponse().getWriter().print("failure");
			} catch (IOException e1) {
				logger.debug("alipayDirectPay exception", e1);
			}
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit alipayDirectPay method.takeTime:" + takeTime + "ms");
		}

	}
	/**
	 * <校验支付宝同步响应结果> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String verifyAlipayResponse() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter verifyAlipayResponse method.");			
		}
		try {
			Map<String, String> params = new HashMap<String, String>();
			boolean result=false;
			Map requestParams = ServletActionContext.getRequest()
					.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter
					.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
				params.put(name, valueStr);
			}
			result = facade.verifyAlipayResponse(params);
			if(logger.isDebugEnabled()){
				long takeTime = System.currentTimeMillis() - beginRunTime;
				logger.debug("exit verifyAlipayResponse method.takeTime:" + takeTime + "ms");
			}
			if (result) {
				return "success";
			} else {
				return "failure";
			}			
		} catch (Exception e) {
			logger.error("verifyAlipayResponse error", e);
			return "failure";
		}		
	}
	/**
	 * <校验支付宝异步响应结果> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void verifyAlipayAsynResponse() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter verifyAlipayAsynResponse method.");			
		}
		try {
			Map<String, String> params = new HashMap<String, String>();
			boolean result = false;
			Map requestParams = ServletActionContext.getRequest()
					.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter
					.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
				params.put(name, valueStr);
			}
			result = facade.verifyAlipayAsynResponse(params);
			ServletActionContext.getResponse().setContentType("text/html");
			if (result) {
				ServletActionContext.getResponse().getWriter().println("success");
			} else {
				ServletActionContext.getResponse().getWriter().println("failure");
			}
		} catch (Exception e) {
			logger.error("alipay asyn response error", e);
			try {
				ServletActionContext.getResponse().getWriter().println("failure");
			} catch (IOException e1) {
				logger.error("alipay asyn response error", e1);
			}
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit verifyAlipayAsynResponse method.takeTime:" + takeTime + "ms");
		}
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getDefaultbank() {
		return defaultbank;
	}

	public void setDefaultbank(String defaultbank) {
		this.defaultbank = defaultbank;
	}

}

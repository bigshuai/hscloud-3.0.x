/**
 * -----------------------------------------------------------------
 * Copyright (c) 2012 hisoft, All Rights Reserved.
 * This software is the proprietary information of hisoft.
 * Use is subject to strict licensing terms.
 * -----------------------------------------------------------------
 *
 * @author: Administrator 2013-4-19   上午10:25:57
 * @brief: 类功能描述
 * @log: Administrator
 */

package com.hisoft.hscloud.web.controller;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openstack.model.compute.nova.NovaServerForCreate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hisoft.hscloud.common.entity.HcEventResource;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudDateUtil;
import com.hisoft.hscloud.common.util.PasswordUtil;
import com.hisoft.hscloud.common.util.RabbitMQUtil;
import com.hisoft.hscloud.common.util.SecurityHelper;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.service.UserService;
import com.hisoft.hscloud.web.dao.RestAccessAccountDao;
import com.hisoft.hscloud.web.service.BasicService;
import com.hisoft.hscloud.web.service.OrderPlan4RestService;
import com.hisoft.hscloud.web.util.BasicUtil;
import com.hisoft.hscloud.web.vo.OrderPlanVo;
import com.hisoft.hscloud.web.vo.OrderResultVo;

@Controller
@Scope("prototype")
@RequestMapping("/order_sc")
public class OrderPlan4RestController {
	/**
	 * @author: Ruan
	 * @brief: 类功能描述
	 * @log: 2013-4-19 上午10:25:57
	 **/
	@Resource
	private OrderPlan4RestService orderPlan4RestService;

	@Resource
	private BasicService basicService;

	@Resource
	private RestAccessAccountDao restAccessAccountDao;
	@Resource
	private UserService userService;

	private Logger logger = Logger.getLogger(this.getClass());

	private OrderResultVo orderResultVo = new OrderResultVo();

	private String user_id;
	private User user = null;
	private String email;

	/**
	 * <套餐购买-直接付费(大客户)> <功能详细描述>
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	// http://127.0.0.1:8080/hscloud-restful/services/order_sc?accessid=10001&accesskey=113c3ceff59e071084390a9c2e0845b1&scid=1&osid=10&osname=1&paymode=1&num=2
	public void OrderPlan4Rest(@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws Exception {

		logger.info("hscloud Restful order plans by user starting......");

		orderResultVo = new OrderResultVo();

		/** 1、获取参数 **/
		String accessIp = request.getRemoteHost();// 获取请求Ip
		String accessId = request.getParameter("accessid");// 鉴权用户
		String accessKey = request.getParameter("accesskey");// 鉴权密码 MD5加密
		String scId = request.getParameter("scid");// 套餐序列号
		String osId = request.getParameter("osid");// 操作系统ID
		String payMode_period = request.getParameter("paymode");// 支付方式：1：月付
																// 3：季付 6：半年付
																// 12：年付
		String num = request.getParameter("num");// 购买套餐数量

		String doUseCoupon = request.getParameter("useCoupon"); // 是否使用返点
		if (!"true".equals(doUseCoupon)) {
			doUseCoupon = "false";
		}
		/*
		 * if(StringUtils.isBlank(doUseCoupon)) { doUseCoupon = "false"; }
		 */

		String doUseGift = request.getParameter("useGift"); // 是否使用返点
		if (!"true".equals(doUseGift)) {
			doUseGift = "false";
		}
		/*
		 * if(StringUtils.isBlank(doUseGift)) { doUseGift = "false"; }
		 */

		boolean flag = checkParameter(accessId, accessKey, accessIp);
		if (flag != true) {
			BasicUtil.printResult(response, orderResultVo);
			return;
		}

		/** 3、执行套餐的订购操作 **/
		logger.info(" ip is valid......");
		OrderPlanVo orderPlanVos = new OrderPlanVo();
		// **********************lihonglei start************
		Map<String, Object> map = orderPlan4RestService
				.queryAccessByAccessId(accessId);

		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			int numInt = Integer.parseInt(num);
			if (numInt > 10) {
				orderResultVo = BasicUtil.getOrderResultVo(false,
						"num can not be more than ten");
				BasicUtil.printResult(response, orderResultVo);
				return;
			}
			resultMap = orderPlan4RestService.scBuyDirect(
					Integer.parseInt(scId), Integer.parseInt(osId),
					payMode_period, numInt, Long.parseLong(user_id),
					orderPlanVos, (String) map.get("id"), accessId,
					doUseCoupon, doUseGift);
		} catch (Exception ex) {
			if (ex.getMessage() != null) {
				resultMap.put("log", ex.getMessage());
			} else {
				resultMap.put("log", "throw a exception when buy a vm");
			}
		}
		// **********************lihonglei end************

		if (!resultMap.isEmpty() && resultMap.containsKey("log")) {
			String log = (String) resultMap.get("log");
			orderResultVo = BasicUtil.getOrderResultVo(false, log);
			BasicUtil.printResult(response, orderResultVo);
			return;
		} else {
			// **********************lihonglei star 2013-05-23************
			String result = "";
			for (String str : resultMap.keySet()) {
				result = str;
			}
			// 消息在事务提交后发送
			@SuppressWarnings("unchecked")
			Map<NovaServerForCreate, HcEventResource> rabbitMQ = (Map<NovaServerForCreate, HcEventResource>) resultMap
					.get(result);
			RabbitMQUtil rabbitMqUtil = new RabbitMQUtil();
			for (NovaServerForCreate nsfc : rabbitMQ.keySet()) {
				HcEventResource her = rabbitMQ.get(nsfc);
				orderPlanVos.getVmList().add(her.getObj_id());
				rabbitMqUtil.sendMessage(her.getMessage(), nsfc, her,
						"HcEventResource");
			}
			// **********************lihonglei end 2013-05-23************
			orderResultVo.setSuccess(true);
			String json = new JSONArray().fromObject(orderPlanVos).toString();
			orderResultVo.setResultObject(json);
			BasicUtil.printResult(response, orderResultVo);
		}

		/** 4、日志的记录：log4j或者记录到数据库 **/
		logger.info("hscloud  Restful order plans by user end");
	}

	/**
	 * <套餐购买-待付费> <功能详细描述>
	 * 
	 * @param sccode
	 * @param request
	 * @param response
	 * @throws Exception
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings({ "deprecation" })
	@RequestMapping(value = "/{sccode}", method = RequestMethod.GET)
	@ResponseBody
	// http://127.0.0.1:8080/hscloud-restful/services/order_sc?accessid=10001&accesskey=113c3ceff59e071084390a9c2e0845b1&scid=1&osid=10&osname=1&paymode=1&num=2
	public void OrderPlanApply4Rest(@PathVariable String sccode,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws Exception {

		logger.info("hscloud Restful order plans by user starting......");

		orderResultVo = new OrderResultVo();

		/** 1、获取参数 **/
		String accessIp = request.getRemoteHost();// 获取请求Ip
		String accessId = request.getParameter("accessid");// 鉴权用户
		String accessKey = request.getParameter("accesskey");// 鉴权密码 MD5加密
		// String scId = request.getParameter("scid");//套餐序列号
		String osId = request.getParameter("osid");// 操作系统ID
		String payMode_period = request.getParameter("paymode");// 支付方式：1：月付
																// 3：季付 6：半年付
																// 12：年付
		String num = request.getParameter("num");// 购买套餐数量

		String doUseCoupon = request.getParameter("useCoupon"); // 是否使用返点
		if (!"true".equals(doUseCoupon)) {
			doUseCoupon = "false";
		}
		/*
		 * if(StringUtils.isBlank(doUseCoupon)) { doUseCoupon = "false"; }
		 */

		String doUseGift = request.getParameter("useGift"); // 是否使用返点
		if (!"true".equals(doUseGift)) {
			doUseGift = "false";
		}
		/*
		 * if(StringUtils.isBlank(doUseGift)) { doUseGift = "false"; }
		 */

		// boolean flag = checkParameter(accessId, accessKey, accessIp);
		boolean flag = basicService.checkParameterOfDomainWithCode(response,
				accessId, accessKey, accessIp, sccode);
		if (flag != true) {
			BasicUtil.printResult(response, orderResultVo);
			return;
		}		
		/** 3、执行套餐的订购操作 **/
		logger.info(" ip is valid......");
		OrderPlanVo orderPlanVos = new OrderPlanVo();
		// **********************lihonglei start************
		Map<String, Object> map = basicService
				.queryAccessByAccessId(accessId);
		if(map !=null){
			email =(String) map.get("emailaddr");
		}
		if("".equals(email) || email == null){
			orderResultVo = BasicUtil.getOrderResultVo(false,
					"user is not exist!");
			BasicUtil.printResult(response, orderResultVo);
			return;
		}
		user = basicService.checkUser(response, email);
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			int numInt = Integer.parseInt(num);
			if (numInt > 10) {
				orderResultVo = BasicUtil.getOrderResultVo(false,
						"num can not be more than ten");
				BasicUtil.printResult(response, orderResultVo);
				return;
			}
			resultMap = orderPlan4RestService.applyOrderPlan(sccode,
					Integer.parseInt(osId), payMode_period, numInt,user.getId(), orderPlanVos,
					(String) map.get("id"), accessId, doUseCoupon, doUseGift);
		} catch (Exception ex) {
			if (ex.getMessage() != null) {
				resultMap.put("log", ex.getMessage());
			} else {
				resultMap.put("log", "throw a exception when buy a vm");
			}
		}
		// **********************lihonglei end************

		if (!resultMap.isEmpty() && resultMap.containsKey("log")) {
			String log = (String) resultMap.get("log");
			orderResultVo = BasicUtil.getOrderResultVo(false, log);
			BasicUtil.printResult(response, orderResultVo);
			return;
		} else {
			String originalKey = user.getEmail() + user.getPassword();
			String authorKey;
			try {
				authorKey = PasswordUtil.getEncyptedPasswd(originalKey);
			} catch (Exception e) {
				logger.info("UserRegisterController.registerUser", e);
				BasicUtil.fillResultVoFalse(e.getMessage(), response);
				return;
			}
			String dateKey = SecurityHelper
					.EncryptData(HsCloudDateUtil.getNowStr(),
							Constants.DEFAULT_SECURITY_KEY);
			dateKey = URLEncoder.encode(dateKey);
			String domainPublishUrl = user.getDomain().getPublishingAddress();
			String appUrl = domainPublishUrl
					+ "/user_mgmt/paymentByUrl!userLoginForPaymentByUrl.action?";
			String result = appUrl + "authorKey=" + authorKey + "&userId="
					+ user.getId() + "&dateKey=" + dateKey + "&operator="
					+ user.getEmail();
			BasicUtil.printResult(response, result);
		}

		/** 4、日志的记录：log4j或者记录到数据库 **/
		logger.info("hscloud  Restful order plans by user end");
	}

	private boolean checkParameter(String accessId, String accessKey,
			String accessIp) {
		if (accessId != null && !"".equals(accessId)) {
			user_id = orderPlan4RestService.getUserId(accessId);
		}
		if (user_id == null) {
			orderResultVo = BasicUtil.getOrderResultVo(false,
					"userId is invalid");
			return false;
		}
		user = userService.getUser(Long.valueOf(user_id));
		if (user == null) {
			orderResultVo = BasicUtil.getOrderResultVo(false,
					"user is not exist!");
			return false;
		}
		String access_key = restAccessAccountDao.getAccessKey(accessId);
		if (!access_key.equalsIgnoreCase(accessKey)) {
			orderResultVo = BasicUtil.getOrderResultVo(false,
					"AccessKey is invalid");
			return false;
		}
		boolean ipIsValid = basicService.checkIP(accessIp, accessId);
		if (ipIsValid != true) {
			orderResultVo = BasicUtil.getOrderResultVo(false,
					"ip is not correct!");
			return false;
		}
		return true;
	}
}

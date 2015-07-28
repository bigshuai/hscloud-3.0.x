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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hisoft.hscloud.web.util.BasicUtil;
import com.hisoft.hscloud.web.util.MD5Util;
import com.hisoft.hscloud.web.vo.OrderResultVo;


@Controller
@Scope("prototype")
public class AccessKeyController {
	/**
	 * @author: Ruan
	 * @brief: 类功能描述
	 * @log: 2013-4-19 上午10:25:57
	 **/
	private Logger logger = Logger.getLogger(this.getClass());
	
    /*http://115.182.68.160:8080/hscloud-restful/services/get_access_key?accessid=10001&email=fll@126.com&facSeq=aadfafafafasf*/
	@RequestMapping(value = "/get_access_key", method = RequestMethod.GET)
	@ResponseBody
	public void  OrderPlan4Rest(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		
		OrderResultVo orderResultVo=new OrderResultVo();
		logger.info("hscloud Restful get accesskey starting......");
		/**1、获取参数**/
		String accessId = request.getParameter("accessid");//鉴权用户
		String email = request.getParameter("email");//套餐序列号
		String facSeq = request.getParameter("facSeq");//操作系统ID
		/**2、生成accesskey**/
		String str = "%s%s%s";
		String access_key=null;
		try {
			if(accessId!=null&&email!=null&&facSeq!=null){
				access_key = MD5Util.encode(String.format(str, accessId, email, facSeq));
				orderResultVo.setSuccess(true);
				orderResultVo.setResultObject("AccessKey:"+access_key);
			}else{
				orderResultVo = BasicUtil.getOrderResultVo(false, "param num is not correct!");
			}
			
			BasicUtil.printResult(response,orderResultVo);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		logger.info("hscloud  Restful order plans by user end");

	}
	
}

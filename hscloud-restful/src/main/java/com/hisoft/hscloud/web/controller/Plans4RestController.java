/**
 * -----------------------------------------------------------------
 * Copyright (c) 2012 hisoft, All Rights Reserved.
 * This software is the proprietary information of hisoft.
 * Use is subject to strict licensing terms.
 * -----------------------------------------------------------------
 *
 * @author: Administrator 2013-4-18   下午3:39:42
 * @brief: 类功能描述
 * @log: Administrator
 */

package com.hisoft.hscloud.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hisoft.hscloud.web.dao.RestAccessAccountDao;
import com.hisoft.hscloud.web.service.BasicService;
import com.hisoft.hscloud.web.service.OrderPlan4RestService;
import com.hisoft.hscloud.web.service.Plans4RestService;
import com.hisoft.hscloud.web.util.BasicUtil;
import com.hisoft.hscloud.web.util.JsonUtil;
import com.hisoft.hscloud.web.vo.OrderResultVo;
import com.hisoft.hscloud.web.vo.PlansInfoVo;

@Controller
public class Plans4RestController {
	/**
	 * @author: Administrator
	 * @brief: 类功能描述
	 * @log: 2013-4-18 下午3:39:42
	 **/

	@Resource
	private Plans4RestService plans4RestService;
	
	@Resource
	private OrderPlan4RestService orderPlan4RestService;
	
	@Resource
	private RestAccessAccountDao restAccessAccountDao;
	
	@Resource
    private BasicService basicService;
	
	private String user_id;
	
	private OrderResultVo orderResultVo = new OrderResultVo();

	private Logger logger = Logger.getLogger(this.getClass());

	//http://localhost:8080/hscloud-restful/services/query_scinfo?accessid=10001&accesskey=61b3e340c50fcec226a673cb350c6aef&id=147
	@RequestMapping(value = "/query_scinfo", method = RequestMethod.GET)
	@ResponseBody
	public void getPlans4RestByUser(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		
		logger.info("hscloud Restful query plan info by user starting......");
		
		orderResultVo=new OrderResultVo();
		
		String vmId = request.getParameter("id");
		String accessId = request.getParameter("accessid");//鉴权用户
		String accessKey = request.getParameter("accesskey");//鉴权密码  MD5加密
		
		String accessIp=request.getRemoteHost();//获取请求Ip
		
		boolean flag = checkParameter(accessId, accessKey, accessIp);
        if(flag != true) {
            BasicUtil.printResult(response,orderResultVo);
            return;
        }
		
		List<PlansInfoVo> list = null;
		String result = null;
				list = plans4RestService.getPlans4RestByUser(user_id, vmId);
		
		if(list != null){
			result = JsonUtil.listToJson(list);
			orderResultVo.setSuccess(true);
			orderResultVo.setResultObject(result);
		} else {
		    orderResultVo.setSuccess(false);
		}
		BasicUtil.printResult(response, orderResultVo);
		logger.info("hscloud Restful query plan info by user end");

	}
	
	private boolean checkParameter(String accessId, String accessKey, String accessIp) {
        if(accessId!=null&&!"".equals(accessId)){
            user_id=orderPlan4RestService.getUserId(accessId);
        }
        if(user_id==null){
            orderResultVo = BasicUtil.getOrderResultVo(false,"userId is invalid");
            return false;
        }
        String  access_key=restAccessAccountDao.getAccessKey(accessId);
        if(!access_key.equalsIgnoreCase(accessKey)){
            orderResultVo = BasicUtil.getOrderResultVo(false, "AccessKey is invalid");
            return false;
        }
        boolean ipIsValid = basicService.checkIP(accessIp, accessId);
        if(ipIsValid != true){
            orderResultVo = BasicUtil.getOrderResultVo(false, "ip is not correct!");
            return false;
        }
        return true;
    }
}

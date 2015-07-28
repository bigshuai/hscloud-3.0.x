/**
 * -----------------------------------------------------------------
 * Copyright (c) 2012 hisoft, All Rights Reserved.
 * This software is the proprietary information of hisoft.
 * Use is subject to strict licensing terms.
 * -----------------------------------------------------------------
 *
 * @author: hisoft 2013-1-29   上午10:17:10
 * @brief: 类功能描述
 * @log: hisoft
 */

package com.hisoft.hscloud.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.hisoft.hscloud.web.service.VMInfoService;
import com.hisoft.hscloud.web.util.BasicUtil;
import com.hisoft.hscloud.web.util.JsonUtil;
import com.hisoft.hscloud.web.vo.OrderResultVo;
import com.hisoft.hscloud.web.vo.VMInfoVo;

@Controller
public class VMInfoController {
	/**
	 * @author: hisoft
	 * @brief: 
	 * @log: 2013-1-29 上午10:17:10
	 **/

	@Resource
	private VMInfoService vmInfoService;
	@Resource
	private OrderPlan4RestService orderPlan4RestService;
	@Resource
	private RestAccessAccountDao restAccessAccountDao;
	
	@Resource
	private BasicService basicService; 

	private Logger logger = Logger.getLogger(this.getClass());
	
	private OrderResultVo orderResultVo=new OrderResultVo();
	
	private String user_id;
	
	//http://127.0.0.1:8080/hscloud-restful/services/query_vm_info?accessid=10001&accesskey=61b3e340c50fcec226a673cb350c6aef&machine_no=709660f7-2d46-4ae5-b4bc-3b6ece1216f9
	/*url http://127.0.0.1:8080/hscloud-restful/services/query_vm_info?accessid=10001&accesskey=5SnKTEmUdCIPYPXrl9yjow==&machine_no=709660f7-2d46-4ae5-b4bc-3b6ece1216f9*/
	@RequestMapping(value = "/query_vm_info", method = RequestMethod.GET)
	@ResponseBody
	public void getVMInfo(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		logger.info("hscloud Restful getting VM Info is starting......");
		response.setContentType("text/html");
		orderResultVo=new OrderResultVo();
		
		List<VMInfoVo> vmInfoList = new ArrayList<VMInfoVo>();
		Map<String, String> params = new HashMap<String, String>();
		String machine_no = request.getParameter("machine_no");
		String vm_id = request.getParameter("vm_id");
		String accessId = request.getParameter("accessid");// 鉴权用户
		String accessKey = request.getParameter("accesskey");// 鉴权密码 MD5加密
		if (machine_no != null) {
			params.put("machine_no", machine_no);
		}
		if (vm_id != null) {
			params.put("vm_id", vm_id);
		}
		
		String accessIp=request.getRemoteHost();//获取请求Ip
		
		boolean flag = checkParameter(accessId, accessKey, accessIp);
        if(flag != true) {
            BasicUtil.printResult(response,orderResultVo);
            return;
        }
		
	
		    params.put("user_id", user_id);
			vmInfoList = vmInfoService.getVMInfo(params);
			if(vmInfoList == null || vmInfoList.isEmpty()) {
			    orderResultVo = BasicUtil.getOrderResultVo(false, "Do not find the vm");
			    BasicUtil.printResult(response, orderResultVo);
			    return;
			}
			
			String listStr = JsonUtil.listToJson(vmInfoList);
			orderResultVo.setResultObject(listStr);
			orderResultVo.setSuccess(true);
			BasicUtil.printResult(response, orderResultVo);
		
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

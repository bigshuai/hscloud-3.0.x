package com.hisoft.hscloud.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.hisoft.hscloud.web.dao.RestAccessAccountDao;
import com.hisoft.hscloud.web.service.BasicService;
import com.hisoft.hscloud.web.service.OrderPlan4RestService;
import com.hisoft.hscloud.web.service.RestOperationLogService;
import com.hisoft.hscloud.web.util.BasicUtil;
import com.hisoft.hscloud.web.vo.OperationLogVo;
import com.hisoft.hscloud.web.vo.OrderResultVo;

@Controller
@Scope("prototype")
public class OperationLogController {
	private Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private OrderPlan4RestService orderPlan4RestService;
	@Resource
	private RestOperationLogService restOperationLogService;;

	@Resource
	private RestAccessAccountDao restAccessAccountDao;

	@Resource
	private BasicService basicService;

	private OrderResultVo orderResultVo = new OrderResultVo();

	@RequestMapping(value = "/operation_result", method = RequestMethod.GET)
	@ResponseBody
	// http://127.0.0.1:8080/hscloud-restful/services/renewOrder?accessid=10001&accesskey=61b3e340c50fcec226a673cb350c6aef&feeTypeId=1&machine_no=xx
	public void getOperationResult(@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws Exception {
		String accessId = request.getParameter("accessid");// 鉴权用户
		String accessKey = request.getParameter("accesskey");// 鉴权密码 MD5加密

		String logId = request.getParameter("taskId");
		String jobType = request.getParameter("taskType");

		// 判断AccessKey是否符号MD5加密
		String user_id = orderPlan4RestService.getUserId(accessId);
		String access_key = restAccessAccountDao.getAccessKey(accessId);

		String accessIp = request.getRemoteHost();// 获取请求Ip

		if (user_id == null) {
			getOrderResultVo(false, "userId is invalid");
			BasicUtil.printResult(response, orderResultVo);
			return;
		}

		boolean ipIsValid = basicService.checkIP(accessIp, accessId);
		if (!ipIsValid) {
			getOrderResultVo(false, "ip is not correct!");
			BasicUtil.printResult(response, orderResultVo);
			return;
		}

		if (access_key != null) {
			if (access_key.equalsIgnoreCase(accessKey)) {
				logger.info(" AccessKey is valid......");
				Long logIdLocal = Long.parseLong(logId);
				OperationLogVo result = restOperationLogService
						.getOperationLogById(logIdLocal, jobType,user_id);

				if (result != null) {
					Gson gson = new Gson();
					String resultObject=gson.toJson(result);
					String resultMsg="";
					getOrderResultVo(true,resultObject,resultMsg);
				} else {
					getOrderResultVo(false,
							"Do not find a record,pleace check your request parameter.");
				}

			} else {
				getOrderResultVo(false, "AccessKey is invalid");
			}
		} else {
			getOrderResultVo(false, "AccessID is invalid");
		}
		BasicUtil.printResult(response, orderResultVo);
	}

	private void getOrderResultVo(boolean result, String reason) {
		this.orderResultVo.setReason(reason);
		this.orderResultVo.setSuccess(result);
	}
	
	private void getOrderResultVo(boolean result,String resultObject,String resultMsg) {
		this.orderResultVo.setResultObject(resultObject);
		this.orderResultVo.setSuccess(result);
	}
}

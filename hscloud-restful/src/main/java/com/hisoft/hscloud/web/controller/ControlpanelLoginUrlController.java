package com.hisoft.hscloud.web.controller;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

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

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudDateUtil;
import com.hisoft.hscloud.common.util.PasswordUtil;
import com.hisoft.hscloud.common.util.SecurityHelper;
import com.hisoft.hscloud.controlpanel.entity.ControlPanelUser;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.web.dao.RestAccessAccountDao;
import com.hisoft.hscloud.web.service.BasicService;
import com.hisoft.hscloud.web.service.ControlpanelLoginService;
import com.hisoft.hscloud.web.service.OrderPlan4RestService;
import com.hisoft.hscloud.web.util.BasicUtil;
import com.hisoft.hscloud.web.vo.OrderResultVo;

@Controller
@Scope("prototype")
public class ControlpanelLoginUrlController {
	private Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private OrderPlan4RestService orderPlan4RestService;
	@Resource
	private ControlpanelLoginService controlpanelLoginService;;

	@Resource
	private RestAccessAccountDao restAccessAccountDao;

	@Resource
	private BasicService basicService;

	private OrderResultVo orderResultVo = new OrderResultVo();

	@RequestMapping(value = "/controlpanel_login", method = RequestMethod.GET)
	@ResponseBody
	// http://127.0.0.1:8080/hscloud-restful/services/renewOrder?accessid=10001&accesskey=61b3e340c50fcec226a673cb350c6aef&feeTypeId=1&machine_no=xx
	public void getControlpanelLoginUrl(@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws Exception {
		String accessId = request.getParameter("accessid");// 鉴权用户
		String accessKey = request.getParameter("accesskey");// 鉴权密码 MD5加密

		String vmId = request.getParameter("machine_no");

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
				long referenceId = basicService.queryReferenceId(vmId, user_id);
				if (referenceId > 0) {
					ControlPanelUser cpUser = controlpanelLoginService
							.getCPUserByVmId(vmId);
					if (cpUser != null) {
						String originalKey = vmId + cpUser.getUserPassword();
						String authorKey = PasswordUtil
								.getEncyptedPasswd(originalKey);
						SimpleDateFormat sf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Date now=new Date();
						String nowStr=sf.format(now);
						String dateKey = SecurityHelper.EncryptData(
								nowStr,
								Constants.DEFAULT_SECURITY_KEY);
						dateKey=URLEncoder.encode(dateKey);
						User user=controlpanelLoginService.getUserByUserId(Long.parseLong(user_id));
						String appUrl = user.getDomain().getPublishing_address_cp()+Constants.CP_LOGIN_ACTION;
						String result = appUrl + "authorKey=" + authorKey
								+ "&vmId=" + vmId + "&dateKey=" + dateKey;
						BasicUtil.printResult(response, result);
						return;
					} else {
						getOrderResultVo(false,
								"Do not find a record,pleace check your request parameter.");
					}
				}else{
					getOrderResultVo(false,
							"Do not find the vm.");
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

	private void getOrderResultVo(boolean result, String resultObject,
			String resultMsg) {
		this.orderResultVo.setResultObject(resultObject);
		this.orderResultVo.setSuccess(result);
	}
}

package com.hisoft.hscloud.web.interceptor;

import org.apache.log4j.Logger;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.vo.ActionResult;
import com.hisoft.hscloud.controlpanel.entity.ControlPanelUser;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

public class AuthorInterceptor extends MethodFilterInterceptor {
	
	private static Logger logger = Logger.getLogger(AuthorInterceptor.class
			.getName());

	public ActionResult fillMsgCode(String code, String msg) {
		ActionResult actionResult = new ActionResult();
		actionResult.setResultCode(code);
		actionResult.setResultMsg(msg);
		actionResult.setSuccess(false);
		return actionResult;
	}

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {

		logger.info("AuthorInterceptor enter");
		String result = "";
		try {

			Object obj = invocation.getInvocationContext().getSession()
					.get(Constants.LOGIN_CURRENTUSER);
			if (obj == null || !(obj instanceof ControlPanelUser)) {
				return Action.LOGIN;
			} else {
				result = invocation.invoke();
				return result;
			}

		} catch (Exception e) {
			logger.error(e);
		}
		logger.info("AuthorInterceptor end");
		return result;
	}

	
}

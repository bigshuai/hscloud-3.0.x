package com.hisoft.hscloud.web.interceptor;
import org.apache.log4j.Logger;
import org.springside.modules.web.struts2.Struts2Utils;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.vo.ActionResult;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

public class LoginInterceptor extends MethodFilterInterceptor {
	private static Logger logger = Logger.getLogger(LoginInterceptor.class
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
		logger.debug("AuthorInterceptor enter");
		Object obj = invocation.getInvocationContext().getSession()
				.get(Constants.LOGIN_CURRENTUSER);
		if (obj == null) {
			Struts2Utils.renderJson(fillMsgCode(Constants.UNLOGGED, "没有登录"));
		} else {
			return invocation.invoke();
		}
		return null;
	}

}

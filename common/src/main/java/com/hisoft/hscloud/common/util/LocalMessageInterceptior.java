/**
 * @title Interceptior.java
 * @package com.hisoft.hscloud.common.util
 * @description 拦截器
 * @author AaronFeng
 * @update 2012-4-11 下午4:12:24
 * @version V1.0
 */
package com.hisoft.hscloud.common.util;

import java.text.MessageFormat;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springside.modules.web.struts2.Struts2Utils;

import com.hisoft.hscloud.common.vo.ActionResult;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @description 用来处理action返回的结果，将消息代码转换成对应local的语言文字
 * @version 1.0
 * @author AaronFeng
 * @update 2012-4-11 下午4:12:24
 */
public class LocalMessageInterceptior extends AbstractInterceptor {
	/**
	* @fields serialVersionUID 用一句话描述这个变量表示什么
	*/
	private static final long serialVersionUID = 3064506245200051677L;
	org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this
			.getClass());	
	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		log.debug("pre action enter LocalMessage intercept......");
		String result = actionInvocation.invoke();
		log.debug("after action enter LocalMessage intercept......");

		// 获取请求的action名称
		String actionName = actionInvocation.getInvocationContext().getName();
		log.debug("actionName=" + actionName);
		ValueStack vs = actionInvocation.getStack();
		ActionResult actionResult = (ActionResult) vs.findValue("actionResult");
		if (actionResult != null) {
			String code = actionResult.getResultCode();
			log.debug("code=" + code);
			String msg = actionResult.getResultMsg();
			log.debug("init msg=" + msg);
			if (!StringUtils.isEmpty(code)
					&& !Constants.OPTIONS_SUCCESS.equals(code)) {
				actionResult.setSuccess(false);
				try {
					Locale local = actionInvocation.getInvocationContext()
							.getLocale();
					
					String m = LanguageUtil.getString(code, local);
					if(null != msg && !"".equals(msg)){
						msg = MessageFormat.format(m, new Object[]{msg});
					}else{
						msg = m;
					}
					log.debug("msg=" + msg);
					actionResult.setResultMsg(msg);
				} catch (Exception e) {
					log.error(e);
					actionResult.setResultMsg(e.getMessage());
				}
			}			
			Struts2Utils.renderJson(actionResult);	
		}		
		return result;
	}

}

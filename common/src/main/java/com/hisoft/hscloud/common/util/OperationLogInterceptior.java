/**
 * @title Interceptior.java
 * @package com.hisoft.hscloud.common.util
 * @description 拦截器
 * @author AaronFeng
 * @update 2012-4-11 下午4:12:24
 * @version V1.0
 */
package com.hisoft.hscloud.common.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.RequestUtils;
import org.json.JSONObject;
import org.omg.CORBA.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.web.struts2.Struts2Utils;

import com.hisoft.hscloud.common.message.MsgSender;
import com.hisoft.hscloud.common.message.RabbitMQSender;
import com.hisoft.hscloud.common.vo.ActionResult;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @description 用来增加操作日志的拦截
 * @version 1.0
 * @author AaronFeng
 * @update 2012-4-11 下午4:12:24
 */
public class OperationLogInterceptior extends AbstractInterceptor {
	/**
	* @fields serialVersionUID 用一句话描述这个变量表示什么
	*/
	private static final long serialVersionUID = 3064506245200051677L;
	org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this
			.getClass());
	//private MsgSender msgSender=new RabbitMQSender();
	@Autowired
	private MsgSender msgSender;	
	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		log.info("pre action enter operation log intercept......");
		String result = actionInvocation.invoke();
		log.info("after action enter operation log intercept......");
		
		Object obj=null;
		String name=null;
		//只记录后台管理员操作日志
		obj=ActionContext.getContext().getSession()
				.get(Constants.LOGIN_CURRENTUSER);			
		if(obj!=null){
			String userStr = obj.toString();
			JSONObject jsonobj = new JSONObject(obj);
			name=jsonobj.getString("name");
			log.info("name->"+name);			
		}
		// 获取请求的action名称
		String actionName = actionInvocation.getInvocationContext().getName();
		String methodName=actionInvocation.getProxy().getMethod();
		log.info("action" + actionName+"!"+methodName+".action");
		ValueStack vs = actionInvocation.getStack();
		ActionResult actionResult = (ActionResult) vs.findValue("actionResult");
		if (actionResult != null) {
			if(name==null||"".equals(name)){
				log.info("operator name is"+name);
			}else{
				String message = "{'body':{'operator':'"+name+"','actionName':'" + actionName+"!"+methodName+".action"
						+ "','operationTime':'" + new Date() + "','operationResult':'"
						+ actionResult.isSuccess() + "','parameter':'','remark':''}}";			
				msgSender.sendMessage(Constants.QNAME4CLOUDLOG,message);			
				log.info("message=" + message);
			}			
		}		
		return result;
	}

}

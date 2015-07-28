package com.hisoft.hscloud.web.interceptor;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.web.struts2.Struts2Utils;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.vo.ActionResult;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.service.PermissionService;
import com.hisoft.hscloud.crm.usermanager.service.RoleService;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.opensymphony.xwork2.util.ValueStack;

//public class AuthorInterceptor extends AbstractInterceptor {
public class AuthorInterceptor extends MethodFilterInterceptor {
	private static Logger logger = Logger.getLogger(AuthorInterceptor.class
			.getName());

	@Autowired
	private PermissionService permissionService;
	
	@Autowired
    private RoleService roleService;

	public ActionResult fillMsgCode(String code, String msg) {
		ActionResult actionResult=new ActionResult();
		actionResult.setResultCode(code);
		actionResult.setResultMsg(msg);
		actionResult.setSuccess(false);
		return actionResult;
	}

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {

		logger.debug("AuthorInterceptor enter");
		String result = "";
		 ValueStack vs = invocation.getStack();

		
		try {

//			Object obj = invocation.getInvocationContext().getSession()
//					.get(Constants.LOGIN_CURRENTUSER);
//			
//			if (obj == null) {
//				return Action.LOGIN;
//			} else {
//				
//				String resourceType = (String) vs.findValue("resourceType");
//				if(null == resourceType || "".equals(resourceType)){
//					result = invocation.invoke();
//					return result;
//				}
//				
//				Admin  admin = (Admin)obj;
//				if(admin.getIsSuper() || roleService.isSpecialAdmin(admin.getId())){
//					result = invocation.invoke();
//					return result;
//				}
//				
//				Map<String, Object> parameters = invocation.getInvocationContext().getParameters();
//				String primKey = parameters.get("primKey") == null ? null: ((String[]) parameters.get("primKey"))[0];
//				String actionName = invocation.getInvocationContext().getName();
//				String methodName = invocation.getProxy().getMethod();
//				String classKey = actionName + "!" + methodName;
//				logger.info("actionName:" + actionName + "  ;  method: "+ methodName);
//				
//
//
//				if(Constants.USER.equals(userType) && permissionService.getUActionPermission(user.getId(), "0", resourceType, classKey)){
//					result = invocation.invoke();
//					return result;
//				}
//				if(Constants.ADMIN.equals(userType) && permissionService.getAActionPermission(user.getId(), "0", resourceType, classKey)){
//					result = invocation.invoke();
//					return result;
//				}
//				
//				if (primKey == null) {
//					
//					 @SuppressWarnings("unchecked")
//					 List<Long> primKeys = (List<Long>)vs.findValue("primKeys");
//				     primKeys = permissionService.getAPrimKey(admin.getId(), resourceType, classKey);
//					 vs.setValue("primKeys", primKeys);
//					 result = invocation.invoke();
//					 return result;
//					 
//				} 
//				
//			    if(permissionService.getAActionPermission(admin.getId(), primKey, resourceType, classKey)){
//					 result = invocation.invoke();
//					 return result;
//			    }else{
//				     Struts2Utils.renderJson(fillMsgCode("","对不起，你没有权限进行此操作"));
//			    }
//			    
//			}
			
			

			Object obj = invocation.getInvocationContext().getSession()
					.get(Constants.LOGIN_CURRENTUSER);
	
			if (obj == null) {
				return Action.LOGIN;
			} else {
				
				String resourceType = (String) vs.findValue("resourceType");
				if(null == resourceType || "".equals(resourceType)){
					result = invocation.invoke();
					return result;
				}
				Admin  admin = (Admin)obj;
				if(admin.getIsSuper() || roleService.isSpecialAdmin(admin.getId())){
					result = invocation.invoke();
					return result;
				}

				Map<String, Object> parameters = invocation.getInvocationContext().getParameters();
				String primKey = parameters.get("primKey") == null ? null
						: ((String[]) parameters.get("primKey"))[0];
				String actionName = invocation.getInvocationContext().getName();
				String methodName = invocation.getProxy().getMethod();
			//	String classKey = actionName + "!" + methodName;
				String classKey = (String) vs.findValue("classKey");
				if(null == classKey || "".equals(classKey)){
					classKey = actionName + "!" + methodName;
				}
				logger.debug("actionName:" + actionName + "  ;  method: "+ methodName);
				
				if (primKey == null) {
					
					 @SuppressWarnings("unchecked")
					 List<Long> primKeys = (List<Long>)vs.findValue("primKeys");
					 primKeys = permissionService.getAPrimKey(admin.getId(), resourceType, classKey);
					 if(!primKeys.contains(0l)){
						 vs.setValue("primKeys", primKeys);
					 }
					 result = invocation.invoke();
					 return result;
					 
				} 
				
				if(permissionService.getAActionPermission(admin.getId(),primKey, resourceType, classKey)){
					result = invocation.invoke();
					return result;
				}else{
					Struts2Utils.renderJson(fillMsgCode("","对不起，你没有权限进行此操作"));
				}
			    
			}
		} catch (Exception e) {
			logger.error(e);
			Struts2Utils.renderJson(fillMsgCode("","获取权限异常"));
		}
		logger.debug("AuthorInterceptor end");
		return result;
	}

}

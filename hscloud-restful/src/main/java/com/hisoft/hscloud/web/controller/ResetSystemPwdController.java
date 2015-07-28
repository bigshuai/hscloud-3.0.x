/* 
* 文 件 名:  ResetSystemPwdController.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-6-7 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.controller; 

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hisoft.hscloud.web.dao.RestAccessAccountDao;
import com.hisoft.hscloud.web.service.BasicService;
import com.hisoft.hscloud.web.service.OrderPlan4RestService;
import com.hisoft.hscloud.web.service.ResetSystemPwdService;
import com.hisoft.hscloud.web.util.BasicUtil;
import com.hisoft.hscloud.web.util.RestConstant;
import com.hisoft.hscloud.web.vo.OrderResultVo;

/** 
 * <重置系统密码> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-6-7] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Controller
@Scope("prototype")
public class ResetSystemPwdController {
    
    private Logger logger = Logger.getLogger(this.getClass());
    
    @Resource
    private OrderPlan4RestService orderPlan4RestService;

    @Resource
    private ResetSystemPwdService resetSystemPwdService;
    
    @Resource
    private RestAccessAccountDao restAccessAccountDao;
    
    @Resource
    private BasicService basicService;
    
    private OrderResultVo orderResultVo = new OrderResultVo();
    
    private String user_id;
    
    @RequestMapping(value = "/reset_os_pwd", method = RequestMethod.GET)
    @ResponseBody
    //http://127.0.0.1:8080/hscloud-restful/services/reset_pwd?accessid=10001&accesskey=61b3e340c50fcec226a673cb350c6aef&password=1&machine_no=xx
    public void resetSystemPwd(@Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
        
        logger.info("hscloud Restful query plan info by user starting......");
        orderResultVo=new OrderResultVo();
        
        String machineNo = request.getParameter("machine_no");
        String password = request.getParameter("password");
        String accessId = request.getParameter("accessid");//鉴权用户
        String accessKey = request.getParameter("accesskey");//鉴权密码  MD5加密
        
        String accessIp=request.getRemoteHost();//获取请求Ip
        
        if(StringUtils.isBlank(machineNo) 
                || StringUtils.isBlank(password)) {
            orderResultVo = BasicUtil.getOrderResultVo(false, "paremeter is wrong");
            BasicUtil.printResult(response,orderResultVo);
            return;
        }
        

        boolean flag = checkParameter(accessId, accessKey, accessIp);
        if(flag != true) {
            BasicUtil.printResult(response,orderResultVo);
            return;
        }
        
       
                
                if(BasicUtil.checkPassword(password) == false) {
                    orderResultVo = BasicUtil.getOrderResultVo(false, "Password format error!");
                    BasicUtil.printResult(response, orderResultVo);
                    return;
                }
                
              //查询用户是否虚拟机拥有者 referenceId 小于0 不是拥有者
                long referenceId = basicService.queryReferenceId(machineNo, user_id);
                if(referenceId <= 0) {
                    orderResultVo = BasicUtil.getOrderResultVo(false, "Do not find the vm");
                    BasicUtil.printResult(response, orderResultVo);
                    return;
                }
                
                try {
                    Map<String, Object> taskMap = new HashMap<String, Object>();
                    taskMap.put("create_id", user_id);
                    taskMap.put("status", RestConstant.TASK_STATUS_CREATE);
                    taskMap.put("machine_no", machineNo);
                    taskMap.put("operating_type", "resetSystemPwd");
                    
                    long taskId = basicService.saveTask(taskMap);
                    
                    resetSystemPwdService.resetSystemPwd(user_id, machineNo, password, taskId);
                    orderResultVo.setTaskId(Long.toString(taskId));
                    orderResultVo.setSuccess(true);
                    BasicUtil.printResult(response, orderResultVo);
                } catch(Exception ex) {
                    ex.printStackTrace();
                    logger.info("resetSystemPwd", ex);
                    
                    orderResultVo = BasicUtil.getOrderResultVo(false, ex.getMessage());
                    BasicUtil.printResult(response, orderResultVo);
                }
                
         
        
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

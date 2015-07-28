/* 
* 文 件 名:  RefundApplyController.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-6-8 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.controller; 

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hisoft.hscloud.web.dao.RestAccessAccountDao;
import com.hisoft.hscloud.web.service.BasicService;
import com.hisoft.hscloud.web.service.OrderPlan4RestService;
import com.hisoft.hscloud.web.service.RefundApplyService;
import com.hisoft.hscloud.web.util.BasicUtil;
import com.hisoft.hscloud.web.vo.OrderResultVo;

/** 
 * <退款申请> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-6-8] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Controller
public class RefundApplyController {
    @Resource
    private RefundApplyService refundApplyService;
    
    @Resource
    private OrderPlan4RestService orderPlan4RestService;
    
    @Resource
    private RestAccessAccountDao restAccessAccountDao;
    
    @Resource
    private BasicService basicService;

    private Logger logger = Logger.getLogger(this.getClass());
    
    private OrderResultVo orderResultVo = new OrderResultVo();
    
    private String user_id;
    
  //http://127.0.0.1:8080/hscloud-restful/services/apply_refund?accessid=10001&accesskey=61b3e340c50fcec226a673cb350c6aef&orderno=1&machine_no=123&applyReason=1&refundReasonType=1
    @RequestMapping(value = "/apply_refund", method = RequestMethod.GET)
    @ResponseBody
    public void refundApply(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        
        logger.info("hscloud Restful query plan info by user starting......");
        
        orderResultVo=new OrderResultVo();
        
        String accessId = request.getParameter("accessid");//鉴权用户
        String accessKey = request.getParameter("accesskey");//鉴权密码  MD5加密
        
        String orderNo = request.getParameter("orderno");
        String machineNo = request.getParameter("machine_no");
        String applyReason = request.getParameter("refundreason");
        String refundReasonType = request.getParameter("refundtype");
        
        String accessIp=request.getRemoteHost();//获取请求Ip
        
        if(StringUtils.isBlank(orderNo) 
                || StringUtils.isBlank(machineNo) 
                || StringUtils.isBlank(refundReasonType) 
                || StringUtils.isBlank(applyReason)) {
            orderResultVo = BasicUtil.getOrderResultVo(false, "paremeter is wrong");
            BasicUtil.printResult(response,orderResultVo);
            return;
        }
        
        
        boolean flag = checkParameter(accessId, accessKey, accessIp);
        if(flag != true) {
            BasicUtil.printResult(response,orderResultVo);
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
                    //applyReason = new String(applyReason.getBytes("iso8859_1"), "UTF-8");
                    String result = refundApplyService.refundApply(user_id, orderNo, machineNo, applyReason, Short.valueOf(refundReasonType));
                    if(result.equals("success")) {
                        orderResultVo.setSuccess(true);
                        BasicUtil.printResult(response,orderResultVo);
                    } else {
                        orderResultVo = BasicUtil.getOrderResultVo(false, null);
                        BasicUtil.printResult(response,orderResultVo);
                    }
                } catch(Exception ex) {
                    ex.printStackTrace();
                    logger.info(ex);
                    orderResultVo = BasicUtil.getOrderResultVo(false, null);
                    BasicUtil.printResult(response,orderResultVo);
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

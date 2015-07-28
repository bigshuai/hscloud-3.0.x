/* 
* 文 件 名:  DemandRestController.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2014-3-19 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.controller; 

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openstack.model.compute.nova.NovaServerForCreate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hisoft.hscloud.bss.sla.om.util.SubmitOrderData;
import com.hisoft.hscloud.common.entity.HcEventResource;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudDateUtil;
import com.hisoft.hscloud.common.util.PasswordUtil;
import com.hisoft.hscloud.common.util.RabbitMQUtil;
import com.hisoft.hscloud.common.util.SecurityHelper;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.service.UserService;
import com.hisoft.hscloud.web.dao.RestAccessAccountDao;
import com.hisoft.hscloud.web.service.BasicService;
import com.hisoft.hscloud.web.service.DemandRestService;
import com.hisoft.hscloud.web.service.OrderPlan4RestService;
import com.hisoft.hscloud.web.util.BasicUtil;
import com.hisoft.hscloud.web.vo.DemandItemsVo;
import com.hisoft.hscloud.web.vo.OrderPlanVo;
import com.hisoft.hscloud.web.vo.ResultVo;

/** 
 * <按需购买rest请求> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2014-3-19] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Controller
public class DemandRestController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private OrderPlan4RestService orderPlan4RestService;
	@Resource
	private RestAccessAccountDao restAccessAccountDao;
	@Resource
	private DemandRestService demandRestService;
	@Resource
    private UserService userService;
	@Resource
    private BasicService basicService;
	private ResultVo resultVo = null;
	private String userId = null;
	private SubmitOrderData submitOrderData = new SubmitOrderData();
	private DemandItemsVo demandItemsVo = null;
	private User user = null;
//	private List<Integer>itemIds = new ArrayList<Integer>();
	
	/**
	 * <按需购买-待付费（分平台）> 
	* <功能详细描述> 
	* @param request
	* @param response
	* @throws Exception 
	* @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/payasugo", method = RequestMethod.GET)
	@ResponseBody
	//http://127.0.0.1:8080/hscloud-restful/services/payasugo?accessid=10001&accesskey=e51f6832af5cf3d27d6ab7885292ef5c&email=xxxx&password=61b3e340c50fcec226a673cb350c6aef&osid=10paymode=1&ext_disk=100&mem_size=4&vcpu=2&bandwidth=5&datacenter=BJBGP&num=2
	public void DemandForApplyRest(@Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {		
		logger.info("hscloud Restful order plans by user starting......");	
		
		resultVo=new ResultVo();	
		/**1、获取参数**/
		String accessIp=request.getRemoteHost();//获取请求Ip
		String accessId = request.getParameter("accessid");//鉴权用户-分平台code
		String email = request.getParameter("email");
		String accessKey = request.getParameter("accesskey");//鉴权密码  MD5加密
		String osId = request.getParameter("osid");//操作系统ID
		String paymode = request.getParameter("paymode");//支付方式：1：月付 3：季付 	6：半年付  12：年付
		String num = request.getParameter("num");//购买套餐数量
		String vcpu = request.getParameter("vcpu");//CPU核数
		String extDisk = request.getParameter("ext_disk");//数据盘
		String memSize = request.getParameter("mem_size");//内存大小
		String bandwidth = request.getParameter("bandwidth");//带宽
		String datacenter = request.getParameter("datacenter");//数据中心编码
		
		demandItemsVo = new DemandItemsVo();
		demandItemsVo.setAccessIp(accessIp);
		demandItemsVo.setAccessId(accessId);
		demandItemsVo.setAccessKey(accessKey);
		demandItemsVo.setOsId(osId);
		demandItemsVo.setPaymode(paymode);
		demandItemsVo.setVcpu(vcpu);
		demandItemsVo.setExtDisk(extDisk);
		demandItemsVo.setMemSize(memSize);
		demandItemsVo.setBandwidth(bandwidth);
		demandItemsVo.setDatacenter(datacenter);
		
		String doUseCoupon = request.getParameter("useCoupon"); //是否使用返点
		if (!"true".equals(doUseCoupon)) {
			doUseCoupon = "false";
		}
		/*if(StringUtils.isBlank(doUseCoupon)) {
		    doUseCoupon = "false";		    
		}*/
		demandItemsVo.setUseCoupon(doUseCoupon);
		String doUseGift = request.getParameter("useGift"); //是否使用礼金
		if (!"true".equals(doUseGift)) {
			doUseGift = "false";
		}
        /*if(StringUtils.isBlank(doUseGift)) {
            doUseGift = "false";            
        }*/
		demandItemsVo.setUseGift(doUseGift);
		boolean flag = basicService.checkParameterOfDomain(response, accessId, accessKey, accessIp, email);
		if(flag != true) {
		    return;
		}
		user = basicService.checkUser(response, email);
       boolean checkFlag = basicService.checkDomainOfUser(response, user, accessId);
        if(!checkFlag){
        	return;
        }
        /** 3、执行套餐的订购操作 **/       
        OrderPlanVo orderPlanVos = new OrderPlanVo();
        
        Map<String, Object> map = orderPlan4RestService.queryAccessByAccessId(accessId);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        
        try {
            int vmNum = Integer.parseInt(num);
            if(vmNum > 10) {
            	resultVo = new ResultVo(false,"num can not be more than ten");
                BasicUtil.printResult(response, resultVo);
                return;
            }
            demandItemsVo.setVmnum(num);           
            submitOrderData.setUser(user);
            submitOrderData.setVmNum(vmNum);
            submitOrderData.setBuyPeriod(Integer.valueOf(paymode));
            
           resultMap = demandRestService.demandBuyDirect(submitOrderData, orderPlanVos, (String) map.get("id"), demandItemsVo,false);
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                resultMap.put("log", ex.getMessage());
            } else {
                resultMap.put("log", "throw an exception when buy a vm");
            }
        }

        if (!resultMap.isEmpty() && resultMap.containsKey("log")) {
            String log = (String) resultMap.get("log");
            resultVo = new ResultVo(false, log);
            BasicUtil.printResult(response, resultVo);
            return;
        }else{
        	String originalKey = user.getEmail() + user.getPassword();
            String authorKey;
            try {
                authorKey = PasswordUtil.getEncyptedPasswd(originalKey);
            } catch (Exception e) {
                logger.info("UserRegisterController.registerUser", e);
                BasicUtil.fillResultVoFalse(e.getMessage(), response);
                return;
            }
            String dateKey = SecurityHelper.EncryptData(
                    HsCloudDateUtil.getNowStr(), Constants.DEFAULT_SECURITY_KEY);
            dateKey = URLEncoder.encode(dateKey);
            String domainPublishUrl = user.getDomain().getPublishingAddress();
            String appUrl = domainPublishUrl + "/user_mgmt/paymentByUrl!userLoginForPaymentByUrl.action?";
            String result = appUrl + "authorKey=" + authorKey + "&userId="
                    + user.getId() + "&dateKey=" + dateKey + "&operator=" + user.getEmail();            
            BasicUtil.printResult(response, result);
        }
        
		
		/**4、日志的记录：log4j或者记录到数据库**/
		logger.info("hscloud  Restful order plans by user end");
	}
	/**
	 * <按需购买-直接付费（大客户）> 
	* <功能详细描述> 
	* @param request
	* @param response
	* @throws Exception 
	* @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("static-access")
//	@RequestMapping(value = "/payasugo", method = RequestMethod.GET)
//	@ResponseBody
	//http://127.0.0.1:8080/hscloud-restful/services/payasugo?accessid=10001&accesskey=e51f6832af5cf3d27d6ab7885292ef5c&email=xxxx&password=61b3e340c50fcec226a673cb350c6aef&osid=10paymode=1&ext_disk=100&mem_size=4&vcpu=2&bandwidth=5&datacenter=BJBGP&num=2
	public void DemandForPaymentRest(@Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {		
		logger.info("hscloud Restful order plans by user starting......");	
		
		resultVo=new ResultVo();	
		/**1、获取参数**/
		String accessIp=request.getRemoteHost();//获取请求Ip
		String accessId = request.getParameter("accessid");//鉴权用户
		String accessKey = request.getParameter("accesskey");//鉴权密码  MD5加密
		String osId = request.getParameter("osid");//操作系统ID
		String paymode = request.getParameter("paymode");//支付方式：1：月付 3：季付 	6：半年付  12：年付
		String num = request.getParameter("num");//购买套餐数量
		String vcpu = request.getParameter("vcpu");//CPU核数
		String extDisk = request.getParameter("ext_disk");//数据盘
		String memSize = request.getParameter("mem_size");//内存大小
		String bandwidth = request.getParameter("bandwidth");//带宽
		String datacenter = request.getParameter("datacenter");//数据中心编码
		
		demandItemsVo = new DemandItemsVo();
		demandItemsVo.setAccessIp(accessIp);
		demandItemsVo.setAccessId(accessId);
		demandItemsVo.setAccessKey(accessKey);
		demandItemsVo.setOsId(osId);
		demandItemsVo.setPaymode(paymode);
		demandItemsVo.setVcpu(vcpu);
		demandItemsVo.setExtDisk(extDisk);
		demandItemsVo.setMemSize(memSize);
		demandItemsVo.setBandwidth(bandwidth);
		demandItemsVo.setDatacenter(datacenter);
		
		String doUseCoupon = request.getParameter("useCoupon"); //是否使用返点
		if(StringUtils.isBlank(doUseCoupon)) {
		    doUseCoupon = "false";
		    demandItemsVo.setUseCoupon(doUseCoupon);
		}
		
		String doUseGift = request.getParameter("useGift"); //是否使用礼金
        if(StringUtils.isBlank(doUseGift)) {
            doUseGift = "false";
            demandItemsVo.setUseGift(doUseGift);
        }
        
		boolean flag = checkParameter(accessId, accessKey, accessIp);
		if(flag != true) {
		    BasicUtil.printResult(response,resultVo);
		    return;
		}
		
        /** 3、执行套餐的订购操作 **/
        logger.info(" ip is valid......");
        OrderPlanVo orderPlanVos = new OrderPlanVo();
        
        Map<String, Object> map = orderPlan4RestService.queryAccessByAccessId(accessId);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        
        try {
            int vmNum = Integer.parseInt(num);
            if(vmNum > 10) {
            	resultVo = new ResultVo(false,"num can not be more than ten");
                BasicUtil.printResult(response, resultVo);
                return;
            }
            demandItemsVo.setVmnum(num);
           
//            itemIds.add(Integer.valueOf(osId));
            submitOrderData.setUser(user);
            submitOrderData.setVmNum(vmNum);
            submitOrderData.setBuyPeriod(Integer.valueOf(paymode));
            
           /* resultMap = orderPlan4RestService.scBuyDirect(Integer.parseInt(scId), Integer.parseInt(osId),
                    payMode_period, numInt, Long.parseLong(userId), orderPlanVos,
                    (String) map.get("id"), accessId, doUseCoupon, doUseGift);*/
            resultMap = demandRestService.demandBuyDirect(submitOrderData, orderPlanVos, (String) map.get("id"), demandItemsVo,true);
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                resultMap.put("log", ex.getMessage());
            } else {
                resultMap.put("log", "throw an exception when buy a vm");
            }
        }
        // **********************lihonglei end************

        if (!resultMap.isEmpty() && resultMap.containsKey("log")) {
            String log = (String) resultMap.get("log");
            resultVo = new ResultVo(false, log);
            BasicUtil.printResult(response, resultVo);
            return;
        } else {
            // **********************lihonglei star 2013-05-23************
            String result = "";
            for (String str : resultMap.keySet()) {
                result = str;
            }
            // 消息在事务提交后发送
            @SuppressWarnings("unchecked")
            Map<NovaServerForCreate, HcEventResource> rabbitMQ = (Map<NovaServerForCreate, HcEventResource>) resultMap.get(result);
            RabbitMQUtil rabbitMqUtil = new RabbitMQUtil();
            for (NovaServerForCreate nsfc : rabbitMQ.keySet()) {
                HcEventResource her = rabbitMQ.get(nsfc);
                orderPlanVos.getVmList().add(her.getObj_id());
                rabbitMqUtil.sendMessage(her.getMessage(), nsfc, her,"HcEventResource");
            }
            // **********************lihonglei end 2013-05-23************
            resultVo.setSuccess(true);
            String json = new JSONArray().fromObject(orderPlanVos).toString();
            resultVo.setResultObject(json);
            BasicUtil.printResult(response, resultVo);
        }
		
		/**4、日志的记录：log4j或者记录到数据库**/
		logger.info("hscloud  Restful order plans by user end");
	}
	private boolean checkParameter(String accessId, String accessKey, String accessIp) {
        if(accessId!=null&&!"".equals(accessId)){
        	userId=orderPlan4RestService.getUserId(accessId);
        }
        if(userId==null){
        	resultVo = new ResultVo(false,"userId is invalid");
            return false;
        }
    	user = userService.getUser(Long.valueOf(userId));
        if(user==null){
        	resultVo = new ResultVo(false,"user is not exist!");
            return false;
        }
        String  access_key=restAccessAccountDao.getAccessKey(accessId);
        if(!access_key.equalsIgnoreCase(accessKey)){
        	resultVo = new ResultVo(false, "AccessKey is invalid");
            return false;
        }
        return true;
	}

}

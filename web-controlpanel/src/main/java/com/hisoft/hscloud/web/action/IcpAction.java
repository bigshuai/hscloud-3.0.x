/* 
* 文 件 名:  IcpAction.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-1-8 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.action; 

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.entity.City;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.controlpanel.entity.ControlPanelUser;
import com.hisoft.hscloud.crm.icp.vo.IcpVO;
import com.hisoft.hscloud.crm.usermanager.util.Utils;
import com.hisoft.hscloud.web.facade.Facade;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-1-8] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class IcpAction extends HSCloudAction {
    /** 
    * 注释内容 
    */
    private static final long serialVersionUID = -2634125437159166359L;
    private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private Facade facade;
    private IcpVO icpVO;
    private String operateObject;
    
    /**
     * 初始化icp信息 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void initIcp() {
        long beginRunTime = 0;
        if (logger.isDebugEnabled()) {
            beginRunTime = System.currentTimeMillis();
            logger.debug("enter initIcp method.");
        }

        ControlPanelUser controlPanelUserv = (ControlPanelUser) super
                .getCurrentLoginUser();
        if(controlPanelUserv==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
		}
        String vmId = controlPanelUserv.getVmId();
        try {
            Map<String, Object> result = facade.initIcp(vmId);
            this.fillActionResult(result);
        } catch (Exception ex) {
            dealThrow(new HsCloudException(Constants.ICP_EXCEPITON, "icp备案异常",
                    logger, ex), Constants.ICP_EXCEPITON);
        }
        if (logger.isDebugEnabled()) {
            long takeTime = System.currentTimeMillis() - beginRunTime;
            logger.debug("exit initIcp method.takeTime:" + takeTime + "ms");
        }
    }
    
    /**
     * icp代码提交 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void icpPutOnRecord() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter icpPutOnRecord method.");			
		}
		ControlPanelUser controlPanelUserv = (ControlPanelUser) super
                .getCurrentLoginUser();
        if(controlPanelUserv==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
		}
        try{
            icpVO = Utils.strutsJson2Object(IcpVO.class);
            operateObject = "VM[vmIp:" + icpVO.getIp() + "]";
            String result = facade.icpPutOnRecord(icpVO);
            logger.debug("result:" + result);
            if(!result.equals("1000")) {
                this.fillActionResult("ICP" + result);
            }
            facade.insertOperationLog(controlPanelUserv,"icp备案","icp备案",Constants.RESULT_SUCESS,operateObject);
        } catch(Exception ex) {
            facade.insertOperationLog(controlPanelUserv,"icp备案错误:"+ex.getMessage(),"icp备案",Constants.RESULT_FAILURE,operateObject);
            dealThrow(new HsCloudException(Constants.ICP_EXCEPITON,
                    "icp备案异常", logger, ex), Constants.ICP_EXCEPITON);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit icpPutOnRecord method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 省份地市联动 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void changeProvince() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter changeProvince method.");			
		}
        String provinceCode = icpVO.getProvince();
        List<City> list = facade.changeProvince(provinceCode);
        this.fillActionResult(list);
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit changeProvince method.takeTime:" + takeTime + "ms");
		}
    }
    

    public IcpVO getIcpVO() {
        return icpVO;
    }

    public void setIcpVO(IcpVO icpVO) {
        this.icpVO = icpVO;
    }
}

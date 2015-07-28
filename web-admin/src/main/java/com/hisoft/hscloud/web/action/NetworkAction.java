/* 
* 文 件 名:  NetworkAction.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2014-2-10 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.action; 

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.IPConvert;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.entity.IPDetail;
import com.hisoft.hscloud.vpdc.ops.json.bean.NetWorkBean;
import com.hisoft.hscloud.web.facade.Facade;

/** 
 * <资源管理中的外网管理> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2014-2-10] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class NetworkAction  extends HSCloudAction {

    /** 
    * 注释内容 
    */
    private static final long serialVersionUID = -637160938912270021L;
    @Autowired
    private Facade facade;
    private int page;
    private int limit;
    private NetWorkBean netWorkBean;
    
    private Logger logger = Logger.getLogger(this.getClass());
    
    private Page<NetWorkBean> pageNetworkBean = new Page<NetWorkBean>();
    
    public void findPageNetwork() {
        pageNetworkBean.setPageNo(page);
        pageNetworkBean.setPageSize(limit);
        pageNetworkBean = facade.findPageNetwork(pageNetworkBean);
        this.fillActionResult(pageNetworkBean);
    }
    
    public String deleteNetwork() {
        long beginRunTime = 0;
        if(logger.isDebugEnabled()){
            beginRunTime = System.currentTimeMillis();
            logger.debug("enter deleteIP method.");         
        }
        Admin admin = (Admin) super.getCurrentLoginUser();
        if(admin==null){
            super.fillActionResult(Constants.OPTIONS_TIMEOUT);
            return null;
        }
        boolean result = false;
        long rangeId = netWorkBean.getIpRangeId();
        if (rangeId != 0) {
            try {
                List<IPDetail> list = facade.findIPDetailByStatus(rangeId, Constants.IP_STATUS_ASSIGNED);
                if (list.size() > 0) {
                    logger.error("ERROR:ip inused");
                    super.fillActionResult(Constants.IP_USED);
                    return null;
                } else {
                    result = facade.deleteNetwork(netWorkBean.getId(), rangeId);
                }
                facade.insertOperationLog(admin,"删除外网","删除外网",Constants.RESULT_SUCESS);
            } catch (Exception ex) {
                facade.insertOperationLog(admin,"删除外网错误:"+ex.getMessage(),"删除外网",Constants.RESULT_FAILURE);
                dealThrow(new HsCloudException(Constants.NETWORK_DELETE_EXCEPTION, "deleteIP Exception:",
                        logger, ex), Constants.NETWORK_DELETE_EXCEPTION);
            }
        }
        if (!result) {
            logger.error("ERROR:" + result);
            super.fillActionResult(Constants.OPTIONS_FAILURE);
        } else {
            super.fillActionResult(Constants.OPTIONS_SUCCESS);
        }
        if(logger.isDebugEnabled()){
            long takeTime = System.currentTimeMillis() - beginRunTime;
            logger.debug("exit deleteIP method.takeTime:" + takeTime + "ms");
        }
        return null;
    }
    
    /**
     * 创建外网管理的WanNetwork（网段）
     * @return
     */
    public String createWanNetwork(){
        long beginRunTime = 0;
        if(logger.isDebugEnabled()){
            beginRunTime = System.currentTimeMillis();
            logger.debug("enter createWanNetwork method.");         
        }
        Admin admin = (Admin) super.getCurrentLoginUser();
        if(admin==null){
            super.fillActionResult(Constants.OPTIONS_TIMEOUT);
            return null;
        }
        /*NetWorkBean netWork = new NetWorkBean();
        //test
        netWork.setCidr("192.168.2.0/27");
        netWork.setDns1("114.114.114.114");
        netWork.setGateway("192.168.2.254");
        netWork.setZoneGroup(1l);*/
        try {
            String ipStart = IPConvert.cidrToStringIPs(netWorkBean.getCidr())[0];
            String ipEnd = IPConvert.cidrToStringIPs(netWorkBean.getCidr())[1];
            long startIP = IPConvert.getIntegerIP(ipStart);
            long endIP = IPConvert.getIntegerIP(ipEnd);
            
            String useIPstart = netWorkBean.getUseIPstart();
            String useIPend = netWorkBean.getUseIPend();
            long useStartIP = IPConvert.getIntegerIP(useIPstart);
            long useEndIP = IPConvert.getIntegerIP(useIPend);
            if(useStartIP<startIP || useStartIP>endIP || useEndIP<startIP || useEndIP>endIP || useEndIP-useStartIP<0){
            	 logger.error("ERROR:ip invalid");
                 super.fillActionResult(Constants.IP_EXIST);
            }
            List<IPDetail> list = facade.findIPDetailByIP(useStartIP, useEndIP, netWorkBean.getGateway());
            if (list.size() > 0) {
                logger.error("ERROR:ip invalid");
                super.fillActionResult(Constants.IP_EXIST);
            }
            
            facade.createWanNetwork(netWorkBean,admin.getId());
            facade.insertOperationLog(admin,"添加外网","添加外网",Constants.RESULT_SUCESS);
        } catch (Exception e) {
            facade.insertOperationLog(admin,"添加外网:"+e.getMessage(),"添加外网",Constants.RESULT_FAILURE);
            dealThrow(new HsCloudException(Constants.VPDC_WANNETWORK_ERROR,
                    "createWanNetwork(Admin) Exception:", logger, e), Constants.OPTIONS_FAILURE);
            return null;
        }
        if(logger.isDebugEnabled()){
            long takeTime = System.currentTimeMillis() - beginRunTime;
            logger.debug("exit createWanNetwork method.takeTime:" + takeTime + "ms");
        }
        return null;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public NetWorkBean getNetWorkBean() {
        return netWorkBean;
    }

    public void setNetWorkBean(NetWorkBean netWorkBean) {
        this.netWorkBean = netWorkBean;
    }

}

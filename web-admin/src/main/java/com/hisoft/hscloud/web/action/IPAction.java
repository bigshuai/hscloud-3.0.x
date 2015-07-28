/* 
* 文 件 名:  IPAction.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2012-10-18 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.action; 

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.sc.entity.ServerZone;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.entity.IPDetail;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo.IPDetailInfoVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo.IPDetailVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo.IPRangeVO;
import com.hisoft.hscloud.web.facade.Facade;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2012-10-18] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class IPAction extends HSCloudAction {

    /** 
    * 注释内容 
    */
    private static final long serialVersionUID = -700054269200895089L;
    private Logger logger = Logger.getLogger(this.getClass());
    
    @Autowired
    private Facade facade;
    
    private int page;
    private int limit;
    private long startIP;
    private long endIP;
    private long rangeId;
    private String remark;
    private int status;
    private String detailIds;
    private Long ip;
    private String nodeName;
    private String vmName;
    private String userName;    
    private IPRangeVO ipRangeVO;
    private Page<IPRangeVO> pageIPRVO = new Page<IPRangeVO>();
    private Page<IPDetailVO> pageIPDVO = new Page<IPDetailVO>();
    private ServerZone serverZone;
    private String type;// 查询类型
	private String query;// 模糊查询条件
	private String userEmail;
    
    /**
     * 
     * @title: findAllIPRange
     * @description 查询当前用户所有IP段
     * @return 设定文件
     * @return String 返回类型
     * @throws
     * @version 1.4
     * @author ljg
     * @update 2012-10-18 上午11:19:19
     */
    public String findAllIPRange() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findAllIPRange method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
        int pageNo = page;
        int pageSize = limit;
        try {
        	if(query!=null && !"".equals(query)){
				query = new String(query.getBytes("iso8859_1"), "UTF-8");
			}
            pageIPRVO.setPageNo(pageNo);
            pageIPRVO.setPageSize(pageSize);
            facade.findIPRanges(pageIPRVO,type,query);
            super.fillActionResult(pageIPRVO);
        } catch (Exception ex) {
            dealThrow(new HsCloudException(Constants.IP_RANGE_FIND_EXCEPTION,
                    "findAllIPRange Exception:", logger, ex), Constants.IP_EXCEPTION);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findAllIPRange method.takeTime:" + takeTime + "ms");
		}
        return null;
    }
    
    /**
     * 
     * @title: createIP
     * @description 添加某一段的IP
     * @return 设定文件
     * @return String 返回类型
     * @throws
     * @version 1.4
     * @author ljg
     * @update 2012-10-18 上午11:22:15
     */
    public String createIP() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter createIP method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
        String result = null;
        long range = 0;        
        ipRangeVO.setCreateUid(admin.getId());
        try {
        	startIP = ipRangeVO.getStartIP();
            endIP = ipRangeVO.getEndIP();
            List<IPDetail> list = facade.findIPDetailByIP(startIP, endIP, ipRangeVO.getGateWay());
            range = endIP - startIP;
            if ((range < 0) || (list.size() > 0)) {// ||(range>=10)
                logger.error("ERROR:ip invalid");
                super.fillActionResult(Constants.IP_EXIST);
                return result;
            } else {
                result = facade.createIP(ipRangeVO);
            }
            facade.insertOperationLog(admin,"后台添加IP段","后台添加IP段",Constants.RESULT_SUCESS);
        } catch (Exception ex) {
        	facade.insertOperationLog(admin,"后台添加IP段错误:"+ex.getMessage(),"后台添加IP段",Constants.RESULT_FAILURE);
            dealThrow(new HsCloudException(Constants.IP_CREATE_EXCEPTION, "createIP Exception:",
                    logger, ex), Constants.IP_EXCEPTION);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit createIP method.takeTime:" + takeTime + "ms");
		}
        return null;
    }
    
    /**
     * 
     * @title: deleteIP
     * @description 删除IP
     * @return 设定文件
     * @return String 返回类型
     * @throws
     * @version 1.4
     * @author ljg
     * @update 2012-8-29 上午11:23:19
     */
    public String deleteIP() {
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
        if (rangeId != 0) {
            try {
                List<IPDetail> list = facade.findIPDetailByStatus(rangeId, Constants.IP_STATUS_ASSIGNED);
                if (list.size() > 0) {
                    logger.error("ERROR:ip inused");
                    super.fillActionResult(Constants.IP_USED);
                    return null;
                } else {
                    result = facade.deleteIP(rangeId);
                }
                facade.insertOperationLog(admin,"后台删除IP段","后台删除IP段",Constants.RESULT_SUCESS);
            } catch (Exception ex) {
            	facade.insertOperationLog(admin,"后台删除IP段错误:"+ex.getMessage(),"后台删除IP段",Constants.RESULT_FAILURE);
                dealThrow(new HsCloudException(Constants.IP_DELETE_EXCEPTION, "deleteIP Exception:",
                        logger, ex), Constants.IP_EXCEPTION);
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
     * ip查询 
    * <功能详细描述> 
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public String findAllIPDetail() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findAllIPDetail method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
        int pageNo = page;
        int pageSize = limit;
        pageIPDVO.setPageNo(pageNo);
        pageIPDVO.setPageSize(pageSize);
        try {
            if (!(ip == null || ip == 0 || "".equals(ip))) {
                pageIPDVO = facade.findIPDetailsByCondition(pageIPDVO,
                        rangeId,"ip", String.valueOf(ip));
            } else if (!(nodeName == null || "".equals(nodeName))) {
                nodeName = new String(nodeName.getBytes("iso8859_1"), "UTF-8");//
                pageIPDVO = facade.findIPDetailsByCondition(pageIPDVO,
                        rangeId,"nodeName", nodeName);
            } else if (!(vmName == null || "".equals(vmName))) {
                vmName = new String(vmName.getBytes("iso8859_1"), "UTF-8");//
                pageIPDVO = facade.findIPDetailsByCondition(pageIPDVO, rangeId,"vmName", vmName);
            } else if (!(userName == null || "".equals(userName))) {
                userName = new String(userName.getBytes("iso8859_1"), "UTF-8");//
                pageIPDVO = facade.findIPDetailsByCondition(pageIPDVO, rangeId,"userName", userName);
            } else if (!(userEmail == null || "".equals(userEmail))) {
                userEmail = new String(userEmail.getBytes("iso8859_1"), "UTF-8");//
                pageIPDVO = facade.findIPDetailsByCondition(pageIPDVO, rangeId,"userEmail", userEmail);
            } else {
                pageIPDVO = facade.findIPDetailsByCondition(pageIPDVO, rangeId,"",null);
            }
        } catch (Exception ex) {
            dealThrow(new HsCloudException(Constants.IP_FIND_EXCEPTION, "findAllIPDetail Exception:", logger, ex),
                    Constants.IP_EXCEPTION);
        }
        super.fillActionResult(pageIPDVO);
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findAllIPDetail method.takeTime:" + takeTime + "ms");
		}
        return null;
    }
    
    /**
     * 
     * @title: updateIPStatus
     * @description 禁用或启用IP
     * @return 设定文件
     * @return String 返回类型
     * @throws
     * @version 1.3
     * @author ljg
     * @update 2012-8-29 上午11:28:47
     */
    public String updateIPStatus() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter updateIPStatus method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
        boolean result = false;
        try {            
            long adminId = admin.getId();
            if(remark != null && remark.length()>64){
            	remark = remark.substring(0, 64);  
            }
//            remark = remark.substring(0, 64 < remark.length() ? 64 : remark.length());
            String[] detailIdsArray = detailIds.split(",");
            for (int i = 0; i < detailIdsArray.length; i++) {
                result = facade.updateIPDetail(
                        Long.parseLong(detailIdsArray[i]), status, adminId,remark);
            }
            facade.insertOperationLog(admin,"后台禁用或启用IP","后台禁用或启用IP",Constants.RESULT_SUCESS);
        } catch (Exception ex) {
        	facade.insertOperationLog(admin,"后台禁用或启用IP错误:"+ex.getMessage(),"后台禁用或启用IP",Constants.RESULT_FAILURE);
            dealThrow(new HsCloudException(Constants.IP_UPDATE_STATUS_EXCEPTION, 
                    "updateIPStatus Exception:", logger, ex),
                    Constants.IP_EXCEPTION);
        }
        if (!result) {
            logger.error("ERROR:" + result);
            super.fillActionResult(Constants.IP_UPDATE_STATUS_EXCEPTION);
        } else {
            super.fillActionResult(Constants.OPTIONS_SUCCESS);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit updateIPStatus method.takeTime:" + takeTime + "ms");
		}
        return null;
    }
    
    public String findAllFreeIPDetail() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findAllFreeIPDetail method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
        List <IPDetailInfoVO> list=null;
        try{
            list = facade.findAvailableIPDetailOfServerZone(serverZone);
        }catch (Exception ex) {
            dealThrow(new HsCloudException(Constants.IP_FIND_EXCEPTION, "findAllFreeIPDetail Exception:", logger, ex),
                    Constants.IP_EXCEPTION);
        }
        super.fillActionResult(list);
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findAllFreeIPDetail method.takeTime:" + takeTime + "ms");
		}
        return null;
    }
    

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Facade getFacade() {
        return facade;
    }

    public void setFacade(Facade facade) {
        this.facade = facade;
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

    public long getStartIP() {
        return startIP;
    }

    public void setStartIP(long startIP) {
        this.startIP = startIP;
    }

    public long getEndIP() {
        return endIP;
    }

    public void setEndIP(long endIP) {
        this.endIP = endIP;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Page<IPRangeVO> getPageIPRVO() {
        return pageIPRVO;
    }

    public void setPageIPRVO(Page<IPRangeVO> pageIPRVO) {
        this.pageIPRVO = pageIPRVO;
    }

    public Page<IPDetailVO> getPageIPDVO() {
        return pageIPDVO;
    }

    public void setPageIPDVO(Page<IPDetailVO> pageIPDVO) {
        this.pageIPDVO = pageIPDVO;
    }

    public long getRangeId() {
        return rangeId;
    }

    public void setRangeId(long rangeId) {
        this.rangeId = rangeId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDetailIds() {
        return detailIds;
    }

    public void setDetailIds(String detailIds) {
        this.detailIds = detailIds;
    }

    public Long getIp() {
		return ip;
	}

	public void setIp(Long ip) {
		this.ip = ip;
	}

	public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getVmName() {
        return vmName;
    }

    public void setVmName(String vmName) {
        this.vmName = vmName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

	public IPRangeVO getIpRangeVO() {
		return ipRangeVO;
	}

	public void setIpRangeVO(IPRangeVO ipRangeVO) {
		this.ipRangeVO = ipRangeVO;
	}

	public ServerZone getServerZone() {
		return serverZone;
	}

	public void setServerZone(ServerZone serverZone) {
		this.serverZone = serverZone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

}

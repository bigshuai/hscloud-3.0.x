/* 
* 文 件 名:  DomainAction.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-3-13 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.action; 

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.vo.ActionResult;
import com.hisoft.hscloud.common.vo.UserBrandVO;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.util.Constant;
import com.hisoft.hscloud.crm.usermanager.util.Utils;
import com.hisoft.hscloud.crm.usermanager.vo.DomainVO;
import com.hisoft.hscloud.web.facade.Facade;

/** 
 * <分平台操作> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-3-13] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class DomainAction extends HSCloudAction {

    private static final long serialVersionUID = 4982122547160728491L;
    private Logger logger = Logger.getLogger(this.getClass());
    private String query;//查询条件
    private int page; //页数
    private int limit;//每页数量
    private DomainVO domainVO = new DomainVO();//分平台对象
    private Page<UserBrandVO> pageBrand = new Page<UserBrandVO>(); 
    private long domainId;
    private String abbreviation;
    private Long[] brandIds;
    @Autowired
    private Facade facade;
    /**
     * <获取所有分平台> 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void getAllDomain() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAllDomain method.");			
		}
        try{
            List<Domain> list = facade.getAllDomain();
            this.fillActionResult(list);
        } catch(Exception ex) {
            this.dealThrow(Constants.DOMAIN_GET_ALL_EXCEPTION,ex, logger);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAllDomain method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * <查询分平台页> 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void findDomainPage() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findDomainPage method.");			
		}
        try{
            Page<Domain> pageDomain = new Page<Domain>();
            pageDomain.setPageNo(page);
            pageDomain.setPageSize(limit);
            if (!StringUtils.isEmpty(query)) { // 如果查询框不为空时，进入模糊查询
                query = query.trim();
                query = new String(query.getBytes("iso8859_1"), "UTF-8");
            }
            pageDomain = this.facade.findDomainPage(pageDomain, query);
            this.fillActionResult(pageDomain);
        } catch(Exception ex) {
            this.dealThrow(Constants.DOMAIN_GET_ALL_EXCEPTION, ex, logger);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findDomainPage method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * <启用分平台> 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void enableDomain() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter enableDomain method.");			
		}
        Admin admin = (Admin)super.getCurrentLoginUser();
        try {
            domainVO = Utils.strutsJson2Object(DomainVO.class);
            facade.updateStatusDomain(domainVO.getId(), admin.getId(), Constant.DOMAIN_STATUS_VALID);
            facade.insertOperationLog(admin,"启用分平台","启用分平台",Constants.RESULT_SUCESS);
        } catch (Exception ex) {
            facade.insertOperationLog(admin,"启用分平台错误:"+ex.getMessage(),"启用分平台",Constants.RESULT_FAILURE);
            this.dealThrow(Constants.DOMAIN_EXCEPTION, ex, logger);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit enableDomain method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * <禁用分平台> 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void disableDomain() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter disableDomain method.");			
		}
        Admin admin = (Admin)super.getCurrentLoginUser();
        try {
            domainVO = Utils.strutsJson2Object(DomainVO.class);
            facade.updateStatusDomain(domainVO.getId(), admin.getId(), Constant.DOMAIN_STATUS_INVALID);
            facade.insertOperationLog(admin,"禁用分平台","禁用分平台",Constants.RESULT_SUCESS);
        } catch (Exception ex) {
            facade.insertOperationLog(admin,"禁用分平台错误:"+ex.getMessage(),"禁用分平台",Constants.RESULT_FAILURE);
            this.dealThrow(Constants.DOMAIN_EXCEPTION, ex, logger);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit disableDomain method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * <编辑分平台> 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void editDomain() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter editDomain method.");			
		}
        Admin admin = (Admin)super.getCurrentLoginUser();
        try {
            Object result = facade.editDomain(domainVO, admin.getId());
            this.fillActionResult(result);
            facade.insertOperationLog(admin,"编辑分平台","编辑分平台",Constants.RESULT_SUCESS);
        } catch (Exception ex) {
            facade.insertOperationLog(admin,"编辑分平台错误:"+ex.getMessage(),"编辑分平台",Constants.RESULT_FAILURE);
            this.dealThrow(Constants.DOMAIN_EDIT_EXCEPTION, ex, logger);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit editDomain method.takeTime:" + takeTime + "ms");
		}
    }
    
    public String findRelatedBrandByPage() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findRelatedBrandByPage method.");			
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
        	pageBrand.setPageNo(pageNo);
        	pageBrand.setPageSize(pageSize);            
            facade.getRelatedBrand(pageBrand, domainId, query);            
            super.fillActionResult(pageBrand);
        } catch (Exception ex) {
            dealThrow(new HsCloudException(Constants.GET_ALL_BRAND_PAGING_ERROR,
                    "findRelatedBrandByPage Exception:", logger, ex), Constants.GET_ALL_BRAND_PAGING_ERROR);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findRelatedBrandByPage method.takeTime:" + takeTime + "ms");
		}
        return null;
    }
	public String findUnRelatedBrandByPage() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findUnRelatedBrandByPage method.");			
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
        	pageBrand.setPageNo(pageNo);
        	pageBrand.setPageSize(pageSize);            
            facade.getUnRelatedBrand(pageBrand, domainId, query);            
            super.fillActionResult(pageBrand);
        } catch (Exception ex) {
            dealThrow(new HsCloudException(Constants.GET_ALL_BRAND_PAGING_ERROR,
                    "findUnRelatedBrandByPage Exception:", logger, ex), Constants.GET_ALL_BRAND_PAGING_ERROR);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findUnRelatedBrandByPage method.takeTime:" + takeTime + "ms");
		}
        return null;
    }
	public String associateBrandAndDomain(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter associateBrandAndDomain method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}		
		ActionResult result = getActionResult();		
		try {
			boolean resultFlag = facade.associateBrandAndDomain(brandIds, domainId);
			if(!resultFlag){
				super.fillActionResult(Constants.DOMAIN_EDIT_EXCEPTION);
				return null;
			}			
		}catch (Exception ex) {			
			result.setSuccess(false);
			result.setResultCode(Constants.OPTIONS_FAILURE);
			dealThrow(new HsCloudException(Constants.DOMAIN_EDIT_EXCEPTION,
					"associateBrandAndDomain Exception:", logger, ex), Constants.DOMAIN_EDIT_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit associateBrandAndDomain method.takeTime:" + takeTime + "ms");
		}
		return null;		
	}
	public String disAssociateBrandAndDomain(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter disAssociateBrandAndDomain method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}		
		ActionResult result = getActionResult();		
		try {
			boolean resultFlag = facade.disAssociateBrandAndDomain(brandIds, domainId);
			if(!resultFlag){
				super.fillActionResult(Constants.DOMAIN_EDIT_EXCEPTION);
				return null;
			}			
		}catch (Exception ex) {			
			result.setSuccess(false);
			result.setResultCode(Constants.OPTIONS_FAILURE);
			dealThrow(new HsCloudException(Constants.DOMAIN_EDIT_EXCEPTION,
					"disAssociateBrandAndDomain Exception:", logger, ex), Constants.DOMAIN_EDIT_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit disAssociateBrandAndDomain method.takeTime:" + takeTime + "ms");
		}
		return null;		
	}
	
	public String findRelatedBrandByDomainId() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findRelatedBrandByDomainId method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}  
		List<UserBrandVO> userBrandList = null;
        try {        	         
        	userBrandList = facade.getRelatedBrandByDomainId(domainId);            
            super.fillActionResult(userBrandList);
        } catch (Exception ex) {
            dealThrow(new HsCloudException(Constants.GET_ALL_BRAND_PAGING_ERROR,
                    "findRelatedBrandByDomainId Exception:", logger, ex), Constants.GET_ALL_BRAND_PAGING_ERROR);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findRelatedBrandByDomainId method.takeTime:" + takeTime + "ms");
		}
        return null;
    }
	
	public String findRelatedBrand() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findRelatedBrand method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}  
		List<UserBrandVO> userBrandList = null;
        try {        	         
        	userBrandList = facade.getRelatedBrand(abbreviation);            
            super.fillActionResult(userBrandList);
        } catch (Exception ex) {
            dealThrow(new HsCloudException(Constants.GET_ALL_BRAND_PAGING_ERROR,
                    "findRelatedBrand Exception:", logger, ex), Constants.GET_ALL_BRAND_PAGING_ERROR);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findRelatedBrand method.takeTime:" + takeTime + "ms");
		}
        return null;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
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

    public DomainVO getDomainVO() {
        return domainVO;
    }

    public void setDomainVO(DomainVO domainVO) {
        this.domainVO = domainVO;
    }
    
	public Page<UserBrandVO> getPageBrand() {
		return pageBrand;
	}

	public void setPageBrand(Page<UserBrandVO> pageBrand) {
		this.pageBrand = pageBrand;
	}

	public long getDomainId() {
		return domainId;
	}

	public void setDomainId(long domainId) {
		this.domainId = domainId;
	}

	public Long[] getBrandIds() {
		return brandIds;
	}

	public void setBrandIds(Long[] brandIds) {
		this.brandIds = brandIds;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
    
}
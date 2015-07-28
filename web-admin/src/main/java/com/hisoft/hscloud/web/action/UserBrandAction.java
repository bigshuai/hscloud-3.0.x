/* 
 * 文 件 名:  UserBrandAction.java 
 * 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
 * 描    述:  <描述> 
 * 修 改 人:  houyh 
 * 修改时间:  2013-1-24 
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
import com.hisoft.hscloud.common.entity.UserBrand;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.web.facade.Facade;

/**
 * <用户品牌请求处理> <功能详细描述>
 * 
 * @author houyh
 * @version [版本号, 2013-1-24]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class UserBrandAction extends HSCloudAction {
	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = -4005164936577426019L;
	private static Logger logger = Logger.getLogger(UserBrandAction.class);
	@Autowired
	private Facade facade;
	private Long brandId;
	private long referenceId;
	private Page<UserBrand> paging=new Page<UserBrand>();
	private String name;
	private int page;
	private int limit;
	private short status;
	private boolean approveOrNot;
	private Integer rebateRate;// 返点率
	private Integer giftsDiscountRate;// 礼金率
	private String query;
	private String description;

	/**
	 * <添加品牌数据> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void addBrand() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter addBrand method.");			
		}
		try {
			facade.addBrand(name, description,rebateRate,giftsDiscountRate,approveOrNot);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "add brand exception",
					logger, e),Constants.ADD_USER_BRAND_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit addBrand method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void modifyBrand() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter modifyBrand method.");			
		}
		try {
			facade.modifyBrand(brandId, name, description,status,rebateRate,giftsDiscountRate,approveOrNot);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "modify brand exception",
					logger, e),Constants.MODIFY_USER_BRAND_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit modifyBrand method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <获取所有非删除状态品牌> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void getAllNormalBrand() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAllNormalBrand method.");			
		}
		try {
			List<UserBrand> result=facade.getAllNormalBrand();
			super.fillActionResult(result);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "get all normal brand exception",
					logger, e),Constants.GET_ALL_NORMAL_BRAND_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAllNormalBrand method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <分页获取品牌信息> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void getBrandByPage() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getBrandByPage method.");			
		}
		try {
			paging.setPageSize(limit);
			paging.setPageNo(page);
			if(StringUtils.isNotBlank(query)){
				query=new String(query.getBytes("ISO8859_1"),"UTF-8");
			}
			paging=facade.getBrandByPage(query, paging);
			super.fillActionResult(paging);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "get all brand by page exception",
					logger, e),Constants.GET_ALL_BRAND_PAGING_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getBrandByPage method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <根据物理Id删除品牌，逻辑删除> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void deleteBrand() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter deleteBrand method.");			
		}
		try {
			facade.deleteBrandById(brandId);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "delete brand exception",
					logger, e),Constants.DELETE_USER_BRAND_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit deleteBrand method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <校验品牌名称是否重复> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void checkBrandNameDup() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter checkBrandNameDup method.");			
		}
		try {
			boolean result=facade.checkBrandNameDup(name);
			super.getActionResult().setResultObject(result);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "checkBrandNameDup exception",
					logger, e),Constants.CHECK_BRAND_NAME_DUP_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit checkBrandNameDup method.takeTime:" + takeTime + "ms");
		}
	}
	
	public void enableBrand(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter enableBrand method.");			
		}
		try {
			facade.enableBrandById(brandId);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "enable brand exception",
					logger, e),Constants.ENABLE_USER_BRAND_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit enableBrand method.takeTime:" + takeTime + "ms");
		}
	}
	
	public void getUserUserBrandInfo(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getUserUserBrandInfo method.");			
		}
		try {
			super.fillActionResult(facade.getUserAndBrandByReferenceId(referenceId));
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "getUserUserBrandInfo exception",
					logger, e),Constants.ENABLE_USER_BRAND_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getUserUserBrandInfo method.takeTime:" + takeTime + "ms");
		}
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Page<UserBrand> getPaging() {
		return paging;
	}

	public void setPaging(Page<UserBrand> paging) {
		this.paging = paging;
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

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public Integer getRebateRate() {
		return rebateRate;
	}

	public void setRebateRate(Integer rebateRate) {
		this.rebateRate = rebateRate;
	}

	public boolean getApproveOrNot() {
		return approveOrNot;
	}

	public void setApproveOrNot(boolean approveOrNot) {
		this.approveOrNot = approveOrNot;
	}

	public long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(long referenceId) {
		this.referenceId = referenceId;
	}

	public Integer getGiftsDiscountRate() {
		return giftsDiscountRate;
	}

	public void setGiftsDiscountRate(Integer giftsDiscountRate) {
		this.giftsDiscountRate = giftsDiscountRate;
	}
	
}
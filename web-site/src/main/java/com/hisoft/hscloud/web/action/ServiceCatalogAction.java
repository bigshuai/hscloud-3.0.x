package com.hisoft.hscloud.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.sc.entity.ServiceCatalog;
import com.hisoft.hscloud.bss.sla.sc.utils.SCUtil;
import com.hisoft.hscloud.bss.sla.sc.vo.ScFeeTypeVo;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.entity.UserBrand;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.web.facade.Facade;

/**
 * @description 处理对service catalog的操作请求
 * @version 1.0
 * @author jiaquan.hu
 * @update 2012-3-31 上午9:41:52
 */
public class ServiceCatalogAction extends HSCloudAction {
	private static final long serialVersionUID = 2593476475769370987L;

	private Logger logger = Logger.getLogger(ServiceCatalogAction.class);

	private Page<ServiceCatalog> paging = null;

	private ServiceCatalog serviceCatalog;
	private Long zoneGroupId;

	private int start;

	private int limit = 5;
	
	private long referenceId;

	private int page;
	
	private String sort;

	@Autowired
	private Facade facade;


	/**web-site 获取在已经审批effectiveDate<=now expirationDate>now的套餐
	 * <一句话功能简述> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void getAllSC() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAllSC method.");			
		}
		try {
			Map<String ,Object> map = new HashMap<String, Object>();
			serviceCatalog = serviceCatalog == null ? new ServiceCatalog()
					: serviceCatalog;
			List<Sort> sortList = null;
			if (StringUtils.isNotBlank(sort)) {
				sortList = SCUtil.json2Object(sort,
						new TypeReference<List<Sort>>() {
						});
			}
			User user=getCurrentUser();
			String userLevel=null;
			Long domainId=null;
			if(user!=null){
				userLevel=user.getLevel();
				domainId=user.getDomain().getId();
			}
			List<ServiceCatalog> result=new ArrayList<ServiceCatalog>();
			if(StringUtils.isNotBlank(userLevel)&&domainId!=null&&domainId.longValue()!=0L){
				UserBrand brand=facade.getBrandByCode(userLevel);
				map.put("rebateRate", brand.getRebateRate());
				if(brand!=null&&brand.getStatus()==1){
				 result=facade.getAllSC(sortList,userLevel,domainId,zoneGroupId);
				 for(ServiceCatalog sc:result){
					 sc.getDomainList().get(0).getUserBrandList().clear();
					 sc.setUserBrand(null);
					 sc.setDomainList(null);
				 }
				}
			}
			map.put("totalCount",result.size());
			map.put("result", result);
			
			fillActionResult(map);
		} catch (Exception e) {
			dealThrow(
					new HsCloudException("", "分页获取套餐信息异常，pageServiceCatalogs()", logger, e),
					Constants.GET_SC_BY_PAGE_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAllSC method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <根据ReferenceId获取套餐的计费规则信息> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void getScFeeTypeByReferenceId(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getScFeeTypeByReferenceId method.");			
		}
		try {
			List<ScFeeTypeVo> scFeeTypeList=facade.getScFeeTypeByReferenceId(referenceId);
			fillActionResult(scFeeTypeList);
		} catch (Exception e) {
			dealThrow(
					new HsCloudException("", "根据orderItemId获取套餐的计费规则信息 exception", logger, e),"");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getScFeeTypeByReferenceId method.takeTime:" + takeTime + "ms");
		}
	}

	private User getCurrentUser(){
		User user=null;
		Object obj=super.getCurrentLoginUser();
		if(obj!=null){
			user=(User)obj;
		}
		return user;
	}
	
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public Page<ServiceCatalog> getPaging() {
		return paging;
	}

	public void setPaging(Page<ServiceCatalog> paging) {
		this.paging = paging;
	}

	public ServiceCatalog getServiceCatalog() {

		return serviceCatalog;
	}

	public void setServiceCatalog(ServiceCatalog serviceCatalog) {
		this.serviceCatalog = serviceCatalog;
	}

	public long getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(long referenceId) {
		this.referenceId = referenceId;
	}
	public Long getZoneGroupId() {
		return zoneGroupId;
	}
	public void setZoneGroupId(Long zoneGroupId) {
		this.zoneGroupId = zoneGroupId;
	}
	
	
}

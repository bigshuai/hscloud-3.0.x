package com.hisoft.hscloud.web.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.sc.entity.Os;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceCatalog;
import com.hisoft.hscloud.bss.sla.sc.utils.SCUtil;
import com.hisoft.hscloud.bss.sla.sc.vo.SCVo;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.common.vo.ActionResult;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.vo.DomainVO;
import com.hisoft.hscloud.web.facade.Facade;

/**
 * 
 * <套餐Action> <功能详细描述>
 * 
 * @author houyh
 * @version [1.4, 2012-12-3]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ServiceCatalogAction extends HSCloudAction {
	private static final long serialVersionUID = 2593476475769370987L;

	private Logger logger = Logger.getLogger(ServiceCatalogAction.class);

	private Page<ServiceCatalog> paging = null;

	private ServiceCatalog serviceCatalog;

	private int start;

	private int limit = 5;

	private Long brandId;
	
	private Long zoneId;
	
	private Long domainId;

	private String effectiveDateParam;
	private String copySCName;
	private String experitionDateParam;
	private long referenceId;

	private int page;

	private String query = "";

	private String sort;

	private Integer[] scIds;
	
	private String scCode;
	
	private String domainCode;
	
	private String serviceCatalogCode;

	@Autowired
	private Facade facade;

	/**
	 * <根据id获取service catalog的信息> <功能详细描述>
	 * 
	 * @see [ServiceCatalogAction、ServiceCatalogAction#get、类#成员]
	 */
	public void get() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter get method.");
		}
		try {
			serviceCatalog = facade.getServiceCatalogById(serviceCatalog
					.getId());
			ActionResult result = getActionResult();

			if (serviceCatalog != null) {
				result.setSuccess(true);
				result.setResultObject(serviceCatalog);
			} else {
				result.setSuccess(false);
				result.setResultCode(Constants.SC_NOT_EXISTS);
			}
		} catch (Exception ex) {
			dealThrow(
					new HsCloudException("", "根据套餐Id获取套餐异常，get()", logger, ex),
					Constants.GET_SC_BY_ID_ERROR, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit get method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <根据套餐Id获取套餐中操作系统> <功能详细描述>
	 * 
	 * @see [ServiceCatalogAction、ServiceCatalogAction#getOs、类#成员]
	 */
	public void getOs() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getOs method.");
		}
		Os os = facade.getOsByServicecatalogId(serviceCatalog.getId());
		try {
			if (os != null) {
				fillActionResult(os);
			} else {
				fillActionResult(Constants.SC_NOT_EXISTS);
			}
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "根据套餐Id获取套餐中操作系统异常，getOS()",
					logger, e), Constants.GET_OS_BY_ID_ERROR, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getOs method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <保存套餐数据> <功能详细描述>
	 * 
	 * @see [ServiceCatalogAction、ServiceCatalogAction#save、类#成员]
	 */
	public void save() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter save method.");
		}
		ActionResult result = getActionResult();
		Admin admin = null;
		try {
			admin = (Admin)super.getCurrentLoginUser();
			serviceCatalog = SCUtil.strutsJson2Object(ServiceCatalog.class);
			logger.info("serviceCatalog name" + serviceCatalog.getName());
			logger.info("serviceCatalog code" + serviceCatalog.getCatalogCode());
//			if(facade.hasServiceCatalogCode(serviceCatalog)){
//				result.setSuccess(false);
//				result.setResultCode(Constants.SC_CODE_EXISTS);
//				return;
//			}
			SimpleDateFormat sfTime = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String effectiveDateStr = effectiveDateParam + " 00:00:00";
			String expirationDateStr = experitionDateParam + " 23:59:59";
			logger.info("effectiveDateStr:" + effectiveDateStr);
			logger.info("expirationDateStr:" + expirationDateStr);
			Date effectiveDate = sfTime.parse(effectiveDateStr);
			Date expirationDate = sfTime.parse(expirationDateStr);
			serviceCatalog.setEffectiveDate(effectiveDate);
			serviceCatalog.setExpirationDate(expirationDate);
			if (!facade.checkImageSizeVSDiskCapacity(serviceCatalog)) {
				result.setSuccess(false);
				result.setResultCode(Constants.IMAGE_SIZE_GT_DISK_SIZE);
				return;
			}
			
			
			facade.saveSC(serviceCatalog);
			result.setSuccess(true);
			facade.insertOperationLog(admin,"保存套餐","保存套餐",Constants.RESULT_SUCESS);
		} catch (Exception e) {
			facade.insertOperationLog(admin,"保存套餐错误："+e.getMessage(),"保存套餐",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException("", "保存套餐异常，save()", logger, e),
					Constants.SAVE_SC_ERROR, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit save method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <检查套餐是否重名> <功能详细描述>
	 * 
	 * @see 
	 *      [ServiceCatalogAction、ServiceCatalogAction#checkServiceCatalogName、类#
	 *      成员]
	 */
	public void checkServiceCatalogName() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter checkServiceCatalogName method.");
		}
		try {
			int delieveId = serviceCatalog.getId();
			ActionResult result = getActionResult();
			String name=serviceCatalog.getName();
//			if(StringUtils.isNotBlank(name)){
//				name=new String(name.getBytes("iso8859-1"),"UTF-8");
//			}
			serviceCatalog = facade.getSCByName(name);
			if (serviceCatalog != null && serviceCatalog.getId() != delieveId) {
				result.setResultObject("true");
			} else {
				result.setResultObject("false");
			}
			
			
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"检查套餐是否同名异常，checkServiceCatalogName()", logger, e),
					Constants.CHECK_SC_NAME_DUPLICATE_ERROR, true);
			dealThrow(new HsCloudException("",
					"检查code是否同名异常，checkServiceCatalogCode()", logger, e),
					Constants.SC_CODE_EXISTS, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit checkServiceCatalogName method.takeTime:"
					+ takeTime + "ms");
		}
	}

	/**
	 * <处理对service catalog 的分页请求> <功能详细描述>
	 * 
	 * @see [ServiceCatalogAction、ServiceCatalogAction#pageServiceCatalogs、类#成员]
	 */
	public void pageServiceCatalogs() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter pageServiceCatalogs method.");
		}
		try {
			paging = new Page<ServiceCatalog>(limit);
			paging.setPageNo(page);
			serviceCatalog = serviceCatalog == null ? new ServiceCatalog()
					: serviceCatalog;

			if (StringUtils.isNotBlank(query)) {
				query = new String(query.getBytes("iso8859_1"), "UTF-8");
			}

			List<Sort> sortList = null;

			if (StringUtils.isNotBlank(sort)) {
				sortList = SCUtil.json2Object(sort,
						new TypeReference<List<Sort>>() {
						});
			}
			serviceCatalog.setName(query);
			ActionResult result = getActionResult();
			result.setSuccess(true);
			result.setResultObject(facade.getSCByPage(paging, serviceCatalog,
					sortList, brandId,zoneId,domainId));
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"分页获取套餐信息异常，pageServiceCatalogs()", logger, e),
					Constants.GET_SC_BY_PAGE_ERROR, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit pageServiceCatalogs method.takeTime:" + takeTime
					+ "ms");
		}
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @see [ServiceCatalogAction、ServiceCatalogAction#disable、类#成员]
	 */
	public void disable() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter disable method.");
		}
		Admin admin=null;
		try {
			admin=(Admin)super.getCurrentLoginUser();
			facade.disableSC(serviceCatalog.getId());
			facade.insertOperationLog(admin,"停用套餐","停用套餐",Constants.RESULT_SUCESS);
		} catch (Exception e) {
			facade.insertOperationLog(admin,"停用套餐错误："+e.getMessage(),"停用套餐",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException("", "停用套餐异常，disable()", logger, e),
					Constants.DISABLE_SC_ERROR, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit disable method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @see [ServiceCatalogAction、ServiceCatalogAction#enable、类#成员]
	 */
	public void enable() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter enable method.");
		}
		Admin admin=null;
		try {
			admin=(Admin)super.getCurrentLoginUser();
			facade.enableSC(serviceCatalog.getId());
			facade.insertOperationLog(admin,"启用套餐","启用套餐",Constants.RESULT_SUCESS);
		} catch (Exception e) {
			facade.insertOperationLog(admin,"启用套餐错误:"+e.getMessage(),"启用套餐",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException("", "启用套餐异常，enable()", logger, e),
					Constants.ENABLE_SC_ERROR, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit enable method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 仅试用（套餐只能试用，不能购买）
	 * 
	 * @see [ServiceCatalogAction、ServiceCatalogAction#enable、类#成员]
	 */
	public void onlyTry() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter onlyTry method.");
		}
		Admin admin=null;
		try {
			admin=(Admin)super.getCurrentLoginUser();
			facade.onlyTrySC(serviceCatalog.getId());
			facade.insertOperationLog(admin,"套餐仅试用","套餐仅试用",Constants.RESULT_SUCESS);
		} catch (Exception e) {
			facade.insertOperationLog(admin,"套餐仅试用错误:"+e.getMessage(),"仅试用套餐",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException("", "套餐仅试用异常，onlyTry()", logger, e),
					Constants.ENABLE_SC_ERROR, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit onlyTry method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @see [ServiceCatalogAction、ServiceCatalogAction#approve、类#成员]
	 */
	public void approve() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter approve method.");
		}
		Admin admin=null;
		try {
			admin=(Admin)super.getCurrentLoginUser();
			facade.approveSC(serviceCatalog.getId());
			facade.insertOperationLog(admin,"审核套餐","审核套餐",Constants.RESULT_SUCESS);
		} catch (Exception e) {
			facade.insertOperationLog(admin,"审核套餐错误："+e.getMessage(),"审核套餐",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException("", "审批套餐异常，approve()", logger, e),
					Constants.APPROVE_SC_ERROR, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit approve method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <套餐克隆> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void copySc() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter copySc method.");
		}
		Admin admin = null;
		ActionResult result = getActionResult();
		try {
			admin=(Admin)super.getCurrentLoginUser();
			facade.copySc(serviceCatalog.getId(), copySCName);
			facade.insertOperationLog(admin,"克隆套餐","克隆套餐",Constants.RESULT_SUCESS);
			result.setResultObject(true);
			
			
		} catch (Exception e) {
			facade.insertOperationLog(admin,"克隆套餐错误："+e.getMessage(),"克隆套餐",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException("", "审批套餐异常，approve()", logger, e),
					Constants.APPROVE_SC_ERROR, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit copySc method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <获取与brand关联的所有套餐> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void getRelatedScByBrandId() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getRelatedScByBrandId method.");
		}
		try {
			Page<SCVo> paging = new Page<SCVo>(limit);
			paging.setPageNo(page);
			if (StringUtils.isNotBlank(query)) {
				query = new String(query.getBytes("iso8859_1"), "UTF-8");
			}
			Page<SCVo> result = facade.getRelatedSCByBrandId(brandId, paging,
					query);
			super.fillActionResult(result);
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"getRelatedScByBrandId Exception", logger, e), "");
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getRelatedScByBrandId method.takeTime:"
					+ takeTime + "ms");
		}
	}
	
	

	/**
	 * <获取虚拟机所有者所属品牌关联的所有套餐> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void getRelatedScByReferenceId() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getRelatedScByReferenceId method.");
		}
		try {
			List<SCVo> result = facade.getRelatedScByReferenceId(referenceId);
			super.fillActionResult(result);
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"getRelatedScByReferenceId Exception", logger, e), "");
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getRelatedScByReferenceId method.takeTime:"
					+ takeTime + "ms");
		}
	}


	/**
	 * <获取与brand没有关联的所有套餐> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void getUnRelatedScByBrandId() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getUnRelatedScByBrandId method.");
		}
		try {
			Page<SCVo> paging = new Page<SCVo>(limit);
			paging.setPageNo(page);
			if (StringUtils.isNotBlank(query)) {
				query = new String(query.getBytes("iso8859_1"), "UTF-8");
			}
			Page<SCVo> result = facade.getUnRelatedScByBrandId(brandId, paging,
					query);
			super.fillActionResult(result);
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"getUnRelatedScByBrandId Exception", logger, e), "");
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getUnRelatedScByBrandId method.takeTime:"
					+ takeTime + "ms");
		}
	}

	/**
	 * <关联品牌与套餐> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void brandRelateSCOperation() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter brandRelateSCOperation method.");
		}
		try {
			facade.brandRelateSCOperation(scIds, brandId);
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"brandRelateSCOperation Exception", logger, e),
					Constants.RELATED_USER_BRAND_SC_ERROR, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit brandRelateSCOperation method.takeTime:"
					+ takeTime + "ms");
		}
	}

	/**
	 * <解除品牌与套餐的关联> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void brandUnRelateSCOperation() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter brandUnRelateSCOperation method.");
		}
		try {
			facade.brandUnRelateSCOperation(scIds, brandId);
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"brandUnRelateSCOperation Exception", logger, e),
					Constants.UNRELATED_USER_BRAND_SC_ERROR, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit brandUnRelateSCOperation method.takeTime:"
					+ takeTime + "ms");
		}
	}
	
	public void checkServiceCatalogCode(){
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter checkServiceCatalogCode method.");
		}
		try {
			ActionResult result = getActionResult();
			
			if(facade.hasServiceCatalogCode(serviceCatalog)){
				result.setResultObject("true");
			}else{
				result.setResultObject("false");
			}
			
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"检查code是否同名异常，checkServiceCatalogCode()", logger, e),
					Constants.SC_CODE_EXISTS, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit checkServiceCatalogCode method.takeTime:"
					+ takeTime + "ms");
		}
		
	}
	

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
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

	public String getEffectiveDateParam() {
		return effectiveDateParam;
	}

	public void setEffectiveDateParam(String effectiveDateParam) {
		this.effectiveDateParam = effectiveDateParam;
	}

	public String getExperitionDateParam() {
		return experitionDateParam;
	}

	public void setExperitionDateParam(String experitionDateParam) {
		this.experitionDateParam = experitionDateParam;
	}

	public String getCopySCName() {
		return copySCName;
	}

	public void setCopySCName(String copySCName) {
		this.copySCName = copySCName;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public Integer[] getScIds() {
		return scIds;
	}

	public void setScIds(Integer[] scIds) {
		this.scIds = scIds;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(long referenceId) {
		this.referenceId = referenceId;
	}

	public Long getDomainId() {
		return domainId;
	}

	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}

	public String getScCode() {
		return scCode;
	}

	public void setScCode(String scCode) {
		this.scCode = scCode;
	}

	public String getDomainCode() {
		return domainCode;
	}

	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}

	public String getServiceCatalogCode() {
		return serviceCatalogCode;
	}

	public void setServiceCatalogCode(String serviceCatalogCode) {
		this.serviceCatalogCode = serviceCatalogCode;
	}
	
	
}

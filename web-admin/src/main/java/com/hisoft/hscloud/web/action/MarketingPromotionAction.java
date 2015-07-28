//$Id: MarketingPromotionAction.java Sep 18, 2013 11:03:34 AM liyunhui  $begin:~
package com.hisoft.hscloud.web.action;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.entity.MarketingPromotion;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.vo.MarketingPromotionVO;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.web.facade.Facade;

/**
 * @className: MarketingPromotionAction
 * @package: com.hisoft.hscloud.web.action
 * @description: TODO
 * @author: liyunhui
 * @since: 1.0
 * @createTime: Sep 18, 2013 11:03:34 AM
 * @company: Pactera Technology International Ltd
 */
public class MarketingPromotionAction extends HSCloudAction {

	private static final long serialVersionUID = 7487364656125413559L;
	private static Logger logger = Logger.getLogger(MarketingPromotionAction.class);
	@Autowired
	private Facade facade;
	private int page;
	private int limit;
	private String query;
	private Page<MarketingPromotion> paging = new Page<MarketingPromotion>();
	private Page<MarketingPromotionVO> paging_VO = new Page<MarketingPromotionVO>();
	private Long marketingPromotionId;// hc_marketing_promotion的主键
	private String name;// 推广名称
	private String code;// 推广代码
	private String address;// 推广链接地址
	private Long domain_id;// 所属分平台的id
	private String domain_abbreviation;// 所属分平台的缩写名称
	private String brand_code;// 关联品牌的code
	private String brand_name;// 关联品牌的名称
	private String description;// 描述
	
	/**
	 * <分页获取市场营销> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void getMarketingPromotionByPage() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getMarketingPromotionByPage method.");			
		}
		try {
			paging.setPageSize(limit);
			paging.setPageNo(page);
			if(StringUtils.isNotBlank(query)){
				query=new String(query.getBytes("ISO8859_1"),"UTF-8");
			}
			paging_VO = facade.getMarketingPromotionByPage(query, paging);
			super.fillActionResult(paging_VO);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "getMarketingPromotionByPage exception",
					logger, e),Constants.GET_MARKETINGPROMOTION_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getMarketingPromotionByPage method.takeTime:" + takeTime + "ms");
		}
	}	

	/**
	 * <校验推广名称是否重复> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void checkMarketingPromotionNameDup() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter checkMarketingPromotionNameDup method.");			
		}
		try {
			boolean result = facade.checkMarketingPromotionParameterDup(name,null, null);
			super.getActionResult().setResultObject(result);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "checkMarketingPromotionNameDup exception",
					logger, e),Constants.CHECK_MARKETINGPROMOTIONNAMEDUP_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit checkMarketingPromotionNameDup method.takeTime:" + takeTime + "ms");
		}
	}	
	
	/**
	 * <校验推广代码是否重复> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void checkMarketingPromotionCodeDup() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter checkMarketingPromotionCodeDup method.");			
		}
		try {
			boolean result = facade.checkMarketingPromotionParameterDup(null, code, null);
			super.getActionResult().setResultObject(result);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "checkMarketingPromotionCodeDup exception",
					logger, e),Constants.CHECK_MARKETINGPROMOTIONCODEDUP_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit checkMarketingPromotionCodeDup method.takeTime:" + takeTime + "ms");
		}
	}		
	
	/**
	 * <校验推广地址是否重复> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void checkMarketingPromotionUrlDup() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter checkMarketingPromotionUrlDup method.");			
		}
		try {
			boolean result = facade.checkMarketingPromotionParameterDup(null, null, address);
			super.getActionResult().setResultObject(result);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "checkMarketingPromotionUrlDup exception",
					logger, e),Constants.CHECK_MARKETINGPROMOTIONURLDUP_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit checkMarketingPromotionUrlDup method.takeTime:" + takeTime + "ms");
		}
	}		
	
	/**
	 * <添加市场推广> <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	public void addMarketingPromotion() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter addMarketingPromotion method.");			
		}
		try {
			Admin admin = (Admin) getCurrentLoginUser();
			facade.addMarketingPromotion(name, code, address, domain_id, brand_code, description,admin);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "addMarketingPromotion exception",
					logger, e),Constants.ADD_MARKETINGPROMOTION_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit addMarketingPromotion method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * <修改市场推广> <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	public void modifyMarketingPromotion() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter modifyMarketingPromotion method.");			
		}
		try {
			Admin admin = (Admin) getCurrentLoginUser();
			facade.modifyMarketingPromotion(marketingPromotionId, name, address, 
					domain_id, brand_code, description, admin);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "modifyMarketingPromotion exception",
					logger, e),Constants.MODIFYMARKETINGPROMOTION_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit modifyMarketingPromotion method.takeTime:" + takeTime + "ms");
		}
	}
	
	
	/**
	 * <启用市场推广> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void enableMarketingPromotion(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter enableMarketingPromotion method.");			
		}
		try {
			facade.enableMarketingPromotionById(marketingPromotionId);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "enableMarketingPromotion exception",
					logger, e),Constants.ENABLE_USER_BRAND_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit enableMarketingPromotion method.takeTime:" + takeTime + "ms");
		}
	}	
	
	/**
	 * <停用市场推广> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void disableMarketingPromotion() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter disableMarketingPromotion method.");			
		}
		try {
			facade.disableMarketingPromotionById(marketingPromotionId);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "disableMarketingPromotion exception",
					logger, e),Constants.DELETE_USER_BRAND_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit disableMarketingPromotion method.takeTime:" + takeTime + "ms");
		}
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

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Page<MarketingPromotion> getPaging() {
		return paging;
	}

	public void setPaging(Page<MarketingPromotion> paging) {
		this.paging = paging;
	}

	public Page<MarketingPromotionVO> getPaging_VO() {
		return paging_VO;
	}

	public void setPaging_VO(Page<MarketingPromotionVO> paging_VO) {
		this.paging_VO = paging_VO;
	}

	public Long getMarketingPromotionId() {
		return marketingPromotionId;
	}

	public void setMarketingPromotionId(Long marketingPromotionId) {
		this.marketingPromotionId = marketingPromotionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getDomain_id() {
		return domain_id;
	}

	public void setDomain_id(Long domain_id) {
		this.domain_id = domain_id;
	}

	public String getDomain_abbreviation() {
		return domain_abbreviation;
	}

	public void setDomain_abbreviation(String domain_abbreviation) {
		this.domain_abbreviation = domain_abbreviation;
	}

	public String getBrand_code() {
		return brand_code;
	}

	public void setBrand_code(String brand_code) {
		this.brand_code = brand_code;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	

}
//$Id: MarketingPromotionAction.java Sep 18, 2013 11:03:34 AM liyunhui  $end:~
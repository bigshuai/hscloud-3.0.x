/**
 * @title ServiceItemAction.java
 * @package com.hisoft.hscloud.bss.sla.sc.action
 * @description 用一句话描述该文件做什么
 * @author jiaquan.hu
 * @update 2012-5-4 上午11:28:25
 * @version V1.0
 */
package com.hisoft.hscloud.web.action;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.sc.entity.ServiceItem;
import com.hisoft.hscloud.bss.sla.sc.utils.CloudCache;
import com.hisoft.hscloud.bss.sla.sc.utils.SCUtil;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.web.facade.Facade;

/**
* <一句话功能详细描述> 
* 
* @author  houyh 
* @version  [版本号, Oct 22, 2013] 
* @see  [相关类/方法] 
* @since  [产品/模块版本]
 */
public class ServiceItemAction extends HSCloudAction {

	private static final long serialVersionUID = -5434300835430139172L;
	private Logger logger = Logger.getLogger(ServiceItemAction.class);
	private ServiceItem serviceItem;
	private Page<ServiceItem> paging;
	private int start;
	private int limit = 5;
	private int page;
	private String sort;
	private int serviceType;
	private String name;
	private String query;
	private String family;// 操作系统是windows或linux
	private Integer listServiceItem;
	private int zoneGroupId;
	@Autowired
	private CloudCache cache;
	@Autowired
	private Facade facade;

	/**
	 * <获取某一类别的套餐资源> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void listServiceItem() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter listServiceItem method.");
		}
		List<Sort> sortList = null;
		try {
			if (StringUtils.isNotBlank(sort)) {
				sortList = SCUtil.json2Object(sort, new TypeReference<List<Sort>>() {
				});
			}
			fillActionResult(facade.listServiceItem(serviceType,sortList));
		}
		catch (Exception e) {
			dealThrow(new HsCloudException("", "获取资源列表异常,listServiceItem()", logger, e),
					Constants.GET_SC_RESOURCE_ERROR, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit listServiceItem method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <获取windows或者linux的操作系统套餐资源> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void listOSItem() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter listOSItem method.");
		}
		List<Sort> sortList = null;
		try {
			if (StringUtils.isNotBlank(sort)) {
				sortList = SCUtil.json2Object(sort, new TypeReference<List<Sort>>() {
				});
			}
			fillActionResult(facade.listOSItem(serviceType, sortList, family));
		}
		catch (Exception e) {
			dealThrow(new HsCloudException("", "获取资源列表异常,listOSItem()", logger, e), 
					Constants.GET_SC_RESOURCE_ERROR, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit listOSItem method.takeTime:" + takeTime + "ms");
		}
	}
	
	public void listServiceItemByZoneGroup() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter listServiceItemByZoneGroup method.");
		}
		List<Sort> sortList = null;
		try {
			if (StringUtils.isNotBlank(sort)) {
				sortList = SCUtil.json2Object(sort, new TypeReference<List<Sort>>() {
				});
			}
			fillActionResult(facade.listServiceItemByZoneGroup(serviceType, zoneGroupId, sortList));
		}
		catch (Exception e) {
			dealThrow(new HsCloudException("", "获取资源列表异常,listServiceItem()", logger, e),
					Constants.GET_SC_RESOURCE_ERROR, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit listServiceItemByZoneGroup method.takeTime:" + takeTime + "ms");
		}
	}

	public int getServiceType() {
		return serviceType;
	}

	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Page<ServiceItem> getPaging() {
		return paging;
	}

	public void setPaging(Page<ServiceItem> paging) {
		this.paging = paging;
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

	public ServiceItem getServiceItem() {
		return serviceItem;
	}

	public void setServiceItem(ServiceItem serviceItem) {
		this.serviceItem = serviceItem;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public int getZoneGroupId() {
		return zoneGroupId;
	}

	public void setZoneGroupId(int zoneGroupId) {
		this.zoneGroupId = zoneGroupId;
	}

}
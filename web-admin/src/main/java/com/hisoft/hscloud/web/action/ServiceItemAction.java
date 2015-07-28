/**
 * @title ServiceItemAction.java
 * @package com.hisoft.hscloud.bss.sla.sc.action
 * @description 用一句话描述该文件做什么
 * @author jiaquan.hu
 * @update 2012-5-4 上午11:28:25
 * @version V1.0
 */
package com.hisoft.hscloud.web.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.sc.entity.Os;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceItem;
import com.hisoft.hscloud.bss.sla.sc.entity.Software;
import com.hisoft.hscloud.bss.sla.sc.entity.ZoneGroup;
import com.hisoft.hscloud.bss.sla.sc.utils.CloudCache;
import com.hisoft.hscloud.bss.sla.sc.utils.SCUtil;
import com.hisoft.hscloud.bss.sla.sc.vo.ServiceItemVo;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.CharacterSetUtil;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.vpdc.ops.vo.ImageVO;
import com.hisoft.hscloud.web.facade.Facade;
import com.wgdawn.persist.more.model.app.MoreApp;
import com.wgdawn.service.AppInfoService;

/**
 * 
* <一句话功能简述> 
* <功能详细描述> 
* 
* @author  houyh 
* @version  [版本号, Oct 22, 2013] 
* @see  [相关类/方法] 
* @since  [产品/模块版本]
 */
public class ServiceItemAction extends HSCloudAction {
	/** 
	* 注释内容 
	*/
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
	
	private Long[] zoneGroupList;
	
	@Autowired
	private CloudCache cache;
	
	@Autowired
	private Facade facade;
	@Autowired
	private AppInfoService appInfoService;
	/**
	 * <获取某一类别的套餐资源> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void listServiceItem() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter listServiceItem method.");			
		}
		List<Sort> sortList = null;
		try {
			if(StringUtils.isNotBlank(sort)){
			sortList = SCUtil.json2Object(sort,
					new TypeReference<List<Sort>>() {
					});
			}
			fillActionResult(facade.listServiceItem(serviceType, sortList));

		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"获取资源列表异常,listServiceItem()", logger, e),Constants.GET_SC_RESOURCE_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit listServiceItem method.takeTime:" + takeTime + "ms");
		}
	}
    /**
     * <分页获取套餐资源> 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
	public void pageServiceItem() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter pageServiceItem method.");			
		}
		try {
			paging = new Page<ServiceItem>(limit);
			paging.setPageNo(page);
			List<Sort> sortList = null;
			if (null != query) {
				query = new String(query.getBytes("iso8859_1"), "UTF-8");
			}
			sortList = SCUtil.json2Object(sort,
					new TypeReference<List<Sort>>() {
					});
			fillActionResult(facade.pageServiceItem(paging, serviceType,
					sortList, query));
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"分页获取套餐资源异常,pageServiceItem()", logger, e), Constants.GET_SC_RESOURCE_BY_PAGE_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit pageServiceItem method.takeTime:" + takeTime + "ms");
		}
	}
	
	public void pageServiceItemByZoneId() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter pageServiceItem method.");			
		}
		try {
			paging = new Page<ServiceItem>(limit);
			paging.setPageNo(page);
			List<Sort> sortList = null;
			if (null != query) {
				query = new String(query.getBytes("iso8859_1"), "UTF-8");
			}
			sortList = SCUtil.json2Object(sort,
					new TypeReference<List<Sort>>() {
					});
			fillActionResult(facade.pageServiceItem(paging, serviceType,
					sortList, query));
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"分页获取套餐资源异常,pageServiceItem()", logger, e), Constants.GET_SC_RESOURCE_BY_PAGE_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit pageServiceItem method.takeTime:" + takeTime + "ms");
		}
	}
	
	
	/**
	 * <获取未与现有操作系统绑定的镜像> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void getImages() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getImages method.");			
		}
		try {
			List<ImageVO> images = cache.getImages();
			List<ServiceItem> list = facade.getServiceItemByType(4);
			List<ImageVO> rs = new ArrayList<ImageVO>();
			for (ImageVO imageVO : images) {
				boolean exists = false;
				for (ServiceItem si : list) {
					Os os = (Os) si;
					if (imageVO.getMetadata().get("distro")
							.equals(os.getName())) {
						exists = true;
						break;
					}
				}
				if (!exists) {
					imageVO.setLinks(null);
					rs.add(imageVO);
				}
			}
			if (rs.size() == 0) {
				fillActionResult(Constants.NO_IMAGE_FOUND);
			} else {
				fillActionResult(rs);
			}
		} catch (Exception e) {
			dealThrow(new HsCloudException("SI002",
					"获取未与现有操作系统绑定镜像异常，getImages()", logger, e), "000");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getImages method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <获取当前的所有镜像> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void getImagesNew() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getImagesNew method.");			
		}
		try {
			List<ImageVO> images = cache.getImages();
			
			for(ImageVO image:images){
				String imageName=image.getMetadata().get("distro");
				
				String imageNameNew=CharacterSetUtil.fromUnicodeAndcheck(imageName);
				image.getMetadata().put("distro", imageNameNew);
			}

			if (images.size() == 0) {
				fillActionResult(Constants.NO_IMAGE_FOUND);
			} else {
				fillActionResult(images);
			}
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "获取所有镜像异常，getImagesNew()",
					logger, e), Constants.GET_ALL_IMAGE_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getImagesNew method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * <获取当前云应用> 
	 * <功能详细描述>  
	 * @see [类、类#方法、类#成员]
	 */
	public void getApp() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getApp method.");			
		}
		try {
			List<MoreApp> applst = appInfoService.queryNewAppInfoList(1, 1, 2);
			if (applst.size() == 0) {
				fillActionResult(Constants.NO_IMAGE_FOUND);
			} else {
				fillActionResult(applst);
			}
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "获取所有镜像异常，getImagesNew()",
					logger, e), Constants.GET_ALL_IMAGE_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getApp method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
     * <获取当前的所有vrouter镜像> 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void getImagesForVRouter() {
        long beginRunTime = 0;
        if(logger.isDebugEnabled()){
            beginRunTime = System.currentTimeMillis();
            logger.debug("enter getImagesNew method.");         
        }
        try {
            List<ImageVO> images = cache.getImagesForVRouter();
            
            for(ImageVO image:images){
                String imageName=image.getMetadata().get("distro");
                String imageNameNew=CharacterSetUtil.fromUnicodeAndcheck(imageName);
                image.getMetadata().put("distro", imageNameNew);
            }

            if (images.size() == 0) {
                fillActionResult(Constants.NO_IMAGE_FOUND);
            } else {
                fillActionResult(images);
            }
        } catch (Exception e) {
            dealThrow(new HsCloudException("", "获取所有镜像异常，getImagesNew()",
                    logger, e), Constants.GET_ALL_IMAGE_ERROR,true);
        }
        if(logger.isDebugEnabled()){
            long takeTime = System.currentTimeMillis() - beginRunTime;
            logger.debug("exit getImagesNew method.takeTime:" + takeTime + "ms");
        }
    }
	/**
	 * <保存套餐> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void save() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter save method.");			
		}
		try {
			serviceItem = SCUtil.strutsJson2Object(Class.forName(facade
					.getServiceType().get(serviceType).getClassName()));
			facade.saveServiceItem(serviceItem);
		} catch (Exception e) {
			String code = Constants.SAVE_SC_RESOURCE_ERROR;
			if (serviceItem.getServiceType() == 4) {
				code = Constants.SAVE_SC_RESOURCE_ERROR;
			} else {
				code = Constants.SI_EXISTS;
			}
			dealThrow(new HsCloudException("", "保存套餐资源异常，save()", logger, e),
					code,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit save method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <保存os或者software> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void saveOSOrSoftware(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter saveOSOrSoftware method.");			
		}
		ServiceItemVo siVo=new ServiceItemVo();
		Os os = null;
		try{
			
			if(serviceType==4){
				 os=SCUtil.strutsJson2Object(Class.forName(facade
						.getServiceType().get(serviceType).getClassName()));
				BeanUtils.copyProperties(siVo, os);
			}else if(serviceType==6){
				Software st=SCUtil.strutsJson2Object(Class.forName(facade
						.getServiceType().get(serviceType).getClassName()));
				BeanUtils.copyProperties(siVo, st);
			} else if(serviceType == 9) {
			     os=SCUtil.strutsJson2Object(Class.forName(facade
                        .getServiceType().get(4).getClassName()));
                BeanUtils.copyProperties(siVo, os);
			}
//			List<ZoneGroup> zoneList =  new ArrayList<ZoneGroup>();
//			for(Long list: zoneGroupList){
//				ZoneGroup zoneGroup= new ZoneGroup();
//				zoneGroup.setId(list);
//				zoneList.add(zoneGroup);
//			}
//			os.setZoneGroupList(zoneList);
//			siVo.setZoneGroupList(zoneList);
			facade.saveServiceItemVo(siVo);
		}catch(Exception e){
//			if(siVo.getId()>0){
//				facade.deleteServiceItem(siVo.getId());
//			}
			dealThrow(new HsCloudException("", "保存套餐资源异常，save()", logger, e),
					Constants.SAVE_SC_RESOURCE_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit saveOSOrSoftware method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <删除套餐资源> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void delete() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter delete method.");			
		}
		try {
			boolean isDeleted = facade.deleteServiceItem(serviceItem.getId());
			if (!isDeleted) {
				fillActionResult(Constants.SI_ISUESD);
			} 
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "删除套餐资源异常，delete()",
					logger, e), Constants.DELETE_SC_RESOURCE_ERROR);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit delete method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <检查资源名称是否重复> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void checkServiceItemRepeat() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter checkServiceItemRepeat method.");			
		}
		try {
			boolean result = facade.checkServiceItemNameRepeat(serviceType,name);
			fillActionResult(result);
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"检查套餐资源是否重名异常，checkServiceItemRepeat()", logger, e),Constants.CHECK_SC_RESOURCE_NAME_DUP_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit checkServiceItemRepeat method.takeTime:" + takeTime + "ms");
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
	public Long[] getZoneGroupList() {
		return zoneGroupList;
	}
	public void setZoneGroupList(Long[] zoneGroupList) {
		this.zoneGroupList = zoneGroupList;
	}
	
	

}

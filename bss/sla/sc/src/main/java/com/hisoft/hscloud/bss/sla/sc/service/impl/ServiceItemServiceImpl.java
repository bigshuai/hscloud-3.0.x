/**
 * @title ServiceItemService.java
 * @package com.hisoft.hscloud.bss.sla.sc.service.impl
 * @description 用一句话描述该文件做什么
 * @author jiaquan.hu
 * @update 2012-5-4 上午11:32:05
 * @version V1.0
 */
package com.hisoft.hscloud.bss.sla.sc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.sc.dao.ServiceItemDao;
import com.hisoft.hscloud.bss.sla.sc.dao.ServiceTypeDao;
import com.hisoft.hscloud.bss.sla.sc.entity.Os;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceItem;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceType;
import com.hisoft.hscloud.bss.sla.sc.entity.Software;
import com.hisoft.hscloud.bss.sla.sc.entity.ZoneGroup;
import com.hisoft.hscloud.bss.sla.sc.service.IServiceItemService;
import com.hisoft.hscloud.bss.sla.sc.service.ZoneGroupService;
import com.hisoft.hscloud.bss.sla.sc.vo.OAZoneGroupVO;
import com.hisoft.hscloud.bss.sla.sc.vo.ServiceItemVo;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author jiaquan.hu
 * @update 2012-5-4 上午11:32:05
 */
@Service
public class ServiceItemServiceImpl implements IServiceItemService {
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private ServiceTypeDao serviceTypeDao;
	@Autowired
	private ServiceItemDao<ServiceItem> serviceItemDao;
	@Autowired
	private ZoneGroupService zoneGroupService;
	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	private static final int OS_TYPE = 4;
	private static final int VROUTER_TYPE = 9;
	
	private static final int OSTYPE_OS = 0;
    private static final int OSTYPE_VROUTER = 1;
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<ServiceItem> listServiceItem(int serviceType, List<Sort> sorts) {
		// Page<ServiceItem> page = new Page<ServiceItem>(500);
		// if (sorts != null) {
		// for (Sort sort : sorts) {
		// page.setOrder(sort.getDirection());
		// page.setOrderBy(sort.getProperty());
		// logger.info("sort by" + sort.getProperty() + " "
		// + sort.getDirection());
		// }
		// }
		// List<ServiceItem> result=serviceItemDao.findPage(page,
		// Restrictions.eq("serviceType", serviceType)).getResult();

	//	String type = getServiceType().get(serviceType).getClassName();
		
		String type;
        if(serviceType == VROUTER_TYPE) { //serviceType等于vrouter类型时，当OS_TYPE取type
            type = getServiceType().get(OS_TYPE).getClassName();
        } else {
            type = getServiceType().get(serviceType).getClassName();
        }

		StringBuilder hql = new StringBuilder("from ");
		hql.append(type).append(" where 1=1 ");
		hql.append(" and status=0 ");
		if(serviceType == OS_TYPE) {
            hql.append(" and osType = 0 ");
        } else if(serviceType == VROUTER_TYPE) {
            hql.append(" and osType = 1 ");
        }
		
		if (sorts != null && sorts.size() > 0) {
			hql.append(" order by ").append(sorts.get(0).getProperty());
			hql.append(" ").append(sorts.get(0).getDirection());
		}

		Map map = new HashMap();
		return serviceItemDao.find(hql.toString(), map);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<ServiceItem> listOSItem(int serviceType, List<Sort> sortList, String family) {
		String type;
		if (serviceType == VROUTER_TYPE) { // serviceType等于vrouter类型时，当OS_TYPE取type
			type = getServiceType().get(OS_TYPE).getClassName();
		}
		else {
			type = getServiceType().get(serviceType).getClassName();
		}
		StringBuilder hql = new StringBuilder("from ");
		hql.append(type).append(" where 1=1 ");
		hql.append(" and status=0 ");
		if (serviceType == OS_TYPE) {
			hql.append(" and osType = 0 ");
		}
		else if (serviceType == VROUTER_TYPE) {
			hql.append(" and osType = 1 ");
		}
		if (StringUtils.isNotEmpty(family)) {// windows或linux
			hql.append(" and family = '" + family + "' ");
		}
		if (sortList != null && sortList.size() > 0) {
			hql.append(" order by ").append(sortList.get(0).getProperty());
			hql.append(" ").append(sortList.get(0).getDirection());
		}
		Map map = new HashMap();
		return serviceItemDao.find(hql.toString(), map);
	}

	@Override
	public List<ServiceItem> listServiceItem(int serviceType,List<Sort> sorts,
			Map<String, Object> map) {
		String type = getServiceType().get(serviceType).getClassName();
		Set<String> keys = map.keySet();
		String hql = "from " + type + " where ";
		for (String key : keys) {
			if (!hql.endsWith("where ")) {
				hql = hql + " and ";
			}
			hql = hql + key + "=:" + key;
		}
		hql = hql + " and status=0";
		if (sorts != null) {
			hql = hql + " order by ";
			for (Sort sort : sorts) {
				if (!hql.endsWith("order by ")) {
					hql = hql + " and ";
				}
				hql = hql + sort.getProperty() + " " + sort.getDirection();
			}
		}
		return serviceItemDao.find(hql, map);
	}

	@Override
	public Page<ServiceItem> pageServiceItemByType(Page<ServiceItem> paging,
			int serviceType, List<Sort> sorts,String query) {
//		if (sorts != null) {
//			for (Sort sort : sorts) {
//				paging.setOrder(sort.getDirection());
//				paging.setOrderBy(sort.getProperty());
//			}
//		}
	    
	    String type;
		if(serviceType == VROUTER_TYPE) { //serviceType等于vrouter类型时，当OS_TYPE取type
		    type = getServiceType().get(OS_TYPE).getClassName();
		} else {
		    type = getServiceType().get(serviceType).getClassName();
		}
		
		StringBuilder hql=new StringBuilder("from ");
		hql.append(type).append(" where 1=1 ");
		if(query!=null&&!"".equals(query)){
		   //hql.append(" and ( description like '%").append(query).append("%'");
		   //hql.append(" or name like '%").append(query).append("%' )");
			hql.append(" and name like '%").append(query).append("%' ");
		}
		
		if(serviceType == OS_TYPE) {
		    hql.append(" and osType = 0 ");
		} else if(serviceType == VROUTER_TYPE) {
            hql.append(" and osType = 1 ");
        }
		
		hql.append(" and status=0 ");
		if(sorts!=null&&sorts.size()>0){
			hql.append(" order by ").append(sorts.get(0).getProperty());
			hql.append(" ").append(sorts.get(0).getDirection());
		}
		
		
		return serviceItemDao.findPage(paging, hql.toString(), new HashMap());
//				serviceItemDao.findPage(paging,
//				Restrictions.eq("serviceType", serviceType),
//				Restrictions.eq("status", 0));
	}

	@Override
	public List<ServiceItem> getAllServiceItems() {
		List<ServiceItem> result = serviceItemDao.getAll();
		return result;
	}

	@Override
	@Transactional
	public int save(ServiceItem serviceItem) {
		serviceItem.setUpdateDate(new Date());
		if (serviceItem.getId() == 0) {
			serviceItem.setCreateDate(new Date());
		} else {
			Date createDate = serviceItemDao.findUnique(
					"select createDate from ServiceItem where id=?",
					serviceItem.getId());
			serviceItem.setUpdateDate(new Date());
			serviceItem.setCreateDate(createDate);
		}
		serviceItemDao.save(serviceItem);
		return serviceItem.getId();
	}
	
	@Override
	public int saveSIVo(ServiceItemVo serviceItemVo) throws HsCloudException {
		logger.debug("saveSIVo enter");
		try {
			if (serviceItemVo.getId() == 0) {
				if (serviceItemVo.getServiceType() == 4) {
					Os os = new Os();
					BeanUtils.copyProperties(os, serviceItemVo);
					os.setCreateDate(new Date());
					os = serviceItemDao.saveOs(os);
//					List<ZoneGroup> zoneGroupList = new ArrayList<ZoneGroup>();
//					ZoneGroup zoneGroup = zoneGroupService.getZoneGroupById(serviceItemVo.getZoneGroupList().get(0).getId());
//					zoneGroupList.add(zoneGroup);
//					os.setZoneGroupList(zoneGroupList);
				} else if (serviceItemVo.getServiceType() == 6) {
					Software st = new Software();
					BeanUtils.copyProperties(st, serviceItemVo);
					st.setCreateDate(new Date());
					serviceItemDao.save(st);
				} else if (serviceItemVo.getServiceType() == 9) {
                    Os os = new Os();
                    BeanUtils.copyProperties(os, serviceItemVo);
                    os.setCreateDate(new Date());
                    os.setOsType(OSTYPE_VROUTER);
                    serviceItemDao.save(os);
                }
			} else {
				ServiceItem si = serviceItemDao.findUniqueBy("id",
						serviceItemVo.getId());
				if (si != null) {
					if (serviceItemVo.getServiceType() == 4) {
						Os os = (Os) si;
						BeanUtils.copyProperties(os, serviceItemVo);
						os.setUpdateDate(new Date());
						os.setOsType(OSTYPE_OS);
						serviceItemDao.saveOs(os);
//						serviceItemDao.save(os);
//						ZoneGroup zoneGroup = zoneGroupService.getZoneGroupById(serviceItemVo.getZoneGroupList().get(0).getId());
//						List<ZoneGroup> zoneGroupList = os.getZoneGroupList();
//						if(zoneGroupList != null){
//							zoneGroupList.clear();
//						}
//						zoneGroupList.add(zoneGroup);
//						os.setZoneGroupList(zoneGroupList);

					} else if (serviceItemVo.getServiceType() == 6) {
						Software st = (Software) si;
						BeanUtils.copyProperties(st, serviceItemVo);
						st.setUpdateDate(new Date());
						serviceItemDao.save(st);
					} else if (serviceItemVo.getServiceType() == 9) {
					    Os os = (Os) si;
                        BeanUtils.copyProperties(os, serviceItemVo);
                        os.setUpdateDate(new Date());
                        os.setOsType(OSTYPE_VROUTER);
                        serviceItemDao.save(os);
					}
				}
			}
			logger.debug("saveSIVo end");
			return 0;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
		
		
	}

	@Override
	public Map<Integer, ServiceType> getServiceType() {
		List<ServiceType> all = serviceTypeDao.getAll();
		Map<Integer, ServiceType> map = new HashMap<Integer, ServiceType>();

		for (ServiceType serviceType : all) {
			map.put(serviceType.getId(), serviceType);
		}
		return map;
	}

	@Override
	public boolean isUsed(int id) {
		Query query = serviceTypeDao.getSession().createSQLQuery(
				"select * from hc_service_catalog_item where item_id=" + id);
		List list = query.list();
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isExist(int serviceType, Map<String, Object> map) {
		List<ServiceItem> list = getServiceItemByProperty(serviceType, map);
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<Object> getNetworkType() {
		return serviceItemDao.find("select distinct type from Network");

	}
	@Override
	public List<ServiceItem> getServiceItemByProperty(int serviceType,
			Map<String, Object> map) {
		String type = getServiceType().get(serviceType).getClassName();
		String hql="from "+type;
		if (map!=null) {
			Set<String> keys = map.keySet();
			hql = "from " + type + " where ";
			for (String key : keys) {
				if (!hql.endsWith("where ")) {
					hql = hql + " and ";
				}
				hql = hql + key + "=:" + key;
			}
		}
		logger.info(hql);
		Query query = serviceItemDao.createQuery(hql, map);
		List<ServiceItem> list = query.list();
		return list;
	}

	@Override
	@Transactional
	public void delete(int id) {
	/*	ServiceItem item = serviceItemDao.findUniqueBy("id", id);
		item.setStatus(1);*/
		serviceItemDao.delete(id);

	}

	@Override
	public boolean checkServiceItemRepeat(String itemName, int serviceType) {
		StringBuilder hql=new StringBuilder();
		hql.append("from ServiceItem si where si.name=?").append(" and si.serviceType=?");
		//ServiceItem si=serviceItemDao.findUnique(hql.toString(), itemName,serviceType);
		List<ServiceItem> sis=serviceItemDao.find(hql.toString(),itemName,serviceType);
		if(sis==null||sis.size()==0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public ServiceItem getSIById(int id) {
		logger.debug("getSIById enter");
		try{
			return serviceItemDao.findUniqueBy("id",id);
		}catch(Exception e){
			throw new HsCloudException("",logger,e);
		}
		
	}
	
	@Override
	public Os getOs(int osId) {
	    Map<String, Integer> map = new HashMap<String, Integer>();
	    map.put("id", osId);
	    return serviceItemDao.findUnique("from Os where id = :id", map);
	}
	
	@Override
	public ServiceItem getSIBySize(int size, String category) throws HsCloudException {
		logger.debug("getSIBySize enter");
		Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			if (category.equalsIgnoreCase("cpu")) {
				map.put("coreNum", size);
				return serviceItemDao.findUnique("from Cpu where coreNum = :coreNum", map);
			}
			else if (category.equalsIgnoreCase("ram")) {
				map.put("size", size);
				return serviceItemDao.findUnique("from Ram where size = :size", map);
			}else if(category.equalsIgnoreCase("disk")){
				map.put("size", size);
				return serviceItemDao.findUnique("from Disk where capacity = :size", map);
			}else if(category.equalsIgnoreCase("extdisk")){
				map.put("size", size);
				return serviceItemDao.findUnique("from ExtDisk where capacity = :size", map);
			}else if(category.equalsIgnoreCase("network")){
				map.put("size", size);
				return serviceItemDao.findUnique("from Network where bandWidth = :size", map);
			}
		}
		catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<OAZoneGroupVO> listServiceItemByZoneGroup(int serviceType,
			int zoneGroupId, List<Sort> sorts) {
		String type;
		List<OAZoneGroupVO> result = new ArrayList<OAZoneGroupVO>();
        if(serviceType == VROUTER_TYPE) { //serviceType等于vrouter类型时，当OS_TYPE取type
            type = getServiceType().get(OS_TYPE).getClassName();
        } else {
            type = getServiceType().get(serviceType).getClassName();
        }
        StringBuilder sql = new StringBuilder("SELECT h.os_id as id,hsi.service_type as serviceType ,hsi.name as name,");
        sql.append("hsi.status as status,hsi.description AS description,h.vendor AS vendor,h.arch as arch,h.language AS language,");
        sql.append("h.family AS family,h.version AS version,h.image_id AS imageId,h.imageSize AS imageSize,h.os_type AS osType,h.port AS port ");
        sql.append(" FROM hc_os h");
        sql.append(" LEFT JOIN hc_service_item hsi ON h.os_id = hsi.item_id ");
        sql.append("LEFT JOIN hc_os_zonegroup hoz ON h.os_id = hoz.os_id ");
        sql.append("WHERE hsi.`status` = 0 ");
		
		if(serviceType == OS_TYPE) {
            sql.append(" AND h.os_type = 0 ");
        } else if(serviceType == VROUTER_TYPE) {
            sql.append(" AND h.os_type = 1 ");
        }
		sql.append(" AND  hoz.zoneGroup_id = :zoneGroupId");
		sql.append(" AND h.os_id = hoz.os_id");
		SQLQuery sqlQuery = hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		
		
		sqlQuery.addScalar("id", Hibernate.INTEGER);
		sqlQuery.addScalar("serviceType",Hibernate.INTEGER);
		sqlQuery.addScalar("name", Hibernate.STRING);
		sqlQuery.addScalar("status",Hibernate.INTEGER);
		sqlQuery.addScalar("description", Hibernate.STRING);
		sqlQuery.addScalar("vendor", Hibernate.STRING);
		sqlQuery.addScalar("arch",Hibernate.STRING);
		sqlQuery.addScalar("language", Hibernate.STRING);
		sqlQuery.addScalar("family", Hibernate.STRING);
		sqlQuery.addScalar("version", Hibernate.STRING);
		sqlQuery.addScalar("imageId",Hibernate.STRING);
		sqlQuery.addScalar("imageSize",Hibernate.STRING);
		sqlQuery.addScalar("osType",Hibernate.STRING);
		sqlQuery.addScalar("port",Hibernate.STRING);
		sqlQuery.setResultTransformer(Transformers.aliasToBean(OAZoneGroupVO.class));
		sqlQuery.setParameter("zoneGroupId", zoneGroupId);
		
		result = sqlQuery.list();
		
		return result;
//		if (sorts != null && sorts.size() > 0) {
//			hql.append(" order by ").append(sorts.get(0).getProperty());
//			hql.append(" ").append(sorts.get(0).getDirection());
//		}
//		Map map = new HashMap();
//		return serviceItemDao.find(hql.toString(), map);
	}
	
}
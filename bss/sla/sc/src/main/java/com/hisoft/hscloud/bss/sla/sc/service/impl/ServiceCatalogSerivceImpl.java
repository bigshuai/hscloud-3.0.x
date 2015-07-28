package com.hisoft.hscloud.bss.sla.sc.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.sc.dao.ScFeeTypeDao;
import com.hisoft.hscloud.bss.sla.sc.dao.ServiceCatalogDao;
import com.hisoft.hscloud.bss.sla.sc.dao.ServiceItemDao;
import com.hisoft.hscloud.bss.sla.sc.entity.Cpu;
import com.hisoft.hscloud.bss.sla.sc.entity.Disk;
import com.hisoft.hscloud.bss.sla.sc.entity.ExtDisk;
import com.hisoft.hscloud.bss.sla.sc.entity.Os;
import com.hisoft.hscloud.bss.sla.sc.entity.Ram;
import com.hisoft.hscloud.bss.sla.sc.entity.ScFeeType;
import com.hisoft.hscloud.bss.sla.sc.entity.ScIsolationConfig;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceCatalog;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceItem;
import com.hisoft.hscloud.bss.sla.sc.service.IServiceCatalogService;
import com.hisoft.hscloud.bss.sla.sc.vo.SCVo;
import com.hisoft.hscloud.bss.sla.sc.vo.ScFeeTypeVo;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.vo.DomainVO;
import com.hisoft.hscloud.vpdc.ops.service.Operation;
import com.hisoft.hscloud.vpdc.ops.vo.FlavorVO;

/**
 * @description service catalog服务层的实现
 * @version 1.0
 * @author jiaquan.hu
 * @update 2012-3-31 上午10:09:44
 */
@Service(value = "serviceCatalogService")
public class ServiceCatalogSerivceImpl implements IServiceCatalogService {
	private Logger logger = Logger.getLogger(ServiceCatalogSerivceImpl.class);
	@Autowired
	private ServiceCatalogDao serviceCatalogDao;
	@Autowired
	private ServiceItemDao<ServiceItem> serviceItemDao;
	@Autowired
    private ScFeeTypeDao scFeeTypeDao;
	@Autowired
	private Operation operation;
	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	@Transactional(readOnly = true)
	public ServiceCatalog get(int id) {
		return serviceCatalogDao.findScById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public ServiceCatalog getByName(String name) {
		if(StringUtils.isNotBlank(name)){
			int length=name.length();
			String hql="from ServiceCatalog s where s.name =  '"+name+ "'";
			List<ServiceCatalog> list = serviceCatalogDao
					.getServiceCatalogList(hql,null);
			if (list.size() == 0) {
				return null;
			} else {
				return list.get(0);
			}
		}else{
			return new ServiceCatalog();
		}
		
	}

	@Override
	@Transactional
	public void save(ServiceCatalog serviceCatalog) throws HsCloudException {
		logger.info("service:get into save or update service catalog");

		serviceCatalog.setUpdateDate(new Date());
		if (serviceCatalog.getId() == 0) {
			serviceCatalog.setCreateDate(new Date());
			serviceCatalog.setStatus(0);
		} else {
			scFeeTypeDao.deleteFeeTypeByScId(serviceCatalog);
			Date createDate = serviceCatalogDao.getCreateTime(serviceCatalog.getId());
			serviceCatalog.setCreateDate(createDate);
		}
		serviceCatalogDao.save(serviceCatalog);
		FlavorVO flavorVO = new FlavorVO();
		// String flavorName=new SimpleDateFormat("yyyyMMddhhmmss").format(new
		// Date());
		// logger.info("create flavor: "+flavorName);
		// flavorVO.setName(flavorName);

		List<ServiceItem> items = serviceCatalog.getItems();
		for (ServiceItem si : items) {
			logger.info("serviceItem id: " + si.getId());
			if (si.getServiceType() == 1) {
				Cpu cpu = (Cpu) serviceItemDao.findUnique(
						"from Cpu where id=?", si.getId());
				logger.info("cpu coreNum " + cpu.getCoreNum());
				flavorVO.setVcpus(cpu.getCoreNum());
			} else if (si.getServiceType() == 2) {
				Ram ram = (Ram) serviceItemDao.findUnique(
						"from Ram where id=?", si.getId());
				logger.info("ram size " + ram.getSize());
				flavorVO.setRam(ram.getSize());
			} else if (si.getServiceType() == 3) {
				Disk disk = (Disk) serviceItemDao.findUnique(
						"from Disk where id=?", si.getId());
				logger.info("disk capacity " + disk.getCapacity());
				flavorVO.setDisk(disk.getCapacity());
			}
		}
		try {
			serviceCatalog.setFlavorId(Integer.parseInt(operation.createFlavor(flavorVO)));
		} catch (HsCloudException e) {
			throw new HsCloudException("SC-Service001", "save异常", logger, e);
		}

	}

	@Override
	@Transactional
	public void delete(int id) {
		ServiceCatalog serviceCatalog = serviceCatalogDao
				.findScById(id);
		serviceCatalog.setStatus(2);
	}

	@Override
	@Transactional
	public void approve(int id) {
		ServiceCatalog serviceCatalog = serviceCatalogDao
				.findScById(id);
		serviceCatalog.setStatus(1);
	}
	
	@Override
	@Transactional
	public void onlyTrySC(int id) {
		ServiceCatalog serviceCatalog = serviceCatalogDao
				.findScById(id);
		serviceCatalog.setStatus(3);
		serviceCatalog.setTryOrNo(true);
	}

	@Override
	@Transactional
	public List<ServiceCatalog> getAll(List<Sort> sorts,String userLevel,Long domainId,Long zoneGroupId) {
		logger.info("get into retrieve all service catalogs");
		StringBuilder hql=new StringBuilder();
		Map<String,Object> params=new HashMap<String,Object>();
		hql.append("select distinct sc from ServiceCatalog sc,UserBrand as ub,Domain as domain,ServerZone as zone ,ZoneGroup as zoneGroup where sc.status not in(2,0) ")
		//hql.append("from ServiceCatalog sc inner join sc.userBrand as ub where sc.status=1 ")
		.append(" and sc.effectiveDate <= now() ")
		.append(" and sc.expirationDate > now() ");
		if(userLevel!=null){
			//hql.append(" and sc.userLevel = :userLevel ");
			hql.append(" and ub in elements( sc.userBrand ) ");
			hql.append(" and ub.code = :userLevel ");
			params.put("userLevel", userLevel);
		}
		
		if(domainId!=null&&domainId.longValue()!=0L){
			hql.append(" and domain in elements( sc.domainList ) ");
			hql.append(" and domain.id = :domainId ");
			params.put("domainId", domainId);
		}
		
		if(userLevel!=null &&domainId!=null&&domainId.longValue()!=0L){
			hql.append(" and ub in elements( domain.userBrandList )");
		}
		
		if(zoneGroupId!=null&&zoneGroupId.longValue()!=0L){
			hql.append(" and zone in elements( sc.zoneList ) ");
			hql.append(" and zoneGroup in elements( zone.zoneGroupList )");
			hql.append(" and zoneGroup.id = :zoneGroupId ");
			params.put("zoneGroupId", zoneGroupId);
		}
		
		//prepareHqlForQuery(hql, queryInt);
		
		if(sorts!=null&&sorts.size()>0){
			hql.append(" order by ");
			for(Sort sort:sorts){
				if(sorts.lastIndexOf(sort)!=sorts.size()-1){
					hql.append("sc.").append(sort.getProperty()).append(" ").append(sort.getDirection()).append(",");
				}else{
					hql.append("sc.").append(sort.getProperty()).append(" ").append(sort.getDirection());
				}
			}
		}
		List<ServiceCatalog> result=serviceCatalogDao.getServiceCatalogList(hql.toString(),params);
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ServiceCatalog> page(Page<ServiceCatalog> page,
			ServiceCatalog serviceCatalog, List<Sort> sortList,Long brandId,Long zoneId,Long domainId) {
		String name = serviceCatalog.getName();
		logger.info("serviceImpl->page->query for service catalog name" + name);
		StringBuilder hql=new StringBuilder();
		Map<String,Object> params=new HashMap<String,Object>();
		hql.append("select distinct sc from ServiceCatalog sc ");
		
		StringBuilder table=new StringBuilder("");
		StringBuilder where=new StringBuilder(" where 1=1 ");
		
		if(brandId!=null&&brandId.longValue()!=0L){
			table.append(",UserBrand as ub ");
			where.append(" and ub in elements( sc.userBrand ) ");
			where.append(" and ub.id = :brandId ");
			params.put("brandId", brandId);
		}
		
		if(zoneId!=null&&zoneId.longValue()!=0L){
			table.append(",ServerZone as zone ");
			where.append(" and zone in elements( sc.zoneList )");
			where.append(" and zone.id = :zoneId ");
			params.put("zoneId", zoneId);
		}
		
        if(domainId!=null&&domainId.longValue()!=0L){
        	table.append(",Domain as domain ");
			where.append(" and domain in elements( sc.domainList )");
			where.append(" and domain.id = :domainId ");
			params.put("domainId", domainId);
		}
		
		if(StringUtils.isNotBlank(name)){
			where.append(" and sc.name like :scName or sc.catalogCode like :scName");
			params.put("scName", "%"+name+"%");
		}	
		
		hql.append(table).append(where);
		if(sortList!=null&&sortList.size()>0){
			hql.append(" order by ");
			for(Sort sort:sortList){
				if(sortList.lastIndexOf(sort)!=sortList.size()-1){
					hql.append("sc.").append(sort.getProperty()).append(" ").append(sort.getDirection()).append(",");
				}else{
					hql.append("sc.").append(sort.getProperty()).append(" ").append(sort.getDirection());
				}
			}
		}

		return serviceCatalogDao.findByPage(page, hql.toString(), params);
	}
	@Override
	@Transactional(readOnly = true)
	public Page<ServiceCatalog> getScByPage(Page<ServiceCatalog> page,
			 List<Sort> sortList,Long domainId,String userLevel) {
		StringBuilder hql=new StringBuilder();
		Map<String,Object> params=new HashMap<String,Object>();
		hql.append("select distinct sc from ServiceCatalog sc,Domain domain,UserBrand ub  where sc.status not in(2,0)");
		hql.append(" and sc.effectiveDate <= now() and sc.expirationDate > now()");
		if(userLevel!=null){
			hql.append(" and ub in elements( sc.userBrand ) ");
			hql.append(" and ub.code = :userLevel ");
			params.put("userLevel", userLevel);
		}
		
		if(domainId!=null&&domainId.longValue()!=0L){
			hql.append(" and domain in elements( sc.domainList ) ");
			hql.append(" and domain.id = :domainId ");
			params.put("domainId", domainId);
		}
		
		if(userLevel!=null &&domainId!=null&&domainId.longValue()!=0L){
			hql.append(" and ub in elements( domain.userBrandList )");
		}
		if(sortList!=null&&sortList.size()>0){
			hql.append(" order by ");
			for(Sort sort:sortList){
				if(sortList.lastIndexOf(sort)!=sortList.size()-1){
					hql.append("sc.").append(sort.getProperty()).append(" ").append(sort.getDirection()).append(",");
				}else{
					hql.append("sc.").append(sort.getProperty()).append(" ").append(sort.getDirection());
				}
			}
		}
		
		return serviceCatalogDao.findByPage(page, hql.toString(), params);
	}

	private void serviceCatalogSync() throws Exception {
		/*
		 * List<ServiceCatalog> list = serviceCatalogDao.getAll();
		 * 
		 * List<Integer> existFlavors = new ArrayList<Integer>(); for
		 * (ServiceCatalog serviceCatalog : list) {
		 * existFlavors.add(serviceCatalog.getFlavorId()); } List<FlavorVO>
		 * listFlavors = operation.fuzzyFindFlavors(null, null, new
		 * Page<FlavorVO>()).getResult(); for (FlavorVO flavor : listFlavors) {
		 * if (!existFlavors.contains(Integer.parseInt(flavor.getId()))) { Cpu
		 * cpu = getObj( cpuDao.findUniqueBy("params",
		 * String.valueOf(flavor.getVcpus())), Cpu.class);
		 * cpu.setParams(String.valueOf(flavor.getVcpus()));
		 * cpuDao.getSession().saveOrUpdate(cpu); Memory memory = getObj(
		 * memoryDao.findUniqueBy("params", flavor.getRam()), Memory.class);
		 * memory.setParams(Integer.toString(flavor.getRam()));
		 * memoryDao.getSession().saveOrUpdate(memory); Disk disk = getObj(
		 * diskDao.findUniqueBy("params", flavor.getDisk()), Disk.class);
		 * disk.setParams(Integer.toString(flavor.getDisk()));
		 * diskDao.getSession().saveOrUpdate(disk);
		 * 
		 * ServiceCatalog serviceCatalog = new ServiceCatalog();
		 * serviceCatalog.setName(flavor.getName()); serviceCatalog.setCpu(cpu);
		 * serviceCatalog.setMemory(memory); serviceCatalog.setDisk(disk);
		 * serviceCatalog .setFlavorId(Integer.parseInt(flavor.getId()));
		 * serviceCatalogDao.save(serviceCatalog);
		 * 
		 * } }
		 */
	}

	@SuppressWarnings("unchecked")
	private <T> T getObj(T o, Class<?> c) throws Exception {
		T newInstance = null;
		if (o == null) {
			newInstance = (T) Class.forName(c.getName()).newInstance();
		} else {
			newInstance = o;
		}
		return newInstance;
	}

	@Override
	public Os getOs(int serviceCatalogId) {
		Os os = null;
		List<ServiceItem> items = get(serviceCatalogId).getItems();
		for (ServiceItem serviceItem : items) {
			if (serviceItem.getServiceType() == 4) {
				os = (Os) serviceItemDao
						.findUniqueBy("id", serviceItem.getId());
			}
		}
		return os;

	}
	
	private void prepareHqlForQuery(StringBuilder hql,int buyType){
		hql.append(" and sc.priceType=1 ");
		switch(buyType){
		case 1:{
			hql.append(" and sc.periodType=1 and sc.period=1 ");
			break;
		}
		case 2:{
			hql.append(" and sc.periodType=1 and sc.period=3 ");
			break;
		}
		case 3:{
			hql.append(" and sc.periodType=1 and sc.period=6 ");
			break;
		}
		case 4:{
			hql.append(" and ( (sc.periodType=0 and sc.period=1) or ");
			hql.append(" (sc.periodType=1 and sc.period=12) ) ");
			break;
		}
		}
	}

	@Override
	public ScFeeType getfeeTypeById(Long id,int scId) throws HsCloudException {
		return scFeeTypeDao.getFeeTypeById(id,scId);
	}
	
	

	@Override
	public ScFeeType getfeeTypeByPeriod(String period, int scId)
			throws HsCloudException {
		ServiceCatalog sc=get(scId);
		if(sc!=null){
			List<ScFeeType> feeTypeList=sc.getFeeTypes();
			for(ScFeeType feeType:feeTypeList){
				String periodLocal=feeType.getPeriod();
				if(StringUtils.isNotBlank(periodLocal)&&periodLocal.equals(period)){
				  return feeType;
				}
			}
		}
		return null;
	}

	@Override
	public List<Os> getOsListByScId(int scId) throws HsCloudException {
		List<ServiceItem> items=get(scId).getItems();
		List<Os> oss=new ArrayList<Os>();
		if(items!=null&&items.size()>0){
			for(ServiceItem item:items){
				if(item.getServiceType()==4){
					Os os=(Os)item;
					oss.add(os);
				}
			}
		}
		return oss;
	}
	
	@Override
	public List<ExtDisk> getExtDiskListByScId(int scId) throws HsCloudException {
		List<ServiceItem> items=get(scId).getItems();
		List<ExtDisk> extDiskList=new ArrayList<ExtDisk>();
		if(items!=null&&items.size()>0){
			for(ServiceItem item:items){
				if(item.getServiceType()==8){
					ExtDisk extDisk=(ExtDisk)item;
					extDiskList.add(extDisk);
				}
			}
		}
		return extDiskList;
	}

	@Override
	public List<ScFeeTypeVo> getScFeeTypeByScId(int scId) throws HsCloudException {
	//	return scFeeTypeDao.getScFeeTypeByScId(scId);
	    List<ScFeeType> list = scFeeTypeDao.getScFeeTypeByScId(scId);
	    List<ScFeeTypeVo> result = new ArrayList<ScFeeTypeVo>();
	    for(ScFeeType scFeeType : list) {
	        ScFeeTypeVo scFeeTypeVo = new ScFeeTypeVo();
	        try {
                BeanUtils.copyProperties(scFeeTypeVo, scFeeType);
            } catch (IllegalAccessException e) {
                throw new HsCloudException(Constants.VM_REGULAR_ERROR, "getScFeeTypeByScId异常", logger, e);
            } catch (InvocationTargetException e) {
                throw new HsCloudException(Constants.VM_REGULAR_ERROR, "getScFeeTypeByScId异常", logger, e);
            }
	        scFeeTypeVo.setUsePointOrNot(scFeeType.getSc().getUsePointOrNot());
	        scFeeTypeVo.setUseGiftOrNot(scFeeType.getSc().getUseGiftOrNot());
	        result.add(scFeeTypeVo);
	    }
	    return result;
	}

	@Override
	public List<ScFeeTypeVo> getScFeeTypeByOrderItemId(long orderItemId)
			throws HsCloudException {
		return scFeeTypeDao.getScFeeTypeByOrderItemId(orderItemId);
	}

	@Override
	public Page<SCVo> getRelatedSCByBrandId(long brandId,Page<SCVo> paging,String query)
			throws HsCloudException {
		int limit=paging.getPageSize();
		int pageNo=paging.getPageNo();
		List<SCVo> result=serviceCatalogDao.getRelatedSCByBrandId(brandId,limit,pageNo,query);
		int totalCount=serviceCatalogDao.getRelatedSCcountByBrandId(brandId, limit, pageNo, query);
		paging.setResult(result);
		paging.setTotalCount(totalCount);
		return paging;
	}

	@Override
	public Page<SCVo> getUnRelatedScByBrandId(long brandId,Page<SCVo> paging,String query)
			throws HsCloudException {
		int limit=paging.getPageSize();
		int pageNo=paging.getPageNo();
		List<SCVo> result=serviceCatalogDao.getUnRelatedScByBrandId(brandId,limit,pageNo,query);
		int totalCount=serviceCatalogDao.getUnRelatedScCountByBrandId(brandId, limit, pageNo, query);
		paging.setResult(result);
		paging.setTotalCount(totalCount);
		return paging;
	}

	@Override
	public List<SCVo> getRelatedScByReferenceId(long referenceId)
			throws HsCloudException {
		return serviceCatalogDao.getRelatedScByReferenceId(referenceId);
	}

	@Override
	public ScIsolationConfig getScIsolationConfigByScId(int scId)
			throws HsCloudException {
		return serviceCatalogDao.getScIsolationConfigByScId(scId);
	}

	@Override
	public ServiceCatalog getByCode(String scCode, String domainCode,String brandCode) {
		return serviceCatalogDao.getByCode(scCode, domainCode,brandCode);
	}

	@Override
	public List<DomainVO> getDomainCodebyId(List<Domain> domainList) {
		
		return serviceCatalogDao.getDomainCodebyId(domainList);
	}

	@Override
	public boolean hasServiceCatalogCode(ServiceCatalog serviceCatalog)
			throws HsCloudException {
		boolean res = false;
		Session session = null;
		try{
			List<ServiceCatalog> scl = serviceCatalogDao.getServiceCatalogCodeByCondtion(serviceCatalog);
			if(!scl.isEmpty()){
//				if(serviceCatalog.getId() == scl.getId()){
//					session = hibernateTemplate.getSessionFactory().getCurrentSession();
//					session.evict(scl);
//					scl= null;
//					return false;
//				}else{
//					res = true;
//					return true;
//				}
				return true;
			}
		}catch(Exception e){
			logger.error("getServiceCatalogCodeByCondtion Exception:", e);
		}
		
		return res;
	}

	

	
	
}

package com.hisoft.hscloud.systemmanagement.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.systemmanagement.entity.ProcessResource;
@Repository
 public class ProcessResourceDaoImpl extends HibernateDao<ProcessResource, Long> implements ProcessResourceDao {
	private Logger logger = Logger.getLogger(this.getClass());
	
	public Page<ProcessResource> findAllProcessResource(Page<ProcessResource> page) throws HsCloudException {
		try{
			String hql = "from ProcessResource pr where 2>1";				
			return this.findPage(page, hql);
		}catch (Exception e) {
			// logger.error(e);
			throw new HsCloudException("XX007", "findAllProcessResource异常", logger,e);
		}		
	}

	public boolean updateProcessResource(ProcessResource processResource) throws HsCloudException {
		boolean result = false;
		if(processResource != null){
			try{
				processResource.setUpdateDate(new Date());
				this.save(processResource);
				result = true;
			}catch (Exception e) {
				// logger.error(e);
				throw new HsCloudException("XX007", "updateProcessResource异常", logger,e);
			}
		}
		return result;
	}

	@Override
	public ProcessResource getProcessResourceById(long id) throws HsCloudException {
		try {
			return this.get(id);
		} catch (Exception e) {
			throw new HsCloudException("XX007", "getProcessResourceById异常", logger, e);
		}
	}

	@Override
	public List<ProcessResource> findAllProcessResource()
			throws HsCloudException {
		try{
			//String hql = "from ProcessResource pr where 2>1";				
			return this.getAll();
		}catch (Exception e) {
			// logger.error(e);
			throw new HsCloudException("XX007", "findAllProcessResource异常", logger,e);
		}	
	}

	@Override
	public ProcessResource getProcessResourceByPropertyName(
			String propertyName, Object obj) throws HsCloudException {
		try {
			return this.findUniqueBy(propertyName, obj);
		} catch (Exception e) {
			throw new HsCloudException("XX007", "getProcessResourceByPropertyName异常", logger, e);
		}
	}

}

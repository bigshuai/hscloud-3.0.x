/**
 * @title ProcessResourceDao.java
 * @package com.hisoft.hscloud.systemmanagement.dao
 * @description 用一句话描述该文件做什么
 * @author AaronFeng
 * @update 2012-10-18 下午2:34:39
 * @version V1.0
 */
package com.hisoft.hscloud.systemmanagement.dao;

import java.util.List;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.systemmanagement.entity.ProcessResource;

/**
 * @description 线程资源的数据访问层接口
 * @version 1.0
 * @author AaronFeng
 * @update 2012-10-18 下午2:34:39
 */
public interface ProcessResourceDao {
	 public Page<ProcessResource> findAllProcessResource(Page<ProcessResource> page) throws HsCloudException;
	 public boolean updateProcessResource(
			 ProcessResource processResource) throws HsCloudException;
	 public ProcessResource getProcessResourceById(long id) throws HsCloudException;
	 public List<ProcessResource> findAllProcessResource() throws HsCloudException;
	 public ProcessResource getProcessResourceByPropertyName(String propertyName,Object obj) throws HsCloudException;
}

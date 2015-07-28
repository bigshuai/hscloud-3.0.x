package com.hisoft.hscloud.systemmanagement.service; 

import java.util.List;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.entity.HcEventResource;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.systemmanagement.vo.LogQueryVO;

public interface HcEventResourceService {
	/**
	 * <查询关于资源的操作日志> <功能详细描述>
	 * 
	 * @param param
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public Page<HcEventResource> getResourceLog(Page<HcEventResource> paing,LogQueryVO param)
			throws HsCloudException;
	/**
	 * <获取jobserver关于资源操作的message> 
	* <功能详细描述> 
	* @param id
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public String getResourceLogMessageById(Long id)throws HsCloudException;
}

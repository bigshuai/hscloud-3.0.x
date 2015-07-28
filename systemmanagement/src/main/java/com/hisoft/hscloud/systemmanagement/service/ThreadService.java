/**
 * @title ThreadService.java
 * @package com.hisoft.hscloud.systemmanagement.service
 * @description 用一句话描述该文件做什么
 * @author AaronFeng
 * @update 2012-10-16 下午3:52:31
 * @version V1.0
 */
package com.hisoft.hscloud.systemmanagement.service;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.systemmanagement.entity.ProcessResource;
import com.hisoft.hscloud.systemmanagement.vo.ProcessResourceVO;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author AaronFeng
 * @update 2012-10-16 下午3:52:31
 */
public interface ThreadService {
//	public void initThread(WebApplicationContext wc);
	public void initThread();
	public void startThread(String threadName);
	public void stopThread(String threadName);
	public Page<ProcessResourceVO> findAllProcess(Page<ProcessResourceVO> pageProcessVO) throws HsCloudException;
	public ProcessResource getProcessResourceByProcessCode(String processCode) throws HsCloudException;
	public boolean updateProcessResource(ProcessResource processResource) throws HsCloudException;
}

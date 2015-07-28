/* 
* 文 件 名:  Facade.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-4-10 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.facade; 

import java.util.List;
import java.util.Map;
import org.springside.modules.orm.Page;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.storage.entity.Module;
import com.hisoft.hscloud.storage.vo.OperationLogVO;
import com.hisoft.hscloud.storage.vo.UserVO;
import com.hisoft.hscloud.storage.vo.VideoVO;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-4-10] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface Facade {

	/**
	 * <账户登录> 
	* <功能详细描述> 
	* @param userName
	* @param password
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public UserVO login(String userName,String password) throws HsCloudException;
    /**
     * <视频列表查询> 
    * <功能详细描述> 
    * @param page
    * @param condition
    * @return 
    * @see [类、类#方法、类#成员]
     */
	public Page<VideoVO> findVideo(Page<VideoVO> page, Map<String, Object> condition);
	/**
	 * <获取视频播放连接> 
	* <功能详细描述> 
	* @param videoVO
	* @param url
	* @return 
	* @see [类、类#方法、类#成员]
	 */
    public String getPlayUrl(VideoVO videoVO, String url, String tenantId);
    
    /**
     * <加载模块列表> 
    * <功能详细描述> 
    * @param tenantId
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public List<Module> loadModule(String tenantId);
    
    /**
     * <记录操作日志> 
    * <功能详细描述> 
    * @param userName
    * @param password
    * @param ip 
    * @see [类、类#方法、类#成员]
     */
    public void addOperationLog(String userName, String ip, int operationType);
    
    /**
     * <操作日志查询> 
    * <功能详细描述> 
    * @param page
    * @param condition
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public Page<OperationLogVO> findOperationLogPage(Page<OperationLogVO> page,
            Map<String, Object> condition);
}

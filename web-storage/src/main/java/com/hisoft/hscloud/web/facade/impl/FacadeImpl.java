/* 
* 文 件 名:  FacadeImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-4-10 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.facade.impl; 

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.storage.entity.Module;
import com.hisoft.hscloud.storage.entity.OperationLog;
import com.hisoft.hscloud.storage.service.LoginService;
import com.hisoft.hscloud.storage.service.OperationLogService;
import com.hisoft.hscloud.storage.service.VideoService;
import com.hisoft.hscloud.storage.vo.OperationLogVO;
import com.hisoft.hscloud.storage.vo.OperationType;
import com.hisoft.hscloud.storage.vo.UserVO;
import com.hisoft.hscloud.storage.vo.VideoVO;
import com.hisoft.hscloud.web.facade.Facade;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-4-10] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class FacadeImpl implements Facade {
    @Autowired
    private LoginService loginService;
    
    @Autowired
    private VideoService videoService;
    
    @Autowired
    private OperationLogService operationLogService;
    
    //登录
	@Override
	public UserVO login(String userName, String password)
			throws HsCloudException {
		return loginService.authenticate(userName, password);
	}
	
	//记录操作日志
	@Override
	public void addOperationLog(String userName, String ip, int operationType) {
	    OperationLog operationLog = new OperationLog();
        operationLog.setOperationType(operationType);
        operationLog.setIp(ip);
        operationLog.setOperator(userName);
        operationLog.setDate(new Date());
	    operationLogService.addOperationLog(operationLog);
	}
	
	//查询视频列表
	@Override
	public Page<VideoVO> findVideo(Page<VideoVO> page, Map<String, Object> condition) {
	    return videoService.findVideo(page, condition);
	}
	
	//获取视频链接
	@Override
	public String getPlayUrl(VideoVO videoVO, String url, String tenantId) {
	    return videoService.getPlayUrl(videoVO, url, tenantId);
	}
	
	//加载模块列表
	@Override
	public List<Module> loadModule(String tenantId) {
	    return videoService.loadModule(tenantId);
	}
	
	@Override
	public Page<OperationLogVO> findOperationLogPage(Page<OperationLogVO> page, Map<String, Object> condition) {
	    return operationLogService.findPage(page, condition);
	}

}

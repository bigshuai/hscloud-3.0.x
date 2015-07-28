/* 
* 文 件 名:  OperationLogService.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-8-5 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.storage.service; 

import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.storage.entity.OperationLog;
import com.hisoft.hscloud.storage.vo.OperationLogVO;

/** 
 * <操作日志服务> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-8-5] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface OperationLogService {
    /**
     * <日志查询> 
    * <功能详细描述> 
    * @param condition
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public Page<OperationLogVO> findPage(Page<OperationLogVO> page, Map<String, Object> condition);
    
    /**
     * <记录日志> 
    * <功能详细描述> 
    * @param operationLog 
    * @see [类、类#方法、类#成员]
     */
    public void addOperationLog(OperationLog operationLog);
}

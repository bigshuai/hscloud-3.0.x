/* 
* 文 件 名:  OperationLogServiceImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-8-5 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.storage.service.impl; 

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.storage.dao.OperationLogDao;
import com.hisoft.hscloud.storage.entity.OperationLog;
import com.hisoft.hscloud.storage.service.OperationLogService;
import com.hisoft.hscloud.storage.vo.OperationLogVO;
import com.hisoft.hscloud.storage.vo.OperationType;

/** 
 * <操作日志服务> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-8-5] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class OperationLogServiceImpl implements OperationLogService{
    @Autowired
    private OperationLogDao operationLogDao;
    
    @Override
    public Page<OperationLogVO> findPage(Page<OperationLogVO> page, Map<String, Object> condition) {
        Page<OperationLog> operationLogPage = new Page<OperationLog>();
        operationLogPage.setOrder(page.getOrder());
        operationLogPage.setOrderBy(page.getOrderBy());
        operationLogPage.setPageNo(page.getPageNo());
        operationLogPage.setPageSize(page.getPageSize());
        operationLogPage = operationLogDao.findPage(operationLogPage, condition);
        
        page.setResult(new ArrayList<OperationLogVO>());
        for(OperationLog OperationLog : operationLogPage.getResult()) {
            OperationLogVO operationLogVO = new OperationLogVO();
            try {
                BeanUtils.copyProperties(operationLogVO, OperationLog);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            operationLogVO.setOperationTypeText(OperationType.getCodeById(operationLogVO.getOperationType()));
            page.getResult().add(operationLogVO);
        }
        page.setTotalCount(operationLogPage.getTotalCount());
        return page;
    }

    @Override
    public void addOperationLog(OperationLog operationLog) {
        operationLogDao.addOperationLog(operationLog);
    }

}

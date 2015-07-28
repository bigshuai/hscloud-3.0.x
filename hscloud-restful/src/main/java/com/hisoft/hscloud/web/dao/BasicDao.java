/* 
* 文 件 名:  BasicDao.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-6-25 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.dao; 

import java.util.List;
import java.util.Map;

import com.hisoft.hscloud.crm.usermanager.entity.PlatformRelation;

/** 
 * <基础查询> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-6-25] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface BasicDao {
    public Map<String, Object> queryAccessByAccessId(String accessId);

    public String queryReferenceIdByVmId(String vmId, String userId);
    
    /**
     * <查询ip> 
    * <功能详细描述> 
    * @param accessid
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public List<String> getIpByAccesskey(String accessid);
    
    /**
     * <创建任务记录> 
    * <功能详细描述> 
    * @param taskMap
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public long saveTask(Map<String, Object> taskMap);
    /**
     * <根据AccessId和emailaddr查询AccessKey> 
    * <功能详细描述> 
    * @param accessId
    * @param emailaddr
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public Map<String, Object> queryAccessByAccessId(String accessId,String emailaddr);
    /**
     * <查询外部平台用户是否已经存在于hscloud数据库中> 
    * <功能详细描述> 
    * @param exteranalUserId
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public PlatformRelation getPlatformRelationByExteranalUserId(String externalUserId);
    /**
     * <添加外部平台用户到hscloud数据库中> 
    * <功能详细描述> 
    * @param exteranalUserId
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public boolean addPlatformRelation(String externalUserId);
}

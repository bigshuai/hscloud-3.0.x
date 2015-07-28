/* 
* 文 件 名:  RenewOrderService.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-6-25 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.service; 

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.web.vo.RenewOrderVo;

/** 
 * <续费> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-6-25] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface RenewOrderService {
    /**
     * <续费> 
    * <功能详细描述> 
    * @param referenceId
    * @param feeTypeId
    * @param user
    * @return
    * @throws HsCloudException 
    * @see [类、类#方法、类#成员]
     */
    public String renewOrderV2(long referenceId,String paymode,String feeTypeId, User user, RenewOrderVo renewOrderVo, String doUseCoupon, String doUseGift)
            throws HsCloudException;

    
    /**
     * <查询referenceId> 
    * <功能详细描述> 
    * @param vmId
    * @return 
    * @see [类、类#方法、类#成员]
     */
//    public long queryReferenceId(String vmId, String userId);

}

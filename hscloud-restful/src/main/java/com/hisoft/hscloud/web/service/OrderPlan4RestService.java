/* 
* 文 件 名:  OrderPlan4RestService.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-5-29 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.service; 

import java.util.Map;

import com.hisoft.hscloud.web.vo.OrderPlanVo;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-5-29] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface OrderPlan4RestService {
    
    public String getFactorySeq(String accessid);
    public String getUserId(String accessid);
    
    public Map<String, Object> queryAccessByAccessId(String accessid);
    /**
     * <大客户购买套餐> 
    * <功能详细描述> 
    * @param scId
    * @param osId
    * @param period
    * @param scNum
    * @param userId
    * @param orderPlanVos
    * @param id
    * @param accessId
    * @param doUseCoupon
    * @param doUseGift
    * @return
    * @throws Exception 
    * @see [类、类#方法、类#成员]
     */
    public Map<String, Object> scBuyDirect(int scId, int osId, String period,
            int scNum, long userId, OrderPlanVo orderPlanVos, 
            String id, String accessId, String doUseCoupon, String doUseGift) throws Exception;
    /**
     * <分平台用户购买套餐> 
    * <功能详细描述> 
    * @param code
    * @param osId
    * @param period
    * @param scNum
    * @param userId
    * @param orderPlanVos
    * @param id
    * @param accessId 分平台code
    * @param doUseCoupon
    * @param doUseGift
    * @return
    * @throws Exception 
    * @see [类、类#方法、类#成员]
     */
    public Map<String,Object> applyOrderPlan(String code, int osId, String period, int scNum,
            long userId, OrderPlanVo orderPlanVos, String id, String accessId, String doUseCoupon, String doUseGift) throws Exception;
}

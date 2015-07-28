/* 
* 文 件 名:  UserBrandAction.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-8-8 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.action; 

import org.springframework.beans.factory.annotation.Autowired;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.entity.UserBrand;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.web.facade.Facade;

/** 
 * <用户品牌> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-8-8] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class UserBrandAction extends HSCloudAction {

    /** 
    * 注释内容 
    */
    private static final long serialVersionUID = 2758956485099593705L;
    
    @Autowired
    private Facade facade;
    
    /**
     * <获取返点率> 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void getRebateRate() {
        User user = (User)this.getCurrentLoginUser();
        String code = user.getLevel();
        UserBrand userBrand = facade.getBrandByCode(code);
        
        fillActionResult(userBrand.getRebateRate());
    }
}

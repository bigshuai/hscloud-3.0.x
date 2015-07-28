/* 
* 文 件 名:  IcpThreadServiceImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-1-9 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.crm.icp.service.Impl; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.crm.icp.service.IcpService;
import com.hisoft.hscloud.crm.icp.service.IcpThreadService;
import com.hisoft.hscloud.crm.icp.vo.IcpVO;
import com.hisoft.hscloud.crm.usermanager.entity.User;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-1-9] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class IcpThreadServiceImpl extends Thread implements IcpThreadService{
    
    @Autowired
    private IcpService icpService;
    
    private User user;
    private IcpVO icpVO;
    private String company;
    
    @Override
    public void startRun(User user, IcpVO icpVO, String company) {
        this.user = user;
        this.icpVO = icpVO;
        this.company = company;
        
        this.start();
    }
    
    public void run() {
   //     icpService.icpPutOnRecord(user, icpVO, company);
    }
}

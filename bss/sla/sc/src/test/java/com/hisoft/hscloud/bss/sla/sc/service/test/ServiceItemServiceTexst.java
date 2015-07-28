/* 
* 文 件 名:  ServiceItemServiceTexst.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2014-1-26 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.sc.service.test; 

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.sc.entity.Os;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceItem;
import com.hisoft.hscloud.bss.sla.sc.service.IServiceItemService;
import com.hisoft.hscloud.common.util.Sort;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2014-1-26] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-bss-sla-sc-test.xml"})
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=false)
@Transactional
public class ServiceItemServiceTexst {
    @Autowired
    private IServiceItemService serviceItemService;
    
    @Test
    public void testPageServiceItemByType() {
        Page<ServiceItem> paging = new Page<ServiceItem>();
        int serviceType = 4;
        String query = null;
        List<Sort> sortList = null;
        serviceItemService.pageServiceItemByType(paging, serviceType, sortList, query);
    }
    
    @Test
    public void testGetOs() {
        int osId = 35;
        Os os = serviceItemService.getOs(osId);
        System.out.println(os.getImageId());
    }
}

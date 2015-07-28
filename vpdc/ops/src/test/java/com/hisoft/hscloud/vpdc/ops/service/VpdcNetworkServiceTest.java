/* 
* 文 件 名:  VpdcNetworkServiceTest.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2014-2-10 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.service; 

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

import com.hisoft.hscloud.vpdc.ops.json.bean.NetWorkBean;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2014-2-10] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@Transactional  
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@ContextConfiguration(locations = { "classpath:applicationContext-hscloud-vpdc-ops-test.xml" })
public class VpdcNetworkServiceTest {
    @Autowired
    private VpdcNetworkService vpdcNetworkService;
    
    @Test
    public void testCreateWanNetwork() {
        NetWorkBean netWork = new NetWorkBean();
        netWork.setCidr("192.168.2.0/27");
        netWork.setDns1("114.114.114.114");
        netWork.setGateway("192.168.2.254");
        netWork.setZoneGroup(1l);
        netWork.setIpRangeId(45l);
        
        long adminId = 1l;
        vpdcNetworkService.createWanNetwork(netWork, adminId);
    }
    
    @Test
    public void testFindPageNetwork() {
        Page<NetWorkBean> pageNetworkBean = new Page<NetWorkBean>();
        pageNetworkBean.setPageNo(1);
        pageNetworkBean.setPageSize(16);
        pageNetworkBean = vpdcNetworkService.findPageNetwork(pageNetworkBean);
        System.out.println(pageNetworkBean.getResult().size());
    }
    
    @Test
    public void testDeleteNetwork() {
        long id = 1l;
        vpdcNetworkService.deleteNetwork(id);
    }
}

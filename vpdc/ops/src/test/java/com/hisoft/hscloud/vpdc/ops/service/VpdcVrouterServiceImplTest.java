/* 
* 文 件 名:  VpdcVrouterServiceImplTest.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2014-1-23 
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

import com.hisoft.hscloud.vpdc.ops.entity.VpdcVrouterTemplate;
import com.hisoft.hscloud.vpdc.ops.vo.VrouterTemplateVO;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2014-1-23] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@Transactional  
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@ContextConfiguration(locations = { "classpath:applicationContext-hscloud-vpdc-ops-test.xml" })
public class VpdcVrouterServiceImplTest {
    
    
    @Autowired
    private VpdcVrouterService vpdcVrouterService;
    
   /* @Test
    public void testAddVrouterTemplate() {
        VpdcVrouterTemplate vpdcVrouterTemplate = new VpdcVrouterTemplate();
        vpdcVrouterTemplate.setCpuCore(1);
        vpdcVrouterTemplate.setDiskCapacity(1);
        vpdcVrouterTemplate.setMemSize(128);
        vpdcVrouterTemplate.setOsId(1);
        vpdcVrouterTemplate.setImageId("ttttt");
        
        VrouterTemplateVO vrouterTemplateVO = new VrouterTemplateVO();
        vrouterTemplateVO.setName("name");
        vrouterTemplateVO.setCpuCore(1);
        vrouterTemplateVO.setDiskCapacity(1);
        vrouterTemplateVO.setMemSize(128);
        vrouterTemplateVO.setOsId(1);
        vrouterTemplateVO.setImageId("ttttt");
        vpdcVrouterService.addVrouterTemplate(vrouterTemplateVO);
    }
    
    @Test
    public void testUpdateVrouterTemplate() {
        VrouterTemplateVO vrouterTemplateVO = new VrouterTemplateVO();
        vrouterTemplateVO.setName("nameUpdate");
        vrouterTemplateVO.setCpuCore(1);
        vrouterTemplateVO.setDiskCapacity(1);
        vrouterTemplateVO.setMemSize(128);
        vrouterTemplateVO.setOsId(1);
        vrouterTemplateVO.setImageId("ttttt");
        vrouterTemplateVO.setId(6l);
        vpdcVrouterService.updateVrouterTemplate(vrouterTemplateVO);
    }*/
    
    @Test
    public void testGetVrouterTemplate() {
        long id = 1l;
        VpdcVrouterTemplate vpdcVrouterTemplate = vpdcVrouterService.getVrouterTemplate(id);
        System.out.println(vpdcVrouterTemplate.getCpuCore());
    }
    
    @Test
    public void testPageVrouterTemplate() {
        Page<VrouterTemplateVO> page = new Page<VrouterTemplateVO>();
        page = vpdcVrouterService.pageVrouterTemplateVO(page);
        System.out.println(page);
    }
    
    /*@Test
    public void testGetVrouterTemplateByName() {
        List<VpdcVrouterTemplate> list = vpdcVrouterService.getVrouterTemplateByName("123999");
        if(list == null || list.isEmpty()) {
            System.out.println("list is empty");
        } else {
            System.out.println(list);
        }
    }*/
}

/**
 * 
 */
package com.hisoft.usermanager.service.test;

import java.util.Date;

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

import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.service.DomainService;
import com.hisoft.hscloud.crm.usermanager.util.Constant;
import com.hisoft.hscloud.crm.usermanager.vo.DomainVO;

/**
 * @author lihonglei
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@Transactional  
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@ContextConfiguration(locations = { "classpath:applicationContext-hscloud-crm-usermanager.xml" })
public class DomainServiceTest {
    @Autowired
    private DomainService domainService;
    
    /*@Test
    public void testFindDomainList() {
        long roleId = 1l;
        Page page = new Page();
        page = domainService.findDomainList(page, roleId);
        System.out.println(page);
    }*/
    @Test
    public void testEditDomainPermission() {
        long roleId = 1l;
        String permissionValue = "1,2";
        String resourceValue = "1,2";
        domainService.editDomainPermission(permissionValue, resourceValue, roleId);
    }
    
    @Test
    public void testEditDomain() {
        for(int i = 0; i < 30; i++) {
            String abbreviation = "abbreviation" + i;
            String address = "address" + i;
            String bank = "bank" + i;
            String cardNo = "cardNo";
            String code = "code" + i;
            long createId = 1l;
            String description = "description";
            String name = "name";
            String remark = "remark";
            String status = Constant.DOMAIN_STATUS_VALID;
            String telephone = "123456";
            String url = "url";
            long updateId = 1l;
            Date date = new Date();
            
            long adminId = 1l;
            
            DomainVO domain = new DomainVO();
            domain.setAbbreviation(abbreviation);
            domain.setAddress(address);
            domain.setBank(bank);
            domain.setCardNo(cardNo);
            domain.setCode(code);
          /*  domain.setCreateId(createId);
            domain.setCreateTime(date);*/
            domain.setDescription(description);
            domain.setName(name);
           /* domain.setRemark(remark);
            domain.setStatus(status);*/
            domain.setTelephone(telephone);
           /* domain.setUpdateId(updateId);
            domain.setUpdateTime(date);*/
            domain.setUrl(url);
            domainService.editDomain(domain, adminId);
        }
    }
}

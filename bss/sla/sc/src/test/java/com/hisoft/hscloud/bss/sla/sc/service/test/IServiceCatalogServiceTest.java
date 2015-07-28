package com.hisoft.hscloud.bss.sla.sc.service.test;

import java.util.ArrayList;
import java.util.Date;
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

import com.hisoft.hscloud.bss.sla.sc.entity.ServerZone;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceCatalog;
import com.hisoft.hscloud.bss.sla.sc.service.IServerZoneService;
import com.hisoft.hscloud.bss.sla.sc.vo.ZoneVO;
import com.hisoft.hscloud.bss.sla.sc.service.*;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-bss-sla-sc-test.xml"})
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=false)
@Transactional
public class IServiceCatalogServiceTest {
	@Autowired
	private IServiceCatalogService iServiceCatalogService;
	@Test
	public void testDeleteServerZone(){
		ServiceCatalog result = new ServiceCatalog() ;
		result = iServiceCatalogService.getByCode("12345678", "GHXW","brand1");
		System.out.println("result:"+result);
	}
	
	
	
}

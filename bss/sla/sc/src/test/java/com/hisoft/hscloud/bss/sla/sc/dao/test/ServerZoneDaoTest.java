/* 
* 文 件 名:  ServerZoneDaoTest.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-3-12 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.sc.dao.test; 

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

import com.hisoft.hscloud.bss.sla.sc.dao.ServerZoneDao;
import com.hisoft.hscloud.bss.sla.sc.entity.ServerZone;

/** 
 * <ServerZoneDaoTest> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-3-12] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-bss-sla-sc-test.xml"})
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=false)
@Transactional
public class ServerZoneDaoTest {

	@Autowired
	private ServerZoneDao serverZoneDao;
	@Test
	public void testCreateServerZone(){
		ServerZone serverZone = new ServerZone();
		serverZone.setCode("Z1");
		serverZone.setName("Zone1");
		serverZone.setCreateDate(new Date());
		serverZone.setCreateId(1L);
		serverZone.setDescription("test");
		serverZone.setIsDefault(1);
		serverZone.setUpdateDate(new Date());
		serverZone.setUpdateId(1L);
		serverZoneDao.createServerZone(serverZone);
		System.out.println("result:"+serverZone.getId());
	}
	@Test
	public void testDeleteServerZone(){
		boolean result=serverZoneDao.deleteServerZone(19);
		System.out.println("result:"+result);
	}
	@Test
	public void testUpdateServerZone(){
		ServerZone serverZone = new ServerZone();
		serverZone.setId(13l);
		serverZone.setCode("Z1");
		serverZone.setName("Zone1");
		serverZone.setCreateDate(new Date());
		serverZone.setCreateId(1L);
		serverZone.setDescription("test");
		serverZone.setIsDefault(1);
		serverZone.setUpdateDate(new Date());
		serverZone.setUpdateId(1L);
		boolean result = serverZoneDao.updateServerZone(serverZone);
		System.out.println("result:"+result);
	}
	@Test
	public void testGetServerZoneById(){
		ServerZone serverZone = serverZoneDao.getServerZoneById(13);
		System.out.println("result:"+serverZone);
	}
	@Test
	public void testFindServerZone(){
		Page<ServerZone> pageServerZone = new Page<ServerZone>();
		pageServerZone.setPageNo(0);
		pageServerZone.setPageSize(16);
		pageServerZone = serverZoneDao.findServerZone(pageServerZone, "name", "zone1");
		System.out.println("result:"+pageServerZone);
	}
	@Test
	public void testGetAllZones(){
		List<ServerZone> serverZoneList = serverZoneDao.getAllZones();
		System.out.println("result:"+serverZoneList);
	}
	@Test
	public void testBatchUpdateServerZone(){
		boolean result = serverZoneDao.batchUpdateServerZone(0);
		System.out.println("result:"+result);
	}
	@Test
	public void testGetServerZonesByCondition(){
		List<ServerZone> serverZoneList = serverZoneDao.getServerZonesByCondition("code", "z1");
		System.out.println("result:"+serverZoneList);
	}
	@Test
	public void testGetDefaultServerZone(){
		ServerZone serverZone = serverZoneDao.getDefaultServerZone();
		System.out.println("result:"+serverZone);
	}
	@Test
	public void getAllZonesByCondition(){
		List<ServerZone> serverZoneList = serverZoneDao.getAllZonesByCondition(1, "0");
		System.out.println("result:"+serverZoneList);
	}
}

/* 
* 文 件 名:  ServerZoneServiceTest.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-3-21 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
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
import com.hisoft.hscloud.bss.sla.sc.service.IServerZoneService;
import com.hisoft.hscloud.bss.sla.sc.vo.ZoneVO;

/** 
 * <ServerZoneServiceTest> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-3-21] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-bss-sla-sc-test.xml"})
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=false)
@Transactional
public class ServerZoneServiceTest {

	@Autowired
	private IServerZoneService zoneService;
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
		zoneService.createServerZone(serverZone);
		System.out.println("result:"+serverZone.getId());
	}
	@Test
	public void testDeleteServerZone(){
		boolean result=zoneService.deleteServerZone(19);
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
		boolean result = zoneService.updateServerZone(serverZone);
		System.out.println("result:"+result);
	}
	@Test
	public void testGetServerZoneById(){
		ServerZone serverZone = zoneService.getServerZoneById(13);
		System.out.println("result:"+serverZone);
	}
	@Test
	public void testFindServerZone(){
		Page<ServerZone> pageServerZone = new Page<ServerZone>();
		pageServerZone.setPageNo(0);
		pageServerZone.setPageSize(16);
		pageServerZone = zoneService.findServerZone(pageServerZone, "name", "zone1");
		System.out.println("result:"+pageServerZone);
	}
	@Test
	public void testGetAllZones(){
		List<ServerZone> serverZoneList = zoneService.getAllZones();
		System.out.println("result:"+serverZoneList);
	}
	@Test
	public void testSetDefaultServerZone(){
		ServerZone serverZone = new ServerZone();
		serverZone.setId(13l);		
		boolean result = zoneService.setDefaultServerZone(serverZone);
		System.out.println("result:"+result);
	}
	@Test
	public void testHasSameZoneName(){
		ServerZone serverZone = new ServerZone();
		serverZone.setName("Zone1");		
		boolean result = zoneService.hasSameZoneName(serverZone);
		System.out.println("result:"+result);
	}
	@Test
	public void testHasSameZoneCode(){
		ServerZone serverZone = new ServerZone();
		serverZone.setName("Zone1");		
		boolean result = zoneService.hasSameZoneCode(serverZone);
		System.out.println("result:"+result);
	}
	@Test
	public void testGetServerZonesByCondition(){
		List<ServerZone> serverZoneList = zoneService.getServerZonesByCondition("code", "z1");
		System.out.println("result:"+serverZoneList);
	}
	@Test
	public void testGetDefaultServerZone(){
		ServerZone serverZone = zoneService.getDefaultServerZone();
		System.out.println("result:"+serverZone);
	}
	@Test
	public void testGetRightServerZoneOfGroup(){
		ServerZone serverZone = zoneService.getRightServerZoneOfGroup(1);
		System.out.println("result:"+serverZone);
	}
	@Test
	public void testGetRightServerZone(){
//		List<Object> zoneIds = new ArrayList<Object>();
//		zoneIds.add(1L);
//		zoneIds.add(2L);
		String zoneCode = "nova,beijing";
		ServerZone serverZone = zoneService.getRightServerZoneOfGroup(zoneCode);
		System.out.println("result:"+serverZone);
	}
	@Test
	public void testGetRelatedServerZone(){
		Page<ZoneVO> pageZoneVO = new Page<ZoneVO>();
		pageZoneVO.setPageNo(0);
		pageZoneVO.setPageSize(16);
		pageZoneVO = zoneService.getRelatedServerZone(pageZoneVO, 1, "");
		System.out.println("result:"+pageZoneVO);
	}
	@Test
	public void testGetUnRelatedServerZone(){
		Page<ZoneVO> pageZoneVO = new Page<ZoneVO>();
		pageZoneVO.setPageNo(0);
		pageZoneVO.setPageSize(16);
		pageZoneVO = zoneService.getUnRelatedServerZone(pageZoneVO, 1, "");
		System.out.println("result:"+pageZoneVO);
	}
}

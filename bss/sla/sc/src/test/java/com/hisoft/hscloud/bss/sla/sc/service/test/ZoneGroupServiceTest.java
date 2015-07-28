/* 
* 文 件 名:  ZoneGroupServiceTest.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-6-21 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.sc.service.test; 

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

import com.hisoft.hscloud.bss.sla.sc.entity.ZoneGroup;
import com.hisoft.hscloud.bss.sla.sc.service.ZoneGroupService;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-6-21] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-bss-sla-sc-test.xml"})
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=false)
@Transactional
public class ZoneGroupServiceTest {

	@Autowired
	private ZoneGroupService zoneGroupService;
	@Test
	public void testCreateZoneGroup(){
		ZoneGroup zoneGroup = new ZoneGroup();
		zoneGroup.setCode("Z2");
		zoneGroup.setName("Zone2");
		zoneGroup.setCreateDate(new Date());
		zoneGroup.setCreateId(1L);
		zoneGroup.setDescription("test");
		zoneGroup.setUpdateDate(new Date());
		zoneGroup.setUpdateId(1L);
		zoneGroupService.createZoneGroup(zoneGroup);
		System.out.println("result:"+zoneGroup.getId());
	}
	@Test
	public void testDeleteZoneGroup(){
		boolean result=zoneGroupService.deleteZoneGroup(5);
		System.out.println("result:"+result);
	}
	@Test
	public void testUpdateZoneGroup(){
		ZoneGroup zoneGroup = new ZoneGroup();
		zoneGroup.setId(6);
		zoneGroup.setCode("Z2");
		zoneGroup.setName("Zone2");
		zoneGroup.setCreateDate(new Date());
		zoneGroup.setCreateId(1L);
		zoneGroup.setDescription("test");
		zoneGroup.setUpdateDate(new Date());
		zoneGroup.setUpdateId(1L);
		boolean result = zoneGroupService.updateZoneGroup(zoneGroup);
		System.out.println("result:"+result);
	}
	@Test
	public void testGetZoneGroupById(){
		ZoneGroup zoneGroup = zoneGroupService.getZoneGroupById(6);
		System.out.println("result:"+zoneGroup);
	}
	@Test
	public void testFindZoneGroup(){
		Page<ZoneGroup> pageZoneGroup = new Page<ZoneGroup>();
		pageZoneGroup.setPageNo(0);
		pageZoneGroup.setPageSize(16);
		pageZoneGroup = zoneGroupService.findZoneGroup(pageZoneGroup, "name", "Zone1");
		System.out.println("result:"+pageZoneGroup);
	}
	@Test
	public void testGetAllZones(){
		List<ZoneGroup> zoneGroupList = zoneGroupService.getAllZoneGroups();
		System.out.println("result:"+zoneGroupList);
	}
	@Test
	public void testHasSameZoneName(){
		ZoneGroup zoneGroup = new ZoneGroup();
		zoneGroup.setName("Zone1");		
		boolean result = zoneGroupService.hasSameZoneGroupName(zoneGroup);
		System.out.println("result:"+result);
	}
	@Test
	public void testHasSameZoneCode(){
		ZoneGroup zoneGroup = new ZoneGroup();
		zoneGroup.setCode("");		
		boolean result = zoneGroupService.hasSameZoneGroupCode(zoneGroup);
		System.out.println("result:"+result);
	}
}

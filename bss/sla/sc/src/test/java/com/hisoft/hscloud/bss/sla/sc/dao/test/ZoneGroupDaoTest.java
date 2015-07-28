/* 
* 文 件 名:  ZoneGroupDaoTest.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-6-21 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.sc.dao.test; 

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

import com.hisoft.hscloud.bss.sla.sc.dao.ZoneGroupDao;
import com.hisoft.hscloud.bss.sla.sc.entity.ZoneGroup;

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
public class ZoneGroupDaoTest {

	@Autowired
	private ZoneGroupDao zoneGroupDao;
	@Test
	public void testCreateZoneGroup(){
		ZoneGroup zoneGroup = new ZoneGroup();
		zoneGroup.setCode("Z1");
		zoneGroup.setName("Zone1");
		zoneGroup.setCreateDate(new Date());
		zoneGroup.setCreateId(1L);
		zoneGroup.setDescription("test");
		zoneGroup.setUpdateDate(new Date());
		zoneGroup.setUpdateId(1L);
		zoneGroupDao.createZoneGroup(zoneGroup);
		System.out.println("result:"+zoneGroup.getId());
	}
	@Test
	public void testDeleteZoneGroup(){
		boolean result=zoneGroupDao.deleteZoneGroup(1);
		System.out.println("result:"+result);
	}
	@Test
	public void testUpdateZoneGroup(){
		ZoneGroup zoneGroup = new ZoneGroup();
		zoneGroup.setId(5);
		zoneGroup.setCode("Z1");
		zoneGroup.setName("Zone1");
		zoneGroup.setCreateDate(new Date());
		zoneGroup.setCreateId(1L);
		zoneGroup.setDescription("test");
		zoneGroup.setUpdateDate(new Date());
		zoneGroup.setUpdateId(1L);
		boolean result = zoneGroupDao.updateZoneGroup(zoneGroup);
		System.out.println("result:"+result);
	}
	@Test
	public void testGetZoneGroupById(){
		ZoneGroup zoneGroup = zoneGroupDao.getZoneGroupById(6);
		System.out.println("result:"+zoneGroup);
	}
	@Test
	public void testFindZoneGroup(){
		Page<ZoneGroup> pageZoneGroup = new Page<ZoneGroup>();
		pageZoneGroup.setPageNo(0);
		pageZoneGroup.setPageSize(16);
		pageZoneGroup = zoneGroupDao.findZoneGroup(pageZoneGroup, "name", "zone1");
		System.out.println("result:"+pageZoneGroup);
	}
	@Test
	public void testGetAllZones(){
		List<ZoneGroup> zoneGroupList = zoneGroupDao.getAllZoneGroups();
		System.out.println("result:"+zoneGroupList);
	}
	@Test
	public void testGetZoneGroupsByCondition(){
		List<ZoneGroup> zoneGroupList = zoneGroupDao.getZoneGroupsByCondition("name", "Zone1");
		System.out.println("result:"+zoneGroupList);
	}
	@Test
	public void testGetAllZoneGroupIdsByZoneIds(){
		List<Long> ids = new ArrayList<Long>();
		ids.add(1L);
		ids.add(27L);
		List<Long> groupidList = zoneGroupDao.getAllZoneGroupIdsByZoneIds(ids);
		System.out.println("result:"+groupidList);
	}
}

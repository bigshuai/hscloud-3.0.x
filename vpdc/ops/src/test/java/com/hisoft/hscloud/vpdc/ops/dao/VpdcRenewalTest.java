/* 
* 文 件 名:  VpdcRenewalTest.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-5-13 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.dao; 

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

import com.hisoft.hscloud.vpdc.ops.entity.VpdcRenewal;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-5-13] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-vpdc-ops-test.xml"})
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=false)
@Transactional
public class VpdcRenewalTest {

	@Autowired
	private VpdcRenewalDao vpdcRenewalDao;
	@Test
	public void testSaveVpdcRenewal(){
		VpdcRenewal vpdcRenewal = new VpdcRenewal();
		vpdcRenewal.setId(1l);
		vpdcRenewal.setName("26fcc188-00c3-4b20-a9be-27177d464376");
		vpdcRenewal.setReferenceId(355);
		vpdcRenewal.setVmUUID("d18e04e7-1d1f-46a8-868f-31e404277436");
		vpdcRenewal.setOrderNo("imsVyS1368263571502");
		vpdcRenewal.setBusinessType(1);
		vpdcRenewal.setBusinessStatus(1);
		vpdcRenewal.setStartTime(new Date());
		vpdcRenewal.setEndTime(new Date());
		long id=vpdcRenewalDao.saveVpdcRenewal(vpdcRenewal);
		System.out.println("result:"+id);
	}
	@Test
	public void testGetIdByReferenceId(){
		long id = vpdcRenewalDao.getIdByReferenceId(355);
		System.out.println("result:"+id);
	}
	@Test
	public void testDeleteVpdcRenewalByReferenceId(){
		boolean result = vpdcRenewalDao.deleteVpdcRenewalByReferenceId(358);
		System.out.println("result:"+result);
	}
	@Test
	public void testFindVpdcRenewal(){
		Page<VpdcRenewal> page = new Page<VpdcRenewal>();
		page.setPageNo(0);
		page.setPageSize(10);
		String field="businessType";
		int fieldValue=1;
		String query = "26e";
		page = vpdcRenewalDao.findVpdcRenewal(page, field, fieldValue, query,55);
		System.out.println("result:"+page);
	}
}

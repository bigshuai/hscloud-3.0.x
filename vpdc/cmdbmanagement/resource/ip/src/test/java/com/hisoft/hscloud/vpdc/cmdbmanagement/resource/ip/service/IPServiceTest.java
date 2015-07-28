package com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;


import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.IPConvert;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.entity.IPDetail;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.entity.IPRange;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.util.BeanReadUtil;

/*@TestExecutionListeners(TestExecutionListener.class)
@ContextConfiguration(locations = { "classpath:applicationContext-hscloud-vpdc-cmdbmanagement-resource-ip.xml" })
*/
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@Transactional  
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@ContextConfiguration(locations = { "classpath:applicationContext-hscloud-vpdc-cmdbmanagement-resource-ip-test.xml" })

public class IPServiceTest {
	
	@Autowired
	private IPService iPService;
//	@Test
//	public void testCreateIP(){
//		String dataid=ips.createIP(IPConvert.getIntegerIP("192.168.5.1"), IPConvert.getIntegerIP("192.168.5.10"), 1, "");
//		System.out.println(dataid);
//	}
//	@Test
//	public void testDeleteIP(){
//		boolean operateFlag=ips.deleteIP(2);
//		System.out.println(operateFlag);
//	}
	
//	@Test
//	public void testFindIPRangeByUId(){
//		Page<IPRange> page=new Page<IPRange>();
//		page.setPageNo(0);
//		page.setPageSize(10);
//		page=ips.findIPRangeByUId(page, 1);
//		System.out.println(page.getTotalCount());
//	}
	
//	@Test
//	public void testFindIPDetailByRId(){
//		Page<IPDetail> page=new Page<IPDetail>();
//		page.setPageNo(0);
//		page.setPageSize(10);
//		page=ips.findIPDetailByRId(page, 4);
//		System.out.println(page.getTotalCount());
//	}
	
	@Test
	public void testUpdateIPDetail() throws HsCloudException{
		boolean operateFlag;
		try {
			operateFlag = iPService.updateIPDetail(21, 1, 2,"");
			System.out.println(operateFlag);
		} catch (HsCloudException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetIPListByUserId() {
	    long userId = 2l;
	    List<BigInteger> list = iPService.getIPListByUserId(userId);
	    System.out.println(list.size());
	    for(BigInteger ip : list) {
	        IPConvert.getStringIP(ip.longValue());
	        System.out.println(ip);
	    }
	}
	
	@Test 
	public void testAssignIPDetail() {
	    long zoneId = 1l;
	    String ipStr = iPService.assignIPDetail(zoneId);
	    System.out.println(ipStr);
	}
	
	@Test
	public void testGetIpAndNetwork() {
	    long zoneGroupId = 15;
	    Map<String, String> map = iPService.getIpAndNetwork(zoneGroupId);
	    System.out.println(map);
	}
}

/* 
* 文 件 名:  IcpServiceTest.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-1-7 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.icp.service; 


import java.io.IOException;

import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.junit.Assert;
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

import com.hisoft.hscloud.crm.icp.vo.IcpVO;
import com.hisoft.hscloud.crm.icp.service.IcpService;
import com.hisoft.hscloud.crm.icp.service.IcpThreadService;
import com.hisoft.hscloud.crm.usermanager.entity.Region;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.entity.UserProfile;


/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-1-7] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@Transactional  
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@ContextConfiguration(locations = { "classpath:applicationContext-hscloud-crm-icp-test.xml" })
public class IcpServiceTest {
	@Autowired
	private IcpService icpService;
	
	/*@Autowired
    private DataService dataService;
	*/
	@Autowired
    private IcpThreadService icpThreadService;
	
	@Test
	public void testIcpPutOnRecord() throws HttpException, IOException, InterruptedException {
		User user = new User();
		UserProfile userProfile = new UserProfile();
		Region region = new Region();
		region.setNameCode("001");
		userProfile.setRegion(region);
		userProfile.setIdCard("211202198610152037");
		user.setUserProfile(userProfile);
		
		user.setUserType("EntUser");
		user.setName("lihonglei");
		
		
		IcpVO icpVO = new IcpVO();
		icpVO.setAddress("西二旗环洋大厦");
		icpVO.setCity("110000000000");
		icpVO.setContactName("李洪磊");
		icpVO.setDomain("www.hisoft.com");
		icpVO.setIcpcertId("123456");
		icpVO.setIcpUrl("www.hisoft.com");
		icpVO.setIcpwebTitle("海辉");
		icpVO.setIp("192.169.7.138");
		icpVO.setMemberPwd("123456");
		icpVO.setMobile("18600332250");
		icpVO.setOrgcertId("123456");
		icpVO.setPostcode("100000");
		icpVO.setRegistered("0");
		icpVO.setTelno("021-86868686");
		
		
		user.setEmail("honglei.li@hisoft.com");
		icpVO.setMemberLogin("honglei.li@hisoft.com");
		
		icpVO.setCountry("CN");
		icpVO.setIdNumber("211202198320152019");
		icpVO.setIdType("1");
		icpVO.setIp("10.0.0.14");
		icpVO.setMemberPwd("123456");
		icpVO.setMemberType("0");
		icpVO.setRegistered("0");
		
		
		long start = System.currentTimeMillis();
		String result = icpService.icpPutOnRecord(icpVO);
		
	//	icpThreadService.startRun(user, icpVO, company);
		long end = System.currentTimeMillis();
		
		System.out.println(result);
		System.out.println("start:" + start);
		System.out.println("end:" + end);
		System.out.println("Seconds:" + (end - start));
	}
	
	
	/*@Test
	public void testGetProvinceList() {
	    List<com.hisoft.hscloud.common.entity.Province> list = dataService.getProvinceList();
	    Assert.assertEquals(34, list.size());
	}*/
	
	/*@Test
	public void testGetCityList() {
	    String bjProvinceCode = "110000000000";
	    List<City> list = icpService.getCityList(bjProvinceCode);
	    Assert.assertEquals(1, list.size());
	    
	    String hbProvinceCode = "130000000000";
        List<City> list2 = icpService.getCityList(hbProvinceCode);
        Assert.assertEquals(11, list2.size());
	}*/
}

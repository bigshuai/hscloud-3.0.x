package com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.dao;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.entity.IPRange;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.util.BeanReadUtil;

@TestExecutionListeners(TestExecutionListener.class)
@ContextConfiguration(locations = { "classpath:applicationContext-hscloud-vpdc-cmdbmanagement-resource-ip-test.xml" })
public class IPRangeDaoTest {
	
	@Autowired
	private IPRangeDao iPRangeDao;
	IPRangeDao sIAct = (IPRangeDao)BeanReadUtil.getBean("iPRangeDao");
//	@Test
//	public void testCreateIPRange(){
//		IPRange iPRange=new IPRange();
//		iPRange.setCreateTime(new Date());
//		iPRange.setCreateUid(1);
//		iPRange.setStartIP(IPConvert.getIntegerIP("192.168.5.1"));
//		iPRange.setEndtIP(IPConvert.getIntegerIP("192.168.5.10"));
//		long id=sIAct.createIPRange(iPRange);
//		System.out.println(id);
//	}
//	@Test
//	public void testGetIPRangeById(){
//		IPRange iPRange=new IPRange();
//		iPRange=sIAct.getIPRangeById(1);
//		System.out.print(iPRange.toString());
//	}
//	
//	@Test
//	public void testDeleteIPRange(){
//		sIAct.deleteIPRange(1);
//	}
//	
	@Test
	public void testFindIPRange() throws HsCloudException{
	/*	List<IPRange> list=sIAct.findIPRange(1, 0, 15);
		System.out.print(list.size());*/
	}

}

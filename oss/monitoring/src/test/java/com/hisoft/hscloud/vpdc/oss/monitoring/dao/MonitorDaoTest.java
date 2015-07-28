package com.hisoft.hscloud.vpdc.oss.monitoring.dao;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.hisoft.hscloud.vpdc.oss.monitoring.entity.HostMonitorHistory;
import com.hisoft.hscloud.vpdc.oss.monitoring.entity.VmMonitorHistory;


 
//@Transactional  
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class MonitorDaoTest  {
	MonitorDao dao;

	private static Logger logger = Logger.getLogger(MonitorDaoTest.class);   
  
	//
//	@Test
//	public void testSaveVmMonitorHistory() {
//		 logger.info("测试开始............");   
//		 VmMonitorHistory history = new VmMonitorHistory();
//			List<VmMonitorHistory> histories = new ArrayList<VmMonitorHistory>();
//			history.setCpu_rate(12);
//			history.setMemory_rate(21);
//			history.setMonitorTime(new Date());
//			history.setVmName("test");
//			history.setVmId("1");
//			history.setOrder_item_id((long)1);
//			histories.add(history);
//			dao.saveVmMonitorHistory(histories);  
//
//	}

//	@Test
//	public void testGetVmMonitorHistoryByVmId() {
//		List<VmMonitorHistory> monitorHistories=new ArrayList<VmMonitorHistory>();
//		logger.info("enter the method to test the function of getTheVmRecord By vmId");
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		try {
//			Date date1 = dateFormat.parse("2012-05-20");
//			Date date2 = dateFormat.parse("2012-05-25");
//			System.out.println(dateFormat.format(date2) +"***"+dateFormat.format(date1));
//			logger.info("start the medthod of getVmMonitorRecordByVmid");
//			monitorHistories = dao.getVmMonitorHistoryByVmId("1", date1, date2);
//			logger.info("the num of vmHistoryRecord is:"+monitorHistories.size());
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		
//	}

//	@Test
//	public void testGetVmMonitorHistoryByOrderItemId() {
//		List<VmMonitorHistory> monitorHistories=new ArrayList<VmMonitorHistory>();
//		System.out.println("进入查询方法更具order_item_id"+1);
//		monitorHistories = dao.getVmMonitorHistoryByOrderItemId((long)1);  
//		System.out.println(monitorHistories.get(0)+"*******");
//	}
//
//	@Test
//	public void testSaveHostMonitorHistory() {
//		 logger.info("enter the test method of saveHostHistoryRecord");
//		 HostMonitorHistory history =new HostMonitorHistory();
//		 history.setCpu_rate(12);
//		 history.setCpu_total(20);
//		 history.setMonitorTime(new Date());
//		 history.setName("test1");
//		 List<HostMonitorHistory> histories = new ArrayList<HostMonitorHistory>();
//		 histories.add(history);
//		 logger.info("enter the dao method");
//		 dao.saveHostMonitorHistory(histories);
//	}
//
	@Test
	public void testGetHostMonitorHistoryByHostName() {
		List<HostMonitorHistory> monitorHistories=new ArrayList<HostMonitorHistory>();
		logger.info("enter the test method of saveHostHistoryRecord");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = null,date2 = null;
		try {
			date1 = dateFormat.parse("2012-05-20");
			date2 = dateFormat.parse("2012-05-25");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		logger.info("enter the dao method");
		monitorHistories = dao.getHostMonitorHistoryByHostName("test1",date1 , date2);
		logger.info("the num of the history record of the host name test1 is :"+monitorHistories.size());
	}

}

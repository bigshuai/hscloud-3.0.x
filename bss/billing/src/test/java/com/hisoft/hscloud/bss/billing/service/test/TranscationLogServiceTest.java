package com.hisoft.hscloud.bss.billing.service.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.dao.TranscationLogDao;
import com.hisoft.hscloud.bss.billing.entity.TranscationLog;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-bss-billing.xml"})
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=true)
@Transactional
public class TranscationLogServiceTest {
	
//	@Autowired
//	private TranscationLogService transcationLogService;
	
	@Autowired
    private TranscationLogDao transcationLogDao;
    
    @Test
    public void testfindPage(){
		Page<TranscationLog> page = new Page<TranscationLog>();
		page.setPageNo(1);
		page.setPageSize(10);
		//String sql = "select * from hc_transcation_log htl left join hc_account ha on htl.accountId=ha.id LEFT JOIN hc_user hu on hu.id=ha.user_id";
		String sql ="from TranscationLog";
		//page =transcationLogDao.findPage(page, sql, null);
    	System.out.println();
    }
    
    @Test
    public void testFindPageBySQL(){
    	String sql ="select htl.* from hc_transcation_log htl left join hc_account ha on htl.accountId=ha.id LEFT JOIN hc_user hu on hu.id=ha.user_id";
    	Map<String,Object> map = new HashMap<String,Object>();
    	//Integer l = transcationLogDao.findCountBySQL(sql, map);
    	//System.out.println(l);
    }

	

}

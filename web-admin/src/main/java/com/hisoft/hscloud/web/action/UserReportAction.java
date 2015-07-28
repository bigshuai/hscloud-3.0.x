package com.hisoft.hscloud.web.action; 
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hisoft.hscloud.common.HSCloudAction;
import com.wgdawn.common.CommonUtils;
import com.wgdawn.persist.more.model.report.MoreReportYear;
import com.wgdawn.persist.more.model.report.MoreUserAmountStatis;
import com.wgdawn.service.UserReportService;
	/** 
	 * 用户统计
	 * <功能详细描述> 
	 * @author liutao
	 * @version  [版本号, 2015-6-1] 
	 * @see  [相关类/方法] 
	 * @since  [产品/模块版本] 
	 */
	public class UserReportAction extends HSCloudAction {
		private static final long serialVersionUID = 1L;
		private Logger logger = Logger.getLogger(this.getClass());	
		@Autowired
		private UserReportService userReportService;
		private String searchAppyear;
		 /**
		 * 查询用户数量统计
		 * <功能详细描述>  
		 * @see [类、类#方法、类#成员]
		 */
		public void findUserAmountStatisticsList(){
			long beginRunTime = 0;
			List<MoreUserAmountStatis> list=new ArrayList<MoreUserAmountStatis>();
			if(logger.isDebugEnabled()){
				beginRunTime = System.currentTimeMillis();
				logger.debug("enter findUserAmountStatisticsList method.");			
			}
			if(null!=searchAppyear&&!"".endsWith(searchAppyear)){
				 list=userReportService.getUserAmountStatistics(searchAppyear);
			}else{
				Calendar sysCal=Calendar.getInstance();
				int sysYear=sysCal.get(Calendar.YEAR);
				list=userReportService.getUserAmountStatistics(String.valueOf(sysYear));
			}
		    fillActionResult(list);
		    if(logger.isDebugEnabled()){
				long takeTime = System.currentTimeMillis() - beginRunTime;
				logger.debug("exit findUserAmountStatisticsList method.takeTime:" + takeTime + "ms");
			}
		}
		
		
		 /**
		 * 查询非成都企业用户数量统计
		 * <功能详细描述>  
		 * @see [类、类#方法、类#成员]
		 */
		public void findNoEntUserAmountStatisticsList(){
			long beginRunTime = 0;
			List<MoreUserAmountStatis> list=new ArrayList<MoreUserAmountStatis>();
			if(logger.isDebugEnabled()){
				beginRunTime = System.currentTimeMillis();
				logger.debug("enter findNoEntUserAmountStatisticsList method.");			
			}
			if(null!=searchAppyear&&!"".endsWith(searchAppyear)){
				 list=userReportService.getNoEntUserAmountStatistics(searchAppyear);
			}else{
				Calendar sysCal=Calendar.getInstance();
				int sysYear=sysCal.get(Calendar.YEAR);
				list=userReportService.getNoEntUserAmountStatistics(String.valueOf(sysYear));
			}
		    fillActionResult(list);
		    if(logger.isDebugEnabled()){
				long takeTime = System.currentTimeMillis() - beginRunTime;
				logger.debug("exit findNoEntUserAmountStatisticsList method.takeTime:" + takeTime + "ms");
			}
		}
		
		
		 /**
		 * 查询成都企业用户数量统计
		 * <功能详细描述>  
		 * @see [类、类#方法、类#成员]
		 */
		public void findEntUserAmountStatisticsList(){
			long beginRunTime = 0;
			List<MoreUserAmountStatis> list=new ArrayList<MoreUserAmountStatis>();
			if(logger.isDebugEnabled()){
				beginRunTime = System.currentTimeMillis();
				logger.debug("enter findEntUserAmountStatisticsList method.");			
			}
			if(null!=searchAppyear&&!"".endsWith(searchAppyear)){
				 list=userReportService.getEntUserAmountStatistics(searchAppyear);
			}else{
				Calendar sysCal=Calendar.getInstance();
				int sysYear=sysCal.get(Calendar.YEAR);
				list=userReportService.getEntUserAmountStatistics(String.valueOf(sysYear));
			}
		    fillActionResult(list);
		    if(logger.isDebugEnabled()){
				long takeTime = System.currentTimeMillis() - beginRunTime;
				logger.debug("exit findEntUserAmountStatisticsList method.takeTime:" + takeTime + "ms");
			}
		}
		
		 /**
		 * 查询个人用户数量统计
		 * <功能详细描述>  
		 * @see [类、类#方法、类#成员]
		 */
		public void findPersonUserAmountStatisticsList(){
			long beginRunTime = 0;
			List<MoreUserAmountStatis> list=new ArrayList<MoreUserAmountStatis>();
			if(logger.isDebugEnabled()){
				beginRunTime = System.currentTimeMillis();
				logger.debug("enter findPersonUserAmountStatisticsList method.");			
			}
			if(null!=searchAppyear&&!"".endsWith(searchAppyear)){
				 list=userReportService.getPersonUserAmountStatistics(searchAppyear);
			}else{
				Calendar sysCal=Calendar.getInstance();
				int sysYear=sysCal.get(Calendar.YEAR);
				list=userReportService.getPersonUserAmountStatistics(String.valueOf(sysYear));
			}
		    fillActionResult(list);
		    if(logger.isDebugEnabled()){
				long takeTime = System.currentTimeMillis() - beginRunTime;
				logger.debug("exit findPersonUserAmountStatisticsList method.takeTime:" + takeTime + "ms");
			}
		}
		 /**
		 * 查询报表年份(查询条件)
		 * <功能详细描述>  
		 * @see [类、类#方法、类#成员]
		 */
		public void findReoprtYear(){
			long beginRunTime = 0;
			List<MoreReportYear> list=new ArrayList<MoreReportYear>();
			if(logger.isDebugEnabled()){
				beginRunTime = System.currentTimeMillis();
				logger.debug("enter findReoprtYear method.");			
			}
		    list=CommonUtils.getReportYear();
		    fillActionResult(list);
		    if(logger.isDebugEnabled()){
				long takeTime = System.currentTimeMillis() - beginRunTime;
				logger.debug("exit findReoprtYear method.takeTime:" + takeTime + "ms");
			}
		}

		public String getSearchAppyear() {
			return searchAppyear;
		}

		public void setSearchAppyear(String searchAppyear) {
			this.searchAppyear = searchAppyear;
		}
		
		
		
  	
}

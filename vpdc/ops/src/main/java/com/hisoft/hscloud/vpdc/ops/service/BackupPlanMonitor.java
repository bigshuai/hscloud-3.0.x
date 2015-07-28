package com.hisoft.hscloud.vpdc.ops.service;

import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.common.entity.LogOperatorType;
import com.hisoft.hscloud.common.vo.ProcessThread;
import com.hisoft.hscloud.vpdc.ops.dao.VpdcDao;
import com.hisoft.hscloud.vpdc.ops.entity.VmSnapShotPlan;
/**
 * 
* <定时备份监控类> 
* <功能详细描述> 
* 
* @author  dinghb 
* @version  [版本号, 2013-2-5] 
* @see  [相关类/方法] 
* @since  [产品/模块版本]
 */
@Service
 public class BackupPlanMonitor extends ProcessThread{
	 @Autowired
	 private VpdcDao vpdcDao;
	 @Autowired
	 private Operation operation;
	 private Logger logger = Logger.getLogger(this.getClass());
	 public void scanBackupPlans(){
		 try {
			 List<VmSnapShotPlan> vsspList = vpdcDao.findVmSnapShotPlan(new Date());
			 java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
			 int i = 0;
			 for(VmSnapShotPlan vssp:vsspList){
				 i++;
				 String backupName = sdf.format(java.util.Calendar.getInstance().getTime())+i;
				 String vmId = vssp.getVmId();
				 operation.backupsVm(vmId, backupName, "auto backup", 1,null,LogOperatorType.PROCESS.getName());
			 }
		} catch (Exception e) {
			logger.error("scanBackupPlans Exception:",e);
		}
	 }
}

/* 
* 文 件 名:  StatisMonthIncomingServiceImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-6-24 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.billing.service.impl; 

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hisoft.hscloud.bss.billing.dao.IncomingReportDao;
import com.hisoft.hscloud.bss.billing.dao.MonthIncomingDao;
import com.hisoft.hscloud.bss.billing.dao.ReportDao;
import com.hisoft.hscloud.bss.billing.dao.StatisLogDao;
import com.hisoft.hscloud.bss.billing.dao.TranscationLogDao;
import com.hisoft.hscloud.bss.billing.entity.Report;
import com.hisoft.hscloud.bss.billing.entity.StatisLog;
import com.hisoft.hscloud.bss.billing.service.StatisMonthIncomingService;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-6-24] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class StatisMonthIncomingServiceImpl implements
        StatisMonthIncomingService {
    
    private Logger logger = Logger.getLogger(this.getClass());
    
    @Autowired
    private MonthIncomingDao monthIncomingDao;
    
    @Autowired
    private StatisLogDao statisLogDao;
    
	@Autowired
	private TranscationLogDao transcationLogDao;
	
	@Autowired
	private IncomingReportDao incomingReportDao;
	
	@Autowired
	private ReportDao reportDao;
    
    @Override
    @Transactional(readOnly=false)
    public void statisMonthIncoming() {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(calendar.get(Calendar.YEAR), 
//                calendar.get(Calendar.MONTH), 
//                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        calendar.set(Calendar.DAY_OF_MONTH, 1);
//        Date expirationDate = calendar.getTime();
//        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
//        Date effectiveDate = calendar.getTime();
//        
//        String yearStr = Integer.toString(year);
//        String monthStr = Integer.toString(month);
//        monthStr = monthStr.length() > 1 ? monthStr : "0" + monthStr;
//        monthStr = yearStr + monthStr;
//        try {
//            List<StatisLog> list = statisLogDao.queryStatisLog(yearStr, monthStr);
//            if(list == null || list.isEmpty()) {
//                StatisLog statisLog = new StatisLog();
//                statisLog.setYear(yearStr);
//                statisLog.setMonth(monthStr);
//                statisLogDao.saveStatisLog(statisLog);
//                monthIncomingDao.statisMonthIncoming(yearStr, 
//                        monthStr, effectiveDate, expirationDate);
//            }
//        } catch(Exception ex) {
//            logger.info("statisMonthIncoming", ex);
//            ex.printStackTrace();
//        }
    	this.statistics();
    	this.report();
    }
    
    @Transactional(readOnly=false)
    private void statistics(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), 
                calendar.get(Calendar.MONTH), 
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date expirationDate = calendar.getTime();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        Date effectiveDate = calendar.getTime();
        
        String yearStr = Integer.toString(year);
        String monthStr = Integer.toString(month);
        monthStr = monthStr.length() > 1 ? monthStr : "0" + monthStr;
        monthStr = yearStr + monthStr;
        try {
            List<StatisLog> list = statisLogDao.queryStatisLog(yearStr, monthStr);
            if(list == null || list.isEmpty()) {
                StatisLog statisLog = new StatisLog();
                statisLog.setYear(yearStr);
                statisLog.setMonth(monthStr);
                statisLogDao.saveStatisLog(statisLog);
                monthIncomingDao.statisMonthIncoming(yearStr, 
                        monthStr, effectiveDate, expirationDate);
            }
        } catch(Exception ex) {
            logger.info("statisMonthIncoming", ex);
            ex.printStackTrace();
        }
    }
    
    @Transactional
    public void report(){
    	
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        calendar.add(Calendar.MONTH, -1);
        String preYearMonth = sdf.format(calendar.getTime());
        List<Report> reports = reportDao.report(preYearMonth);
     	reportDao.save(reports);
     	
    }
    
    
}

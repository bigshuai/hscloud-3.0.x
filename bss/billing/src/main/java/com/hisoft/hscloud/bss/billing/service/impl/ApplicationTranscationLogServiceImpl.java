package com.hisoft.hscloud.bss.billing.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.dao.ApplicationTranscationLogDao;
import com.hisoft.hscloud.bss.billing.dao.IncomingReportDao;
import com.hisoft.hscloud.bss.billing.dao.TranscationLogDao;
import com.hisoft.hscloud.bss.billing.entity.TranscationLog;
import com.hisoft.hscloud.bss.billing.service.ApplicationTranscationLogService;
import com.hisoft.hscloud.bss.billing.service.TranscationLogService;
import com.hisoft.hscloud.bss.billing.vo.ApplicationTranscationLogVO;
import com.hisoft.hscloud.bss.billing.vo.QueryVO;
import com.hisoft.hscloud.bss.billing.vo.TranscationLogVO;
import com.hisoft.hscloud.bss.billing.vo.VMResponsibility;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;


@Service
public class ApplicationTranscationLogServiceImpl implements ApplicationTranscationLogService {
	
	private	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private ApplicationTranscationLogDao applicationTranscationLogDao;

	@Override
	public Page<ApplicationTranscationLogVO> getAppTransactionByPage(
			List<Sort> sorts, Page<ApplicationTranscationLogVO> page,
			String query) {
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sb  = new StringBuffer();
        if(logger.isDebugEnabled()){
        	logger.info("enter getAppTransactionByPage method");
        	logger.info("sorts"+sorts);
        	logger.info("page"+page);
        	logger.info("query"+query);
        }
        sb.append("select id,username,useremail,dealDate,transaction_account");
        sb.append(",type,balance,app_name,supplier,remark from app_bill");
		return applicationTranscationLogDao.getAppTransactionByPage(sorts, page,sb.toString(),map);
	}
	
}

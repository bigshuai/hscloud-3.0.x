package com.hisoft.hscloud.bss.billing.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.dao.CapitalSourceDao;
import com.hisoft.hscloud.bss.billing.dao.ReportDao;
import com.hisoft.hscloud.bss.billing.service.ReportService;
import com.hisoft.hscloud.bss.billing.vo.CapitalSource;
import com.hisoft.hscloud.bss.billing.vo.OtherResponsibility;
import com.hisoft.hscloud.bss.billing.vo.QueryVO;
import com.hisoft.hscloud.bss.billing.vo.ResponsibilityIncoming;
import com.hisoft.hscloud.bss.billing.vo.Statistics;
import com.hisoft.hscloud.common.util.Sort;

@Service
public class ReportServiceImpl implements ReportService{
	
	@Autowired
	private  CapitalSourceDao capitalSourceDao;
	
	@Autowired
	private  ReportDao reportDao;
	

	@Override
	@Transactional(readOnly=true)
	public List<CapitalSource> findCapitalSource(Long domainId, String yearMonth) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT yearmonth yearMonth,oldPlatform,alipay,easyMoney,eBank,cheque,cash from hc_report report where domain_id=");
		sb.append(domainId);
		if(null != yearMonth && !"".equals(yearMonth)){
			sb.append(" and yearmonth >='");
			//String month = new StringBuffer(new StringBuffer(yearMonth).reverse().toString().replaceFirst("[0-9]{1}", "1")).reverse().toString();
			sb.append(yearMonth.substring(0, 4)+"01");
			sb.append("'");
			sb.append(" and yearmonth <='");
			sb.append(yearMonth);
			sb.append("'");
			
		}
		List<CapitalSource> capitalSources = capitalSourceDao.findBySQL(sb.toString());
		return capitalSources;
	}

	@Override
	@Transactional(readOnly=true)
	public List<ResponsibilityIncoming> findResponsibilityIncoming(
			Long domainId, String yearMonth) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("SELECT yearmonth yearMonth,consume,prepay,refund,draw drawCash,machine_responsibility machine,monthresponsibility_balance monthResponsibilityBalance,express,other_responsibility other from hc_report where domain_id=");
		sb.append(domainId);
		if(null != yearMonth && !"".equals(yearMonth)){
//			sb.append(" and yearmonth >='");
//			//String month = new StringBuffer(new StringBuffer(yearMonth).reverse().toString().replaceFirst("[0-9]{1}", "1")).reverse().toString();
//			sb.append(yearMonth.substring(0, 4)+"01");
//			sb.append("'");
			sb.append(" and yearmonth <='");
			sb.append(yearMonth);
			sb.append("'");
			
		}
		sb.append(" order by yearmonth");
		List<ResponsibilityIncoming> responsibilityIncomings = reportDao.findResponsibilityIncoming(sb.toString());
		return responsibilityIncomings;
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Statistics> findStatistics(Long domainId, String yearMonth){
		StringBuffer sb = new StringBuffer();
        sb.append("SELECT yearmonth yearMonth,deposit,draw,refund,responsibility,deposit_balance depositBalance,consume_balance consumeBalance ,consume from hc_report report where domain_id=");
        sb.append(domainId);
        if(null != yearMonth && !"".equals(yearMonth)){
        	//String month = new StringBuffer(new StringBuffer(yearMonth).reverse().toString().replaceFirst("[0-9]{1}", "1")).reverse().toString();
			sb.append(" and yearmonth >='");
			sb.append(yearMonth.substring(0, 4)+"01");
			sb.append("'");
			sb.append(" and yearmonth <='");
			sb.append(yearMonth);
			sb.append("'");
		}
        List<Statistics> Statistics = reportDao.findStatistics(sb.toString());
		return Statistics;
	}

	@Override
	public List<OtherResponsibility> findOtherResponsibility(QueryVO queryVO) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT his.email,his.currentIncoming,his.noneventIncoming, htl.amount, htl.transcation_on transcationOn, htl.description description, htl.transcation_type type, htl.id transcationId FROM ( SELECT email, current_incoming currentIncoming, nonevent_incoming noneventIncoming, incoming_log_id FROM hc_incoming_statis WHERE `month` ='").append(queryVO.getMonth()).append("' AND product_type != 1 AND domain_id =").append(queryVO.getDomainId()).append(") his LEFT JOIN hc_incoming_log hil ON hil.id = his.incoming_log_id LEFT JOIN hc_transcation_log htl ON htl.id = hil.transaction_id WHERE htl.accountId NOT IN ( SELECT account_id FROM hc_test_account WHERE `status` = 1 )");
		return reportDao.findOtherResponsibility(sb.toString());
	}

	@Override
	public Page<ResponsibilityIncoming> findResponsibilityByPage(
			List<Sort> sort, Page<ResponsibilityIncoming> page, QueryVO queryVO) {
		StringBuffer sb = new StringBuffer();
		DateFormat df = new SimpleDateFormat("yyyyMM");
		Map<String,Object> map = new HashMap<String,Object>();
		sb.append("SELECT d.id domainId,d.abbreviation,yearmonth yearMonth,consume,prepay,refund,draw drawCash,machine_responsibility machine,monthresponsibility_balance monthResponsibilityBalance,express,other_responsibility other from hc_report r LEFT JOIN hc_domain d on d.id=r.domain_id where 1=1 ");
		if(null != queryVO && null != queryVO.getStartTime() && !"".equals(queryVO.getEndTime())){
			sb.append(" and r.yearmonth>='");
			sb.append(df.format(queryVO.getStartTime()));
			sb.append("'");
		}
		if(null != queryVO && null != queryVO.getEndTime() && !"".equals(queryVO.getEndTime())){
			sb.append(" and r.yearmonth<='");
			sb.append(df.format(queryVO.getEndTime()));
			sb.append("'");
		}
		if(null != queryVO && null != queryVO.getDomainId() && !"".equals(queryVO.getDomainId())){
			sb.append(" and domain_id=").append(queryVO.getDomainId());
		}
		if(null != queryVO && null != queryVO.getDomainIds()){
			map.put("domainIds", queryVO.getDomainIds());
			sb.append(" and domain_id in (:domainIds)");
		}
		return reportDao.findPageBySQL(sort, page, sb.toString(), map);
	}
}

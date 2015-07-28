package com.hisoft.hscloud.bss.billing.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.dao.MonthIncomingVODao;
import com.hisoft.hscloud.bss.billing.service.MonthIncomingService;
import com.hisoft.hscloud.bss.billing.vo.MonthIncomingVO;
import com.hisoft.hscloud.bss.billing.vo.QueryVO;

@Service
public class MonthIncomingServiceImpl implements MonthIncomingService {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private MonthIncomingVODao monthIncomingVODao;
	
	@Override
	public Page<MonthIncomingVO> getMonthIncomingMonths(Page<MonthIncomingVO> page,QueryVO queryVO) {
		StringBuffer sb = new StringBuffer();
		DateFormat df = new SimpleDateFormat("yyyyMM");
		Map<String,Object> map = new HashMap<String,Object>();
		//sb.append("select id,month,domain_id domainId from hc_incoming_statis where 1=1");
		sb.append("SELECT  hr.id,hr.yearmonth month,hr.domain_id domainId,hd.abbreviation  from hc_report hr LEFT JOIN hc_domain hd on hr.domain_id=hd.id  where 1=1 ");
		if(null != queryVO && null != queryVO.getStartTime() && !"".equals(queryVO.getEndTime())){
			sb.append(" and hr.yearmonth>='");
			sb.append(df.format(queryVO.getStartTime()));
			sb.append("'");
		}
		if(null != queryVO && null != queryVO.getEndTime() && !"".equals(queryVO.getEndTime())){
			sb.append(" and hr.yearmonth<='");
			sb.append(df.format(queryVO.getEndTime()));
			sb.append("'");
		}
		if(null != queryVO && null != queryVO.getDomainIds()){
			map.put("domainIds", queryVO.getDomainIds());
			sb.append(" and domain_id in (:domainIds)");
		}
		String sql = sb.toString();
		Page<MonthIncomingVO> mis = monthIncomingVODao.findPageBySQL(page, sql,map);
		return mis;
	}


}

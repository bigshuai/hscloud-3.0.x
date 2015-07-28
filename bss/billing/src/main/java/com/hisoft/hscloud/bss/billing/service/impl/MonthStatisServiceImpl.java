package com.hisoft.hscloud.bss.billing.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.dao.MonthStatisVODao;
import com.hisoft.hscloud.bss.billing.service.MonthStatisService;
import com.hisoft.hscloud.bss.billing.vo.MonthStatisVO;
import com.hisoft.hscloud.bss.billing.vo.QueryVO;

@Service
public class MonthStatisServiceImpl implements MonthStatisService {

	@Autowired
	private MonthStatisVODao monthStatisVODao;
	@Override
	public Page<MonthStatisVO> getMonthStatisByPage(Page<MonthStatisVO> page,
			QueryVO queryVO) {
		StringBuffer sb = new StringBuffer();
		Map<String,Object> map = new HashMap<String,Object>();
		sb.append("SELECT hms.statis_day statisDay,hms.order_cnt orderCNT,hms.positive_cnt positiveCNT,hms.renew_cnt renewCNT,hms.refund_cnt refundCNT,hms.expired_cnt expiredCNT,hms.order_incoming orderIncoming,hms.renew_incoming renewIncoming,hms.refund_fee refundFee,hd.`name` domainName,hd.abbreviation abbName from hc_month_statis hms left join hc_domain hd on hms.domain_id=hd.id where 1=1 ");
		if(null != queryVO && null != queryVO.getDomainIds() && !queryVO.getDomainIds().isEmpty()){
			map.put("domainIds", queryVO.getDomainIds());
			sb.append(" and domain_id in (:domainIds)");
		}
		if(null != queryVO  && null != queryVO.getStartTime()){
			DateFormat df = new SimpleDateFormat("yyyyMM");
			String yearMonth = df.format(queryVO.getStartTime());
			sb.append(" and DATE_FORMAT(hms.statis_day,'%Y%m')='").append(yearMonth).append("'");
		}
		String sql = sb.toString();
		Page<MonthStatisVO> mss = monthStatisVODao.findPageBySQL(page, sql,map);
		return mss;
	}
	@Override
	public List<MonthStatisVO> getMonthStatis(QueryVO queryVO) {
		StringBuffer sb = new StringBuffer();
		Map<String,Object> map = new HashMap<String,Object>();
		sb.append("SELECT hms.statis_day statisDay,hms.order_cnt orderCNT,hms.positive_cnt positiveCNT,hms.renew_cnt renewCNT,hms.refund_cnt refundCNT,hms.expired_cnt expiredCNT,hms.order_incoming orderIncoming,hms.renew_incoming renewIncoming,hms.refund_fee refundFee,hd.`name` domainName,hd.abbreviation abbName from hc_month_statis hms  left join hc_domain hd on hms.domain_id=hd.id where 1=1 ");
		if(null != queryVO && null != queryVO.getDomainIds() && !queryVO.getDomainIds().isEmpty()){
			map.put("domainIds", queryVO.getDomainIds());
			sb.append(" and domain_id in (:domainIds)");
		}
		if(null != queryVO  && null != queryVO.getStartTime()){
			DateFormat df = new SimpleDateFormat("yyyyMM");
			String yearMonth = df.format(queryVO.getStartTime());
			sb.append(" and DATE_FORMAT(hms.statis_day,'%Y%m')='").append(yearMonth).append("'");
		}
		String sql = sb.toString();
		List<MonthStatisVO> mss = monthStatisVODao.findBySQL(sql, map);
		return mss;
	}

}

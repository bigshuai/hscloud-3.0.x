package com.hisoft.hscloud.bss.billing.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.dao.IPMessageVODao;
import com.hisoft.hscloud.bss.billing.dao.MonthStatisVODao;
import com.hisoft.hscloud.bss.billing.service.IPMessageService;
import com.hisoft.hscloud.bss.billing.service.MonthStatisService;
import com.hisoft.hscloud.bss.billing.vo.IpMessageVO;
import com.hisoft.hscloud.bss.billing.vo.MonthStatisVO;
import com.hisoft.hscloud.bss.billing.vo.QueryVO;

@Service
public class IPMessageServiceImpl implements IPMessageService {

	@Autowired
	private IPMessageVODao ipMessageVODao;
	
	@Override
	public List<IpMessageVO> getIPMessage(QueryVO queryVO) {
		StringBuffer sb = new StringBuffer();
		Map<String,Object> map = new HashMap<String,Object>();
		sb.append("SELECT up.`company` userName,u.`user_type` userProperties,up.`address` userAddress,u.`name` contactName,up.`telephone` contactPhone,up.`id_card` contactDocumentNum,u.email,z.`description` installedAddress,v.`create_date` installedDate,v.vm_outerIP startIp,v.vm_outerIP endIp");
		sb.append(" FROM hc_vpdc_reference v");
		sb.append(" LEFT JOIN hc_user u ON v.vm_owner=u.id");
		sb.append(" LEFT JOIN hc_user_profile up ON u.`userProfile_id`=up.`id`");
		sb.append(" LEFT JOIN hc_zone z ON z.`code`=v.`vm_zone`");
		sb.append(" WHERE v.status=0 AND v.vm_type=1 AND v.creater_type='user'");
		String sql = sb.toString();
		List<IpMessageVO> mss = ipMessageVODao.findBySQL(sql, map);
		return mss;
	}

}

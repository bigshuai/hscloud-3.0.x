package com.hisoft.hscloud.bss.billing.dao;

import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.entity.TranscationLog;
import com.hisoft.hscloud.bss.billing.vo.ApplicationTranscationLogVO;
import com.hisoft.hscloud.bss.billing.vo.CapitalSource;
import com.hisoft.hscloud.bss.billing.vo.OtherResponsibility;
import com.hisoft.hscloud.bss.billing.vo.ResponsibilityIncoming;
import com.hisoft.hscloud.bss.billing.vo.Statistics;
import com.hisoft.hscloud.bss.billing.vo.TranscationLogVO;
import com.hisoft.hscloud.bss.billing.vo.VMResponsibility;
import com.hisoft.hscloud.common.util.Sort;

public interface ApplicationTranscationLogDao {
	/**
	 * @param sorts 排序
	 * @param page 分页
	 * @param sql  sql语句
	 * @param map 参数
	 * @return
	 * 查询账单日志
	 */
	public Page<ApplicationTranscationLogVO> getAppTransactionByPage(List<Sort> sorts,Page<ApplicationTranscationLogVO> page,String sql,Map<String,Object> map);
	/**
	 * @param sql
	 * @param map
	 * @return
	 * 获取当前查询总数
	 */
	public Long findCountBySQL(String sql, Map<String,Object> map);
	/**
	 * @param sql
	 * @param map
	 * @return
	 * 获取账单信息
	 */
	public List<ApplicationTranscationLogVO> findBySQL(String sql,Map<String, ?> map);
}

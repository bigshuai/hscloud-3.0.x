package com.hisoft.hscloud.bss.billing.service;

import java.util.List;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.entity.TranscationLog;
import com.hisoft.hscloud.bss.billing.vo.QueryVO;
import com.hisoft.hscloud.bss.billing.vo.TranscationLogVO;
import com.hisoft.hscloud.bss.billing.vo.VMResponsibility;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;

public interface TranscationLogService {
	
	/**
	 * 查询日志
	 * @param sorts 排序条件
	 * @param page Page<TranscationLogVO>
	 * @param query 查询条件
	 * @param primKeys 权限
	 * @return Page<TranscationLogVO>
	 */
	public Page<TranscationLogVO> searchPermissionTranLog(List<Sort> sorts,Page<TranscationLogVO> page,String query,List<Object> primKeys);

	/**
	 * 
	 * @param sort 排序条件
	 * @param id   查询日志的用户id
	 * @param pageLog
	 * @param query  查询条件
	 * @param primKeys 权限
	 * @return Page<TranscationLogVO>
	 */
	public Page<TranscationLogVO> searchPermissionTranLog(List<Sort> sort,
			long id, Page<TranscationLogVO> pageLog, String query,
			List<Object> primKeys);

	/**
	 * <多条件查询> 
	* <功能详细描述> 
	* @param page
	* @param queryVO
	* @param sorts
	* @param primKeys
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<TranscationLogVO> findTransactionLog(
			Page<TranscationLogVO> page, QueryVO queryVO, List<Sort> sorts,
			List<Object> primKeys,List<Long> domains) throws HsCloudException;
	
	/**
	 * 查询日志
	 * @param queryVO 查询条件
	 * @param primKeys 权限
	 * @return
	 */
	public List<TranscationLogVO> findTransactionLog(List<Long> domains,QueryVO queryVO,List<Object> primKeys);
	
	public List<TranscationLogVO> findTransactionLog(QueryVO queryVO);

	public List<VMResponsibility> findVMResponsibility(QueryVO queryVO);
	
	public Page<VMResponsibility> findVMResponsibility(
			Page<VMResponsibility> page, QueryVO queryVO, List<Sort> sorts,
			List<Object> primKeys,List<Long> domains) throws HsCloudException;

	public List<TranscationLog> findTransactionLog(Long id);


}

package com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.sc.entity.ServerZone;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.entity.IPDetail;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.entity.IPRange;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo.IPDetailInfoVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo.IPDetailVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo.IPRangeVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo.IPStatistics;

public interface IPService {

	/**
	 * 
	* @title: createIP
	* @description 创建IP
	* @param startIP
	* @param endIP
	* @param createUid
	* @param remark
	* @return 设定文件
	* @return String    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	 * @throws Exception 
	* @update 2012-8-29 上午11:33:18
	 */
	public String createIP(IPRange ipRange) throws HsCloudException, Exception;
	/**
	 * 创建WanNetwork下属IP
	 * @param ipRange
	 * @return
	 * @throws HsCloudException
	 * @throws Exception
	 */
	public Long createWanNetworkIP(IPRange ipRange,long useStartIp,long useEndIp) throws HsCloudException, Exception;
	/**
	 * 
	* @title: deleteIP
	* @description 删除IP
	* @param id
	* @return 设定文件
	* @return boolean    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	* @update 2012-8-29 上午11:33:50
	 */
	public boolean deleteIP(long id) throws HsCloudException;
	/**
	 * 
	* @title: updateIPDetail
	* @description 修改IP状态（启用、禁用）
	* @param id
	* @param status
	* @param userId
	* @return 设定文件
	* @return boolean    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	* @update 2012-8-29 上午11:34:19
	 */
	public boolean updateIPDetail(long id,int status, long userId,String remark) throws HsCloudException;
	/**
	 * 
	* @title: updateIPDetail
	* @description IP被分配后修改状态、关联设备，如IP分配给了VM
	* @param id
	* @param status
	* @param userId
	* @param objectId
	* @param objectType
	* @return 设定文件
	* @return boolean    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	* @update 2012-8-29 上午11:34:25
	 */
	public boolean updateIPDetail(long id,int status,long userId,long objectId,int objectType) throws HsCloudException;	
	/**
	 * 
	* @title: findIPDetailByStatus
	* @description 查询某一状态的IP
	* @param ipRangeId
	* @param status
	* @return 设定文件
	* @return List<IPDetail>    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	* @update 2012-8-29 上午11:34:44
	 */
	public List<IPDetail> findIPDetailByStatus(long ipRangeId,int status) throws HsCloudException;
	/**
	 * 
	* @title: findIPDetailByIP
	* @description 查询某一段的IP
	* @param startIP
	* @param endIP
	* @return 设定文件
	* @return List<IPDetail>    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	* @update 2012-8-29 上午11:34:54
	 */
	public List<IPDetail> findIPDetailByIP(long startIP,long endIP) throws HsCloudException;	
	/**
	 * 
	* @title: findIPDetailsByCondition
	* @description 分页查询属于某一网段的IP
	* @param page
	* @param ipRangeId
	* @param field
	* @param fieldValue
	* @return 设定文件
	* @return Page<IPDetailVO>    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	* @update 2012-8-29 上午11:35:05
	 */
	public Page<IPDetailVO> findIPDetailsByCondition(Page<IPDetailVO> page, long ipRangeId,String field,
			String fieldValue) throws HsCloudException;	
	/**
	 * 
	* @title: findIPRangesByUId
	* @description 分页查询IP段
	* @param page
	* @param userId
	* @return 设定文件
	* @return Page<IPRangeVO>    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	* @update 2012-8-29 上午11:35:38
	 */
	public Page<IPRangeVO> findIPRanges(Page<IPRangeVO> page,String field,String fieldValue) throws HsCloudException;
	/**
	 * 查询所有的空闲IP 
	* <功能详细描述> 
	* @param status
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<IPDetailInfoVO> findIPDetailByStatus(int status) throws HsCloudException;
	/**
	 * <通过IP查询IP详细信息> 
	* <功能详细描述> 
	* @param ip
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public IPDetail getIPDetailByIP(long ip) throws HsCloudException;
	
	/**
	 * 通过userId取ip 
	* <功能详细描述> 
	* @param userId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<BigInteger> getIPListByUserId(long referenceId);
	/**
	 * <查询zone下的所有IP> 
	* <功能详细描述> 
	* @param zoneId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<IPRange> getAllIPsByServerZone(long zoneId) throws HsCloudException;
	/**
	 * <查询zone下的所有IP> 
	* <功能详细描述> 
	* @param zoneId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<IPDetailInfoVO> findAvailableIPDetailOfServerZone(ServerZone serverZone) throws HsCloudException;
	
	/**
	 * <分配ip> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public String assignIPDetail(long zoneId);
	/**
	 * <统计某资源域下ip的使用情况> 
	* <功能详细描述> 
	* @param zoneCode
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public IPStatistics getIPStatisticsByzoneCode(String zoneCode) throws HsCloudException;
	/**
	 * <统计所有ip的使用情况> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public IPStatistics getAllIPStatistics() throws HsCloudException;
    /**
     * <释放ip为空闲状态> 
    * <功能详细描述> 
    * @param sIP
    * @return 
    * @see [类、类#方法、类#成员]
     */
	public boolean releaseIp(String sIP);
	/**
	 * <通过IP查询IP详细信息> 
	* <功能详细描述> 
	* @param ip
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public IPDetail getIPDetailByIP(String ip) throws HsCloudException;
    /**
     * <查询ip段中ip状态> 
    * <功能详细描述> 
    * @param rangeId
    * @return 
    * @see [类、类#方法、类#成员]
     */
	public IPStatistics getIPStatisticsByRangeId(long rangeId);
	/**
	 *  <查询ip和uuid> 
	* <功能详细描述> 
	* @param zoneGroupId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
    public Map<String, String> getIpAndNetwork(long zoneGroupId);
}

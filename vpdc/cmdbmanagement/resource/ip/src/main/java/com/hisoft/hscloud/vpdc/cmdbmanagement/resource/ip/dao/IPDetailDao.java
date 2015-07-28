package com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.dao;

import java.math.BigInteger;
import java.util.List;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.sc.entity.ServerZone;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.entity.IPDetail;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo.IPDetailVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo.IPStatistics;

public interface IPDetailDao {

	/**
	 * 
	* @title: createIPDetail
	* @description 创建IP详细
	* @param iPDetail
	* @return 设定文件
	* @return long    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	* @update 2012-8-29 下午2:19:18
	 */
	public long createIPDetail(IPDetail iPDetail) throws HsCloudException;
	/**
	 * 
	* @title: getIPDetailById
	* @description 获取IP详细
	* @param id
	* @return 设定文件
	* @return IPDetail    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	* @update 2012-8-29 下午2:19:23
	 */
	public IPDetail getIPDetailById(long id) throws HsCloudException;
	/**
	 * 
	* @title: getIPDetailByIP
	* @description 通过IP查询IP详细
	* @param ip
	* @return 设定文件
	* @return IPDetail    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	* @update 2012-8-29 下午2:19:32
	 */
	public IPDetail getIPDetailByIP(long ip) throws HsCloudException;
	/**
	 * 
	* @title: updateIPDetail
	* @description 更新IP状态
	* @param id
	* @param status
	* @param userId
	* @return 设定文件
	* @return boolean    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	* @update 2012-8-29 下午2:19:45
	 */
	public boolean updateIPDetail(long id,int status,long userId,String remark) throws HsCloudException;
	/**
	 * 
	* @title: updateIPDetail
	* @description 更新IP详细
	* @param iPDetail
	* @return 设定文件
	* @return boolean    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	* @update 2012-8-29 下午2:21:10
	 */
	public boolean updateIPDetail(IPDetail iPDetail) throws HsCloudException;
	/**
	 * 
	* @title: findIPDetailByStatus
	* @description 查询某一状态的ip
	* @param ipRangeId
	* @param status
	* @return 设定文件
	* @return List<IPDetail>    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	* @update 2012-8-29 下午2:21:36
	 */
	public List<IPDetail> findIPDetailByStatus(long ipRangeId,int status) throws HsCloudException;	
	/**
	 * 
	* @title: findIPDetailByIP
	* @description 查询某一范围的ip
	* @param startIP
	* @param endIP
	* @return 设定文件
	* @return List<IPDetail>    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	* @update 2012-8-29 下午2:21:42
	 */
	public List<IPDetail> findIPDetailByIP(long startIP,long endIP) throws HsCloudException;
	/**
	 * 
	* @title: getIPStatisticsByRangeId
	* @description 统计某段ip的使用情况
	* @param ipRangeId
	* @return 设定文件
	* @return IPStatistics    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	* @update 2012-8-29 下午2:21:47
	 */
	public IPStatistics getIPStatisticsByRangeId(long ipRangeId) throws HsCloudException;
	/**
	 * 
	* @title: findIPDetail
	* @description 查询某种使用类型的ip列表
	* @param ipRangeId
	* @param objectType
	* @param offset
	* @param length
	* @return 设定文件
	* @return List<IPDetail>    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	* @update 2012-8-29 下午2:21:52
	 */
//	public List<IPDetail> findIPDetail(long ipRangeId,Object objectType,int offset,int length) throws HsCloudException;
	
	@SuppressWarnings("rawtypes")
    public Page<IPDetail> findIPDetail(long ipRangeId, Page page, Object objectType);
	/**
	 * 
	* @title: findIPDetail
	* @description 查询某种状态的ip列表
	* @param ipRangeId
	* @param field
	* @param fieldValue
	* @param offset
	* @param length
	* @return 设定文件
	* @return List<IPDetail>    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	* @update 2012-8-29 下午2:21:57
	 */
	public List<IPDetail> findIPDetail(long ipRangeId,String field, String fieldValue,int offset,int length) throws HsCloudException;
	/**
	 * 
	* @title: findIPDetail
	* @description 查询某种状态的ip列表
	* @param ipRangeId
	* @param field
	* @param fieldValue
	* @param offset
	* @param length
	* @return 设定文件
	* @return List<IPDetail>    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	* @update 2012-8-29 下午2:22:03
	 */
	public List<IPDetail> findIPDetail(long ipRangeId,String field, long fieldValue,int offset,int length) throws HsCloudException;
	/**
	 * 
	* @title: getIPStatisticsByHostId
	* @description 统计某节点ip的使用情况
	* @param hostId
	* @return 设定文件
	* @return IPStatistics    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	* @update 2012-9-3 上午10:44:09
	 */
	public IPStatistics getIPStatisticsByHostId() throws HsCloudException;

	public List<IPDetail> findIPDetailByRId(long ipRangeId);

	/**
	 * 查询某一状态的IP 
	* <功能详细描述> 
	* @param status
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<IPDetail> findIPDetailByStatus(int status) throws HsCloudException;
	/**
	 * <分页查询Ip> 
	* <功能详细描述> 
	* @param ipRangeId
	* @param field
	* @param fieldValue
	* @param offset
	* @param length
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<IPDetailVO> findIPDetailVO(long ipRangeId,String field, String fieldValue,int offset,int length) throws HsCloudException;
	
	/**
	 * 通过userId取ip 
	* <功能详细描述> 
	* @param userId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<BigInteger> getIPListByUserId(long referenceId);
	/**
	 * <查询某一域下的可用IP> 
	* <功能详细描述> 
	* @param zoneId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<IPDetail> findAvailableIPDetailOfServerZone(ServerZone serverZone) throws HsCloudException;
	/**
	 * <统计某资源域下ip的使用情况> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public IPStatistics getIPStatisticsByzoneCode(String zoneCode) throws HsCloudException;
	/**
	 * <sql获取可用空闲IP> 
	* <功能详细描述> 
	* @param serverZone
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public IPDetail getAvailableIPDetailOfServerZone(ServerZone serverZone) throws HsCloudException;
	/**
	 * <sql修改IP状态> 
	* <功能详细描述> 
	* @param id
	* @param status 
	* @see [类、类#方法、类#成员]
	 */
	public void updateIPStatus(long id,int status);
	/**
	* @title: getIPDetailByIP
	* @description 获取IP详细
	* @param id
	* @return 设定文件
	* @return IPDetail    返回类型
	* @author zjw
	 * @throws HsCloudException 
	* @update 2012-10-22 
	 */
	public IPDetail getIPDetailByIP(String ip) throws HsCloudException;
	
	/**
	 * 获取外网ip和uuid 
	* <功能详细描述> 
	* @param zoneGroupId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
    public List<Object[]> getIpAndNetwork(long zoneGroupId);
}

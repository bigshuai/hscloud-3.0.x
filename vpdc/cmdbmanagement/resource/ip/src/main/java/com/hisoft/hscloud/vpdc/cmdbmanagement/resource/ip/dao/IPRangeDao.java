package com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.dao;

import java.util.List;
import org.springside.modules.orm.Page;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.entity.IPRange;

public interface IPRangeDao {
	/**
	 * 
	* @title: createIPRange
	* @description 添加IP段
	* @param iPRange
	* @return 设定文件
	* @return long    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	* @update 2012-8-29 下午2:22:13
	 */
	public long createIPRange(IPRange iPRange);
	/**
	 * 
	* @title: getIPRangeById
	* @description 根据id获得IP段信息
	* @param id
	* @return 设定文件
	* @return IPRange    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	* @update 2012-8-29 下午2:22:20
	 */
	public IPRange getIPRangeById(long id) throws HsCloudException;
	/**
	 * 
	* @title: deleteIPRange
	* @description 删除IP段
	* @param id
	* @return 设定文件
	* @return boolean    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	* @update 2012-8-29 下午2:22:37
	 */
	public boolean deleteIPRange(long id) throws HsCloudException;
	/**
	 * 
	* @title: findIPRange
	* @description 分页查询IP段
	* @param userId
	* @param offset
	* @param length
	* @return 设定文件
	* @return List<IPRange>    返回类型
	* @throws
	* @version 1.3
	* @author ljg
	 * @throws HsCloudException 
	* @update 2012-8-29 下午2:22:31
	 */
//	public List<IPRange> findIPRange(long userId,int offset,int length) throws HsCloudException;
	
	/**
     * 
    * @title: findIPRange
    * @description 分页查询IP段
    * @param userId
    * @param offset
    * @param length
    * @return 设定文件
    * @return List<IPRange>    返回类型
    * @throws
    * @version 1.1
    * @author lihonglei
     * @throws HsCloudException 
    * @update 2012-9-20 下午2:22:31
     */
	@SuppressWarnings("rawtypes")
    public Page<IPRange> findIPRange(Page page, String field, String fieldValue);
	/**
	 * <查询zone下的所有IP> 
	* <功能详细描述> 
	* @param zoneId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public  List<IPRange> getAllIPsByServerZone(long zoneId) throws HsCloudException;
}

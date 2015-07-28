package com.hisoft.hscloud.crm.usermanager.service;

import java.util.List;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.Resource;
import com.hisoft.hscloud.crm.usermanager.vo.TreeVO;

public interface ResourceService {
	
	/**
	 * 查询resourceType类型的所有资源
	 * @param resourceType
	 * @return
	 */
	public List<Resource> getResource(String resourceType);

	/**
	 * 通过资源类型和资源类型id查询
	 * @param resourceType
	 * @param primKey
	 * @return
	 */
	public Resource getResource(String resourceType,String primKey);
	/**
	 * <保存resource> 
	* <功能详细描述> 
	* @param re
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public void saveResource(Resource re)throws HsCloudException;
	/**
	 * 查询资源类型为resourceType，id在primkeys中的resource.
	 * @param resourceType
	 * @param primkeys
	 * @return
	 */
	public List<Resource> getResource(String resourceType,List<Long> primkeys);
	/**
	 * 添加资源
	 * @param primKey
	 * @param resourceType
	 * @return
	 */
	public long addResource(String primKey,String resourceType);
	
//	public List<ResourceVO> getPermissionResource(String resourceType,List<Long> primkeys); 
//	public List<ResourceVO> getPermissionResource(List<Long> primkeys); 
	
	public List<TreeVO> getTreeVO(String resourceType,String query,String tableName,List<Long> primKeys,List<Long> pids);


    /** <一句话功能简述> 
    * <功能详细描述> 
    * @param roleId
    * @return 
    * @see [类、类#方法、类#成员] 
    */
    List<Resource> getResourceForRoleId(long roleId);
	
    public boolean checkPermission(Long adminId);
    
    public List<Resource> ownerResource(String resourceType,String email);
    
    public List<Object> getOwnerResourcePrimKey(String resourceType,String email);

    /**
     * lihonglei
     * @param resourceType
     * @param ownerIds
     * @return
     */
    public List<Resource> getResourceList(String resourceType, List<Long> ownerIds);
    
    /**
     * 
     * @param resourceType
     * @param ownerIds
     * @return
     */
    public List<Object> getVMResourceKeyList(String resourceType, List<Long> ownerIds);
}

package com.hisoft.hscloud.crm.usermanager.service; 

import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.crm.usermanager.entity.ResourceType;
import com.hisoft.hscloud.crm.usermanager.vo.AdminMenuVO;
import com.hisoft.hscloud.crm.usermanager.vo.CheckboxVO;
import com.hisoft.hscloud.crm.usermanager.vo.MenuVO;
import com.hisoft.hscloud.crm.usermanager.vo.PrivilegeVO;
import com.hisoft.hscloud.crm.usermanager.vo.TreeQueryVO;

public interface TreeService {
	
//	public List<TreeQueryVO> createTree(String table, String type);

//	public Map<String, List<TreeQueryVO>> createTreeForSuper(Map<String, ResourceType> resourceTypeMap);

//	public Map<String, List<TreeQueryVO>> createTreeByRole(long adminId, Map<String, ResourceType> resourceTypeMap);

//	public List<TreeQueryVO> createTreeByCondition(String table, String type, String linkTable, String condition, Map<String, Object> map);

//	public List<TreeQueryVO> queryRootResource();

	public List<AdminMenuVO> getMenuBySuper(String parentId, List<Object> referenceIds);

	public Long findCount(String table, String type, String query);

	public List<TreeQueryVO> findPageBySQL(String table, String type, int firstResult,
			int maxResult, String query);

	public List<TreeQueryVO> findUnassignedList(Map<String, Object> map,
			Page<PrivilegeVO> pagePrivilege);

	public List<Object> findAssignedList(Map<String, Object> map,
			Page<PrivilegeVO> pagePrivilege);
	
	public List<CheckboxVO> findPermissionByResourceIds(List<Object> list, Long roleId);

	public List<PrivilegeVO> findUiformDefList(String type, Long roleId, String tableAndCondition, String column);

	public List<MenuVO> findMenuStore(Long roleId);

	public List<CheckboxVO> findPermissionForUser(List<Object> list, Long userId);
}

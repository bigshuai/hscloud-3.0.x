package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.crm.usermanager.vo.AdminMenuVO;
import com.hisoft.hscloud.crm.usermanager.vo.CheckboxVO;
import com.hisoft.hscloud.crm.usermanager.vo.MenuVO;
import com.hisoft.hscloud.crm.usermanager.vo.PrivilegeVO;
import com.hisoft.hscloud.crm.usermanager.vo.TreeQueryVO;
import com.hisoft.hscloud.crm.usermanager.vo.TreeVO;
import com.hisoft.hscloud.crm.usermanager.vo.UniformDefQueryVO;

public interface TreeVODao {
	
	public List<TreeVO> findBySQL(String sql, Map<String, ?> map);
	
	public List<TreeQueryVO> findTreeByHibernateSQL(String sql, Map<String, Object> map);
	
	public List<AdminMenuVO> findMenuBySQL(String sql, Map<String, Object> map);

	public List<Long> findCount(String sql, Map<String, Object> map);

	public List<TreeQueryVO> findPageBySQL(String sql, Map<String, Object> map,
			int firstResult, int maxResult);

	public List<TreeQueryVO> findUnassignedList(Map<String, Object> map,
			Page<PrivilegeVO> pagePrivilege);

	public List<Object> findAssignedList(Map<String, Object> map, 
			Page<PrivilegeVO> pagePrivilege);

	public List<CheckboxVO> findPermissionByResourceIds(List<Object> list, Long roleId);

	public List<UniformDefQueryVO> findUiformDefList(String type, Long id, String tableAndCondition, String column);

	public List<MenuVO> findMenuStore(Long roleId);

	public List<CheckboxVO> findPermissionForUser(List<Object> list, Long userId);
}

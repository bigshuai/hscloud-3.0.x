package com.hisoft.hscloud.crm.usermanager.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.crm.usermanager.dao.TreeVODao;
import com.hisoft.hscloud.crm.usermanager.service.TreeService;
import com.hisoft.hscloud.crm.usermanager.util.Constant;
import com.hisoft.hscloud.crm.usermanager.vo.AdminMenuVO;
import com.hisoft.hscloud.crm.usermanager.vo.CheckboxVO;
import com.hisoft.hscloud.crm.usermanager.vo.MenuVO;
import com.hisoft.hscloud.crm.usermanager.vo.PrivilegeVO;
import com.hisoft.hscloud.crm.usermanager.vo.TreeQueryVO;
import com.hisoft.hscloud.crm.usermanager.vo.UniformDefQueryVO;

@Service
public class TreeServiceImpl implements TreeService {

	@Autowired
	private TreeVODao treeVODao;
	
	/**
	 * 获取菜单
	* @param parentId
	* @param referenceIds
	* @return
	 */
	@Override
	public List<AdminMenuVO> getMenuBySuper(String parentId, List<Object> referenceIds) {
		StringBuilder sb = new StringBuilder();
		sb.append("select  m.id, m.name, m.url, m.icon, m.parent_id as parentId from hc_menu m where  m.parent_id in (");
		sb.append(parentId);
		sb.append(") ");
		if(referenceIds != null && !referenceIds.isEmpty()) {
			sb.append(" and m.id in (:referenceIds) ");
		}
		sb.append(" order by m.position ");
		Map<String, Object> map = new HashMap<String, Object>();
		if(referenceIds != null && !referenceIds.isEmpty()) {
			map.put("referenceIds", referenceIds);
		}
		
		return treeVODao.findMenuBySQL(sb.toString(), map);
	}
	
	@Override
	public Long findCount(String table, String type, String query) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(x.id) count ");
		sql.append("from "+ table+ " as x left join hc_resource r on x.id = r.primKey and r.resourceType = :type left join ");
		sql.append("hc_permission p on r.id = p.resource_id ");
		if (Constant.RESOURCE_TYPE_VM.equals(type)) {
			sql.append(" where x.status = 0 ");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		if(StringUtils.isNotBlank(query)) {
			map.put("query", query);
		}
		List<Long> list = treeVODao.findCount(sql.toString(), map);
		if(list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public List<TreeQueryVO> findPageBySQL(String table, String type, int firstResult, int maxResult, String query) {
		StringBuilder sql = new StringBuilder();
		sql.append("select x.id as id, x.name as name, :type as type, ");
		sql.append("p.id as permissionId, r.id as resourceId, p.action_id as actionId ");
		sql.append("from "+ table+ " as x left join hc_resource r on x.id = r.primKey and r.resourceType = :type left join ");
		sql.append("hc_permission p on r.id = p.resource_id order by x.id");
		if (Constant.RESOURCE_TYPE_VM.equals(type)) {
			sql.append(" where x.status = 0 ");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		if(StringUtils.isNotBlank(query)) {
			map.put("query", query);
		}
		return treeVODao.findPageBySQL(sql.toString(), map, firstResult, maxResult);
	}
	
	/**
	 * 未分配虚拟机查询
	 */
	@Override
	public List<TreeQueryVO> findUnassignedList(Map<String, Object> map, Page<PrivilegeVO> pagePrivilege) {
		return treeVODao.findUnassignedList(map, pagePrivilege);
	}
	
	/**
	 * 已分配虚拟机查询
	 */
	@Override
	public List<Object> findAssignedList(Map<String, Object> map, Page<PrivilegeVO> pagePrivilege) {
		return treeVODao.findAssignedList(map, pagePrivilege);
	}

	@Override
	public List<CheckboxVO> findPermissionByResourceIds(List<Object> list, Long roleId) {
		return treeVODao.findPermissionByResourceIds(list, roleId);
	}
	
	@Override
	public List<CheckboxVO> findPermissionForUser(List<Object> list, Long userId) {
		return treeVODao.findPermissionForUser(list, userId);
	}

	@Override
	public List<PrivilegeVO> findUiformDefList(String type, Long id, String tableAndCondition, String column) {
		List<UniformDefQueryVO> list = treeVODao.findUiformDefList(type, id, tableAndCondition, column);
		List<PrivilegeVO> result = new ArrayList<PrivilegeVO>();
		PrivilegeVO privilegeVO = null;
		for(UniformDefQueryVO vo : list) {
			if(privilegeVO == null) {
				privilegeVO = new PrivilegeVO();
				privilegeVO.setResourceId(vo.getResourceId());
				privilegeVO.setName(Constant.RESOURCE_PARENT_NAME);
			}
			CheckboxVO checkboxVO = new CheckboxVO();
			checkboxVO.setActionId(vo.getActionId());
			checkboxVO.setPermissionId(vo.getPermissionId());
			checkboxVO.setResourceId(vo.getResourceId());
			if(vo.getPermissionId() != null) {
				checkboxVO.setChecked(vo.getPermissionId() == vo.getCheckId() 
						|| vo.getPermissionId().equals(vo.getCheckId()));
			}
			privilegeVO.getCheckboxVOList().add(checkboxVO);
		}
		result.add(privilegeVO);
		return result;
	}
	
	/**
	 * 菜单赋权查询
	 */
	@Override
	public List<MenuVO> findMenuStore(Long roleId) {
		return treeVODao.findMenuStore(roleId);
	}
}

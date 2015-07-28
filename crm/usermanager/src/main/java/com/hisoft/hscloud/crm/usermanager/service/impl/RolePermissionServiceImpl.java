package com.hisoft.hscloud.crm.usermanager.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.dao.RolePermissionDao;
import com.hisoft.hscloud.crm.usermanager.dao.RoleZoneDao;
import com.hisoft.hscloud.crm.usermanager.entity.RolePermission;
import com.hisoft.hscloud.crm.usermanager.service.RolePermissionService;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {
	
	@Autowired
	private RolePermissionDao rolePermissionDao;
	@Autowired
	private RoleZoneDao roleZoneDao;
	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void deleteRolePermissionByRoleId(long roleId) {
		String hql = "delete RolePermission rp where roleId=?";
		rolePermissionDao.delete(hql, roleId);
	}

    /** 
    * @param roleId
    * @param permissionId 
    */
    @Override
    public void addRolePermissiion(long roleId, long permissionId) {
        RolePermission rolePermission = new RolePermission();
        rolePermission.setPermissionId(permissionId);
        rolePermission.setRoleId(roleId);
        rolePermissionDao.save(rolePermission);
    }

	@Override
	public void batchDelete(String resourceValue, long roleId) {
		String[] strArray = resourceValue.split(",");
		Long[] idArray = new Long[strArray.length];
		for(int i = 0; i < idArray.length; i++) {
			idArray[i] = Long.valueOf(strArray[i]);
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("resource", idArray);
		condition.put("roleId", roleId);
		rolePermissionDao.batchDelete(condition);
	}

	@Override
	public void deleteRPForMenu(Long roleId, String noCheckStr) {
		String[] array = noCheckStr.split(",");
		List<String> list = new ArrayList<String>();
		for(String str : array){
			if(StringUtils.isNotBlank(str) && str.indexOf("-") == -1) {
				list.add(str);
			}
		}
		if(!list.isEmpty()) {
			rolePermissionDao.deleteRPForMenu(roleId, list);
		}
	}

	@Override
	@Transactional
	public boolean updateZoneOfRolePermission(long roleId, String oldZoneIds,
			String newZoneIds) throws HsCloudException {
		boolean result = false;
		String[] zoneArray = null;
		List<Object> objList = null;
		try{
			zoneArray = oldZoneIds.split(",");
			objList = new ArrayList<Object>();
			for(Object obj:zoneArray){
				objList.add(obj);
			}
			roleZoneDao.deleteZoneOfRole(roleId, objList);
			
			zoneArray = newZoneIds.split(",");
			objList = new ArrayList<Object>();
			for(Object obj:zoneArray){
				objList.add(obj);
			}
			roleZoneDao.addZoneOfRole(roleId, objList);
			result = true;
		}catch (Exception e) {
			logger.error("updateZoneOfRolePermission Exception:", e);
		}		
		return result;
	}
	@Override
	public boolean hasZoneOfRolePermission(long roleId, long zoneId) {
		return roleZoneDao.hasZoneOfRolePermission(roleId, zoneId);
	}

	@Override
	public List<Object> getZoneIdsByAdminId(long adminId) {
		return roleZoneDao.getZoneIdsByAdminId(adminId);
	}

	@Override
	public boolean deleteZoneOfRole(long zoneId) {
		return roleZoneDao.deleteZoneOfRole(zoneId);
	}

}

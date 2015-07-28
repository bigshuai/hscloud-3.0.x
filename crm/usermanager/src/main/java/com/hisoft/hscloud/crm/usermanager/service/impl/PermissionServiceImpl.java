package com.hisoft.hscloud.crm.usermanager.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hisoft.hscloud.crm.usermanager.constant.ResourceType;
import com.hisoft.hscloud.crm.usermanager.dao.ActionDao;
import com.hisoft.hscloud.crm.usermanager.dao.PermissionDao;
import com.hisoft.hscloud.crm.usermanager.dao.PermissionVODao;
import com.hisoft.hscloud.crm.usermanager.dao.ResourceDao;
import com.hisoft.hscloud.crm.usermanager.entity.Action;
import com.hisoft.hscloud.crm.usermanager.entity.Permission;
import com.hisoft.hscloud.crm.usermanager.entity.Resource;
import com.hisoft.hscloud.crm.usermanager.service.PermissionService;
import com.hisoft.hscloud.crm.usermanager.vo.PermissionVO;

@Service
public class PermissionServiceImpl implements PermissionService {
	
	private Logger logger = Logger.getLogger(PermissionServiceImpl.class);
	
	@Autowired
	private PermissionDao permissionDao;
	
	@Autowired
	private PermissionVODao permissionVODao;
	
	@Autowired
	private ResourceDao resourceDao;
	
	@Autowired
	private ActionDao actionDao;
	
	
	@Override
	@Transactional
	public List<Permission> getUPermission(long userId) {
		
        if(logger.isDebugEnabled()){
        	logger.debug("enter getUPermission method.");
        	logger.debug("userId:"+userId);
        }
		
        List<Permission> rolePermissions = this.getURolePermission(userId);
        List<Permission> userPermissions = this.getUserPermission(userId);
        List<Permission> userGroupPermissions = this.getUserGroupPermission(userId);
        
        if(logger.isDebugEnabled()){
        	logger.debug("rolePermissions:"+rolePermissions);
        	logger.debug("userPermissions:"+userPermissions);
        	logger.debug("userGroupPermissions:"+userGroupPermissions);
        }
        
        List<Permission> permissions = new ArrayList<Permission>();
		userGroupPermissions.removeAll(permissions);
		permissions.addAll(userGroupPermissions);
		userPermissions.removeAll(permissions);
		permissions.addAll(userPermissions);
		rolePermissions.removeAll(permissions);
		permissions.addAll(rolePermissions);
		
		if(logger.isDebugEnabled()){
			logger.debug("permissions:"+permissions);
			logger.debug("exit getUPermission method.");
		}
		
		return permissions;
		
		
	}

	@Override
	public List<Permission> getApermission(long adminId) {
		
        if(logger.isDebugEnabled()){
        	logger.debug("enter getApermission method.");
        	logger.debug("adminId:"+adminId);
        }
		
        List<Permission> rolePermissions = this.getARolePermission(adminId);
        
		if(logger.isDebugEnabled()){
			logger.debug("rolePermissions:"+rolePermissions);
			logger.debug("exit getApermission method.");
		}
	    return rolePermissions;
	    
	}
	
	@Override
	public List<Permission> getURolePermission(long userId) {
		
        if(logger.isDebugEnabled()){
        	logger.debug("enter getURolePermission method.");
        	logger.debug("userId:"+userId);
        }
		
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct hp.*  from hc_permission hp left join hc_role_permission hrp on hrp.permission_id=hp.id left join hc_role hr on hr.id = hrp.role_id left join hc_user_role hur on hur.roleId=hr.id where hur.userId=:userId");
		String queryString = sb.toString();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		List<Permission> permissions = permissionDao.findBySQL(queryString, map);
		if(logger.isDebugEnabled()){
			logger.debug("permissions:"+permissions);
			logger.debug("exit getURolePermission method.");
		}
		
		return permissions;
		
	}


	@Override
	public List<Permission> getARolePermission(long adminId) {
		
        if(logger.isDebugEnabled()){
        	logger.debug("enter getARolePermission method.");
        	logger.debug("adminId:"+adminId);
        }
        
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct hp.*  from hc_permission hp left join hc_role_permission hrp on hrp.permission_id=hp.id left join hc_role hr on hr.id = hrp.role_id left join hc_admin_role har on har.roleId=hr.id where har.adminId=:adminId");
		String queryString = sb.toString();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("adminId", adminId);
		List<Permission> permissions = permissionDao.findBySQL(queryString, map);
		
		if(logger.isDebugEnabled()){
			logger.debug("permissions:"+permissions);
			logger.debug("exit getARolePermission method.");
		}
		
		return permissions;
	}
	
	
	@Override
	public List<Permission> getUserPermission(long userId) {
		
        if(logger.isDebugEnabled()){
        	logger.debug("enter getUserPermission method.");
        	logger.debug("userId:"+userId);
        }
		
		StringBuffer sb = new StringBuffer();
		sb.append("select  distinct hp.* from hc_permission hp join hc_user_permission hup on hp.id=hup.permissionId where hup.userId=:userId");
		String queryString = sb.toString();
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		List<Permission> permissions = permissionDao.findBySQL(queryString, map);
		
		if(logger.isDebugEnabled()){
			logger.debug("permissions:"+permissions);
			logger.debug("exit getUserPermission method.");
		}
		
		return permissions;
		
	}
	
	@Override
	public List<Permission> getUserGroupPermission(long userId) {
		
        if(logger.isDebugEnabled()){
        	logger.debug("enter getUserGroupPermission method.");
        	logger.debug("userId:"+userId);
        }
		
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct hp.* from hc_permission hp left join  hc_usergroup_permission hugp  on hp.id=hugp.permission_id left join  hc_usergroup hug on hugp.usergroup_id=hug.id left join hc_user_usergroup huug on huug.userGroupId=hug.id LEFT JOIN hc_user hu on hug.create_id = hu.id where hug.flag=1 and hu.is_enable=3 and huug.userId=:userId");
		//sb.append("select distinct hp.* from hc_permission hp left join  hc_usergroup_permission hugp  on hp.id=hugp.permission_id left join  hc_usergroup hug on hugp.usergroup_id=hug.id left join hc_user_usergroup huug on huug.userGroupId=hug.id where huug.userId=:userId");
		String queryString = sb.toString();
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		List<Permission> permissions = permissionDao.findBySQL(queryString, map);
		
		if(logger.isDebugEnabled()){
			logger.debug("permissions:"+permissions);
			logger.debug("exit getUserGroupPermission method.");
		}
		
		return permissions;
		
	}
	
	@Override
	public List<Permission> getUPermission(long userId, long actionId, long resourceId) {
		
        if(logger.isDebugEnabled()){
        	logger.debug("enter getUPermission method.");
        	logger.debug("actionId:"+actionId);
        	logger.debug("resourceId:"+resourceId);
        }
		
		List<Permission> permission = permissionDao.find("from Permission where actionId=? and resoureId=?", actionId,resourceId);
		List<Permission> permissions = this.getUPermission(userId);
		
		if(logger.isDebugEnabled()){
			logger.debug("permission:"+permission);
			logger.debug("permissions:"+permissions);
		}
		
		permissions.retainAll(permission);
		
		if(logger.isDebugEnabled()){
			logger.debug("permission:"+permission);
			logger.debug("permissions:"+permissions);
			logger.debug("exit getUPermission method.");
		}
		
		return permissions;
		
	}
	
	@Override
	public List<Permission> getAPermission(long adminId, long actionId,long resourceId) {
		
		
        if(logger.isDebugEnabled()){
        	logger.debug("enter getAPermission method.");
        	logger.debug("adminId:"+adminId);
        	logger.debug("actionId:"+actionId);
        	logger.debug("resourceId:"+resourceId);
        }
		List<Permission> permission = permissionDao.find("from Permission where actionId=? and resoureId=?", actionId,resourceId);
		List<Permission> permissions = this.getApermission(adminId);
		
		if(logger.isDebugEnabled()){
			logger.debug("permission:"+permission);
			logger.debug("permissions:"+permissions);
		}
		
		
		permissions.retainAll(permission);
		
		if(logger.isDebugEnabled()){
			logger.debug("permission:"+permission);
			logger.debug("permissions:"+permissions);
			logger.debug("exit getAPermission method.");
		}
		
		return permissions;
	}
	
	@Override
	@Transactional
	public List<PermissionVO> getUPermissionVO(long userId,String resourceType) {
		
        if(logger.isDebugEnabled()){
        	logger.debug("enter getUPermissionVO method.");
        	logger.debug("userId:"+userId);
        }
		List<Permission> ps = this.getUPermission(userId);
		List<PermissionVO> pvos = new ArrayList<PermissionVO>();
		if(ps.isEmpty()){
			return pvos;
		}
		List<Long> ids = new ArrayList<Long>();
		for (Permission p : ps) {
			ids.add(p.getId());
		}
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct ha.id actionId,hp.id permissionId,hr.id resourceId,hr.primKey,hr.resourceType  from hc_permission hp  left join hc_resource hr on hp.resource_id = hr.id left join hc_action ha on ha.id=hp.action_id where hp.id in(:ids) and hr.resourceType='")
		.append(resourceType).append("'");
	    String sql = sb.toString();
	    map.put("ids", ids);
	    pvos = permissionVODao.findBySQL(sql, map);
        if(logger.isDebugEnabled()){
        	logger.debug("ids:"+ids);
        	logger.debug("sql:"+sql);
        	logger.debug("pvos:"+pvos);
        	logger.debug("exit getUPermissionVO method.");
        }
		return pvos;
		
	}

	@Override
	public List<PermissionVO> getAPermissionVO(long adminId,String resourceType) {
		
        if(logger.isDebugEnabled()){
        	logger.debug("enter getAPermissionVO method.");
        	logger.debug("adminId:"+adminId);
        }
		List<Permission> ps = this.getApermission(adminId);
		List<PermissionVO> pvos = new ArrayList<PermissionVO>();
		if(ps.isEmpty()){
			return pvos;
		}
		List<Long> ids = new ArrayList<Long>();
		for (Permission p : ps) {
			ids.add(p.getId());
		}
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct ha.id,hp.id,hr.id,hr.primKey,hr.resourceType  from hc_permission hp  left join hc_resource hr on hp.resource_id = hr.id left join hc_action ha on ha.id=hp.action_id where hp.id in(:ids) and hr.resourceType='")
		.append(resourceType).append("'");
	    String sql = sb.toString();
	    map.put("ids", ids);
		pvos = permissionVODao.findBySQL(sql, map);
        if(logger.isDebugEnabled()){
        	logger.debug("ids:"+ids);
        	logger.debug("sql:"+sql);
        	logger.debug("pvos:"+pvos);
        	logger.debug("exit getUPermissionVO method.");
        }
		return pvos;
	}

	@Override
	public boolean getAActionPermission(long adminId,String priKey, String resourceType,
			String classKey) {
		
        if(logger.isDebugEnabled()){
        	logger.debug("enter getAPermissionVO method.");
        	logger.debug("adminId:"+adminId);
        	logger.debug("priKey:"+priKey);
        	logger.debug("resourceType:"+resourceType);
        	logger.debug("classKey:"+classKey);
        }
		List<Permission> ps = this.getApermission(adminId);
		if(ps.isEmpty()){
			return false;
		}
		List<Long> ids = new ArrayList<Long>();
		for (Permission p : ps) {
			ids.add(p.getId());
		}
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		map.put("ids", ids);
		sb.append("select distinct hp.* from hc_permission hp left join hc_resource hr on hp.resource_id = hr.id left join hc_action ha on ha.id = hp.action_id where hr.resourceType = ha.actionType and  hp.id in (:ids) and (hr.primKey=0 or (ha.classKey='").append(classKey).append("' and hr.primKey =").append(priKey).append("))");
	    String sql = sb.toString();
	    List<Permission> permissions = permissionDao.findBySQL(sql, map);
        if(logger.isDebugEnabled()){
        	logger.debug("ids:"+ids);
        	logger.debug("sql:"+sql);
        	logger.debug("pvos:"+permissions);
        	logger.debug("exit getAActionPermission method.");
        }
		
		return permissions.isEmpty()?false:true;
	}

	@Override
	public boolean getUActionPermission(long userId,String priKey, String resourceType,
			String classKey) {

        if(logger.isDebugEnabled()){
        	logger.debug("enter getUActionPermission method.");
        	logger.debug("userId:"+userId);
        	logger.debug("priKey:"+priKey);
        	logger.debug("resourceType:"+resourceType);
        	logger.debug("classKey:"+classKey);
        }
		List<Permission> ps = this.getUPermission(userId);
		List<Long> ids = new ArrayList<Long>();
		for (Permission p : ps) {
			ids.add(p.getId());
		}
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
//		sb.append("select distinct hp.*  from hc_permission hp  left join hc_resource hr on hp.resource_id = hr.id left join hc_action ha on ha.id=hp.action_id where and hr.resourceType='")
//		.append(resourceType).append("'");;
//		if(!"0".equals(priKey)){
//			sb.append("' and ha.classKey='").append(classKey).append("'");
//		}
//		sb.append(" and hr.primKey=:priKey");
//	    String sql = sb.toString();
//	    map.put("ids", ids);
//	    map.put("priKey", priKey);
		
		
//		sb.append("select distinct hp.* from hc_permission hp left join hc_resource hr on hp.resource_id = hr.id left join hc_action ha on ha.id = hp.action_id where hr.resourceType = '").append(resourceType).append("' and  hp.id in (:ids) and (hr.primKey=0 or (ha.classKey='").append(classKey).append("' and hr.primKey =").append(priKey).append("))");
		
//		sb.append("select distinct hp.* from hc_permission hp left join  hc_resource hr on hp.resource_id = hr.id  join  hc_action ha on ha.id = hp.action_id where hr.resourceType = '").append(resourceType).append("' and ( hr.primKey=0 or (hr.primKey = ").append(priKey).append("and ((hr.ownerId=").append(userId).append(") or(ha.classKey=").append(classKey).append(" and hp.id))))");
		
		sb.append("select distinct hr.* from hc_resource hr left join hc_permission hp on hp.resource_id = hr.id left join  hc_action ha on ha.id = hp.action_id where hr.resourceType = '").append(resourceType).append("' and (hr.ownerId = ").append(userId).append(" or (ha.classKey = '").append(classKey).append("' and hp.id is not null and (hr.primKey = 0 ");
		if(!ids.isEmpty()){
			sb.append("or (hr.primKey = ").append(priKey).append(" and hp.id in(:ids))");
			map.put("ids", ids);
		}
		sb.append(")))");
	    String sql = sb.toString();
	    List<Resource> resources = resourceDao.findBySQL(sql, map);
//	    List<Permission> permissions = permissionDao.findBySQL(sql, map);
        if(logger.isDebugEnabled()){
        	logger.debug("ids:"+ids);
        	logger.debug("sql:"+sql);
        	logger.debug("resources:"+resources);
        	logger.debug("exit getUActionPermission method.");
        }
		
		return resources.isEmpty()?false:true;
		
	}
	
	@Override
	public List<Long> getAPrimKey(long adminId, String resourceType,
			String classKey) {

		if(logger.isDebugEnabled()){
        	logger.debug("enter getAPermissionVO method.");
        	logger.debug("adminId:"+adminId);
        	logger.debug("resourceType:"+resourceType);
        	logger.debug("classKey:"+classKey);
        }
		List<Permission> ps = this.getApermission(adminId);
		List<Long> pIds = new ArrayList<Long>();
		List<Long> ids = new ArrayList<Long>();
		if(ps.isEmpty()){
			return pIds;
		}
		for (Permission p : ps) {
			ids.add(p.getId());
		}
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct ha.id actionId,hp.id permissionId,hr.id resourceId,hr.primKey,hr.resourceType  from hc_permission hp  left join hc_resource hr on hp.resource_id = hr.id left join hc_action ha on ha.id=hp.action_id where hp.id in(:ids) and hr.resourceType= ha.actionType ")
		.append(" and ha.classKey='").append(classKey).append("'");
	    String sql = sb.toString();
	    map.put("ids", ids);
	    List<PermissionVO> pvos = permissionVODao.findBySQL(sql, map);
	    for (PermissionVO pvo : pvos) {
	    	pIds.add(Long.valueOf(pvo.getPrimKey()));
		}
        if(logger.isDebugEnabled()){
        	logger.debug("ids:"+ids);
        	logger.debug("sql:"+sql);
        	logger.debug("pIds:"+pIds);
        	logger.debug("exit getAActionPermission method.");
        }
		return pIds;
	}

	@Override
	public List<Long> getUPrimKey(long userId, String resourceType,
			String classKey) {
		
        if(logger.isDebugEnabled()){
        	logger.debug("enter getUActionPermission method.");
        	logger.debug("userId:"+userId);
        	logger.debug("resourceType:"+resourceType);
        	logger.debug("classKey:"+classKey);
        }
		List<Permission> ps = this.getUPermission(userId);
		Set<Long> pIds = new HashSet<Long>();
		List<Long> ids = new ArrayList<Long>();
		for (Permission p : ps) {
			ids.add(p.getId());
		}
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct hr.* from hc_resource hr left join hc_permission hp on hp.resource_id = hr.id left join  hc_action ha on ha.id = hp.action_id where hr.resourceType = '").append(resourceType).append("' and hr.ownerId = ").append(userId);
		if(!ids.isEmpty()){
			sb.append(" UNION select distinct hr.* from hc_resource hr left join hc_permission hp on hp.resource_id = hr.id left join  hc_action ha on ha.id = hp.action_id where ha.classKey = '").append(classKey).append("' and hp.id is not null");
			sb.append(" and hp.id in(:ids)");
			map.put("ids", ids);
		}
	    String sql = sb.toString();
	    //List<PermissionVO> pvos = permissionVODao.findBySQL(sql, map);
	    List<Resource> resources = resourceDao.findBySQL(sql, map);
	    for (Resource resource: resources) {
	    	pIds.add(Long.valueOf(resource.getPrimKey()));
		}
        if(logger.isDebugEnabled()){
        	logger.debug("ids:"+ids);
        	logger.debug("sql:"+sql);
        	logger.debug("pIds:"+pIds);
        	logger.debug("exit getUActionPermission method.");
        }
		return new ArrayList<Long>(pIds);
	}
	
	@Override
	public List<Long> getAPrimKey(long adminId, String resourceType) {

        if(logger.isDebugEnabled()){
        	logger.debug("enter getAPermissionVO method.");
        	logger.debug("adminId:"+adminId);
        	logger.debug("resourceType:"+resourceType);
        }
		List<Permission> ps = this.getApermission(adminId);
		List<Long> pIds = new ArrayList<Long>();
		List<Long> ids = new ArrayList<Long>();
		if(ps.isEmpty()){
			return pIds;
		}
		for (Permission p : ps) {
			ids.add(p.getId());
		}
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct ha.id actionId,hp.id permissionId,hr.id resourceId,hr.primKey,hr.resourceType  from hc_permission hp  left join hc_resource hr on hp.resource_id = hr.id left join hc_action ha on ha.id=hp.action_id where hp.id in(:ids) and hr.resourceType='")
		.append(resourceType).append("'");
	    String sql = sb.toString();
	    map.put("ids", ids);
	    List<PermissionVO> pvos = permissionVODao.findBySQL(sql, map);
	    for (PermissionVO pvo : pvos) {
	    	pIds.add(Long.valueOf(pvo.getPrimKey()));
		}
        if(logger.isDebugEnabled()){
        	logger.debug("ids:"+ids);
        	logger.debug("sql:"+sql);
        	logger.debug("pIds:"+pIds);
        	logger.debug("exit getAActionPermission method.");
        }
		return pIds;
	}

	@Override
	public List<Long> getUPrimKey(long userId, String resourceType) {
		
        if(logger.isDebugEnabled()){
        	logger.debug("enter getUActionPermission method.");
        	logger.debug("userId:"+userId);
        	logger.debug("resourceType:"+resourceType);
        }
		List<Permission> ps = this.getUPermission(userId);
		List<Long> pIds = new ArrayList<Long>();
		List<Long> ids = new ArrayList<Long>();
		if(ps.isEmpty()){
			return pIds;
		}
		for (Permission p : ps) {
			ids.add(p.getId());
		}
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct ha.id actionId,hp.id permissionId,hr.id resourceId,hr.primKey,hr.resourceType  from hc_permission hp  left join hc_resource hr on hp.resource_id = hr.id left join hc_action ha on ha.id=hp.action_id where hp.id in(:ids) and hr.resourceType='")
		.append(resourceType).append("'");
	    String sql = sb.toString();
	    map.put("ids", ids);
	    List<PermissionVO> pvos = permissionVODao.findBySQL(sql, map);
	    for (PermissionVO pvo : pvos) {
	    	pIds.add(Long.valueOf(pvo.getPrimKey()));
		}
        if(logger.isDebugEnabled()){
        	logger.debug("ids:"+ids);
        	logger.debug("sql:"+sql);
        	logger.debug("pIds:"+pIds);
        	logger.debug("exit getUActionPermission method.");
        }
		return pIds;
		
	}
	
	@Override
	@Transactional
	public List<Permission> getPermissionInGroup(long groupId){
		
        if(logger.isDebugEnabled()){
        	logger.debug("enter getPermissionInGroup method.");
        	logger.debug("groupId:"+groupId);
        }
		StringBuffer sb = new StringBuffer();
		sb.append("select hp.* from hc_permission hp left join hc_usergroup_permission hup on hup.permission_id=hp.id left join hc_usergroup hug on hug.id = hup.usergroup_id  where hug.id =:groupId");
		String sql = sb.toString();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("groupId", groupId);
		List<Permission> permissions = permissionDao.findBySQL(sql, map);
        if(logger.isDebugEnabled()){
        	logger.debug("sql:"+sql);
        	logger.debug("permissions:"+permissions);
        	logger.debug("exit getPermissionInGroup method.");
        }
		return permissions;
		
	}
	
	@Override
	public Permission getPermission(long actionId, long resourceId) {
		String hql = "from Permission p where  p.actionId=? and p.resourceId=? ";
		Permission permission = permissionDao.findUnique(hql, actionId,resourceId);
		return permission;
	}
	
	@Override
	public long addPermission(long resourceId, long actionId) {
		
		Permission permission = new Permission();
		permission.setActionId(actionId);
		permission.setResourceId(resourceId);
		permissionDao.save(permission);
		logger.info("permission:"+permission);
		return permission.getId();
		
	}
	
	@Override
	public void savePermission(String resourceType,long ownerId,String primKey){
		
		Resource resource = new Resource();
		resource.setResourceType(resourceType);
		resource.setOwnerId(ownerId);
		resource.setPrimKey(primKey);
		resource.setName(ResourceType.getType(resourceType));
		resource.setCreateId(0l);
		resource.setUpdateId(0l);
		resourceDao.save(resource);
		
		String hql = "from Action a where a.actionType = ?";
		List<Action> actions = actionDao.find(hql, resourceType);
		for (Action action : actions) {
			Permission p = new Permission();
			p.setActionId(action.getId());
			p.setResourceId(resource.getId());
			permissionDao.save(p);
		}
		
    }
	
    /** lihonglei 2012-10-11
    * @return 
    */
   /* @Override
    public List<Permission> getAllPermission() {
        return permissionDao.find("from Permission");
    }*/

	@Override
	public void deletePermissions(List<Long> ids) {
		permissionDao.deleteByIds(ids);
		
	}

}

package com.hisoft.hscloud.crm.usermanager.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.dao.ResourceDao;
import com.hisoft.hscloud.crm.usermanager.dao.TreeVODao;
import com.hisoft.hscloud.crm.usermanager.dao.UserDao;
import com.hisoft.hscloud.crm.usermanager.entity.Resource;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.service.ResourceService;
import com.hisoft.hscloud.crm.usermanager.util.Constant;
import com.hisoft.hscloud.crm.usermanager.vo.TreeVO;

@Service
public class ResourceServiceImpl implements ResourceService {
	
	private Logger logger = Logger.getLogger(ResourceServiceImpl.class);
	
	@Autowired
	private ResourceDao resourceDao;
	
	
	@Autowired
	private TreeVODao treeVODao;
	
	@Autowired
	private UserDao userDao;


	@Override
	public List<Resource> getResource(String resourceType) {
		
		return resourceDao.findBy("resourceType", resourceType);
		
	}
	
	@Override
	public List<Resource> ownerResource(String resourceType,String email){
		
		StringBuffer sb = new StringBuffer();
		List<Resource> resource = new ArrayList<Resource>();
		List<Long> ownerIds = new ArrayList<Long>();
		if(email!=null && !"".equals(email)){
			
			sb.append("from User u where u.email like ");
			sb.append("'%" + email + "%'");
			String hql = sb.toString();
			List<User> users = userDao.find(hql);
			if(users.isEmpty()){
				return resource;
			}
			for (User user : users) {
				ownerIds.add(user.getId());
			}
		}else{
			ownerIds.add(0L);
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("ownerIds", ownerIds);
		resource = resourceDao.findByHQL("from Resource r where resourceType='"+resourceType+"' and primKey!=0 and r.ownerId in (:ownerIds)", map);
		return resource;
		
	}
	
	@Override
    public List<Resource> getResourceList(String resourceType,List<Long> ownerIds){
        List<Resource> resource = new ArrayList<Resource>();
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("ownerIds", ownerIds);
        resource = resourceDao.findByHQL("from Resource r where resourceType='"+resourceType+"' and primKey!=0 and r.ownerId in (:ownerIds)", map);
        return resource;
        
    }
    
	@Override
    public List<Object> getVMResourceKeyList(String resourceType, List<Long> ownerIds){
		 List<Object> VMIds = new ArrayList<Object>();
	        Map<String,Object> map = new HashMap<String, Object>();
	        map.put("ownerIds", ownerIds);
	        VMIds = resourceDao.findPrimKeyByHQL("select r.primKey from Resource r,VpdcReference vr where r.resourceType='"+resourceType+"' and r.primKey=vr.id and vr.status=0 and primKey!=0 and r.ownerId in (:ownerIds)", map);
	        return VMIds;
	}
	
	public List<Object> getOwnerResourcePrimKey(String resourceType,String email){
		StringBuffer sb = new StringBuffer();
		List<Object> resource = new ArrayList<Object>();
		List<Long> ownerIds = new ArrayList<Long>();
		if(email!=null && !"".equals(email)){
			
			sb.append("from User u where u.email like ");
			sb.append("'%" + email + "%'");
			String hql = sb.toString();
			List<User> users = userDao.find(hql);
			if(users.isEmpty()){
				return resource;
			}
			for (User user : users) {
				ownerIds.add(user.getId());
			}
		}else{
			ownerIds.add(0L);
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("ownerIds", ownerIds);
		resource = resourceDao.findPrimKeyByHQL("select r.primKey from Resource r where resourceType='"+resourceType+"' and primKey!=0 and r.ownerId in (:ownerIds)", map);
		return resource;
	}
	@Override
	public Resource getResource(String resourceType, String primKey) {
		
		if(logger.isDebugEnabled()){
			   logger.debug("enter getResource method.");
			   logger.debug("resourceType:"+resourceType);
			   logger.debug("primKey:"+primKey);
		}
		
		
		String hql = "from Resource r where resourceType=? and primKey=?";
		Resource resource = resourceDao.findUnique(hql, resourceType,primKey);
		
		if(logger.isDebugEnabled()){
		   logger.debug("resource:"+resource);
		   logger.debug("exit getResource.");
		}
		return resource;
		
	}
	
	public void saveResource(Resource re)throws HsCloudException{
		resourceDao.save(re);
	}

	@Override
	public List<Resource> getResource(String resourceType, List<Long> primkeys) {
		
		
		if(logger.isDebugEnabled()){
			   logger.debug("enter getResource method.");
			   logger.debug("resourceType:"+resourceType);
			   logger.debug("primkeys:"+primkeys);
		}
		
		Map<String,Object[]> map = new HashMap<String,Object[]>();
		map.put("ids", primkeys.toArray());
		StringBuffer sb = new StringBuffer();
		sb.append("from Resource r where r.resourceType='");
		sb.append(resourceType);
		sb.append("' and r.primKey in (:ids)");
		String hql = sb.toString();
		List<Resource> resources = resourceDao.findByHQL(hql, map);
		if(logger.isDebugEnabled()){
			   logger.debug("resources:"+resources);
			   logger.debug("exit getResource.");
		}
		return resources;
		
	}


	@Override
	@Transactional
	public long addResource(String primKey, String resourceType) {
		
		
		if(logger.isDebugEnabled()){
			   logger.debug("enter addResource method.");
			   logger.debug("primKey:"+primKey);
			   logger.debug("resourceType:"+resourceType);
		}
		
		Resource resource = new Resource();
		resource.setPrimKey(primKey);
		resource.setResourceType(resourceType);
		resourceDao.save(resource);
		
		if(logger.isDebugEnabled()){
			
			   logger.debug("resource.getId()"+resource.getId());
			   logger.debug("exit addResource method.");
			   
		}
		return resource.getId();
		
	}

	
	@Override
	public List<TreeVO> getTreeVO(String resourceType,String query,String tableName,List<Long> primKeys,List<Long> pids) {
		
		StringBuffer sb = new StringBuffer();
		List<TreeVO> trees = new ArrayList<TreeVO>();
		if(primKeys.isEmpty()){
			return trees;
		}
        sb.append("select distinct hr.primKey primKey,hr.id resourceId, case temp.`level` when 0 then temp.resourceType else temp.resourceName  end resourceName,case temp.`level` when 0 then 0 else temp.id end id,hp.id permissionId,temp.actionId actionId,temp.acitonName acitonName,temp.actionType resourceType,temp.`level` from (select ha.id actionId,ha.`name` acitonName,ha.actionType,ha.`level`, t.`name` resourceName,t.id,case ha.`level` when 0 then '0' else t.id end joinPrimKey, ha.actionType resourceType from (select id,name,\"")
        .append(resourceType).append("\" rType from ").append(tableName).append(" where id in(:ids) ").append(query).append(" union select distinct r.primKey id,r.resourceType,r.resourceType rType from hc_resource r where r.resourceType='").append(resourceType)
        .append("' and r.primKey='0') t left join hc_action ha on (ha.actionType=t.rType and (ha.front=").append(Constant.STATUS_ALL).append(" or ha.front=").append(Constant.STATUS_USER).append("))) temp left join hc_resource hr on  (case temp.joinPrimKey when 0 then temp.joinPrimKey = hr.primKey and hr.resourceType='").append(resourceType)
        .append("' else temp.id=hr.primKey and hr.resourceType='").append(resourceType).append("' end ) left join hc_permission hp on (hr.id=hp.resource_id and hp.action_id=temp.actionId) where temp.resourceType is not null and temp.resourceType='")
        .append(resourceType).append("' and ((temp.id !=0 and temp.`level`!='0') or(temp.id=0 and temp.`level`='0')) ");
        
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("ids", primKeys);
        if(!pids.isEmpty()){
       	 sb.append(" and hp.id in (:pids)");
         map.put("pids", pids);
       }
      

	    trees = treeVODao.findBySQL(sb.toString(), map);
		return trees;
		
	}
	
    @Override
    public List<Resource> getResourceForRoleId(long roleId) {
        return resourceDao.getResourceForRoleId(roleId);
    }

	@Override
	public boolean checkPermission(Long adminId) {
		String sql = "select r.* from hc_resource r, hc_permission p, hc_role_permission rp, hc_admin_role ar where r.id = p.resource_id and p.id = rp.permission_id and rp.role_id = ar.roleId and ar.adminId = :adminId and r.resourceType = :resourceType";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("adminId", adminId);
		paramMap.put("resourceType", Constant.RESOURCE_TYPE_RESOURCE);
		List<Resource> list = resourceDao.findBySQL(sql, paramMap);
		if(list != null && !list.isEmpty()) {
			return true;
		}
		return false;
	}


}

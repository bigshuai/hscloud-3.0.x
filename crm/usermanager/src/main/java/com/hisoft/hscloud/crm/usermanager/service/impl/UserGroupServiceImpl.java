package com.hisoft.hscloud.crm.usermanager.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.constant.UserGroupState;
import com.hisoft.hscloud.crm.usermanager.constant.UserType;
import com.hisoft.hscloud.crm.usermanager.dao.UserCompanyDao;
import com.hisoft.hscloud.crm.usermanager.dao.UserGroupCompanyDao;
import com.hisoft.hscloud.crm.usermanager.dao.UserGroupDao;
import com.hisoft.hscloud.crm.usermanager.dao.UserUserGroupDao;
import com.hisoft.hscloud.crm.usermanager.entity.Company;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.entity.UserCompany;
import com.hisoft.hscloud.crm.usermanager.entity.UserGroup;
import com.hisoft.hscloud.crm.usermanager.entity.UserGroupCompany;
import com.hisoft.hscloud.crm.usermanager.entity.UserUserGroup;
import com.hisoft.hscloud.crm.usermanager.service.CompanyService;
import com.hisoft.hscloud.crm.usermanager.service.UserGroupService;
import com.hisoft.hscloud.crm.usermanager.service.UserService;
import com.hisoft.hscloud.crm.usermanager.vo.UserGroupVO;

@Service
public class UserGroupServiceImpl implements UserGroupService {
	
	@Autowired
	private UserGroupDao userGroupDao;
	
	@Autowired
	private UserCompanyDao userCompanyDao;
	
	@Autowired
	private UserUserGroupDao userUserGroupDao;
	
	@Autowired
	private UserGroupCompanyDao userGroupCompanyDao;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private UserService userService;

	@Override
	@Transactional
	public long createUserGroup(UserGroup userGroup,long userId) {
		userGroup.setCreateId(userId);
		userGroupDao.save(userGroup);
		UserGroupCompany userGroupCompany = new UserGroupCompany();
		Company company = companyService.getCompanyByUserId(userId);
		userGroupCompany.setCompanyId(company.getId());
		userGroupCompany.setUserGroupId(userGroup.getId());
		userGroupCompanyDao.save(userGroupCompany);
//		UserUserGroup userUserGroup= new UserUserGroup();
//		userUserGroup.setUserGroupId(userGroup.getId());
//		userUserGroup.setUserId(userId);
//		userUserGroupDao.save(userUserGroup);
		return userGroup.getId();
	}

	@Override
	@Transactional
	public void modifyUserGroup(UserGroup userGroup) {
		userGroupDao.save(userGroup);
	}

	
	@Override
	public Page<UserGroupVO> searchPermissionGroup(List<Sort> sorts,String query,
			Page<UserGroup> page, long userId, List<Object> primKeys) {
		
		Page<UserGroupVO> pageUserGroupVO = this.setPage(page);
		
		if(null != primKeys && primKeys.isEmpty()){
			return pageUserGroupVO;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("from UserGroup u where u.flag !=2");
		sb.append(" and id in(:ids) ");
		
		if(null != query  && !"".equals(query)){
			sb.append(" and u.name like '%"+query+"%'");
		}
		
		for (int i = 0; i < sorts.size(); i++) {
			Sort sort = sorts.get(i);
			if (i == 0) {
				sb.append(" order by ");
			}
			sb.append(sort.getProperty() + " " + sort.getDirection());

			if (i < sorts.size() - 1) {
				sb.append(",");
			}
		}
		Map<String,List<Long>> map = new HashMap<String,List<Long>>();
		if(null == primKeys){
			
			List<Long> c_id = new ArrayList<Long>();
			//同一公司下的USERGROUOP
			UserCompany userCompany = userCompanyDao.findUniqueBy("userId", userId);
			List<UserGroupCompany> userGroupCompany = userGroupCompanyDao.findBy("companyId", userCompany.getCompanyId());
			if(userGroupCompany.isEmpty()){
				return pageUserGroupVO;
			}
			
			for (UserGroupCompany ugc : userGroupCompany) {
				c_id.add(ugc.getUserGroupId());
			}
			map.clear();
			map.put("ids", c_id);
			
		}else{
			if(primKeys.isEmpty()){
				return pageUserGroupVO;
			}
			List<Long> pKeys = new ArrayList<Long>();
			for(Object o :primKeys){
				pKeys.add((Long)o);
			}
			map.clear();
			map.put("ids", pKeys);
		}

		String hql = sb.toString();
		Page<UserGroup> pages = userGroupDao.findPage(page, hql, map);
		List<UserGroupVO> userGroupVOs = this.setUserGroupVO(pages.getResult());
		for (UserGroupVO userGroupVO : userGroupVOs) {
			List<User> users = userService.getUserGroupMembers(userGroupVO.getId(), UserType.PERSONAL_USER.getType());
			userGroupVO.setMembers(users.size());
		}
		pageUserGroupVO.setTotalCount(pages.getTotalCount());
		pageUserGroupVO.setResult(userGroupVOs);
		pageUserGroupVO.getTotalPages();
		pages.getTotalPages();
		return pageUserGroupVO;
		
		
	}


	
	@Override
	@Transactional
	public List<UserGroupVO> getPermissionUserGroup(long userId,List<Long> primKeys) {
		
		
		List<UserGroupVO> proUserGroupVOs = new LinkedList<UserGroupVO>();
		if(primKeys.isEmpty()){
			return proUserGroupVOs;
		}else{
            String hql = "from UserGroup ug where ug.flag=1 and ug.id in(:ids)";
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("ids", primKeys);
            List<UserGroup> userGroups = userGroupDao.findByHQL(hql, map);
	        for (UserGroup userGroup : userGroups) {
				UserGroupVO userGroupVO = new UserGroupVO();
				try {
					BeanUtils.copyProperties(userGroupVO, userGroup);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				proUserGroupVOs.add(userGroupVO);
			}
			return proUserGroupVOs;
		}
	}

	
	@Override
	public List<UserGroupVO> getUserGroupByCompanyId(long companyId) {
		
		List<UserGroupCompany> userGroupCompanys = userGroupCompanyDao.findBy("companyId", companyId);
		List<UserGroupVO> proUserGroupVOs = new ArrayList<UserGroupVO>();
		if(userGroupCompanys.isEmpty()){
			return proUserGroupVOs;
		}else{
			List<Long> ids = new ArrayList<Long>();
			for (UserGroupCompany userGroupCompany : userGroupCompanys) {
				ids.add(userGroupCompany.getUserGroupId());
			}
			String hql = "from UserGroup ug where ug.flag=1 and ug.id in(:ids)";
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("ids", ids);
			List<UserGroup> userGroups = userGroupDao.findByHQL(hql, map);
			proUserGroupVOs = this.setUserGroupVO(userGroups);
			return proUserGroupVOs;
		}
		
	}
	

	@Override
	public void deleteUserGroupById(long groupId) {
		UserGroup userGroup = userGroupDao.findUniqueBy("id", groupId);
		userGroup.setFlag(UserGroupState.DELETED.getIndex());
		userGroupDao.save(userGroup);
	}

	
	private Page<UserGroupVO> setPage(Page<UserGroup> page) {
		
		Page<UserGroupVO> propage = new Page<UserGroupVO>();
		propage.setPageNo(page.getPageNo());
		propage.setOrder(page.getOrder());
		propage.setOrderBy(page.getOrderBy());
		propage.setPageSize(page.getPageSize());
		return propage;
		
	}
	 
	private List<UserGroupVO> setUserGroupVO(List<UserGroup> userGroups){
		
		List<UserGroupVO> proUserGroupVOs = new LinkedList<UserGroupVO>();
		
		for (UserGroup userGroup : userGroups) {
			
			UserGroupVO userGroupVO = new UserGroupVO();
			try {
				BeanUtils.copyProperties(userGroupVO, userGroup);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			proUserGroupVOs.add(userGroupVO);
			
		}
		return proUserGroupVOs;
		
	}


	@Override
	public UserGroup getUserGroupById(long groupId) {
		
		return userGroupDao.findUniqueBy("id", groupId);
	}

	@Override
	public List<UserGroup> duplicateUserGroup(long companyId, String groupName) {
		 String sql = "select * from hc_usergroup ug left join hc_usergroup_company uc on ug.id=uc.usergroup_id where ug.`name`=(:groupName) and uc.company_id=(:companyId) and ug.flag=1";
		 Map<String,Object> map = new HashMap<String,Object>();
		 map.put("companyId", companyId);
		 map.put("groupName", groupName);
		 return userGroupDao.findBySQL(sql, map);
	}







	

}

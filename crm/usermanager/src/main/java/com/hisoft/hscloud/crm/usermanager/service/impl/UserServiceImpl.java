package com.hisoft.hscloud.crm.usermanager.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.crm.usermanager.constant.UserState;
import com.hisoft.hscloud.crm.usermanager.constant.UserType;
import com.hisoft.hscloud.crm.usermanager.dao.AdminDao;
import com.hisoft.hscloud.crm.usermanager.dao.LoginLogDao;
import com.hisoft.hscloud.crm.usermanager.dao.RoleDao;
import com.hisoft.hscloud.crm.usermanager.dao.UserCompanyDao;
import com.hisoft.hscloud.crm.usermanager.dao.UserDao;
import com.hisoft.hscloud.crm.usermanager.dao.UserGroupDao;
import com.hisoft.hscloud.crm.usermanager.dao.UserProfileDao;
import com.hisoft.hscloud.crm.usermanager.dao.UserRoleDao;
import com.hisoft.hscloud.crm.usermanager.dao.UserUserGroupDao;
import com.hisoft.hscloud.crm.usermanager.dao.UserVO1Dao;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.LoginLog;
import com.hisoft.hscloud.crm.usermanager.entity.Role;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.entity.UserCompany;
import com.hisoft.hscloud.crm.usermanager.entity.UserGroup;
import com.hisoft.hscloud.crm.usermanager.entity.UserProfile;
import com.hisoft.hscloud.crm.usermanager.entity.UserRole;
import com.hisoft.hscloud.crm.usermanager.entity.UserUserGroup;
import com.hisoft.hscloud.crm.usermanager.service.UserService;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.PasswordUtil;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.vo.CountOfUserVO;
import com.hisoft.hscloud.crm.usermanager.vo.UserGroupVO;
import com.hisoft.hscloud.crm.usermanager.vo.UserVO;
import com.hisoft.hscloud.crm.usermanager.vo.UserVO1;

@Service
public class UserServiceImpl implements UserService {
	
	private	Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private UserDao userDao;

	@Autowired
	private AdminDao adminDao;
	
	@Autowired
	private UserCompanyDao userCompanyDao;

	@Autowired
	private UserGroupDao userGroupDao;
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private UserUserGroupDao userUserGroupDao;

	@Autowired
	private UserProfileDao userProfileDao;
	
	@Autowired
	private LoginLogDao loginLogDao;
	
	@Autowired
	private UserVO1Dao userVO1Dao;
	 
	@Override
	@Transactional
	public long createUser(User user,String userType) {
		
		try {
			String enPassword = PasswordUtil.getEncyptedPasswd(user.getPassword());
			user.setPassword(enPassword);
		} catch (Exception e) {
			throw new HsCloudException(Constants.PASSWORD_ENCRYPTION_FAILED," Encryption failed.",logger);
		}
		
		if(UserType.ENTERPRISE_USER.getType().equals(userType)){
			userDao.save(user);
			Role role = roleDao.findUniqueBy("code", "Role_Enterprise");
			UserRole userRole = new UserRole();
			userRole.setRoleId(role.getId());
			userRole.setUserId(user.getId());
			userRoleDao.save(userRole);
		}else{
			userDao.save(user);
		}
		return user.getId();
		
	}

	@Override
	@Transactional
	public User modifyUser(User user) {
		
		try {
			String enPassword = PasswordUtil.getEncyptedPasswd(user.getPassword());
			user.setPassword(enPassword);
		} catch (Exception e) {
			throw new HsCloudException(Constants.PASSWORD_ENCRYPTION_FAILED," Encryption failed.",logger);
		}
		User oldUser = userDao.get(user.getId());
		oldUser.setEmail(null == user.getEmail() || "".equals(user.getEmail())?oldUser.getEmail():user.getEmail());
		oldUser.setName(null == user.getName() || "".equals(user.getName())?oldUser.getName():user.getName());
		oldUser.setPassword(null == user.getPassword() || "".equals(user.getPassword())?oldUser.getPassword():user.getPassword());
		oldUser.setUserType(null == user.getUserType() || "".equals(user.getUserType())?oldUser.getUserType():user.getUserType());
		oldUser.setEnable(null == user.getEnable()?oldUser.getEnable():user.getEnable());
		oldUser.setUpdateDate(new Date());
		userDao.save(oldUser);
		return oldUser;
	}
	
	@Override
	@Transactional
	public void modifyUserOnlineStatus(long userId,short onlineStatus) {
		User oldUser = userDao.get(userId);
		oldUser.setOnlineStatus(onlineStatus);
		userDao.save(oldUser);
		
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<User> searchUserByState(String userState, List<Sort> sorts,
			Page<User> page, List<Long> domainIds) {
		StringBuffer sb = new StringBuffer();
		sb.append("from User u where u.enable in (");
		sb.append(userState);
		sb.append(")");
		Map<String, Object> map = new HashMap<String, Object>();
		if(null != domainIds){
			sb.append(" and  u.domain.id in (:domainIds)");
			map.put("domainIds", domainIds);
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

		String hql = sb.toString();
		Page<User> pages = userDao.findPage(page, hql, map);
		List<User> users = this.setUser(pages.getResult());
		pages.getResult().clear();
		pages.getResult().addAll(users);

		return pages;
	}
	

	@Override
	@Transactional(readOnly = true)
	public Page<User> searchUserLikeName(String username, List<Sort> sorts,
			Page<User> page, List<Long> domainIds) {

		StringBuffer sb = new StringBuffer();
		sb.append("from User u where u.name like ");
		sb.append("'%" + username + "%'");
		Map<String, Object> map = new HashMap<String, Object>();
		if(null != domainIds){
			sb.append(" and  u.domain.id in (:domainIds)");
			map.put("domainIds", domainIds);
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

		String hql = sb.toString();
		Page<User> pages = userDao.findPage(page, hql, map);
		List<User> users = this.setUser(pages.getResult());
		pages.getResult().clear();
		pages.getResult().addAll(users);

		return pages;

	}

	@Override
	@Transactional(readOnly = true)
	public Page<User> searchUserLikeEmail(String email, List<Sort> sorts,
			Page<User> page, List<Long> domainIds) {

		StringBuffer sb = new StringBuffer();
		sb.append("from User u where u.email like ");
		sb.append("'%" + email + "%'");
		

		Map<String, Object> map = new HashMap<String, Object>();
		if(null != domainIds){
			sb.append(" and  u.domain.id in (:domainIds)");
			map.put("domainIds", domainIds);
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

		String hql = sb.toString();
		Page<User> pages = userDao.findPage(page, hql, map);

		List<User> users = this.setUser(pages.getResult());
		pages.getResult().clear();
		pages.getResult().addAll(users);

		return pages;

	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<User> searchUserBySupplier(String supplier, List<Sort> sorts,
			Page<User> page, List<Long> domainIds) {
		StringBuffer sb = new StringBuffer();
		Map<String, Object> map = new HashMap<String, Object>();
		sb.append("from User as u where 1=1");
		if (!"".equals(supplier)) {
			sb.append(" and u.userProfile.supplier=");
			sb.append(supplier);
			// map.put("supplier", 1);
		}

		if (null != domainIds) {
			sb.append(" and u.domain.id in (:domainIds)");
			map.put("domainIds", domainIds);
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

		String hql = sb.toString();
		Page<User> pages = userDao.findPage(page, hql, map);
		List<User> users = this.setUser(pages.getResult());
		pages.getResult().clear();
		pages.getResult().addAll(users);

		return pages;
	}
	
	public Page<User> searchUserLikeDomain(String domainName, List<Sort> sorts,
			Page<User> page, List<Long> domainIds){
//		if(null != domainIds && !domainIds.isEmpty()){
//			return page;
//		}
//		List<Domain> domains = domainDao.findDomainByName(domainName);
//		if(domains.isEmpty()){
//			return page;
//		}
//		List<Long> tempIds = new ArrayList<Long>();
//		for (Domain domain : domains) {
//			tempIds.add(domain.getId());
//		}
//		tempIds.retainAll(domainIds);
//		
//		if(tempIds.isEmpty()){
//			return page;
//		}

		
		StringBuffer sb = new StringBuffer();
		sb.append("from User u where  u.domain.name like ");
		sb.append("'%" + domainName + "%'");
		if(null != domainIds){
			sb.append(" and u.domain.id in(:domainIds)");
			return page;
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

		Map<String, List<Long>> map = new HashMap<String, List<Long>>();
		map.put("domainIds", domainIds);
		String hql = sb.toString();
		Page<User> pages = userDao.findPage(page, hql, map);

		List<User> users = this.setUser(pages.getResult());
		pages.getResult().clear();
		pages.getResult().addAll(users);
		return pages;
	} 

	@Override
	@Transactional(readOnly = true)
	public Page<User> searchUser(List<Sort> sorts, Page<User> page, List<Long> domainIds) {

		StringBuffer sb = new StringBuffer();
		sb.append("from User u ");
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(null != domainIds){
			sb.append(" where  u.domain.id in (:domainIds)");
			map.put("domainIds", domainIds);
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
		String hql = sb.toString();
		Page<User> pages = userDao.findPage(page, hql, map);

		List<User> users = this.setUser(pages.getResult());
		pages.getResult().clear();
		pages.getResult().addAll(users);

		return pages;

	}

	
	@Override
	public Page<UserVO> searchPermissionUser(String query, List<Sort> sorts,
			Page<User> page, String userType, long userId, List<Object> primKeys) {
		
			Page<UserVO> pageUserVO = this.setPage(page);
//			if(null != primKeys && primKeys.isEmpty()){
//				return pageUserVO;
//			}
			List<Long> u_id = new ArrayList<Long>();
			UserCompany userCompany = userCompanyDao.findUniqueBy("userId", userId);
			if(null == primKeys){
				//同一公司用户
				
				List<UserCompany> userCompanys = userCompanyDao.findBy("companyId", userCompany.getCompanyId());
				for (UserCompany uc : userCompanys) {
					u_id.add(uc.getUserId());
				}
			}else{
				for(Object o :primKeys){
					u_id.add((Long)o);
				}
			}
			u_id.add(40l);
			StringBuffer sb = new StringBuffer();
			sb.append("from User where");
			sb.append(" enable=");
			sb.append(UserState.APPROVED.getIndex());
			sb.append(" and id in(:ids)");
			if(null != userType && !"".equals(userType)){
				sb.append(" and userType = '");
				sb.append(UserType.PERSONAL_USER.getType()+"'");
			}
			if(null != query && !"".equals(query) ){
				sb.append(" and (name like");
				sb.append("'%" + query + "%'");
				sb.append(" or  email like");
				sb.append("'%" + query + "%'");
				sb.append(")");
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

			String hql = sb.toString();
			Map<String, List<Long>> map = new HashMap<String, List<Long>>();
			map.put("ids", u_id);
			Page<User> pages = userDao.findPage(page, hql, map);
			List<UserVO> users = this.setUserVO(pages.getResult());
			pageUserVO.setResult(users);
			pageUserVO.setTotalCount(pages.getTotalCount());
			return pageUserVO;


	}
	
	@Override
	@Transactional(readOnly = true)
	public User getUserByEmail(String email){
		return userDao.findUniqueBy("email", email);
	}

	@Override
	@Transactional(readOnly = true)
	public UserVO getUserVO(long id) {
		UserVO userVO = new UserVO();
		User user = userDao.get(id);
		userVO.setId(user.getId());
		userVO.setName(user.getName());
		userVO.setEmail(user.getEmail());
		userVO.setLastLoginDate(user.getLastLoginDate());
		userVO.setCreateDate(user.getCreateDate());
		userVO.setUpdateDate(user.getUpdateDate());
		userVO.setUserProfile(user.getUserProfile());
		List<UserUserGroup> userUserGroups = userUserGroupDao.findBy("userId",
				user.getId());

		List<Long> ids = new ArrayList<Long>();
		for (UserUserGroup userUserGroup : userUserGroups) {
			ids.add(userUserGroup.getUserGroupId());
		}

		List<UserGroupVO> userGroupVOs = new LinkedList<UserGroupVO>();
		
		if (!ids.isEmpty()) {

			String sql = "select * from hc_usergroup ug where ug.flag=1 and id in(:ids)";
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ids", ids);
			List<UserGroup> userGroups = userGroupDao.findBySQL(sql, map);
			for (UserGroup userGroup : userGroups) {
				UserGroupVO userGroupVO = new UserGroupVO();
				try {
					BeanUtils.copyProperties(userGroupVO, userGroup);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				userGroupVOs.add(userGroupVO);
			}
			
		}
		userVO.setUserGroup(userGroupVOs);

		return userVO;
	}

	@Override
	@Transactional(readOnly = true)
	public User getUser(long id) {
		return userDao.get(id);
	}

	@Override
	@Transactional(readOnly = true)
	public User getUserAdmin(long subUserId) {
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public User findUserByEmail(String email) {

		return userDao.findUniqueBy("email", email);

	}

	@Override
	@Transactional
	public User loginUserByEmail(String email, String password) {
		String enPassword = "";
		try {
			enPassword = PasswordUtil.getEncyptedPasswd(password);
		} catch (Exception e) {
			return null;
		}
		User user = userDao.LoginByEmail(email, enPassword);
		if(user!=null){			
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("id", user.getId());
			map.put("userType",Constants.USER);
			LoginLog log = loginLogDao.findLastLoginLog(map);
			LoginLog loginLog = new LoginLog();
			loginLog.setUserId(user.getId());
			loginLog.setUserType(Constants.USER);
			loginLogDao.save(loginLog);
			if(null != log){
				user.setLastLoginDate(log.getLoginDate());
			}
			user.setOnlineStatus((short)1);
			user.setLastLoginDate(new Date());
			userDao.save(user);
			User u = setSessionUser(user);
			return u;
		}else{
			return null;
		}
	}
	
	public User loginUserByEmail(String email, String password, long domainId){
		
		String enPassword = "";
		try {
			enPassword = PasswordUtil.getEncyptedPasswd(password);
		} catch (Exception e) {
			return null;
		}
		
		User user = userDao.LoginByEmail(email, enPassword);
		if(user!=null){
            if(null == user.getDomain() || user.getDomain().getId()!= domainId){
            	throw new HsCloudException(Constants.PASSWORD_IS_DISABLE,"Account is disabled.",logger);
            }
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("id", user.getId());
			map.put("userType",Constants.USER);
			LoginLog log = loginLogDao.findLastLoginLog(map);
			LoginLog loginLog = new LoginLog();
			loginLog.setUserId(user.getId());
			loginLog.setUserType(Constants.USER);
			loginLogDao.save(loginLog);
			if(null != log){
				user.setLastLoginDate(log.getLoginDate());
			}
			user.setOnlineStatus((short)1);
			return setSessionUser(user);
		}else{
			return null;
		}
		
	}

	@Override
	public Admin loginAdimnByEmail(String email, String password) {
		String enPassword = "";
		try {
			enPassword = PasswordUtil.getEncyptedPasswd(password);
		} catch (Exception e) {
			return null;
		}
		Admin admin = adminDao.LoginByEmail(email, enPassword);
		if(admin!=null){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("id", admin.getId());
			map.put("userType",Constants.ADMIN);
			LoginLog log = loginLogDao.findLastLoginLog(map);
			LoginLog loginLog = new LoginLog();
			loginLog.setUserId(admin.getId());
			loginLog.setUserType(Constants.ADMIN);
			loginLogDao.save(loginLog);
			if(null != log){
				admin.setLastLoginDate(log.getLoginDate());
			}
			return setSessionAdmin(admin);
		}else{
			return null;
		}
	}

//	@Override
//	@Transactional(readOnly = true)
//	public long getCurrentUserAdminID(long userID) {
//		User user = userDao.get(userID);
//		return (user.getCreateId() == 0) ? user.getId() : user.getCreateId();
//	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserVO> pageUserInGroup(Page<User> page, List<Sort> sorts,
			String userType, long groupId,long userId) {
		
		Page<UserVO> pageUserVO = this.setPage(page);
		List<UserUserGroup> userUserGroups = userUserGroupDao.findBy(
				"userGroupId", groupId);
		if(userUserGroups.isEmpty()){
			return pageUserVO;
		}
		
		List<Long> uugs = new ArrayList<Long>();
		for (UserUserGroup userUserGroup : userUserGroups) {
			uugs.add(userUserGroup.getUserId());
		}
		
		UserCompany userCompany = userCompanyDao.findUniqueBy("userId", userId);
		List<UserCompany> userCompanys = userCompanyDao.findBy("companyId", userCompany.getCompanyId());
		if(userCompanys.isEmpty()){
			return pageUserVO;
		}

		List<Long> ucs = new ArrayList<Long>();
		for (UserCompany uc : userCompanys) {
			ucs.add(uc.getUserId());
		}

		List<Long> finalIds = new ArrayList<Long>();
		finalIds.addAll(uugs);
		finalIds.retainAll(ucs);
		
		if(finalIds.isEmpty()){
			return pageUserVO;
		}else{
			
			StringBuffer sb = new StringBuffer();
			sb.append("from User u where enable=").append(UserState.APPROVED.getIndex());
			sb.append(" and u.id in(:ids) ");
			if(null != userType && !"".equals(userType)){
				sb.append(" and u.userType='");
				sb.append(UserType.PERSONAL_USER.getType()+"'");
			}
			sb.append(" order by createDate ");
			String hql = sb.toString();
			List<Long> ids = new ArrayList<Long>();
		    ids.addAll(finalIds);
			Map<String, List<Long>> map = new HashMap<String, List<Long>>();
			map.put("ids", ids);
			Page<User> pages = userDao.findPage(page, hql, map);
			List<UserVO> users = this.setUserVO(pages.getResult());
			pageUserVO.setResult(users);
			pageUserVO.setTotalCount(pages.getTotalCount());
			return pageUserVO;
			
		}
		

	}
	

	@Override
	@Transactional
	public void resetPassword(User user) {
		user.setUpdateDate(new Date());
		try {
			String enPassword = PasswordUtil.getEncyptedPasswd(user.getPassword());
			user.setPassword(enPassword);
		} catch (Exception e) {
			throw new HsCloudException(Constants.PASSWORD_ENCRYPTION_FAILED," Encryption failed.",logger);
		}
		//this.modifyUser(user);

	}

	@Override
	public List<User> getUserByType(String userType) {

		String hql = "from User u where u.userType=?";
		List<User> users = userDao.find(hql, userType);
		return users;

	}

	@Override
	@Transactional(readOnly = true)
	public long getAllEntUser() {

		List<User> users = this.getUserByType(UserType.ENTERPRISE_USER
				.getType());
		return users.size();

	}

	@Override
	@Transactional(readOnly = true)
	public long getAllNorUser() {

		List<User> users = this.getUserByType(UserType.PERSONAL_USER.getType());
		return users.size();

	}

	private User setSessionUser(User user) {
		
			User sessionUser = new User();
			sessionUser.setLastLoginDate(user.getLastLoginDate());
			sessionUser.setId(user.getId());
			sessionUser.setEmail(user.getEmail());
			sessionUser.setName(user.getName());
			sessionUser.setEnable(user.getEnable());
			sessionUser.setUserType(user.getUserType());
			sessionUser.setLevel(user.getLevel());
			sessionUser.setDomain(user.getDomain());
			sessionUser.setCreateDate(user.getCreateDate());
			sessionUser.setSpecialFlag(user.getSpecialFlag());
			return sessionUser;

	}

	private Admin setSessionAdmin(Admin admin) {

		if (admin != null) {
			Admin sessionAdmin = new Admin();
			sessionAdmin.setId(admin.getId());
			sessionAdmin.setEmail(admin.getEmail());
			sessionAdmin.setName(admin.getName());
			sessionAdmin.setType(admin.getType());
			sessionAdmin.setEnable(admin.getEnable());
			sessionAdmin.setLastLoginDate(admin.getLastLoginDate());
			sessionAdmin.setIsSuper(admin.getIsSuper());
			sessionAdmin.setCreateDate(admin.getCreateDate());
			return sessionAdmin;
		} else {
			return null;
		}

	}

	@Override
	@Transactional
	public void deleteUser(long userId) {
		
		User user = userDao.get(userId);
		user.setEnable(UserState.DELETED.getIndex());
        this.updateUser(user);
		

	}
	
	@Override
	@Transactional
	public void freezedUser(long userId) {
		
		User user = userDao.get(userId);
		user.setEnable(UserState.FREEZE.getIndex());
        this.updateUser(user);
		
	}

	

	@Override
	@Transactional
	public void enableUser(long userId) {
		User user = userDao.get(userId);
		user.setEnable(UserState.APPROVED.getIndex());
        this.updateUser(user);
	}

	@Override
	@Transactional
	public void modifyUserProfile(UserProfile userProfile) {
		userProfileDao.modifyUserProfile(userProfile);
	}
	
	@Override
	@Transactional
	public List<User> getUserGroupMembers(long userGroupId,String userType){
		
		List<UserUserGroup> userUserGroups = userUserGroupDao.findBy("userGroupId", userGroupId);
		List<Long> ids = new ArrayList<Long>();
		List<User> users = new ArrayList<User>();
		if(userUserGroups.isEmpty()){
			return users;
		}
		for (UserUserGroup userUserGroup : userUserGroups) {
			ids.add(userUserGroup.getUserId());
		}
		StringBuffer sb = new StringBuffer();
		sb.append("from User u where enable=").append(UserState.APPROVED.getIndex()).append(" and id in(:ids)");
		if(null != userType && !"".equals(userType)){
			sb.append(" and userType='");
			sb.append(userType);
			sb.append("'");
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("ids", ids);
		String hql = sb.toString();
		users = userDao.findUser(hql, map);
		return users;
	}
	
	private List<User> setUser(List<User> users) {

		List<User> proUser = new ArrayList<User>();

		for (User user : users) {
			UserProfile userProfile = user.getUserProfile();
			if (null != userProfile) {
                if(userProfile.getRegion()!=null){
                	userProfile.getRegion().setCountry(null);
                }
                if(null != userProfile.getCountry()){
                	userProfile.getCountry().setRegions(null);
                }
			}
			proUser.add(user);
		}

		return proUser;

	}

	@Override
	@Transactional
	public UserProfile getUserProfileById(long id) {
		return userProfileDao.getUserProfileById(id);
	}
	
	private List<UserVO> setUserVO(List<User> users) {

		List<UserVO> proUser = new ArrayList<UserVO>();

		for (User user : users) {
			UserProfile userProfile = user.getUserProfile();
			if (null != userProfile) {
                if(null != userProfile.getRegion()){
                	userProfile.getRegion().setCountry(null);
                }
				
				userProfile.getCountry().setRegions(null);

			}
			UserVO userVO = new UserVO();
			userVO.setUserProfile(userProfile);
			userVO.setId(user.getId());
			userVO.setName(user.getName());
			userVO.setEmail(user.getEmail());
			userVO.setCreateDate(user.getCreateDate());
			userVO.setEnable(user.getEnable());

			List<UserUserGroup> userUserGroups = userUserGroupDao.findBy(
					"userId", user.getId());
			List<Long> ids = new ArrayList<Long>();
			for (UserUserGroup userUserGroup : userUserGroups) {
				ids.add(userUserGroup.getUserGroupId());
			}
			List<UserGroupVO> userGroupVOs = new LinkedList<UserGroupVO>();
			if (!ids.isEmpty()) {
				
				String sql = "select * from hc_usergroup ug where ug.flag=1 and id in(:ids)";
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("ids", ids);
				List<UserGroup> userGroups = userGroupDao.findBySQL(sql, map);
				for (UserGroup userGroup : userGroups) {
					UserGroupVO userGroupVO = new UserGroupVO();
					try {
						BeanUtils.copyProperties(userGroupVO, userGroup);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					userGroupVOs.add(userGroupVO);
				}
			}
			userVO.setUserGroup(userGroupVOs);
			proUser.add(userVO);

		}

		return proUser;

	}

	private Page<UserVO> setPage(Page<User> page) {
		Page<UserVO> propage = new Page<UserVO>();
		propage.setPageNo(page.getPageNo());
		propage.setOrder(page.getOrder());
		propage.setOrderBy(page.getOrderBy());
		propage.setPageSize(page.getPageSize());
		return propage;
	}
	
    public void updateUser(User user){
		userDao.save(user);
    }



	@Override
	public CountOfUserVO getCountOfUser() {
//		long countOfEntUserAll=getAllEntUser();
//		long countOfNorUserAll=getAllNorUser();
//		long countOfOnlineEntUser=getCountOfOnlineUser(UserType.ENTERPRISE_USER.getType());
//		long countOfOnlineNorUser=getCountOfOnlineUser(UserType.PERSONAL_USER.getType());
//		CountOfUserVO result=new CountOfUserVO(countOfEntUserAll,countOfNorUserAll,countOfOnlineEntUser,countOfOnlineNorUser);
		return userDao.getCountOfUser();
	}

	@Override
	public long getCountOfOnlineUser(String userType) {
		String hql = "from User u where u.onlineStatus=1 and u.userType=?";
		List<User> users = userDao.find(hql, userType);
		return users.size();
	}

	@Override
	public List<UserVO> getSameCompanySubUser(long userId) {
		
		UserCompany userCompany = userCompanyDao.findUniqueBy("userId", userId);
		List<UserCompany> userCompanys = userCompanyDao.findBy("companyId", userCompany.getCompanyId());
		List<User> users = new ArrayList<User>();
		List<Long> ids = new ArrayList<Long>();
		for (UserCompany uc: userCompanys) {
			ids.add(uc.getId());
		}
		users = userDao.findByIds(ids);
		List<UserVO> userVOs = this.setUserVOs(users);

		return userVOs;
	}
    
	private List<UserVO> setUserVOs(List<User> users){
		List<UserVO> userVOs = new ArrayList<UserVO>();
		for (User user : users) {
			UserVO userVO = new UserVO();
			userVO.setId(user.getId());
			userVO.setName(user.getName());
			userVO.setEmail(user.getEmail());
			userVO.setCreateDate(user.getCreateDate());
			userVO.setEnable(user.getEnable());
			userVOs.add(userVO);
		}
		return userVOs;
	}


	  @Override
	  public void modifyEnterpriseUser(User user) {
	        userDao.save(user);
	        
	  }

//	@Override
//	public long getEntUserId(long id) {
//		
//		User user = userDao.findUniqueBy("id", id);
//		if(user.getCreateId()>0){
//			return user.getCreateId();
//		}else{
//			return user.getId();
//		}
//		
//		
//	}

	@Override
	public List<UserVO> getAllAvailableUser(String email) {
		List<UserVO> userVOs = userDao.getAllAvailableUser(email);
		/*StringBuffer sb = new StringBuffer();
		sb.append("from User u where enable=:enable");		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("enable", Constant.USER_STATE_APPROVED);
		String hql = sb.toString();
		List<User> userList = userDao.findUser(hql, map);		
		UserVO userVO = null;
		for (User user : userList) {
			userVO = new UserVO();
			userVO.setId(user.getId());
			userVO.setName(user.getName());
			userVO.setEmail(user.getEmail());
			userVO.setCreateDate(user.getCreateDate());
			userVO.setEnable(user.getEnable());
			userVOs.add(userVO);
		}	*/	
		return userVOs;
	}

	@Override
	public User getPersonalUser(User user) {
		
		String hql = "from User u where u.userType='NorUser' and u.email=? and  u.domain.id =? and u.enable="+UserState.APPROVED.getIndex();
		User u = userDao.findUnique(hql, user.getEmail(),user.getDomain().getId());
		return u;
		
	}

	@Override
	public List<User> getUsersByDomainIds(List<Long> domainIds) {
		
		if(domainIds.isEmpty()){
			return new ArrayList<User>();
		}
		String hql = "from User u where u.domain.id in (:domainIds)";
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("domainIds", domainIds);
		List<User> users = userDao.findUser(hql, map);
		return users;
		
	}

	@Override
	public void saveUser(String sql, Map<String, Object> map) {
		userDao.saves(sql, map);
	}

	@Override
	public List<Long> getUserIdsByDomainIds(List<Long> domainIds) {
		return userDao.getUserIdsByDomainIds(domainIds);
	}

	
	@Override
	public UserVO1 findUserVO1(long id) {
		String sql = "select id, create_date createDate,name,email,is_enable enable, userProfile_id profileId ,last_login_date lastLoginDate,user_type userType,`level`,domain_id domainId from hc_user u where u.id=(:id)";
		Map<String,Object> params =new HashMap<String, Object>();
		params.put("id", id);
		List<UserVO1> userVO1s = userVO1Dao.findUserVO1(sql, params);
		return null == userVO1s || userVO1s.isEmpty()?null:userVO1s.get(0);
	}

	@Override
	public List<String> getAllUserMobile() {
		String hql = "select huf.telephone from hc_user hu,hc_user_profile huf where hu.userProfile_id=huf.id AND hu.is_enable=3";
		return userDao.getAllUserMobile(hql);
	}
	@Override
	public List<User> getAvailableSupplier(short supplier,short userStatus) {
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("userStatus", userStatus);
		param.put("supplier", supplier==0?false:true);
		String hql = "from User hu left join hu.userProfile huf where hu.enable=:userStatus and huf.supplier=:supplier";
		return userDao.findUser(hql, param);
	}



    

}

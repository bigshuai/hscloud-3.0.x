package com.hisoft.hscloud.crm.usermanager.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.PasswordUtil;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.constant.UserState;
import com.hisoft.hscloud.crm.usermanager.dao.AdminDao;
import com.hisoft.hscloud.crm.usermanager.dao.AdminRoleDao;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.AdminRole;
import com.hisoft.hscloud.crm.usermanager.service.AdminService;
import com.hisoft.hscloud.crm.usermanager.vo.AdminVO;

@Service
public class AdminServiceImpl implements AdminService {
	
	private Logger logger = Logger.getLogger(AdminServiceImpl.class);
	
	@Autowired
	private AdminDao adminDao;
	
	@Autowired
	private AdminRoleDao adminRoleDao;

	@Override
	@Transactional
	public Admin loginAdminByEmail(String email, String password) {
		
		if(logger.isDebugEnabled()){
			logger.debug("enter loginAdminByEmail method.");
			logger.debug("email:"+email);
			logger.debug("password:"+password);
		}
		Admin admin = adminDao.LoginByEmail(email, password);
		if(logger.isDebugEnabled()){
			logger.debug("admin:"+admin);
			logger.debug("exit loginAdminByEmail method.");
		}
		return admin;
		
	}

	@Override
	public Page<Admin> pageAdminInRole(Page<Admin> page,long roleId) {
		
		String adminRoleHQL = "from AdminRole ar where ar.roleId=?";
		List<AdminRole> adminRoles = adminRoleDao.find(adminRoleHQL, roleId);
		if(!adminRoles.isEmpty()) {
			List<Object> ids = new ArrayList<Object>();
			for (AdminRole adminRole: adminRoles) {
				ids.add(adminRole.getAdminId());
			}
			Map<String,List<Object>> map = new HashMap<String, List<Object>>();
			map.put("ids", ids);
			String adminHQL = "from Admin a where a.id in(:ids)";
			Page<Admin> admins = adminDao.findPage(page, adminHQL, map);
			return admins;
		}
		return new Page<Admin>(Constants.PAGE_NUM);
		
	}

	@Override
	@Transactional
	public void addAdmin(Admin admin, Long roleId) {
		
		try {
			String enPassword = PasswordUtil.getEncyptedPasswd(admin.getPassword());
			admin.setPassword(enPassword);
		} catch (Exception e) {
			throw new HsCloudException(Constants.PASSWORD_ENCRYPTION_FAILED," Encryption failed.",logger);
		}
		
		adminDao.save(admin);
		if (roleId != null) {
			AdminRole adminRole = new AdminRole();
			adminRole.setAdminId(admin.getId());
			adminRole.setRoleId(roleId);
			adminRoleDao.save(adminRole);
		}

	}

	@Override
	public void modifyAdmin(Admin admin) {
		try {
			String enPassword = PasswordUtil.getEncyptedPasswd(admin.getPassword());
			admin.setPassword(enPassword);
		} catch (Exception e) {
			throw new HsCloudException(Constants.PASSWORD_ENCRYPTION_FAILED," Encryption failed.",logger);
		}
		adminDao.save(admin);
	}

	@Override
	@Transactional(readOnly=true)
	public Page<AdminVO> getAllAdminUser(List<Sort> sorts,String query,int start,int limit,int page,String type) {
		List<AdminVO> result=adminDao.getAllAdminUser(sorts,query,start,limit,page,type);
		long totalCount=adminDao.getAdminCount(query,type);
		Page<AdminVO> paging=new Page<AdminVO>();
		paging.setPageNo(page);
		paging.setPageSize(limit);
		paging.setTotalCount(totalCount);
		paging.setResult(result);
		return paging;
				
	}

	@Override
	public void saveAdminRole(AdminRole amdinRole) {
		adminRoleDao.save(amdinRole);
	}

	@Override
	public Admin getAdminById(long adminId) {
		
		return adminDao.get(adminId);
	}

	@Override
	public AdminRole getAdminRoleById(long adminRoleId) {
		return adminRoleDao.findAdminRoleById(adminRoleId);
	}

	@Override
	public Admin getAdminByEmail(String email) {
		return adminDao.getAdminByEmail(email);
	}

	@Override
	public void deleteAdminRoleById(Long adminRoleId) {
		adminRoleDao.delete(adminRoleId);
		
	}

	@Override
	@Transactional
	public void enableAdmin(long adminId) {
		Admin admin=adminDao.get(adminId);
		
		admin.setEnable(UserState.APPROVED.getIndex());
		
		//this.modifyAdmin(admin);
		
	}
	
	
	

}

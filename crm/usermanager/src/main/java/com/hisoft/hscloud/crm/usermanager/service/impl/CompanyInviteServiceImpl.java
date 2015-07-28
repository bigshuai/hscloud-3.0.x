package com.hisoft.hscloud.crm.usermanager.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.constant.InviteState;
import com.hisoft.hscloud.crm.usermanager.dao.CompanyInviteDao;
import com.hisoft.hscloud.crm.usermanager.dao.UserCompanyDao;
import com.hisoft.hscloud.crm.usermanager.dao.UserGroupCompanyDao;
import com.hisoft.hscloud.crm.usermanager.dao.UserUserGroupDao;
import com.hisoft.hscloud.crm.usermanager.entity.CompanyInvite;
import com.hisoft.hscloud.crm.usermanager.entity.UserCompany;
import com.hisoft.hscloud.crm.usermanager.entity.UserGroupCompany;
import com.hisoft.hscloud.crm.usermanager.service.CompanyInviteService;

@Service
public class CompanyInviteServiceImpl implements CompanyInviteService {
	
	private	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
    private CompanyInviteDao companyInviteDao;
	
	@Autowired
	private UserCompanyDao userCompanyDao;
	
	@Autowired
	private UserGroupCompanyDao UserGroupCompanyDao;
	
	@Autowired
	private UserUserGroupDao userUserGroupDao;

	@Override
	public void invite(CompanyInvite companyInvite) {
		String hql = "from CompanyInvite ci where ci.receiverId=? and ci.companyId=? and (ci.status=1 or ci.status=2)";
		CompanyInvite ci = companyInviteDao.findUnique(hql, companyInvite.getReceiverId(),companyInvite.getCompanyId());
		if(null != ci){
			if(1==ci.getStatus()){
				throw new HsCloudException(Constants.INVITE_SENT,"Invite sended",logger);
			}
			if(2==ci.getStatus()){ 
				throw new HsCloudException(Constants.INVITE_JOINED,"Joined already.",logger);
			}
			
		}else{
			companyInviteDao.save(companyInvite);
		}
		
	}

	@Override
	public CompanyInvite getInviteById(long id) {
		
		return companyInviteDao.findUniqueBy("id", id);
		
	}

	@Override
	public void acceptedInvite(long id) {
		
		CompanyInvite companyInvite = companyInviteDao.findUniqueBy("id", id);
		companyInvite.setStatus(InviteState.INVITE_ACCEPTED.getIndex());
		UserCompany userCompany = new UserCompany();
		userCompany.setCompanyId(companyInvite.getCompanyId());
		userCompany.setUserId(companyInvite.getReceiverId());
		userCompanyDao.save(userCompany);
		
	}

	@Override
	public void terminateInvite(Long id) {
		
		CompanyInvite companyInvite = companyInviteDao.findUniqueBy("id", id);
		companyInvite.setStatus(InviteState.INVITE_TERMINATE.getIndex());
		String hql = "from UserCompany uc where uc.userId=? and uc.companyId=?";
		UserCompany userCompany  = userCompanyDao.findUnique(hql, companyInvite.getReceiverId(),companyInvite.getCompanyId());
		userCompanyDao.delete(userCompany.getId());
		List<UserGroupCompany> userGroupCompanys = UserGroupCompanyDao.findBy("companyId", companyInvite.getCompanyId());
		
		if(!userGroupCompanys.isEmpty()){
			
			List<Long> ids = new ArrayList<Long>();
			for (UserGroupCompany userGroupCompany : userGroupCompanys) {
				ids.add(userGroupCompany.getUserGroupId());
			}
			String dHql = "delete UserUserGroup uug where uug.userId=:userId and uug.userGroupId in (:ids)";
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ids", ids);
			map.put("userId", companyInvite.getReceiverId());
			userUserGroupDao.delete(dHql, map);
			
		}
		
	}

	@Override
	public List<CompanyInvite> getInvitesByReceiverId(long receiverId) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("from  CompanyInvite ci where receiverId=? and (ci.status=")
		.append(InviteState.INVITE_UNTREATED.getIndex())
		.append(" or ").append("ci.status=")
		.append(InviteState.INVITE_ACCEPTED.getIndex()).append(")");
		//String hql ="from  CompanyInvite ci where receiverId=? and (ci.status=1 or ci.status=2)";
		String hql = sb.toString();
		return companyInviteDao.find(hql, receiverId);
		
	}

	@Override
	public CompanyInvite getInvite(long senderId,long receiverId,short status) {
//		String hql ="from  CompanyInvite ci where receiverId=? and (ci.status=1 or ci.status=2)";
		
		StringBuilder sb = new StringBuilder();
		sb.append("from  CompanyInvite ci where senderId=? and receiverId= ? and ci.status=?");
		String hql = sb.toString();
		return companyInviteDao.findUnique(hql, senderId,receiverId,status);
	}

}

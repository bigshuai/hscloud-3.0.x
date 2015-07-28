package com.hisoft.hscloud.crm.usermanager.service;

import java.util.List;

import com.hisoft.hscloud.crm.usermanager.entity.CompanyInvite;

public interface CompanyInviteService {
	
	public void invite(CompanyInvite companyInvite);
	
	public CompanyInvite getInviteById(long id);
	
	/**
	 * 接受邀请，并加入该公司
	 * @param id
	 */
	public void acceptedInvite(long id);

	/**
	 * 终止（邀请）关系
	 * 并删除该公司给予用户的所有权限
	 * @param inviteId 邀请id
	 */
	public void terminateInvite(Long inviteId);
	
	/**
	 * 查询接受到的邀请
	 * @param receiverId
	 * @return
	 */
	public List<CompanyInvite> getInvitesByReceiverId(long receiverId);
	
	/**
	 * 状态 为 1，2 是能查询到唯一值。(3不能得到唯一值会报错)
	 * @param senderId
	 * @param receiverId
	 * @param status (1,2)
	 * @return
	 */
	public CompanyInvite getInvite(long senderId,long receiverId,short status);

}

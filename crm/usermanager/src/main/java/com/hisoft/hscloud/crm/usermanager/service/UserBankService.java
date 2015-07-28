/**
 * 
 */
package com.hisoft.hscloud.crm.usermanager.service;

import com.hisoft.hscloud.crm.usermanager.entity.UserBank;

/**
 * @author lihonglei
 *
 */
public interface UserBankService {

	public void saveUserBank(UserBank userBank);
	
	public UserBank findBankByUserId(Long userId);
}

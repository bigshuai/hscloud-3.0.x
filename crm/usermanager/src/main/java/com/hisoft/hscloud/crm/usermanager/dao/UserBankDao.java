/**
 * 
 */
package com.hisoft.hscloud.crm.usermanager.dao;

import com.hisoft.hscloud.crm.usermanager.entity.UserBank;

/**
 * @author lihonglei
 *
 */
public interface UserBankDao {
	public void saveUserBank(UserBank userBank);
	
	public UserBank findBankByUserId(Long userId);
}

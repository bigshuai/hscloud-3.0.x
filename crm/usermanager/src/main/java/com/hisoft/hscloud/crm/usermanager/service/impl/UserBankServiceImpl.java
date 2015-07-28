/**
 * 
 */
package com.hisoft.hscloud.crm.usermanager.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.crm.usermanager.dao.UserBankDao;
import com.hisoft.hscloud.crm.usermanager.entity.UserBank;
import com.hisoft.hscloud.crm.usermanager.service.UserBankService;

/**
 * @author lihonglei
 *
 */
@Service
public class UserBankServiceImpl implements UserBankService {
	
	private Logger logger = Logger.getLogger(UserBankServiceImpl.class);
	
	@Autowired
	private UserBankDao userBankDao;
	
	@Override
	public void saveUserBank(UserBank userBank) {
	    UserBank temp = userBankDao.findBankByUserId(userBank.getUserId());
	    if(temp == null) {
	        temp = new UserBank();
	    }
	    temp.setBank(userBank.getBank());
	    temp.setPayee(userBank.getPayee());
	    temp.setAccount(userBank.getAccount());
	    /**
	     *  bug: 0002444: ALL:前台提交完银行信息却界面显示为空。
	     *  modifier: liyunhui 2013-10-24
	     */
	    temp.setUserId(userBank.getUserId());
	    userBankDao.saveUserBank(temp);
	}
	@Override
	public UserBank findBankByUserId(Long userId) {
		return userBankDao.findBankByUserId(userId);
	}
}

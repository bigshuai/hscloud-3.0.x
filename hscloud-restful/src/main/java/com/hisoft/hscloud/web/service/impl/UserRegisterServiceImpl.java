/* 
* 文 件 名:  UserRegisterService.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-10-9 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.service.impl; 

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hisoft.hscloud.bss.billing.service.AccountService;
import com.hisoft.hscloud.bss.sla.om.entity.InvoiceAccount;
import com.hisoft.hscloud.bss.sla.om.service.InvoiceAccountService;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.crm.usermanager.constant.UserType;
import com.hisoft.hscloud.crm.usermanager.entity.Country;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.entity.Industry;
import com.hisoft.hscloud.crm.usermanager.entity.PlatformRelation;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.entity.UserProfile;
import com.hisoft.hscloud.crm.usermanager.service.DomainService;
import com.hisoft.hscloud.crm.usermanager.service.UserService;
import com.hisoft.hscloud.web.dao.BasicDao;
import com.hisoft.hscloud.web.service.UserRegisterService;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-10-9] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class UserRegisterServiceImpl implements UserRegisterService {
    
    @Resource
    private UserService userService;
    
    @Resource
    private DomainService domainService;
    
    @Resource
    private AccountService accountService;
    
    @Resource
    private InvoiceAccountService invoiceAccountService;
    @Resource
    private BasicDao basicDao;
    
    @Override
    @Transactional
    public String registerUser(String email, String name, String domainCode) {
        Domain domain = domainService.getDomainByCode(domainCode);
        if(domain == null) {
            return "Domain does not exist!";
        }
        User tempUser = userService.getUserByEmail(email);
        if(tempUser != null) {
            return "email is exist!";
        }
        
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setCreateDate(new Date());
        user.setCreateId(0l);
        user.setUpdateId(0l);
        int random = ((int)(Math.random() * 10000));
        user.setPassword(random + "");
        user.setDomain(domain);
        user.setUserType(UserType.PERSONAL_USER.getType());
        
        UserProfile userProfile = new UserProfile();
        Country country = new Country();
        country.setId(1l);
        userProfile.setCountry(country);
        Industry industry = new Industry();
        industry.setId(1l);
        userProfile.setIndustry(industry);
        user.setUserProfile(userProfile);
        
        userService.createUser(user, UserType.PERSONAL_USER.getType());
        
        accountService.createAccount(user);
        InvoiceAccount invoiceAccount = new InvoiceAccount();
        invoiceAccount.setUserId(user.getId());
        invoiceAccountService.createInvoiceAccount(invoiceAccount);
        
        return Constants.SUCCESS;
    }

	@Override
	@Transactional
	public String registerUserFromExternal(String externalUserId) {
		if(!"".equals(externalUserId)){
			PlatformRelation platformRelation = basicDao.getPlatformRelationByExteranalUserId(externalUserId);
			if(platformRelation == null){
				basicDao.addPlatformRelation(externalUserId);
			}			
		}		
		return Constants.SUCCESS;
	}

}

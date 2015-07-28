/* 
* 文 件 名:  AccessAccountServiceImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-10-16 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.crm.usermanager.service.impl; 

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.crm.usermanager.dao.AccessAccountDao;
import com.hisoft.hscloud.crm.usermanager.entity.AccessAccount;
import com.hisoft.hscloud.crm.usermanager.service.AccessAccountService;
import com.hisoft.hscloud.crm.usermanager.vo.AccessAccountVO;

/** 
 * <API接口用户服务> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-10-16] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class AccessAccountServiceImpl implements AccessAccountService {
    
 //   private Logger logger = Logger.getLogger(this.getClass());
    
    @Autowired
    private AccessAccountDao accessAccountDao;

    @Override
    public void saveAccessAccount(AccessAccount accessAccount) {
        accessAccountDao.saveAccessAccount(accessAccount);
    }

    @Override
    public Page<AccessAccountVO> findAccessAccountPage(Page<AccessAccountVO> accessAccountPage,
            String query) {
        Page<AccessAccount> page = new Page<AccessAccount>();
        page.setPageNo(accessAccountPage.getPageNo());
        page.setPageSize(accessAccountPage.getPageSize());
        page = accessAccountDao.findAccessAccountPage(page, query);
        List<AccessAccount> list = page.getResult();
        if(!list.isEmpty()) {
            accessAccountPage.setResult(new ArrayList<AccessAccountVO>());
            for(AccessAccount accessAccount : list) {
                AccessAccountVO vo = new AccessAccountVO();
                try {
                    BeanUtils.copyProperties(vo, accessAccount);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                accessAccountPage.getResult().add(vo);
            }
        }
        accessAccountPage.setTotalCount(page.getTotalCount());
        return accessAccountPage;
        
    }

    @Override
    public void deleteAccessAccount(long id) {
        accessAccountDao.deleteAccessAccount(id);
    }

    @Override
    public AccessAccount getAccessAccount(String accessId) {
        return accessAccountDao.getAccessAccount(accessId);
    }
}

/* 
* 文 件 名:  AccessAccountDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-10-16 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.crm.usermanager.dao.impl; 

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.crm.usermanager.dao.AccessAccountDao;
import com.hisoft.hscloud.crm.usermanager.entity.AccessAccount;

/** 
 * <API接口用户服务> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-10-16] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class AccessAccountDaoImpl extends HibernateDao<AccessAccount, Long> implements AccessAccountDao{

    @Override
    public Page<AccessAccount> findAccessAccountPage(Page<AccessAccount> accessAccountPage,
            String query) {
        String hql = "from AccessAccount ";
        
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(query)) {
            hql += " where accessId like :query ";
            paramMap.put("query", query);
        }

        hql += " order by id desc";
        return this.findPage(accessAccountPage, hql, paramMap);
    }

    @Override
    public void saveAccessAccount(AccessAccount accessAccount) {
        this.save(accessAccount);
    }

    @Override
    public void deleteAccessAccount(long id) {
        this.delete(id);
    }

    @Override
    public AccessAccount getAccessAccount(String accessId) {
        return this.findUniqueBy("accessId", accessId);
    }

}

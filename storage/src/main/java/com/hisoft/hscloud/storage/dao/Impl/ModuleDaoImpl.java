/* 
* 文 件 名:  ModuleDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-7-27 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.storage.dao.Impl; 

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.storage.dao.ModuleDao;
import com.hisoft.hscloud.storage.entity.Module;



/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-7-27] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class ModuleDaoImpl extends HibernateDao<Module, Long>  implements ModuleDao {

    @Override
    public List<Module> loadModule(String tenantId) {
        return this.findBy("tenantId", tenantId);
    }

}

/* 
* 文 件 名:  ModuleDao.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-7-27 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.storage.dao; 

import java.util.List;

import com.hisoft.hscloud.storage.entity.Module;

/** 
 * <模块接口> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-7-27] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface ModuleDao {
    /**
     * <加载模块> 
    * <功能详细描述> 
    * @param userName
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public List<Module> loadModule(String userName);
}

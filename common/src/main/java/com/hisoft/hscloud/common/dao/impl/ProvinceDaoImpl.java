/* 
* 文 件 名:  ProvinceDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-1-10 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.common.dao.impl; 

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.dao.ProvinceDao;
import com.hisoft.hscloud.common.entity.Province;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;


/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-1-10] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class ProvinceDaoImpl extends HibernateDao<Province, Long> implements ProvinceDao {
    private Logger logger = Logger.getLogger(this.getClass());
    
    @Override
    public List<Province> getProvinceList() {
        return this.find("from Province");
    }
    
    @Override
    public Province getProvinceByRegionCode(String regionCode) {
        List<Province> list = this.findBy("regionCode", regionCode);
        if(list.size() == 1) {
            return list.get(0);
        }
        throw new HsCloudException(Constants.ICP_GET_PROVINCE_EXCEPITON, "获取省份错误", logger);
    }
}

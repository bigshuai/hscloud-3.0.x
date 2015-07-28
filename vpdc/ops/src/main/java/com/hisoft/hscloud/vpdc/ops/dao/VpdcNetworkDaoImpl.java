/* 
* 文 件 名:  VpdcNetworkDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2014-2-10 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.dao; 

import java.util.HashMap;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcNetwork;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2014-2-10] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class VpdcNetworkDaoImpl extends HibernateDao<VpdcNetwork, Long> implements VpdcNetworkDao{
    
    @Override
    public Long saveVpdcNetwork(VpdcNetwork network) throws HsCloudException {
        this.save(network);
        return network.getId();
    }

    /** 
    * @param pageNetworkBean 
    */
    @Override
    public Page<VpdcNetwork> findPageNetwork(Page<VpdcNetwork> page) {
        String hql = "from VpdcNetwork where deleted = 0 and label='"+Constants.NETWORK_WAN+"' order by id desc";
        return this.findPage(page, hql, new HashMap<String, Object>());
    }

    @Override
    public VpdcNetwork getVpdcNetwork(long id) {
        return this.get(id);
    }
}

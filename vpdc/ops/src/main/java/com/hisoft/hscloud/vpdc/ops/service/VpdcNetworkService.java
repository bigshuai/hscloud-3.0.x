/* 
* 文 件 名:  VpdcNetworkService.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2014-2-10 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.service; 

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.vpdc.ops.json.bean.NetWorkBean;

/** 
 * <外网管理服务> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2014-2-10] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface VpdcNetworkService {

    /** <创建WanNetwork> 
    * <功能详细描述> 
    * @param netWork
    * @param adminId
    * @return
    * @throws HsCloudException 
    * @see [类、类#方法、类#成员] 
    */
    boolean createWanNetwork(NetWorkBean netWork, Long adminId)
            throws HsCloudException;

    /** <Network分页查询> 
    * <功能详细描述> 
    * @param pageNetworkBean 
     * @return 
    * @see [类、类#方法、类#成员] 
    */
    public Page<NetWorkBean> findPageNetwork(Page<NetWorkBean> pageNetworkBean);

    /** <删除Network> 
    * <功能详细描述> 
    * @param id 
    * @see [类、类#方法、类#成员] 
    */
    public void deleteNetwork(long id);

}

/* 
* 文 件 名:  VpdcNetworkServiceImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2014-2-10 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.service; 

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.openstack.api.compute.VpdcNetworkResource;
import org.openstack.model.hscloud.impl.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.OpenstackUtil;
import com.hisoft.hscloud.vpdc.ops.dao.VpdcNetworkDao;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcNetwork;
import com.hisoft.hscloud.vpdc.ops.json.bean.NetWorkBean;

/** 
 * <VpdcNetwork服务接口> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2014-2-10] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class VpdcNetworkServiceImpl implements VpdcNetworkService{
    @Autowired
    private VpdcNetworkDao vpdcNetworkDao;
    private OpenstackUtil openstackUtil = new OpenstackUtil();
    private Logger logger = Logger.getLogger(this.getClass());
    
    private final int DELETED_TRUE = 1;
    
    @Override
    public boolean createWanNetwork(NetWorkBean netWork,Long adminId) throws HsCloudException{
        boolean bl = false;
        //openstack 创建 WanNetwork
        Network nw = null;
        String cidr = netWork.getCidr();
        String dns1 = netWork.getDns1();
        String gateway = netWork.getGateway();
        try {
            VpdcNetworkResource vnr = openstackUtil.getCompute().vpdcNetworks();
            nw = vnr.createWanNetwork(Constants.NETWORK_WAN, cidr, dns1, gateway);
        } catch (Exception e) {
            e.printStackTrace();
            throw new HsCloudException(Constants.VM_OPENSTACK_API_ERROR, "createWanNetwork异常", logger, e);
        }
        //业务创建WanNetwork
        try {
            VpdcNetwork vnet = new VpdcNetwork();
            vnet.setLabel(Constants.NETWORK_WAN);
            vnet.setDns1(dns1);
            vnet.setDns2(nw.getDns2()==null?"":nw.getDns2());
            vnet.setBridge(nw.getBridge());
            //vnet.setBridge("br101");
            vnet.setBridgeInterface(nw.getBridgeInterface());
            //vnet.setBridgeInterface("eth1");
            vnet.setBroadcast(nw.getBroadcast());
            //vnet.setBroadcast("172.12.0.191");
            vnet.setCidr(cidr);
            vnet.setDhcptart(nw.getDhcpStart()==null?"":nw.getDhcpStart());
            vnet.setGateway(gateway);
            vnet.setNetworkId(nw.getId());
            //vnet.setNetworkId("bd24f800-9450");
            vnet.setInjected(nw.getInjected());
            //vnet.setInjected(true);
            vnet.setNetmask(nw.getNetmask());
            //vnet.setNetmask("255.255.255.192");
            vnet.setProjectId(nw.getProjectId()==null?"":nw.getProjectId());
            vnet.setVlanId(nw.getVlan()==null?0:nw.getVlan());
            vnet.setZoneGroup(netWork.getZoneGroup());
            vnet.setIpRangeId(netWork.getIpRangeId());
            vpdcNetworkDao.saveVpdcNetwork(vnet);
            bl = true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new HsCloudException(Constants.VPDC_WANNETWORK_ERROR, "createWanNetwork异常", logger, e);
        }
        return bl;
    }
    
    @Override
    public Page<NetWorkBean> findPageNetwork(Page<NetWorkBean> pageNetworkBean) {
        Page<VpdcNetwork> page = new Page<VpdcNetwork>();
        page.setPageNo(pageNetworkBean.getPageNo());
        page.setPageSize(pageNetworkBean.getPageSize());
        page = vpdcNetworkDao.findPageNetwork(page);
        
        pageNetworkBean.setResult(new ArrayList<NetWorkBean>());
        for(VpdcNetwork vpdcNetwork : page.getResult()) {
            NetWorkBean network = new NetWorkBean();
            network.setId(vpdcNetwork.getId());
            network.setCidr(vpdcNetwork.getCidr());
            network.setDns1(vpdcNetwork.getDns1());
            network.setGateway(vpdcNetwork.getGateway());
            network.setIpRangeId(vpdcNetwork.getIpRangeId());
            network.setZoneGroup(vpdcNetwork.getZoneGroup());
            network.setNetworkId(vpdcNetwork.getNetworkId());
            pageNetworkBean.getResult().add(network);
        }
        pageNetworkBean.setTotalCount(page.getTotalCount());
        return pageNetworkBean;
    }
    
    @Override
    public void deleteNetwork(long id) {
        VpdcNetwork vpdcNetwork = vpdcNetworkDao.getVpdcNetwork(id);
        vpdcNetwork.setDeleted(DELETED_TRUE);
        vpdcNetworkDao.saveVpdcNetwork(vpdcNetwork);
    }
}

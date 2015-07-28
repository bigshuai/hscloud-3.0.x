/* 
* 文 件 名:  RefundApplyServiceImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-6-8 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.service.impl; 

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hisoft.hscloud.bss.sla.om.entity.VmRefundLog;
import com.hisoft.hscloud.bss.sla.om.service.VmRefundLogService;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.service.UserService;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_Period;
import com.hisoft.hscloud.vpdc.ops.service.Operation;
import com.hisoft.hscloud.web.service.RefundApplyService;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-6-8] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class RefundApplyServiceImpl implements RefundApplyService {
    @Autowired
    private Operation operation;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private VmRefundLogService vmRefundLogService;
    
    @Override
    @Transactional
    public String refundApply(String userId, String orderNo, String uuid, String applyReason, Short refundReasonType) {
        User user = userService.getUser(Long.valueOf(userId));
        
       /* VpdcReference vr=operation.getReferenceByVmId(uuid);
        VpdcReference_Period vrp=operation.getReferencePeriod(vr.getId());
        VmRefundLog vrl=new VmRefundLog();
        vrl.setApplyDate(new Date());
        vrl.setApplyRefundReason(applyReason);
        vrl.setOpenDate(vrp.getStartTime());
        vrl.setExpirationDate(vrp.getEndTime());
        vrl.setOrderNo(orderNo);
        vrl.setRefundReasonType(refundReasonType);
        vrl.setOperator(user.getEmail());
        vrl.setOwnerEmail(user.getEmail());
        vrl.setReferenceId(vr.getId());
        vrl.setStatus((short)1);
        vrl.setUuid(uuid);
        vrl.setUpdateDate(new Date());
        vmRefundLogService.saveVmRefundLog(vrl);*/
        
        VmRefundLog vrl = vmRefundLogService.isExistedVMStatusEquals3Or4ByUuid(uuid);
        if(vrl != null) {
            submitRefundApplyOnceAgain(uuid, refundReasonType, applyReason);
        } else {
            // 根据虚拟机uuid判断云主机是否为申请中
            vrl=vmRefundLogService.isVMApplyingRefundByUuid(uuid);
            if(vrl==null){
                submitRefundApply(user, uuid, orderNo, applyReason, refundReasonType);
            } else {
                return "Application has been made";
            }
        }
        return "success";
    }
    
    private void submitRefundApply(User user, String uuid,
            String orderNo,String applyReason,Short refundReasonType)
            throws HsCloudException {
       VpdcReference vr=operation.getReferenceByVmId(uuid);
       VpdcReference_Period vrp=operation.getReferencePeriod(vr.getId());
       VmRefundLog vrl=new VmRefundLog();
       vrl.setApplyDate(new Date());
       vrl.setApplyRefundReason(applyReason);
       vrl.setOpenDate(vrp.getStartTime());
       vrl.setExpirationDate(vrp.getEndTime());
       vrl.setOrderNo(orderNo);
       vrl.setRefundReasonType(refundReasonType);
       vrl.setOperator(user.getEmail());
       vrl.setOwnerEmail(user.getEmail());
       vrl.setReferenceId(vr.getId());
       vrl.setStatus((short)1);
       vrl.setUuid(uuid);
       vrl.setUpdateDate(new Date());
       vrl.setOwnerId(user.getId());
       vmRefundLogService.saveVmRefundLog(vrl);    
    }
    
    /**
     * <用于处理:  申请退款-->取消退款-->再次申请退款时的 再次申请退款操作>
     */
    private void submitRefundApplyOnceAgain(String uuid, Short refundReasonType,
            String applyReason) throws HsCloudException {
       // 更新时根据uuid去判断是哪条记录,因为uuid字段是unique,需要更新的字段:
       // refundReasonType,applyReason,status更新为1,applyDate和updateDate
       vmRefundLogService.updateVmRefundLog(uuid,refundReasonType,applyReason);   
    }
    
    
    

}

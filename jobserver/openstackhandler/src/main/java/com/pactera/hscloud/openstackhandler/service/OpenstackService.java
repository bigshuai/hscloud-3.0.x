package com.pactera.hscloud.openstackhandler.service;

import java.util.List;

import com.pactera.hscloud.openstackhandler.bo.O_Network;
import com.pactera.hscloud.openstackhandler.bo.O_Router;
import com.pactera.hscloud.openstackhandler.bo.O_Snapshot;
import com.pactera.hscloud.openstackhandler.bo.O_VPDCInstance;
import com.pactera.hscloud.openstackhandler.bo.O_VPDC_Extdisk;
import com.pactera.hscloud.openstackhandler.vo.IPVO;
import com.pactera.hscloud.openstackhandler.vo.SnapshotVO;
import com.pactera.hscloud.openstackhandler.vo.VMVO;
import com.pactera.hscloud.openstackhandler.vo.VPDCReferenceVO;

public interface OpenstackService {

    /**
     * 绑定ip（虚拟机）
     * @param vpdcrvo
     * @param ipvo
     * @exception OpenstackHandlerException 参数或方法执行错误
     */
	public void bindingIP(VPDCReferenceVO vpdcrvo, IPVO ipvo);
	
	/**
	 * 释放ip（虚拟机）
	 * @param ipvo
	 * @exception OpenstackHandlerException 参数或方法执行错误
	 *  
	 */
	public void unbindingIP(IPVO ipvo);
	
	/**
	 * 绑定ip(路由)
	 * @param network
	 * @param ipvo
	 * @param r
	 * @exception OpenstackHandlerException 参数或方法执行错误
	 */
	public void bindingRouterIP(O_Network network,IPVO ipvo,O_Router r);

	/**
	 * 创建虚拟机
	 * @param vpdcr
	 * @param vpdci
	 * @exception OpenstackHandlerException 参数或方法执行错误
	 */
	public void createVM(VPDCReferenceVO vpdcr, O_VPDCInstance vpdci);
	
	/**
	 * 创建路由
	 * @param router
	 */
	public void createRouter(O_Router router);

	/**
	 * 同步虚拟机状态
	 * @param vpdcrvo
	 * @param vpdci
	 * @exception OpenstackHandlerException 参数或方法执行错误
	 */
	public void syncVMState(VPDCReferenceVO vpdcrvo, O_VPDCInstance vpdci);
	
	/**
	 * 同步路由状态
	 * @param r
	 * @exception OpenstackHandlerException 参数或方法执行错误
	 */
	public void syncRouterState(O_Router r);

	/**
	 * 绑定磁盘
	 * @param disks
	 * @exception OpenstackHandlerException 参数或方法执行错误
	 */
	public void bindingDisk(List<O_VPDC_Extdisk> disks);

	/**
	 * 操作成功失败结果，回写hc_event_resource，hc_event_vmops表
	 * @param r
	 * @exception OpenstackHandlerException 参数或方法执行错误
	 */
	public void saveResult(Object r);

	/**
	 * 备份
	 * @param snapshotVO
	 * @exception OpenstackHandlerException 参数或方法执行错误
	 */
	public void backupVM(SnapshotVO snapshotVO);
	
	/**
	 * 重置操作系统发邮件
	 * @param vmVO
	 *  @exception OpenstackHandlerException 参数或方法执行错误
	 */
	public void resetVmOs(VMVO vmVO);

	/**
	 * @param ss
	 * @exception OpenstackHandlerException 参数或方法执行错误
	 */
	public void updateSnapshot(O_Snapshot ss);

	/**
	 * 处理方法未找到执行方法。（空方法，主要用来记录日志）
	 */
	public void doNothing();
	
	/**
	 * MessageDiscerner解析报错，执行方法。（空方法，主要用来记录日志）
	 */
	public void error();

}

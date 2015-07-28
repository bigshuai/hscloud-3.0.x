package com.pactera.hscloud.openstackhandler.adapter;

import java.lang.reflect.Proxy;
import java.util.List;

import com.pactera.hscloud.common4j.util.CommonThreadLocal;
import com.pactera.hscloud.openstackhandler.bo.O_Network;
import com.pactera.hscloud.openstackhandler.bo.O_Router;
import com.pactera.hscloud.openstackhandler.bo.O_VPDCInstance;
import com.pactera.hscloud.openstackhandler.bo.O_VPDC_Extdisk;
import com.pactera.hscloud.openstackhandler.proxy.HandlerProxy;
import com.pactera.hscloud.openstackhandler.service.OpenstackService;
import com.pactera.hscloud.openstackhandler.service.impl.OpenstackServiceImpl;
import com.pactera.hscloud.openstackhandler.vo.IPVO;
import com.pactera.hscloud.openstackhandler.vo.SnapshotVO;
import com.pactera.hscloud.openstackhandler.vo.VMVO;
import com.pactera.hscloud.openstackhandler.vo.VPDCReferenceVO;

public class OpenstackAdapter {

	/**
	 * OpenstackServiceImpl 代理
	 */
	public static final OpenstackService proxy;

	static {
		OpenstackService openstackService = new OpenstackServiceImpl();
		HandlerProxy hadler = new HandlerProxy(openstackService);
		proxy = (OpenstackService) Proxy.newProxyInstance(openstackService
				.getClass().getClassLoader(), openstackService.getClass()
				.getInterfaces(), hadler);

	}

	public void createVM() {
		Object[] o = CommonThreadLocal.getParam();
		proxy.createVM((VPDCReferenceVO) o[0], (O_VPDCInstance) o[1]);
	}
	
	public void createRouter(){
		Object[] o = CommonThreadLocal.getParam();
		proxy.createRouter((O_Router) o[0]);
	}

	public void syncVMState() {
		Object[] o = CommonThreadLocal.getParam();
		proxy.syncVMState((VPDCReferenceVO) o[0], (O_VPDCInstance) o[1]);
	}
	
	public void syncRouterState(){
		Object[] o = CommonThreadLocal.getParam();
		proxy.syncRouterState((O_Router)o[0]);
	}

	public void unbindingIP(){
		Object[] o = CommonThreadLocal.getParam();
		proxy.unbindingIP((IPVO) o[0]);
	}
	
	public void bindingIP() {
		Object[] o = CommonThreadLocal.getParam();
		proxy.bindingIP((VPDCReferenceVO) o[0], (IPVO) o[1]);
	}
	
	public void bindingRouterIP(){
		Object[] o = CommonThreadLocal.getParam();
		proxy.bindingRouterIP((O_Network)o[0], (IPVO)o[1],(O_Router)o[2]);
	}

	@SuppressWarnings("unchecked")
	public void bindingDisk() {
		Object[] o = CommonThreadLocal.getParam();
		proxy.bindingDisk((List<O_VPDC_Extdisk>) o[0]);
	}

	public void saveResult() {
		Object[] o = CommonThreadLocal.getParam();
		proxy.saveResult(o[0]);
	}

	public void doNothing() {
		proxy.doNothing();
	}

	public void error() {
		proxy.error();
	}

	public void backupVM() {
		Object[] o = CommonThreadLocal.getParam();
		proxy.backupVM((SnapshotVO) o[0]);
	}

	public void resetVmOs(){
		Object[] o = CommonThreadLocal.getParam();
		proxy.resetVmOs((VMVO)o[0]);
	}

}

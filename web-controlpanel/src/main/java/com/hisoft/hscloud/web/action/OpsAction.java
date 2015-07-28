/* 
* 文 件 名:  OpsAction2.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  dinghb 
* 修改时间:  2013-1-5 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.action; 

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.entity.HcEventVmOps;
import com.hisoft.hscloud.common.entity.LogOperatorType;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.controlpanel.entity.ControlPanelUser;
import com.hisoft.hscloud.systemmanagement.vo.LogQueryVO;
import com.hisoft.hscloud.vpdc.ops.entity.VmSnapShotPlan;
import com.hisoft.hscloud.vpdc.ops.util.Utils;
import com.hisoft.hscloud.vpdc.ops.vo.InstanceDetailVo;
import com.hisoft.hscloud.vpdc.ops.vo.VmSnapShotPlanVO;
import com.hisoft.hscloud.vpdc.ops.vo.VmSnapShotVO;
import com.hisoft.hscloud.vpdc.ops.vo.VncClientVO;
import com.hisoft.hscloud.web.facade.Facade;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  dinghb 
 * @version  [版本号, 2013-1-5] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class OpsAction extends HSCloudAction {

	private static final long serialVersionUID = -6639340131348788134L;
	private Logger logger = Logger.getLogger(this.getClass());
	private Page<HcEventVmOps> pageOps = new Page<HcEventVmOps>(Constants.PAGE_NUM);
	@Autowired
	private Facade facade;
	private String osId;// 新的IP
	private String ssid;// snapshotId
	private String sn_name;// snapshot 名字
	private String sn_comments;// snapshot 备注
	private int sn_type;// snapshot 备份类型（0:手动、1:自动）
	private String planTime = "00:00";//定时备份时间
	private String name;
	private String password;
	private String code;
	private VmSnapShotPlan vssp;
	private int page;
    private int limit;
    private Page<HcEventVmOps> paging;
    private LogQueryVO logQueryVO = new LogQueryVO();
    
	/**
	 * <启动虚拟机>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String startVM() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter startVM method.");			
		}
		boolean result = false;
		ControlPanelUser controlPanelUser = (ControlPanelUser)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
		if(controlPanelUser==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String vmId = controlPanelUser.getVmId();
		String operateObject = "VM[vmId:" + vmId + "]";
		try {
			result = facade.startVmByVmId(vmId,controlPanelUser,LogOperatorType.CP.getName());
			facade.insertOperationLog(controlPanelUser,"发送启动虚拟机命令成功","发送启动虚拟机命令",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(controlPanelUser,"发送启动虚拟机命令出现错误:"+e.getMessage(),"发送启动虚拟机命令",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VM_START_ERROR, "startVM异常",
					logger, e), "000");
			return null;
		}
		if (result) {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		} else {
			logger.error("ERROR:" + result);
			super.fillActionResult(Constants.VM_START_ERROR);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit startVM method.takeTime:" + takeTime + "ms");
		}
		return null;
	}

	/**
	 * <发送重启虚拟机命令>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String rebootVM() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter rebootVM method.");			
		}
		String result = null;
		ControlPanelUser controlPanelUser = (ControlPanelUser)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
		if(controlPanelUser==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String vmId = controlPanelUser.getVmId();
		String operateObject = "VM[vmId:" + vmId + "]";
		try {
			result = facade.rebootVmByVmId(vmId,controlPanelUser,LogOperatorType.CP.getName());
			facade.insertOperationLog(controlPanelUser,"发送重启虚拟机命令成功","发送重启虚拟机命令",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(controlPanelUser,"发送重启虚拟机命令出现错误:"+e.getMessage(),"发送重启虚拟机命令",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VM_REBOOT_ERROR, "rebootVM异常",
					logger, e), "000");
			return null;
		}
		if (result != null) {
			logger.info("ERROR:" + result);
			super.fillActionResult(Constants.VM_REBOOT_ERROR);
		} else {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit rebootVM method.takeTime:" + takeTime + "ms");
		}
		return null;
	}

	/**
	 * <关闭虚拟机>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String closeVM() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter closeVM method.");			
		}
		boolean bl;
		ControlPanelUser controlPanelUser = (ControlPanelUser)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
		if(controlPanelUser==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String vmId = controlPanelUser.getVmId();
		String operateObject = "VM[vmId:" + vmId + "]";
		try {
			bl = facade.closeVmByVmId(vmId,controlPanelUser,LogOperatorType.CP.getName());
			facade.insertOperationLog(controlPanelUser,"发送关闭虚拟机命令成功","发送关闭虚拟机命令",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(controlPanelUser,"发送关闭虚拟机命令出现错误:"+e.getMessage(),"发送关闭虚拟机命令",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VM_CLOSE_ERROR, "suspendVM异常",
					logger, e), "000");
			return null;
		}
		if (bl) {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		} else {
			super.fillActionResult(Constants.VM_CLOSE_ERROR);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit closeVM method.takeTime:" + takeTime + "ms");
		}
		return null;
	}

	/**
	 * <获取虚拟机远程控制>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String getVNC() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getVNC method.");			
		}
		String vncUrl = null;
		ControlPanelUser controlPanelUser = (ControlPanelUser)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
		if(controlPanelUser==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String vmId = controlPanelUser.getVmId();
		String operateObject = "VM[vmId:" + vmId + "]";
		try {
			vncUrl = facade.getVNCUrl(vmId);
			facade.insertOperationLog(controlPanelUser,"发送获取VNC远程控制命令成功","发送获取VNC远程控制命令",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(controlPanelUser,"发送获取VNC远程控制命令出现错误:"+e.getMessage(),"发送获取VNC远程控制命令",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VM_VNC_ERROR, "getVNC异常", logger,
					e), "000");
			return null;
		}
		if (vncUrl != null) {
			super.fillActionResult((Object) vncUrl);
		} else {
			super.fillActionResult(Constants.VM_VNC_ERROR);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getVNC method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <获取虚拟机操作系统用户名> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String getVmOSUser(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter resetVmOS method.");			
		}
		String osUser = "";
		try {
			osUser = facade.getVMOSUser(osId);
		} catch (Exception e) {
			dealThrow(new HsCloudException(Constants.VM_RESETOS_ERROR, "resetVmOS异常", logger, e), "000");
			return null;
		}
		Object user = osUser;
		super.fillActionResult(user);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getVmOSUser method.takeTime:" + takeTime + "ms");
		}
		return null;
	}

	/**
	 * <重置虚拟机操作系统> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String resetVmOS(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter resetVmOS method.");			
		}
		ControlPanelUser controlPanelUser = (ControlPanelUser)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
		if(controlPanelUser==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String vmId = controlPanelUser.getVmId();
		String operateObject = "VM[vmId:" + vmId + "]";
		try {
			facade.resetVMOS(vmId, osId,name,password,controlPanelUser,LogOperatorType.CP.getName());
			facade.insertOperationLog(controlPanelUser,"发送重置操作系统命令成功","发送重置操作系统命令",Constants.RESULT_SUCESS,operateObject);
		} catch (Exception e) {
			facade.insertOperationLog(controlPanelUser,"发送重置操作系统命令出现错误:"+e.getMessage(),"发送重置操作系统命令",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VM_RESETOS_ERROR, "resetVmOS异常", logger, e), "000");
			return null;
		}
		super.fillActionResult(Constants.OPTIONS_SUCCESS);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit resetVmOS method.takeTime:" + takeTime + "ms");
		}
		return null;
	}

	/**
	 * <查询虚拟机详情>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String findDetailVmById() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findDetailVmById method.");			
		}
		InstanceDetailVo detailVo = new InstanceDetailVo();
		ControlPanelUser controlPanelUser = (ControlPanelUser)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
		if(controlPanelUser==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String vmId = controlPanelUser.getVmId();
		try {
			detailVo = facade.findDetailVmById(vmId);
			if (detailVo == null) {// 因为openstaic没有得到vm
				super.fillActionResult(Constants.VM_DETAIL_ERROR);
			}
		} catch (HsCloudException e) {
			dealThrow(new HsCloudException(Constants.VM_DETAIL_ERROR,
					"findDetailVmById异常", logger, e), "000");
			return null;
		}
		super.fillActionResult(detailVo);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findDetailVmById method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * 获取vncviewer连接VNC的参数
	 * @return
	 */
	public String getClientVNC(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getClientVNC method.");			
		}
		ControlPanelUser controlPanelUser = (ControlPanelUser)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
		if(controlPanelUser==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String vmId = controlPanelUser.getVmId();	
		String operateObject = "VM[vmId:" + vmId + "]";
		VncClientVO vnc = null;
		int exceptionType = 0;//0:验证码错误 1:获取远程连接参数异常
		try {
			//检查验证码是否正确
			String sessionCode = (String)ServletActionContext.getRequest().getSession().getAttribute("code");
			if (null == this.code || "".equals(this.code)) {
				fillActionResult(Constants.VERCODE_IS_NULL);
			} else if (null == sessionCode || !sessionCode.equalsIgnoreCase(this.code)) {
				fillActionResult(Constants.VERCODE_IS_ERROR);
			} else {
				exceptionType = 1;
				vnc = facade.getClientVnc(vmId);
			}
		} catch (HsCloudException e) {
			if (exceptionType == 0) {
				if(e.getClass()==new HsCloudException().getClass()){
					HsCloudException ex = (HsCloudException)e;
					dealThrow(new HsCloudException(ex.getCode(), "getClientVNC异常", logger, e), "000");
				}else{
					dealThrow(new HsCloudException(Constants.VERCODE_IS_ERROR, "getClientVNC异常", logger, e), "000");
				}
				return null;
			} else {
				if(e.getClass()==new HsCloudException().getClass()){
					HsCloudException ex = (HsCloudException)e;
					dealThrow(new HsCloudException(ex.getCode(), "getClientVNC异常", logger, e), "000");
				}else{
					dealThrow(new HsCloudException(Constants.VM_VNC_ERROR, "getClientVNC异常", logger, e), "000");
				}
				return null;
			}
		}
		if (exceptionType == 1 && vnc != null) {
			super.fillActionResult((Object) vnc);
		} else if (exceptionType == 1 && vnc == null) {
			super.fillActionResult(Constants.VM_VNC_ERROR);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getVNC method.takeTime:" + takeTime + "ms");
		}
		return null;
	}	
	
	/**
	 * <获取VM的OS集合> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String getVmOsListByVmId() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getVmOsListByVmId method.");			
		}
		InstanceDetailVo idv = null;
		ControlPanelUser controlPanelUser = (ControlPanelUser)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
		if(controlPanelUser==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String vmId = controlPanelUser.getVmId();
		try {
			idv = facade.getVmOsListByVmId(vmId);
		} catch (HsCloudException e) {
			dealThrow(new HsCloudException(Constants.VM_DETAIL_ERROR,
					"getVmOsListByVmId异常", logger, e), "000");
			return null;
		}
		super.fillActionResult(idv);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getVmOsListByVmId method.takeTime:" + takeTime + "ms");
		}
		return null;
	}

	/**
	 * <备份虚拟机> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String backupVM() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter backupVM method.");			
		}
		ControlPanelUser controlPanelUser = (ControlPanelUser)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
		if(controlPanelUser==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String vmId = controlPanelUser.getVmId();
		String operateObject = "VM[vmId:" + vmId + "]";
		String result = null;
		try {
			result = facade.backupVmByVmId(vmId, sn_name, sn_comments,
					sn_type,controlPanelUser,LogOperatorType.CP.getName());
			facade.insertOperationLog(controlPanelUser,"发送备份虚拟机命令成功","发送备份虚拟机命令",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(controlPanelUser,"发送备份虚拟机命令出现错误:"+e.getMessage(),"发送备份虚拟机命令",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VM_BACKUP_ERROR, "createBackupVmPlan异常", logger, e), "000");
			return null;
		}
		if (result != null) {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		} else {
			super.fillActionResult(Constants.VM_BACKUP_ERROR);
		}
		if(null == result){
			fillActionResult(Constants.VM_NO_ACTIVE); 
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit backupVM method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <创建备份计划> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String createBackupVmPlan(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter createBackupVmPlan method.");			
		}
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
		ControlPanelUser controlPanelUser = (ControlPanelUser)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
		if(controlPanelUser==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String result = null;
		String vmId = controlPanelUser.getVmId();
		String operateObject = "VM[vmId:" + vmId + "]";
		try {			
			vssp = Utils.strutsJson2Object(VmSnapShotPlan.class);
			Date dtime = sdf.parse(planTime);
			Time time = new Time(dtime.getTime());
			vssp.setPlanTime(time);
			vssp.setModifyUser(controlPanelUser.getId());
			vssp.setVmId(controlPanelUser.getVmId());
			VmSnapShotPlanVO vsspVO = facade.getBackupVmPlanByVmId(vmId);
			if (vsspVO != null) {
				vssp.setId(vsspVO.getId());
				if (0 == vssp.getPlanType()) {
					vssp.setPlanTime(vsspVO.getPlanTime());
					vssp.setPlanDate(vsspVO.getPlanDate());
				}
			}
			result = facade.createBackupVmPlanByVmId(vssp);
			facade.insertOperationLog(controlPanelUser,"创建备份计划成功","创建备份计划",Constants.RESULT_SUCESS,operateObject);
		} catch (Exception e) {
			facade.insertOperationLog(controlPanelUser,"创建备份计划出现错误:"+e.getMessage(),"创建备份计划",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VM_BACKUP_PLAN_ERROR, "createBackupVmPlan异常", logger, e), "000");
			return null;
		}
		if (result != null) {
			super.fillActionResult(Constants.VM_BACKUP_PLAN_ERROR);
		} else {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit createBackupVmPlan method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	public String getVmBackupPlan() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getVmBackupPlan method.");			
		}
		ControlPanelUser controlPanelUser = (ControlPanelUser)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
		if(controlPanelUser==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String vmId = controlPanelUser.getVmId();
		String operateObject = "VM[vmId:" + vmId + "]";
		VmSnapShotPlanVO vsspVO = null;
		try {
			vsspVO = facade.getBackupVmPlanByVmId(vmId);
		} catch (Exception e) {
			dealThrow(new HsCloudException(Constants.VM_SNAPSHOT_GET_ERROR, "getVmBackupPlan异常", logger, e), "000");
			return null;
		}
		super.fillActionResult(vsspVO);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getVmBackupPlan method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <还原虚拟机> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String renewVm() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter renewVm method.");			
		}
		String result = null;
		ControlPanelUser controlPanelUser = (ControlPanelUser)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
		if(controlPanelUser==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String vmId = controlPanelUser.getVmId();
		String operateObject = "VM[vmId:" + vmId + "]";
		try {
			result = facade.renewVM(vmId, ssid,controlPanelUser,LogOperatorType.CP.getName());
			fillActionResult(true);
			facade.insertOperationLog(controlPanelUser,"发送还原虚拟机命令成功","发送还原虚拟机命令",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(controlPanelUser,"发送还原虚拟机命令出现错误:"+e.getMessage(),"发送还原虚拟机命令",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VM_RENEW_ERROR, "renewVm异常", logger, e), "000");
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit renewVm method.takeTime:" + takeTime + "ms");
		}
		return result;
	}
	/**
	 * <获得虚拟机最后一次的备份> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String getLastestVmSnapshot(){		
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getLastestVmSnapshot method.");			
		}
		Map<String ,Object> map = new HashMap<String, Object>();
		ControlPanelUser controlPanelUser = (ControlPanelUser)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
		if(controlPanelUser==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String vmId = controlPanelUser.getVmId();
		try{
			VmSnapShotVO vmSnapShotVO = facade.getLastestVmSnapshot(vmId);
			map.put("totalCount", 1);
			map.put("result", vmSnapShotVO);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.OPTIONS_FAILURE,
					"getLastestVmSnapshot异常", logger, ex), Constants.OPTIONS_FAILURE);
		}
		super.fillActionResult(map);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getLastestVmSnapshot method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <终止快照:实质就是删除超过30分钟都还没有创建成功的快照> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String stopSnapshot() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter stopSnapshot method.");			
		}
		ControlPanelUser controlPanelUser = (ControlPanelUser) super
				.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
		if (controlPanelUser == null) {
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String vmId = controlPanelUser.getVmId();
		boolean result = false;
		String operateObject = "VM[vmId:" + vmId + "]";
		try {
			result = facade.stopSnapshot(vmId);
			facade.insertOperationLog(controlPanelUser,"终止快照成功","终止快照",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(controlPanelUser,"终止快照出现错误:"+e.getMessage(),"终止快照",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException("", "stopSnapshot异常", logger, e),"");
			return null;
		}
		super.fillActionResult(result);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit stopSnapshot method.takeTime:" + takeTime + "ms");
		}
		return null;
	}	
	/**
	 * <查询关于虚拟机操作的日志> 
	* <功能详细描述> 
	 */
	public void pageVmOpsLog() {
		paging = new Page<HcEventVmOps>(limit);
		paging.setPageNo(page);
		paging.setOrder(Page.DESC);
		paging.setOrderBy("id");
		long beginRunTime = 0;
		ControlPanelUser controlPanelUser = (ControlPanelUser)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
		logQueryVO.setUuid(controlPanelUser.getVmId());
		logQueryVO.setOperationType(LogOperatorType.CP.getIndex());
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter pageVmOpsLog method.");
		}
		try {
			Page<HcEventVmOps> log = facade.getVmOpsLog(paging, logQueryVO);
			super.fillActionResult(log);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "pageVmOpsLog 异常", logger, e),"");
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit pageVmOpsLog method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <用途: 主机修复 >
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public String repairOS() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter repairOS method.");			
		}
		ControlPanelUser controlPanelUser = (ControlPanelUser) super
				.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
		if (controlPanelUser == null) {
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String vmId = controlPanelUser.getVmId();
		String operateObject = "VM[vmId:" + vmId + "]";
		boolean bl;
		try {
			bl = facade.repairOSByVmId(vmId, controlPanelUser, LogOperatorType.CP.getName());
			facade.insertOperationLog(controlPanelUser,"发送主机修复命令成功","发送主机修复命令",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(controlPanelUser,"发送主机修复命令出现错误:"+e.getMessage(),"发送主机修复命令",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VM_CLOSE_ERROR, "repairOS Exception:",
					logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if (bl) {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		} else {
			super.fillActionResult(Constants.VM_REPAIR_ERROR);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit repairOS method.takeTime:" + takeTime + "ms");
		}
		return null;
	}		
	
	
	public String getOsId() {
		return osId;
	}

	public void setOsId(String osId) {
		this.osId = osId;
	}

	/**
	 * @return 返回 log
	 */
	public Logger getLog() {
		return logger;
	}

	/**
	 * @param 对log进行赋值
	 */
	public void setLog(Logger logger) {
		this.logger = logger;
	}

	/**
	 * @return 返回 serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSn_name() {
		return sn_name;
	}

	public void setSn_name(String sn_name) {
		this.sn_name = sn_name;
	}

	public String getSn_comments() {
		return sn_comments;
	}

	public void setSn_comments(String sn_comments) {
		this.sn_comments = sn_comments;
	}

	public int getSn_type() {
		return sn_type;
	}

	public void setSn_type(int sn_type) {
		this.sn_type = sn_type;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getPlanTime() {
		return planTime;
	}

	public void setPlanTime(String planTime) {
		this.planTime = planTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Page<HcEventVmOps> getPageOps() {
		return pageOps;
	}

	public void setPageOps(Page<HcEventVmOps> pageOps) {
		this.pageOps = pageOps;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public Page<HcEventVmOps> getPaging() {
		return paging;
	}

	public void setPaging(Page<HcEventVmOps> paging) {
		this.paging = paging;
	}

	public LogQueryVO getLogQueryVO() {
		return logQueryVO;
	}

	public void setLogQueryVO(LogQueryVO logQueryVO) {
		this.logQueryVO = logQueryVO;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
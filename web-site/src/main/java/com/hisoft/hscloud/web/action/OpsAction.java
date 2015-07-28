/**
 * @title UserAction.java
 * @package com.hisoft.hscloud.vpdc.ops.action
 * @description 用一句话描述该文件做什么
 * @author hongqin.li
 * @update 2012-3-30 上午11:44:16
 * @version V1.0
 */
package com.hisoft.hscloud.web.action;

import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.sc.vo.ScFeeTypeVo;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.entity.HcEventVmOps;
import com.hisoft.hscloud.common.entity.LogOperatorType;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.systemmanagement.vo.LogQueryVO;
import com.hisoft.hscloud.vpdc.ops.entity.VmSnapShotPlan;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcRenewal;
import com.hisoft.hscloud.vpdc.ops.json.bean.BackupsVmBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVmBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVpdcBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVpdcBean.VlanNetwork;
import com.hisoft.hscloud.vpdc.ops.json.bean.RenewVmBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.UserBean;
import com.hisoft.hscloud.vpdc.ops.util.Utils;
import com.hisoft.hscloud.vpdc.ops.vo.FlavorVO;
import com.hisoft.hscloud.vpdc.ops.vo.InstanceDetailVo;
import com.hisoft.hscloud.vpdc.ops.vo.InstanceVO;
import com.hisoft.hscloud.vpdc.ops.vo.RenewOrderVO;
import com.hisoft.hscloud.vpdc.ops.vo.SecurityPolicyVO;
import com.hisoft.hscloud.vpdc.ops.vo.UnaddedIntranetInstanceVO;
import com.hisoft.hscloud.vpdc.ops.vo.VmExtranetSecurityVO;
import com.hisoft.hscloud.vpdc.ops.vo.VmIntranetSecurityVO;
import com.hisoft.hscloud.vpdc.ops.vo.VmSnapShotPlanVO;
import com.hisoft.hscloud.vpdc.ops.vo.VmSnapShotVO;
import com.hisoft.hscloud.vpdc.ops.vo.VncClientVO;
import com.hisoft.hscloud.vpdc.ops.vo.VolumeVO;
import com.hisoft.hscloud.vpdc.ops.vo.VpdcRouterVo;
import com.hisoft.hscloud.vpdc.ops.vo.VpdcVo;
import com.hisoft.hscloud.vpdc.ops.vo.VrouterTemplateVO;
import com.hisoft.hscloud.web.facade.Facade;

/**
 * @description 普通用户使用此类
 * @version 1.0
 * @author hongqin.li
 * @update 2012-3-30 上午11:44:16 df
 */
public class OpsAction extends HSCloudAction {

	private static final long serialVersionUID = -6639340131348788134L;
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private Facade facade;
	private BackupsVmBean backupsVmBean;
	private VmSnapShotPlan vssp;
	private UserBean userBean;
	private RenewVmBean renewVmBean;
	private CreateVpdcBean createVpdcBean;
	private CreateVmBean createVmBean;
	private InstanceDetailVo instanceDetailVo;
	private String id;
	private String osId;//操作系统id
	private String scId;//套餐id
	private String terminateFlag;//1:同时删除配置reference;null或者0只删除instance
	private final String resourceType = "com.hisoft.hscloud.vpdc.ops.entity.VpdcReference";
	private String name;
	private String type;//VM的类型（0:试用;正式:1）
	private String status_buss;//VM的业务状态（0:试用待审;1：试用中；2：延期待审；3：已延期；4：正式；5：取消；6：到期）
	private String sn_name;// snapshot 名字
	private String sn_comments;// snapshot 备注
	private int sn_type;// snapshot 备份类型（0:手动、1:自动）
	private String ssid;// snapshotId
	private String planTime;
	private String CPpwd;//控制面板密码
	private String password;
	private long referenceId;
	private String feeTypeId;//计费方式id
	private String page;
	private String start;
	private String limit;
	private String queryType;// 模糊查询条件类型
	private String query;// 模糊查询条件值
	private String sort;
	private String uuid;
	private String operationType;
	private String startTime;
	private String endTime;
	private String couponAmount; //返点数
	private String giftAmount;
	private String code; //验证码
	private Short ops;// 模糊查询条件类型
	private String groupId;//内网安全云主机所属同一组的组号
	private String instanceId;//云主机instanceId
	private String submit_instanceIds;//内网安全提交的所有的云主机的instanceId
	private String submit_uuids;//内网安全提交的所有的云主机的uuid
	private String old_instanceIds;//原来的数据库中加载的内网安全的所有的云主机的instanceId
	private String old_uuids;//原来的数据库中加载的内网安全的所有的云主机的uuid
	private String extranet_submit_data;//外网安全提交的数据
	private String extranet_old_data;//原来的数据库中加载的外网安全的数据
	private String class_dns1;//修改的时候提交的dns1的值合起来组成的字符串
	private String class_dns2;//修改的时候提交的dns2的值合起来组成的字符串
	private int vpdcType;//VPDC类型（0：非路由；1：路由） 
	private long zoneGroup;//机房线路
	private long vpdcId;//当用用户需要前往的VPDC
	private List<VmExtranetSecurityVO> vmExtra_list = new ArrayList<VmExtranetSecurityVO>();
	private List<VmIntranetSecurityVO> vmIntra_list = new ArrayList<VmIntranetSecurityVO>();
	private Page<InstanceVO> pageIns = new Page<InstanceVO>();
	private Page<UnaddedIntranetInstanceVO> pageUnaddedIns = new Page<UnaddedIntranetInstanceVO>();
	private Page<VpdcRenewal> pageRenewal = new Page<VpdcRenewal>();
	private Page<HcEventVmOps> pageVmOpsLog = new Page<HcEventVmOps>();
	private Page<VpdcVo> pageVpdc = new Page<VpdcVo>();	
	private int volumeId;//扩展盘Id
	private int volumeMode;//扩展盘挂载方式：//0:无-1：isics方式扩展盘-2：SCIS方式扩展盘
	private int volumeSize;//扩展盘大小
	private String addDisk;//扩展盘
	private FlavorVO flavorVO;
	private String planId;
	
	/**
	 * <分页获取User用户所有的云主机> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public String getVmsByUser() throws UnsupportedEncodingException {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getVmsByUser method.");			
		}
		User user = (User)super.getCurrentLoginUser();
		Long statusId = 0L;
		if(super.getSession().getAttribute("enUid")!=null){
			statusId = Long.valueOf(super.getSession().getAttribute("enUid").toString());
		}
		List<Object> SubUserVMIds = super.getPrimKeys();
		try {
			pageIns = facade.getVmsByUser(page, limit, pageIns, queryType,query, sort,user,SubUserVMIds,statusId,type,status_buss);
		}catch (HsCloudException e) {
			dealThrow(new HsCloudException(Constants.VM_LIST_USER_ERROR, "getVmsByUser异常", logger, e), "000");
		}
		super.fillActionResult(pageIns);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getVmsByUser method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <新加的接口：UI3.0   分页获取User用户所有的云主机> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public String getHostsByUser() throws UnsupportedEncodingException {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getHostsByUser method.");			
		}
		User user = (User)super.getCurrentLoginUser();
		Long statusId = 0L;
		if(super.getSession().getAttribute("enUid")!=null){
			statusId = Long.valueOf(super.getSession().getAttribute("enUid").toString());
		}
		List<Object> SubUserVMIds = super.getPrimKeys();
		try {
			if (StringUtils.isNotEmpty(query)) {
				query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
			}
			pageIns = facade.getHostsByUser(page, limit, pageIns, query, sort,user,SubUserVMIds,statusId,type,status_buss);
		}catch (HsCloudException e) {
			dealThrow(new HsCloudException(Constants.VM_LIST_USER_ERROR, "getHostsByUser异常", logger, e), "000");
		}
		super.fillActionResult(pageIns);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getHostsByUser method.takeTime:" + takeTime + "ms");
		}
		return null;
	}	
	
	/**
	 * <用途: 1.默认加载分页获取User用户所有的待添加内网安全策略的云主机
	 *       2.当点击内网安全策略的搜索按钮: 分页获取User用户所有的待添加内网安全策略的云主机> 
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public String getWaitAddIntranetSecurityVms() throws UnsupportedEncodingException {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getWaitAddIntranetSecurityVms method.");
		}
		User user = (User) super.getCurrentLoginUser();
		Long statusId = 0L;
		if (super.getSession().getAttribute("enUid") != null) {
			statusId = Long.valueOf(super.getSession().getAttribute("enUid")
					.toString());
		}
		List<Object> SubUserVMIds = super.getPrimKeys();
		try {
			if (StringUtils.isNotEmpty(query)) {
				query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
			}
			pageUnaddedIns = facade.getWaitAddIntranetSecurityVms(page, limit,
					pageUnaddedIns, query, sort, user, SubUserVMIds, statusId, type,
					status_buss, groupId);
			super.fillActionResult(pageUnaddedIns);
		} catch (HsCloudException e) {
			dealThrow(new HsCloudException(Constants.VM_LIST_USER_ERROR,
					"getWaitAddIntranetSecurityVms异常", logger, e), "000");
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getWaitAddIntranetSecurityVms method.takeTime:"
					+ takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <用途: 获取当前用户的当前uuid这台云主机的添加了外网安全的协议和端口的详细信息
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public String getExtranetSecurityInfo() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getExtranetSecurityInfo method.");			
		}
		try {
			vmExtra_list = facade.getExtranetSecurityInfo(uuid);
		}catch (HsCloudException e) {
			dealThrow(new HsCloudException(Constants.VM_LIST_USER_ERROR, "getExtranetSecurityInfo异常", logger, e), "000");
		}
		super.fillActionResult(vmExtra_list);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getExtranetSecurityInfo method.takeTime:" + takeTime + "ms");
		}
		return null;
	}

	/**
	 * <启动云主机> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String startVM(){
		long beginRunTime = 0;
		User user = (User)super.getCurrentLoginUser();
		if(user==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter startVM method.");			
		}
		boolean result = false;
		String operateObject = "VM[vmId:" + id + "]";
		try {
			result = facade.startVmByVmId(id,user,LogOperatorType.USER.getName());
			facade.insertOperationLog(user,"成功受理启动云主机操作","启动云主机",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"启动云主机操作出现错误:"+e.getMessage(),"启动云主机",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VM_START_ERROR, "startVM异常", logger, e), "000");
			return null;
		}
		if (result) {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		} else {
			super.fillActionResult(Constants.VM_START_ERROR);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit startVM method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <重启云主机> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String rebootVM() {
		long beginRunTime = 0;
		User user = (User)super.getCurrentLoginUser();
		if(user==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter rebootVM method.");			
		}
		String result = null;
		String operateObject = "VM[vmId:" + id + "]";
		try {
			result = facade.rebootVmByVmId(id,user,LogOperatorType.USER.getName());
			facade.insertOperationLog(user,"成功受理重启云主机操作","重启云主机",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"重启云主机操作出现错误:"+e.getMessage(),"重启云主机",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VM_REBOOT_ERROR, "rebootVM异常", logger, e), "000");
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
	 * <关闭云主机> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String closeVM() {
		long beginRunTime = 0;
		User user = (User)super.getCurrentLoginUser();
		if(user==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter closeVM method.");			
		}
		boolean bl;
		String operateObject = "VM[vmId:" + id + "]";
		try {
			bl = facade.closeVmByVmId(id,user,LogOperatorType.USER.getName());
			facade.insertOperationLog(user,"成功受理关闭云主机操作","关闭云主机",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"关闭云主机操作出现错误:"+e.getMessage(),"关闭云主机",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VM_CLOSE_ERROR, "suspendVM异常", logger, e), "000");
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
	 * <唤醒云主机> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	/*public String resumeVm() {
		logger.info("OPS-Action-resumeVm start");
		String result = null;
		try {
			result = facade.resumeVmByVmId(id);
		} catch (HsCloudException e) {
			dealThrow(new HsCloudException(Constants.VM_RESUME_ERROR, "resumeVmByVmId异常", logger, e), "000");
			return null;
		}
		if (result != null) {
			logger.info("ERROR:" + result);
			super.fillActionResult(Constants.VM_RESUME_ERROR);
		} else {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		}
		return null;
	}*/
	/**
	 * <获取云主机远程控制> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String getVNC() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getVNC method.");			
		}
		User user = (User)super.getCurrentLoginUser();
		if(user==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String vncUrl = null;
		String operateObject = "VM[vmId:" + id + "]";
		try {
			vncUrl = facade.getVNCUrl(id);
			facade.insertOperationLog(user,"成功受理获取VNC远程控制操作","VNC远程控制",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"获取VNC远程控制操作出现错误:"+e.getMessage(),"VNC远程控制",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VM_VNC_ERROR, "getVNC异常", logger, e), "000");
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
	 * 获取vncviewer连接VNC的参数
	 * @return
	 */
	public String getClientVNC(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getClientVNC method.");			
		}
		User user = (User)super.getCurrentLoginUser();
		if(user==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
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
				vnc = facade.getClientVnc(id,user);
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
	 * <备份云主机> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String backupVM() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter backupVM method.");			
		}
		User user = (User)super.getCurrentLoginUser();
		if(user==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String result = null;
		String operateObject = "VM[vmId:" + id + "]";
		try {
			result = facade.backupVmByVmId(id, sn_name, sn_comments,
					sn_type,user,LogOperatorType.USER.getName());
			facade.insertOperationLog(user,"成功受理备份云主机操作","备份云主机",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"备份云主机操作出现错误:"+e.getMessage(),"受理备份云主机",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VM_BACKUP_ERROR, "createBackupVmPlan异常", logger, e), "000");
			return null;
		}
		if (result != null) {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		} else {
			super.fillActionResult(Constants.VM_BACKUP_ERROR);
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
		String result = null;
		User user = (User)super.getCurrentLoginUser();
		if(user==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String operateObject = "VM[vmId:" + id + "]";
		try {
			vssp = Utils.strutsJson2Object(VmSnapShotPlan.class);
			Date dtime = sdf.parse(planTime);
			Time time = new Time(dtime.getTime());
			vssp.setPlanTime(time);
			vssp.setModifyUser(user.getId());
			VmSnapShotPlanVO vsspVO = facade.getBackupVmPlanByVmId(id);
			if (vsspVO != null) {
				vssp.setId(vsspVO.getId());
				if (0 == vssp.getPlanType()) {
					vssp.setPlanTime(vsspVO.getPlanTime());
					vssp.setPlanDate(vsspVO.getPlanDate());
				}
			}
			result = facade.createBackupVmPlanByVmId(vssp);
			facade.insertOperationLog(user,"成功创建备份计划","创建备份计划",Constants.RESULT_SUCESS,operateObject);
		} catch (Exception e) {
			facade.insertOperationLog(user,"创建备份计划出现错误:"+e.getMessage(),"创建备份计划",Constants.RESULT_FAILURE,operateObject);
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
	/**
	 * <删除云主机备份> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String deleteBackup() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter deleteBackup method.");			
		}
		User user = (User)super.getCurrentLoginUser();
		if(user==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String result = null;
		String operateObject = "VM[vmId:" + id + "]";
		try {
			result = facade.deleteBackupByBackupId(id,ssid);
			facade.insertOperationLog(user,"成功受理删除云主机备份操作","删除云主机备份",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"删除云主机备份出现错误:"+e.getMessage(),"删除云主机备份",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VM_SNAPSHOT_DELETE_ERROR, "deleteBackup异常", logger, e), "000");
			return null;
		}
		if (result != null) {
			super.fillActionResult(Constants.VM_SNAPSHOT_DELETE_ERROR);
		} else {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit deleteBackup method.takeTime:" + takeTime + "ms");
		}
		return null;

	}
	/**
	 * <获取云主机备份计划> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String getVmBackupPlan() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getVmBackupPlan method.");			
		}
		VmSnapShotPlanVO vsspVO = null;
		try {
			vsspVO = facade.getBackupVmPlanByVmId(id);
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
	 * <获取所有的备份记录> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String findAllBackups() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findAllBackups method.");			
		}
		try {
			List<VmSnapShotVO> backups = facade.findAllBackupsByVmId(id);
			Map<String, Object> map = new HashMap<String, Object>();
			if (backups == null) {
				map.put("totalCount", 0);
				map.put("result", null);
				return null;
			}
			map.put("totalCount", backups.size());
			map.put("result", backups);
			super.fillActionResult(backups);
		} catch (HsCloudException e) {
			dealThrow(new HsCloudException(Constants.VM_SNAPSHOT_LIST_ERROR, "findAllBackups异常", logger, e), "000");
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findAllBackups method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <还原云主机> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String renewVm() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter renewVm method.");			
		}
		User user = (User)super.getCurrentLoginUser();
		if(user==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String result = null;
		String operateObject = "VM[vmId:" + id + "]";
		try {
			result = facade.renewVM(id, ssid,user,LogOperatorType.USER.getName());
			facade.insertOperationLog(user,"成功受理还原云主机操作","还原云主机",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"还原云主机操作出现错误:"+e.getMessage(),"还原云主机",Constants.RESULT_FAILURE,operateObject);
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
	 * <终止云主机> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String terminateVm() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter terminateVm method.");			
		}
		User user = (User)super.getCurrentLoginUser();
		if(user==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String operateObject = "VM[vmId:" + id + "]";
		try {
			facade.terminateVmByVmId(id,name,terminateFlag,user.getId());
			facade.insertOperationLog(user,"成功受理终止云主机操作","终止云主机",Constants.RESULT_SUCESS,operateObject);
		} catch (Exception e) {
			facade.insertOperationLog(user,"终止云主机操作出现错误:"+e.getMessage(),"终止云主机",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VM_TERMINATE_ERROR, "terminateVm异常", logger, e), "000");
			return null;
		}
		super.fillActionResult(Constants.OPTIONS_SUCCESS);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit terminateVm method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <获取云主机操作系统用户名> 
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
	 * <重置云主机操作系统> 
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
		User user = (User)super.getCurrentLoginUser();
		if(user==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String operateObject = "VM[vmId:" + id + "]";
		try {
			facade.resetVMOS(id, osId,name,password,user,LogOperatorType.USER.getName());
			facade.insertOperationLog(user,"成功受理重置操作系统操作","重置操作系统",Constants.RESULT_SUCESS,operateObject);
		} catch (Exception e) {
			facade.insertOperationLog(user,"重置操作系统操作出现错误:"+e.getMessage(),"重置操作系统",Constants.RESULT_FAILURE,operateObject);
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
	 * <重置云主机密码> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public String resetSystemPwd() {
	    long beginRunTime = 0;
        if(logger.isDebugEnabled()){
            beginRunTime = System.currentTimeMillis();
            logger.debug("enter reSendOpenEmail method.");          
        }
		User user = (User)super.getCurrentLoginUser();
		if(user==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String operateObject = "VM[vmId:" + id + "]";
        try {
            facade.resetSystemPwd(id, password,user,LogOperatorType.USER.getName());
			facade.insertOperationLog(user,"成功受理重置云主机密码操作","重置云主机密码",Constants.RESULT_SUCESS,operateObject);
        } catch (HsCloudException e) {
			facade.insertOperationLog(user,"重置云主机密码操作出现错误:"+e.getMessage(),"重置云主机密码",Constants.RESULT_FAILURE,operateObject);
            dealThrow(new HsCloudException("", "resetPassword Exception:", logger, e),Constants.VM_RESET_PASSWORD_ERROR,true);
        }
        if(logger.isDebugEnabled()){
            long takeTime = System.currentTimeMillis() - beginRunTime;
            logger.debug("exit reSendOpenEmail method.takeTime:" + takeTime + "ms");
        }
        return null;
	}
	/**
	 * <修改云主机名称> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String updateVmName() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter updateVmName method.");			
		}
		User user = (User)super.getCurrentLoginUser();
		if(user==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String operateObject = "VM[vmId:" + id + "]";
		try {
			if(!facade.hasSameVmName(name)){
				facade.updateVmName(name, id, user.getId());
			}else{
				super.fillActionResult(Constants.NAME_HAD_HEEN_USED);
			}
			facade.insertOperationLog(user,"成功修改云主机名称","修改云主机名称",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"修改云主机名称出现错误:"+e.getMessage(),"修改云主机名称",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VM_UPDATENAME_ERROR, "updateVmName异常", logger, e), "000");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit updateVmName method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <查询云主机详情> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String findDetailVmById() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findDetailVmById method.");			
		}
		User user = (User)super.getCurrentLoginUser();
		if(user==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		InstanceDetailVo detailVo = new InstanceDetailVo();
		try {
			detailVo = facade.findDetailVmById(id);
		} catch (HsCloudException e) {
			dealThrow(new HsCloudException(Constants.VM_DETAIL_ERROR, "findDetailVmById异常", logger, e), "000");
		}
		super.fillActionResult(detailVo);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findDetailVmById method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <获取续费云主机订单详情> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String getRenewOrderInfo() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getRenewOrderInfo method.");			
		}
		RenewOrderVO roVO = null;
		try {
			User user = (User) super.getCurrentLoginUser();
			roVO = facade.getRenewOrderInfo(id,user);
		} catch (HsCloudException e) {
			dealThrow(new HsCloudException(Constants.VM_GET_RENEW_ORDER, "getRenewOrderInfo异常", logger, e), "000");
		}
		super.fillActionResult(roVO);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getRenewOrderInfo method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <云主机续费> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String renewFeeVM() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter renewFeeVM method.");			
		}
		User user = (User) super.getCurrentLoginUser();
		if(isCouponFormat(couponAmount) == false) {
            dealThrow(Constants.ORDER_COUPON_FORMAT_ERROR, new RuntimeException(), logger);
            return null;
        }
		if(isCouponFormat(giftAmount) == false) {
            dealThrow(Constants.GIFT_FORMAT_ERROR, new RuntimeException(), logger);
            return null;
        }
		String operateObject = "VM[vmId:" + id + "]";
		try {
			String result=facade.renewOrderV2(id, Long.valueOf(feeTypeId), user, couponAmount,giftAmount,planId);
			if(Constants.VM_RENEW_RESULT_FAIL.equals(result)){
				dealThrow(new HsCloudException("", "renewFeeVM异常", logger, null), Constants.VM_RENEW_ORDER,true);
			}else if(Constants.VM_RENEW_RESULT_NOFEE.equals(result)){
				dealThrow(new HsCloudException("", "renewFeeVM异常", logger, null),Constants.VM_RENEW_ORDER_AMOUNT,true);
			}
			facade.insertOperationLog(user,"云主机续费成功","云主机续费",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"云主机续费出现错误:"+e.getMessage(),"云主机续费",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException("", "renewFeeVM异常", logger, e),Constants.VM_RENEW_ORDER,true);
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit renewFeeVM method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <设置控制面板密码> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String setCPpwd() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter setCPpwd method.");			
		}
		User user = (User) super.getCurrentLoginUser();
		String operateObject = "VM[vmId:" + id + "]";
		try {
			facade.setCPpwd(id, CPpwd, user);
			facade.insertOperationLog(user,"成功设置控制面板密码","设置控制面板密码",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"设置控制面板密码出现错误:"+e.getMessage(),"设置控制面板密码",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VM_CPPWD_ERROR, "setCPpwd异常", logger, e), "000");
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit setCPpwd method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <取消试用待审核VM> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String cancelApplyTryVm(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter cancelApplyTryVm method.");			
		}
		User user = (User) super.getCurrentLoginUser();
		String operateObject = "VM[referenceId:" + referenceId + "]";
		try {
			facade.cancelApplyTryVm(user.getId(),referenceId);
			facade.insertOperationLog(user,"成功取消试用待审核云主机","取消试用云主机",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"取消试用待审核云主机出现错误:"+e.getMessage(),"取消试用云主机",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VM_APPLYDELAY_ERROR, "applyForDelayTryVm异常", logger, e), "000");
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit cancelApplyTryVm method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <试用VM延期申请> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String applyForDelayTryVm(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter applyForDelayTryVm method.");			
		}
		User user = (User) super.getCurrentLoginUser();
		String operateObject = "VM[referenceId:" + referenceId + "]";
		try {
			facade.applyForDelayTryVm(user.getId(),referenceId);
			facade.insertOperationLog(user,"成功受理试用云主机延期申请操作","试用云主机延期",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"试用云主机延期申请出现错误:"+e.getMessage(),"试用云主机延期",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VM_APPLYDELAY_ERROR, "applyForDelayTryVm异常", logger, e), "000");
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit applyForDelayTryVm method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <获取VM对应套餐的计费方式> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String getVMFeeType(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getVMFeeType method.");			
		}
		try {
			 if(scId == null || "".equals(scId) || "null".equals(scId)){
				 scId = "0";
			 }
			 List<ScFeeTypeVo> lst = facade.getScFeeTypeByScId(Integer.valueOf(scId));
			super.getActionResult().setResultObject(lst);
		} catch (HsCloudException e) {
			dealThrow(new HsCloudException(Constants.VM_REGULAR_ERROR, "regularTryVm异常", logger, e), "000");
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getVMFeeType method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <试用VM转正> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String regularTryVm(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter regularTryVm method.");			
		}
		User user = (User) super.getCurrentLoginUser();
		String operateObject = "VM[referenceId:" + referenceId + "]";
		try {
			String result=facade.toNormalV2(Long.valueOf(feeTypeId),referenceId, user, couponAmount,giftAmount);
			if(Constants.VM_RENEW_RESULT_FAIL.equals(result)){
				dealThrow(new HsCloudException("", "regularTryVm异常", logger, null),Constants.VM_REGULAR_ERROR,true);
			}else if(Constants.VM_RENEW_RESULT_NOFEE.equals(result)){
				dealThrow(new HsCloudException("", "regularTryVm异常", logger, null),Constants.VM_RENEW_ORDER_AMOUNT,true);
			}
			facade.insertOperationLog(user,"试用VM转正成功","试用VM转正",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"试用VM转正出现错误:"+e.getMessage(),"试用VM转正",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException("", "regularTryVm异常", logger, e),Constants.VM_REGULAR_ERROR,true);
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit regularTryVm method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	public String getRenewalVm(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getRenewalVm method.");			
		}
		int pageNo = 1;
		int pageSize = 3;
		if (StringUtils.isNumeric(page)) {
			pageNo = Integer.parseInt(page);
		}		
		if (StringUtils.isNumeric(limit)) {
			pageSize = Integer.parseInt(limit);
		}
		try {
			User user = (User) super.getCurrentLoginUser();
			if(query!=null && !"".equals(query)){
				query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
			}
			pageRenewal.setPageNo(pageNo);
			pageRenewal.setPageSize(pageSize);
			pageRenewal = facade.findVpdcRenewal(pageRenewal, "businessType", type, query, user.getId());
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "getRenewalVm Exception", logger, e),Constants.VM_LIST_USER_ERROR,true);
			return null;
		}
		super.fillActionResult(pageRenewal);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getRenewalVm method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <查询关于云主机操作的日志> 
	* <功能详细描述> 
	 */
	@SuppressWarnings("deprecation")
	public void pageVmOpsLog() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter pageVmOpsLog method.");
		}
		try {
		    LogQueryVO logQueryVO = new LogQueryVO();
			logQueryVO.setUuid(uuid);
			logQueryVO.setOps(ops);
			logQueryVO.setOperationType(1);
			if(!(startTime.equalsIgnoreCase("null") || startTime.equalsIgnoreCase(""))){
				logQueryVO.setStartTime(new Date(startTime));
			}
			if(!(endTime.equalsIgnoreCase("null") || endTime.equalsIgnoreCase(""))){
				logQueryVO.setEndTime(new Date(new Date(endTime).getTime() + 24*60*60*1000));
			}
			pageVmOpsLog = new Page<HcEventVmOps>(Integer.valueOf(limit));
			pageVmOpsLog.setPageNo(Integer.valueOf(page));
			Page<HcEventVmOps> log = facade.getVmOpsLog(pageVmOpsLog, logQueryVO);
			fillActionResult(log);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "pageVmOpsLog 异常", logger, e),"");
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit pageVmOpsLog method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * <用途: 获取当前用户的当前instanceId这台云主机的同一组下面所有已经添加内网安全的云主机
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public String getAddedIntranetSecurityVms() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAddedIntranetSecurityVms method.");			
		}
		try {
			vmIntra_list = facade.findIntranetVmsByUuid(uuid);
		}catch (HsCloudException e) {
			dealThrow(new HsCloudException(Constants.VM_LIST_USER_ERROR, "getAddedIntranetSecurityVms异常", logger, e), "000");
		}
		super.fillActionResult(vmIntra_list);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAddedIntranetSecurityVms method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	//是否符合返点格式
    private static boolean isCouponFormat(String str) {  
        String regex = "^(([1-9]{1}[0-9]*|[0-9]{1})(\\.[0-9]{1,2})|([1-9]{1}[0-9]*|[0]{1}))$";
        Pattern pattern = Pattern.compile(regex);  
        Matcher matcher = pattern.matcher(str);  
        return matcher.matches();  
    }  
	
	/**
	 * <用途: 提交内网安全和外网安全的数据 >
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public void submitSecurityPolicy() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter submitSecurityPolicy method.");
		}
		User user = (User) super.getCurrentLoginUser();
		String operateObject = "VM[vmId:" + uuid + "]";
		try {
			SecurityPolicyVO spVO = new SecurityPolicyVO(submit_instanceIds,
					submit_uuids, old_instanceIds, old_uuids, extranet_submit_data, 
					extranet_old_data, uuid, instanceId, groupId, user);
			boolean result = facade.submitSecurityPolicy(spVO);
			super.getActionResult().setResultObject(result);
			facade.insertOperationLog(user,"成功受理设置安全策略操作","设置安全策略",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"设置安全策略操作出现错误:"+e.getMessage(),"设置安全策略",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VM_LIST_USER_ERROR,
					"submitSecurityPolicy异常", logger, e), "000");
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit submitSecurityPolicy method.takeTime:"
					+ takeTime + "ms");
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
		boolean bl;
		User user = (User) super.getCurrentLoginUser();
		if (user == null) {
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String operateObject = "VM[vmId:" + id + "]";
		try {
			bl = facade.repairOSByVmId(id, user, LogOperatorType.USER.getName());
			facade.insertOperationLog(user,"成功受理主机修复操作","主机修复",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"主机修复操作出现错误:"+e.getMessage(),"主机修复",Constants.RESULT_FAILURE,operateObject);
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
	
	public String createVPDC(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter createVPDC method.");			
		}
		User user = (User) super.getCurrentLoginUser();
		if (user == null) {
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String operateObject = "CreateVpdcBean[name:" + name + "]";
		try {
			createVpdcBean = new CreateVpdcBean();
			createVpdcBean.setVpdcType(vpdcType);
			createVpdcBean.setZoneGroup(zoneGroup);
			createVpdcBean.setName(name);
			facade.createVPDC(createVpdcBean, user);
			facade.insertOperationLog(user,"成功受理创建VPDC操作","创建VPDC",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"创建VPDC操作出现错误:"+e.getMessage(),"创建VPDC",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VPDC_CREATE_ERROR, "createVPDC Exception:",
					logger, e), Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit createVPDC method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * 根据VPDC的id查询出VPDC信息> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public String getVpdcById() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getVpdcById method.");
		}
		VpdcVo vpdcVo = null;
		try {
			vpdcVo = facade.getVpdcById(vpdcId);
		}
		catch (HsCloudException e) {
			dealThrow(new HsCloudException(Constants.VPDC_DETAIL_ERROR, "getVpdcById异常", logger, e), "000");
		}
		super.fillActionResult(vpdcVo);
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getVpdcById method.takeTime:" + takeTime + "ms");
		}
		return null;
	}		
	
	/**
	 * <修改VPDC> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String updateVPDC(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter updateVPDC method.");			
		}
		User user = (User) super.getCurrentLoginUser();
		if (user == null) {
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String operateObject = "VPDC[vpdcId:" + vpdcId + "]";
		try {
			List<VlanNetwork> vlans = new ArrayList<VlanNetwork>();
			String[] dns1_array = class_dns1.split(",");
			String[] dns2_array = class_dns2.split(",");
			for (int i = 0; i < dns1_array.length; i++) {
				VlanNetwork vn = new CreateVpdcBean().new VlanNetwork();
				vn.setDns1(dns1_array[i]);
				vn.setDns2(dns2_array[i]);
				vlans.add(vn);
			}
			facade.updateVPDC(vpdcId, name, vlans, user.getId());
			facade.insertOperationLog(user,"成功受理修改VPDC操作","修改VPDC",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"修改VPDC操作出现错误:"+e.getMessage(),"修改VPDC",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VPDC_UPDATE_ERROR, "updateVPDC Exception:",
					logger, e), Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit updateVPDC method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <删除VPDC> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String deleteVPDC(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter deleteVPDC method.");			
		}
		User user = (User) super.getCurrentLoginUser();
		if (user == null) {
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String operateObject = "VPDC[vpdcId:" + vpdcId + "]";
		try {
			boolean result = facade.deleteVPDC(vpdcId);
			if (result == false) {
				super.fillActionResult(Constants.VPDC_HAS_UNRELEASED_RESOURCES);
			}
			facade.insertOperationLog(user,"成功受理删除VPDC操作","删除VPDC",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"删除VPDC操作出现错误:"+e.getMessage(),"删除VPDC",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VPDC_DELETE_ERROR, "deleteVPDC Exception:",
					logger, e), Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit deleteVPDC method.takeTime:" + takeTime + "ms");
		}
		return null;
	}		
	
	/**
	 * 根据VPDC的id查询出VpdcRouter信息> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public String getRouterByVpdcId() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getRouterByVpdcId method.");
		}
		VpdcRouterVo vrv = null;
		try {
			vrv = facade.getRouterByVpdcId(vpdcId);
		}
		catch (HsCloudException e) {
			dealThrow(new HsCloudException(Constants.ROUTER_DETAIL_ERROR, "getRouterByVpdcId异常", logger, e), "000");
		}
		super.fillActionResult(vrv);
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getRouterByVpdcId method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <分页获取User用户所有的VPDC(包含非路由模式和路由模式)> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public String findVpdcsByUser() throws UnsupportedEncodingException {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findVpdcsByUser method.");
		}
		User user = (User) super.getCurrentLoginUser();
		try {
			if (StringUtils.isNotEmpty(name)) {
				name = new String(name.getBytes("ISO-8859-1"), "UTF-8");
			}
			pageVpdc = facade.findVpdcsByUser(page, limit, pageVpdc, vpdcType, name, user);
		}
		catch (HsCloudException e) {
			dealThrow(new HsCloudException(Constants.VM_LIST_USER_ERROR, "findVpdcsByUser异常", logger, e), "000");
		}
		super.fillActionResult(pageVpdc);
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findVpdcsByUser method.takeTime:" + takeTime + "ms");
		}
		return null;
	}	
	
	/**
	 * <分页获取User用户某一个VPDC下面的所有的云主机> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public String getHostsByVpdcId() throws UnsupportedEncodingException {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getHostsByVpdcId method.");
		}
		User user = (User) super.getCurrentLoginUser();
		Long statusId = 0L;
		if (super.getSession().getAttribute("enUid") != null) {
			statusId = Long.valueOf(super.getSession().getAttribute("enUid").toString());
		}
		try {
			if (StringUtils.isNotEmpty(query)) {
				query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
			}
			pageIns = facade.getHostsByVpdcId(page, limit, pageIns, query, sort, user, statusId, 
					type, status_buss, vpdcId);
		}
		catch (HsCloudException e) {
			dealThrow(new HsCloudException(Constants.VM_LIST_USER_ERROR, "getHostsByVpdcId异常", logger, e), "000");
		}
		super.fillActionResult(pageIns);
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getHostsByVpdcId method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <获取云主机下的扩展盘> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String getAttachVolumesOfVm() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAttachVolumesOfVm method.");			
		}
		User user = (User) super.getCurrentLoginUser();
		if(user==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		Map<String ,Object> map = new HashMap<String, Object>();
		List<VolumeVO> volumeVOList = null; 
		try{
			volumeVOList = facade.getAttachVolumesOfVm(id);
			map.put("totalCount",volumeVOList==null?0:volumeVOList.size());
			map.put("result", volumeVOList);
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.OPTIONS_FAILURE,
					"getAttachVolumesOfVm Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		super.fillActionResult(map);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAttachVolumesOfVm method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <删除挂载盘> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String dettachVolume() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter dettachVolume method.");			
		}
		User user = (User) super.getCurrentLoginUser();
		if(user==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;
		String operateObject = "VM[vmId:" + id + "]";
		try {
			result = facade.dettachVolume(id, volumeId,volumeMode,(User) super.getCurrentLoginUser());
			if (result) {
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}
			facade.insertOperationLog(user,"成功受理删除扩展盘操作","删除扩展盘",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"删除扩展盘操作出现错误:"+e.getMessage(),"删除扩展盘",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.OPTIONS_FAILURE,
					"dettachVolume Exception:", logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit dettachVolume method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <添加云主机的扩展盘> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String attachVolume() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter attachVolume method.");			
		}
		User user = (User) super.getCurrentLoginUser();
		if(user==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;		
		String operateObject = "VM[vmId:" + id + "]";
		try {
			result = facade.attachVolume(id, addDisk,user);
			if (result) {
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}
			facade.insertOperationLog(user,"成功受理添加扩展盘操作","添加扩展盘",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"添加扩展盘操作出现错误:"+e.getMessage(),"添加扩展盘",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.OPTIONS_FAILURE,
					"attachVolume Exception:", logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit attachVolume method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <调整云主机flavor> <功能详细描述>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String resizeVm() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter resizeVm method.");			
		}
		User user = (User) super.getCurrentLoginUser();
		if(user==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;		
		String operateObject = "VM[vmId:" + id + "]";
		try {
			flavorVO = Utils.strutsJson2Object(FlavorVO.class);
			result = facade.resizeVm(id, flavorVO,addDisk,user);
			if (result) {
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}
			facade.insertOperationLog(user,"成功受理调整云主机flavor操作","调整云主机flavor",Constants.RESULT_SUCESS,operateObject);
		} catch (Exception e) {
			facade.insertOperationLog(user,"调整云主机flavor操作出现错误:"+e.getMessage(),"调整云主机flavor",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.VM_RESIZE_ERROR, "resizeVm Exception:",
					logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit resizeVm method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <修改云主机扩展盘大小> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String modifyVolume() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter modifyVolume method.");			
		}
		User user = (User) super.getCurrentLoginUser();
		if(user==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;
		String operateObject = "VM[vmId:" + id + "]";
		try {
			result = facade.modifyVolume(id, volumeId,volumeSize,user);
			if (result) {
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}
			facade.insertOperationLog(user,"成功受理修改扩展盘操作","修改扩展盘",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"修改扩展盘操作出现错误:"+e.getMessage(),"修改扩展盘",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.OPTIONS_FAILURE,
					"modifyVolume Exception:", logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit modifyVolume method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <启动路由器> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String startRouter(){
		long beginRunTime = 0;
		User user = (User)super.getCurrentLoginUser();
		if(user==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter startRouter method.");			
		}
		boolean result = false;
		String operateObject = "VpdcRouter[routerUUID:" + id + "]";
		try {
			result = facade.openRouter(id,user,LogOperatorType.USER.getName());
			facade.insertOperationLog(user,"成功受理启动路由器操作","启动路由器",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"启动路由器操作出现错误:"+e.getMessage(),"启动路由器",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.ROUTER_START_ERROR, "startRouter异常", logger, e), "000");
			return null;
		}
		if (result) {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		} else {
			super.fillActionResult(Constants.ROUTER_START_ERROR);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit startRouter method.takeTime:" + takeTime + "ms");
		}
		return null;
	}

	/**
	 * <关闭路由器> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String closeRouter() {
		long beginRunTime = 0;
		User user = (User)super.getCurrentLoginUser();
		if(user==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter closeRouter method.");			
		}
		boolean bl;
		String operateObject = "VpdcRouter[routerUUID:" + id + "]";
		try {
			bl = facade.closeRouter(id,user,LogOperatorType.USER.getName());
			facade.insertOperationLog(user,"成功受理关闭路由器操作","关闭路由器",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"关闭路由器操作出现错误:"+e.getMessage(),"关闭路由器",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.ROUTER_CLOSE_ERROR, "closeRouter异常", logger, e), "000");
			return null;
		}
		if (bl) {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		} else {
			super.fillActionResult(Constants.ROUTER_CLOSE_ERROR);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit closeRouter method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <重启路由器> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String rebootRouter() {
		long beginRunTime = 0;
		User user = (User)super.getCurrentLoginUser();
		if(user==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter rebootRouter method.");			
		}
		String result = null;
		String operateObject = "VpdcRouter[routerUUID:" + id + "]";
		try {
			result = facade.rebootRouter(id,user,LogOperatorType.USER.getName());
			facade.insertOperationLog(user,"成功受理重启路由器操作","重启路由器",Constants.RESULT_SUCESS,operateObject);
		} catch (HsCloudException e) {
			facade.insertOperationLog(user,"重启路由器操作出现错误:"+e.getMessage(),"重启路由器",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.ROUTER_REBOOT_ERROR, "rebootRouter异常", logger, e), "000");
			return null;
		}
		if (result != null) {
			logger.info("ERROR:" + result);
			super.fillActionResult(Constants.ROUTER_REBOOT_ERROR);
		} else {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit rebootRouter method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <查询出VrouterTemplate的第一条信息> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public String getVrouterTemplate() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getVrouterTemplate method.");
		}
		VrouterTemplateVO vtVO = null;
		try {
			vtVO = facade.getVrouterTemplate();
		}
		catch (HsCloudException e) {
			dealThrow(new HsCloudException("", "getVrouterTemplate异常", logger, e), "000");
		}
		super.fillActionResult(vtVO);
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getVrouterTemplate method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	public BackupsVmBean getBackupsVmBean() {
		return backupsVmBean;
	}

	public void setBackupsVmBean(BackupsVmBean backupsVmBean) {
		this.backupsVmBean = backupsVmBean;
	}

	public VmSnapShotPlan getVssp() {
		return vssp;
	}

	public void setVssp(VmSnapShotPlan vssp) {
		this.vssp = vssp;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public RenewVmBean getRenewVmBean() {
		return renewVmBean;
	}

	public void setRenewVmBean(RenewVmBean renewVmBean) {
		this.renewVmBean = renewVmBean;
	}
	
	public CreateVpdcBean getCreateVpdcBean() {
		return createVpdcBean;
	}

	public void setCreateVpdcBean(CreateVpdcBean createVpdcBean) {
		this.createVpdcBean = createVpdcBean;
	}

	public CreateVmBean getCreateVmBean() {
		return createVmBean;
	}

	public void setCreateVmBean(CreateVmBean createVmBean) {
		this.createVmBean = createVmBean;
	}

	public InstanceDetailVo getInstanceDetailVo() {
		return instanceDetailVo;
	}

	public void setInstanceDetailVo(InstanceDetailVo instanceDetailVo) {
		this.instanceDetailVo = instanceDetailVo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOsId() {
		return osId;
	}

	public void setOsId(String osId) {
		this.osId = osId;
	}

	public String getScId() {
		return scId;
	}

	public void setScId(String scId) {
		this.scId = scId;
	}

	public String getTerminateFlag() {
		return terminateFlag;
	}

	public void setTerminateFlag(String terminateFlag) {
		this.terminateFlag = terminateFlag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getVolumeId() {
		return volumeId;
	}

	public void setVolumeId(int volumeId) {
		this.volumeId = volumeId;
	}

	public int getVolumeMode() {
		return volumeMode;
	}

	public void setVolumeMode(int volumeMode) {
		this.volumeMode = volumeMode;
	}

	public int getVolumeSize() {
		return volumeSize;
	}

	public void setVolumeSize(int volumeSize) {
		this.volumeSize = volumeSize;
	}

	public String getAddDisk() {
		return addDisk;
	}

	public void setAddDisk(String addDisk) {
		this.addDisk = addDisk;
	}

	public FlavorVO getFlavorVO() {
		return flavorVO;
	}

	public void setFlavorVO(FlavorVO flavorVO) {
		this.flavorVO = flavorVO;
	}

	public String getStatus_buss() {
		return status_buss;
	}

	public void setStatus_buss(String status_buss) {
		this.status_buss = status_buss;
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

	public String getCPpwd() {
		return CPpwd;
	}

	public void setCPpwd(String cPpwd) {
		CPpwd = cPpwd;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(long referenceId) {
		this.referenceId = referenceId;
	}

	public String getFeeTypeId() {
		return feeTypeId;
	}

	public void setFeeTypeId(String feeTypeId) {
		this.feeTypeId = feeTypeId;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(String couponAmount) {
		this.couponAmount = couponAmount;
	}

	public Short getOps() {
		return ops;
	}

	public void setOps(Short ops) {
		this.ops = ops;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getSubmit_instanceIds() {
		return submit_instanceIds;
	}

	public void setSubmit_instanceIds(String submit_instanceIds) {
		this.submit_instanceIds = submit_instanceIds;
	}

	public String getSubmit_uuids() {
		return submit_uuids;
	}

	public void setSubmit_uuids(String submit_uuids) {
		this.submit_uuids = submit_uuids;
	}

	public String getOld_instanceIds() {
		return old_instanceIds;
	}

	public void setOld_instanceIds(String old_instanceIds) {
		this.old_instanceIds = old_instanceIds;
	}

	public String getOld_uuids() {
		return old_uuids;
	}

	public void setOld_uuids(String old_uuids) {
		this.old_uuids = old_uuids;
	}

	public String getExtranet_submit_data() {
		return extranet_submit_data;
	}

	public void setExtranet_submit_data(String extranet_submit_data) {
		this.extranet_submit_data = extranet_submit_data;
	}

	public String getExtranet_old_data() {
		return extranet_old_data;
	}

	public void setExtranet_old_data(String extranet_old_data) {
		this.extranet_old_data = extranet_old_data;
	}

	public List<VmExtranetSecurityVO> getVmExtra_list() {
		return vmExtra_list;
	}

	public void setVmExtra_list(List<VmExtranetSecurityVO> vmExtra_list) {
		this.vmExtra_list = vmExtra_list;
	}

	public List<VmIntranetSecurityVO> getVmIntra_list() {
		return vmIntra_list;
	}

	public void setVmIntra_list(List<VmIntranetSecurityVO> vmIntra_list) {
		this.vmIntra_list = vmIntra_list;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getResourceType() {
		return resourceType;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getVpdcType() {
		return vpdcType;
	}

	public void setVpdcType(int vpdcType) {
		this.vpdcType = vpdcType;
	}

	public long getZoneGroup() {
		return zoneGroup;
	}

	public void setZoneGroup(long zoneGroup) {
		this.zoneGroup = zoneGroup;
	}

	public long getVpdcId() {
		return vpdcId;
	}

	public void setVpdcId(long vpdcId) {
		this.vpdcId = vpdcId;
	}

	public String getClass_dns1() {
		return class_dns1;
	}

	public void setClass_dns1(String class_dns1) {
		this.class_dns1 = class_dns1;
	}

	public String getClass_dns2() {
		return class_dns2;
	}

	public void setClass_dns2(String class_dns2) {
		this.class_dns2 = class_dns2;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getGiftAmount() {
		return giftAmount;
	}

	public void setGiftAmount(String giftAmount) {
		this.giftAmount = giftAmount;
	}
	
}
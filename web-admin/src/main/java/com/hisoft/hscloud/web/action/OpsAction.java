/**
 * @title UserAction.java
 * @package com.hisoft.hscloud.vpdc.ops.action
 * @description 用一句话描述该文件做什么
 * @author hongqin.li
 * @update 2012-3-30 上午11:44:16
 * @version V1.0
 */
package com.hisoft.hscloud.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.entity.LogOperatorType;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.vpdc.ops.entity.VmSnapShotPlan;
import com.hisoft.hscloud.vpdc.ops.json.bean.BackupsVmBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVmBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.NetWorkBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.RenewVmBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.ResourceLimit;
import com.hisoft.hscloud.vpdc.ops.json.bean.UserBean;
import com.hisoft.hscloud.vpdc.ops.util.Utils;
import com.hisoft.hscloud.vpdc.ops.vo.FlavorVO;
import com.hisoft.hscloud.vpdc.ops.vo.InstanceDetailVo;
import com.hisoft.hscloud.vpdc.ops.vo.VmInfoVO;
import com.hisoft.hscloud.vpdc.ops.vo.VmSnapShotVO;
import com.hisoft.hscloud.vpdc.ops.vo.VncClientVO;
import com.hisoft.hscloud.vpdc.ops.vo.VolumeVO;
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
	private Page<VmInfoVO> vmInfoVO = new Page<VmInfoVO>();
	private BackupsVmBean backupsVmBean;
	private VmSnapShotPlan vssp;
	private UserBean userBean;
	private RenewVmBean renewVmBean;
	private CreateVmBean createVmBean;
	private InstanceDetailVo instanceDetailVo;
	private Long referenceId = 0L;
	private String id;
	private String terminateFlag;// 1:同时删除配置reference;null或者0只删除instance
	private int page;
	private int limit;
	private String start;
	private String type;// 查询类型
	private String query;// 模糊查询条件
	private String vmType;//VM的类型（0:试用;正式:1）
	private String status_buss;//VM的业务状态（0:试用待审;1：试用中；2：延期待审；3：已延期；4：正式）
	private String sort;//倒序排序（0:开通时间；1：到期时间；2：申请时间）
	private String name;
	private String sn_name;// snapshot 名字
	private String sn_comments;// snapshot 备注
	private int sn_type;// snapshot 备份类型（0:手动、1:自动）
	private String ssid;// snapshotId
	private String planTime;
	private FlavorVO flavorVO;
	private String addDisk;//扩展盘
	private String oldIP;// 原来的IP
	private String newIP;// 新的IP
	private String confirmFlag;// 确认迁移标示
	private int volumeId;//扩展盘Id
	private long owner;//云主机所有者
	private String email;//用户邮箱
	private String hostName;//节点名称
	private String comments;//内部备注信息
	private String outComments;//外部备注信息
	private int delayLong;//延长时间（天）
	private int volumeMode;//扩展盘挂载方式：//0:无-1：isics方式扩展盘-2：SCIS方式扩展盘
	private int volumeSize;//扩展盘大小
	private String password;
	private String vmId;
	private final String resourceType = "com.hisoft.hscloud.vpdc.ops.entity.VpdcReference";
	private String zoneCode;
	private String remark;
	private String code;//验证码
	private int scId;//套餐Id
	private ResourceLimit resourceLimit;
	private String[] newIPs;// 新的IP
	public String getResourceType() {
		return resourceType;
	}
	
	/**
	 * 创建外网管理的WanNetwork（网段）
	 * @return
	 */
	public String createWanNetwork(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter createWanNetwork method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		NetWorkBean netWork = new NetWorkBean();
		//test
		netWork.setCidr("192.168.2.0/27");
		netWork.setDns1("114.114.114.114");
		netWork.setGateway("192.168.2.254");
		netWork.setZoneGroup(1l);
		try {
			facade.createWanNetwork(netWork,admin.getId());
		} catch (Exception e) {
			dealThrow(new HsCloudException(Constants.VPDC_WANNETWORK_ERROR,
					"createWanNetwork(Admin) Exception:", logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit createWanNetwork method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <admin用户创建云主机>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String createVm() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter createVm method.");			
		}
		CreateVmBean cvb = null;
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		try {
			cvb = Utils.strutsJson2Object(CreateVmBean.class);
			if (facade.hasSameVmName(cvb.getName())) {
				super.fillActionResult(Constants.NAME_HAD_HEEN_USED);
			} else {
				String vmId = facade.createVM(cvb, admin.getId());
				if (vmId != null) {
					super.fillActionResult((Object) vmId);
				} else {
					super.fillActionResult(Constants.VM_CREATE_ADMIN_ERROR);
				}
			}
			facade.insertOperationLog(admin,"成功受理创建云主机操作","创建云主机",Constants.RESULT_SUCESS);
		} catch (Exception e) {
			facade.insertOperationLog(admin,"创建云主机操作错误:"+e.getMessage(),"创建云主机",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_CREATE_ADMIN_ERROR,
					"createVm(Admin) Exception:", logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit createVm method.takeTime:" + takeTime + "ms");
		}
		return null;
	}

	/**
	 * <启动云主机>
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
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		try {
			result = facade.startVmByVmId(id,admin,LogOperatorType.ADMIN.getName());
			facade.insertOperationLog(admin,"成功受理启动云主机操作","启动云主机",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"启动云主机操作错误:"+e.getMessage(),"启动云主机",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_START_ERROR, "startVM Exception:",
					logger, e), Constants.OPTIONS_FAILURE);
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
	 * <重启云主机>
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
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		try {
			result = facade.rebootVmByVmId(id,admin,LogOperatorType.ADMIN.getName());
			facade.insertOperationLog(admin,"成功受理重启云主机操作","重启云主机",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"重启云主机操作错误:"+e.getMessage(),"重启云主机",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_REBOOT_ERROR, "rebootVM Exception:",
					logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if (result != null) {
			logger.error("ERROR:" + result);
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
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		try {
			bl = facade.closeVmByVmId(id,admin,LogOperatorType.ADMIN.getName());
			facade.insertOperationLog(admin,"成功受理关闭云主机操作","关闭云主机",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"关闭云主机操作错误:"+e.getMessage(),"关闭云主机",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_CLOSE_ERROR, "suspendVM Exception:",
					logger, e), Constants.OPTIONS_FAILURE);
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
	 * 主机修复
	 * @return
	 */
	public String repairOS() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter repairOS method.");			
		}
		boolean bl;
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		try {
			bl = facade.repairOSByVmId(id,admin,LogOperatorType.ADMIN.getName());
			facade.insertOperationLog(admin,"成功受理主机修复操作","主机修复",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"主机修复操作错误:"+e.getMessage(),"主机修复",Constants.RESULT_FAILURE);
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
	
	/**
	 * 回收站恢复VM
	 * @return
	 */
	public String recycleRestore() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter recycleRestore method.");			
		}
		boolean bl;
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		try {
			bl = facade.recycleRestore(id,admin);
			facade.insertOperationLog(admin,"成功受理回收站恢复云主机操作","恢复云主机",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"回收站恢复云主机操作错误:"+e.getMessage(),"恢复云主机",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_RECYCLE_RESTORE_ERROR, "recycleRestore Exception:",
					logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if (bl) {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		} else {
			super.fillActionResult(Constants.VM_RECYCLE_RESTORE_ERROR);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit recycleRestore method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * 回收站终止VM
	 * @return
	 */
	public String recycleTerminate() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter recycleTerminate method.");			
		}
		boolean bl;
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		try {
			bl = facade.recycleTerminate(id,admin);
			facade.insertOperationLog(admin,"成功受理回收站终止云主机操作","终止云主机",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"回收站终止云主机操作错误:"+e.getMessage(),"终止云主机",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_RECYCLE_DELETE_ERROR, "recycleTerminate Exception:",
					logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if (bl) {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		} else {
			super.fillActionResult(Constants.VM_RECYCLE_DELETE_ERROR);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit recycleTerminate method.takeTime:" + takeTime + "ms");
		}
		return null;
	}

	/**
	 * <唤醒云主机>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	/*public String resumeVm() {
		logger.info("OPS-Action-resumeVm start");
		String result = null;
		try {
			result = facade.resumeVmByVmId(id);
		} catch (HsCloudException e) {
			dealThrow(new HsCloudException(Constants.VM_RESUME_ERROR, "resumeVmByVmId异常",
					logger, e), "000");
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
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		try {
			vncUrl = facade.getVNCUrl(id);
			facade.insertOperationLog(admin,"成功受理获取VNC远程控制操作","VNC远程控制",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"获取VNC远程控制操作错误:"+e.getMessage(),"VNC远程控制",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_VNC_ERROR, "getVNC Exception:", logger,
					e), Constants.OPTIONS_FAILURE);
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
	 * <备份云主机>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String backupVM() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter backupVM method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String result = null;
		try {
			result = facade.backupVmByVmId(id, sn_name,
					sn_comments, sn_type,admin,LogOperatorType.ADMIN.getName());
			facade.insertOperationLog(admin,"成功受理备份云主机操作","备份云主机",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"备份云主机操作错误:"+e.getMessage(),"备份云主机",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_BACKUP_ERROR, "backupsVm Exception:",
					logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if (result != null) {
			super.fillActionResult(Constants.VM_BACKUP_ERROR);
		} else {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit backupVM method.takeTime:" + takeTime + "ms");
		}
		return null;
	}

	/**
	 * <还原云主机>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String renewVm() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter renewVm method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String result = null;
		try {
			result = facade.renewVM(id, ssid,admin,LogOperatorType.ADMIN.getName());
			facade.insertOperationLog(admin,"成功受理还原云主机操作","还原云主机",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"还原云主机操作错误:"+e.getMessage(),"还原云主机",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_RENEW_ERROR, "renewVm Exception:",
					logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if (result != null) {
			super.fillActionResult(Constants.VM_RENEW_ERROR);
		} else {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit renewVm method.takeTime:" + takeTime + "ms");
		}
		return null;
	}

	/**
	 * <获取所有的备份记录>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String findAllBackups() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findAllBackups method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
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
			return null;
		} catch (HsCloudException e) {
			dealThrow(new HsCloudException(Constants.VM_SNAPSHOT_LIST_ERROR, "findAllBackups Exception:",
					logger, e), Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findAllBackups method.takeTime:" + takeTime + "ms");
		}
		return null;
	}

	/**
	 * <删除云主机>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String terminateVm() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter terminateVm method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;
		try {
			result = facade.terminateVmByVmId(id,name, terminateFlag,admin.getId());
			if (result) {
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}
			facade.insertOperationLog(admin,"成功受理删除云主机操作","删除云主机",Constants.RESULT_SUCESS);
		} catch (Exception e) {
			facade.insertOperationLog(admin,"删除云主机操作错误:"+e.getMessage(),"删除云主机",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_TERMINATE_ERROR, "terminateVm Exception:",
					logger, e), Constants.OPTIONS_FAILURE);
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
	 * <修改云主机名称>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String updateVmName() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter updateVmName method.");			
		}
		boolean result = false;
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		try {
			if (facade.hasSameVmName(name)) {
				super.fillActionResult(Constants.NAME_HAD_HEEN_USED);
			} else {
				result = facade.updateVmName(name, id, admin.getId());
			}
			if (result) {
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}
			facade.insertOperationLog(admin,"成功修改云主机名称","修改云主机名称",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"修改云主机名称操作错误:"+e.getMessage(),"修改云主机名称",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_UPDATENAME_ERROR, "updateVmName Exception:",
					logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit updateVmName method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <修改云主机备注>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String updateComments() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter updateComments method.");			
		}
		boolean result = false;
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		try {
			
			result = facade.updateComments(comments,outComments, id, admin.getId());
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"修改云主机备注操作错误:"+e.getMessage(),"修改云主机备注",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_UPDATENAME_ERROR, "updateVmName Exception:",
					logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit updateComments method.takeTime:" + takeTime + "ms");
		}
		return null;
	}

	/**
	 * <查询云主机详情>
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
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		InstanceDetailVo detailVo = new InstanceDetailVo();
		try {
			detailVo = facade.findDetailVmById(id);
			if (detailVo == null) {// 因为openstaic没有得到vm
				super.fillActionResult(Constants.VM_DETAIL_ERROR);
			}
		} catch (HsCloudException e) {
			dealThrow(new HsCloudException(Constants.VM_DETAIL_ERROR,
					"findDetailVmById Exception:", logger, e), Constants.OPTIONS_FAILURE);
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
	 * <查询试用待审核云主机详情>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String getTryVmInfo() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getTryVmInfo method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		InstanceDetailVo detailVo = new InstanceDetailVo();
		try {
			detailVo = facade.getTryVmInfo(referenceId);
			if (detailVo == null) {// 因为openstaic没有得到vm
				super.fillActionResult(Constants.VM_DETAIL_ERROR);
			}
		} catch (HsCloudException e) {
			dealThrow(new HsCloudException(Constants.VM_DETAIL_ERROR,
					"findDetailVmById Exception:", logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		super.fillActionResult(detailVo);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getTryVmInfo method.takeTime:" + takeTime + "ms");
		}
		return null;
	}

	/**
	 * <自动迁移云主机> <功能详细描述>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String autoMigrateVm() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter autoMigrateVm method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;
		try {
			result = facade.autoMigrateVm(id);
			if (result) {
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}
			facade.insertOperationLog(admin,"成功受理自动迁移云主机操作","自动迁移云主机",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"自动迁移云主机操作错误:"+e.getMessage(),"自动迁移云主机",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_AUTO_MIGRATE_ERROR, "autoMigrateVm Exception:",
					logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit autoMigrateVm method.takeTime:" + takeTime + "ms");
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
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;		
		try {
			flavorVO = Utils.strutsJson2Object(FlavorVO.class);
			result = facade.resizeVm(id, flavorVO,addDisk,admin);
			if (result) {
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}
			facade.insertOperationLog(admin,"成功受理调整云主机Flavor操作","调整云主机Flavor",Constants.RESULT_SUCESS);
		} catch (Exception e) {
			facade.insertOperationLog(admin,"调整云主机Flavor操作错误:"+e.getMessage(),"调整云主机Flavor",Constants.RESULT_FAILURE);
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
	 * <调整云主机确认> <功能详细描述>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String confirmResizeVm() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter confirmResizeVm method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;
		boolean flag =false;
		if(Constants.VM_RESIZE_CONFIRM.equals(confirmFlag)){
			flag = true;
		}
		try {
			result = facade.confirmResizeVm(id,flag);
			if (result) {
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}
			facade.insertOperationLog(admin,"确认调整云主机成功","确认调整云主机",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"确认调整云主机操作错误:"+e.getMessage(),"确认调整云主机",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_CONFIRM_RESIZE_ERROR,
					"confirmResizeVm Exception:", logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit confirmResizeVm method.takeTime:" + takeTime + "ms");
		}
		return null;
	}

	/**
	 * <迁移云主机确认> <功能详细描述>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String confirmMigrateVm() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter confirmMigrateVm method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;
		boolean flag =false;
		if(Constants.VM_RESIZE_CONFIRM.equals(confirmFlag)){
			flag = true;
		}
		try {
			result = facade.confirmMigrateVm(id,flag);
			if (result) {
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}
			facade.insertOperationLog(admin,"确认迁移云主机成功","确认迁移云主机",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"确认迁移云主机出现操作错误:"+e.getMessage(),"确认迁移云主机",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_CONFIRM_MIGRATE_ERROR,
					"confirmMigrateVm Exception:", logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit confirmMigrateVm method.takeTime:" + takeTime + "ms");
		}
		return null;
	}

	/**
	 * <修改VM name> <功能详细描述>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String updateHostNameOfVm() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter updateHostNameOfVm method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;
		try {
			result = facade.updateHostNameOfVm(id);
			if (result) {
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}
			facade.insertOperationLog(admin,"成功修改云主机名称","修改云主机名称",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"修改云主机名称操作错误:"+e.getMessage(),"修改云主机名称",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.SC_UPDATE_NODENAME_ERROR,
					"updateHostNameOfVm Exception:", logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit updateHostNameOfVm method.takeTime:" + takeTime + "ms");
		}
		return null;
	}

	/**
	 * <发布云主机>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String publishVm() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter publishVm method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		String uuid = null;
		try {
			uuid = facade.publishVm(name, admin.getId());
			facade.insertOperationLog(admin,"成功受理发布云主机操作","发布云主机",Constants.RESULT_SUCESS);
		} catch (Exception e) {
			facade.insertOperationLog(admin,"发布云主机操作错误:"+e.getMessage(),"发布云主机",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_PUBLISH_ERROR, "publishVm Exception:",
					logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if(uuid!=null){
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		}else{
			super.fillActionResult(Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit publishVm method.takeTime:" + takeTime + "ms");
		}
		return null;
	}

	/**
	 * <删除绑定IP或修改绑定IP> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String updateIPOfVm() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter updateIPOfVm method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;	
//		long newip=0;
		try {
			result = facade.updateIPOfVm(id, oldIP, newIP,admin.getId());
			if (result) {
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}
			facade.insertOperationLog(admin,"成功受理更新云主机绑定IP操作","更新云主机IP",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"更新云主机绑定IP操作错误:"+e.getMessage(),"更新云主机IP",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.OPTIONS_FAILURE,
					"updateIPOfVm Exception:", logger, e), Constants.OPTIONS_FAILURE);
			return null;
				}
//			newip = IPConvert.getIntegerIP(newIP);
//			IPDetail ipDetail = facade.getIPDetailByIP(newip);
//			//如果IP池里存在此IP，再查询该IP是否未使用；如果IP池中无此IP，则创建
//			if(ipDetail != null){
//				if(ipDetail.getStatus()!=0){
//					super.fillActionResult(Constants.IP_USED);
//					return null;
//				}
//			}else{
//				facade.findIPDetailByIP(newip, newip);
//			}	
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit updateIPOfVm method.takeTime:" + takeTime + "ms");
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
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
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
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;		
		try {
			result = facade.dettachVolume(id, volumeId,volumeMode,(Admin) super.getCurrentLoginUser());
			if (result) {
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}
			facade.insertOperationLog(admin,"成功受理卸载磁盘操作","卸载磁盘",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"卸载磁盘操作错误:"+e.getMessage(),"卸载磁盘",Constants.RESULT_FAILURE);
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
	 * <冻结云主机> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String freezeVM(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter freezeVM method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;	
		try {
			result = facade.freezeVM(id, admin.getId());
			if (result) {
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}
			facade.insertOperationLog(admin,"成功受理冻结云主机操作","冻结云主机",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"冻结云主机操作错误:"+e.getMessage(),"冻结云主机",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_FREEZE_ERROR,
					"freezeVM Exception:", logger, e), Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit freezeVM method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <启用云主机> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String activeVM(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter activeVM method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;	
		try {
			result = facade.activeVM(id, admin.getId());
			if (result) {
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}
			facade.insertOperationLog(admin,"成功受理启用云主机操作","启用云主机",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"启用云主机操作错误:"+e.getMessage(),"启用云主机",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_ACTIVE_ERROR,
					"activeVM Exception:", logger, e), Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit activeVM method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <调整云主机的所有者> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String updateVmOwner() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter updateVmOwner method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;
		try {			
			result = facade.updateVmOwmer(owner, id, admin.getId());
			if (result) {
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}
			facade.insertOperationLog(admin,"成功变更云主机的所有者","变更云主机所有者",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"变更云主机所有者操作错误:"+e.getMessage(),"变更云主机所有者",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_UPDATE_VMOWNER_EXCEPTION, "updateVmOwner Exception:",
					logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit updateVmOwner method.takeTime:" + takeTime + "ms");
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
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;		
		try {
			result = facade.attachVolume(id, addDisk,admin);
			if (result) {
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}
			facade.insertOperationLog(admin,"成功受理云主机添加扩展盘操作","云主机添加扩展盘",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"云主机添加扩展盘操作错误:"+e.getMessage(),"云主机添加扩展盘",Constants.RESULT_FAILURE);
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
	 * 手动迁移云主机
	 */
	public String manualMigrateVm() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter manualMigrateVm method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;
		try {
			result = facade.manualMigrateVm(id, hostName,admin);
			if (result) {
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}
			facade.insertOperationLog(admin,"成功受理手动迁移云主机操作","手动迁移云主机",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"手动迁移云主机操作操作错误:"+e.getMessage(),"手动迁移云主机",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_AUTO_MIGRATE_ERROR, "manualMigrateVm Exception:",
					logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit manualMigrateVm method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <试用VM审核> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String verifyForTryVm(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter verifyForTryVm method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		try {
			String vmid = facade.verifForTryVm(referenceId,admin.getId(),zoneCode,remark);
			if(vmid==null){
				dealThrow(new HsCloudException(Constants.VM_VERIFYTRY_ERROR, "verifyForTryVm Exception:", logger, null), Constants.OPTIONS_FAILURE);
			}
			facade.insertOperationLog(admin,"审核试用云主机成功","审核试用云主机",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"审核试用云主机操作错误:"+e.getMessage(),"审核试用云主机",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_VERIFYTRY_ERROR, "verifyForTryVm Exception:", logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit verifyForTryVm method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <延期试用VM审核> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String verifyForDelayTryVm(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter verifyForDelayTryVm method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		try {
			facade.verifyForDelayTryVm(referenceId,delayLong,admin.getId());
			facade.insertOperationLog(admin,"试用云主机延期成功","试用云主机延期",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"试用云主机延期操作错误:"+e.getMessage(),"试用云主机延期",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_VERIFYDELAY_ERROR, "verifyForDelayTryVm Exception:", logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit verifyForDelayTryVm method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <额外延期试用VM> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String extraDelayTryVm(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter extraDelayTryVm method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		try {
			facade.extraDelayTryVm(referenceId,delayLong,admin.getId());
			facade.insertOperationLog(admin,"试用云主机额外延期成功","试用云主机额外延期",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"试用云主机额外延期操作错误:"+e.getMessage(),"试用云主机额外延期",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_EXTRADELAY_ERROR, "extraDelayTryVm Exception:", logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit extraDelayTryVm method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <后台管理员为正式云主机延期> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String delayRegularVm(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter delayRegularVm method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		try {
			facade.delayRegularVm(referenceId,delayLong,admin.getId());
			facade.insertOperationLog(admin,"正式云主机延期成功","正式云主机延期",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"正式云主机延期操作错误:"+e.getMessage(),"正式云主机延期",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_VERIFYDELAY_ERROR, "delayRegularVm Exception:", logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit delayRegularVm method.takeTime:" + takeTime + "ms");
		}
		return null;
	}	
	/**
	 * <拒绝通过试用待审核VM> 
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
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		try {
			facade.cancelApplyTryVm(referenceId, comments, admin.getId());
			facade.insertOperationLog(admin,"拒绝试用待审核云主机成功","拒绝试用云主机",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"拒绝试用待审核云主机操作错误:"+e.getMessage(),"拒绝试用云主机",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_EXTRADELAY_ERROR, "extraDelayTryVm Exception:", logger, e), Constants.OPTIONS_FAILURE);
			return null;
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit cancelApplyTryVm method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <业务获取VM列表> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String listVMForBussiness(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter listVMForBussiness method.");			
		}
		int pageNo=page;
		int pageSize=limit;	
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		List<Object> referenceIds = super.getPrimKeys();
		try{
			if(query!=null && !"".equals(query)){
				query = new String(query.getBytes("iso8859_1"), "UTF-8");
			}
			vmInfoVO.setPageNo(pageNo);
			vmInfoVO.setPageSize(pageSize);
			vmInfoVO = facade.listVMForBusiness(vmInfoVO, type, query, 
					admin, referenceIds,vmType,status_buss,sort);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_VM_DETAILS_EXCEPTION,
					"findVmDetails Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		super.fillActionResult(vmInfoVO);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit listVMForBussiness method.takeTime:" + takeTime + "ms");
		}
		return null;	
	}
	
	/**
	 * <回收站获取VM列表> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String listVMForRecycle(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter listVMForRecycle method.");			
		}
		int pageNo=page;
		int pageSize=limit;	
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		List<Object> referenceIds = super.getPrimKeys();
		boolean isSpecial = facade.isSpecialAdmin(admin);
		if(!isSpecial){
			referenceIds = facade.getZoneIdsByAdminId(admin.getId());
		}
		try{
			if(query!=null && !"".equals(query)){
				query = new String(query.getBytes("iso8859_1"), "UTF-8");
			}
			vmInfoVO.setPageNo(pageNo);
			vmInfoVO.setPageSize(pageSize);
			vmInfoVO = facade.listVMForRecycle(vmInfoVO, type, query, 
					admin, referenceIds,vmType,status_buss,sort);			
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_VM_DETAILS_EXCEPTION,
					"listVMForRecycle Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		super.fillActionResult(vmInfoVO);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit listVMForRecycle method.takeTime:" + takeTime + "ms");
		}
		return null;	
	}
	
	/**
	 * <重发vm开通邮件> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public String reSendOpenEmail(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter reSendOpenEmail method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		try {
			facade.reSendOpenEmail(name,newIP,email,vmId);
			facade.insertOperationLog(admin,"重发云主机开通邮件成功","重发云主机开通邮件",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"重发云主机开通邮件操作错误:"+e.getMessage(),"重发云主机开通邮件",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException("", "reSendOpenEmail Exception:", logger, e),Constants.VM_RESEND_OPEN_EMAIL_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit reSendOpenEmail method.takeTime:" + takeTime + "ms");
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
        Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
        try {
            facade.resetSystemPwd(vmId, password,admin,LogOperatorType.ADMIN.getName());
			facade.insertOperationLog(admin,"成功受理重置云主机密码操作","重置云主机密码",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"重置云主机密码操作错误:"+e.getMessage(),"重置云主机密码",Constants.RESULT_FAILURE);
            dealThrow(new HsCloudException("", "resetPassword Exception:", logger, e),Constants.VM_RESET_PASSWORD_ERROR,true);
        }
        if(logger.isDebugEnabled()){
            long takeTime = System.currentTimeMillis() - beginRunTime;
            logger.debug("exit reSendOpenEmail method.takeTime:" + takeTime + "ms");
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
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;		
		try {
			result = facade.modifyVolume(id, volumeId,volumeSize,admin);
			if (result) {
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}
			facade.insertOperationLog(admin,"成功受理磁盘扩容操作","磁盘扩容",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"磁盘扩容操作错误:"+e.getMessage(),"磁盘扩容",Constants.RESULT_FAILURE);
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
	 * <变更云主机关联的套餐> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void changeVMSC(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter changeVMSC method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		try{
			facade.changeVMSC(referenceId, scId);
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
			facade.insertOperationLog(admin,"变更云主机关联套餐成功","变更云主机关联套餐",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"变更云主机关联套餐操作错误:"+e.getMessage(),"变更云主机关联套餐",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException("", "changeVMSC Exception:",
					logger, e),Constants.VM_CHANGE_SC_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit changeVMSC method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * < 更改vm资源配置>
	 * @author jianwei zhang
	 * @return
	 */
	public String changeVMResourceLimit() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter changeVMResourceLimit method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		try {
			ResourceLimit resourceLimit = Utils.strutsJson2Object(ResourceLimit.class);
			facade.changeVMResourceLimit(resourceLimit,id);
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
			facade.insertOperationLog(admin,"更新云主机资源限置成功","更新云主机资源限置",Constants.RESULT_SUCESS);
		} catch (Exception e) {
			facade.insertOperationLog(admin,"更新云主机资源限置操作错误:"+e.getMessage(),"更新云主机资源限置",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.OPTIONS_FAILURE,
					"modifyVolume Exception:", logger, e), Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit createVm method.takeTime:" + takeTime + "ms");
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
		VncClientVO vnc = null;
		int exceptionType = 0;//0:验证码操作错误 1:获取远程连接参数异常
		try {
			//检查验证码是否正确
			String sessionCode = (String)ServletActionContext.getRequest().getSession().getAttribute("code");
			if(null == this.code || "".equals(this.code)){
				fillActionResult(Constants.VERCODE_IS_NULL);
			}else if( null == sessionCode || !sessionCode.equalsIgnoreCase(this.code)){
				fillActionResult(Constants.VERCODE_IS_ERROR);
			}else{
				exceptionType = 1;
				vnc = facade.getClientVnc(id);
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
	 * <删除绑定IP> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String deleteIPOfVm() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter deleteIPOfVm method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;	
//		long newip=0;
		try {
			result = facade.deleteIPOfVm(id, oldIP,admin.getId());
			if (result) {
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}
			facade.insertOperationLog(admin,"发送受理删除云主机IP操作","删除云主机IP",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"删除云主机IP操作错误:"+e.getMessage(),"删除云主机IP",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.OPTIONS_FAILURE,
					"deleteIPOfVm Exception:", logger, e), Constants.OPTIONS_FAILURE);
			return null;
				}	
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit deleteIPOfVm method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <增加IP> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String addIPOfVm() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter addIPOfVm method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;	
//		long newip=0;
		try {
			result = facade.addIPOfVm(id, newIP, admin.getId());
			if (result) {
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}
			facade.insertOperationLog(admin,"成功受理云主机添加IP操作","云主机添加IP",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"云主机添加IP操作错误:"+e.getMessage(),"云主机添加IP",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.OPTIONS_FAILURE,
					"addIPOfVm Exception:", logger, e), Constants.OPTIONS_FAILURE);
			return null;
				}	
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit addIPOfVm method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * @return 返回 terminateFlag
	 */
	public String getTerminateFlag() {
		return terminateFlag;
	}

	/**
	 * @param 对terminateFlag进行赋值
	 */
	public void setTerminateFlag(String terminateFlag) {
		this.terminateFlag = terminateFlag;
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

	public Page<VmInfoVO> getVmInfoVO() {
		return vmInfoVO;
	}

	public void setVmInfoVO(Page<VmInfoVO> vmInfoVO) {
		this.vmInfoVO = vmInfoVO;
	}

	/**
	 * @return 返回 backupsVmBean
	 */
	public BackupsVmBean getBackupsVmBean() {
		return backupsVmBean;
	}

	/**
	 * @param 对backupsVmBean进行赋值
	 */
	public void setBackupsVmBean(BackupsVmBean backupsVmBean) {
		this.backupsVmBean = backupsVmBean;
	}

	/**
	 * @return 返回 vssp
	 */
	public VmSnapShotPlan getVssp() {
		return vssp;
	}

	/**
	 * @param 对vssp进行赋值
	 */
	public void setVssp(VmSnapShotPlan vssp) {
		this.vssp = vssp;
	}

	/**
	 * @return 返回 userBean
	 */
	public UserBean getUserBean() {
		return userBean;
	}

	/**
	 * @param 对userBean进行赋值
	 */
	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	/**
	 * @return 返回 renewVmBean
	 */
	public RenewVmBean getRenewVmBean() {
		return renewVmBean;
	}

	/**
	 * @param 对renewVmBean进行赋值
	 */
	public void setRenewVmBean(RenewVmBean renewVmBean) {
		this.renewVmBean = renewVmBean;
	}

	/**
	 * @return 返回 createVmBean
	 */
	public CreateVmBean getCreateVmBean() {
		return createVmBean;
	}

	/**
	 * @param 对createVmBean进行赋值
	 */
	public void setCreateVmBean(CreateVmBean createVmBean) {
		this.createVmBean = createVmBean;
	}

	/**
	 * @return 返回 instanceDetailVo
	 */
	public InstanceDetailVo getInstanceDetailVo() {
		return instanceDetailVo;
	}

	/**
	 * @param 对instanceDetailVo进行赋值
	 */
	public void setInstanceDetailVo(InstanceDetailVo instanceDetailVo) {
		this.instanceDetailVo = instanceDetailVo;
	}

	/**
	 * @return 返回 id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param 对id进行赋值
	 */
	public void setId(String id) {
		this.id = id;
	}


	/**
	 * @return 返回 start
	 */
	public String getStart() {
		return start;
	}

	/**
	 * @param 对start进行赋值
	 */
	public void setStart(String start) {
		this.start = start;
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

	/**
	 * @return 返回 query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param 对query进行赋值
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * @return 返回 name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param 对name进行赋值
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 返回 sn_name
	 */
	public String getSn_name() {
		return sn_name;
	}

	/**
	 * @param 对sn_name进行赋值
	 */
	public void setSn_name(String sn_name) {
		this.sn_name = sn_name;
	}

	/**
	 * @return 返回 sn_comments
	 */
	public String getSn_comments() {
		return sn_comments;
	}

	/**
	 * @param 对sn_comments进行赋值
	 */
	public void setSn_comments(String sn_comments) {
		this.sn_comments = sn_comments;
	}

	/**
	 * @return 返回 sn_type
	 */
	public int getSn_type() {
		return sn_type;
	}

	/**
	 * @param 对sn_type进行赋值
	 */
	public void setSn_type(int sn_type) {
		this.sn_type = sn_type;
	}

	/**
	 * @return 返回 ssid
	 */
	public String getSsid() {
		return ssid;
	}

	/**
	 * @param 对ssid进行赋值
	 */
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	/**
	 * @return 返回 planTime
	 */
	public String getPlanTime() {
		return planTime;
	}

	/**
	 * @param 对planTime进行赋值
	 */
	public void setPlanTime(String planTime) {
		this.planTime = planTime;
	}

	public FlavorVO getFlavorVO() {
		return flavorVO;
	}

	public void setFlavorVO(FlavorVO flavorVO) {
		this.flavorVO = flavorVO;
	}
	

	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}

	/**
	 * @return 返回 serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAddDisk() {
		return addDisk;
	}

	public void setAddDisk(String addDisk) {
		this.addDisk = addDisk;
	}

	public String getOldIP() {
		return oldIP;
	}

	public void setOldIP(String oldIP) {
		this.oldIP = oldIP;
	}

	public String getNewIP() {
		return newIP;
	}

	public void setNewIP(String newIP) {
		this.newIP = newIP;
	}

	public String getConfirmFlag() {
		return confirmFlag;
	}

	public void setConfirmFlag(String confirmFlag) {
		this.confirmFlag = confirmFlag;
	}

	public int getVolumeId() {
		return volumeId;
	}

	public void setVolumeId(int volumeId) {
		this.volumeId = volumeId;
	}

	public long getOwner() {
		return owner;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVmType() {
		return vmType;
	}

	public void setVmType(String vmType) {
		this.vmType = vmType;
	}

	public String getStatus_buss() {
		return status_buss;
	}

	public void setStatus_buss(String status_buss) {
		this.status_buss = status_buss;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId==null?0:referenceId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public String getOutComments() {
		return outComments;
	}

	public void setOutComments(String outComments) {
		this.outComments = outComments;
	}

	public int getDelayLong() {
		return delayLong;
	}

	public void setDelayLong(int delayLong) {
		this.delayLong = delayLong;
	}

	public int getVolumeMode() {
		return volumeMode;
	}

	public void setVolumeMode(int volumeMode) {
		this.volumeMode = volumeMode;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVmId() {
        return vmId;
    }

    public void setVmId(String vmId) {
        this.vmId = vmId;
    }

	public int getVolumeSize() {
		return volumeSize;
	}

	public void setVolumeSize(int volumeSize) {
		this.volumeSize = volumeSize;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getScId() {
		return scId;
	}

	public void setScId(int scId) {
		this.scId = scId;
	}

	public ResourceLimit getResourceLimit() {
		return resourceLimit;
	}

	public void setResourceLimit(ResourceLimit resourceLimit) {
		this.resourceLimit = resourceLimit;
	}

	public String[] getNewIPs() {
		return newIPs;
	}

	public void setNewIPs(String[] newIPs) {
		this.newIPs = newIPs;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
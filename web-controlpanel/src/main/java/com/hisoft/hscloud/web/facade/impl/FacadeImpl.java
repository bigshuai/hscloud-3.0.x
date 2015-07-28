/* 
* 文 件 名:  FacadeImp.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  dinghb 
* 修改时间:  2013-1-5 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.facade.impl; 

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.sc.entity.ServerNode;
import com.hisoft.hscloud.bss.sla.sc.service.IServerNodeService;
import com.hisoft.hscloud.common.entity.City;
import com.hisoft.hscloud.common.entity.HcEventVmOps;
import com.hisoft.hscloud.common.entity.LogOperatorType;
import com.hisoft.hscloud.common.entity.OperationLog;
import com.hisoft.hscloud.common.entity.Province;
import com.hisoft.hscloud.common.service.DataService;
import com.hisoft.hscloud.common.service.OperationLogService;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.IPConvert;
import com.hisoft.hscloud.common.util.IPUtil;
import com.hisoft.hscloud.common.util.PasswordUtil;
import com.hisoft.hscloud.common.util.SocketUtil;
import com.hisoft.hscloud.controlpanel.entity.ControlPanelUser;
import com.hisoft.hscloud.controlpanel.service.ControlPanelService;
import com.hisoft.hscloud.crm.icp.service.IcpService;
import com.hisoft.hscloud.crm.icp.vo.IcpVO;
import com.hisoft.hscloud.crm.usermanager.entity.MaintenanceLog;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.service.MaintenanceLogService;
import com.hisoft.hscloud.crm.usermanager.service.UserService;
import com.hisoft.hscloud.systemmanagement.service.HcEventVmOpsService;
import com.hisoft.hscloud.systemmanagement.vo.LogQueryVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.service.IPService;
import com.hisoft.hscloud.vpdc.ops.entity.VmSnapShot;
import com.hisoft.hscloud.vpdc.ops.entity.VmSnapShotPlan;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcInstance;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_Period;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_extdisk;
import com.hisoft.hscloud.vpdc.ops.service.Operation;
import com.hisoft.hscloud.vpdc.ops.vo.InstanceDetailVo;
import com.hisoft.hscloud.vpdc.ops.vo.VmSnapShotPlanVO;
import com.hisoft.hscloud.vpdc.ops.vo.VmSnapShotVO;
import com.hisoft.hscloud.vpdc.ops.vo.VncClientVO;
import com.hisoft.hscloud.vpdc.ops.vo.VolumeVO;
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
@Service
public class FacadeImpl implements Facade {
	
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private Operation operation;
	@Autowired
	private ControlPanelService controlPanelService;
	@Autowired
	private IcpService icpService;
	@Autowired
	private IPService iPService;
	@Autowired
	private DataService dataService;
	@Autowired
	private HcEventVmOpsService vmOpsLogService;
	@Autowired
	private MaintenanceLogService maintenanceLogService;
	@Autowired
	private OperationLogService operationLogService;
	@Autowired
	private UserService userService;
    private SocketUtil socket = new SocketUtil();	
	@Autowired
	private IServerNodeService nodeService;	
    
	@Override
	public boolean startVmByVmId(String vmId,Object o,String otype) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter startVmByVmId method.");			
		}
		boolean bl = false;
		bl = operation.openvm(vmId,o,otype);
		if(logger.isDebugEnabled()){
			logger.debug("exit startVmByVmId method.");
		}
		return bl;
	}
	
	@Override
	public String rebootVmByVmId(String vmId,Object o,String otype) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter rebootVmByVmId method.");			
		}
		String result = operation.rebootVm(vmId,o,otype);
		if(logger.isDebugEnabled()){
			logger.debug("exit rebootVmByVmId method.");
		}
		return result;
	}
	
	@Override
	public boolean closeVmByVmId(String vmId,Object o,String otype) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter closeVmByVmId method.");			
		}
		boolean bl = false;
		bl = operation.closeVm(vmId,o,otype);
		if(logger.isDebugEnabled()){
			logger.debug("exit closeVmByVmId method.");
		}
		return bl;
	}
	
	@Override
	public String getVNCUrl(String vmId) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter getVNCUrl method.");			
		}
		String result = operation.getVnc(vmId);
		if(logger.isDebugEnabled()){
			logger.debug("exit getVNCUrl method.");
		}
		return result;
	}
	
	public String getVMOSUser(String osId){
		return operation.getVMOSUser(Integer.valueOf(osId));
	}
	
	@Transactional(readOnly = false)
	public void resetVMOS(String vmId,String osId,String user,String pwd,Object o,String otype){
		logger.info("OPS-Facade-resetVMOS start");
		try {
			operation.resetVMOS(vmId, Integer.valueOf(osId),user,pwd,o,otype);
		} catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_RESETOS_ERROR, "resetVMOS异常",
					logger, e);
		}
	}
	
	@Override
	public String renewVM(String vmId, String backupId,Object o,String otype) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter renewVM method.");			
		}
		String result = operation.renewVm(vmId, backupId,o,otype);	
		if(logger.isDebugEnabled()){
			logger.debug("exit renewVM method.");
		}
		return result;
	}
	
	@Override
	public List<VmSnapShotVO> findAllBackupsByVmId(String vmId) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter findAllBackupsByVmId method.");			
		}
		List<VmSnapShotVO> lvssVO = operation.findAllSnapShots(vmId);
		if(logger.isDebugEnabled()){
			logger.debug("exit findAllBackupsByVmId method.");
		}
		return lvssVO;
	}
	
	@Override	
	public boolean terminateVmByVmId(String vmId,String vmName,String terminateFlag,long adminId) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter terminateVmByVmId method.");			
		}
		boolean result = false;
		result = operation.terminate(vmId,vmName,terminateFlag,adminId);		
		if(logger.isDebugEnabled()){
			logger.debug("exit terminateVmByVmId method.");
		}
		return result;
	}
	
	@Override
	public InstanceDetailVo findDetailVmById(String vmId) throws HsCloudException {
		if(logger.isDebugEnabled()){
			logger.debug("enter findDetailVmById method.");			
		}
		InstanceDetailVo idVO = operation.findVm(vmId);	
		if(logger.isDebugEnabled()){
			logger.debug("exit findDetailVmById method.");
		}
		return idVO;
	}

	@Override
	@Transactional
	public VncClientVO getClientVnc(String uuid) throws HsCloudException{
		logger.info("OPS-Facade-getClientVnc start");
		VncClientVO vnc = new VncClientVO();
		vnc.setProxyIP(socket.getProxyIP());
		try {
			//获取业务数据
			VpdcInstance vi = operation.getInstanceByVmId(uuid);
			User u = userService.getUser(vi.getVpdcreference().getOwner());
			String nodeName = vi.getNodeName();
			if(StringUtils.isEmpty(nodeName)){
				logger.error("vm's nodename is null");
			}
			ServerNode sn = nodeService.getNodeByName(nodeName);
			String IP = "";
			if(sn!=null){
				IP = sn.getIp();
			}
			//Socket传送业务请求
			JSONObject jsonChild = new JSONObject();
			jsonChild.accumulate("IP", IP);
			jsonChild.accumulate("UUID", uuid);
			JSONObject json = new JSONObject();
			json.accumulate("Node", jsonChild);
			String response = socket.sendRequest(IP,json.toString());
			//处理vnc参数
			JSONObject responseJson = JSONObject.fromObject(response);
			Object responsePort = JSONObject.fromObject(responseJson.get("Node")).get("port");
			Integer port = Integer.valueOf(responsePort.toString());
			Integer proxyPort = operation.getAvailablePort(u==null?null:u.getDomain().getId());
			if(proxyPort==null){
				HsCloudException he = new HsCloudException();
				he.setCode(Constants.VM_VNC_CLIENT_PORT);
				//抛无可用的VNC代理端口
				throw new HsCloudException(Constants.VM_VNC_CLIENT_PORT, "getClientVnc异常", logger,
						he);
			}else{
				//要确保服务器安装了socat，才能执行
				String command = "socat TCP4-LISTEN:"+proxyPort+",reuseaddr TCP4:"+IP+":"+port;
				try {
					Runtime.getRuntime().exec(command);
					vnc.setProxyPort(proxyPort);
					operation.useVncPort(proxyPort.toString(), u, uuid);
				} catch (Exception e) {
					throw new HsCloudException(Constants.VM_VNC_ERROR, "getClientVnc异常", logger,e);
				}
			}
		} catch (HsCloudException e) {
			if(e.getClass()==new HsCloudException().getClass()){
				HsCloudException ex = (HsCloudException)e;
				throw new HsCloudException(ex.getCode(), "getClientVnc异常", logger, ex);
			}else{
				throw new HsCloudException(Constants.VM_VNC_ERROR, "getClientVnc异常", logger,
						e);
			}
		}
		return vnc;
	}	
	
	public InstanceDetailVo getVmOsListByVmId(String vmId) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter getVmOsListByVmId method.");			
		}
		InstanceDetailVo idv = operation.getVmOsListByVmId(vmId);	
		if(logger.isDebugEnabled()){
			logger.debug("exit getVmOsListByVmId method.");
		}
		return idv;
	}
	
	@Override
	@Transactional(readOnly=true)
	public ControlPanelUser login(String userName, String password) {
		
		if(logger.isDebugEnabled()){
			logger.debug("enter login method.");		
			logger.debug("userName:"+userName);		
			logger.debug("password:"+password);		
		}
		ControlPanelUser controlPanelUser = null;
		if(IPUtil.isIP(userName))
			controlPanelUser = controlPanelService.loginByIp(userName, password);
		
		if(null == controlPanelUser){
			VpdcInstance vpdcInstance = operation.getInstanceByVmName(userName);
			if(null != vpdcInstance){
				controlPanelUser =  controlPanelService.loginByVMid(vpdcInstance.getVmId(), password);
			}
		}
		
        if(null != controlPanelUser){
        	
    		VpdcReference vr  = operation.getReferenceByVmId(controlPanelUser.getVmId());
    		if(null==vr){
    			throw new HsCloudException(Constants.VM_OPSEXCEPTION,logger);
    		}
    		if(null != vr && vr.getIsEnable()!= 0){
    			throw new HsCloudException(Constants.VM_FREEZE_NODO,logger);
    		}
    		VpdcReference_Period vp = operation.getReferencePeriod(vr.getId());
    		if(logger.isDebugEnabled()){
    			logger.debug("controlPanelUser:"+controlPanelUser);		
    			logger.debug("exit login method.");			
    		}
    		if(null != vp.getEndTime() && vp.getEndTime().compareTo(new Date())==-1){
    			throw new HsCloudException(Constants.VM_EXPIRE,logger);
    		}
    		controlPanelUser.setUserPassword(null);
    		
        }
		return controlPanelUser;
		
	}
	
	@Override
	@Transactional(readOnly=false)
	public void modifyPassword(String oldPassword, String newPassword, long id) {
		
		if(logger.isDebugEnabled()){
			logger.debug("enter login method.");
			logger.debug("id:"+id);		
			logger.debug("userName:"+oldPassword);		
			logger.debug("password:"+newPassword);		
		}
		try {
			
		    ControlPanelUser user = controlPanelService.findControlUserByID(id);
		    String enPassword = PasswordUtil.getEncyptedPasswd(oldPassword);
		    if(!enPassword.equals(user.getUserPassword())){
		    	
			    throw new HsCloudException(Constants.PASSWORD_IS_ERROR,"Old Password is error.",logger);
			    
		    }else{
		    	String enNewPassword = PasswordUtil.getEncyptedPasswd(newPassword);
			    user.setUpdateDate(new Date());
			    user.setUserPassword(enNewPassword);
			    controlPanelService.saveControlPanelUser(user);
			    
		    }
		    
		} catch(HsCloudException hse){
			
			throw hse;
			
		}catch (Exception e) {
			throw new HsCloudException(Constants.PASSWORD_ENCRYPTION_FAILED," Encryption failed.",logger);
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("exit modifyPassword method.");			
		}
		
	}
	
	//**************lihonglei start***********************************
    /**
     * icp备案
    * @param icpVO
    * @return
     */
	@Override
    public String icpPutOnRecord(IcpVO icpVO) {
        return icpService.icpPutOnRecord(icpVO);
    }
    
    /**
     * icp初始化
    * @param vmId
    * @return
     */
    @Override
    @Transactional(readOnly=true)
    public Map<String, Object> initIcp(String vmId) {
        try{
            VpdcReference reference = operation.getReferenceByVmId(vmId);
           // long userId = reference.getOwner();
            /*User user = userService.getUser(userId);
            IcpVO icpVO = new IcpVO();
            icpVO.setMemberLogin(user.getEmail());
            icpVO.setMemberPwd(user.getPassword());
            if(UserType.ENTERPRISE_USER.getType().equals(user.getUserType())) {
                icpVO.setMemberType("1");
                icpVO.setName(user.getUserProfile().getCompany());
            } else if(UserType.PERSONAL_USER.getType().equals(user.getUserType())) {
                icpVO.setMemberType("2");
                icpVO.setName(user.getName());
            } else {
                throw new HsCloudException(Constants.ICP_EXCEPITON, "UserType用户类型异常", logger);
            }
            icpVO.setContactName(user.getName());
            icpVO.setIdType("1");
            icpVO.setIdNumber(user.getUserProfile().getIdCard());
            icpVO.setCountry("CN");
            String province = icpService.getProvinceCode(user.getUserProfile().getRegion().getNameCode());
            icpVO.setProvince(province);
            icpVO.setAddress(user.getUserProfile().getAddress());
            icpVO.setEmail(user.getEmail());
            if(user.getUserProfile().getTelephone().length() == 11) {
                icpVO.setMobile(user.getUserProfile().getTelephone());
            } else {
                icpVO.setTelno(user.getUserProfile().getTelephone());
            }*/
            
            long referenceId = reference.getId();
            List<BigInteger> list = iPService.getIPListByUserId(referenceId);
            List<String> ipList = new ArrayList<String>();
            for(BigInteger ipLong : list) {
                ipList.add(IPConvert.getStringIP(ipLong.longValue()));
            }
            
            List<Province> provinceList = dataService.getProvinceList();
            List<City> cityList = dataService.getCityList(provinceList.get(0).getProvinceCode());
            
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("ipList", ipList);
        //    result.put("icpVO", icpVO);
            result.put("provinceList", provinceList);
            result.put("cityList", cityList);
            return result;
        } catch (Exception ex) {
            throw new HsCloudException(Constants.ICP_EXCEPITON, "ICP备案initIcp异常", logger);
        }
        
    }
    
    /**
     * 省份地市联动
    * @param provinceCode
    * @return
     */
    @Override
    public List<City> changeProvince(String provinceCode) {
        List<City> cityList = dataService.getCityList(provinceCode);
        return cityList;
    }
    
  //**************lihonglei end***********************************
	@Override
	public String backupVmByVmId(String vmId, String sn_name, 
			String sn_comments, int sn_type,Object o,String otype)
			throws HsCloudException {
		logger.info("OPS-Facade-backupVmByVmId start");
		String result;
		try {
			result = operation.backupsVm(vmId, sn_name,
					sn_comments, sn_type, o, otype);
		} catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_BACKUP_ERROR, "backupVmByVmId异常",
					logger, e);
		}
		return result;
	}
	
    /**
	 * <终止快照:实质就是删除超过30分钟都还没有创建成功的快照> 
	* @param vmId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	@Override
	public boolean stopSnapshot(String vmId) throws HsCloudException {
		logger.info("OPS-Facade-stopSnapshot start");
		try {
			boolean result = false;
			result = operation.stopSnapshot(vmId);
			return result;
		} catch (HsCloudException e) {
			throw new HsCloudException("", "stopSnapshot异常", logger, e);
		}
	}
	
	@Override
	public String createBackupVmPlanByVmId(VmSnapShotPlan vssp)
			throws HsCloudException {
		logger.info("OPS-Facade-createBackupVmPlanByVmId start");
		String result;
		try {
			result = operation.backupsVmPlan(vssp);
		} catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_BACKUP_PLAN_ERROR,
					"createBackupVmPlanByVmId异常", logger, e);
		}
		return result;
	}
	
	@Override
	public VmSnapShotPlanVO getBackupVmPlanByVmId(String vmId)
			throws HsCloudException {
		logger.info("OPS-Facade-getBackupVmPlanByVmId start");
		VmSnapShotPlanVO vsspVO = null;
		try {
			vsspVO = operation.getVmSnapShotPlanByVmId(vmId);
		} catch (HsCloudException e) {
			throw new HsCloudException(Constants.VM_SNAPSHOT_GET_ERROR,
					"getBackupVmPlanByVmId异常", logger, e);
		}
		return vsspVO;
	}
	
	@Override
	public VmSnapShotVO getLastestVmSnapshot(String vmId)
			throws HsCloudException {
		logger.info("OPS-Facade-getLastestVmSnapshot start");
		VmSnapShotVO vmSnapShotVO =new VmSnapShotVO();
		vmSnapShotVO.setHasExtDisk(0);
		//List<VolumeVO> volumeVOList= operation.getAttachVolumesOfVm(vmId);
		List<VpdcReference_extdisk> extDiskList = operation.getAttachExttendDisksOfVmByScsis(vmId);
		//isics方式扩展盘
		/*if(volumeVOList.size()>0){
			vmSnapShotVO.setHasExtDisk(1);
		}*/
		//SCIS方式扩展盘
		if(extDiskList.size()>0){
			vmSnapShotVO.setHasExtDisk(2);
		}
		//两种方式的扩展盘都有
		/*if(volumeVOList.size()>0 && extDiskList.size()>0){
			vmSnapShotVO.setHasExtDisk(3);
		}*/
		VmSnapShot vmSnapShot = operation.getNewestVmSnapShot(vmId);
		if(vmSnapShot!=null){
			vmSnapShotVO.setId(vmSnapShot.getId());
			vmSnapShotVO.setSnapShot_id(vmSnapShot.getSnapShot_id());
			vmSnapShotVO.setSnapShot_name(vmSnapShot.getSnapShot_name());
			vmSnapShotVO.setSnapShot_comments(vmSnapShot.getSnapShot_comments());
			vmSnapShotVO.setSnapShot_type(vmSnapShot.getSnapShot_type());
			vmSnapShotVO.setCreateTime(vmSnapShot.getCreateTime());
			vmSnapShotVO.setStatus(vmSnapShot.getStatus());
		}
		return vmSnapShotVO;
	}
	
	@Override
	public Page<HcEventVmOps> getVmOpsLog(Page<HcEventVmOps> paging,
			LogQueryVO logQueryVO) {
		return vmOpsLogService.getVmOperationLog(paging, logQueryVO);
	}
	
	@Override
	public ControlPanelUser getCPUserByVmId(String vmId)
			throws HsCloudException {
		return controlPanelService.findControlUserByVmID(vmId);
	}
	
	@Override
	@Transactional
	public void saveLogByLoginWithoutPass(MaintenanceLog maintenanceLog)
			throws HsCloudException {
		maintenanceLogService.saveMaintenanceLog(maintenanceLog);
	} 

	@Override
	@Transactional
	public void insertOperationLog(ControlPanelUser user, String description, String actionName,
			short operationResult, String operateObject) throws HsCloudException {
		if (user != null) {
			String vmId = user.getVmId();
			VpdcReference vr = operation.getReferenceByVmId(vmId);
			if (vr != null) {
				User vmOwner = userService.getUser(vr.getOwner());
				if (vmOwner != null) {
					OperationLog log = new OperationLog();
					log.setActionName(actionName);
					log.setDescription(description);
					log.setOperationDate(new Date());
					log.setDomainName(vmOwner.getDomain().getAbbreviation());
					log.setOperationResult(operationResult);
					log.setOperator(user.getVmId());
					log.setOperatorType(LogOperatorType.CP.getIndex());
					log.setUserName(vmOwner.getName());
					log.setOperateObject(operateObject);
					operationLogService.insertOperationLog(log);
				}
			}
		}
	}
	
	@Transactional
	public boolean repairOSByVmId(String vmId,Object o,String otype) throws HsCloudException{
		if(logger.isDebugEnabled()){
			logger.debug("enter repairOSByVmId method.");			
		}
		boolean bl = false;
		bl = operation.repairOS(vmId,o,otype);
		if(logger.isDebugEnabled()){
			logger.debug("exit repairOSByVmId method.");
		}
		return bl;
	}	

}
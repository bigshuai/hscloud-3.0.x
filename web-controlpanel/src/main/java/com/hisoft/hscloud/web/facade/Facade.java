/* 
* 文 件 名:  Facade2.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  dinghb 
* 修改时间:  2013-1-5 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.facade; 

import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.entity.City;
import com.hisoft.hscloud.common.entity.HcEventVmOps;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.controlpanel.entity.ControlPanelUser;
import com.hisoft.hscloud.crm.icp.vo.IcpVO;
import com.hisoft.hscloud.crm.usermanager.entity.MaintenanceLog;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.systemmanagement.vo.LogQueryVO;
import com.hisoft.hscloud.vpdc.ops.entity.VmSnapShotPlan;
import com.hisoft.hscloud.vpdc.ops.json.bean.VmOsBean;
import com.hisoft.hscloud.vpdc.ops.vo.InstanceDetailVo;
import com.hisoft.hscloud.vpdc.ops.vo.VmSnapShotPlanVO;
import com.hisoft.hscloud.vpdc.ops.vo.VmSnapShotVO;
import com.hisoft.hscloud.vpdc.ops.vo.VncClientVO;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  dinghb 
 * @version  [版本号, 2013-1-5] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface Facade {
	/**
	 * <启动虚拟机>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean startVmByVmId(String vmId,Object o,String otype) throws HsCloudException;

	/**
	 * <重启虚拟机>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String rebootVmByVmId(String vmId,Object o,String otype) throws HsCloudException;

	/**
	 * <关闭虚拟机>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean closeVmByVmId(String vmId,Object o,String otype) throws HsCloudException;

	/**
	 * <获取虚拟机远程连接>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String getVNCUrl(String vmId) throws HsCloudException;
	
	/**
	 * <获取虚拟机操作系统用户名> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public String getVMOSUser(String osId);

	/**
	 * <重置虚拟机操作系统> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void resetVMOS(String vmId,String osId,String user,String pwd,Object o,String otype);

	/**
	 * <获取所有虚拟机备份历史记录>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List<VmSnapShotVO> findAllBackupsByVmId(String vmId) throws HsCloudException;
	/**
	 * <根据虚拟机id和备份的id还原虚拟机>
	 * 
	 * @param vmId
	 * @param backupId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String renewVM(String vmId, String backupId,Object o,String otype) throws HsCloudException;

	/**
	 * <删除虚拟机>
	 * 
	 * @param vmId
	 * @see [类、类#方法、类#成员]
	 */
	public boolean terminateVmByVmId(String vmId,String vmName,String terminateFlag,long adminId) throws HsCloudException;

	/**
	 * <查看虚拟机详情>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public InstanceDetailVo findDetailVmById(String vmId) throws HsCloudException;
	
	/**
	 * 获取vncviewer连接VNC的参数
	 * @param uuid
	 * @return
	 * @throws HsCloudException
	 */
	public VncClientVO getClientVnc(String uuid) throws HsCloudException;	
	
	/**
	 * <获取VM的OS集合> 
	* <功能详细描述> 
	* @param vmId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public InstanceDetailVo getVmOsListByVmId(String vmId) throws HsCloudException;
	
	/**
	 * 
	 * @param userName  登录用户 
	 * @param password  密码
	 * @return
	 */
	public ControlPanelUser login(String userName,String password);

	/**
	 * 修改密码
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * @param id  用户id
	 */
	public void modifyPassword(String oldPassword, String newPassword, long id);
	
	/**
     * icp备案
    * <功能详细描述> 
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public String icpPutOnRecord(IcpVO icpVO);

    /**
     * 初始化icp数据  
    * <功能详细描述> 
    * @param vmId
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public Map<String, Object> initIcp(String vmId);
    
    /**
     * 改变省份变动地市 
    * <功能详细描述> 
    * @param provinceCode
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public List<City> changeProvince(String provinceCode);
    /**
	 * <备份虚拟机> 
	* @param vmId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String backupVmByVmId(String vmId,String sn_name,String sn_comments,int sn_type,Object o,String otype) throws HsCloudException;
    /**
	 * <终止快照:实质就是删除超过30分钟都还没有创建成功的快照> 
	* @param vmId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public boolean stopSnapshot(String vmId) throws HsCloudException;
	/**
	 * <创建虚拟机备份计划> 
	* @param vssp
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String createBackupVmPlanByVmId(VmSnapShotPlan vssp) throws HsCloudException;
	/**
	 * <根据虚拟机id获取虚拟机备份计划> 
	* @param vmId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public VmSnapShotPlanVO getBackupVmPlanByVmId(String vmId) throws HsCloudException;
	/**
	 * <获得虚拟机最后一次的备份> 
	* <功能详细描述> 
	* @param vmId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public VmSnapShotVO getLastestVmSnapshot(String vmId) throws HsCloudException;

	public Page<HcEventVmOps> getVmOpsLog(Page<HcEventVmOps> paging,
			LogQueryVO logQueryVO);
    public ControlPanelUser getCPUserByVmId(String vmId)throws HsCloudException;
    
    /**
	 * <记录没有密码登录的登录日志> 
	* <功能详细描述> 
	* @param maintenanceLog
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void saveLogByLoginWithoutPass(MaintenanceLog maintenanceLog)throws HsCloudException;
	/**
	 * <插入操作日志> 
	* <功能详细描述> 
	* @param user 控制面板用户
	* @param description 操作描述
	* @param actionName 操作中文名称
	* @param operationResult 操作结果
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void insertOperationLog(ControlPanelUser user, String description, String actionName,
			short operationResult, String operateObject) throws HsCloudException;
	/**
	 * <系统修复>
	 * 
	 * @param vmId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean repairOSByVmId(String vmId,Object o,String otype) throws HsCloudException;

}
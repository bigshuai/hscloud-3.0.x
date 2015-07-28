/* 
* 文 件 名:  ControlPanelServiceImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  dinghb 
* 修改时间:  2013-1-4 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.controlpanel.service; 

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hisoft.hscloud.common.exception.NoUniqueException;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.PasswordUtil;
import com.hisoft.hscloud.controlpanel.dao.ControlPanelDao;
import com.hisoft.hscloud.controlpanel.entity.ControlPanelUser;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  dinghb 
 * @version  [版本号, 2013-1-4] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class ControlPanelServiceImpl implements ControlPanelService {

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private ControlPanelDao controlPanelDao;
	/** 
	* @param cuser
	* @throws HsCloudException 
	*/
	@Override
	@Transactional(readOnly = false)
	public void createControlUser(String ip,String pwd,long userId,String vmId) throws HsCloudException {
		ControlPanelUser cuser = controlPanelDao.getCPByVmId(vmId);
		Date d = new Date();
		if(cuser==null){
			cuser = new ControlPanelUser();
			cuser.setCreate_Id(userId);
			cuser.setCreateDate(d);
			cuser.setStatus(0);
			cuser.setVmId(vmId);
		}
		cuser.setUpdateDate(d);
		if(ip!=null){
			cuser.setVmIP(ip);
		}
		try {
			if(pwd==null){
				cuser.setUserPassword(PasswordUtil.getEncyptedPasswd(PasswordUtil.getRandomNum(6)));
			}else{
				cuser.setUserPassword(pwd);
			}
			cuser.setUserPassword(PasswordUtil.getEncyptedPasswd(cuser.getUserPassword()));
			controlPanelDao.saveControlUser(cuser);
		} catch (Exception e) {
			throw new HsCloudException(Constants.PASSWORD_ENCRYPTION_FAILED," Encryption failed.",logger);
		}
	}
	
	@Transactional(readOnly = false)
	public void deleCPByVmId(String vmId)throws HsCloudException{
		ControlPanelUser CP = controlPanelDao.getCPByVmId(vmId);
		if(CP!=null){
			CP.setStatus(1);
			controlPanelDao.saveControlUser(CP);
		}
	}
	
   public ControlPanelUser findControlUserByID(long id){
	   return controlPanelDao.findUniqueBy("id", id);
   }
   
   public ControlPanelUser loginByVMid(String vmId,String password){
	   
		if(logger.isDebugEnabled()){
			   logger.debug("enter ControlPanelServiceImpl login method.");
			   logger.debug("vmId:"+vmId);
			   logger.debug("password:"+password);
			}
	   
		String enPassword = "";
		try {
			enPassword = PasswordUtil.getEncyptedPasswd(password);
		} catch (Exception e) {
			throw new HsCloudException(Constants.PASSWORD_ENCRYPTION_FAILED," Encryption failed.",logger);
		}
		
		try {

			StringBuilder sb = new StringBuilder();
			sb.append("select * from hc_controlpanel_user hcu where hcu.status=0 and hcu.userPassword='").append(enPassword).append("' and hcu.vmId='").append(vmId).append("'");
			String sql = sb.toString();
			Map<String,Object> map = new HashMap<String,Object>();
			List<ControlPanelUser> controlPanelUsers = controlPanelDao.findBySQL(sql, map);
			if(logger.isDebugEnabled()){
			   logger.debug("enter ControlPanelServiceImpl findBySQL method.");
			}
			if(controlPanelUsers.isEmpty()){
				return null;
			}else{
				return controlPanelUsers.get(0);
			}
			
			
		} catch (Exception e) {
			throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
		}
		
	}
   
    @Override
	public ControlPanelUser loginByIp(String ip, String password) {
    	
		if(logger.isDebugEnabled()){
			  logger.debug("enter ControlPanelServiceImpl login method.");
			  logger.debug("ip:"+ip);
			  logger.debug("password:"+password);
			  
		}
		String enPassword = "";
		try {
			enPassword = PasswordUtil.getEncyptedPasswd(password);
		} catch (Exception e) {
			throw new HsCloudException(Constants.PASSWORD_ENCRYPTION_FAILED," Encryption failed.",logger);
		}
		
		try {

		    StringBuilder sb = new StringBuilder();
			sb.append("SELECT * from hc_controlpanel_user hcu where hcu.status=0 and hcu.userPassword='").append(enPassword).append("' and concat(',',hcu.vmIP,',') like '%,").append(ip).append(",%'").append(" ORDER BY hcu.id DESC");
			String sql = sb.toString();
			Map<String,Object> map = new HashMap<String,Object>();
			ControlPanelUser controlPanelUsers  = controlPanelDao.findUniqueBySQL(sql, map);
			return controlPanelUsers;
			
		} catch(NoUniqueException nue){
			throw new HsCloudException(Constants.ACCOUNT_NOUNIQUE,nue.getMessage(),logger, nue);
		} catch (Exception e) {
			throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
		}
			
	}
    
	@Override
	public void saveControlPanelUser(ControlPanelUser controlPanelUser) {
		try {
			controlPanelDao.saveControlUser(controlPanelUser);
		} catch (Exception e) {
			throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
		}
		
	}
	
	@Override
	public ControlPanelUser findControlUserByVmID(String vmid){
		return controlPanelDao.getCPByVmId(vmid);
	}
	

}

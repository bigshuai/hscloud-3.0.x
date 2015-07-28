package com.hisoft.hscloud.web.action;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.entity.UserBrand;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.MD5Util;
import com.hisoft.hscloud.common.util.PasswordUtil;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.constant.UserState;
import com.hisoft.hscloud.crm.usermanager.constant.UserType;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.exception.MailAddressException;
import com.hisoft.hscloud.crm.usermanager.util.Utils;
import com.hisoft.hscloud.crm.usermanager.vo.CompanyInviteVO;
import com.hisoft.hscloud.crm.usermanager.vo.UserProfileVo;
import com.hisoft.hscloud.crm.usermanager.vo.UserVO;
import com.hisoft.hscloud.crm.usermanager.vo.UserVO1;
import com.hisoft.hscloud.web.facade.Facade;

public class UserAction extends HSCloudAction {
	
	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = -5935383224602133991L;

	private Logger logger = Logger.getLogger(this.getClass());
	
	private final String resourceType = "com.hisoft.hscloud.crm.usermanager.entity.User";
	
	//公司名称
	private String companyName;
	
	//旧密码
	private String oldPwd;
	
	//验证码
	private String code;
	
	//查询用户的id
	private String userId;
	
	//邀请id
	private String inviteId;
	
	//找回密码时所传邮箱
	private String mailAddress;
	
	//用户类型
	private String user_type;
	
	//创建子用户时所属组，用，隔开
	private String groupIds;
	
	//组id
	private String groupId;
	
	// 查询类型
	private String type;
	
	// 模糊查询条件
	private String query;
	
	//激活操作结果
	private String activeFlag = "0";
	
	private User user;
	
	private String externalUser;

	private String localUser;

	private int page;
	
	private int start;
	
	private int limit;
	
	private String sort;
	
	private long domainId;
	
	private Page<User> pageUser = new Page<User>(Constants.PAGE_NUM);
	
	private UserProfileVo userProfileVo;
	private String domainCode;
	
	@Autowired
	private Facade facade;
	
	/**
	 * 读取版权信息
	 * @author lihonglei
	 */
	public void readCopyright() {
		try{
//			Domain domain = facade.getDomainById(domainId);
			Domain domain = facade.loadDomain(domainCode);
			Map<String, String> result = new HashMap<String, String>();
			result.put("zh_CN", domain.getCopyright_zh_CN());
			result.put("en_US", domain.getCopyright_en_US());
			this.fillActionResult(result);
		} catch(Exception e) {
			this.dealThrow(Constants.DOMAIN_COPYRIGHT_EXCEPTION, e, logger);
		}
	}
	/**
	 * 通过email登录
	 */
	public void userLoginByEmail(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter userLoginByEmail method.");			
		}
		try {
			//检查验证码是否正确
			String sessionCode = (String)ServletActionContext.getRequest().getSession().getAttribute("code");
//			if(null == this.code || null == sessionCode || !sessionCode.equalsIgnoreCase(this.code)){
//				fillActionResult(Constants.VERCODE_IS_ERROR);
//				return ;
//			}
			
			user = Utils.strutsJson2Object(User.class);
			logger.info("userLoginByEmail");
			User loginUser=facade.loginUserByEmail(user.getEmail(), user.getPassword());

//			User loginUser=facade.loginUserByEmail(user.getEmail(), user.getPassword(),user.getDomain().getId());
			Object urlStr = super.getSession().getAttribute("urlStr");
			if(loginUser!=null){
				if(loginUser.isApproved()){
			        if(!loginUser.getDomain().getCode().equals(user.getDomain().getCode())){
			        	fillActionResult(Constants.USER_ERR_URL,loginUser.getDomain().getPublishingAddress());
			        	return ;
		            }
					//特殊用户登录
					if(!(Constants.SPECIAL_USER == loginUser.getSpecialFlag())){
						if(null == this.code || "".equals(this.code)){
							fillActionResult(Constants.VERCODE_IS_NULL);
							return ;
						}else if(null == sessionCode || !sessionCode.equalsIgnoreCase(this.code)){
							fillActionResult(Constants.VERCODE_IS_ERROR);
							return ;
						}
					}
//					try {
						ServletContext sc = super.getApplication();
						String email = loginUser.getEmail();
						HttpSession session = null;
						Object sessionObject = sc.getAttribute(email);
						if(sessionObject != null)
							session = (HttpSession)sessionObject;
						if(null != session){
							//((HttpSession)super.getApplication().getAttribute(user.getEmail())).invalidate();
							//logger.info(session.getId());
							session=null;
							//session.setAttribute(Constants.LOGIN_CURRENTUSER,null);
//							super.getRequest().getSession().setMaxInactiveInterval(2);
//							super.getRequest().getSession(true).
//							if(null != hs){
//								hs.removeAttribute(Constants.LOGIN_CURRENTUSER);
//							}
						}
						HttpSession currentSession = super.getSession();
						super.getSession().setAttribute("enUid", loginUser.getId());
						currentSession.setAttribute(Constants.LOGIN_CURRENTUSER, loginUser);
						sc.setAttribute(email, currentSession);
//						currentSession.setMaxInactiveInterval(1);
//					} catch (Exception e) {
//						logger.error(e.getMessage());
//						super.getApplication().removeAttribute(loginUser.getEmail());
//					}		
					fillActionResult(urlStr);
				}else if(UserState.FREEZE.getIndex()==loginUser.getEnable()){
					fillActionResult(Constants.ACCOUNT_IS_FREEZED);
				}else{
					fillActionResult(Constants.ACCOUNT_IS_ENABLE);
				}
			}else{
				fillActionResult(Constants.ACCOUNT_NOT_FOUND);
			}
		}catch (HsCloudException hse) {
			logger.error("login error:",hse);
			dealThrow(hse, hse.getCode());
		} catch (Exception e) {
			logger.error("login error:",e);
			fillActionResult(null,e.getMessage());
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit userLoginByEmail method.takeTime:" + takeTime + "ms");
		}
	}
	
	
	/**
	 * 退出登录  清空session
	 * @return
	 * @throws Exception
	 */
	public void logout()throws Exception{
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter logout method.");			
		}
		User user=(User)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
		if(user!=null){
			facade.modifyUserOnlineStatus(user.getId(), (short)0);
		}
		super.getSession().setAttribute(Constants.LOGIN_CURRENTUSER,null);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit logout method.takeTime:" + takeTime + "ms");
		}
		fillActionResult(true);
		
	}
	
	/**
	 * 分页模糊查询显示子用户
	 */
	public void pageGroupUser(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter pageSubUser method.");			
		}
		pageUser = new Page<User>(limit);
		pageUser.setPageNo(page);
		pageUser.setOrder(Page.DESC);
		pageUser.setOrderBy("id");
		
		User user = (User)super.getCurrentLoginUser();

		try {
			
			Page<UserVO> userVOPage = facade.fuzzySearchPermissionUser(query, parseSort(), pageUser, UserType.PERSONAL_USER.getType(), user.getId(), super.getPrimKeys());
			fillActionResult(userVOPage);
		} catch (HsCloudException hce){
			dealThrow(hce, "");
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit pageSubUser method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 查询组下的用户
	 */
	public void pageUserInGroup(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter pageSubUserInGroup method.");			
		}
		pageUser = new Page<User>(limit);
		pageUser.setPageNo(page);
		pageUser.setOrder(Page.DESC);
		pageUser.setOrderBy("id");
		User user = (User)super.getCurrentLoginUser();
		try {
			Page<UserVO> userVOPage = facade.userInGroup(pageUser, parseSort(), UserType.PERSONAL_USER.getType(), groupId,user.getId());
			fillActionResult(userVOPage);
		} catch (HsCloudException hce){
			dealThrow(hce, "");
		}  catch (Exception e) {
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit pageSubUserInGroup method.takeTime:" + takeTime + "ms");
		}
		
	}
	
	public void getSessionUser(){
		
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getSessionUser method.");			
		}
		user= (User)super.getCurrentLoginUser();
		//去除品牌
		if(null != user.getDomain() && null != user.getDomain().getUserBrandList()){
			user.getDomain().getUserBrandList().clear();
		}
		
		if(user!=null){
			fillActionResult(user);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getSessionUser method.takeTime:" + takeTime + "ms");
		}
	}
	
	
	/**
	 * 获得当前用户信息
	 */
	public void getUserInfor(){
		
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getUserInfor method.");			
		}
		user= (User)super.getCurrentLoginUser();
		User u = facade.getUserById(user.getId());
		u.setDomain(null);
		if(u!=null){
			fillActionResult(u);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getUserInfor method.takeTime:" + takeTime + "ms");
		}
		
	}
	
	
	
	public void get(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter get method.");			
		}
		user = (User)super.getCurrentLoginUser();
		if(user!=null){
			fillActionResult(user);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit get method.takeTime:" + takeTime + "ms");
		}
	}
	
	public void getEnterpriseUser() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getEnterpriseUser method.");			
		}
		user = (User) super.getCurrentLoginUser();
		if (user != null) {
			User userA = facade.getUserByEmail(user.getEmail());
			if (null != userA && null != userA.getUserProfile()) {
				if (null != userA.getUserProfile().getCountry()) {
					userA.getUserProfile().getCountry().setRegions(null);
				}
				if (null != userA.getUserProfile().getRegion()) {
					userA.getUserProfile().getRegion().setCountry(null);
				}
			}
			fillActionResult(userA);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getEnterpriseUser method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 判断用户是否存在
	 */
	public void existPersonalUser(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter existPersonalUser method.");			
		}
		try {
			user = Utils.strutsJson2Object(User.class);
			if (user != null) {
				
				boolean flag = facade.existPersonalUser(user);
				fillActionResult(flag);
				
			}
		} catch (HsCloudException hce){
			dealThrow(hce, "");
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit existPersonalUser method.takeTime:" + takeTime + "ms");
		}
		
	}
	
	/**
	 * 创建用户（企业用户，子用户，个人用户）
	 * @throws Exception
	 */
	public void createUser() throws Exception {		
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter createUser method.");			
		}

		try {
			if(user_type.equals(UserType.ENTERPRISE_USER.getType())){
				
				User  entUser= Utils.strutsJson2Object(User.class);
				entUser.setUserType(UserType.ENTERPRISE_USER.getType());
				user =entUser;
				
			}else if(user_type.equals(UserType.PERSONAL_USER.getType())){
				
				User norUser  = Utils.strutsJson2Object(User.class);
				norUser.setUserType(UserType.PERSONAL_USER.getType());
				user =norUser;
				
			}
//			else if(user_type.equals(UserType.SUB_USER.getType())){
//				
//				User subUser = Utils.strutsJson2Object(User.class);
//				subUser.setUserType(UserType.SUB_USER.getType());
//				subUser.setCreateId(((User)super.getCurrentLoginUser()).getId());
//				user =subUser;
//			}
			else{
				fillActionResult(Constants.REGISTER_IS_ERROR);
			}
			if(user!=null){
				if(facade.getUserByEmail(user.getEmail())==null){
					facade.userRegister(user,companyName,groupIds,externalUser,localUser,domainCode);//已经整合了用户同步账户信息					
					//facade.synchroPlatformRelation(userId,user);
				}else{
					fillActionResult(Constants.EMAIL_HAD_HEEN_USED);
				}
			}
//			Object result = facade.synchroExternalUser(externalUser,localUser,domainCode);
//			this.fillActionResult(result);
		} catch (HsCloudException hce){
			dealThrow(hce, "");
		}  catch (Exception e) {
			fillActionResult(Constants.REGISTER_IS_ERROR);
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit createUser method.takeTime:" + takeTime + "ms");
		}
	}
	
//	public void synchroExternalUser(){
//		try{
//			Object result = facade.synchroExternalUser(externalUser,localUser,domainCode);
//		this.fillActionResult(result);
//	} catch(Exception e) {
//		this.dealThrow(Constants.DOMAIN_COPYRIGHT_EXCEPTION, e, logger);
//	}
//	}
	
	/**
	 * 更新用户信息
	 */
	@Deprecated
	public void updateUser(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter updateUser method.");			
		}

		try {
			if(user_type.equals(UserType.ENTERPRISE_USER.getType())){
				
				User  entUser= Utils.strutsJson2Object(User.class);
				entUser.setUserType(UserType.ENTERPRISE_USER.getType());
				user =entUser;
				
			}else if(user_type.equals(UserType.PERSONAL_USER.getType())){
				
				User norUser  = Utils.strutsJson2Object(User.class);
				norUser.setUserType(UserType.PERSONAL_USER.getType());
				user =norUser;
				
			}
//			else if(user_type.equals(UserType.SUB_USER.getType())){
//				
//				User subUser = Utils.strutsJson2Object(User.class);
//				subUser.setUserType(UserType.SUB_USER.getType());
//				user =subUser;
//				
//			}
			else{
				fillActionResult(Constants.REGISTER_IS_ERROR);
			}
			if(user!=null){
				
			    facade.updateUser(user,companyName,groupIds);

			}
		} catch (HsCloudException hce){
			dealThrow(hce, "");
		}  catch (Exception e) {
			fillActionResult(Constants.REGISTER_IS_ERROR);
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit updateUser method.takeTime:" + takeTime + "ms");
		}
		
	}
	
	/**
	 * 修改用户信息
	 */
	public void modifyUser() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter modifyUser method.");			
		}
	    try {
	        user= Utils.strutsJson2Object(User.class);
	        facade.modifyUser(user);
        } catch (HsCloudException hce){
			dealThrow(hce, "");
		}  catch (Exception e) {
            // TODO Auto-generated catch block 
            e.printStackTrace();
        }
	    if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit modifyUser method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 修改用户信息(通过注册页面)
	 */
	public void modifyUserInf() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter modifyUser method.");			
		}
	    try {
	    	User user= Utils.strutsJson2Object(User.class);
	    	user.setId(Long.parseLong(userId));
	    	user.getUserProfile().setCompany(companyName);
	        facade.modifyUser2(user);
        } catch (HsCloudException hce){
			dealThrow(hce, "");
		}  catch (Exception e) {
            // TODO Auto-generated catch block 
            e.printStackTrace();
        }
	    if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit modifyUser method.takeTime:" + takeTime + "ms");
		}
	}
	
	public void modify(){
		
		user = (User) super.getCurrentLoginUser();
		try {
			User u = Utils.strutsJson2Object(User.class);
			u.setEmail(user.getEmail());
			facade.modify(u);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 激活用户
	 * @return
	 * @throws Exception
	 */
	public String activeUser() throws Exception{
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter activeUser method.");			
		}
		try {
			
			user = facade.getUserById(user.getId());
			if(user.getEnable()>0){
				user.setName(URLEncoder.encode(user.getName(),"UTF-8"));
				activeFlag = "2";
				logger.info("activeFlag"+activeFlag);
				return "success";
			}
			String activeKey = super.getRequest().getParameter("activeKey");
			logger.info("activeKey"+activeKey);
			
			String tempKey = MD5Util.getMD5Str(user.getName() + String.valueOf(user.getUpdateDate().getTime()).substring(0, 8));
			logger.info("tempKey"+tempKey);
			logger.info(!"".equals(activeKey) && activeKey.equals(tempKey));
			//比较验证字符串
			if(!"".equals(activeKey) && activeKey.equals(tempKey)) {
				UserBrand ub = facade.getBrandByCode(user.getLevel());
				if(null != ub && ub.isApprovalOrNot()){
					user.setEnable(UserState.APPROVED.getIndex());
				}else{
					user.setEnable(UserState.ACTIVE.getIndex());
				}
				facade.activeUser(user);
				activeFlag = "1";
				logger.info("activeFlag"+activeFlag);
				
			}
			user.setName(URLEncoder.encode(user.getName(),"UTF-8"));
			logger.info("activeFlag"+activeFlag);
			fillActionResult(null);
			if(logger.isDebugEnabled()){
				long takeTime = System.currentTimeMillis() - beginRunTime;
				logger.debug("exit activeUser method.takeTime:" + takeTime + "ms");
			}
			return "success";
		} catch (Exception e) {
			logger.error("activeUser:", e);
			return "success";
		}
		
		
	}
	

	
	public void getPassword(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getPassword method.");			
		}
		try {
			user= Utils.strutsJson2Object(User.class);
			facade.findPassword(user);
		} catch(MailAddressException mailAddressException){
			
			fillActionResult(Constants.ACCOUNT_IS_ENABLE);
			
		} catch(HsCloudException hs){
			fillActionResult(hs.getCode());
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getPassword method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 修改密码
	 */
	public void resetPassword(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter resetPassword method.");			
		}
		try {
			user = Utils.strutsJson2Object(User.class);
			User u = (User)super.getCurrentLoginUser();
	        user.setId(u.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		String enPassword = "";
		try {
			enPassword = PasswordUtil.getEncyptedPasswd(oldPwd);
		} catch (Exception e) {
		}
		if(user!=null){
			String password = user.getPassword();
			user = facade.getUserById(user.getId());
			
			if(!StringUtils.isBlank(user.getPassword()) &&user.getPassword().equals(enPassword)) {
				user.setPassword(password);
				facade.resetUserPassword(user);
				fillActionResult(true);
			} else {
				fillActionResult(Constants.PASSWORD_IS_ERROR);
			}
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit resetPassword method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 判断邮箱是否占用
	 */
	public void duplicateEmail(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter duplicateEmail method.");			
		}
		if(facade.getUserByEmail(mailAddress)==null){
			fillActionResult(true);
		}else{
			fillActionResult(Constants.EMAIL_HAD_HEEN_USED);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit duplicateEmail method.takeTime:" + takeTime + "ms");
		} 
		
	}
	/**
	 * 根据uerId查询用户
	 */
	public void getUserById(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getUserById method.");			
		}
		UserVO userVO;
		try {
			userVO = facade.getUserVOById(userId);
			fillActionResult(userVO);
		}catch (HsCloudException hce){
			dealThrow(hce, "");
		}   catch (Exception e) {

			fillActionResult(e.getMessage());
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getUserById method.takeTime:" + takeTime + "ms");
		}
		
	}
	
	/**
	 * 根据userid删除用户
	 */
	public void deleteUserById(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter deleteUserById method.");			
		}
		try {
			facade.deleteUserById(userId);
			fillActionResult(true);
		}catch (HsCloudException hce){
			dealThrow(hce, "");
		}   catch (Exception e) {
			fillActionResult(e.getMessage());
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit deleteUserById method.takeTime:" + takeTime + "ms");
		}
		
	}
	
	
	/**
	 * 公司邀请
	 */
	public void companyInvite(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter companyInvite method.");			
		}
		user = (User) super.getCurrentLoginUser();
		try {
			User u = Utils.strutsJson2Object(User.class);
			if(logger.isDebugEnabled()){
				logger.debug(" u.getEmail():"+u.getEmail());
			}
			facade.companyInvite(user.getId(),u.getEmail());
		} catch(HsCloudException he){
			fillActionResult(he.getCode());
		}catch (Exception e) {
			
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit companyInvite method.takeTime:" + takeTime + "ms");
		}
		
	}
	
	/**
	 * 拒绝邀请
	 */
	public void rejectedInvite(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter rejectedInvite method.");			
		}
		try {
			facade.rejectedInvite(inviteId);
			fillActionResult(true);
		}catch (HsCloudException he) {
			dealThrow(he,"");
		}catch (Exception e) {
			
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit rejectedInvite method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 接受邀请
	 */
	public void acceptedInvite(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter acceptedInvite method.");			
		}
		try {

			facade.acceptedInvite(inviteId);
			fillActionResult(true);
			
		} catch (HsCloudException he) {
			
			dealThrow(he,"");
			
		}catch (Exception e) {
			
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit acceptedInvite method.takeTime:" + takeTime + "ms");
		}
		
	}
	
	/**
	 * 用户解除关系
	 */
	public void userTerminateInvite(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter userTerminateInvite method.");			
		}
		try {
			
			facade.userTerminateInvite(inviteId);
			fillActionResult(true);
			
		} catch (HsCloudException he) {
			
			dealThrow(he,"");
			
		}catch (Exception e) {
			
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit userTerminateInvite method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 企业解除关系
	 */
	public void entTerminateInvite(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter entTerminateInvite method.");			
		}
		try {

			user = (User) super.getCurrentLoginUser();
			facade.entTerminateInvite(user.getId(),Long.valueOf(userId));
			fillActionResult(true);
			
		} catch (HsCloudException he) {
			
			dealThrow(he,"");
			
		}catch (Exception e) {
			
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit entTerminateInvite method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 查询收到的邀请
	 */
	public void getInvite(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getInvite method.");			
		}
		try {

			user = (User) super.getCurrentLoginUser();
			List<CompanyInviteVO> companyInviteVOs = facade.getInvite(user.getId());
			fillActionResult(companyInviteVOs);
			
		} catch (HsCloudException he) {
			
			dealThrow(he,"");
			
		}catch (Exception e) {
			
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getInvite method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <根据物理主键获取实体bean> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void getDomainByDomainId(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getDomainByDomainId method.");			
		}
		try {
			user = (User) super.getCurrentLoginUser();
			if(user!=null){
				long domainId=user.getDomain().getId();
				Domain domain=facade.getDomainByDomainId(domainId);
				fillActionResult(domain);
			}
		} catch (HsCloudException he) {
			
			dealThrow(he,"");
			
		}catch (Exception e) {
			
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getDomainByDomainId method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * <获取客户平台用户信息> 
	* <请求客户方server>  
	* @see [类、类#方法、类#成员]
	 */
	public void getOuterInfomation() {
		try{			
			Object result = facade.getUserInfomation(userId,domainCode);
			this.fillActionResult(result);
		} catch(Exception e) {
			this.dealThrow(Constants.DOMAIN_COPYRIGHT_EXCEPTION, e, logger);
		}
	}
	/**
	 * <获取客户平台用户信息> 
	* <根据本地数据库信息获取>  
	* @see [类、类#方法、类#成员]
	 */
	public void getExternalUserOfLocal() {
		try{
			user = (User) super.getCurrentLoginUser();
			Object result = facade.getExternalUserOfLocal(user);
			this.fillActionResult(result);
		} catch(Exception e) {
			this.dealThrow(Constants.DOMAIN_COPYRIGHT_EXCEPTION, e, logger);
		}
	}


	public void getUserVO(){
		user = (User) super.getCurrentLoginUser();
		UserVO1 userVO = facade.getUserVO(user.getId());
		fillActionResult(userVO);
		
	}
	public String getResourceType() {
		return resourceType;
	}
	


	public Logger getLogger() {
		return logger;
	}


	public void setLogger(Logger logger) {
		this.logger = logger;
	}


	public String getCompanyName() {
		return companyName;
	}


	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}


	public String getOldPwd() {
		return oldPwd;
	}


	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getMailAddress() {
		return mailAddress;
	}


	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}


	public String getUser_type() {
		return user_type;
	}


	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}


	public String getGroupIds() {
		return groupIds;
	}


	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}


	public String getGroupId() {
		return groupId;
	}


	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getQuery() {
		return query;
	}


	public void setQuery(String query) {
		this.query = query;
	}


	public String getActiveFlag() {
		return activeFlag;
	}


	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}


	public String getInviteId() {
		return inviteId;
	}


	public void setInviteId(String inviteId) {
		this.inviteId = inviteId;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public int getPage() {
		return page;
	}


	public void setPage(int page) {
		this.page = page;
	}


	public int getStart() {
		return start;
	}


	public void setStart(int start) {
		this.start = start;
	}


	public int getLimit() {
		return limit;
	}


	public void setLimit(int limit) {
		this.limit = limit;
	}


	public String getSort() {
		return sort;
	}


	public void setSort(String sort) {
		this.sort = sort;
	}


	public Page<User> getPageUser() {
		return pageUser;
	}


	public void setPageUser(Page<User> pageUser) {
		this.pageUser = pageUser;
	}

	private List<Sort> parseSort() throws Exception {
		return Utils.json2Object(sort, new TypeReference<List<Sort>>() {});
	}
	public long getDomainId() {
		return domainId;
	}
	public void setDomainId(long domainId) {
		this.domainId = domainId;
	}
	
	public UserProfileVo getUserProfileVo() {
		return userProfileVo;
	}
	
	public void setUserProfileVo(UserProfileVo userProfileVo) {
		this.userProfileVo = userProfileVo;
	}
	public String getDomainCode() {
		return domainCode;
	}
	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}
	public String getExternalUser() {
		return externalUser;
	}
	public void setExternalUser(String externalUser) {
		this.externalUser = externalUser;
	}
	public String getLocalUser() {
		return localUser;
	}
	public void setLocalUser(String localUser) {
		this.localUser = localUser;
	}
	

}

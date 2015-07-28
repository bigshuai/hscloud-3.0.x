package com.hisoft.hscloud.web.action;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.util.Utils;
import com.hisoft.hscloud.crm.usermanager.vo.AdminVO;
import com.hisoft.hscloud.web.facade.Facade;
public class AdminManagementAction extends HSCloudAction {
	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = 5421332382829271061L;
	private String code;
	private Admin admin;
	@Autowired
	private Facade facade;
	private int page;
	private int start;
	private int limit;
	private String sort;
	private String query;
	private String type;
	private String name;
	private String email;
	private String telephone;
	private String password;
	private long adminId;
	private Long roleId;
	private Integer adminType;
	private String resourceType="com.hisoft.hscloud.crm.usermanager.entity.Admin";
	private Long adminRoleId;
	private static final Logger logger = Logger
			.getLogger(AdminManagementAction.class);
	/**
	 * <获取所有后台管理员（除超级管理员）> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void getAllAdminUser() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAllAdminUser method.");			
		}
		try{
			boolean b = null!=query;
			if(b){
				query=new String(query.getBytes("iso8859_1"),"UTF-8");
			}
			List<Sort> sorts = Utils.json2Object(sort, new TypeReference<List<Sort>>() {});
			Page<AdminVO> admins=facade.getAllAdminUser(sorts,query,start,limit,page,type);
			fillActionResult(admins);
		}catch(Exception e){
			dealThrow(new HsCloudException(Constants.GET_ALL_AMIND_ERROR, "获取admin用户列表异常getAllAdminUser",
					logger, e) ,"000");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAllAdminUser method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <添加管理员> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void addAdmin(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter addAdmin method.");			
		}
		try{
			if(facade.getAdminByEmail(email)==null){
				facade.addAdmin(password,telephone,name,email,roleId,adminType);
			}else{
				fillActionResult(Constants.EMAIL_HAD_HEEN_USED);
			}
			
		}catch(HsCloudException hse){
			logger.error("login error:",hse);
			dealThrow(hse, hse.getCode());
		}catch(Exception e){
			dealThrow(new HsCloudException(Constants.ADD_ADMIN_ERROR, "添加admin异常 addAdmin",
					logger, e) ,"000");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit addAdmin method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <修改管理员> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void modifyAdmin(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter modifyAdmin method.");			
		}
		try{
			facade.modifyAdmin(telephone,adminId,roleId,adminRoleId,adminType);
		}catch(Exception e){
			dealThrow(new HsCloudException(Constants.MODIFY_ADMIN_ERROR, "修改admin异常 modifyAdmin",
					logger, e) ,"000");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit modifyAdmin method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <逻辑删除管理员> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void deleteAdmin(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter deleteAdmin method.");			
		}
		try{
			facade.deleteAdmin(adminId);
		}catch(Exception e){
			dealThrow(new HsCloudException(Constants.DELETE_ADMIN_ERROR, "删除admin异常 deleteAdmin",
					logger, e) ,"000");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit deleteAdmin method.takeTime:" + takeTime + "ms");
		}
	}
	
	public void freezedAdmin(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter freezedAdmin method.");			
		}
		try{
			facade.freezedAdmin(adminId);
		}catch(Exception e){
			dealThrow(new HsCloudException(Constants.DELETE_ADMIN_ERROR, "冻结admin异常 deleteAdmin",
					logger, e) ,"000");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit freezedAdmin method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <解冻管理员> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void enableAdmin(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter enableAdmin method.");			
		}
		try{
			facade.enableAdmin(adminId);
		}catch(Exception e){
			dealThrow(new HsCloudException(Constants.ENABLE_ADMIN_ERROR, "启用admin异常 enableAdmin",
					logger, e) ,"000");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit enableAdmin method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <重置管理员密码> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void resetPassword(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter resetPassword method.");			
		}
		try{
			facade.resetPasswd(adminId,password);
		}catch(HsCloudException hse){
			logger.error("login error:",hse);
			dealThrow(hse, hse.getCode());
		}catch(Exception e){
			dealThrow(new HsCloudException(Constants.RESET_ADMIN_PASSWD_ERROR, "重置密码异常 resetPassword",
					logger, e) ,"000");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit resetPassword method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <后台登陆> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void adminLoginByEmail(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter adminLoginByEmail method.");			
		}
		try {
			//检查验证码是否正确
			String sessionCode = (String)ServletActionContext.getRequest().getSession().getAttribute("code");
			if(null == this.code || "".equals(this.code)){
				fillActionResult(Constants.VERCODE_IS_NULL);
				return ;
			}else if( null == sessionCode || !sessionCode.equalsIgnoreCase(this.code)){
				fillActionResult(Constants.VERCODE_IS_ERROR);
				return ;
			}
			
			admin = Utils.strutsJson2Object(Admin.class);
			logger.info("userLoginByEmail");
			Admin loginAdmin = facade.loginUserByEmail(admin.getEmail(),admin.getPassword());
			Object urlStr = super.getSession().getAttribute("urlStr");
			if(loginAdmin!=null){
				if(loginAdmin.isApproved()){
					//为了避免一个账号同时多次登陆，需要把登陆状态缓存为application级别
					ServletContext sc = super.getApplication();
					String email = loginAdmin.getEmail();
					HttpSession session = null;
					Object sessionObject = sc.getAttribute(email);
					//已经有账号登陆
					if(sessionObject != null)
						session = (HttpSession)sessionObject;
					//踢出已经登陆的账号
					if(null != session){
						session=null;
					}
					//把自己登陆的状态存到application中，以便保证一个账号同时只有一个登陆
					HttpSession currentSession = super.getSession();
					currentSession.setAttribute(Constants.LOGIN_CURRENTUSER, loginAdmin);
					sc.setAttribute(email, currentSession);
	
					fillActionResult(urlStr);
				}else{
					fillActionResult(Constants.ACCOUNT_IS_ENABLE);
				}
			}else{
				fillActionResult(Constants.ACCOUNT_NOT_FOUND);
			}
		} catch (Exception e) {
			dealThrow(new HsCloudException(Constants.ADMIN_LOGIN_ERROR, "adminLoginByEmail exception",
					logger, e) ,"000");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit adminLoginByEmail method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <获取当前登陆用户> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void getSessionUser(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getSessionUser method.");			
		}
		try {
			admin = (Admin) getCurrentLoginUser();
			if (admin != null) {
				fillActionResult(admin);
			}
		} catch (Exception e) {
			dealThrow(new HsCloudException(Constants.GET_CURRENT_USER_ERROR, "adminLoginByEmail exception",
					logger, e) ,"000");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getSessionUser method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * <登陆退出> 
	* <功能详细描述> 
	* @throws Exception 
	* @see [类、类#方法、类#成员]
	 */
	public void logout()throws Exception{
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter logout method.");			
		}
		try{
			super.getSession().setAttribute(Constants.LOGIN_CURRENTUSER,null);
		}catch(Exception e){
			dealThrow(new HsCloudException(Constants.ADMIN_LOGOUT_ERROR, "adminLoginByEmail exception",
					logger, e) ,"000");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit logout method.takeTime:" + takeTime + "ms");
		}
    }
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getAdminRoleId() {
		return adminRoleId;
	}

	public void setAdminRoleId(Long adminRoleId) {
		this.adminRoleId = adminRoleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getAdminId() {
		return adminId;
	}

	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
	public Integer getAdminType() {
		return adminType;
	}
	public void setAdminType(Integer adminType) {
		this.adminType = adminType;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	
	
}

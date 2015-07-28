package com.hisoft.hscloud.web.action;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.constant.UserType;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.Country;
import com.hisoft.hscloud.crm.usermanager.entity.Industry;
import com.hisoft.hscloud.crm.usermanager.entity.Region;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.util.Utils;
import com.hisoft.hscloud.crm.usermanager.vo.UserProfileVo;
import com.hisoft.hscloud.crm.usermanager.vo.UserVO;
import com.hisoft.hscloud.web.facade.Facade;

/**
 * 
 * <一句话功能简述> <功能详细描述>
 * 
 * @author houyh
 * @version [1.4, 2012-10-8]
 * @see [相关类/方法]
 * @since [HSCLOUD/1.4]
 */
public class UserManagementAction extends HSCloudAction {
	private static final long serialVersionUID = -2186719250312172886L;
	private User user;
	private String sort;
	private int start;
	private String company;
	private int limit;
	private String type;
	private String query;
	private String userType;
	private int page;
	private long country_id;
	private Page<User> paging;
	private final String resourceType="com.hisoft.hscloud.crm.usermanager.entity.Admin";
	private String externalUser;
	private String domainCode;
	private String localUser;
	private long userId;
	private String email;
	private boolean supplieStatus;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	@Autowired
	private Facade facade;
	private static Logger logger = Logger.getLogger(UserManagementAction.class
			.getName());
	/**
	 * <分页获取用户信息> 
	* <功能详细描述>  
	* @see [UserManagementAction、UserManagementAction#getAllUserByPage、类#成员]
	 */
	public void getAllUserByPage() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAllUserByPage method.");			
		}
		paging = new Page<User>(limit);
		paging.setPageNo(page);
		List<Sort> sorts = null;
		Admin admin=(Admin)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
		try {
			sorts = Utils.json2Object(sort, new TypeReference<List<Sort>>() {
			});
			if(null != query){
				query = new String(query.getBytes("iso8859_1"),"UTF-8");
			}
			fillActionResult(facade.getAllUserByPage(admin,sorts, paging, 0L,type,query));
		} catch (Exception e) {
			dealThrow(new HsCloudException(Constants.GET_USER_BY_PAGE_ERROR, "获取用户列表异常getAllUserByPage",
					logger, e), "000");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAllUserByPage method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <获取用户国家信息> 
	* <功能详细描述>  
	* @see [UserManagementAction、UserManagementAction#loadCountry、类#成员]
	 */
	public void loadCountry() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter loadCountry method.");			
		}
		try {
			List<Country> countries = facade.loadCountry();
			fillActionResult(countries);
		} catch (Exception e) {
			dealThrow(new HsCloudException(Constants.LOAD_COUNTRY_ERROR, "获取Country信息异常loadCountry",
					logger, e), "000");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit loadCountry method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <获取用户行业信息> 
	* <功能详细描述>  
	* @see [UserManagementAction、UserManagementAction#loadIndustry、类#成员]
	 */
	public void loadIndustry() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter loadIndustry method.");			
		}
		try {
			List<Industry> industries = facade.loadIndustry();
			fillActionResult(industries);
		} catch (Exception e) {
			dealThrow(new HsCloudException(Constants.LOAD_INDUSTRY_ERROR, "获取行业信息异常loadIndustry",
					logger, e), "000");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit loadIndustry method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <获取用户地区信息> 
	* <功能详细描述>  
	* @see [UserManagementAction、UserManagementAction#loadRegion、类#成员]
	 */
	public void loadRegion() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter loadRegion method.");			
		}
		try {
			List<Region> regions = facade.loadRegion(country_id);
			fillActionResult(regions);
		} catch (Exception e) {
			dealThrow(new HsCloudException(Constants.LOAD_REGION_ERROR, "获取region信息异常loadRegion",
					logger, e), "000");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit loadRegion method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <根据用户Id冻结用户> 
	* <功能详细描述>  
	* @see [UserManagementAction、UserManagementAction#deleteUserByUserId、类#成员]
	 */
	public void deleteUserByUserId() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter deleteUserByUserId method.");			
		}
		try {
			user = Utils.strutsJson2Object(User.class);
			user.setUpdateDate(new Date());
			facade.deleteUser(user.getId());
		} catch (Exception e) {
			dealThrow(new HsCloudException(Constants.DELETE_USER_ERROR, "删除用户异常deleteUserByUserId",
					logger, e), "000");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit deleteUserByUserId method.takeTime:" + takeTime + "ms");
		}
	}
	
	public void freezedUser(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter freezedUser method.");			
		}
		try {
			user = Utils.strutsJson2Object(User.class);
			user.setUpdateDate(new Date());
			facade.freezedUser(user.getId());
		} catch (Exception e) {
			dealThrow(new HsCloudException(Constants.DELETE_USER_ERROR, "冻结用户异常deleteUserByUserId",
					logger, e), "000");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit freezedUser method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <根据用户Id解冻用户> 
	* <功能详细描述>  
	* @see [UserManagementAction、UserManagementAction#enableUser、类#成员]
	 */
	public void enableUser(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter enableUser method.");			
		}
		try {
			user = Utils.strutsJson2Object(User.class);
			user.setUpdateDate(new Date());
			facade.enableUser(user.getId());
		} catch (Exception e) {
			dealThrow(new HsCloudException(Constants.ENABLE_USER_ERROR, "启用用户异常enableUser",
					logger, e), "000");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit enableUser method.takeTime:" + takeTime + "ms");
		}
	}
	
	public void approvedUser(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter approvedUser method.");			
		}
		try {
			user = Utils.strutsJson2Object(User.class);
			facade.approvedUser(user.getId());
			long userId=user.getId();
			Object result = facade.synchroExternalUser2(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit approvedUser method.takeTime:" + takeTime + "ms");
		}
	}
	
	
	public void supplieApproved(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter supplieApproved method.");			
		}
		try {
			facade.supplieApproved(userId,supplieStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit supplieApproved method.takeTime:" + takeTime + "ms");
		}
	}
	

	
	/**
	 * <重置用户密码> 
	* <功能详细描述>  
	* @see [UserManagementAction、UserManagementAction#resetPasswd、类#成员]
	 */
	public void resetPasswd() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter resetPasswd method.");			
		}
		try {
			user = Utils.strutsJson2Object(User.class);
			facade.resetPasswd(user.getPassword(), user.getId());
		} catch (Exception e) {
			dealThrow(new HsCloudException(Constants.RESET_USER_PASSWD_ERROR, "重置密码异常resetPasswd", logger,
					e), "000");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit resetPasswd method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <一句话功能简述> 
	* <功能详细描述>  
	* @see [UserManagementAction、UserManagementAction#addUser、类#成员]
	 */
	public void addUser() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter addUser method.");			
		}
		try {
			if (userType.equals(UserType.ENTERPRISE_USER.getType())) {
				User entUser = Utils.strutsJson2Object(User.class);
				entUser.setUserType(UserType.ENTERPRISE_USER.getType());
				user = entUser;
			} else if (userType.equals(UserType.PERSONAL_USER.getType())) {
				User norUser = Utils.strutsJson2Object(User.class);
				norUser.setUserType(UserType.PERSONAL_USER.getType());
				user = norUser;
			} else {
				fillActionResult(Constants.REGISTER_IS_ERROR);
			}

			if (facade.getUserByEmail(user.getEmail()) == null) {
				//URLDecoder.decode(company, "UTF-8");
				company = new String(company.getBytes("iso8859_1"),"UTF-8");
				facade.addUser(user, company);
			} else {
				fillActionResult(Constants.EMAIL_HAD_HEEN_USED);
			}

		} catch(HsCloudException hse){
			logger.error("login error:",hse);
			dealThrow(hse, hse.getCode());
		}catch (Exception e) {
			dealThrow(new HsCloudException(Constants.ADD_USER_ERROR, "添加用户异常addUser", logger, e),
					"000");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit addUser method.takeTime:" + takeTime + "ms");
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
	    try {
	    	
			User u = Utils.strutsJson2Object(User.class);
			if(facade.getUserByEmail(u.getEmail())==null){
				fillActionResult(true);
			}else{
				fillActionResult(Constants.EMAIL_HAD_HEEN_USED);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	    if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit duplicateEmail method.takeTime:" + takeTime + "ms");
		} 
		
	}
	/**
	 * <一句话功能简述> 
	* <功能详细描述>  
	* @see [UserManagementAction、UserManagementAction#方法、类#成员]
	 */
	public void modifyUserProfile(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter modifyUserProfile method.");			
		}
		try{
			UserProfileVo userProfileVo=Utils.strutsJson2Object(UserProfileVo.class);
			facade.modifyUserProfile(userProfileVo);
		}catch(HsCloudException hse){
			logger.error("login error:",hse);
			dealThrow(hse, hse.getCode());
		}catch(Exception e){
			dealThrow(new HsCloudException(Constants.MODIFY_USER_ERROR, "修改用户信息异常modifyUserProfile", logger, e),
					"000");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit modifyUserProfile method.takeTime:" + takeTime + "ms");
		}
	}
	public String getAllAvailableUser(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAllAvailableUser method.");			
		}
		List <UserVO> list=null;
        try{
        	if(StringUtils.isNotEmpty(query)){
				query = new String(query.getBytes("iso8859_1"),"UTF-8");
			}           
        	list = facade.getAllAvailableUser(query);
        }catch (Exception ex) {
            dealThrow(new HsCloudException(Constants.GET_USER_BY_PAGE_ERROR, "查询异常getAllAvailableUser", logger, ex),
                    "00000");
        }
        super.fillActionResult(list);
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAllAvailableUser method.takeTime:" + takeTime + "ms");
		}
        return null;
	}
	
	/**
	 * <重发激活邮件> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void reActivation() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter reActivation method.");			
		}
	    try {
            user = Utils.strutsJson2Object(User.class);
            facade.reActivation(user.getId());
        } catch (Exception e) {
            this.dealThrow(Constants.USER_RE_ACTIVATION_EMAIL, "重发激活邮件异常", e, logger);
        }
	    if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit reActivation method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * <获取客户平台用户信息> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void getOuterInfomation() {
		try{
				Object result = facade.getOuterInfomation(externalUser,domainCode);
			this.fillActionResult(result);
		} catch(Exception e) {
			this.dealThrow(Constants.DOMAIN_COPYRIGHT_EXCEPTION, e, logger);
		}
	}
	/**
	 * <同步客户平台用户信息> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void synchroExternalUser(){
		try{
			Object result = facade.synchroExternalUser(externalUser,email,userId,domainCode);
		this.fillActionResult(result);
	} catch(Exception e) {
		this.dealThrow(Constants.DOMAIN_COPYRIGHT_EXCEPTION, e, logger);
	}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
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

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public Page<User> getPaging() {
		return paging;
	}

	public void setPaging(Page<User> paging) {
		this.paging = paging;
	}

	public long getCountry_id() {
		return country_id;
	}

	public void setCountry_id(long country_id) {
		this.country_id = country_id;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	public String getResourceType() {
		return resourceType;
	}
	public String getExternalUser() {
		return externalUser;
	}
	public void setExternalUser(String externalUser) {
		this.externalUser = externalUser;
	}
	public String getDomainCode() {
		return domainCode;
	}
	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}
	public String getLocalUser() {
		return localUser;
	}
	public void setLocalUser(String localUser) {
		this.localUser = localUser;
	}
	public boolean isSupplieStatus() {
		return supplieStatus;
	}
	public void setSupplieStatus(boolean supplieStatus) {
		this.supplieStatus = supplieStatus;
	}
	
	
	
}

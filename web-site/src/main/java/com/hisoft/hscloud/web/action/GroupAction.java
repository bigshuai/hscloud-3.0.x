package com.hisoft.hscloud.web.action;

import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;
import org.springside.modules.web.struts2.Struts2Utils;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.entity.Action;
import com.hisoft.hscloud.crm.usermanager.entity.ResourceType;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.entity.UserGroup;
import com.hisoft.hscloud.crm.usermanager.util.Utils;
import com.hisoft.hscloud.crm.usermanager.vo.PrivilegeVO;
import com.hisoft.hscloud.crm.usermanager.vo.UserGroupVO;
import com.hisoft.hscloud.web.facade.Facade;

public class GroupAction extends HSCloudAction {
	
	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = -1059757570270006018L;

	private Logger logger = Logger.getLogger(this.getClass());
	
	private  final String resourceType = "com.hisoft.hscloud.crm.usermanager.entity.UserGroup";
	
	//组id
	private String groupId;
	
	//修改子用户时所属组，用，隔开
	private String groupIds;
	
	//更改用户组id
    private String userId;
	
	// 模糊查询条件
	private String query;
	
	//组名称
	private String groupName;
	
	private int page;
	
	private int start;
	
	private int limit;
	
	private String sort;
	
	private Page<UserGroup> pageUserGroup = new Page<UserGroup>(Constants.PAGE_NUM);
	
	private UserGroup group;
	
	private String type;// 查询类型
	
	private Page<PrivilegeVO> pagePrivilege = new Page<PrivilegeVO>(Constants.PAGE_NUM);
	
	private String actionValue;
    private String permissionValue;
    private String resourceValue;
	
	@Autowired
	private Facade facade;
	
	
	/**
	 * 分页模糊查询组
	 */
	public void pageUserGroup(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter pageUserGroup method.");			
		}
		pageUserGroup = new Page<UserGroup>(limit);
		pageUserGroup.setPageNo(page);
		pageUserGroup.setOrder(Page.DESC);
		pageUserGroup.setOrderBy("id");
		User user = (User)super.getCurrentLoginUser();
		try {
			
			Page<UserGroupVO> userGroupVOPage= facade.fuzzySearchPermissionUserGroup(query, parseSort(), pageUserGroup, user.getId(),super.getPrimKeys());
			fillActionResult(userGroupVOPage);
			
		} catch (HsCloudException hce){
			dealThrow(hce, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit pageUserGroup method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 根据当前用户获得有权限分配的用户组
	 */
	public void getUserGroup(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getUserGroup method.");			
		}
		try{
			
			User user = (User)super.getCurrentLoginUser();
			List<UserGroupVO> userGroupVOs = facade.getPermissionUserGroupVO(user.getId(),super.getPrimKeys());
			fillActionResult(userGroupVOs);
			
		} catch (HsCloudException hce){
			dealThrow(hce, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getUserGroup method.takeTime:" + takeTime + "ms");
		}
	}
	
	public void getUserGroupById(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getUserGroupById method.");			
		}
		try{
			
			UserGroup userGroup = facade.getUserGroupById(groupId);
			fillActionResult(userGroup);
		
		} catch (HsCloudException hce){
			dealThrow(hce, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getUserGroupById method.takeTime:" + takeTime + "ms");
		}
	}
	
	public void createGroup(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter createGroup method.");			
		}
		try{
			
			User user = (User)super.getCurrentLoginUser();
			group=Utils.strutsJson2Object(UserGroup.class);
			facade.createUserGroup(group.getName(),user.getId());
			fillActionResult(true);
		
		}catch (HsCloudException hce){
			dealThrow(hce, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit createGroup method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 更改用户所属组
	 */
	public void changeGroupForUer(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter changeGroupForUer method.");			
		}
		try{
			
			User user = (User)super.getCurrentLoginUser();
			facade.changeGroupForUer(user.getId(),userId, groupIds);
			fillActionResult(true);
		
		}catch (HsCloudException hce){
			dealThrow(hce, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit changeGroupForUer method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 根据userGroup的id删除
	 */
	public void deleteUserGroup(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter deleteUserGroup method.");			
		}
		try {
			
			facade.deleteUserGroupById(groupId);
			fillActionResult(true);
			
		}catch (HsCloudException hce){
			dealThrow(hce, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit deleteUserGroup method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 判断同一公司下是否有同名组（同一公司下只能组名为一）
	 */
	public void duplicateUserGroup(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter duplicateUserGroup method.");			
		}
		User user = (User)super.getCurrentLoginUser();
		try {
			
			group=Utils.strutsJson2Object(UserGroup.class);
			boolean flag = facade.duplicateUserGroup(user.getId(),group.getName());
			fillActionResult(flag);
			
		}catch (HsCloudException hce){
			dealThrow(hce, "");
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit duplicateUserGroup method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
     * 查询资源类型list 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
	public void getResourceTypeList() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getResourceTypeList method.");			
		}
    	try{
    		List<ResourceType> list = facade.getResourceTypeList();
    		JSONArray jsonArray = JSONArray.fromObject(list);
            Struts2Utils.renderText(jsonArray.toString());
            setActionResult(null);
    	} catch(Exception e){
    		dealThrow(new HsCloudException(Constants.ROLE_RESOURCE_TYPE_EXCEPTION, 
					logger, e), Constants.ROLE_EXCEPTION);
		}
    	if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getResourceTypeList method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
     * 获取类型对应的action集合 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void getActionList(){
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getActionList method.");			
		}
    	try{
    		List<Action> list = facade.getActionList(type);
    		JSONArray jsonArray = JSONArray.fromObject(list);
            Struts2Utils.renderText(jsonArray.toString());
            setActionResult(null);
    	} catch(Exception e){
    		dealThrow(new HsCloudException(Constants.ROLE_ACTION_EXCEPTION, 
					logger, e), Constants.ROLE_EXCEPTION);
		}
    	if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getActionList method.takeTime:" + takeTime + "ms");
		}
    }
    
    public void findUnassignedList() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findUnassignedList method.");			
		}
    	try{
    		if (!StringUtils.isEmpty(query)) { // 如果查询框不为空时，进入模糊查询
                query = new String(query.getBytes("iso8859_1"), "UTF-8");
            }
    		
    		pagePrivilege.setPageNo(page);
    		List<PrivilegeVO> list = facade.findUnassignedList(type, Long.valueOf(groupId), pagePrivilege, query, getPrimKeys());
    		pagePrivilege.setResult(list);
            fillActionResult(pagePrivilege);
    	} catch(Exception e){
    		dealThrow(new HsCloudException(Constants.ROLE_FIND_EXCEPTION, 
					logger, e), Constants.ROLE_EXCEPTION);
		}
    	if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findUnassignedList method.takeTime:" + takeTime + "ms");
		}
    }
    
    public void findAssignedList() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findAssignedList method.");			
		}
    	try{
    		if (!StringUtils.isEmpty(query)) { // 如果查询框不为空时，进入模糊查询
                query = new String(query.getBytes("iso8859_1"), "UTF-8");
            }
    		
    		pagePrivilege.setPageNo(page);
    		List<PrivilegeVO> list = facade.findAssignedList(type, Long.valueOf(groupId), pagePrivilege, query, getPrimKeys());
    		pagePrivilege.setResult(list);
            fillActionResult(pagePrivilege);
    	} catch(Exception e){
    		dealThrow(new HsCloudException(Constants.ROLE_FIND_EXCEPTION, 
					logger, e), Constants.ROLE_EXCEPTION);
		}
    	if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findAssignedList method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 统一定义查询
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void findUiformDefList() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findUiformDefList method.");			
		}
    	try{
    		List<PrivilegeVO> list = facade.findUiformDefList(type, Long.valueOf(groupId));
    		
    		pagePrivilege.setResult(list);
    		pagePrivilege.setTotalCount(1l);
            fillActionResult(pagePrivilege);
    	} catch(Exception e){
    		dealThrow(new HsCloudException(Constants.ROLE_FIND_EXCEPTION, 
					logger, e), Constants.ROLE_EXCEPTION);
		}
    	if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findUiformDefList method.takeTime:" + takeTime + "ms");
		}
    }
	
    public void addPermission() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter addPermission method.");			
		}
    	try{
    		facade.modifyRolePermissiion(permissionValue, actionValue, resourceValue, Long.valueOf(groupId));
    	//	Struts2Utils.renderJson(this.getActionResult());
    	} catch(Exception e){
    		dealThrow(new HsCloudException(Constants.ROLE_MODIFY_EXCEPTION, 
					logger, e), Constants.ROLE_EXCEPTION);
		}
    	if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit addPermission method.takeTime:" + takeTime + "ms");
		}
    }
	
	private List<Sort> parseSort() throws Exception {
		return Utils.json2Object(sort, new TypeReference<List<Sort>>() {});
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
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

	public Page<UserGroup> getPageUserGroup() {
		return pageUserGroup;
	}

	public void setPageUserGroup(Page<UserGroup> pageUserGroup) {
		this.pageUserGroup = pageUserGroup;
	}

	public UserGroup getGroup() {
		return group;
	}

	public void setGroup(UserGroup group) {
		this.group = group;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Page<PrivilegeVO> getPagePrivilege() {
		return pagePrivilege;
	}

	public void setPagePrivilege(Page<PrivilegeVO> pagePrivilege) {
		this.pagePrivilege = pagePrivilege;
	}

	public String getActionValue() {
		return actionValue;
	}

	public void setActionValue(String actionValue) {
		this.actionValue = actionValue;
	}

	public String getPermissionValue() {
		return permissionValue;
	}

	public void setPermissionValue(String permissionValue) {
		this.permissionValue = permissionValue;
	}

	public String getResourceValue() {
		return resourceValue;
	}

	public void setResourceValue(String resourceValue) {
		this.resourceValue = resourceValue;
	}
}

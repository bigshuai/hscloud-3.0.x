/* 
 * 文 件 名:  RoleAction.java 
 * 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
 * 描    述:  <描述> 
 * 修 改 人:  lihonglei 
 * 修改时间:  2012-10-9 
 * 跟踪单号:  <跟踪单号> 
 * 修改单号:  <修改单号> 
 * 修改内容:  <修改内容> 
 */
package com.hisoft.hscloud.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;
import org.springside.modules.web.struts2.Struts2Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.Action;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.ResourceType;
import com.hisoft.hscloud.crm.usermanager.entity.Role;
import com.hisoft.hscloud.crm.usermanager.util.Constant;
import com.hisoft.hscloud.crm.usermanager.util.PropertiesUtils;
import com.hisoft.hscloud.crm.usermanager.util.Utils;
import com.hisoft.hscloud.crm.usermanager.vo.AdminMenuVO;
import com.hisoft.hscloud.crm.usermanager.vo.CheckboxVO;
import com.hisoft.hscloud.crm.usermanager.vo.PrivilegeVO;
import com.hisoft.hscloud.crm.usermanager.vo.RoleVO;
import com.hisoft.hscloud.web.facade.Facade;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author lihonglei
 * @version [1.4, 2012-10-9]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class RoleAction extends HSCloudAction {

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 2281338614597976365L;

    private Logger logger = Logger.getLogger(RoleAction.class);

    private Role role = new Role();
    private String sort;
    private int start;
    private int limit;
    private int page;

    private String type;// 查询类型
    private String query;// 模糊查询条件
    private String parentId;
    private Page<Role> pageRole = new Page<Role>(Constants.PAGE_NUM);
    private Page<Admin> pageAdmin = new Page<Admin>(Constants.PAGE_NUM);
    
    private final String resourceType="com.hisoft.hscloud.crm.usermanager.entity.Menu";
    
    
    @SuppressWarnings("rawtypes")
	private Page pagePrivilege = new Page(Constants.PAGE_NUM);
    
    private String kind;
    
    private String privilegesStr;
    
    private String actionValue;
    private String permissionValue;
    private String resourceValue;

    @Autowired
    private Facade facade;

    /**
     * 
     * @title: createRole
     * @description 创建角色
     * @return void 返回类型
     * @throws
     * @version 1.1
     * @author guole.liang
     * @update 2012-5-10 下午3:54:07
     */
    public void createRole() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter createRole method.");			
		}
		Admin admin = null;
        try {
        	RoleVO roleVO = Utils.strutsJson2Object(RoleVO.class);
        	role.setName(roleVO.getName());
        	privilegesStr = roleVO.getPrivilegesStr();
            admin = (Admin) this.getCurrentLoginUser();
            if (admin == null) {
                fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
                return;
            }
            role.setCreateId(admin.getId());
            String result = facade.createRole(role, privilegesStr);
            fillActionResult((Object)result);
            facade.insertOperationLog(admin,"创建角色","创建角色",Constants.RESULT_SUCESS);
		} catch (Exception ex) {
		    facade.insertOperationLog(admin,"创建角色错误:"+ex.getMessage(),"创建角色",Constants.RESULT_FAILURE);
		    dealThrow(new HsCloudException(Constants.ROLE_CREATE_EXCEPTION,
					"createRole异常", logger, ex), Constants.ROLE_EXCEPTION);
		}
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit createRole method.takeTime:" + takeTime + "ms");
		}
    }

    /**
     * 
     * @title: getRoleById
     * @description 根据角色id，查询角色
     * @return void 返回类型
     * @throws
     * @version 1.1
     * @author guole.liang
     * @update 2012-5-10 下午3:54:21
     */
    public void getRoleById() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getRoleById method.");			
		}
        try {
            role = Utils.strutsJson2Object(Role.class);
            fillActionResult(facade.getRoleById(role.getId()));
		} catch (Exception e) {
			dealThrow(new HsCloudException(Constants.ROLE_FIND_EXCEPTION,
					"getRoleById异常", logger, e), Constants.ROLE_EXCEPTION);
		}
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getRoleById method.takeTime:" + takeTime + "ms");
		}
    }

    /**
     * 
     * @title: page
     * @description 分页显示角色，不返回角色的权限
     * @return void 返回类型
     * @throws
     * @version 1.1
     * @author guole.liang
     * @throws Exception
     * @update 2012-5-10 下午3:54:43
     */
    public void page() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter page method.");			
		}
        pageRole = new Page<Role>(limit);
        pageRole.setPageNo(page);
        pageRole.setOrder(Page.DESC);
        pageRole.setOrderBy("id");

        try {
            if (!StringUtils.isEmpty(query)) { // 如果查询框不为空时，进入模糊查询
                query = new String(query.getBytes("iso8859_1"), "UTF-8");
            }
            pageRole = facade.getAllRoleByPage(pageRole, query);
            fillActionResult(pageRole);
        } catch (Exception e) {
            dealThrow(new HsCloudException("A007", "page异常", logger, e), "000");
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit page method.takeTime:" + takeTime + "ms");
		}
    }
    
    public void checkPermission() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter checkPermission method.");			
		}
    	boolean result = false;
    	
    	Admin admin = (Admin) this.getCurrentLoginUser();
    	if(admin.getIsSuper()) {
    		result = true;
        } else {
        	Role roleTemp = facade.getRoleByAdminId(admin.getId());
    		if(roleTemp != null && Constants.SUPER_ROLE.equalsIgnoreCase(roleTemp.getCode())) {
    			result = true;
    		}
        }
        if(result == false) {
        	result = facade.checkPermission(admin.getId());
        }
    	
    	fillActionResult(result);
    	if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit checkPermission method.takeTime:" + takeTime + "ms");
		}
    }

    /**
     * 
     * @title: delete
     * @description 删除角色
     * @return void 返回类型
     * @throws
     * @version 1.1
     * @author guole.liang
     * @update 2012-5-17 下午2:11:38
     */
    public void delete() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter delete method.");			
		}
		Admin admin=null;
		try {
		    admin = (Admin) super.getCurrentLoginUser();
		    role = Utils.strutsJson2Object(Role.class);
			String result = facade.deleteRole(role.getId());
			fillActionResult((Object) result);
			facade.insertOperationLog(admin,"删除角色","删除角色",Constants.RESULT_SUCESS);
		} catch (Exception e) {
		    facade.insertOperationLog(admin,"删除角色错误:"+e.getMessage(),"删除角色",Constants.RESULT_FAILURE);
		    dealThrow(new HsCloudException(Constants.ROLE_DELETE_EXCEPTION,
					"delete异常", logger, e), Constants.ROLE_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit delete method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 根据角色获取用户 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void pageAdminByRoleId() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter pageAdminByRoleId method.");			
		}
        pageAdmin = new Page<Admin>(limit);
        pageAdmin.setPageNo(page);
        pageAdmin.setOrder(Page.DESC);
        pageAdmin.setOrderBy("id");
        try {
            pageAdmin = facade.pageAdminByRoleId(pageAdmin, role.getId());
            fillActionResult(pageAdmin);
        } catch (Exception e) {
            dealThrow(new HsCloudException("A007", "pageAdminByRoleId异常", logger, e), "000");
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit pageAdminByRoleId method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 显示菜单 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void getAdminMenu() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAdminMenu method.");			
		}
    	List<Object> referenceIds = super.getPrimKeys();
    	
    	List<AdminMenuVO> list = null;
    	try {
	    	if(referenceIds != null && referenceIds.isEmpty()) {
	    		list = new ArrayList<AdminMenuVO>();
	    	} else {
	    		list = facade.getAdminMenu(parentId, referenceIds);
	    	}
    	} catch (Exception e) {
			dealThrow(new HsCloudException(Constants.ROLE_FIND_EXCEPTION,
					"getPermissionTree异常", logger, e), Constants.ROLE_EXCEPTION);
		}
    	
    	JSONArray jsonArray = JSONArray.fromObject(list);
        Struts2Utils.renderText(jsonArray.toString());
        setActionResult(null);
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAdminMenu method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * <创建管理员可供选择的角色> 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void getRoleByPermission(){
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getRoleByPermission method.");			
		}
		try{
			List<Object> roleIds=super.getPrimKeys();
			List<Role> roles=facade.getRoleByPermission(roleIds);
			fillActionResult(roles);
		}catch(Exception e){
			dealThrow(new HsCloudException(Constants.ROLE_FIND_EXCEPTION, 
					logger, e), Constants.ROLE_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getRoleByPermission method.takeTime:" + takeTime + "ms");
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
    
    @SuppressWarnings("unchecked")
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
    		pagePrivilege.pageSize(limit);
    		pagePrivilege.setPageNo(page);
    		List<PrivilegeVO> list = facade.findUnassignedList(type, role.getId(), pagePrivilege, query);
    		basePrivilegeList(list);
    	} catch(Exception e){
    		dealThrow(new HsCloudException(Constants.ROLE_FIND_EXCEPTION, 
					logger, e), Constants.ROLE_EXCEPTION);
		}
    	if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findUnassignedList method.takeTime:" + takeTime + "ms");
		}
    }
    
    @SuppressWarnings("unchecked")
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
    		pagePrivilege.pageSize(limit);
    		pagePrivilege.setPageNo(page);
    		List<PrivilegeVO> list = facade.findAssignedList(type, role.getId(), pagePrivilege, query);
    		basePrivilegeList(list);
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
    		List<PrivilegeVO> list = facade.findUiformDefList(type, role.getId());
    		
    		basePrivilegeList(list);
    		pagePrivilege.setTotalCount(list.size());
    	} catch(Exception e){
    		dealThrow(new HsCloudException(Constants.ROLE_FIND_EXCEPTION, 
					logger, e), Constants.ROLE_EXCEPTION);
		}
    	if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findUiformDefList method.takeTime:" + takeTime + "ms");
		}
    }
    
    @SuppressWarnings("unchecked")
	private void basePrivilegeList(List<PrivilegeVO> list) {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter basePrivilegeList method.");			
		}
		List<Action> actionList = facade.getActionList(type);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for(PrivilegeVO obj : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", obj.getName());
			map.put("id", obj.getResourceId());
			List<CheckboxVO> childList = obj.getCheckboxVOList();
			for(int i = 0; i < actionList.size(); i++) {
				Action action = actionList.get(i);
				map.put("param" + (i + 1), obj.getResourceId() + "-" + action.getId() + ",false");
				for(CheckboxVO checkBox : childList) {
					if(((Long)action.getId()).equals(checkBox.getActionId())) {
						map.put("param" + (i + 1), checkBox.getPermissionId() + "," + checkBox.isChecked());
					}
				}
			}
			result.add(map);
		}
		pagePrivilege.setResult(result);
		fillActionResult(pagePrivilege);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit basePrivilegeList method.takeTime:" + takeTime + "ms");
		}
	}
	
    /**
     * <角色分配权限查询> 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
	@SuppressWarnings("unchecked")
    public void findVmGrid() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findVmGrid method.");			
		}
		if(Constant.RESOURCE_TYPE_VM.equals(type)) {
    	    if("UiformDef".equals(kind)) {
    			findUiformDefList();
    		} else if("Assigned".equals(kind)){
    			findAssignedList();
    		} else {
    			findUnassignedList();
    		}
		} else {
		    pagePrivilege.pageSize(limit);
		    pagePrivilege.setPageNo(page);
		    facade.findDomainList(pagePrivilege, role.getId());
		    fillActionResult(pagePrivilege);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findVmGrid method.takeTime:" + takeTime + "ms");
		}
	}	
	
	/**
	 * <分平台赋权> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void editDomainPermission() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter editDomainPermission method.");			
		}
		Admin admin=null;
	    try {
	        admin = (Admin) super.getCurrentLoginUser();
	        facade.editDomainPermission(permissionValue, resourceValue, role.getId());
	        facade.insertOperationLog(admin,"分平台赋权","分平台赋权",Constants.RESULT_SUCESS);
	    } catch (Exception e) {
	        facade.insertOperationLog(admin,"分平台赋权错误:"+e.getMessage(),"分平台赋权",Constants.RESULT_FAILURE);
	        this.dealThrow(Constants.ROLE_MODIFY_EXCEPTION, e, logger);
        }
	    if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit editDomainPermission method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * <菜单查询> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void findMenuStore() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findMenuStore method.");			
		}
        try {
            String language = this.getSession().getAttribute("WW_TRANS_I18N_LOCALE").toString();
            Map<String, String> map = PropertiesUtils.getMenuNameByLanguage(language);
            String str = facade.findMenuStore(role.getId(), map);
            Struts2Utils.renderText(str);
            setActionResult(null);
        } catch (Exception e) {
            this.dealThrow(Constants.ROLE_MODIFY_EXCEPTION, e, logger);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findMenuStore method.takeTime:" + takeTime + "ms");
		}
	}
    
	/**
	 * <虚拟机赋权> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
    public void addPermission() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter addPermission method.");			
		}
    	try{
    		facade.modifyRolePermissiion(permissionValue, actionValue, resourceValue, role.getId());
    		Struts2Utils.renderJson(this.getActionResult());
    	} catch(Exception e){
    	    this.dealThrow(Constants.ROLE_MODIFY_EXCEPTION, e, logger);
		}
    	if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit addPermission method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * <菜单赋权> 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void addPrivilege() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter addPrivilege method.");			
		}
		Admin admin=null;
    	try {
    	    admin = (Admin) super.getCurrentLoginUser();
    	    RoleVO roleVO = Utils.strutsJson2Object(RoleVO.class);
			privilegesStr = roleVO.getPrivilegesStr();
			if(StringUtils.isNotBlank(privilegesStr) || StringUtils.isNotBlank(roleVO.getNoCheckStr())) {
				facade.addPrivilege(privilegesStr, roleVO.getId(), roleVO.getNoCheckStr());
			}
			facade.insertOperationLog(admin,"菜单赋权","菜单赋权",Constants.RESULT_SUCESS);
		} catch(Exception e){
		    facade.insertOperationLog(admin,"菜单赋权错误:"+e.getMessage(),"菜单赋权",Constants.RESULT_FAILURE);
		    dealThrow(new HsCloudException(Constants.ROLE_MODIFY_EXCEPTION, 
					logger, e), Constants.ROLE_EXCEPTION);
		}
    	if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit addPrivilege method.takeTime:" + takeTime + "ms");
		}
    }
    /**
	 * <角色分配查询-Zone> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("unchecked")
	public void findZoneGrid() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findZoneGrid method.");			
		}		
	    pagePrivilege.pageSize(limit);
	    pagePrivilege.setPageNo(page);
	    facade.findZoneListOfRole(pagePrivilege, role.getId());
	    fillActionResult(pagePrivilege);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findZoneGrid method.takeTime:" + takeTime + "ms");
		}
	}
    /**
     * <建立角色与资源域的关系> 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void updateZoneOfRolePermission() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter updateZoneOfRolePermission method.");			
		}
    	try{
    		facade.updateZoneOfRolePermission(role.getId(), resourceValue, permissionValue);
//    		facade.modifyRolePermissiion(permissionValue, actionValue, resourceValue, role.getId());
    	} catch(Exception e){
    	    this.dealThrow(Constants.ROLE_MODIFY_EXCEPTION, e, logger);
		}
    	if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit updateZoneOfRolePermission method.takeTime:" + takeTime + "ms");
		}
    }

	public String getPrivilegesStr() {
        return privilegesStr;
    }

    public void setPrivilegesStr(String privilegesStr) {
        this.privilegesStr = privilegesStr;
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

    public Page<Role> getPageRole() {
        return pageRole;
    }

    public void setPageRole(Page<Role> pageRole) {
        this.pageRole = pageRole;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public Page<Admin> getPageAdmin() {
        return pageAdmin;
    }

    public void setPageAdmin(Page<Admin> pageAdmin) {
        this.pageAdmin = pageAdmin;
    }

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	@SuppressWarnings("unchecked")
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

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

    public String getResourceType() {
        return resourceType;
    }
}

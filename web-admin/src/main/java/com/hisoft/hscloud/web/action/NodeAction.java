package com.hisoft.hscloud.web.action; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.web.struts2.Struts2Utils;

import com.hisoft.hscloud.bss.sla.sc.entity.ServerNode;
import com.hisoft.hscloud.bss.sla.sc.entity.ServerZone;
import com.hisoft.hscloud.bss.sla.sc.utils.SCUtil;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.vo.ActionResult;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.web.facade.Facade;

public class NodeAction extends HSCloudAction{

	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = -3901112832049110518L;
	private Logger logger = Logger.getLogger(this.getClass());	
	@Autowired
	private Facade facade;
	private ServerNode serverNode;
	private ServerZone serverZone;
	private final String resourceType = "com.hisoft.hscloud.bss.sla.sc.entity.ServerNode";
	private int page;
    private int limit;
//    private String code;//区域编码
//    private Page<ServerZone> pageZone = new Page<ServerZone>();
    private String type;// 查询类型
	private String query;// 模糊查询条件
	private String zoneCode;
	private long id;//节点Id
	public ServerNode getServerNode() {
		return serverNode;
	}
	public void setServerNode(ServerNode serverNode) {
		this.serverNode = serverNode;
	}
	public String getResourceType() {
		return resourceType;
	}
	public ServerZone getServerZone() {
		return serverZone;
	}
	public void setServerZone(ServerZone serverZone) {
		this.serverZone = serverZone;
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
	public String getZoneCode() {
		return zoneCode;
	}
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * <发现新节点> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String findNodes(){	
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findNodes method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		Map<String ,Object> map = new HashMap<String, Object>();
		List<ServerNode> serverNodeList = null; 
		try{
			serverNodeList = facade.getNewNodes();
			map.put("totalCount",serverNodeList.size());
			map.put("result", serverNodeList);
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.SC_FIND_NODE_EXCEPTION,
					"findNodes Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		super.fillActionResult(map);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findNodes method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <添加节点> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public String save(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter save method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		ActionResult result = getActionResult();
		try {
			serverNode = SCUtil.strutsJson2Object(ServerNode.class);
			boolean isSameName = facade.hasSameNodeName(serverNode);
			if(isSameName){
				super.fillActionResult(Constants.SC_NODE_NAME_EXISTS);
				return null;
			}
			facade.save(serverNode);
			facade.insertOperationLog(admin,"后台添加/修改节点","后台添加/修改节点",Constants.RESULT_SUCESS);
		} catch (Exception ex) {
			facade.insertOperationLog(admin,"后台添加/修改节点错误:"+ex.getMessage(),"后台添加/修改节点",Constants.RESULT_FAILURE);
			result.setResultCode(Constants.OPTIONS_FAILURE);
			dealThrow(new HsCloudException(Constants.SC_SAVE_NODE_EXCEPTION,
					"saveNodes Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit save method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <删除节点> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public String delete(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter delete method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		ActionResult result = getActionResult();
		try {
			facade.delete(serverNode.getId());
			facade.insertOperationLog(admin,"后台删除节点","后台删除节点",Constants.RESULT_SUCESS);
		} catch (Exception ex) {
			facade.insertOperationLog(admin,"后台删除节点错误:"+ex.getMessage(),"后台删除节点",Constants.RESULT_FAILURE);
			result.setSuccess(false);
			result.setResultCode(Constants.NODE_NOT_EXIT);
			dealThrow(new HsCloudException(Constants.SC_DELETE_NODE_EXCEPTION,
					"deleteNodes Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit delete method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <查询所有节点> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public String pageNodes(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter pageNodes method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		List<ServerNode> serverNodeList = null; 
		try{
			serverNodeList = facade.getAllNodes();
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.SC_NODE_LIST_EXCEPTION,
					"pageNodes Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		super.fillActionResult(serverNodeList);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit pageNodes method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <查询节点树> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String getNodeTree(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getNodeTree method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		setActionResult(null);		
		List<Object> nodeIds = super.getPrimKeys();
		try{
			Struts2Utils.renderText(facade.getNodeTree(nodeIds,zoneCode));
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_HOST_VM_TREE_EXCEPTION,
					"getNodeTree Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getNodeTree method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <根据Zone查询节点列表> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String getAllNodesGroupByZone(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAllNodesGroupByZone method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		List<ServerNode> nodeList = null;
		try{
			nodeList = facade.getAllNodesByServerZone(serverZone.getId());
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.SC_NODE_LIST_EXCEPTION,
					"getAllNodesGroupByZone Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		super.fillActionResult(nodeList);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAllNodesGroupByZone method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <同步所有节点的资源隔离配置信息> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String synchronizationAllNodeIsolation(){	
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter synchronizationAllNodeIsolation method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}		
		try{
			if(admin.getIsSuper()){
				facade.synchronizationAllNodeIsolation();
			}		
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.OPTIONS_FAILURE,
					"synchronizationAllNodeIsolation Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit synchronizationAllNodeIsolation method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <冻结节点> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String freezeNode(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter freezeNode method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;	
		try {
			result = facade.freezeNode(id, admin.getId());
			if (result) {
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}
			facade.insertOperationLog(admin,"成功受理冻结节点操作","冻结节点",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"冻结节点操作错误:"+e.getMessage(),"冻结节点",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_FREEZE_ERROR,
					"freezeNode Exception:", logger, e), Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit freezeNode method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	/**
	 * <启用节点> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String activeNode(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter activeNode method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		boolean result = false;	
		try {
			result = facade.activeNode(id, admin.getId());
			if (result) {
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}
			facade.insertOperationLog(admin,"成功受理启用节点操作","启用节点",Constants.RESULT_SUCESS);
		} catch (HsCloudException e) {
			facade.insertOperationLog(admin,"启用节点操作错误:"+e.getMessage(),"启用节点",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException(Constants.VM_ACTIVE_ERROR,
					"activeNode Exception:", logger, e), Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit activeNode method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
}

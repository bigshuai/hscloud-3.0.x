/**
* @title IServerNodeService.java
* @package com.hisoft.hscloud.bss.sla.sc.service
* @description 用一句话描述该文件做什么
* @author jiaquan.hu
* @update 2012-5-8 上午9:34:06
* @version V1.0
*/
package com.hisoft.hscloud.bss.sla.sc.service;

import java.util.List;
import java.util.Map;

import org.openstack.api.common.IPMIManager;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.sc.entity.IPMIConfig;
import com.hisoft.hscloud.bss.sla.sc.entity.ServerNode;
import com.hisoft.hscloud.common.util.HsCloudException;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author jiaquan.hu
 * @update 2012-5-8 上午9:34:06
 */
public interface IServerNodeService {

	/**
	* @title: save
	* @description 用一句话说明这个方法做什么
	* @param node 设定文件
	* @return void 返回类型
	* @throws
	* @version 1.0
	* @author jiaquan.hu
	* @update 2012-5-15 下午3:39:10
	*/
	public void save(ServerNode node);

	/**
	* @title: getAllNodes
	* @description 用一句话说明这个方法做什么
	* @return 设定文件
	* @return List<ServerNode> 返回类型
	* @throws
	* @version 1.0
	* @author jiaquan.hu
	* @update 2012-5-15 下午4:35:11
	*/
	public List<ServerNode> getAllNodes();
	
	public ServerNode getNodeById(long nodeId);
	
	public ServerNode getNodeByName(String name);

	public void delete(long nodeId);
	/**
	 * <节点列表> 
	* <功能详细描述> 
	* @param page
	* @param field
	* @param fieldValue
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<ServerNode> findHostsByCondition(Page<ServerNode> page,String field,
			String fieldValue,String zoneCode,List<Object> zoneIds) throws HsCloudException;
	/**
	 * 发现节点 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ServerNode> getNewNodes() throws HsCloudException;
	/**
	 * <根据节点Id查询节点列表> 
	* <功能详细描述> 
	* @param nodeIds
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<ServerNode> getAllNodes(List<Object> nodeIds,String zoneCode); 
	/**
	 * <根据节点Zone查询节点列表> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public Map<String,List<ServerNode>> getAllNodesGroupByZone() throws HsCloudException;
	/**
	 * <根据ZoneId查询节点列表> 
	* <功能详细描述> 
	* @param zoneCode
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public  List<ServerNode> getAllNodesByServerZone(long zoneId) throws HsCloudException;
	/**
	 * <获取IPMI连接验证> 
	* <功能详细描述> 
	* @param IPMIConfig
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public IPMIManager connectIPMIManager(IPMIConfig IPMIConfig) throws HsCloudException;
	/**
	 * <修改节点> 
	* <功能详细描述> 
	* @param serverNode
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean updateServerNode(ServerNode serverNode) throws HsCloudException;
	/**
	 * <根据Zone编码查询节点列表> 
	* <功能详细描述> 
	* @param zoneCode
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public  List<ServerNode> getAllNodesByServerZone(String zoneCode) throws HsCloudException;
	/**
	 * <设置资源隔离配置> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean setIsolation(ServerNode serverNode) throws HsCloudException;
	/**
	 * <同步所有节点的资源隔离配置信息> 
	* <功能详细描述> 
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void synchronizationAllNodeIsolation() throws HsCloudException;
	/**
	 * <判断是否有重名的节点> 
	* <功能详细描述> 
	* @param serverNode
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean hasSameNodeName(ServerNode serverNode) throws HsCloudException;
	/**
	 * <查询zone下的所有节点> 
	* <功能详细描述> 
	* @param zoneIds
	* @param zoneCode
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public  List<ServerNode> getAllNodesByServerZone(List<Object> zoneIds,String zoneCode) throws HsCloudException;
	/**
	 * <启用/禁用节点> 
	* <功能详细描述> 
	* @param serverNode
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean enableServerNode(ServerNode serverNode) throws HsCloudException;
}

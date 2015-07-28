/**
* @title ServerNodeDao.java
* @package com.hisoft.hscloud.bss.sla.sc.dao
* @description 用一句话描述该文件做什么
* @author jiaquan.hu
* @update 2012-5-8 上午9:32:36
* @version V1.0
*/
package com.hisoft.hscloud.bss.sla.sc.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.bss.sla.sc.entity.ServerNode;
import com.hisoft.hscloud.common.util.HsCloudException;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author jiaquan.hu
 * @update 2012-5-8 上午9:32:36
 */
@Repository
public class ServerNodeDao extends HibernateDao<ServerNode, Long> {
	
	private Logger logger = Logger.getLogger(this.getClass());

	public Page<ServerNode> findServerNode(Page<ServerNode> page,String zoneCode,List<Object> zoneIds){
		Page<ServerNode> pageNode = null;
		StringBuffer hql = new StringBuffer(
                "from ServerNode sn where 2>1 and sn.serverZone.isEnable=1");		
		Map<String, Object> map = new HashMap<String, Object>();
		if(zoneCode!=null && !"".equals(zoneCode)){
			hql.append(" AND sn.serverZone.code =:zoneCode ");
			map.put("zoneCode", zoneCode);
		}
		if(zoneIds !=null && zoneIds.size()>=0){
			zoneIds.add(0L);
			hql.append(" AND sn.serverZone.id in (:list) ");
			map.put("list", zoneIds);
		}
		try{						
			pageNode = this.findPage(page, hql.toString(),map);
		} catch (Exception e) {
			logger.error("findServerNode Exception:", e);
        }
		return pageNode;
	}
	
	public Page<ServerNode> findServerNodeByNodeName(Page<ServerNode> page,String hostName){
		Page<ServerNode> pageNode = null;
		try{
			StringBuffer hql = new StringBuffer(
	                "from ServerNode sn where sn.name =:hostName");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("hostName", hostName);
			pageNode = this.findPage(page, hql.toString(), map);
		} catch (Exception e) {
			logger.error("findServerNodeByNodeName Exception:", e);
        }	
		return pageNode;
	}
	
	public Page<ServerNode> findServerNodeByIp(Page<ServerNode> page,String ip){
		Page<ServerNode> pageNode = null;
		try{
			StringBuffer hql = new StringBuffer(
	                "from ServerNode sn where sn.ip =:ip");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ip", ip);
			pageNode = this.findPage(page, hql.toString(), map);
		} catch (Exception e) {
			logger.error("findServerNodeByIp Exception:", e);
        }
		return pageNode;
	}
	public Page<ServerNode> queryServerNodeByFuzzy(Page<ServerNode> page,String fieldValue,String zoneCode,List<Object> zoneIds){
		Page<ServerNode> pageNode = null;
		try{
			StringBuffer hql = new StringBuffer("from ServerNode sn where 2>1");
			hql.append(" and (sn.ip like :ip or sn.nodeAliases like :hostName or sn.name like :hostName) ");
			hql.append(" and sn.serverZone.isEnable=1");
			//or sn.name like :hostName
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ip", "%"+fieldValue+"%");
			map.put("hostName", "%"+fieldValue+"%");
			if(zoneCode!=null && !"".equals(zoneCode)){
				hql.append(" AND sn.serverZone.code =:zoneCode ");
				map.put("zoneCode", zoneCode);
			}
			if(zoneIds !=null && zoneIds.size()>=0){
				zoneIds.add(0L);
				hql.append(" AND sn.serverZone.id in (:list) ");
				map.put("list", zoneIds);
			}
			pageNode = this.findPage(page, hql.toString(), map);
		} catch (Exception e) {
			logger.error("queryServerNodeByFuzzy Exception:", e);
        }	
		return pageNode;
	}
	@SuppressWarnings("unchecked")
	public  List<ServerNode> getAllNodes(List<Object> nodeIds,String zoneCode){
		List<ServerNode> serverNodeList = null;
		try{
			StringBuffer hql = new StringBuffer("from ServerNode sn where 2>1");
			if(nodeIds !=null && nodeIds.size()==0){
				nodeIds.add(0L);
			}
			if(zoneCode!=null && !"".equals(zoneCode)){
				hql.append(" AND sn.serverZone.code = '").append(zoneCode).append("'");
			}
			if(nodeIds ==null){
				serverNodeList = this.find(hql.toString());
			}else{
				hql.append(" AND sn.id in (:list)");
				Query query = getSession().createQuery(hql.toString());
				query.setParameterList("list", nodeIds);
				serverNodeList = query.list();
			}			
		} catch (Exception e) {
			logger.error("getAllNodes Exception:", e);
        }		
		return serverNodeList;
	}
	/**
	 * <查询zone下的所有节点> 
	* <功能详细描述> 
	* @param zoneCode
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public  List<ServerNode> getAllNodesByServerZone(long zoneId) throws HsCloudException{
		List<ServerNode> serverNodeList = null;
		try{
			if(!"".equals(zoneId)){
				StringBuffer hql = new StringBuffer(
		                "from ServerNode sn where sn.serverZone.id = :zoneId");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("zoneId",zoneId);
				serverNodeList=this.find(hql.toString(), map);
			}
		}catch (Exception e) {
			logger.error("getAllNodesByServerZone Exception:", e);
		}
		return serverNodeList;		
	}
	public boolean updateServerNode(ServerNode serverNode) throws HsCloudException{
		boolean result = false;
		try{
			Session session = this.getSession();
			session.saveOrUpdate(serverNode);
			result = true;
		}catch (Exception e) {
			logger.error("updateServerNode Exception:", e);
		}		
		return result;
	}
	public void updateNodeIsolationConfig(ServerNode serverNode) throws HsCloudException{
		try{
			Session session = this.getSession();
			session.saveOrUpdate("nodeIsolationConfig", serverNode.getNodeIsolationConfig());
		}catch (Exception e) {
			logger.error("updateServerNode Exception:", e);
		}
	}
	/**
	 * <查询zone下的所有节点> 
	* <功能详细描述> 
	* @param zoneCode
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public  List<ServerNode> getAllNodesByServerZone(String zoneCode) throws HsCloudException{
		List<ServerNode> serverNodeList = null;
		try{
			if(!"".equals(zoneCode)){
				StringBuffer hql = new StringBuffer(
		                "from ServerNode sn where sn.serverZone.code = :code");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("code",zoneCode);
				serverNodeList=this.find(hql.toString(), map);
			}
		}catch (Exception e) {
			logger.error("getAllNodesByServerZone Exception:", e);
		}
		return serverNodeList;		
	}
	/**
	 * <查询zone下的所有节点> 
	* <功能详细描述> 
	* @param zoneCode
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("unchecked")
	public  List<ServerNode> getAllNodesByServerZone(List<Object> zoneIds,String zoneCode) throws HsCloudException{
		List<ServerNode> serverNodeList = null;
		try{
			StringBuffer hql = new StringBuffer("from ServerNode sn where 2>1");
			if(zoneIds !=null && zoneIds.size()==0){
				zoneIds.add(0L);
			}
			if(zoneCode!=null && !"".equals(zoneCode)){
				hql.append(" AND sn.serverZone.code = '").append(zoneCode).append("'");
			}
			if(zoneIds ==null){
				serverNodeList = this.find(hql.toString());
			}else{
				hql.append(" AND sn.serverZone.id in (:list)");
				Query query = getSession().createQuery(hql.toString());
				query.setParameterList("list", zoneIds);
				serverNodeList = query.list();
			}			
		} catch (Exception e) {
			logger.error("getAllNodesByServerZone Exception:", e);
        }
		return serverNodeList;		
	}
	public ServerNode getUniqueServerNode(String nodeAliases){
		ServerNode serverNode = null;
		StringBuffer hql = new StringBuffer("from ServerNode sn where sn.nodeAliases =:nodeAliases");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("nodeAliases", nodeAliases);
		serverNode = (ServerNode) query.uniqueResult();
		getSession().clear();
		return serverNode;
	}
}

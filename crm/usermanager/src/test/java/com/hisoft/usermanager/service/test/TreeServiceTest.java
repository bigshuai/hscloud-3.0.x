/**
 * 
 */
package com.hisoft.usermanager.service.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.crm.usermanager.entity.Permission;
import com.hisoft.hscloud.crm.usermanager.entity.ResourceType;
import com.hisoft.hscloud.crm.usermanager.service.ActionService;
import com.hisoft.hscloud.crm.usermanager.service.PermissionService;
import com.hisoft.hscloud.crm.usermanager.service.ResourceService;
import com.hisoft.hscloud.crm.usermanager.service.ResourceTypeService;
import com.hisoft.hscloud.crm.usermanager.service.TreeService;
import com.hisoft.hscloud.crm.usermanager.util.Constant;
import com.hisoft.hscloud.crm.usermanager.vo.CheckboxVO;
import com.hisoft.hscloud.crm.usermanager.vo.PrivilegeVO;
import com.hisoft.hscloud.crm.usermanager.vo.TreeNode;
import com.hisoft.hscloud.crm.usermanager.vo.TreeQueryVO;

/**
 * @author lihonglei
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@Transactional  
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@ContextConfiguration(locations = { "classpath:applicationContext-hscloud-crm-usermanager.xml" })
public class TreeServiceTest {
	@Autowired
	private TreeService treeService;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private ActionService actionService;
	
	@Autowired
	private ResourceTypeService resourceTypeService;
	
	@Autowired
	private PermissionService permissionService;
	
	public static Map<String, ResourceType> resourceTypeMap = new HashMap<String, ResourceType>();
	
	/*private Map<String, ResourceType> getResourceTypeMap() {
		if(resourceTypeMap == null || resourceTypeMap.isEmpty()) {
			resourceTypeMap = resourceTypeService.getResourceTypeMap(Constant.STATUS_ADMIN);
		}
		return resourceTypeMap;
	}*/
	
	/*@Test
	public void testCreateTree() {
		String table = "hc_menu";
		String type = "com.hisoft.hscloud.crm.usermanager.entity.Menu";
		List<TreeQueryVO> list = treeService.createTree(table, type);
//		Map<String, List<Action>> actionMap = actionService.getAllAction(getResourceTypeMap());
		List<Action> actionList = new ArrayList<Action>();
		actionList = actionMap.get(type);
		
		Map<Long, TreeNode> map = new HashMap<Long, TreeNode>();
		TreeNode treeNode = null;
		System.out.println(list.size() + "************************");
		for(TreeQueryVO tree : list) {
			if(tree.getResourceId() == null) {
				Long resourceId = resourceService.addResource(tree.getId(), tree.getType());
				for(Action action : actionList) {
					long permissionId = permissionService.addPermission(resourceId, action.getId());
				}
				tree.setResourceId(resourceId);
			}
			
			Long id = tree.getResourceId();
			if(map.containsKey(id)) {
				treeNode = (TreeNode)map.get(tree.getResourceId());
			//	treeNode.getPermissionList().add(assemblePermission(tree));
				assemblePermission(tree, treeNode);
			} else {
				treeNode = new TreeNode();
				treeNode.setName(tree.getName());
			//	treeNode.getPermissionList().add(assemblePermission(tree));
				assemblePermission(tree, treeNode);
				map.put(tree.getResourceId(), treeNode);
			}
		}
		int index = 0;
		for(Entry<Long, TreeNode> entry : map.entrySet()) {
			System.out.println("***************************");
			System.out.println("index:" + index++);
			System.out.println(entry.getKey());
			TreeNode node = entry.getValue();
			System.out.println(node.getName());
			for(Permission permission : node.getPermissionList()) {
				System.out.println("acton:" + permission.getActionId());
				System.out.println("permission:" + permission.getId());
			}
			System.out.println("*****************************");
		}
	}*/
	
	/*private void assemblePermission(TreeQueryVO tree, TreeNode node) {
		Permission permission = null;
		if(tree.getPermissionId() != null) {
			permission = new Permission();
			permission.setId(tree.getPermissionId());
			permission.setActionId(tree.getActionId());
			permission.setResourceId(tree.getResourceId());
			node.getPermissionList().add(permission);
		}
	}*/
	
	@Test
	public void testFindCount() {
		String table = "(select * from hc_menu where parent_id = :query)";
		String type = "com.hisoft.hscloud.crm.usermanager.entity.Menu";
		Long counter = treeService.findCount(table, type, "0");
		System.out.println(counter);
	}
	
	@Test
	public void testFindPageBySQL() {
		String table = "(select * from hc_menu where parent_id = :query)";
		String type = "com.hisoft.hscloud.crm.usermanager.entity.Menu";
		int firstResult = 0;
		int maxResult = 10;
		List<TreeQueryVO> list = treeService.findPageBySQL(table, type, firstResult, maxResult, "0");
		int index = 0;
		for(TreeQueryVO queryVo : list) {
			System.out.println("index:" + ++index);
			System.out.println(queryVo.getId());
			System.out.println(queryVo.getName());
		}
		
	}
	
	@Test
	public void testFindUnassignedList() {
		/*String table = "hc_vpdc_reference";
		String resourceCondition = " and  `status`=0 ";
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("table", table);
		//map.put("query", query);
		map.put("resourceCondition", resourceCondition);
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("type", "com.hisoft.hscloud.vpdc.ops.entity.VpdcReference");
		conditionMap.put("roleId", 8l);
		map.put("conditionMap", conditionMap);
		Page<PrivilegeVO> pagePrivilege = new Page<PrivilegeVO>(Constants.PAGE_NUM);
		pagePrivilege.setPageNo(1);
		List<TreeQueryVO> list = treeService.findUnassignedList(map, pagePrivilege);
		for(TreeQueryVO vo : list) {
			System.out.println(vo.getId() + ":" + vo.getName());
		}*/
	    String type = "com.hisoft.hscloud.vpdc.ops.entity.VpdcReference";
	    long roleId = 21l;
	    Page pagePrivilege = new Page(Constants.PAGE_NUM);
	    
	    Map<String, ResourceType> resourceMap = resourceTypeService.getResourceTypeMap(Constant.STATUS_ADMIN);
        ResourceType resourceType = resourceMap.get(type);
        String table = resourceType.getResourceTable();
        Map<String, Object> map = new HashMap<String, Object>();
        
        map.put("table", table);
        map.put("query", "1");
        map.put("resourceCondition", resourceType.getResourceCondition());
        Map<String, Object> conditionMap = new HashMap<String, Object>();
        conditionMap.put("type", type);
        conditionMap.put("roleId", roleId);
        map.put("conditionMap", conditionMap);
		
		String conditionTable = "select permission_id from hc_role_permission rp where rp.role_id = :roleId";
        map.put("conditionTable", conditionTable);
        
        List<TreeQueryVO> list = treeService.findUnassignedList(map, pagePrivilege);
	}
	
	@Test
	public void testFindAssignedList() {
		String table = "hc_vpdc_reference";
		String resourceCondition = " and  `status`=0 ";
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("table", table);
		//map.put("query", query);
		map.put("resourceCondition", resourceCondition);
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("type", "com.hisoft.hscloud.vpdc.ops.entity.VpdcReference");
		conditionMap.put("roleId", 8l);
		map.put("conditionMap", conditionMap);

		Page<PrivilegeVO> pagePrivilege = new Page<PrivilegeVO>(2);
		pagePrivilege.setPageNo(1);
		List<Object> list = treeService.findAssignedList( map, pagePrivilege);
		for(Object obj : list) {
			Object[] array = (Object[])obj;
			System.out.println(array[0] + ":" + array[1]);
		}
		System.out.println(pagePrivilege.getTotalCount());
	}
	@Test
	public void testFindPermissionByResourceIds() {
		List<Object> list = new ArrayList<Object>();
		list.add(136);
		list.add(137);
		List<CheckboxVO> result = treeService.findPermissionByResourceIds(list, 9l);
		System.out.println(result.size());
	}
	
	@Test
	public void testFindUiformDefList() {
		String type = "com.hisoft.hscloud.vpdc.ops.entity.VpdcReference";
		Long roleId = 8l;
		String tableAndCondition = " hc_role_permission rp on p.id = rp.permission_id and rp.role_id = :id ";
		String column = "rp.permission_id";
		treeService.findUiformDefList(type, roleId, tableAndCondition, column);
	}
	@Test
	public void testFindMenuStore() {
		treeService.findMenuStore(8l);
	}
}

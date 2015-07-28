/**
 * 
 */
package com.hisoft.usermanager.service.test;

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

import com.hisoft.hscloud.crm.usermanager.entity.Action;
import com.hisoft.hscloud.crm.usermanager.entity.ResourceType;
import com.hisoft.hscloud.crm.usermanager.service.ActionService;
import com.hisoft.hscloud.crm.usermanager.service.ResourceTypeService;
import com.hisoft.hscloud.crm.usermanager.util.Constant;

/**
 * @author lihonglei
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@Transactional  
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@ContextConfiguration(locations = { "classpath:applicationContext-hscloud-crm-usermanager.xml" })
public class ActionServiceTest {
	@Autowired
	private ActionService actionService;
	
	@Autowired
	private ResourceTypeService resourceTypeService;
	
	public static Map<String, ResourceType> resourceTypeMap = new HashMap<String, ResourceType>();
	
	private Map<String, ResourceType> getResourceTypeMap() {
		if(resourceTypeMap == null || resourceTypeMap.isEmpty()) {
			resourceTypeMap = resourceTypeService.getResourceTypeMap(Constant.STATUS_ADMIN);
		}
		return resourceTypeMap;
	}
	
	/*@Test
	public void testGetAllAction() {
		Map<String, List<Action>> map = actionService.getAllAction(getResourceTypeMap());
		for(Entry<String, List<Action>> entry : map.entrySet()) {
			System.out.println(entry.getKey());
			List<Action> list = entry.getValue();
			for(Action action : list) {
				System.out.println(action.getName());
			}
		}
	}*/
}

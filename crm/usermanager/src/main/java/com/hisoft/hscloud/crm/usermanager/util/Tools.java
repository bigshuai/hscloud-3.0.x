package com.hisoft.hscloud.crm.usermanager.util;
//
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import org.apache.log4j.Level;
//import org.apache.log4j.Logger;
//
//
//import com.hisoft.hscloud.crm.usermanager.dao.ResourceDao;
//import com.hisoft.hscloud.crm.usermanager.entity.Activity;
//import com.hisoft.hscloud.crm.usermanager.entity.Privilege;
//import com.hisoft.hscloud.crm.usermanager.entity.Resource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.hisoft.hscloud.crm.usermanager.service.PrivilegeService;
//import com.hisoft.hscloud.crm.usermanager.service.ResourceService;
//
@Component
public class Tools {
//    private Logger logger = Logger.getLogger(Tools.class);
//    @Autowired
//    private ResourceService resourceService;
//
//    @Autowired
//    private PrivilegeService privilegeService;
//
//    @Autowired
//    private ResourceDao resourceDao;
//
//    /**
//     *
//     * @title: get privilege
//     * @description: 取得森林中树节点，这些树节点是按照数据库中摆放位置排序好的， 并且返回JSON格式的数据给前台页面。
//     * @param List
//     *            <Privilege> privilegeList
//     * @return JSON format string
//     * @throws Exception
//     * @throws
//     * @version 1.1
//     * @author shanming.yang
//     * @update 2012-5-10
//     */
//    public String getPrivilege(List<Privilege> privilegeList) {
//        if (privilegeList == null || privilegeList.size() == 0) {
//            logger.log(Level.WARN,
//                    "There are no privilege data in table resource or database connect is error.");
//            return Constant.DATABASEERROR;
//        }
//        StringBuffer json = new StringBuffer();
//        TreeNode root, rootTemp;
//        int len;
//        json.append("{");
//        json.append("\"");
//        json.append("text");
//        json.append("\":");
//        json.append("\".\",");
//        String fields = getPrivilegeFields(privilegeList);
//        if (fields == null || "".equals(fields)) {
//
//            logger.log(Level.ERROR,
//                    "Jason fromate is error,missing fields data.");
//            return Constant.MISSINGFIELDS;
//        }
//        json.append(fields);
//        Map<Long, Map<Integer, TreeNode>> valueMap = storePrivilege(privilegeList);
//        if (valueMap == null || valueMap.size() == 0) {
//            logger.log(Level.ERROR, "Jason fromate is error,missing body data.");
//        }
//        // 排序森林，按照数据库中位置摆放森林中树节点
//        Map<Integer, TreeNode> rootNode;
//        int i, minPosition, tempPosition;
//        Long keyId, keyIdTmp;
//        Set<Long> keySetV = valueMap.keySet();
//        Iterator<Integer> rootNodeIt;
//        Iterator<Integer> rootNodeItTemp;
//        int tmp;
//        json.append('"' + "children" + '"' + ":[");
//        len = valueMap.size();
//
//        Iterator<Long> it = keySetV.iterator();
//        Iterator<Long> itTemp;
//        i = 0;
//        // 循环森林中树节点
//        while (it.hasNext()) {
//            keyId = it.next();
//            rootNode = valueMap.get(keyId);
//            rootNodeIt = rootNode.keySet().iterator();
//            tmp = rootNodeIt.next();
//            root = rootNode.get(tmp);
//            minPosition = tmp;
//            itTemp = it;
//            while (itTemp.hasNext()) {
//                keyIdTmp = itTemp.next();
//                rootNode = valueMap.get(keyIdTmp);
//                rootNodeItTemp = rootNode.keySet().iterator();
//                tmp = rootNodeItTemp.next();
//                rootTemp = rootNode.get(tmp);
//                tempPosition = tmp;
//                // 根据数据库中位置排序
//                if (tempPosition < minPosition) {
//                    minPosition = tempPosition;
//                    root = rootTemp;
//                    keyId = keyIdTmp;
//                }
//            }
//            json.append("{");
//            // 取树中的子节点
//            transfer2JSONSTR(root, json);
//            valueMap.remove(keyId);
//            // 判断是否森林中最后一棵树
//            if (i == len - 1) {
//                json.append("}");
//            } else {
//                json.append("},");
//            }
//            i++;
//            keySetV = valueMap.keySet();
//            it = keySetV.iterator();
//        }
//
//        json.append("]}");
//        return json.toString();
//    }
//
//    /**
//     *
//     * @title: privilege to JSON format
//     * @description :遍历树中的每一个子节点，返回JSON格式的树的子节点。
//     * @param TreeNode
//     *            root 森林中每棵树节点的root节点
//     * @param StringBuffer
//     *            json 存储JSON 格式数据的缓存
//     * @return JSON format string
//     * @throws Exception
//     * @throws
//     * @version 1.1
//     * @author shanming.yang
//     * @update 2012-5-10
//     */
//    private void transfer2JSONSTR(TreeNode root, StringBuffer json) {
//        int len;
//        String task = root.getTask();
//        Boolean expanded = root.getExpanded();
//        if (expanded == null) {
//            expanded = false;
//        }
//        String icon = root.getIcon();
//        boolean leaf = root.getLeaf();
//        List<Boolean> functionFlag = root.getFunctionFlag();
//
//        List<String> function = root.getFunction();
//        List<Long> privilegeId = root.getPrivilegeId();
//        List<TreeNode> childrens = root.getChildrens();
//        json.append("\"id\":\"" + root.getId() + "\",");
//        json.append("\"parentId\":\"" + root.getParentid() + "\",");
//        json.append("\"task\":\"" + task + "\",");
//        json.append("\"expanded\":" + expanded + ",");
//        len = functionFlag.size();
//        // 寻找功能节点
//        json.append("\"privileges\":[");
//        for (int i = 0; i < len; i++) {
//            json.append("{\"name\":\"" + task + "." + function.get(i) + "\",");
//            json.append("\"selected\"" + ":\"" + functionFlag.get(i) + "\",");
//            if (i == len - 1) {
//                json.append("\"id\"" + ":\"" + privilegeId.get(i) + "\"}");
//            } else {
//                json.append("\"id\"" + ":\"" + privilegeId.get(i) + "\"},");
//            }
//        }
//        json.append("],");
//
//        // 是否叶子节点
//        if (leaf) {
//
//            json.append("\"icon\":\"" + icon + "\",");
//            json.append("\"leaf\":" + leaf);
//        } else {
//            len = childrens.size();
//            if (childrens != null && len != 0) {
//                json.append("\"icon\":\"" + icon + "\",");
//                json.append("\"leaf\":" + false + ',');
//                json.append("\"children\":[");
//            } else {
//                json.append("\"icon\":\"" + icon + "\",");
//                json.append("\"leaf\":" + true);
//            }
//
//        }
//        int childenLen = 0;
//        if (childrens != null) {
//            childenLen = childrens.size();
//        }
//        // 遍历树中兄弟节点，拼凑成JASON格式
//        for (int k = 0; k < childenLen; k++) {
//            root = childrens.get(k);
//
//            if (root != null) {
//                json.append("{");
//                // 递归遍历树的子节点，拼凑成JASON格式
//                transfer2JSONSTR(root, json);
//                json.append("},");
//            }
//        }
//        // 判断是否兄弟中最后一个兄弟，如果是，把多余的逗号去掉。
//        if (childenLen > 0) {
//            json.deleteCharAt(json.lastIndexOf(","));
//            json.append("]");
//        }
//
//    }
//
//    /**
//     *
//     * @title: 存储森林
//     * @description:把数据库中森林结构的数据，存储到内存中， 首先找到森林中树的根节点，然后再根据各个根节点存储子节点。
//     * @param List
//     *            <Privilege> privilegeList
//     * @return 存储数据库中森林的一个map。
//     * @throws Exception
//     * @throws
//     * @version 1.1
//     * @author shanming.yang
//     * @update 2012-5-10
//     */
//    private Map<Long, Map<Integer, TreeNode>> storePrivilege(
//            List<Privilege> privilegeList) {
//        String resourceName = "";
//        String resourceCode = "";
//        long resourceId = 0L;
//        long parentId = 0L;
//        boolean expanded = false;
//        boolean leaf = false;
//        boolean functionFlag = false;
//        boolean funIsExist = false;
//        String icon = "";
//        Privilege privilege = new Privilege();
//        Activity activity = new Activity();
//        Resource resource = new Resource();
//        TreeNode root;
//        long privilegeId;
//        long privilegeIdInner;
//        int position;
//        Map<Integer, TreeNode> rootValueMap;
//        Map<Long, Map<Integer, TreeNode>> rootMap = new HashMap<Long, Map<Integer, TreeNode>>();
//        ArrayList<Integer> needeleteA = new ArrayList<Integer>();
//        // 寻找森林中根节点
//        for (int i = 0; i < privilegeList.size(); i++) {
//            privilege = privilegeList.get(i);
//            privilegeId = privilege.getId();
//            activity = privilege.getActivity();
//            resource = privilege.getResource();
//            resourceId = resource.getId();
//            resourceName = resource.getName();
//            resourceCode = resource.getCode();
//            parentId = resource.getParentID();
//            expanded = resource.getExpanded();
//            icon = resource.getIcon();
//            leaf = resource.isLeaf();
//            position = resource.getPosition();
//
//            // 判断是否为根节点
//
//            if (parentId == Constant.ROOT) {
//
//                root = new TreeNode();
//
//                root.setTask(resourceName);
//
//                root.setExpanded(expanded);
//                root.setIcon(icon);
//                root.setLeaf(leaf);
//                root.setId(resourceId);
//                root.setParentid(parentId);
//
//                // children will been set null when node is leaf.
//                if (leaf) {
//                    root.setChildrens(null);
//                }
//
//                // 寻找功能节点
//                for (int j = 0; j < privilegeList.size(); j++) {
//                    privilege = privilegeList.get(j);
//                    privilegeIdInner = privilege.getId();
//                    activity = privilege.getActivity();
//                    resource = privilege.getResource();
//                    functionFlag = privilege.getFunctionFlag();
//                    if (resourceCode.equals(resource.getCode())) {
//                        for(int k = 0;k <root.getFunction().size();k++) {
//                            if(activity.getName().equals(root.getFunction().get(k))) {
//                                funIsExist = true;
//                            }
//
//                        }
//                        if(!funIsExist){
//                            root.getFunctionFlag().add(functionFlag);
//                            root.getFunction().add(activity.getName());
//                            root.getPrivilegeId().add(privilegeIdInner);
//                        }
//                        funIsExist = false;
//                        // collect all deleted node
//                        needeleteA.add(j);
//
//                    }
//                }
//                // 已经存储到缓存中的数据，从privilegeList中删除。
//                for (int s = 0; s < needeleteA.size(); s++) {
//                    privilegeList.remove(needeleteA.get(s));
//                }
//
//                needeleteA.clear();
//                // 把森林中的根节点存储到map中，根据resourceId判断是否在map已经存储，
//                // 如果已经存储就不在重复存储。
//                if (!rootMap.containsKey(resourceId)) {
//                    rootValueMap = new HashMap<Integer, TreeNode>();
//                    rootValueMap.put(position, root);
//                    rootMap.put(resourceId, rootValueMap);
//                }
//
//            }
//        }
//        // traverse Tree
//        Long rootId;
//        Iterator<Long> it = rootMap.keySet().iterator();
//        Map<Integer, TreeNode> rootNode;
//        while (it.hasNext()) {
//            rootId = it.next();
//            rootNode = rootMap.get(rootId);
//            root = rootNode.get(rootNode.keySet().iterator().next());
//            // 为森林中的每棵树遍历存储子节点。
//            traverseTree(root, rootId, privilegeList);
//
//        }
//        return rootMap;
//    }
//
//    /**
//     *
//     * @title: 存储子节点
//     * @description:根据各个根节点遍历存储子节点。
//     * @param rootBean
//     *            rootBean 根节点
//     * @param rootBean
//     *            rootBean 根节点id
//     * @param privilegeList
//     *            权限列表
//     * @return void
//     * @throws Exception
//     * @throws
//     * @version 1.1
//     * @author shanming.yang
//     * @update 2012-5-10
//     */
//    private void traverseTree(TreeNode rootBean, long rootId,
//                              List<Privilege> privilegeList) {
//        Privilege privilege;
//        Resource resource;
//        List<TreeNode> childrens;
//        long parentId;
//        Boolean leaf;
//        Boolean functionFlag;
//        Boolean isExist = false;
//        String resourceCode;
//        Privilege privilegeInner;
//        long privilegeId;
//        long privilegeIdInner;
//        String task;
//        String tempTask;
//
//        Activity activityInner;
//        Resource resourceInner;
//        ArrayList<Integer> needeleteA = new ArrayList<Integer>();
//        for (int i = 0; i < privilegeList.size(); i++) {
//            privilege = privilegeList.get(i);
//            resource = privilege.getResource();
//            parentId = resource.getParentID();
//            resourceCode = resource.getCode();
//            privilegeId = privilege.getId();
//            // 为根节点遍历孩子节点
//            if (parentId == rootId) {
//                childrens = rootBean.getChildrens();
//                TreeNode child = new TreeNode();
//                child.setId(resource.getId());
//                child.setParentid(parentId);
//                child.setTask(resource.getName());
//                if (resource.getExpanded() == null) {
//                    child.setExpanded(false);
//                } else {
//                    child.setExpanded(resource.getExpanded());
//                }
//                child.setIcon(resource.getIcon());
//                if (resource.isLeaf() == null) {
//                    child.setLeaf(false);
//                } else {
//                    child.setLeaf(resource.isLeaf());
//                }
//                // 判断是否叶子节点，如果是叶子节点，孩子节点为空
//                if (resource.isLeaf()) {
//                    child.setChildrens(null);
//                }
//                // 遍历每个节点的功能。
//                for (int j = 0; j < privilegeList.size(); j++) {
//                    privilegeInner = privilegeList.get(j);
//                    privilegeIdInner = privilegeInner.getId();
//                    activityInner = privilegeInner.getActivity();
//                    resourceInner = privilegeInner.getResource();
//                    functionFlag = privilegeInner.getFunctionFlag();
//                    if (functionFlag == null) {
//                        functionFlag = false;
//                    }
//                    // 根据code判断是否为同一个节点，如果是相同节点，则把该节点的功能收集起来。
//                    if (resourceCode.equals(resourceInner.getCode())) {
//                        child.getFunctionFlag().add(functionFlag);
//                        child.getFunction().add(activityInner.getName());
//                        child.getPrivilegeId().add(privilegeIdInner);
//                        // 为节点遍历完功能后，需要删除该条记录，后面不需要重复遍历
//                        if (privilegeId != privilegeIdInner) {
//                            needeleteA.add(j);
//                        }
//
//                    }
//
//                }
//                // 删除不需要遍历的记录
//                for (int s = 0; s < needeleteA.size(); s++) {
//                    privilegeList.remove(needeleteA.get(s));
//                }
//                // 清空list，为下次遍历使用
//                needeleteA.clear();
//                // 遍历同级兄弟，判断该节点是否已经存储，如果存储，就不在重复存储同一节点。
//                if (childrens != null && childrens.size() > 0) {
//                    for (int m = 0; m < childrens.size(); m++) {
//                        if (childrens.get(m) != null) {
//                            task = childrens.get(m).getTask();
//                            tempTask = child.getTask();
//                            if (task != null && task != ""
//                                    && task.equals(tempTask)) {
//                                isExist = true;
//                                break;
//                            }
//                        }
//
//                    }
//                }
//                // 添加孩子节点
//                if (childrens != null && !isExist && child != null) {
//                    childrens.add(child);
//                }
//                isExist = false;
//                // 存储子节点
//                rootBean.setChildrens(childrens);
//                leaf = resource.isLeaf();
//                // 该节点如果不是叶子节点，继续为它遍历子节点
//                if (!leaf) {
//                    // 移除已经存储到缓存中的子节点。
//                    privilegeList.remove(i);
//
//                    // 递归存储子节点
//                    traverseTree(child, resource.getId(), privilegeList);
//                }
//
//            }
//
//        }
//    }
//
//    /**
//     * @title: JSON格式获取消息头
//     * @description : JSON格式获取消息头
//     * @param List
//     *            <Privilege> privilegeList
//     * @return JSON format string
//     * @throws Exception
//     * @throws
//     * @version 1.1
//     * @author shanming.yang
//     * @update 2012-5-10
//     */
//    private String getPrivilegeFields(List<Privilege> privilegeList) {
//        int len = privilegeList.size();
//        Map<String, Integer> resourceMap = new HashMap<String, Integer>();
//        Privilege privilege = new Privilege();
//        Resource resource = new Resource();
//        String resourceCode = "";
//        StringBuffer fields = new StringBuffer();
//        int functionNum = 0;
//        // 遍历权限列表中功能点最大的，并且记录最大数。
//        for (int i = 0; i < len; i++) {
//            privilege = privilegeList.get(i);
//            resource = privilege.getResource();
//            resourceCode = resource.getCode();
//            if (resourceMap.containsKey(resourceCode)) {
//                functionNum = resourceMap.get(resourceCode);
//                functionNum = functionNum + 1;
//                resourceMap.put(resourceCode, functionNum);
//            } else {
//                resourceMap.put(resourceCode, 1);
//            }
//        }
//        Iterator<String> it = resourceMap.keySet().iterator();
//        resourceCode = it.next();
//        functionNum = resourceMap.get(resourceCode);
//        int num = 0;
//        // 循环比较最大记录数，取得这个最大数据
//        while (it.hasNext()) {
//            resourceCode = it.next();
//            num = resourceMap.get(resourceCode);
//            if (num > functionNum) {
//                functionNum = num;
//            }
//
//        }
//        // 拼凑JASON格式消息头
//        fields.append('\"' + "fields" + '\"');
//        fields.append(": [");
//        fields.append("{\"name\":\"task\",\"type\":\"string\"},");
//        String functionFlag = "";
//        String function = "";
//        for (int i = 0; i < functionNum - 1; i++) {
//            functionFlag = "{\"name\":\"function" + Integer.toString(i)
//                    + "_flag\",\"type\":\"bool\"},";
//            function = "{\"name\":\"function" + Integer.toString(i)
//                    + "\",\"type\":\"string\"},";
//            fields.append(functionFlag);
//            fields.append(function);
//        }
//        functionFlag = "{\"name\":\"function" + Integer.toString(functionNum - 1)
//                + "_flag\",\"type\":\"bool\"},";
//        function = "{\"name\":\"function" + Integer.toString(functionNum - 1)
//                + "\",\"type\":\"string\"}";
//        fields.append(functionFlag);
//        fields.append(function);
//        fields.append("],");
//        fields.append('"' + "num" + '"' + ":" + functionNum + ",");
//        return fields.toString();
//    }
//
//    /**
//     * @title: 编辑权限
//     * @description : 编辑权限时候，首先加载所有权限，并且设置为未选中状态， 然后根据选中列表去设置已经选中的。
//     * @param List
//     *            <Privilege> privilegeList
//     * @return JSON format string
//     * @throws Exception
//     * @throws
//     * @version 1.1
//     * @author shanming.yang
//     * @update 2012-5-10
//     */
//    public String editPrivilege(List<Privilege> privilegeList,
//                                Long[] selectedPrivilege, String which) {
//        if (privilegeList == null || privilegeList.size() == 0) {
//            logger.log(Level.WARN,
//                    "There are no privilege data in table resource or database connect is error.");
//            return Constant.DATABASEERROR;
//        }
//        int len = privilegeList.size();
//        Privilege privilege;
//        long privilegeId;
//        // 加载所有权限
//        for (int i = 0; i < len; i++) {
//            privilege = privilegeList.get(i);
//            // 设置权限没有选中
//            privilege.setFunctionFlag(false);
//            privilegeId = privilege.getId();
//            // 根据选中权限列表，设置权限选中情况
//            if (selectedPrivilege != null) {
//                for (int j = 0; j < selectedPrivilege.length; j++) {
//                    if (selectedPrivilege[j] == privilegeId) {
//                        privilege.setFunctionFlag(true);
//                    }
//                }
//            } else {
//                logger.log(Level.INFO, "There are no privileges selected.");
//            }
//
//        }
//
//        String json = null;
//        if (Constant.GROUP.equals(which)) {
//            json = getPrivilege(privilegeList);
//        } else if (Constant.ROLE.equals(which)) {
//            json = getPrivilegeForRole(privilegeList);
//        }
//        return json;
//    }
//
//    /**
//     * @title:获取角色权限
//     * @description : 获取角色权限，拼凑成JSON格式。
//     * @param List
//     *            <Privilege> privilegeList
//     * @return JSON format string
//     * @throws Exception
//     * @throws
//     * @version 1.1
//     * @author shanming.yang
//     * @update 2012-5-10
//     */
//    private String getPrivilegeForRole(List<Privilege> privilegeList) {
//		StringBuffer json = new StringBuffer();
//		Map<String, TreeNode> forestMap;
//		forestMap = storeRole(privilegeList);
//		Iterator<String> it = forestMap.keySet().iterator();
//		TreeNode treeNode;
//		String key;
//		String task;
//		boolean expanded;
//		boolean functionFlag;
//		long privilegeId;
//		boolean leaf;
//		String icon;
//		int len;
//		List<TreeNode> children;
//		json.append("[");
//		while (it.hasNext()) {
//			key = it.next();
//			treeNode = forestMap.get(key);
//			task = treeNode.getTask();
//			expanded = treeNode.getExpanded();
//			leaf = treeNode.getLeaf();
//			icon = treeNode.getIcon();
//			functionFlag = treeNode.getFunctionFlag().get(0);
//			privilegeId = treeNode.getPrivilegeId().get(0);
//			json.append("{");
//			json.append("text" + ":" + "\"" + task + "\",");
//			json.append("icon" + ":" + "\"" + icon + "\",");
//			json.append("checked" + ":" + functionFlag + ",");
//			json.append("expanded" + ":" + expanded + ",");
//
//			if (leaf) {
//				json.append("leaf" + ":" + leaf + ",");
//				json.append("id" + ":" + privilegeId);
//			} else {
//				json.append("leaf" + ":" + leaf + ",");
//			}
//			if (!leaf) {
//				json.append("children:[");
//				children = treeNode.getChildrens();
//				len = children.size();
//				for (int i = 0; i < len; i++) {
//					json.append("{");
//					json.append("text" + ":" + "\"" + children.get(i).getTask()
//							+ "\",");
//					json.append("icon" + ":" + "\"" + children.get(i).getIcon()
//							+ "\",");
//					json.append("checked" + ":"
//							+ children.get(i).getFunctionFlag().get(0) + ",");
//					json.append("expanded" + ":"
//							+ children.get(i).getExpanded() + ",");
//					json.append("leaf" + ":" + children.get(i).getLeaf() + ",");
//					json.append("id" + ":"
//							+ children.get(i).getPrivilegeId().get(0));
//					json.append("},");
//				}
//				json.deleteCharAt(json.lastIndexOf(","));
//				json.append("]},");
//			}
//
//		}
//		json.deleteCharAt(json.lastIndexOf(","));
//		json.append("]");
//		return json.toString();
//	}
//    /**
//     * @title: 存储角色权限
//     * @description : 为角色存储权限,将角色权限存在缓存。
//     * @param List
//     *            <Privilege> privilegeList
//     * @return JSON format string
//     * @throws Exception
//     * @throws
//     * @version 1.1
//     * @author shanming.yang
//     * @update 2012-5-10
//     */
//    private Map<String, TreeNode> storeRole(List<Privilege> privilegeList) {
//		int len = privilegeList.size();
//		Map<String, TreeNode> forestMap = new HashMap<String, TreeNode>();
//		List<TreeNode> children;
//		Privilege privilege;
//		Activity activity;
//		Resource resource;
//		String resourceCode;
//		String task;
//		boolean expanded;
//		boolean functionFlag;
//		long privilegeId;
//		String icon;
//		TreeNode treeNodeTmp, treeChildrenNode;
//		for (int i = 0; i < len; i++) {
//			TreeNode treeNode = new TreeNode();
//			privilege = privilegeList.get(i);
//			resource = privilege.getResource();
//			activity = privilege.getActivity();
//			resourceCode = resource.getCode();
//			icon = resource.getIcon();
//			expanded = resource.getExpanded();
//			functionFlag = privilege.getFunctionFlag();
//			privilegeId = privilege.getId();
//			treeNode.setExpanded(expanded);
//			treeNode.setIcon(icon);
//			treeNode.getFunctionFlag().add(functionFlag);
//			treeNode.getPrivilegeId().add(privilegeId);
//			if (forestMap.containsKey(resourceCode)) {
//				// 
//				task = activity.getName();
//				treeNode.setTask(task);
//				treeNode.setLeaf(true);
//				treeNodeTmp = forestMap.get(resourceCode);
//				children = treeNodeTmp.getChildrens();
//				children.add(treeNode);
//			} else {
//				// 
//				task = resource.getName();
//				treeNode.setTask(task);
//				treeNode.setLeaf(false);
//				forestMap.put(resourceCode, treeNode);
//				//
//				treeChildrenNode = new TreeNode();
//				task = activity.getName();
//				treeChildrenNode.setTask(task);
//				treeChildrenNode.setLeaf(true);
//				treeChildrenNode.setExpanded(expanded);
//				treeChildrenNode.getFunctionFlag().add(functionFlag);
//				treeChildrenNode.getPrivilegeId().add(privilegeId);
//				treeChildrenNode.setIcon(icon);
//				treeNodeTmp = forestMap.get(resourceCode);
//				children = treeNodeTmp.getChildrens();
//				children.add(treeChildrenNode);
//			}
//
//		}
//		return forestMap;
//	}
//
//    /**
//     * @title:获取角色加载权限
//     * @description : 获取角色加载权限。
//     * @param List
//     *            <Privilege> privilegeList
//     * @return JSON format string
//     * @throws Exception
//     * @throws
//     * @version 1.1
//     * @author shanming.yang
//     * @update 2012-5-10
//     */
//    public String initPrivilegeForRole(List<Privilege> privilegeList) {
//
//        return getPrivilegeForRole(privilegeList);
//    }
//
//    /**
//     * @title: 编辑角色权限
//     * @description : 编辑角色权限时候，首先加载所有权限，并且设置为未选中状态， 然后根据选中列表去设置已经选中的。
//     * @param List
//     *            <Privilege> privilegeList
//     * @return JSON format string
//     * @throws Exception
//     * @throws
//     * @version 1.1
//     * @author shanming.yang
//     * @update 2012-5-10
//     */
//    public String editPrivilegeForRole(List<Privilege> privilegeList,
//                                       Long[] selectedPrivilege) {
//        return editPrivilege(privilegeList, selectedPrivilege, Constant.ROLE);
//    }
//
//    /**
//     * @title: 编辑组权限
//     * @description : 编辑角色权限时候，首先加载所有权限，并且设置为未选中状态， 然后根据选中列表去设置已经选中的。
//     * @param List
//     *            <Privilege> privilegeList
//     * @return JSON format string
//     * @throws Exception
//     * @throws
//     * @version 1.1
//     * @author shanming.yang
//     * @update 2012-5-10
//     */
//    public String editPrivilegeForGroup(List<Privilege> privilegeList,
//                                        Long[] selectedPrivilege) {
//        return editPrivilege(privilegeList, selectedPrivilege, Constant.GROUP);
//    }
//
//    /**
//     * @title:获取用户组加载权限
//     * @description : 获取用户组加载权限。
//     * @param List
//     *            <Privilege> privilegeList
//     * @return JSON format string
//     * @throws Exception
//     * @throws
//     * @version 1.1
//     * @author shanming.yang
//     * @update 2012-5-10
//     */
//    public String initPrivilegeForGroup(List<Privilege> privilegeList) {
//
//        return getPrivilege(privilegeList);
//    }
//
//    /**
//     * @title:插入资源
//     * @description :用户插入资源。
//     * @param String
//     *            [][] resourceA
//     * @param type
//     * @return boolean
//     * @throws Exception
//     * @throws
//     * @version 1.1
//     * @author shanming.yang
//     * @update 2012-5-10
//     */
//    @Transactional
//    public boolean insertResource(String[][] resourceA, String type) {
//        long id;
//        String name = null;
//        String url = null;
//        boolean leaf = true;
//        boolean expanted = false;
//        String icon = null;
//        String referenceId = null;
//        String owerId = null;
//        String createId = null;
//        Resource resource = null;
//        Resource rootResource = null;
//        long parentId = -1;
//        int position;
//        String code = null;
//        String entity = null;
//        Activity activity = null;
//        List<Activity> rootActivityList = null;
//        long rootId;
//        if (resourceA == null || resourceA.length <= 0) {
//            logger.log(Level.ERROR, "parameter resourceA is null");
//            return false;
//        }
//        if (type == null || "".equals(type)) {
//            logger.log(Level.ERROR, "parameter type is null");
//            return false;
//        }
//        // 循环插入资源
//        for (int i = 0; i < resourceA.length; i++) {
//            // 取资源信息
//            name = resourceA[i][0];
//            url = resourceA[i][1];
//            leaf = Boolean.parseBoolean(resourceA[i][2]);
//            expanted = Boolean.parseBoolean(resourceA[i][3]);
//            icon = resourceA[i][4];
//            referenceId = resourceA[i][5];
//            owerId = resourceA[i][6];
//            createId =  resourceA[i][7];
//            // 寻找根资源信息
//            rootResource = resourceService.getResourceByName(type);
//            parentId = rootResource.getId();
//            entity = rootResource.getEntity();
//            // 找出同类资源中最大位置
//            position = resourceService.getMaxPositionLikeCode(type)
//                    .getPosition();
//
//            // 计算出位置
//            position = position + 1;
//            // 拼凑code
//            code = type + parentId + position;
//            // 存储resource
//            resource = new Resource();
//            resource.setName(name);
//            resource.setCode(code);
//            resource.setIcon(icon);
//            resource.setLeaf(leaf);
//            resource.setParentID(parentId);
//            resource.setPosition(position);
//            resource.setUrl(url);
//            resource.setExpanded(expanted);
//            resource.setEntity(entity);
//            resource.setReferenceId(referenceId);
//            resource.setOwnerId(owerId);
//            resource.setCreateId(createId);
//            resourceService.createResource(resource);
//            rootId = rootResource.getId();
//            // 查询 activity
//            rootActivityList = privilegeService.getActivityByResourceId(rootId);
//            for (int j = 0; j < rootActivityList.size(); j++) {
//                activity = rootActivityList.get(j);
//                privilegeService.createPrivilege(resource, activity, "user",
//                        false);
//            }
//        }
//        return true;
//    }
//
//    /**
//     * @title:updateResource
//     * @description :更新资源
//     * @param name
//     * @param oldReferenceId
//     * @param newReferenceId
//     * @param type
//     * @return boolean
//     * @throws Exception
//     * @throws
//     * @version 1.1
//     * @author shanming.yang
//     * @update 2012-5-10
//     */
//    @Transactional
//    public boolean updateResource(String name, String oldReferenceId,
//                                  String newReferenceId, String type) {
//        Resource resource = resourceDao.getResourceByReferenceId(
//                oldReferenceId, type);
//        if (resource != null) {
//            resource.setName(name);
//            resource.setReferenceId(newReferenceId);
//            resourceDao.save(resource);
//            return true;
//        } else {
//            return false;
//        }
//
//    }
//
//    /**
//     * @title:updateResource
//     * @description :
//     * @param type
//     * @return boolean
//     * @throws Exception
//     * @throws
//     * @version 1.1
//     * @author shanming.yang
//     * @throws SQLException
//     * @update 2012-5-10
//     */
//    @Transactional
//    public boolean deleteResource(String[] referenceIdA, String type) throws SQLException {
//        String referenceId = null;
//        List<Privilege> privileges;
//        Privilege privilege;
//        Resource resource;
//        String priIds = null;
//        for (int i = 0; i < referenceIdA.length; i++) {
//            // 取资源信息
//            referenceId = referenceIdA[i];
//            privileges = privilegeService.getPrivilegeByRefenceId(referenceId,
//                    type);
//            for (int k = 0; k < privileges.size(); k++) {
//                privilege = privileges.get(k);
//                priIds = priIds + privilege.getId()+",";
//            }
//            //去掉末尾多余的逗号
//            priIds = priIds.substring(0, priIds.length()-2);
//            resourceService.deleteResource(priIds);
//
//            for (int j = 0; j < privileges.size(); j++) {
//                privilege = privileges.get(j);
//                privilegeService.deletePrivilege(privilege.getId());
//            }
//            resource = resourceService.getResourceByReferenceId(referenceId,
//                    type);
//            resourceService.deleteResource(resource.getId());
//
//        }
//        return true;
//    }
//  
}

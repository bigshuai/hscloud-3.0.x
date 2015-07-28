//package com.hisoft.hscloud.crm.usermanager.util;
//
//import java.util.List;
//
//import org.apache.log4j.Level;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.hisoft.hscloud.crm.usermanager.entity.Activity;
//import com.hisoft.hscloud.crm.usermanager.entity.Admin;
//import com.hisoft.hscloud.crm.usermanager.entity.Group;
//import com.hisoft.hscloud.crm.usermanager.entity.Menu;
//import com.hisoft.hscloud.crm.usermanager.entity.Privilege;
//import com.hisoft.hscloud.crm.usermanager.entity.Resource;
//import com.hisoft.hscloud.crm.usermanager.entity.User;
//import com.hisoft.hscloud.crm.usermanager.service.AdminService;
//import com.hisoft.hscloud.crm.usermanager.service.ResourceService;
//import com.hisoft.hscloud.crm.usermanager.service.UserService;
//
//@Component
//public class AuthorPrivilege {
//	private Logger logger = Logger.getLogger(AuthorPrivilege.class);
//	@Autowired
//	private UserService userService;
//	@Autowired
//	private ResourceService resourceService;
//	@Autowired
//	private AdminService adminService;
//
//	public boolean author(String email, String userType, String action) {
//		boolean ifHasPrivilege = false;
//		Privilege privilege = null;
//		Resource resource = null;
//		Long resourceId = null;
//		Menu menu = null;
//		String url = null;
//		List<Privilege> privileges = null;
//		Activity activity = null;
//		String actionNameInDB = null;
//		User user = null;
//		Admin admin = null;
//		if (email == null || "".equals(email)) {
//			logger.log(Level.ERROR, "email is null.");
//			return false;
//		}
//
//		if (action == null || "".equals(action)) {
//			logger.log(Level.ERROR, "action is null.");
//			return false;
//		}
//
//		if (userType == null || "".equals(userType)) {
//			logger.log(Level.ERROR, "userType is null.");
//			return false;
//		}
//		if (Constant.ADMIN.equals(userType) && Constant.USER.equals(userType)) {
//			logger.log(Level.ERROR, "userType is error.");
//			return false;
//		}
//
//		// 取用户id，根据email
//		if (userType.equals(Constant.USER)) {
//			user = userService.getUserByEmail(email);
//			List<Group> listGroup = user.getGroups();
//			System.out.println("groups.size " + listGroup.size());
//			for (Group group1 : listGroup) {
//				privileges = group1.getPrivileges();
//				for (int i = 0; i < privileges.size(); i++) {
//					privilege = privileges.get(i);
//					activity = privilege.getActivity();
//					actionNameInDB = activity.getAction();
//					System.out.println("actionNameinDB " + i + "  :"
//							+ actionNameInDB);
//					if (action.equals(actionNameInDB)) {
//						ifHasPrivilege = true;
//						break;
//					}
//				}
//
//				// type = user.getUser_type();
//				// // 如果是子用户，找到子用户所属组，在根据组找到权限，根据权限找到action
//				// if (type.equals(Constant.SUB_USER)) {
//				// group = group1;
//				// privileges = group.getPrivileges();
//				//
//				// }
//			}
//		} else if (userType.equals(Constant.ADMIN)) {
//			admin = adminService.getAdminByEmail(email);
//			if(admin.getIsSuper()){
//				return true;
//			}
//			privileges = admin.getRole().getPrivileges();
//			for (int i = 0; i < privileges.size(); i++) {
//				privilege = privileges.get(i);
//				activity = privilege.getActivity();
//				actionNameInDB = activity.getAction();
//				if (action.equals(actionNameInDB)) {
//					ifHasPrivilege = true;
//					break;
//				}
//			}
//		}
//
//		// 菜单权限校验
//		for (int i = 0; i < privileges.size(); i++) {
//			privilege = privileges.get(i);
//			resource = privilege.getResource();
//			// 根据权限，取Resource ID
//			resourceId = resource.getId();
//			// 根据Resource id，取菜单
//			menu = resourceService.getMenuByResourceId(resourceId);
//			// 根据菜单id，取菜单URL
//			if (menu != null) {
//				url = menu.getUrl();
//				if (action.equals(url)) {
//					ifHasPrivilege = true;
//					break;
//				}
//			}
//
//		}
//		return ifHasPrivilege;
//	}
//}

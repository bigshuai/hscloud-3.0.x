package com.hisoft.hscloud.crm.usermanager.service;

import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.entity.UserProfile;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.vo.CountOfUserVO;
import com.hisoft.hscloud.crm.usermanager.vo.UserVO;
import com.hisoft.hscloud.crm.usermanager.vo.UserVO1;

public interface UserService {
	
	/**
	 * 创建用户（包括：企业用户，子用户，个人用户）
	 * @param user
	 * @return
	 */
	public long createUser(User user,String userType);
	
	/**
	 * 修改用户
	 * @param user
	 * @return 
	 */
	public User modifyUser(User user);
	
	/**
	 * 分页查找(无权限控制)
	 * @param sorts
	 * @param page
	 * @param userId
	 * @param domainIds  传null时为超级管理员 不将域作为查询条件
	 * @return
	 */
	public Page<User> searchUser(List<Sort>sorts,Page<User>page,List<Long> domainIds);
	
	public Page<User> searchUserByState(String userState, List<Sort> sorts,Page<User> page, List<Long> domainIds);
	
	public Page<User> searchUserBySupplier(String supplier, List<Sort> sorts,Page<User> page, List<Long> domainIds);
	
	/**
	 * 根据用户名分页查找(无权限控制)
	 * @param param
	 * @param sorts
	 * @param page
	 * @param userId
	 * @param domainIds  传null时为超级管理员 不将域作为查询条件
	 * @return
	 */
	public Page<User> searchUserLikeName(String username,List<Sort>sorts,Page<User>page,List<Long> domainIds);
	
	/**
	 * 根据邮箱分页查找(无权限控制)
	 * @param param
	 * @param sorts
	 * @param page
	 * @param userId
	 * @param domainIds  传null时为超级管理员 不将域作为查询条件
	 * @return
	 */
	public Page<User> searchUserLikeEmail(String email,List<Sort>sorts,Page<User>page,List<Long> domainIds);
	
	/**
	 * 根据域名分页查询(无权限控制)
	 * @param domainName  域名
	 * @param sorts
	 * @param page
	 * @param userId
	 * @param domainIds  传null时为超级管理员 不将域作为查询条件
	 * @return
	 */
	public Page<User> searchUserLikeDomain(String domainName, List<Sort> sorts,Page<User> page, List<Long> domainIds);
	
	/**
	 * 
	 * @param query  查询条件
	 * @param sorts  排序条件
	 * @param page   分页信息
	 * @param userType  用户类型
	 * @param userId    企业用户id
	 * @param primKeys  权限
	 * @return
	 */
   	public Page<UserVO> searchPermissionUser(String query,List<Sort>sorts,Page<User>page,String userType,long userId,List<Object> primKeys);
	
	
	/**
	 * 根据email查找用户
	 * @param email
	 * @return
	 */
//	public User getUser(String email);
	
	/**
	 * 根据用户id查找用户
	 * @param id
	 * @return
	 */
	public User getUser(long id);
	
//	/**
//	 * 根据用户id查找企业用户id
//	 * @param id  id 为子用户返回子用户企业用户id,为企业用户返回查询id.
//	 * @return
//	 */
//	public long getEntUserId(long id);
	
	/**
	 * 根据用户id查找用户
	 * @param id
	 * @return UserVO
	 */
	public UserVO getUserVO(long id);
	
	/**
	 * 获得子用户的企业用户
	 * @param subUserId
	 * @return
	 */
	public User getUserAdmin(long subUserId);
	
	/**
	 * 通过邮箱获得用户
	 * @param email
	 * @return
	 */
	public User findUserByEmail(String email);
	
	/**
	 * 前台用户根据邮箱，密码登陆。
	 * @param email
	 * @param password
	 * @return User
	 */
	public User loginUserByEmail(String email,String password);
	
	/**
	 *  前台用户根据邮箱，密码,域 登陆。
	 * @param email
	 * @param password
	 * @param domainId
	 * @return
	 */
	public User loginUserByEmail(String email, String password, long domainId);
	
	/**
	 * 后台用户根据邮箱，密码登陆。
	 * @param email
	 * @param password
	 * @return
	 */
	public Admin loginAdimnByEmail(String email,String password);

	
	/**
	 * 分页查询特定组下的用户
	 * @param page
	 * @param groupId 组id
	 * @return
	 */
	public Page<UserVO> pageUserInGroup(Page<User> page, List<Sort>sorts,String userType,long groupId,long userId);
	
	/**
	 * 重置密码
	 * @param password
	 */
	public void resetPassword(User user);

//	public long getCurrentUserAdminID(long userID);
	
	/**
	 * 获得所有企业用户（不包含子用户）
	 * @return
	 */
	public long getAllEntUser();
	
	/**
	 * 获得所有个人用户（普通用户）
	 */
	public long getAllNorUser();
	
	/**
	 * 通过用户类型查询前台用户
	 * @param userType
	 * @return
	 */
	public List<User> getUserByType(String userType);
	
	/**
	 * 通过组id查询子用户
	 * @param userGroupId
	 * @return
	 */
	public List<User> getUserGroupMembers(long userGroupId,String userType);
	
	/**
	 * 冻结用户
	 * @param userId 用户ID
	 */
	public void freezedUser(long userId);
	public void deleteUser(long userId);
	/**
	 * <保存用户资料> 
	* <功能详细描述> 
	* @param userProfile 
	* @see [类、类#方法、类#成员]
	 */
	public void modifyUserProfile(UserProfile userProfile);
	/**
	 * <根据主键Id获取user profile> 
	* <功能详细描述> 
	* @param id
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public UserProfile getUserProfileById(long id);
	/**
	 * <统计前台用户总数，及在线用户总数> 
	* <功能详细描述> 
	* @param id
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public CountOfUserVO getCountOfUser();
	/**
	 * <统计前台在线用户总数根据userType区分企业用户和个人用户> 
	* <功能详细描述> 
	* @param id
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public long getCountOfOnlineUser(String userType);
	
	public void modifyUserOnlineStatus(long userId,short onlineStatus);
	
	public List<UserVO> getSameCompanySubUser(long userId);

    /**
    *修改用户信息
    * @param user 
    * @see [类、类#方法、类#成员] 
    */
    public void modifyEnterpriseUser(User user);

	public User getUserByEmail(String email);
	
	public void enableUser(long userId);
	/**
	 * <查询所有有效的用户> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<UserVO> getAllAvailableUser(String email);

	public User getPersonalUser(User user);
	
	/**
	 * 根据 domainIds 集合 获得 domain下的所有用户
	 * @param domainIds
	 * @return
	 */
	public List<User> getUsersByDomainIds(List<Long> domainIds);
	
	public void saveUser(String sql,Map<String,Object> map);
	
	public List<Long> getUserIdsByDomainIds(List<Long> domainIds);
	
	
	
	
	public UserVO1 findUserVO1(long id);
	
    /**
     * @return
     * 获取所有用户电话号码
     */
    public List<String> getAllUserMobile();
    /**
     * @return
     * 获取审核通过的是供销商用户
     */
    public List<User> getAvailableSupplier(short supplier,short userStatus);
}

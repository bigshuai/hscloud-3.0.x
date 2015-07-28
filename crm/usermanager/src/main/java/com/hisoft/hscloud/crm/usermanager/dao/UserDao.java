package com.hisoft.hscloud.crm.usermanager.dao;



import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.vo.CountOfUserVO;
import com.hisoft.hscloud.crm.usermanager.vo.UserVO;

public interface UserDao {
	
	/**
	 * 保持对象
	 * @param user
	 */
	public void save(User user);
	
	/**
	 * 通过Id查询
	 * @param id
	 * @return
	 */
	public User get(long id);
	
	/**
	 * 通过Id查询
	 * @param id
	 * @return
	 */
	public User load(long id);
	
	/**
	 * 删除对象
	 * @param user
	 */
	public void delete(User user);
	
	/**
	 * 通过id删除对象
	 * @param id
	 */
	public void delete(long id);
	
	/**
	 * 通过hql查询用户
	 * @param hql
	 * @param values
	 * @return
	 */
	public List<User> find(String hql, Object... values);
	
	public List<User> findUser(String hql, Map<String,?> map);
	
	public List<User> findByIds(List<Long> ids);
	
	/**
	 * 获得所有前台用户
	 */
	public List<User> getAll();
	
	public List<User> findBy(String propertyName, Object value);
	
	/**
	 * 通过hql查询唯一值
	 * @param hql
	 * @param values
	 * @return
	 */
	public User findUnique(String hql, Object... values);
	
	/**
	 * 通过属性查询唯一值，不唯一报错
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public User findUniqueBy(String propertyName, Object value);
	
	/**
	 * 通过  email和password登陆
	 * @param email
	 * @param password
	 * @return
	 */
	public User LoginByEmail(String email, String password);
	
	/**
	 * 分页查询USER
	 * @param page
	 * @param hql
	 * @param values
	 * @return
	 */
	public Page<User> findPage(Page<User> page,String hql,Object... map);
	
	/**
	 * 分页查询USER
	 * @param page
	 * @param hql
	 * @param values
	 * @return
	 */
	public Page<User> findPage(final Page<User> page, final String hql, final Map<String, ?> map);
	
	public List<User> findBySQL(String sql, Map<String, ?> map);
	/**
	 * <不同类型用户数统计> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public CountOfUserVO getCountOfUser();
	
	public void saves(String sql,Map<String,Object> map);
	/**
	 * <根据domainId查询用户数据集> 
	* <功能详细描述> 
	* @param domainIds
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<Long> getUserIdsByDomainIds(List<Long> domainIds);
	
	public List<UserVO> getAllAvailableUser(String email);
	
	/**
	 * @return
	 * 获取所有可用用户电话号码
	 */
	public List<String> getAllUserMobile(String sql);
}

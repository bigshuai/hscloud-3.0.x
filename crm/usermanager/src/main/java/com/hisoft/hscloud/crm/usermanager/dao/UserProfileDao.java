package com.hisoft.hscloud.crm.usermanager.dao; 

import com.hisoft.hscloud.crm.usermanager.entity.UserProfile;
/**
 * 
* <对user_profile表的操作> 
* <功能详细描述> 
* 
* @author  houyh 
* @version  [版本号, 2012-10-10] 
* @see  [相关类/方法] 
* @since  [HSCLOUD/1.4]
 */
public interface UserProfileDao {
	/**
	 * <修改用户信息> 
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
	
	
    public void save(UserProfile userProfile);
	
    public UserProfile findUniqueBy(String propertyName, Object value);
}

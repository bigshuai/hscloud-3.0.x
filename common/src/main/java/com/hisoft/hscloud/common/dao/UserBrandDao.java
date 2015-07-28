package com.hisoft.hscloud.common.dao;

import java.util.List;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.entity.UserBrand;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.vo.UserUserBrandVO;


public interface UserBrandDao {
	/**
	 * <获取所有品牌数据> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<UserBrand> getAll();
	/**
	 * <添加品牌信息> 
	* <功能详细描述> 
	* @param code
	* @param description
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void addBrand(UserBrand brand)throws HsCloudException; 
	/**
	 * <根据物理主键获取实体bean> 
	* <功能详细描述> 
	* @param brandId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public UserBrand getBrandById(Long brandId)throws HsCloudException;
	/**
	 * <分页获取品牌数据> 
	* <功能详细描述> 
	* @param condition
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<UserBrand> getBrandByPage(String condition,Page<UserBrand> paging)throws HsCloudException;
	/**
	 * <获取所有未删除的品牌> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<UserBrand> getAllNormalBrand()throws HsCloudException;
	/**
	 * <校验品牌名称是否重复> 
	* <功能详细描述> 
	* @param name
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public boolean checkBrandNameDup(String name)throws HsCloudException;
	/**
	 * <根据品牌code获取品牌数据> 
	* <功能详细描述> 
	* @param code
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public UserBrand getBrandByCode(String code)throws HsCloudException;
	/**
	 * <根据用户查询用户及用户品牌信息> 
	* <功能详细描述> 
	* @param userId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public UserUserBrandVO getUserAndBrandByReferenceId(long referenceId)throws HsCloudException;
	
}
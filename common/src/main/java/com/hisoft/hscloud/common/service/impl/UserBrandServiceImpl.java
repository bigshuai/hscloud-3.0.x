/* 
* 文 件 名:  UserBrandServiceImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  houyh 
* 修改时间:  2013-1-23 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.common.service.impl; 

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.dao.UserBrandDao;
import com.hisoft.hscloud.common.entity.UserBrand;
import com.hisoft.hscloud.common.service.UserBrandService;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.vo.UserUserBrandVO;

/** 
 * <用户品牌业务管理> 
 * <功能详细描述> 
 * 
 * @author  houyh 
 * @version  [版本号, 2013-1-23] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class UserBrandServiceImpl implements UserBrandService {
	
	@Autowired
	private UserBrandDao brandDao;
	
	/** 
	 * @param brand
	 * @throws HsCloudException 
	 */
	public void addBrand(UserBrand brand) throws HsCloudException {
		brandDao.addBrand(brand);
	}

	/** 
	 * @param brandId
	 * @return
	 * @throws HsCloudException 
	 */
	public UserBrand getBrandById(Long brandId) throws HsCloudException {
		return brandDao.getBrandById(brandId);
	}

	/** 
	 * @param condition
	 * @param paging
	 * @return
	 * @throws HsCloudException 
	 */
	public Page<UserBrand> getBrandByPage(String condition,
			Page<UserBrand> paging) throws HsCloudException {
		return brandDao.getBrandByPage(condition, paging);
	}

	/** 
	 * @return
	 * @throws HsCloudException 
	 */
	public List<UserBrand> getAllNormalBrand() throws HsCloudException {
		return brandDao.getAllNormalBrand();
	}


	public boolean checkBrandNameDup(String name) throws HsCloudException {
		return brandDao.checkBrandNameDup(name);
	}

	public UserBrand getBrandByCode(String code) throws HsCloudException {
		return brandDao.getBrandByCode(code);
	}

	@Override
	public UserUserBrandVO getUserAndBrandByReferenceId(long referenceId)
			throws HsCloudException {
		return brandDao.getUserAndBrandByReferenceId(referenceId);
	}
	

}
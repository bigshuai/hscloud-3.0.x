/**
 * 
 */
package com.hisoft.hscloud.crm.usermanager.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.ExceptionServiceUtil;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.vo.UserBrandVO;
import com.hisoft.hscloud.crm.usermanager.dao.DomainDao;
import com.hisoft.hscloud.crm.usermanager.dao.RoleDomainDao;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.entity.RoleDomain;
import com.hisoft.hscloud.crm.usermanager.service.DomainService;
import com.hisoft.hscloud.crm.usermanager.util.Constant;
import com.hisoft.hscloud.crm.usermanager.vo.DomainVO;

/**
 * @author lihonglei
 *
 */
@Service
public class DomainServiceImpl implements DomainService{
	
    @Autowired
    private DomainDao domainDao;
    @Autowired
    private RoleDomainDao roleDomainDao;
    private Logger logger = Logger.getLogger(this.getClass());
    private ExceptionServiceUtil eService = new ExceptionServiceUtil(logger);
    
    /**
     * 查询分平台列表
     */
    @Override
    public Page<Map<String, Object>> findDomainList(Page<Map<String, Object>> page,
            long roleId) {
        Page<Domain> domainPage = new Page<Domain>();
        domainPage.setOrder("desc");
        domainPage.setOrderBy("id");
        domainPage.setPageNo(page.getPageNo());
        domainPage.setPageSize(page.getPageSize());
        domainPage = domainDao.findValidDomainPage(domainPage, null);
        List<RoleDomain> roleDomainList = roleDomainDao.findRoleDomainList(roleId);
        
        List<Domain> domainList = domainPage.getResult();
        
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for(Domain domain : domainList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", domain.getId());
            map.put("name", domain.getAbbreviation());
            map.put("param1", "false");
            for(RoleDomain roleDomain : roleDomainList) {
                if(domain.getId() == roleDomain.getDomainId()) {
                    map.put("param1", "true");
                }
            }
            result.add(map);
        }
        page.setResult(result);
        page.setTotalCount(domainPage.getTotalCount());
        return page;
    }
    
    /**
     * 查询分平台页
     * @param page
     * @return
     */
    @Override
    public Page<Domain> findDomainPage(Page<Domain> page, String query){
        return domainDao.findDomainPage(page, query);
    }

    /**
     * 分平台赋权
     * @see com.hisoft.hscloud.crm.usermanager.service.DomainService#editDomainPermission(java.lang.String, java.lang.String, long)
     */
    @Override
    public void editDomainPermission(String permissionValue,
            String resourceValue, long roleId) {
        String[] domainArray = resourceValue.split(",");
        Long[] domainLongArray = new Long[domainArray.length];
        for(int i = 0; i < domainArray.length; i++) {
            domainLongArray[i] = Long.valueOf(domainArray[i]);
        }
        roleDomainDao.batchDeleteRoleDomain(domainLongArray, roleId);
        
        if (StringUtils.isNotBlank(permissionValue)) {
            String[] permissionArray = permissionValue.split(",");
            Long[] permissionLongArray = new Long[permissionArray.length];
            for (int i = 0; i < permissionArray.length; i++) {
                permissionLongArray[i] = Long.valueOf(permissionArray[i]);
            }
            roleDomainDao.batchAdd(permissionLongArray, roleId);
        }
    }
    
    /**
     * 获取所有分平台
     * @return
     */
    @Override
    public List<Domain> getAllDomain() {
        return domainDao.getAllDomain();
    }
    
    /**
     * 编辑分平台
     * @param domain
     * @return
     */
    @Override
    public String editDomain(DomainVO domainVO, long adminId) {
        Domain domain = new Domain();
        
        Date today = new Date();
        
        Domain checkDomain = domainDao.getDomainByCondition(domainVO);
        if(checkDomain != null) {
            if(checkDomain.getAbbreviation().equals(domainVO.getAbbreviation())) {
                return Constants.DOMAIN_ABBREVIATION_EXIST;
            } 
            if(checkDomain.getName().equals(domainVO.getName())){
                return Constants.DOMAIN_NAME_EXIST;
            }
            if(checkDomain.getCode().equals(domainVO.getCode())) {
                return Constants.DOMAIN_CODE_EXIST;
            }
        }
        
        if(domainVO.getId() != 0l) { //修改
            domain = domainDao.getDomainById(domainVO.getId());
        } else { //添加,验证code
            /*domain = domainDao.getDomainByCode(domainVO.getCode());
            if(domain != null) {
                return Constants.EXIST;
            } else {
                domain = new Domain();
                domain.setCreateId(adminId);
                domain.setCreateTime(today);
            }*/
            domain = new Domain();
            domain.setCreateId(adminId);
            domain.setCreateTime(today);
        }
        try {
            BeanUtils.copyProperties(domain, domainVO);
        } catch (IllegalAccessException e) {
            eService.setMessage("domainVO转换domain错误");
            eService.throwException(Constants.DOMAIN_EXCEPTION, e);
        } catch (InvocationTargetException e) {
            eService.setMessage("domainVO转换domain错误");
            eService.throwException(Constants.DOMAIN_EXCEPTION, e);
        }
        
        domain.setStatus(Constant.DOMAIN_STATUS_VALID);
        domain.setUpdateId(adminId);
        domain.setUpdateTime(today);
        domainDao.edit(domain);
        domainVO.setId(domain.getId());
        return Constants.SUCCESS;
    }
    
    /**
     * 根据code获得分平台,查询不到code对应分平台返回空
     * @param code
     * @return
     */
    @Override
    public Domain getDomainByCode(String code) {
        return domainDao.getDomainByCode(code);
    }

    /**
     * 修改分平台状态
     * @see com.hisoft.hscloud.crm.usermanager.service.DomainService#deleteDomain(long)
     */
    @Override
    public void updateStatusDomain(long domainId, long adminId, String status) {
        Domain domain = domainDao.getDomainById(domainId);
        domain.setUpdateId(adminId);
        domain.setUpdateTime(new Date());
        domain.setStatus(status);
        domainDao.edit(domain);
    }
    
    /**
     * 根据id查询分平台
     * @param domainId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Domain getDomainById(long domainId) {
        return domainDao.getDomainById(domainId);
    }

	@Override
	public List<Domain> getDomainByAdmin(long adminId) {
		String sql = "select hd.* from hc_admin_role har left join hc_role_domain hrd on har.roleId=hrd.role_id left JOIN hc_domain hd on hrd.domain_id=hd.id WHERE hd.`status`=1 and har.adminId=(:adminId)";
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("adminId", adminId);
		List<Domain> domains = domainDao.findBySQL(sql, map);
		return domains;
	}

	@Override
	public Page<UserBrandVO> getRelatedBrand(Page<UserBrandVO> page, long domainId,
			String domainName) throws HsCloudException {
		return domainDao.getRelatedBrand(page, domainId, domainName);
	}

	@Override
	public Page<UserBrandVO> getUnRelatedBrand(Page<UserBrandVO> page,
			long domainId, String domainName) throws HsCloudException {
		return domainDao.getUnRelatedBrand(page, domainId, domainName);
	}

	@Override
	public List<UserBrandVO> getRelatedBrandByDomainId(long domainId)
			throws HsCloudException {
		return domainDao.getRelatedBrandByDomainId(domainId);
	}
	
	/**
     * <查询分平台下的品牌> 
    * <功能详细描述> 
    * @param abbreviation
    * @return
    * @throws HsCloudException 
    * @see [类、类#方法、类#成员]
     */
	@Override
	public List<UserBrandVO> getRelatedBrand(String abbreviation) throws HsCloudException {
		Long domainId = domainDao.getBrandIdByBrandAbbreviation(abbreviation);
		return domainDao.getRelatedBrandByDomainId(domainId);
	}

}
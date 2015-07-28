/* 
* 文 件 名:  AccessAccountAction.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-10-16 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.action; 

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.vo.AccessAccountVO;
import com.hisoft.hscloud.web.facade.Facade;

/** 
 * <API用户管理> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-10-16] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class AccessAccountAction extends HSCloudAction {

    /** 
    * 注释内容 
    */
    private static final long serialVersionUID = 5533563522666857452L;
    
    private Page<AccessAccountVO> accessAccountPage = new Page<AccessAccountVO>();
    private String query;
    private int page;
    private int limit;
    private AccessAccountVO accessAccountVO;
    
    private Logger logger = Logger.getLogger(this.getClass());
    
    @Autowired
    private Facade facade;
    
    public void findAccessAccountPage() {
        accessAccountPage.setPageNo(page);
        accessAccountPage.setPageSize(limit);
        try {
            if(StringUtils.isNotBlank(query)) {
                query = "%" + query.trim() + "%";
                query = new String(query.getBytes("iso8859_1"), "UTF-8");
            }
            accessAccountPage = facade.findAccessAccountPage(accessAccountPage, query);
            super.fillActionResult(accessAccountPage);
        } catch (Exception ex) {
            dealThrow(new HsCloudException(Constants.ACCESS_ACCOUNT_FIND_EXCEPTION,
                    "findAccessAccountPage Exception", logger, ex), Constants.ACCESS_ACCOUNT_EXCEPTION);
        }
    }
    
    public void deleteAccessAccount() {
        Admin admin=(Admin) super.getCurrentLoginUser();
        try {
            facade.deleteAccessAccount(accessAccountVO.getId());
            facade.insertOperationLog(admin,"删除API用户","删除API用户",Constants.RESULT_SUCESS);
        } catch (Exception ex) {
            facade.insertOperationLog(admin,"删除API用户错误:"+ex.getMessage(),"删除API用户",Constants.RESULT_FAILURE);
            dealThrow(new HsCloudException(Constants.ACCESS_ACCOUNT_DELETE_EXCEPTION,
                    "findAccessAccountPage Exception", logger, ex), Constants.ACCESS_ACCOUNT_EXCEPTION);
        }
    }
    
    public void saveAccessAccount() {
        Admin admin=(Admin) super.getCurrentLoginUser();
        try {
            String result = facade.saveAccessAccount(accessAccountVO);
            fillActionResult((Object) result);
            if(Constants.SUCCESS.equals(result)) {
                facade.insertOperationLog(admin,"编辑API用户","编辑API用户",Constants.RESULT_SUCESS);
            } else {
                facade.insertOperationLog(admin,"编辑AAPI用户错误:"+result,"编辑API用户",Constants.RESULT_FAILURE); 
            }
        } catch (Exception ex) {
            facade.insertOperationLog(admin,"编辑API用户错误:"+ex.getMessage(),"编辑API用户",Constants.RESULT_FAILURE);
            dealThrow(new HsCloudException( Constants.ACCESS_ACCOUNT_EDIT_EXCEPTION,
                    "findAccessAccountPage Exception", logger, ex), Constants.ACCESS_ACCOUNT_EXCEPTION);
        }
    }

    public Page<AccessAccountVO> getAccessAccountPage() {
        return accessAccountPage;
    }

    public void setAccessAccountPage(Page<AccessAccountVO> accessAccountPage) {
        this.accessAccountPage = accessAccountPage;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public AccessAccountVO getAccessAccountVO() {
        return accessAccountVO;
    }

    public void setAccessAccountVO(AccessAccountVO accessAccountVO) {
        this.accessAccountVO = accessAccountVO;
    }
    
}

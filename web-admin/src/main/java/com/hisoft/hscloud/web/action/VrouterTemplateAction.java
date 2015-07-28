/* 
* 文 件 名:  VrouterTemplateAction.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2014-1-27 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.action; 

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.vpdc.ops.vo.VrouterTemplateVO;
import com.hisoft.hscloud.web.facade.Facade;

/** 
 * <vrouterTemplate> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2014-1-27] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class VrouterTemplateAction extends HSCloudAction {

    /** 
    * 注释内容 
    */
    private static final long serialVersionUID = -4261290028520554469L;
    
    @Autowired
    private Facade facade;
    
    private Logger logger = Logger.getLogger(this.getClass());
    
    private VrouterTemplateVO vrouterTemplateVO;
    
    private Page<VrouterTemplateVO> pageVrouterTemplate = new Page<VrouterTemplateVO>();
    private int page;
    private int limit;

    public void pageVrouterTemplateList() {
        pageVrouterTemplate.setPageNo(page);
        pageVrouterTemplate.setPageSize(limit);
        pageVrouterTemplate = facade.pageVrouterTemplateVO(pageVrouterTemplate);
        super.fillActionResult(pageVrouterTemplate);
    }
    
    public void deleteVrouterTemplate() {
        Admin admin=(Admin) super.getCurrentLoginUser();
        try {
            facade.deleteVrouterTemplate(vrouterTemplateVO.getId());
            facade.insertOperationLog(admin,"删除Vrouter模板","删除Vrouter模板",Constants.RESULT_SUCESS);
        } catch (Exception ex) {
            facade.insertOperationLog(admin,"删除Vrouter模板错误:"+ex.getMessage(),"删除Vrouter模板",Constants.RESULT_FAILURE);
            dealThrow(Constants.VROUTER_TEMPLATE_DELETE_EXCEPTION, "deleteVrouterTemplate Exception", ex, logger);
        }
    }
    
    public void editVrouterTemplate() {
        Admin admin=(Admin) super.getCurrentLoginUser();
        try {
            String result = facade.editVrouterTemplate(vrouterTemplateVO);
            fillActionResult((Object) result);
            facade.insertOperationLog(admin,"编辑Vrouter模板","编辑Vrouter模板",Constants.RESULT_SUCESS);
        } catch (Exception ex) {
            facade.insertOperationLog(admin,"编辑Vrouter模板错误:"+ex.getMessage(),"编辑Vrouter模板",Constants.RESULT_FAILURE);
            dealThrow(Constants.VROUTER_TEMPLATE_EDIT_EXCEPTION, "editVrouterTemplate Exception", ex, logger);
        }
    }

    public Page<VrouterTemplateVO> getPageVrouterTemplate() {
        return pageVrouterTemplate;
    }

    public void setPageVrouterTemplate(Page<VrouterTemplateVO> pageVrouterTemplate) {
        this.pageVrouterTemplate = pageVrouterTemplate;
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

    public VrouterTemplateVO getVrouterTemplateVO() {
        return vrouterTemplateVO;
    }

    public void setVrouterTemplateVO(VrouterTemplateVO vrouterTemplateVO) {
        this.vrouterTemplateVO = vrouterTemplateVO;
    }
    
}

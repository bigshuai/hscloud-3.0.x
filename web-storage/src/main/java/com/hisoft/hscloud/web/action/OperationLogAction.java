/* 
* 文 件 名:  OperationLogAction.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-8-9 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.action; 

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.storage.vo.OperationLogVO;
import com.hisoft.hscloud.storage.vo.UserVO;
import com.hisoft.hscloud.web.facade.Facade;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-8-9] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class OperationLogAction extends HSCloudAction {

    /** 
    * 注释内容 
    */
    private static final long serialVersionUID = -4602696321062897576L;
    private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private Facade facade;
    
    private int page;
    private int limit;
    private String query;// 模糊查询条件
    private Date startDate;
    private Date endDate;
    private String sort;//排序
    
    private Page<OperationLogVO> operationLogVOPage = new Page<OperationLogVO>();
    
    //查询操作日志 
    public void findPage() {
        Map<String, Object> condition = new HashMap<String, Object>();
        try {
            UserVO userVO = (UserVO)this.getCurrentLoginUser();
            condition.put("username", userVO.getName());
            
            if(StringUtils.isNotBlank(query)) {
                condition.put("query", "%" + query + "%");
            }
            if(startDate != null ){
                condition.put("startDate", startDate);
            }
            if(endDate != null ){
                condition.put("endDate", endDate);
            }
            
            operationLogVOPage.setPageNo(page);
            operationLogVOPage.setPageSize(limit);
            pageOrderBy(operationLogVOPage, sort);
            if("operationTypeText".equalsIgnoreCase(operationLogVOPage.getOrderBy())) {
                operationLogVOPage.setOrderBy("operationType");
            }
            
            operationLogVOPage = facade.findOperationLogPage(operationLogVOPage, condition);
            
            super.fillActionResult(operationLogVOPage);
        } catch(Exception ex) {
            logger.info(ex);
        }
    }
    
    //设置排序
    private void pageOrderBy(Page<OperationLogVO> videoPage, String sort) {
        if(StringUtils.isNotBlank(sort)) {
            JSONArray jsonArray = JSONArray.fromObject(sort);
            JSONObject jsonObject = (JSONObject)jsonArray.get(0);
            videoPage.setOrder(jsonObject.get("direction").toString());
            videoPage.setOrderBy(jsonObject.get("property").toString());
        } else {
            videoPage.setOrder("desc");
            videoPage.setOrderBy("id");
        }
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

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Page<OperationLogVO> getOperationLogVOPage() {
        return operationLogVOPage;
    }

    public void setOperationLogVOPage(Page<OperationLogVO> operationLogVOPage) {
        this.operationLogVOPage = operationLogVOPage;
    }
    
}

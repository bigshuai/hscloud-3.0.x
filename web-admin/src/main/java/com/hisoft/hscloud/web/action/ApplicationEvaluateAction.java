package com.hisoft.hscloud.web.action; 
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.entity.Excel;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.web.facade.Facade;
import com.wgdawn.common.PageBean;
import com.wgdawn.conditions.model.EvaluatePageModel;
import com.wgdawn.persist.model.AppProductReview;
import com.wgdawn.persist.more.model.MoreAppProductReview;
import com.wgdawn.service.EvaluateService;
/** 
 * 应用评价管理
 * <功能详细描述> 
 * 
 * @author liutao
 * @version  [版本号, 2015-6-1] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class ApplicationEvaluateAction extends HSCloudAction {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());	
    @Autowired
    private EvaluateService evaluateService;

    @Autowired
    private Facade facade;
    private Page<MoreAppProductReview> appEvaluatePage = new Page<MoreAppProductReview>();
    private int page;
    private int limit;
    private AppProductReview appProductReview;
    private String replyComment;
    private Integer appScore;
    private String appName;
    private Integer appStatus;
	/**
     * 查询应用评价列表 
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void findAppEvaluateListPage(){
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findAppDetailListPage method.");			
		}
		EvaluatePageModel evaluatePageModel=new EvaluatePageModel();
		evaluatePageModel.setPageNo(page);
		evaluatePageModel.setPageSize(limit);
		evaluatePageModel.setAppName(appName);
		evaluatePageModel.setScore(appScore);
		evaluatePageModel.setStatus(appStatus);
		PageBean<MoreAppProductReview> vo=evaluateService.findEvaluateListBySupplierId(evaluatePageModel);
		appEvaluatePage.setResult(new ArrayList<MoreAppProductReview>());
		appEvaluatePage.setTotalCount(vo.getSize());
        appEvaluatePage.setResult(vo.getList());
        fillActionResult(appEvaluatePage);
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findAppEvaluateListPage method.takeTime:" + takeTime + "ms");
		}
    }
    /**
     * 删除应用评价 
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void delAppEvaluate(){
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter delAppEvaluate method.");			
		}
		appProductReview.setDeleteFlag(Constants.DELETE_STATUS_ONE);
		appProductReview.setUpdateId(Constants.ADMINISTRATOR_ID);
		appProductReview.setUpdateTime(new Date());
		evaluateService.deleteEvaluateById(appProductReview);
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit delAppEvaluate method.takeTime:" + takeTime + "ms");
		}
    }
    
    
    /**
     * 回复应用评价 
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void replyAppEvaluate(){
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter delAppEvaluate method.");			
		}
		evaluateService.insertEvaluateReply(appProductReview.getId(),Constants.ADMINISTRATOR_ID, replyComment);
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit delAppEvaluate method.takeTime:" + takeTime + "ms");
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
	public AppProductReview getAppProductReview() {
		return appProductReview;
	}
	public void setAppProductReview(AppProductReview appProductReview) {
		this.appProductReview = appProductReview;
	}
	public String getReplyComment() {
		return replyComment;
	}
	public void setReplyComment(String replyComment) {
		this.replyComment = replyComment;
	}
	public Integer getAppScore() {
		return appScore;
	}
	public void setAppScore(Integer appScore) {
		this.appScore = appScore;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public Integer getAppStatus() {
		return appStatus;
	}
	public void setAppStatus(Integer appStatus) {
		this.appStatus = appStatus;
	}
	
	
    
}

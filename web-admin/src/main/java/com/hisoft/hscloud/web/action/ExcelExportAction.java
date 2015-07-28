package com.hisoft.hscloud.web.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.hisoft.hscloud.bss.billing.vo.QueryVO;
import com.hisoft.hscloud.common.entity.Excel;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.web.facade.Facade;
import com.opensymphony.xwork2.ActionSupport;
import com.wgdawn.conditions.model.EvaluatePageModel;

public class ExcelExportAction extends ActionSupport {

	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = -6252806280967365991L;

	private Logger logger = Logger.getLogger(this.getClass());

	private QueryVO queryVO;//查询条件集合
	
	private List<Object> primKeys = null;
	
	@Autowired
	private Facade facade;
	
	private String appCategoryId;
	    
	private String  appStatus;
	
	private Integer appScore;
	   
	private String appName;
	
	private String appIsCommend;
	
	/**
	 * 导出消费日志
	 */
	public void excelExport() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter excelExport method.");			
		}
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			HttpServletRequest  request = ServletActionContext.getRequest();
			//查询登录管理员id,作为查询导出条件
			Admin admin = (Admin)request.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
			response.reset();
			String exportFileName = "TranscationLog.xls";

			String mimeType = "application/msexcel;charset=UTF-8";
			response.setContentType(mimeType);
			response.setHeader("Content-Disposition", "attachment;filename=\""
					+ exportFileName + "\"");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Cache-Control",
					"must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			response.setDateHeader("Expires",
					(System.currentTimeMillis() + 1000));

			Excel excel = facade.excelExport(admin,queryVO,this.primKeys);
			ServletOutputStream os = response.getOutputStream();
			excel.getWb().write(os);
			os.flush();
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit excelExport method.takeTime:" + takeTime + "ms");
		}
	}
	
	
	/**
	 * 导出应用列表
	 */
	public void excelExportApplication() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter excelExportApplication method.");			
		}
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			HttpServletRequest  request = ServletActionContext.getRequest();
			//查询登录管理员id,作为查询导出条件
			Admin admin = (Admin)request.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
			response.reset();
			String exportFileName = "ApplicationList.xls";

			String mimeType = "application/msexcel;charset=UTF-8";
			response.setContentType(mimeType);
			response.setHeader("Content-Disposition", "attachment;filename=\""
					+ exportFileName + "\"");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Cache-Control",
					"must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			response.setDateHeader("Expires",
					(System.currentTimeMillis() + 1000));
			Map<String,Object> queryMap = new HashMap<String,Object>();
			queryMap.put("appCategoryId",appCategoryId);
			queryMap.put("appStatus",appStatus);
			queryMap.put("appName",appName);
			queryMap.put("appIsCommend",appIsCommend);
			Excel excel = facade.excelExportApplication(queryMap);
			ServletOutputStream os = response.getOutputStream();
			excel.getWb().write(os);
			os.flush();
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit excelExportApplication method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 导出云应用账单表
	 */
	public void excelExportAppBill() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter excelExportAppBill method.");			
		}
		try {
			Map<String,Object> queryMap = new HashMap<String,Object>();
			if(queryVO != null){
				if(queryVO.getFuzzy() != null && queryVO.getFuzzy() !=""){
					queryVO.setFuzzy(new String(queryVO.getFuzzy().trim().getBytes("iso8859-1"),"UTF-8"));
					queryMap.put("fuzzy", queryVO.getFuzzy());
				}
				if(queryVO.getEmail()!=null && !"".equals(queryVO.getEmail())){
					queryMap.put("useremail", queryVO.getEmail());
				}
				if(queryVO.getStartTime()!=null){
					queryMap.put("startTime", queryVO.getStartTime());
				}
				if(queryVO.getEndTime()!=null){
					queryMap.put("endTime", queryVO.getEndTime());
				}
				queryMap.put("transcationType", queryVO.getTransactionType());
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.reset();
			String exportFileName = "AppBillTranscationLog.xls";
			String mimeType = "application/msexcel;charset=UTF-8";
			response.setContentType(mimeType);
			response.setHeader("Content-Disposition", "attachment;filename=\""
					+ exportFileName + "\"");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Cache-Control",
					"must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			response.setDateHeader("Expires",
					(System.currentTimeMillis() + 1000));
			Excel excel =facade.excelExportAppBill(queryMap);
			ServletOutputStream os = response.getOutputStream();
			excel.getWb().write(os);
			os.flush();
			os.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit excelExportAppBill method.takeTime:" + takeTime + "ms");
		}
	}
	
	   /**
		 * 导出应用评价列表
		 */
		public void excelExportAppEvaluate() {
			long beginRunTime = 0;
			if(logger.isDebugEnabled()){
				beginRunTime = System.currentTimeMillis();
				logger.debug("enter excelExportAppEvaluate method.");			
			}
			try {
				HttpServletResponse response = ServletActionContext.getResponse();
				HttpServletRequest  request = ServletActionContext.getRequest();
				//查询登录管理员id,作为查询导出条件
				Admin admin = (Admin)request.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
				response.reset();
				String exportFileName = "AppEvaluateList.xls";

				String mimeType = "application/msexcel;charset=UTF-8";
				response.setContentType(mimeType);
				response.setHeader("Content-Disposition", "attachment;filename=\""
						+ exportFileName + "\"");
				response.setHeader("Content-Transfer-Encoding", "binary");
				response.setHeader("Cache-Control",
						"must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				response.setDateHeader("Expires",
						(System.currentTimeMillis() + 1000));
				EvaluatePageModel evaluatePageModel=new EvaluatePageModel();
				evaluatePageModel.setAppName(appName);
				evaluatePageModel.setScore(appScore);
				if(null!=appStatus&&!"".equals(appStatus)){
					evaluatePageModel.setStatus(Integer.valueOf(appStatus));
				}
				Excel excel = facade.excelExportAppEvaluate(evaluatePageModel);
				ServletOutputStream os = response.getOutputStream();
				excel.getWb().write(os);
				os.flush();
				os.close();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(logger.isDebugEnabled()){
				long takeTime = System.currentTimeMillis() - beginRunTime;
				logger.debug("exit excelExportAppEvaluate method.takeTime:" + takeTime + "ms");
			}
		}
	
		/**
		 * 导出云应用订单信息
		 */
		public void excelExportAppOrder() {
			long beginRunTime = 0;
			if(logger.isDebugEnabled()){
				beginRunTime = System.currentTimeMillis();
				logger.debug("enter excelExportAppOrder method.");			
			}
			try {
				Map<String,Object> queryMap = new HashMap<String,Object>();
				if(queryVO != null){
					if(queryVO.getFuzzy() != null && queryVO.getFuzzy() !=""){
						queryVO.setFuzzy(new String(queryVO.getFuzzy().trim().getBytes("iso8859-1"),"UTF-8"));
						queryMap.put("fuzzy", queryVO.getFuzzy());
					}
					queryMap.put("orderStatus", queryVO.getTransactionType());
				}
				HttpServletResponse response = ServletActionContext.getResponse();
				response.reset();
				String exportFileName = "AppOrderDetail.xls";
				String mimeType = "application/msexcel;charset=UTF-8";
				response.setContentType(mimeType);
				response.setHeader("Content-Disposition", "attachment;filename=\""
						+ exportFileName + "\"");
				response.setHeader("Content-Transfer-Encoding", "binary");
				response.setHeader("Cache-Control",
						"must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				response.setDateHeader("Expires",
						(System.currentTimeMillis() + 1000));
				Excel excel =facade.excelExportAppOrder(queryMap);
				ServletOutputStream os = response.getOutputStream();
				excel.getWb().write(os);
				os.flush();
				os.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
			if(logger.isDebugEnabled()){
				long takeTime = System.currentTimeMillis() - beginRunTime;
				logger.debug("exit excelExportAppOrder method.takeTime:" + takeTime + "ms");
			}
		}
		
//	public void responsibilityExcelExport(){
//		long beginRunTime = 0;
//		if(logger.isDebugEnabled()){
//			beginRunTime = System.currentTimeMillis();
//			logger.debug("enter excelExport method.");			
//		}
//		try {
//			queryVO.setDomainId("1");
//			HttpServletResponse response = ServletActionContext.getResponse();
//			HttpServletRequest  request = ServletActionContext.getRequest();
//			//查询登录管理员id,作为查询导出条件
//			Admin admin = (Admin)request.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
//			response.reset();
//			String exportFileName = null != queryVO && null != queryVO.getMonth() && !"".equals(queryVO.getMonth())?queryVO.getMonth()+".xls":"responsibility.xls";
//			queryVO.setExportWay("responsibility/"+(null != queryVO && null != queryVO.getMonth() && !"".equals(queryVO.getMonth())?queryVO.getMonth()+"_"+queryVO.getDomainId()+".xls":"responsibility.xls"));
//			String mimeType = "application/msexcel;charset=UTF-8";
//			response.setContentType(mimeType);
////			response.setHeader("Content-Disposition", "attachment;filename=\""
////					+new String("权责报表_".getBytes(),"iso8859-1")+ exportFileName + "\"");
//			response.setHeader("Content-Disposition", "attachment;filename=\""
//					+new String("权责报表_".getBytes("GBK"),"iso8859-1")+ exportFileName + "\"");
//			response.setHeader("Content-Transfer-Encoding", "binary");
//			response.setHeader("Cache-Control",
//					"must-revalidate, post-check=0, pre-check=0");
//			response.setHeader("Pragma", "public");
//			response.setDateHeader("Expires",
//					(System.currentTimeMillis() + 1000));
//			Excel excel = facade.responsibilityExcelExport(admin,queryVO);
//			ServletOutputStream os = response.getOutputStream();
//			excel.getWb().write(os);
//			os.flush();
//			os.close();
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if(logger.isDebugEnabled()){
//			long takeTime = System.currentTimeMillis() - beginRunTime;
//			logger.debug("exit excelExport method.takeTime:" + takeTime + "ms");
//		}
//	}
	
	public void responsibilityExcelExport(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
		beginRunTime = System.currentTimeMillis();
		logger.debug("enter excelExport method.");			
	}
	try {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest  request = ServletActionContext.getRequest();
		//查询登录管理员id,作为查询导出条件
		Admin admin = (Admin)request.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
		response.reset();
		String exportFileName = null != queryVO && null != queryVO.getMonth() && !"".equals(queryVO.getMonth())?queryVO.getMonth():"responsibility";
		queryVO.setExportWay("responsibility/"+(null != queryVO && null != queryVO.getMonth() && !"".equals(queryVO.getMonth())?queryVO.getMonth()+"_"+queryVO.getDomainId():"responsibility"));
		String mimeType = "application/x-msdownload;charset=UTF-8";
		response.setContentType(mimeType);
		response.setHeader("Content-Disposition", "attachment;filename=\""
				+new String("权责报表_".getBytes("GBK"),"iso8859-1")+ exportFileName + ".zip\"");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Cache-Control",
				"must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		response.setDateHeader("Expires",
				(System.currentTimeMillis() + 1000));
		InputStream is = facade.responsibilityExcelExport(admin,queryVO);
		ServletOutputStream os = response.getOutputStream();
		this.output(is, os);

	} catch (IOException e) {
		e.printStackTrace();
	} catch (Exception e) {
		e.printStackTrace();
	}
	if(logger.isDebugEnabled()){
		long takeTime = System.currentTimeMillis() - beginRunTime;
		logger.debug("exit excelExport method.takeTime:" + takeTime + "ms");
	}
	}
	
	public void monthStatisExcelExport() {
		
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter excelExport method.");
		}
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			HttpServletRequest  request = ServletActionContext.getRequest();
			//查询登录管理员id,作为查询导出条件
			Admin admin = (Admin)request.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
			response.reset();
			String exportFileName = "summary.xls";

			String mimeType = "application/msexcel;charset=UTF-8";
			response.setContentType(mimeType);
			response.setHeader("Content-Disposition", "attachment;filename=\""
					+ exportFileName + "\"");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Cache-Control",
					"must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			response.setDateHeader("Expires",
					(System.currentTimeMillis() + 1000));

			Excel  excel = facade.monthStatisExcelExport(admin, queryVO);
			ServletOutputStream os = response.getOutputStream();
			excel.getWb().write(os);
			os.flush();
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit excelExport method.takeTime:" + takeTime + "ms");
		}
		
	}
	
	
public void IPMessageExcelExport() {
		
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter excelExport method.");
		}
		try {
//			File file = new File(getClass().getResource("/").getPath() + "responsibility/IPMessage"+".xls"); 
//			File file2 = new File(getClass().getResource("/").getPath() + "responsibility/IPMessage"+".zip"); 
//			if(file.exists()||file2.exists()){
//			     file.delete(); 
//			     file2.delete();
//
//			   }
//			   else{
//			     System.out.println("文件不存在");
//			   }
			HttpServletResponse response = ServletActionContext.getResponse();
			HttpServletRequest  request = ServletActionContext.getRequest();
			//查询登录管理员id,作为查询导出条件
			Admin admin = (Admin)request.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
			response.reset();
//			String exportFileName = "IPMessage.zip";
			String exportFileName = "IPMessage.xls";

/*			String mimeType = "application/x-msdownload;charset=UTF-8";*/
			String mimeType = "application/msexcel;charset=UTF-8";
			response.setContentType(mimeType);
			response.setHeader("Content-Disposition", "attachment;filename=\""
					+ exportFileName + "\"");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Cache-Control",
					"must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			response.setDateHeader("Expires",
					(System.currentTimeMillis() + 1000));

			Excel  excel = facade.IPMessageExcelExport(admin, queryVO);
			ServletOutputStream os = response.getOutputStream();
			excel.getWb().write(os);
			os.flush();
			os.close();
			//增加了打为压缩包的功能
			//InputStream is = facade.IPMessageExcelExport(admin,queryVO);
			//ServletOutputStream os = response.getOutputStream();
			//this.output(is, os);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit excelExport method.takeTime:" + takeTime + "ms");
		}
		
	}

	public QueryVO getQueryVO() {
		return queryVO;
	}

	public void setQueryVO(QueryVO queryVO) {
		this.queryVO = queryVO;
	}

	public List<Object> getPrimKeys() {
		return primKeys;
	}

	public void setPrimKeys(List<Object> primKeys) {
		this.primKeys = primKeys;
	}
	
	private void output(InputStream is,ServletOutputStream os){
		try {

			int ch;
			while ((ch = is.read()) != -1) {
				os.write(ch);
			}
			is.close();
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}





	public String getAppCategoryId() {
		return appCategoryId;
	}


	public void setAppCategoryId(String appCategoryId) {
		this.appCategoryId = appCategoryId;
	}


	public String getAppStatus() {
		return appStatus;
	}


	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
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


	public String getAppIsCommend() {
		return appIsCommend;
	}


	public void setAppIsCommend(String appIsCommend) {
		this.appIsCommend = appIsCommend;
	}
	
	
	

}

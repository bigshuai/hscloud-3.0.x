package com.hisoft.hscloud.mss.report.action;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.mss.report.entity.IncomeReport;
import com.hisoft.hscloud.mss.report.service.IncomeReportService;
import com.hisoft.hscloud.mss.report.util.Tools;
import com.hisoft.hscloud.mss.report.vo.ReportJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Roger
 * Version: 1.1
 * Date: 12-5-11
 * Time: 上午11:17
 * To change this template use File | Settings | File Templates.
 */
    public class IncomeReportAction extends HSCloudAction{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(IncomeReportAction.class);

    private String beginDate;
    private String endDate;

    @Autowired
    private IncomeReportService incomeReportService;

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
	 *
	 * @title: Income Report
	 * @description 根据year查询用户报表
	 * @return IncomeReport
	 * @throws
	 * @version 1.1
	 * @author roger.tong
	 * @update 2012-5-11 上午10:21:54
	 */
	public void getIncomeReportByYear() {
		logger.info("get start");
		
		if(beginDate == null || endDate == null){
			fillActionResult(new ArrayList<ReportJson>());
			logger.info("beginDate or endDate is null");
			return ;
		}
		
        int beginYear = Tools.stringToInteger("year",beginDate,endDate)[0];
        int endYear = Tools.stringToInteger("year",beginDate,endDate)[1];

        List<IncomeReport> incomeReports =  incomeReportService.FetchByYear(beginYear,endYear);

        List<ReportJson>  reportJsons = new ArrayList<ReportJson>();

        for(IncomeReport incomeReport : incomeReports){
            ReportJson reportJson = new ReportJson();
            reportJson.setId(incomeReport.getId());
            String date = String.valueOf(incomeReport.getYear()) + "/" + String.valueOf(incomeReport.getMonth()) + "/" + String.valueOf(incomeReport.getDay());
            reportJson.setIndex(date);
            reportJson.setNum(incomeReport.getAmount().intValue());
            reportJsons.add(reportJson);
        }

        fillActionResult(reportJsons);

		logger.info("get end");
	}

    /**
	 *
	 * @title: Income Report
	 * @description 根据year查询用户报表
	 * @return IncomeReport
	 * @throws
	 * @version 1.1
	 * @author roger.tong
	 * @update 2012-5-11 上午10:21:54
	 */
	public void getIncomeReportByMonth() {
		logger.info("get start");
        int beginMonth = Tools.stringToInteger("month",beginDate,endDate)[0];
        int endMonth = Tools.stringToInteger("month",beginDate,endDate)[1];

        List<IncomeReport> incomeReports =  incomeReportService.FetchByMonth(beginMonth,endMonth);

        List<ReportJson>  reportJsons = new ArrayList<ReportJson>();

        for(IncomeReport incomeReport : incomeReports){
            ReportJson reportJson = new ReportJson();
            reportJson.setId(incomeReport.getId());
            String date = String.valueOf(incomeReport.getYear()) + "/" + String.valueOf(incomeReport.getMonth()) + "/" + String.valueOf(incomeReport.getDay());
            reportJson.setIndex(date);
            reportJson.setNum(incomeReport.getAmount().intValue());
            reportJsons.add(reportJson);
        }

        fillActionResult(reportJsons);

		logger.info("get end");
	}
}

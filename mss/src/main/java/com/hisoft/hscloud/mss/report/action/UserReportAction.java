package com.hisoft.hscloud.mss.report.action;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.mss.report.entity.UserReport;
import com.hisoft.hscloud.mss.report.service.UserReportService;
import com.hisoft.hscloud.mss.report.util.Tools;
import com.hisoft.hscloud.mss.report.vo.ReportJson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserReportAction extends HSCloudAction{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(UserReportAction.class);

    private String beginDate;
    private String endDate;

    @Autowired
    private UserReportService userReportService;

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
	public void getUserReportByYear() {
		logger.info("get start");
		
		if(beginDate == null || endDate == null){
			fillActionResult(new ArrayList<ReportJson>());
			logger.info("beginDate or endDate is null");
			return ;
		}
		
        int beginYear = Tools.stringToInteger("year", beginDate, endDate)[0];
        int endYear = Tools.stringToInteger("year",beginDate,endDate)[1];

        List<UserReport> userReports =  userReportService.FetchByYear(beginYear,endYear);

        List<ReportJson>  reportJsons = new ArrayList<ReportJson>();

        for(UserReport userReport : userReports){
            ReportJson reportJson = new ReportJson();
            reportJson.setId(userReport.getId());
            String date = String.valueOf(userReport.getYear())+"/"+String.valueOf(userReport.getMonth())+"/"+String.valueOf(userReport.getDay());
            reportJson.setIndex(date);
            reportJson.setNum(userReport.getRegistrationTotal().intValue());
            reportJsons.add(reportJson);
        }

        fillActionResult(reportJsons);

		logger.info("get end");

	}

    /**
	 *
	 * @title: Income Report
	 * @description 根据month查询用户报表
	 * @return IncomeReport
	 * @throws
	 * @version 1.1
	 * @author roger.tong
	 * @update 2012-5-11 上午10:21:54
	 */
	public void getUserReportByMonth() {
		logger.info("get start");
        int beginMonth = Tools.stringToInteger("month", beginDate, endDate)[0];
        int endMonth = Tools.stringToInteger("month",beginDate,endDate)[1];

        List<UserReport> userReports =  userReportService.FetchByMonth(beginMonth,endMonth);

        List<ReportJson>  reportJsons = new ArrayList<ReportJson>();

        for(UserReport userReport : userReports){
            ReportJson reportJson = new ReportJson();
            reportJson.setId(userReport.getId());
            String date = String.valueOf(userReport.getYear())+"/"+String.valueOf(userReport.getMonth())+"/"+String.valueOf(userReport.getDay());
            reportJson.setIndex(date);
            reportJson.setNum(userReport.getRegistrationTotal().intValue());
            reportJsons.add(reportJson);
        }
        fillActionResult(reportJsons);

		logger.info("get end");
	}

    /**
	 *
	 * @title: Income Report
	 * @description 根据month查询用户报表
	 * @return IncomeReport
	 * @throws
	 * @version 1.1
	 * @author roger.tong
	 * @update 2012-5-11 上午10:21:54
	 */
	public void getUserReportByDay() {
        logger.info("get start");
        int beginDay = Tools.stringToInteger("day", beginDate, endDate)[0];
        int endDay = Tools.stringToInteger("day",beginDate,endDate)[1];

        List<UserReport> userReports =  userReportService.FetchByDay(beginDay,endDay);

        List<ReportJson>  reportJsons = new ArrayList<ReportJson>();

        for(UserReport userReport : userReports){
            ReportJson reportJson = new ReportJson();
            reportJson.setId(userReport.getId());
            String date = String.valueOf(userReport.getYear())+"/"+String.valueOf(userReport.getMonth())+"/"+String.valueOf(userReport.getDay());
            reportJson.setIndex(date);
            reportJson.setNum(userReport.getRegistrationTotal().intValue());
            reportJsons.add(reportJson);
        }
        fillActionResult(reportJsons);

		logger.info("get end");
	}

}

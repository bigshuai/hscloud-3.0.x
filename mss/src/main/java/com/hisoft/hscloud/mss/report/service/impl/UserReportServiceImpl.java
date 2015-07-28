package com.hisoft.hscloud.mss.report.service.impl;

import com.hisoft.hscloud.mss.report.dao.UserReportDao;
import com.hisoft.hscloud.mss.report.entity.UserReport;
import com.hisoft.hscloud.mss.report.service.UserReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Roger.Tong
 * Version: 1.1
 * Date: 12-5-11
 * Time: 上午11:28
 * To change this template use File | Settings | File Templates.
 */
@Service
public class UserReportServiceImpl implements UserReportService{
    @Autowired
    private UserReportDao userReportDao;

    @Override
    public List<UserReport> FetchByYear(int beginYear, int endYear) {
        List<UserReport> userReports = new ArrayList<UserReport>();
        List objects = userReportDao.selectByYear(beginYear, endYear);

        for (int i = 0; i < objects.size(); i++) {
            Object[] o = (Object[]) objects.get(i);
            UserReport userReport = new UserReport();
            userReport.setId((Integer) o[0]);
            userReport.setYear((Integer) o[1]);
            userReport.setMonth((Integer) o[2]);
            userReport.setDay((Integer) o[3]);
            userReport.setLogintimes((Integer) o[4]);
            userReport.setRegistrationTotal((BigDecimal) o[5]);
            userReports.add(userReport);
        }
        return userReports;
    }

    @Override
    public List<UserReport> FetchByMonth(int beginMonth, int endMonth) {
        List<UserReport> userReports = new ArrayList<UserReport>();
        List objects = userReportDao.selectByMonth(beginMonth,endMonth);
        for (int i = 0; i < objects.size(); i++) {
            Object[] o = (Object[]) objects.get(i);
            UserReport userReport = new UserReport();
            userReport.setId((Integer) o[0]);
            userReport.setYear((Integer) o[1]);
            userReport.setMonth((Integer) o[2]);
            userReport.setDay((Integer) o[3]);
            userReport.setLogintimes((Integer) o[4]);
            userReport.setRegistrationTotal((BigDecimal) o[5]);
            userReports.add(userReport);
        }
        return userReports;
    }

    @Override
    public List<UserReport> FetchByDay(int beginDay, int endDay) {
        List<UserReport> userReports = new ArrayList<UserReport>();
         List objects = userReportDao.selectByDay(beginDay,endDay);
        for (int i = 0; i < objects.size(); i++) {
            Object[] o = (Object[]) objects.get(i);
            UserReport userReport = new UserReport();
            userReport.setId((Integer) o[0]);
            userReport.setYear((Integer) o[1]);
            userReport.setMonth((Integer) o[2]);
            userReport.setDay((Integer) o[3]);
            userReport.setLogintimes((Integer) o[4]);
            userReport.setRegistrationTotal((BigDecimal) o[5]);
            userReports.add(userReport);
        }
        return userReports;
    }
}

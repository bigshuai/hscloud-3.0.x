package com.hisoft.hscloud.mss.report.service.impl;

import com.hisoft.hscloud.mss.report.dao.IncomeReportDao;
import com.hisoft.hscloud.mss.report.entity.IncomeReport;
import com.hisoft.hscloud.mss.report.entity.UserReport;
import com.hisoft.hscloud.mss.report.service.IncomeReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Roger
 * Date: 12-5-11
 * Time: 上午11:28
 * To change this template use File | Settings | File Templates.
 */
@Service
public class IncomeReportServiceImpl implements IncomeReportService {
    @Autowired
    private IncomeReportDao incomeReportDao;

    @Override
    public List<IncomeReport> FetchByYear(int beginYear, int endYear) {
        List<IncomeReport> incomeReports = new ArrayList<IncomeReport>();
        List objects = incomeReportDao.selectByYear(beginYear,endYear);

        for (int i = 0; i < objects.size(); i++) {
            Object[] o = (Object[]) objects.get(i);
            IncomeReport incomeReport = new IncomeReport();
            incomeReport.setId((Integer) o[0]);
            incomeReport.setYear((Integer) o[1]);
            incomeReport.setMonth((Integer) o[2]);
            incomeReport.setDay((Integer) o[3]);
            incomeReport.setAmount((BigDecimal) o[4]);
            incomeReports.add(incomeReport);
        }
        return incomeReports;
    }

    @Override
    public List<IncomeReport> FetchByMonth(int beginMonth, int endMonth) {
        List<IncomeReport> incomeReports = new ArrayList<IncomeReport>();
        List objects = incomeReportDao.selectByMonth(beginMonth,endMonth);

        for (int i = 0; i < objects.size(); i++) {
            Object[] o = (Object[]) objects.get(i);
            IncomeReport incomeReport = new IncomeReport();
            incomeReport.setId((Integer) o[0]);
            incomeReport.setYear((Integer) o[1]);
            incomeReport.setMonth((Integer) o[2]);
            if(null == o[3]) incomeReport.setDay(0);
            else incomeReport.setDay((Integer) o[3]);
            incomeReport.setAmount((BigDecimal) o[4]);
            incomeReports.add(incomeReport);
        }
        return incomeReports;
    }
}

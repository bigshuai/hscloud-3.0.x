package com.hisoft.hscloud.mss.report.dao;

import com.hisoft.hscloud.mss.report.entity.IncomeReport;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Roger
 * Version:1.1
 * Date: 12-5-11
 * Time: 上午10:20
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class IncomeReportDao extends HibernateDao<IncomeReport, Long> {
    /**
	 *
	 * @title: report
	 * @description 根据year查询用户情况
	 * @param beginYear
     * @param endYear
	 * @return 设定文件
	 * @return IncomeReport    返回类型
	 * @throws
	 * @version 1.1
	 * @author roger.tong
	 * @update 2012-5-11 上午10:36:11
	 */
	public List selectByYear(int beginYear, int endYear){

        Session session = null;
        String sql = "select  hr.id, hr.year, hr.month, hr.day, sum(hr.amount) amount from hc_rincome hr where hr.year >= :beginYear and hr.year <= :endYear group by hr.year order by hr.year";
        List list = new ArrayList();

        try{
               session = this.getSession();
               Query query = session.createSQLQuery(sql).setParameter("beginYear",beginYear).setParameter("endYear",endYear);
               list = query.list();
        }catch (HibernateException e){
            e.printStackTrace();
        }finally {
           session.close();
        }
        return list;
	}

    /**
	 *
	 * @title: report
	 * @description 根据month查询用户情况
	 * @param beginMonth
     * @param endMonth
	 * @return 设定文件
	 * @return IncomeReport    返回类型
	 * @throws
	 * @version 1.1
	 * @author roger.tong
	 * @update 2012-5-11 上午10:36:11
	 */
	public List selectByMonth(int beginMonth, int endMonth){
        Session session = null;
        String sql = "select  hr.id, hr.year, hr.month, hr.day, sum(hr.amount) amount from hc_rincome hr where hr.year*100+hr.month >= :beginMonth and hr.year*100+hr.month <= :endMonth group by hr.year*100+hr.month order by hr.year*100+hr.month";
        List list = new ArrayList();

        try{
               session = this.getSession();
               Query query = session.createSQLQuery(sql).setParameter("beginMonth",beginMonth).setParameter("endMonth",endMonth);
               list = query.list();
        }catch (HibernateException e){
            e.printStackTrace();
        }finally {
           session.close();
        }
        return list;	}
}

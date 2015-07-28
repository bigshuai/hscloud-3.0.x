package com.hisoft.hscloud.mss.report.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Roger
 * Date: 12-5-11
 * Time: 上午9:56
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="hc_ruser")
public class UserReport {
    private int id;
    private int year;
    private int month;
    private int day;
    private int logintimes;
    private BigDecimal registrationTotal;

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name="year")
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Column(name="month")
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Column(name="day")
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Column(name="logintimes")
    public int getLogintimes() {
        return logintimes;
    }

    public void setLogintimes(int logintimes) {
        this.logintimes = logintimes;
    }

    @Column(name="registrationTotal")
    public BigDecimal getRegistrationTotal() {
        return registrationTotal;
    }

    public void setRegistrationTotal(BigDecimal registrationTotal) {
        this.registrationTotal = registrationTotal;
    }

    @Override
    public String toString() {
        return "IncomeReport{" +
                "id=" + id +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", logintimes=" + logintimes +
                ", registrationTotal=" + registrationTotal +
                '}';
    }
}

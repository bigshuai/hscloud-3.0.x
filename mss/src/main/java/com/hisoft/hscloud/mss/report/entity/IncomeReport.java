package com.hisoft.hscloud.mss.report.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Roger
 * Date: 12-5-11
 * Time: 上午9:56
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="hc_rincome")
public class IncomeReport {
     private  int id;
     private  int year;
     private  int month;
     private  int day;
     private  BigDecimal amount;

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

    @Column(name="amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "IncomeReport{" +
                "id=" + id +
                ", year=" + year +
                ", month=" + month +
                ", amount=" + amount +
                '}';
    }
}

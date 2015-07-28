package com.hisoft.hscloud.mss.report.vo;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Roger.Tong
 * Date: 12-5-16
 * Time: 下午6:17
 * To change this template use File | Settings | File Templates.
 */
public class ReportJson {
    int id;
    String index;
    int num;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "ReportJson{" +
                "id=" + id +
                ", index=" + index +
                ", num=" + num +
                '}';
    }
}

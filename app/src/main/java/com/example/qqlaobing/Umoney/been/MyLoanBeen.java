package com.example.qqlaobing.Umoney.been;

import java.util.List;

/**
 * Created by zhanglizhi on 2018/7/13.
 */

public class MyLoanBeen {
    private String damount;//贷款金额
    private String interest;//服务费
    private String orderno;//订单号
    private String status;//状态
    private String overduefee;//逾期费
    private String time;//订单生成日期

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOverduefee() {
        return overduefee;
    }

    public void setOverduefee(String overduefee) {
        this.overduefee = overduefee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDamount() {
        return damount;
    }

    public void setDamount(String damount) {
        this.damount = damount;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getRefundtime() {
        return refundtime;
    }

    public void setRefundtime(String refundtime) {
        this.refundtime = refundtime;
    }

    private String deadline;//到期时间
    private String refundtime;//剩余还款日
    private List<FeiYong> feiYongList;

    public List<FeiYong> getFeiYongList() {
        return feiYongList;
    }

    public void setFeiYongList(List<FeiYong> feiYongList) {
        this.feiYongList = feiYongList;
    }

    public static class FeiYong{
        private String val;
        private String desc;

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}

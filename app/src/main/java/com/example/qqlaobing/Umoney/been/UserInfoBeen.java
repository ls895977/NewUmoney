package com.example.qqlaobing.Umoney.been;

/**
 * Created by zhanglizhi on 2018/7/25.
 */

public class UserInfoBeen {
    //最高额度
    public String amount_limit;
    //卡片中文描述
    public String card_desc_cn;
    //卡片英文描述

    public String getAmount_limit() {
        return amount_limit;
    }

    public void setAmount_limit(String amount_limit) {
        this.amount_limit = amount_limit;
    }

    public String getCard_desc_cn() {
        return card_desc_cn;
    }

    public void setCard_desc_cn(String card_desc_cn) {
        this.card_desc_cn = card_desc_cn;
    }

    public String getCard_desc_en() {
        return card_desc_en;
    }

    public void setCard_desc_en(String card_desc_en) {
        this.card_desc_en = card_desc_en;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String card_desc_en;
    //卡号
    public String card_no;
}

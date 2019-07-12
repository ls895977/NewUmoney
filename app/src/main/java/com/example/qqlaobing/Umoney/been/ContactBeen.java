package com.example.qqlaobing.Umoney.been;

/**
 * Created by zhanglizhi on 2018/7/23.
 */

public class ContactBeen {
    public String name;        //联系人姓名
    public String telPhone;    //电话号码
    public String relation;

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public ContactBeen() {
    }
    public ContactBeen(String name, String telPhone) {
        this.name = name;
        this.telPhone = telPhone;
    }
    public ContactBeen(String name, String telPhone,String relation) {
        this.name = name;
        this.telPhone = telPhone;
        this.relation=relation;
    }

}

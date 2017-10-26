package com.project.community.bean;

import java.io.Serializable;

/**
 * author：fangkai on 2017/10/26 11:23
 * em：617716355@qq.com
 */
public class AddressListBean implements Serializable{


    /**
     * id : f241523201334503b45442b29389e28e
     * consignee : 张三
     * contactPhone : 13666666777
     * userArea : 四川省成都市高新区
     * userStreet : 天府四街
     * address : xxx大厦
     * isDefault : 0
     */

    private String id;
    private String consignee;
    private String contactPhone;
    private String userArea;
    private String userStreet;
    private String address;
    private String isDefault;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getUserArea() {
        return userArea;
    }

    public void setUserArea(String userArea) {
        this.userArea = userArea;
    }

    public String getUserStreet() {
        return userStreet;
    }

    public void setUserStreet(String userStreet) {
        this.userStreet = userStreet;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}

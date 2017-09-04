package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/8/17.
 * 缴费方式
 */

public class PaymentWayModel implements Serializable{
    private static final long serialVersionUID = 6753210234564872868L;
    public String id;
    public String payWay;
    public String icon;
    public int res;

    @Override
    public String toString() {
        return "PaymentWayModel{" +
                "id='" + id + '\'' +
                ", payWay='" + payWay + '\'' +
                ", icon='" + icon + '\'' +
                ", res=" + res +
                '}';
    }
}

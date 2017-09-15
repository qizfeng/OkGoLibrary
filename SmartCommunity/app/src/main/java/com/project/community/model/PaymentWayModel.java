package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/8/17.
 * 缴费方式
 */

public class PaymentWayModel implements Serializable {
    private static final long serialVersionUID = 6753210234564872868L;
    /*"value": "1", #属性值
      "label": "燃气费", #类别名称
      "type": "payment_type",
      "icon":"" #图标地址
      */
    public String value;
    public String label;
    public String icon;
    public int res;
    public String type;

    @Override
    public String toString() {
        return "PaymentWayModel{" +
                "value='" + value + '\'' +
                ", label='" + label + '\'' +
                ", icon='" + icon + '\'' +
                ", res=" + res +
                ", type='" + type + '\'' +
                '}';
    }
}

package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/9/5.
 * 缴费详情历史信息
 */

public class PaymentDetailHistoryModel  implements Serializable{
    private static final long serialVersionUID = 6753210234564872868L;
    /*"payMoney": "60",
      "payStatus": "已缴费",
      "payMouth": "2017年7月账单"*/
    public String payMoney;
    public String payStatus;
    public String payMouth;

    @Override
    public String toString() {
        return "PaymentDetailHistoryModel{" +
                "payMoney='" + payMoney + '\'' +
                ", payStatus='" + payStatus + '\'' +
                ", payMouth='" + payMouth + '\'' +
                '}';
    }
}

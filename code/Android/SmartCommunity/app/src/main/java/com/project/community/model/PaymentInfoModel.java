package com.project.community.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qizfeng on 17/9/5.
 * 缴费信息页面
 */

public class PaymentInfoModel implements Serializable{
    private static final long serialVersionUID = 6753210234564872868L;
    private List<PaymentDetailHistoryModel> paymentList=new ArrayList<>();
    private PaymentDetailModel payment =null;

    public List<PaymentDetailHistoryModel> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<PaymentDetailHistoryModel> paymentList) {
        this.paymentList = paymentList;
    }

    public PaymentDetailModel getPayment() {
        return payment;
    }

    public void setPayment(PaymentDetailModel payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "PaymentInfoModel{" +
                "paymentList=" + paymentList +
                ", payment=" + payment +
                '}';
    }
}

package com.project.community.model;

import com.chad.library.adapter.base.entity.SectionEntity;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/8/21.
 * 缴费历史模型
 */

public class PaymentHistroyModel extends SectionEntity<PaymentHistroyModel> implements Serializable {
    private static final long serialVersionUID = 6753210234564872868L;
    public String id;
    public int type;//1:缴费历史第一项标题展示 2:缴费历史
    public String payNo;
    public String header;
    public boolean isHeader;
    public String icon;
    public int res;

    public PaymentHistroyModel(boolean isHeader, String header, String icon,int res) {
        super(isHeader, header);
        this.isHeader=isHeader;
        this.header=header;
        this.icon=icon;
        this.res=res;
    }
    public PaymentHistroyModel(boolean isHeader, String header, String id, int type, String payNo) {
        super(isHeader, header);
        this.id = id;
        this.type = type;
        this.payNo = payNo;
        this.isHeader=isHeader;
        this.header=header;
    }



    @Override
    public String toString() {
        return "PaymentHistroyModel{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", payNo='" + payNo + '\'' +
                '}';
    }
}

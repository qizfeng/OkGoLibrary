package com.project.community.model;


import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/8/24.
 * 家庭成员
 */

public class FamilyPersonModel implements Serializable, MultiItemEntity {
    private static final long serialVersionUID = -4337711009801627866L;
    public static final int HEADER = 1;
    public static final int CONTENT = 2;
    public int itemType=2;//1家庭成员标识 2家庭成员

    public String id;
    public String realName;//真实姓名
    public String headRelation;//和户主关系
    public String occupation;//职业
    public String sex;//1-男 2-女  value-key
    public String religion;//宗教信仰
    public String nation;//民族
    public String party;//党派
    public String phone;//手机
    public String photo;//头像
    public String idNumber;//身份证
    public String roomAddress;//地址


    @Override
    public int getItemType() {
        return itemType;
    }

    @Override
    public String toString() {
        return "FamilyPersonModel{" +
                "itemType=" + itemType +
                ", id='" + id + '\'' +
                ", realName='" + realName + '\'' +
                ", headRelation='" + headRelation + '\'' +
                ", occupation='" + occupation + '\'' +
                ", sex='" + sex + '\'' +
                ", religion='" + religion + '\'' +
                ", nation='" + nation + '\'' +
                ", party='" + party + '\'' +
                ", phone='" + phone + '\'' +
                ", photo='" + photo + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", roomAddress='" + roomAddress + '\'' +
                '}';
    }
}

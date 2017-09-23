package com.project.community.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qizfeng on 17/9/20.
 * 小区楼栋Model
 */

public class CommunityFamilyModel implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    /* "number": "1808", #房间编号
       "roomNo": "100001",#房屋编号
       "memberList": [] #成员列表*/
    public String number;
    public String roomNo;
    public List<FamilyPersonModel> memberList = new ArrayList<>();

    @Override
    public String toString() {
        return "CommunityFamilyModel{" +
                "number='" + number + '\'' +
                ", roomNo='" + roomNo + '\'' +
                ", memberList=" + memberList +
                '}';
    }
}

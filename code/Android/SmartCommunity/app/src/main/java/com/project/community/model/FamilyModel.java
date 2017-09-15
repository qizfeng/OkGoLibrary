package com.project.community.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qizfeng on 17/8/24.
 * 家庭
 */

public class FamilyModel implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    public String id;
    public String familyName;
    public String auditStatus;
    public HouseModel room =null;
    public List<FamilyPersonModel> memberList = new ArrayList<>();
    @Override
    public String toString() {
        return "FamilyModel{" +
                "id='" + id + '\'' +
                ", familyName='" + familyName + '\'' +
                ", auditStatus=" + auditStatus +
                ", room=" + room +
                ", memberList=" + memberList +
                '}';
    }
}

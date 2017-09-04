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
    public String familyNo;
    public String familyAddr;
    public String familyName;
    public List<FamilyPersonModel> familyPersons = new ArrayList<>();


    @Override
    public String toString() {
        return "FamilyModel{" +
                "id='" + id + '\'' +
                ", familyNo='" + familyNo + '\'' +
                ", familyAddr='" + familyAddr + '\'' +
                ", familyName='" + familyName + '\'' +
                ", familyPersons=" + familyPersons +
                '}';
    }
}

package com.project.community.model;


import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/8/24.
 * 家庭成员
 */

public class FamilyPersonModel implements Serializable, MultiItemEntity {
    private static final long serialVersionUID = -4337711009801627866L;
    public String id;
    public String avatar;
    public int res;
    public String name;
    public String relativeId;
    public String relative;
    public String tag;
    public int tagRes;
    public boolean hasTag;
    public int itemType;//1家庭成员标识 2家庭成员
    public static final int HEADER = 1;
    public static final int CONTENT = 2;


    @Override
    public int getItemType() {
        return itemType;
    }

    @Override
    public String toString() {
        return "FamilyPersonModel{" +
                "id='" + id + '\'' +
                ", avatar='" + avatar + '\'' +
                ", res=" + res +
                ", name='" + name + '\'' +
                ", relativeId='" + relativeId + '\'' +
                ", relative='" + relative + '\'' +
                ", tag='" + tag + '\'' +
                ", tagRes=" + tagRes +
                ", hasTag=" + hasTag +
                ", itemType=" + itemType +
                '}';
    }
}

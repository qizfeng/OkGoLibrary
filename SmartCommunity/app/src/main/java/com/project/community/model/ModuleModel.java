package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/8/9.
 * 首页模块模型
 */

public class ModuleModel implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    public String id;
    public String title;
    public String url;
    public int res;
    public boolean hasRedPoint=false;

    @Override
    public String toString() {
        return "ModuleModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", res=" + res +
                ", hasRedPoint=" + hasRedPoint +
                '}';
    }
}


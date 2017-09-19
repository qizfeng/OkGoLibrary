package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/9/18.
 */

public class UnitModel implements Serializable{
    public String id;
    public String unit;

    @Override
    public String toString() {
        return "UnitModel{" +
                "id='" + id + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }
}

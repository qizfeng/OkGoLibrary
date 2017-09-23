package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/8/29.
 */

public class DictionaryModel implements Serializable{
    private static final long serialVersionUID = -4337711009801627866L;
    public String value;
    public String label;
    public String type;

    public DictionaryModel() {
    }

    public DictionaryModel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "DictionaryModel{" +
                "value='" + value + '\'' +
                ", label='" + label + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

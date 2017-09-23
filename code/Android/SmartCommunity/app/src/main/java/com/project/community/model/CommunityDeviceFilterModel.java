package com.project.community.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zipingfang on 17/9/21.
 */

public class CommunityDeviceFilterModel implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    public List<DictionaryModel> type = new ArrayList<>();

    @Override
    public String toString() {
        return "CommunityDeviceFilterModel{" +
                "type=" + type +
                '}';
    }
}

package com.project.community.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qizfeng on 17/8/29.
 * 數據字典
 */

public class DictionaryResponse {
    public List<DictionaryModel> dictList = new ArrayList<>();

    @Override
    public String toString() {
        return "DictionaryResponse{" +
                "dictList=" + dictList +
                '}';
    }
}

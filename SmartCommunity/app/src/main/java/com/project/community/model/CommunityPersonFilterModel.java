package com.project.community.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zipingfang on 17/9/21.
 */

public class CommunityPersonFilterModel implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    public List<DictionaryModel> range = new ArrayList<>();
    public List<DictionaryModel> tag=new ArrayList<>();
}

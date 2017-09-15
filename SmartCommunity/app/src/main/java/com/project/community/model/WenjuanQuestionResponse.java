package com.project.community.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qizfeng on 17/7/31.
 * 问卷问题response
 */

public class WenjuanQuestionResponse implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    public List<WenjuanQuestionModel> data = new ArrayList<>();

    @Override
    public String toString() {
        return "WenjuanQuestionResponse{" +
                "data=" + data +
                '}';
    }
}

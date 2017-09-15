package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/8/1.
 * 文件答案
 */

public class WenjuanAnswerModel implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    public String answer_id;
    public String answer;

    @Override
    public String toString() {
        return "Answer{" +
                "answer_id='" + answer_id + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}


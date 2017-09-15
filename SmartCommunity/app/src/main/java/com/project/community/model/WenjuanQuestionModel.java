package com.project.community.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qizfeng on 17/7/31.
 * 问卷问题模型
 */

public class WenjuanQuestionModel implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    public String question_id;
    public String question;
    public int type;
    public List<WenjuanAnswerModel> answerList = new ArrayList<>();

    @Override
    public String toString() {
        return "WenjuanQuestionModel{" +
                "question_id='" + question_id + '\'' +
                ", question='" + question + '\'' +
                ", type=" + type +
                ", answerList=" + answerList +
                '}';
    }


}

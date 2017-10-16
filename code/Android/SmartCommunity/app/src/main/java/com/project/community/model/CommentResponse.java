package com.project.community.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qizfeng 17/7/27.
 * 评论response
 */

public class CommentResponse implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    public List<CommentModel> comments = new ArrayList<>();
    public int total;

    @Override
    public String toString() {
        return "CommentResponse{" +
                "comments=" + comments +
                ", total=" + total +
                '}';
    }
}

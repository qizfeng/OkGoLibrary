package com.project.community.Event;

import com.project.community.bean.CommentsListBean;

/**
 * Created by fang on 2017/10/29.
 */

public class DelCommnetEvent {


    private CommentsListBean.CommentsBean commentsBean;

    public DelCommnetEvent(CommentsListBean.CommentsBean commentsBean) {

        this.commentsBean = commentsBean;
    }

    public CommentsListBean.CommentsBean getCommentsBean() {
        return commentsBean;
    }

    public void setCommentsBean(CommentsListBean.CommentsBean commentsBean) {
        this.commentsBean = commentsBean;
    }
}

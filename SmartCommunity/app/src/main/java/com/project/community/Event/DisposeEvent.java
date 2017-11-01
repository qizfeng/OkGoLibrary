package com.project.community.Event;

/**
 * author：fangkai on 2017/11/1 17:08
 * em：617716355@qq.com
 */
public class DisposeEvent {

    private String id;

    public DisposeEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

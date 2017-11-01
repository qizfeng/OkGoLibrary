package com.project.community.Event;

/**
 * author：fangkai on 2017/11/1 16:47
 * em：617716355@qq.com
 */
public class RepairsTypeEvent {
    private int type;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RepairsTypeEvent(int type,String id) {
        this.type = type;
        this.id = id;

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

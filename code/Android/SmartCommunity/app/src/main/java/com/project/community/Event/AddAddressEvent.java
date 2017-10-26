package com.project.community.Event;

/**
 * author：fangkai on 2017/10/26 11:00
 * em：617716355@qq.com
 */
public class AddAddressEvent {
    public boolean add;

    public AddAddressEvent(boolean add) {
        this.add = add;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }
}

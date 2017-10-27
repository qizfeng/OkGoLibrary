package com.project.community.Event;

/**
 * author：fangkai on 2017/10/27 09:39
 * em：617716355@qq.com
 */
public class AddHouseEvent {
    private  String item;

    public  AddHouseEvent(String item){
        this.item=item;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}

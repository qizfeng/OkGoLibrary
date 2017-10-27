package com.project.community.Event;

/**
 * author：cj on 2017/10/27 09:35
 */
public class AddGoodsEvent {

    private  String item;

    public AddGoodsEvent(String item){
        this.item=item;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}

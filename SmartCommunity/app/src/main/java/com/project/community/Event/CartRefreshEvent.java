package com.project.community.Event;

/**
 * author：cj on 2017/10/27 09:35
 */
public class CartRefreshEvent {

    private  String item;

    public CartRefreshEvent(String item){
        this.item=item;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}

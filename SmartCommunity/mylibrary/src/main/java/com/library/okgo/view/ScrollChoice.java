package com.library.okgo.view;

import android.content.Context;
import android.util.AttributeSet;

import java.util.List;

/**
 * Created by qizfeng on 17/8/7.
 */

public class ScrollChoice extends WheelPicker {

    private OnItemSelectedListener onItemSelectedListener;

    WheelPicker.Adapter adapter;
    private int defaultIndex;

    public ScrollChoice(Context context) {
        this(context, null);
    }

    public ScrollChoice(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.adapter = new Adapter();
        setAdapter(adapter);
    }

    @Override
    protected void onItemSelected(int position, Object item) {
        if (null != onItemSelectedListener) {
            final String itemText = (String) item;
            onItemSelectedListener.onItemSelected(this, position, itemText);
        }
    }

    @Override
    protected void onItemCurrentScroll(int position, Object item) {
    }

    @Override
    public int getDefaultItemPosition() {
        return defaultIndex;
    }

    public void addItems(List<String> data, int defaultIndex) {
        this.defaultIndex = defaultIndex;
        adapter.setData(data);
        updateDefaultItem();
    }

    public Object getItem(int position){
        return adapter.getItem(position);
    }


    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    private void updateDefaultItem() {
        setSelectedItemPosition(defaultIndex);
    }

    public int getDefaultItemIndex() {
        return defaultIndex;
    }

    public String getCurrentSelection() {
        return adapter.getItemText(getCurrentItemPosition());
    }

    public interface OnItemSelectedListener {
        void onItemSelected(ScrollChoice scrollChoice, int position, String name);
    }
}

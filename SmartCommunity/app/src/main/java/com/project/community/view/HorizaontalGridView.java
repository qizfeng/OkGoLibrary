package com.project.community.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by qizfeng on 17/7/11.
 */

public class HorizaontalGridView extends GridView {

    public HorizaontalGridView(Context context) {
        super(context);
    }

    public HorizaontalGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizaontalGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO 自动生成的构造函数存根
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO 自动生成的方法存根
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

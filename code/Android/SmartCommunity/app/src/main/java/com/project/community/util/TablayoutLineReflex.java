package com.project.community.util;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.library.okgo.utils.DeviceUtil;

import java.lang.reflect.Field;

/**
 * Created by qizfeng on 17/8/21.
 * tablayout下划线反射
 */

public class TablayoutLineReflex {
    public static void setIndicator(Context context,TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        // 这里是为了通过 TextView 的 Paint 来测量文字所占的宽度
        TextView tv = new TextView(context);
        // 必须设置和tab文字一样的大小，因为不同大小字所占宽度不同
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);

            // 当前TAB上的文字
            String str = tabs.getTabAt(i).getText().toString();
            // 所占的宽度
            int width = (int) tv.getPaint().measureText(str);
            // 这里设置宽度，要稍微多一点，否则丑死了！
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width+20, LinearLayout.LayoutParams.MATCH_PARENT);
            // params.leftMargin = left;//莫名的会卡顿
            // params.rightMargin = right;//莫名的会卡顿
            child.setLayoutParams(params);
            child.invalidate();
        }
    }


    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }


    }
}

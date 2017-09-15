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
import com.library.okgo.utils.LogUtils;

import java.lang.reflect.Field;

/**
 * Created by qizfeng on 17/8/21.
 * tablayout下划线反射
 */

public class TablayoutLineReflex {
    public static void setIndicator(Context context, TabLayout tabs, int leftDip, int rightDip) {
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
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);

            // 当前TAB上的文字
            String str = tabs.getTabAt(i).getText().toString();
            // 所占的宽度
            int width = (int) tv.getPaint().measureText(str);
            // 这里设置宽度，要稍微多一点，否则丑死了！
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width + 20, LinearLayout.LayoutParams.MATCH_PARENT);
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

    public static void setTabLine(Context context, TabLayout tab, int left, int right) {
        try {
            Class<?> tabLayout = tab.getClass();
            Field tabStrip = tabLayout.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
            LinearLayout ll_tab = (LinearLayout) tabStrip.get(tab);
            for (int i = 0; i < ll_tab.getChildCount(); i++) {
                View child = ll_tab.getChildAt(i);
                child.setPadding(0, 0, 0, 0);

                //拿到tabView的mTextView属性
                Field mTextViewField = child.getClass().getDeclaredField("mTextView");
                mTextViewField.setAccessible(true);

                TextView mTextView = (TextView) mTextViewField.get(child);
                mTextView.setTextSize(18);
                child.setPadding(0, 0, 0, 0);
                //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                int width = 0;
                width = mTextView.getWidth();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                //修改两个tab的间距
//                params.setMarginStart(DeviceUtil.dipToPx(context,left));
                params.setMargins((int) DeviceUtil.dipToPx(context, left), 0, (int) DeviceUtil.dipToPx(context, right), 0);
//                params.setMarginEnd(DeviceUtil.dipToPx(context,right));
                child.setLayoutParams(params);
                child.invalidate();
            }

//            try {
//                Class<?> tabLayout = tab.getClass();
//                //拿到tabLayout的mTabStrip属性
//                Field mTabStripField = tabLayout.getDeclaredField("mTabStrip");
//                mTabStripField.setAccessible(true);
//
//                LinearLayout mTabStrip = (LinearLayout) mTabStripField.get(tab);
//
//                int dp10 = (int) DeviceUtil.dipToPx(context, 70);
//
//                for (int i = 0; i < mTabStrip.getChildCount(); i++) {
//                    View tabView = mTabStrip.getChildAt(i);
//
//                    //拿到tabView的mTextView属性
//                    Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
//                    mTextViewField.setAccessible(true);
//
//                    TextView mTextView = (TextView) mTextViewField.get(tabView);
//                    mTextView.setTextSize(18);
//                    tabView.setPadding(0, 0, 0, 0);
//                    //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
//                    int width = 0;
//                    width = mTextView.getWidth();
//                    LogUtils.e("width:" + width);
//                    if (width == 0) {
//                        mTextView.measure(0, 0);
//                        width = mTextView.getMeasuredWidth();
//                    }
//                    LogUtils.e("padding:"+tabView.getPaddingLeft());
//                    //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
////                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
//                    params.width = width;
//                    params.leftMargin = dp10;
//                    params.rightMargin = dp10;
//                    tabView.setLayoutParams(params);
//                    LogUtils.e("tabview;"+tabView.getLayoutParams().width);
//                    tabView.invalidate();
//                }
//
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

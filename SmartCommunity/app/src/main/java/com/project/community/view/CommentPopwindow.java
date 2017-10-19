package com.project.community.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.library.okgo.utils.KeyBoardUtils;
import com.project.community.R;

/**
 * Created by zipingfang on 17/9/12.
 */

public class CommentPopwindow extends PopupWindow {

    private Context mContext;
    private View view;
    public EditText et_comment;
    public RecyclerView lv_container;
    public Button btn_send;

    public CommentPopwindow(final Context mContext, View.OnTouchListener itemsOnClick) {

        this.view = LayoutInflater.from(mContext).inflate(R.layout.layout_popupwindow_comment, null);
        setContentView(view);
        view.findViewById(R.id.pop_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyBoardUtils.closeKeybord(et_comment, mContext);
                dismiss();
            }
        });

        btn_send = (Button) view.findViewById(R.id.btn_send);
        et_comment = (EditText) view.findViewById(R.id.et_input);

        //初始化listview，加载数据。
        lv_container = (RecyclerView) view.findViewById(R.id.lv_container);
        lv_container.setLayoutManager(new LinearLayoutManager(mContext));
        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(itemsOnClick);
    /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.popwin_comment_anim);

    }
}

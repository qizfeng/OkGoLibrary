package com.project.community.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.library.okgo.utils.KeyBoardUtils;
import com.project.community.R;

/**
 * Created by qizfeng on 17/7/21.
 */

public class CommentPopWin extends PopupWindow {

    private Context mContext;
    private View view;
    public EditText et_comment;
    public ListView lv_container;
    public Button btn_send;

    public CommentPopWin(final Context mContext, View.OnClickListener itemsOnClick) {

        this.view = LayoutInflater.from(mContext).inflate(R.layout.layout_popup_comment, null);
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
        // KeyBoardUtils.openKeybord(et_comment,mContext);

        //初始化listview，加载数据。
        lv_container = (ListView) view.findViewById(R.id.lv_container);
        lv_container.setItemsCanFocus(false);
        lv_container.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

//                int height = view.findViewById(R.id.pop_layout).getTop();
//                int y = (int) event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < height) {
                KeyBoardUtils.closeKeybord(et_comment, mContext);
                dismiss();
//                    }
//                }
                return true;
            }
        });


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

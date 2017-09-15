package com.project.community.ui.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.R;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.WenjuanAnswerModel;
import com.project.community.model.WenjuanQuestionModel;

import java.util.Iterator;
import java.util.List;

/**
 * Created by qizfeng on 17/8/1.
 * 填写问卷适配器
 */

public class WenjuanDetailAdapter extends BaseQuickAdapter<WenjuanQuestionModel, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;//点击事件

    public WenjuanDetailAdapter(List<WenjuanQuestionModel> data, RecycleItemClickListener itemClick) {
        super(R.layout.layout_item_wenjuan_detail, data);
        itemClickListener = itemClick;

    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final WenjuanQuestionModel model) {
        final int position = baseViewHolder.getLayoutPosition();
        baseViewHolder.setText(R.id.tv_question, model.question);
        LinearLayout layout = baseViewHolder.getView(R.id.layout_answer);
        List<WenjuanAnswerModel> answers = model.answerList;
        if (model.type == 1) {//单选类型
            RadioGroup radioGroup = new RadioGroup(mContext);
            radioGroup.setOrientation(RadioGroup.VERTICAL);
            Iterator<WenjuanAnswerModel> it = answers.iterator();
            while (it.hasNext()) {
                WenjuanAnswerModel answer = it.next();
                RadioButton radioButton = new RadioButton(mContext);
                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(10, 10, 10, 10);
                radioButton.setLayoutParams(layoutParams);
                radioButton.setText(answer.answer);
                radioButton.setTextSize(15);
                //rb.setGravity(Gravity.CENTER_VERTICAL);
                radioButton.setPadding(10, 10, 10, 10);
                radioGroup.addView(radioButton);//将单选按钮添加到RadioGroup中
            }
            layout.addView(radioGroup);
        } else if (model.type == 2) {//多选类型
            Iterator<WenjuanAnswerModel> it = answers.iterator();
            while (it.hasNext()) {
                WenjuanAnswerModel answer = it.next();
                CheckBox checkBox =(CheckBox) LayoutInflater.from(mContext).inflate(R.layout.layout_item_wenjuan_multiple, null).findViewById(R.id.checkbox);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(10, 10, 10, 10);
                checkBox.setLayoutParams(layoutParams);
                checkBox.setText(answer.answer);
                checkBox.setPadding(10, 10, 10, 10);
                checkBox.setTextSize(15);
                layout.addView(checkBox);
            }
        }
        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, position);
            }
        });
    }

}

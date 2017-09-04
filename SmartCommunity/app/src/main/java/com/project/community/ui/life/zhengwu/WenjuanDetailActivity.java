package com.project.community.ui.life.zhengwu;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.WenjuanAnswerModel;
import com.project.community.model.WenjuanQuestionModel;
import com.project.community.ui.adapter.WenjuanDetailAdapter;
import com.project.community.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by qizfeng on 17/7/31.
 * 问卷调查详情
 */

public class WenjuanDetailActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_submit)
    TextView mTvSubmit;
    private List<WenjuanQuestionModel> mData = new ArrayList<>();
    private WenjuanDetailAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wenjuan_detail);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.activity_write_wenjuan),R.mipmap.iv_back);
        initData();
    }

    private void initData(){
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mData = new ArrayList<>();
        WenjuanQuestionModel model1 = new WenjuanQuestionModel();
        model1.type=1;
        model1.question="您觉得智慧社区APP容易上手吗?";
        WenjuanAnswerModel answer1 =new WenjuanAnswerModel();
        answer1.answer="非常容易上手";
        WenjuanAnswerModel answer2 =new WenjuanAnswerModel();
        answer2.answer="容易上手";
        WenjuanAnswerModel answer3 =new WenjuanAnswerModel();
        answer3.answer="不容易上手";
        WenjuanAnswerModel answer4 =new WenjuanAnswerModel();
        answer4.answer="上手困难";
        model1.answerList.add(answer1);
        model1.answerList.add(answer2);
        model1.answerList.add(answer3);
        model1.answerList.add(answer4);

        WenjuanQuestionModel model2 = new WenjuanQuestionModel();
        model2.type=2;
        model2.question="您喜欢智慧社区的哪些功能";
        WenjuanAnswerModel answer5 =new WenjuanAnswerModel();
        answer5.answer="政务";
        WenjuanAnswerModel answer6 =new WenjuanAnswerModel();
        answer6.answer="物业";
        WenjuanAnswerModel answer7 =new WenjuanAnswerModel();
        answer7.answer="民生";
        WenjuanAnswerModel answer8 =new WenjuanAnswerModel();
        answer8.answer="社区";
        model2.answerList.add(answer5);
        model2.answerList.add(answer6);
        model2.answerList.add(answer7);
        model2.answerList.add(answer8);
        mData.add(model1);
        mData.add(model2);

        mAdapter = new WenjuanDetailAdapter(mData, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onCustomClick(View view, int position) {

            }
        });

        SpacesItemDecoration decoration = new SpacesItemDecoration(2,false);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mAdapter);

    }
}

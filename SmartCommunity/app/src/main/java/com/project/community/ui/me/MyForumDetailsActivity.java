package com.project.community.ui.me;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author：fangkai on 2017/10/24 14:22
 * em：617716355@qq.com
 */
public class MyForumDetailsActivity extends BaseActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.bbs_item_head)
    ImageView bbsItemHead;
    @Bind(R.id.bbs_item_name)
    TextView bbsItemName;
    @Bind(R.id.bbs_item_time)
    TextView bbsItemTime;
    @Bind(R.id.bbs_item_comment)
    TextView bbsItemComment;
    @Bind(R.id.bbs_item_content)
    TextView bbsItemContent;
    @Bind(R.id.bbs_item_big_img)
    ImageView bbsItemBigImg;
    @Bind(R.id.bbs_item_ll_two_img_1)
    ImageView bbsItemLlTwoImg1;
    @Bind(R.id.bbs_item_ll_two_img_2)
    ImageView bbsItemLlTwoImg2;
    @Bind(R.id.bbs_item_ll_two_img)
    LinearLayout bbsItemLlTwoImg;
    @Bind(R.id.bbs_item_ll_three_img_1)
    ImageView bbsItemLlThreeImg1;
    @Bind(R.id.bbs_item_ll_three_img_2)
    ImageView bbsItemLlThreeImg2;
    @Bind(R.id.bbs_item_ll_three_img_3)
    ImageView bbsItemLlThreeImg3;
    @Bind(R.id.bbs_item_ll_three_img)
    LinearLayout bbsItemLlThreeImg;
    @Bind(R.id.tv_delete)
    TextView tvDelete;
    @Bind(R.id.tv_anew)
    TextView tvAnew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_forum_details);
        ButterKnife.bind(this);
        setTitles();
    }

    private void setTitles() {
        initToolBar(toolbar, tvTitle, true, "信息详情", R.mipmap.iv_back);


    }

    @OnClick({R.id.tv_delete, R.id.tv_anew})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_delete:
                finish();
                break;
            case R.id.tv_anew:
                finish();
                break;
        }
    }
}

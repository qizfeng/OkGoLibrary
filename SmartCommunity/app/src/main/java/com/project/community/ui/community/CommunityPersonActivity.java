package com.project.community.ui.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by qizfeng on 17/9/18.
 * 人员信息
 */

public class CommunityPersonActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;

    public static void startActivity(Context context,Bundle bundle){
        Intent intent = new Intent(context,CommunityPersonActivity.class);
        intent.putExtra("bundle",bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_person);
        initToolBar(mToolBar,mTvTitle,true,getString(R.string.txt_map_person_info),R.mipmap.iv_back);
    }
}

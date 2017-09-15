package com.project.community.ui.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.constants.SharedPreferenceUtils;

import butterknife.Bind;

/**
 * Created by qizfeng on 17/8/9.
 * 设置
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_logout)
    TextView mTvLogout;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.activity_setting), R.mipmap.iv_back);
        mTvLogout.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_logout:
                logout();
                break;
        }
    }

    /**
     * 退出登录
     */
    private void logout() {
        new AlertDialog.Builder(this).setTitle("确认退出吗？")
                .setIcon(android.R.drawable.ic_menu_info_details)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                        //设置缓存为未登录
                        SharedPreferenceUtils.putBoolean(SettingActivity.this, SharedPreferenceUtils.SP_LOGIN, false);
//                        LoginActivity.startActivity(SettingActivity.this);
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                }).show();

    }
}

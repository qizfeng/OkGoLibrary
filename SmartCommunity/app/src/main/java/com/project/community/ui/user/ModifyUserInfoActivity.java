package com.project.community.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.project.community.R;
import com.project.community.base.BaseActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/8/26.
 * 修改个人信息
 */

public class ModifyUserInfoActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.et_content)
    EditText mEtContent;
    @Bind(R.id.btn_save)
    Button mBtnSave;

    private String title;
    private int type = 1;//1修改昵称 2房屋编号 3职业
    private String content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_userinfo);
        Intent intent = getIntent();
        if (intent != null) {
            title = intent.getStringExtra("title");
            type = intent.getIntExtra("type", 1);
            content = intent.getStringExtra("content");
            initToolBar(mToolBar, mTvTitle, true, title, R.mipmap.iv_back);
            mEtContent.setText(content);
            if (type == 1) {
                mEtContent.setHint(getString(R.string.txt_nickname_hint));
            } else if (type == 2) {
                mEtContent.setHint(getString(R.string.txt_houseno_hint));
            } else if (type == 3) {
                mEtContent.setHint(getString(R.string.txt_position_hint));
            }
            mEtContent.setSelection(content.length());
        }
    }

    @OnClick(R.id.btn_save)
    public void onSave(View v) {
        try {
            String content = mEtContent.getText().toString();
            if (TextUtils.isEmpty(content)) {
                if (type == 1) {
                    showToast(getString(R.string.txt_nickname_hint));
                } else if (type == 2) {
                    showToast(getString(R.string.txt_houseno_hint));
                } else if (type == 3) {
                    showToast(getString(R.string.txt_position_hint));
                }
                return;
            }
            if(type==1){
                doEditUserInfo(getUser(this).id,"",content,"","","","","","","","",content);
            }else if(type==2){
                doEditUserInfo(getUser(this).id,"","","","",content,"","","","","",content);
            }else if(type==3){
                doEditUserInfo(getUser(this).id,"","","","","","",content,"","","",content);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改个人信息
     *
     * @param id
     * @param photo
     * @param loginName
     * @param name
     * @param idCard
     * @param roomNo
     * @param isOwner
     * @param occupation
     * @param nation
     * @param religion
     * @param party
     */
    private void doEditUserInfo(String id, String photo, String loginName, String name, String idCard,
                                String roomNo, String isOwner, String occupation, String nation,
                                String religion, String party,final String content) {
        serverDao.doEditUserInfo(id, photo, loginName, name, idCard, roomNo, isOwner, occupation, nation, religion, party,
                new JsonCallback<BaseResponse<List>>() {
                    @Override
                    public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                        showToast(baseResponse.message);
                        Intent intent = new Intent();
                        intent.putExtra("result", content);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        if (!e.getMessage().contains("No address"))
                            showToast(e.getMessage());
                    }
                });
    }


}

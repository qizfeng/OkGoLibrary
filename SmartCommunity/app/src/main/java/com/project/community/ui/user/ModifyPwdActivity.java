package com.project.community.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.okgo.callback.DialogCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.ValidateUtil;
import com.project.community.R;
import com.project.community.base.BaseActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/8/26.
 * 需改密码
 */

public class ModifyPwdActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.et_old_pwd)
    EditText mEtOldPwd;
    @Bind(R.id.et_new_pwd)
    EditText mEtNewPwd;
    @Bind(R.id.btn_save)
    Button mBtnSave;
    @Bind(R.id.iv_eyes)
    ImageView mIvEyes;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ModifyPwdActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.activity_modify_pwd), R.mipmap.iv_back);
    }

    @OnClick(R.id.btn_save)
    public void onSave(View view) {
        String oldPwd = mEtOldPwd.getText().toString();
        final String newPwd = mEtNewPwd.getText().toString();
        if (TextUtils.isEmpty(oldPwd)) {
            showToast(getString(R.string.txt_ori_pwd_hint));
            return;
        }

        if (TextUtils.isEmpty(newPwd)) {
            showToast(getString(R.string.toast_empty_new_pwd));
            return;
        }

        if (!ValidateUtil.isPwd(newPwd)) {
            showToast(getString(R.string.toast_error_new_pwd));
            return;
        }

        serverDao.doModifyPwd(getUser(this).id, oldPwd, newPwd, newPwd, new DialogCallback<BaseResponse<List>>(this) {
            @Override
            public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                saveUserPwd(ModifyPwdActivity.this,newPwd);
                showToast(baseResponse.message);
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
            }
        });
    }

    @OnClick(R.id.iv_eyes)
    public void onShowPwd(View view) {
        if (mEtNewPwd.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
            //隐藏密码
            mEtNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mIvEyes.setImageResource(R.mipmap.b1_btn1);
            mEtNewPwd.setSelection(mEtNewPwd.getText().toString().length());
        } else if (mEtNewPwd.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
            //显示密码
            mEtNewPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mIvEyes.setImageResource(R.mipmap.b1_btn1_p);
            mEtNewPwd.setSelection(mEtNewPwd.getText().toString().length());

        }
    }
}

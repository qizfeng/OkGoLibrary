package com.project.community.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.platform.comapi.map.K;
import com.library.okgo.callback.DialogCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.KeyBoardUtils;
import com.library.okgo.utils.LogUtils;
import com.library.okgo.utils.ValidateUtil;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.util.CountDownTimerUtils;

import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/8/9.
 * 忘记密码
 */

public class ForgetPassActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.btn_verify)
    Button mBtnVerify;
    @Bind(R.id.et_phone)
    EditText mEtPhone;
    @Bind(R.id.et_verify)
    EditText mEtVerify;
    @Bind(R.id.et_pwd)
    EditText mEtPwd;
    @Bind(R.id.iv_eyes)
    ImageView mIvEyes;
    @Bind(R.id.btn_confirm)
    Button mBtnConfirm;
    @Bind(R.id.layout_root)
    LinearLayout mLayoutRoot;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ForgetPassActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.activity_forget), R.mipmap.iv_back);
        mBtnVerify.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
        mIvEyes.setOnClickListener(this);
        mLayoutRoot.setOnTouchListener(this);
        KeyBoardUtils.closeKeybord(mEtPhone, this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (KeyBoardUtils.isSHowKeyboard(this, view)) {
            KeyBoardUtils.closeKeybord(mEtPhone, this);
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_verify:
//                CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(mBtnVerify, 60000, 1000);
//                mCountDownTimerUtils.start();
                getCode();
                break;
            case R.id.btn_confirm:
                doModify();
                break;
            case R.id.iv_eyes:
                if (mEtPwd.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                    //隐藏密码
                    mEtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mIvEyes.setImageResource(R.mipmap.b1_btn1);
                } else if (mEtPwd.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    //显示密码
                    mEtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mIvEyes.setImageResource(R.mipmap.b1_btn1_p);

                }
                break;
        }
    }


    /**
     * 获取验证码
     */
    private void getCode() {
        String phone = mEtPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            showToast(getString(R.string.toast_empty_phone));
            return;
        }
        if (!ValidateUtil.isPhone(phone)) {
            showToast(getString(R.string.toast_error_phone));
            return;
        }
        serverDao.getCode(phone, new DialogCallback<BaseResponse<List>>(this) {
            @Override
            public void onSuccess(BaseResponse<List> stringBaseResponse, Call call, Response response) {
                showToast(stringBaseResponse.message);
                CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(mBtnVerify, 60000, 1000);
                mCountDownTimerUtils.start();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });
    }


    /**
     * 注册
     */
    private void doModify() {
        String phone = mEtPhone.getText().toString();
        String code = mEtVerify.getText().toString();
        String pwd = mEtPwd.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            showToast(getString(R.string.toast_empty_phone));
            return;
        }
        if (!ValidateUtil.isPhone(phone)) {
            showToast(getString(R.string.toast_error_phone));
            return;
        }
        if (TextUtils.isEmpty(code)) {
            showToast(getString(R.string.toast_empty_code));
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            showToast(getString(R.string.toast_empty_pwd));
            return;
        }

        if (!ValidateUtil.isPwd(pwd)){
            showToast(getString(R.string.toast_error_pwd));
            return;
        }

        serverDao.doForgetPass(phone, code, pwd, pwd, new DialogCallback<BaseResponse<List>>(this) {
            @Override
            public void onSuccess(BaseResponse<List> stringBaseResponse, Call call, Response response) {
                LogUtils.d("onsuccess:" + stringBaseResponse);
                showToast(stringBaseResponse.message);
                LoginActivity.startActivity(ForgetPassActivity.this);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            KeyBoardUtils.closeKeybord(mEtPhone,this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

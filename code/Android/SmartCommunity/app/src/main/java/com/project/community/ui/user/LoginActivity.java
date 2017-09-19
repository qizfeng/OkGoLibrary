package com.project.community.ui.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.library.okgo.callback.DialogCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.KeyBoardUtils;
import com.library.okgo.utils.LogUtils;
import com.library.okgo.utils.SimpleJson;
import com.library.okgo.utils.ToastUtils;
import com.library.okgo.utils.ValidateUtil;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.model.UserModel;
import com.project.community.ui.MainActivity;
import com.project.community.util.NetworkUtils;

import org.json.JSONObject;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/7/19.
 * 登录页面
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener, CompoundButton.OnCheckedChangeListener {
    @Bind(R.id.et_phone)
    EditText mEtPhone;
    @Bind(R.id.et_pwd)
    EditText mEtPwd;
    @Bind(R.id.iv_eyes)
    ImageView mIvEyes;
    @Bind(R.id.layout_root)
    LinearLayout mLayoutRoot;
    @Bind(R.id.scrollView)
    ScrollView mScrollView;
    @Bind(R.id.iv_wechat)
    ImageView mIvWechat;
    @Bind(R.id.iv_qq)
    ImageView mIvQQ;
    @Bind(R.id.iv_alipay)
    ImageView mIvAlipay;
    @Bind(R.id.btn_login)
    Button mBtnLogin;
    @Bind(R.id.cb_remember)
    CheckBox mCbRemember;
    @Bind(R.id.tv_forget)
    TextView mTvForgert;
    @Bind(R.id.tv_guest)
    TextView mTvGuest;
    @Bind(R.id.tv_register)
    TextView mTvRegister;
    @Bind(R.id.tv_phone)
    TextView mTvPhone;
    @Bind(R.id.tv_pwd)
    TextView mTvPwd;
    private String phone;
    private String password;
    private boolean isRemember = false;

    public static Activity instance;

    public static Activity getInstance() {
        return instance;
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        instance = this;
        mIvEyes.setOnClickListener(this);
        mLayoutRoot.setOnClickListener(this);
        mIvWechat.setOnClickListener(this);
        mIvQQ.setOnClickListener(this);
        mIvAlipay.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mTvForgert.setOnClickListener(this);
        mTvGuest.setOnClickListener(this);
        mTvRegister.setOnClickListener(this);
        mScrollView.setOnTouchListener(this);
        mCbRemember.setOnCheckedChangeListener(this);
        mTvPhone.setOnClickListener(this);
        mTvPwd.setOnClickListener(this);
        isRemember = getIsRemember(this);
        phone = getUsername(this);
        password = getUserPwd(this);
        if (!TextUtils.isEmpty(phone))
            mEtPhone.setText(phone);
        if (isRemember) {
            mEtPwd.setText(password);
            mCbRemember.setChecked(true);
        }
        KeyBoardUtils.closeKeybord(mEtPhone, this);
        KeyBoardUtils.closeKeybord(mEtPwd, this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (KeyBoardUtils.isSHowKeyboard(LoginActivity.this, view)) {
            KeyBoardUtils.closeKeybord(mEtPhone, LoginActivity.this);
            KeyBoardUtils.closeKeybord(mEtPwd, LoginActivity.this);
        }
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
            case R.id.layout_root:
                if (KeyBoardUtils.isSHowKeyboard(LoginActivity.this, view)) {
                    KeyBoardUtils.closeKeybord(mEtPhone, LoginActivity.this);
                    KeyBoardUtils.closeKeybord(mEtPwd, LoginActivity.this);
                }
                break;
            case R.id.btn_login:
                doLogin();
                break;
            case R.id.tv_register:
                RegisterActivity.startActivity(LoginActivity.this);
                break;
            case R.id.tv_guest:
                MainActivity.startActivity(LoginActivity.this);
                break;
            case R.id.tv_forget:
                ForgetPassActivity.startActivity(LoginActivity.this);
                break;
            case R.id.tv_phone:
                mEtPhone.requestFocus();
                KeyBoardUtils.openKeybord(mEtPhone,LoginActivity.this);
                break;
            case R.id.tv_pwd:
                mEtPwd.requestFocus();
                KeyBoardUtils.openKeybord(mEtPwd,LoginActivity.this);
                break;
        }
    }

    /**
     * 登录
     */
    private void doLogin() {
        final String phone = mEtPhone.getText().toString();
        final String pwd = mEtPwd.getText().toString();
        if (TextUtils.isEmpty(phone) || !ValidateUtil.isPhone(phone)) {
            showToast(getString(R.string.toast_error_phone));
            return;
        }


        if (TextUtils.isEmpty(pwd)) {
            showToast(getString(R.string.toast_empty_pwd));
            return;
        }
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showToast(getString(R.string.network_error));
            return;
        }
        serverDao.doLogin(phone, pwd, new DialogCallback<BaseResponse<UserModel>>(this) {
            @Override
            public void onSuccess(BaseResponse<UserModel> userModelBaseResponse, Call call, Response response) {
                saveUsername(LoginActivity.this, phone);
                Gson gson = new Gson();
                String userStr = gson.toJson(userModelBaseResponse.retData);
                // JSONObject object = JSONObject.fromObject(userModelBaseResponse.retData);
                saveUser(LoginActivity.this, userStr);
                saveLoginStatus(LoginActivity.this, true);
                if (mCbRemember.isChecked()) {
                    saveUserPwd(LoginActivity.this, pwd);
                    saveIsRemember(LoginActivity.this, true);
                } else {
                    saveUserPwd(LoginActivity.this, "");
                    saveIsRemember(LoginActivity.this, false);
                }
                saveWillPlayAnim(LoginActivity.this, true);
                showToast(userModelBaseResponse.message);
                KeyBoardUtils.closeKeybord(mEtPhone, LoginActivity.this);
//                MainActivity.startActivity(LoginActivity.this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyBoardUtils.closeKeybord(mEtPhone, this);
    }
}

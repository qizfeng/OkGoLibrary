package com.project.community.ui.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.library.okgo.callback.DialogCallback;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.KeyBoardUtils;
import com.library.okgo.utils.LogUtils;
import com.library.okgo.utils.SimpleJson;
import com.library.okgo.utils.ValidateUtil;
import com.library.okgo.view.loopview.LoopView;
import com.library.okgo.view.loopview.OnItemSelectedListener;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.constants.SharedPreferenceUtils;
import com.project.community.model.DistModel;
import com.project.community.model.UserModel;
import com.project.community.ui.MainActivity;
import com.project.community.ui.WebViewActivity;
import com.project.community.util.CountDownTimerUtils;
import com.project.community.util.NetworkUtils;
import com.project.community.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/7/18.
 * 注册
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.btn_verify)
    Button mBtnVerify;
    @Bind(R.id.et_username)
    EditText mEtUsername;
    @Bind(R.id.et_idcard_no)
    EditText mEtIdCardNo;
    @Bind(R.id.et_phone)
    EditText mEtPhone;
    @Bind(R.id.et_verify)
    EditText mEtVerify;
    @Bind(R.id.et_pwd)
    EditText mEtPwd;
    @Bind(R.id.et_repwd)
    EditText mEtRepwd;
    @Bind(R.id.layout_service)
    LinearLayout mLayoutService;
    @Bind(R.id.iv_eyes)
    ImageView mIvEyes;
    @Bind(R.id.iv_re_eyes)
    ImageView mIvReEyes;
    @Bind(R.id.btn_register)
    Button mBtnRegister;
    @Bind(R.id.layout_root)
    LinearLayout mLayoutRoot;
    @Bind(R.id.layout_choise_disc)
    RelativeLayout mLayoutChoiseDisc;
    @Bind(R.id.tv_disc)
    TextView mTvDisc;
    private PopupWindow mPopupWindow;
    private LoopView mLoopView;
    private List<DistModel> distModels = new ArrayList<>();
    private String distId;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.activity_register), R.mipmap.iv_back);
        mBtnVerify.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        mIvEyes.setOnClickListener(this);
        mLayoutService.setOnClickListener(this);
        mTvDisc.setOnClickListener(this);
        mLayoutRoot.setOnTouchListener(this);
        mIvReEyes.setOnClickListener(this);
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
//CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(mBtnVerify, 60000, 1000);
//mCountDownTimerUtils.start();
                getCode();
                break;
            case R.id.btn_register:
                doRegister();
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
            case R.id.iv_re_eyes:
                if (mEtRepwd.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                    //隐藏密码
                    mEtRepwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mIvReEyes.setImageResource(R.mipmap.b1_btn1);
                } else if (mEtRepwd.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    //显示密码
                    mEtRepwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mIvReEyes.setImageResource(R.mipmap.b1_btn1_p);

                }
                break;
            case R.id.layout_service:
                Intent intent = new Intent(RegisterActivity.this, AgreementWebViewActivity.class);
                //Bundle bundle = new Bundle();
                //bundle.putString("url", "https://zhuanlan.zhihu.com/p/26409511");
                //bundle.putString("title", getString(R.string.register_service_title));
                //intent.putExtra("bundle", bundle);
                startActivity(intent);
                break;
            case R.id.tv_disc:
                LogUtils.e("======");
                getDist("选择小区");
                break;
            case R.id.tv_cancel:
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                break;
            case R.id.tv_confirm:
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                    mTvDisc.setText(mLoopView.getCurrentItem().toString());
                    distId = distModels.get(mLoopView.getSelectedItem()).id;
                }
                break;
        }
    }


    /**
     * 获取验证码
     */
    private void getCode() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showToast(getString(R.string.network_error));
            return;
        }
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
    private void doRegister() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showToast(getString(R.string.network_error));
            return;
        }
        String username = mEtUsername.getText().toString();
        String idcardNo = mEtIdCardNo.getText().toString();
        final String phone = mEtPhone.getText().toString();
        String code = mEtVerify.getText().toString();
        String pwd = mEtPwd.getText().toString();
        String repwd = mEtRepwd.getText().toString();
        if (TextUtils.isEmpty(username)) {
            showToast(getString(R.string.toast_empty_name));
            return;
        }
        if (TextUtils.isEmpty(idcardNo)) {
            showToast(getString(R.string.toast_empty_idcardno));
            return;
        }
        if (!ValidateUtil.personIdValidation2(idcardNo)) {
            showToast(getString(R.string.toast_error_idcard));
            return;
        }
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
        if (!ValidateUtil.isPwd(pwd)) {
            showToast(getString(R.string.toast_error_pwd));
            return;
        }

        if (TextUtils.isEmpty(repwd)) {
            showToast(getString(R.string.toast_empty_repwd));
            return;
        }
        if (!repwd.equals(pwd)) {
            showToast(getString(R.string.toast_diff_pwd));
            return;
        }

        serverDao.doRegister(username, phone, code, pwd, repwd, idcardNo, distId, new DialogCallback<BaseResponse<UserModel>>(this) {
            @Override
            public void onSuccess(BaseResponse<UserModel> stringBaseResponse, Call call, Response response) {
                saveUsername(RegisterActivity.this, phone);
                saveLoginStatus(RegisterActivity.this, true);
                Gson gson = new Gson();
                String userStr = gson.toJson(stringBaseResponse.retData);
                saveUser(RegisterActivity.this, userStr);
                showToast(stringBaseResponse.message);
                LoginActivity.getInstance().finish();//注册完成即登录状态,将登录页从堆栈里销毁
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
    protected void onPause() {
        super.onPause();
        LogUtils.e("onPause");
        try {
            KeyBoardUtils.closeKeybord(mEtUsername, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e("onDestroy");
        try {
            KeyBoardUtils.closeKeybord(mEtUsername, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialog(String title, List<DistModel> distModels) {
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_loopview, null);
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < distModels.size(); i++) {
            strings.add(distModels.get(i).orgName);
        }
        //初始化控件
        TextView tv_cancel = (TextView) inflate.findViewById(R.id.tv_cancel);
        TextView tv_confirm = (TextView) inflate.findViewById(R.id.tv_confirm);
        TextView tv_title = (TextView) inflate.findViewById(R.id.tv_title);
        tv_title.setText(title);
        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);

        mLoopView = (LoopView) inflate.findViewById(R.id.loopView);
        mLoopView.setItems(strings);
        mLoopView.setNotLoop();
        mLoopView.setCenterTextColor(getResources().getColor(R.color.txt_color));
        mLoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {

            }
        });
        if (mPopupWindow == null)
            mPopupWindow = new PopupWindow(this);
        // 设置视图
        mPopupWindow.setContentView(inflate);
        // 设置弹出窗体的宽和高
        mPopupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        mPopupWindow.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        mPopupWindow.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        mPopupWindow.setAnimationStyle(R.style.popwin_comment_anim);
        mPopupWindow.showAtLocation(mLayoutRoot, Gravity.BOTTOM, ScreenUtils.getScreenWidth(this), 0);
        inflate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mPopupWindow.dismiss();
                return false;
            }
        });
    }

    /**
     * 小区列表
     *
     * @param title
     */
    private void getDist(final String title) {
        serverDao.getDistList(new DialogCallback<BaseResponse<List<DistModel>>>(this) {
            @Override
            public void onSuccess(BaseResponse<List<DistModel>> baseResponse, Call call, Response response) {
                distModels = new ArrayList<>();
                distModels = baseResponse.retData;
                showDialog(title, distModels);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
            }
        });
    }
}

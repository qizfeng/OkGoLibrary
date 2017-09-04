package com.project.community.ui.life.zhengwu;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.library.okgo.callback.DialogCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.KeyBoardUtils;
import com.library.okgo.utils.ValidateUtil;
import com.project.community.R;
import com.project.community.base.BaseActivity;

import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/8/2.
 * 意见
 */

public class SuggestionActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.btn_submit)
    Button mBtnSubmit;
    @Bind(R.id.et_phone)
    EditText mEtPhone;
    @Bind(R.id.et_title)
    EditText mEtTitle;
    @Bind(R.id.et_suggest)
    EditText mEtSuggest;
    @Bind(R.id.tv_txt_count)
    TextView mTvTxtCount;
    @Bind(R.id.layout_root)
    RelativeLayout mLayoutRoot;
    @Bind(R.id.scrollView)
    ScrollView mScrollView;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_suggestion);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.txt_suggestion), R.mipmap.iv_back);
        if (isLogin(this))
            mEtPhone.setText(getUsername(this));
        mEtPhone.setText(getUsername(this));
        mBtnSubmit.setOnClickListener(this);
        mBtnSubmit.setClickable(false);
        mEtSuggest.setOnTouchListener(this);
        mLayoutRoot.setOnTouchListener(this);
        mEtSuggest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 0) {
                    mBtnSubmit.setBackgroundResource(R.drawable.button_round_gray);
//                    mBtnSubmit.setEnabled(false);
                    mBtnSubmit.setClickable(false);
                } else {
                    mBtnSubmit.setBackgroundResource(R.drawable.button_round_blue);
                    mBtnSubmit.setClickable(true);
                }
                mTvTxtCount.setText(charSequence.toString().length() + "/" + 350);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                if(!ValidateUtil.isPhone(mEtPhone.getText().toString())){
                    showToast(getString(R.string.toast_error_phone));
                    return;
                }
                KeyBoardUtils.closeKeybord(mEtSuggest, SuggestionActivity.this);
                submitData();

                break;
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.et_suggest:
                changeScrollView();
                break;
            case R.id.layout_root:
                //点击空白处软键盘隐藏
                KeyBoardUtils.closeKeybord(mEtSuggest, SuggestionActivity.this);
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * 提交
     */
    private void submitData(){
        phone = mEtPhone.getText().toString();
        String title = mEtTitle.getText().toString();
        String content = mEtSuggest.getText().toString();
        serverDao.submitSuggest(phone, title, content, new DialogCallback<BaseResponse<List>>(this) {
            @Override
            public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                showToast(baseResponse.message);
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
        try {
            KeyBoardUtils.closeKeybord(mEtSuggest, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使ScrollView指向底部
     */
    private void changeScrollView() {
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, mScrollView.getHeight());
            }
        }, 300);
    }

    Handler h = new Handler() {
        public void handleMessage(Message msg) {
        }
    };

}

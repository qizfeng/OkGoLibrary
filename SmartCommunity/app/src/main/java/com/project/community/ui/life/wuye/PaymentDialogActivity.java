package com.project.community.ui.life.wuye;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.util.ScreenUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by qizfeng on 17/8/23.
 * 支付
 */

public class PaymentDialogActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.iv_close)
    ImageView mIvClose;
    @Bind(R.id.layout_alipay)
    RelativeLayout mLayoutAlipay;
    @Bind(R.id.layout_weipay)
    RelativeLayout mLayoutWeipay;
    @Bind(R.id.btn_alipay)
    RadioButton mBtnAlipay;
    @Bind(R.id.btn_weipay)
    RadioButton mBtnWeipay;

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, PaymentDialogActivity.class);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_payment);
        //设置Theme.Dialog View高度   在setContentView(id);之后添加以下代码
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
        // p.height = (int) (ScreenUtils.getScreenHeight(this) * 0.4); // 高度设置为屏幕的0.4
        p.width = (int) (ScreenUtils.getScreenWidth(this) * 0.7); // 宽度设置为屏幕的0.7
        getWindow().setAttributes(p);
        mLayoutAlipay.setOnClickListener(this);
        mLayoutWeipay.setOnClickListener(this);
        mBtnAlipay.setEnabled(false);
        mBtnAlipay.setClickable(false);
        mBtnWeipay.setEnabled(false);
        mBtnWeipay.setClickable(false);
        mBtnAlipay.setVisibility(View.VISIBLE);
        mBtnWeipay.setVisibility(View.GONE);
    }

    @OnClick({R.id.btn_cancel, R.id.iv_close})
    public void onCancelClick(View v) {
        this.finish();
    }

    @OnClick(R.id.btn_confirm)
    public void onConfirm(View v) {
        if (mBtnWeipay.isChecked()) {
            showToast(getString(R.string.txt_wei_pay));
        } else if (mBtnAlipay.isChecked()) {
            showToast(getString(R.string.txt_alipay));
        }

        this.finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_alipay:
                mBtnAlipay.setChecked(true);
                mBtnWeipay.setChecked(false);
                mBtnAlipay.setVisibility(View.VISIBLE);
                mBtnWeipay.setVisibility(View.GONE);
                break;
            case R.id.layout_weipay:
                mBtnAlipay.setChecked(false);
                mBtnWeipay.setChecked(true);
                mBtnAlipay.setVisibility(View.GONE);
                mBtnWeipay.setVisibility(View.VISIBLE);
                break;
        }
    }
}

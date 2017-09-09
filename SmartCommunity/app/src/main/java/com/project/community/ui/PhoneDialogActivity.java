package com.project.community.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.okgo.callback.DialogCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.ToastUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.model.HotlineModel;
import com.project.community.util.ScreenUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/8/2.
 * 热线/客服dialogActivity
 */

public class PhoneDialogActivity extends BaseActivity implements View.OnClickListener {
    public static final int REQUEST_PERMISSION_CODE = 123;

    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;

    private static final String[] requestPermissions = {
            PERMISSION_CALL_PHONE,
    };
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.iv_close)
    ImageView mIvClose;
    @Bind(R.id.iv_header)
    ImageView mIvHeader;
    @Bind(R.id.tv_phone_number)
    TextView mTvPhoneNumber;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.btn_call)
    Button mBtnCall;
    public boolean hasHeader = true;
    private String type = "1";//1政务 2物业

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_callphone);
        //设置Theme.Dialog View高度   在setContentView(id);之后添加以下代码
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (int) (ScreenUtils.getScreenWidth(this) * 0.7); // 宽度设置为屏幕的0.7
        getWindow().setAttributes(p);
        mIvClose.setOnClickListener(this);
        mBtnCall.setOnClickListener(this);

        hasHeader = getIntent().getBooleanExtra("hasHeader", true);
        type = getIntent().getStringExtra("type");
        if (hasHeader) {
            mIvHeader.setVisibility(View.VISIBLE);
            mTvPhoneNumber.setGravity(Gravity.CENTER_VERTICAL);
        } else {
            mTvTitle.setText(getString(R.string.txt_hot_line));
            mIvHeader.setVisibility(View.GONE);
            mTvPhoneNumber.setGravity(Gravity.CENTER);
        }

        getData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.btn_call:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mTvPhoneNumber.getText().toString().trim()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(PhoneDialogActivity.this, PERMISSION_CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // 没有权限，申请权限。
                        ActivityCompat.requestPermissions(PhoneDialogActivity.this, requestPermissions, REQUEST_PERMISSION_CODE);
                    } else {
                        // 有权限了，去放肆吧。
                        startActivity(intent);
                        finish();
                    }
                } else {
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (ContextCompat.checkSelfPermission(PhoneDialogActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // 没有权限，申请权限。
                    ToastUtils.showShortToast(PhoneDialogActivity.this, getString(R.string.permission_tips));
                } else {
//                    if (grantResults.length > 0
//                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意，可以去放肆了。
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mTvPhoneNumber.getText().toString().trim()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
//                    } else {
//                        // 权限被用户拒绝了，洗洗睡吧。
//                        ToastUtils.showShortToast(PhoneDialogActivity.this, getString(R.string.permission_tips));
//                    }
                }

                return;
        }
    }

    private void getData() {
        String orgCode = "";
        if (isLogin(this)) {
            orgCode = getUser(this).orgCode;
        }
        serverDao.getHotLine(type, orgCode, new DialogCallback<BaseResponse<List<HotlineModel>>>(this) {
            @Override
            public void onSuccess(BaseResponse<List<HotlineModel>> baseResponse, Call call, Response response) {
                try {
                    List<HotlineModel> data = new ArrayList<>();
                    data = baseResponse.retData;
                    if (hasHeader) {
                        mTvTitle.setText(data.get(0).org_name);
                    }
                    if (data.size() > 0) {
                        mTvPhoneNumber.setText(data.get(0).contact);
                        if (TextUtils.isEmpty(data.get(0).work_start) || TextUtils.isEmpty(data.get(0).work_end))
                            mTvTime.setVisibility(View.GONE);
                        else {
                            mTvTime.setVisibility(View.VISIBLE);
                            mTvTime.setText("(" + getString(R.string.txt_work_time) + data.get(0).work_start + "~" + data.get(0)
                                    .work_end + ")");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
